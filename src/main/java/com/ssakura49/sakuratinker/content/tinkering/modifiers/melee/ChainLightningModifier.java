package com.ssakura49.sakuratinker.content.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class ChainLightningModifier extends BaseModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        Level world = attacker.level();
        if (world.isClientSide || target == null) return;
        double radius = 6.0D;
        AABB area = target.getBoundingBox().inflate(radius);
        List<LivingEntity> nearby = world.getEntitiesOfClass(LivingEntity.class, area,
                entity -> entity != attacker && entity != target && entity.isAlive() && attacker.canAttack(entity));
        int maxChains = 3;
        int chainCount = 0;
        for (LivingEntity chained : nearby) {
            if (world instanceof ServerLevel serverLevel) {
                spawnLightningArc(serverLevel, target, chained);
            }
            if (attacker instanceof Player player) {
                float chainDamage = damageDealt * 0.5f;
                DamageSource lightningSource = new DamageSource(target.getCommandSenderWorld().registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.LIGHTNING_BOLT), player);
                chained.hurt(lightningSource, chainDamage);
//                world.playSound(null, chained.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.PLAYERS, 0.5F, 1.2F + world.random.nextFloat() * 0.3F);
                chainCount++;
                if (chainCount >= maxChains) break;
            }
        }
    }

    public static void spawnLightningArc(ServerLevel level, LivingEntity from, LivingEntity to) {
        int steps = 10;
        double dx = (to.getX() - from.getX()) / steps;
        double dy = (to.getY() + to.getBbHeight() / 2 - (from.getY() + from.getBbHeight() / 2)) / steps;
        double dz = (to.getZ() - from.getZ()) / steps;
        for (int i = 0; i <= steps; i++) {
            double px = from.getX() + dx * i;
            double py = from.getY() + from.getBbHeight() / 2 + dy * i;
            double pz = from.getZ() + dz * i;
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK,
                    px, py, pz,
                    1, 0.01, 0.01, 0.01, 0.0);
        }

    }
}
