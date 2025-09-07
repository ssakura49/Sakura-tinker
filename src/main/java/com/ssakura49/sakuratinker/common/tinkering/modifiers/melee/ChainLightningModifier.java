package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import com.ssakura49.sakuratinker.network.PacketHandler;
import com.ssakura49.sakuratinker.network.s2c.SLightningPacket;
import com.ssakura49.sakuratinker.register.STSounds;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;

public class ChainLightningModifier extends BaseModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        Level world = attacker.level();
        if (world.isClientSide || target == null) return;

        int level = modifier.getLevel();
        double radius = 5.0D * level; // 每级 +5 格
        int maxChains = 5 * level;    // 每级 +5 个目标

        // 查找范围内的实体
        AABB area = target.getBoundingBox().inflate(radius);
        List<LivingEntity> nearby = world.getEntitiesOfClass(LivingEntity.class, area,
                entity -> entity != attacker && entity != target && entity.isAlive() && attacker.canAttack(entity) && target instanceof Monster);

        // 连锁闪电
        int chainCount = 0;
        for (LivingEntity chained : nearby) {
            if (world instanceof ServerLevel serverLevel) {
                // 渲染闪电弧
                sendLightningArc(attacker, target, chained);

                if (attacker instanceof Player player) {
                    LegacyDamageSource source = LegacyDamageSource.playerAttack(player).setLightning();
                    chained.hurt(source, 10 * modifier.getLevel());
                }
            }
            chainCount++;
            if (chainCount >= maxChains) break;
        }

        // 在目标位置范围内随机生成8个纯视觉闪电
        if (world instanceof ServerLevel serverLevel) {
            spawnRandomVisualLightnings(target, radius, 8);
        }
    }

    /** 发送两个实体之间的闪电弧特效 */
    private void sendLightningArc(LivingEntity attacker, LivingEntity from, LivingEntity to) {
        Vec3 start = from.getEyePosition();
        Vec3 end = to.getEyePosition();
        PacketHandler.sendToAll(new SLightningPacket(start, end, 5));
        from.level().playSound(null, to.blockPosition(), STSounds.ZAP.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
    }

    /** 在目标位置附近随机生成 count 个纯视觉闪电 */
    private void spawnRandomVisualLightnings(LivingEntity centerEntity, double radius, int count) {
        Vec3 center = centerEntity.position();
        Level world = centerEntity.level();

        for (int i = 0; i < count; i++) {
            // 随机生成球体内的一个点
            double theta = world.random.nextDouble() * 2 * Math.PI; // 水平角
            double phi = Math.acos(2 * world.random.nextDouble() - 1); // 垂直角
            double r = world.random.nextDouble() * radius; // 半径

            double x = center.x + r * Math.sin(phi) * Math.cos(theta);
            double y = center.y + r * Math.sin(phi) * Math.sin(theta);
            double z = center.z + r * Math.cos(phi);

            Vec3 randomPos = new Vec3(x, y, z);

            // 起点用实体位置
            Vec3 start = centerEntity.getEyePosition();
            PacketHandler.sendToAll(new SLightningPacket(start, randomPos, 5));
        }
    }
}
