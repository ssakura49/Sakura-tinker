package com.ssakura49.sakuratinker.common.tinkering.modifiers.special;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.register.STAttributes;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;

public class ScytheSweepingModifier extends BaseModifier {
    private static final UUID id = UUID.fromString("7a62442b-36d3-4137-88ea-93d92b06635a");
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target1 = context.getLivingTarget();
        Level world = attacker.level();
        double sweepRange = 5.0;
        Vec3 lookVec = attacker.getLookAngle();
        Vec3 origin = attacker.position().add(0, attacker.getEyeHeight(), 0);
        List<LivingEntity> targets = world.getEntitiesOfClass(LivingEntity.class,
                attacker.getBoundingBox().inflate(sweepRange),
                e -> {
                    if (e == attacker || e == target1 || !e.isAttackable()) return false;
                    Vec3 toEntity = e.position().subtract(origin).normalize();
                    float angle = (float) Math.toDegrees(Math.acos(lookVec.dot(toEntity)));
                    return angle <= 45.0f && e.distanceTo(attacker) <= sweepRange;
                });
        for (LivingEntity target : targets) {
            float sweepDamage = damageDealt + (0.5f + tool.getStats().get(ToolStats.ATTACK_DAMAGE));
            target.hurt(attacker.damageSources().playerAttack((Player)attacker), sweepDamage);
            target.knockback(0.4F,
                    Mth.sin(attacker.getYRot() * ((float)Math.PI / 180F)),
                    -Mth.cos(attacker.getYRot() * ((float)Math.PI / 180F)));
            if (world instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SWEEP_ATTACK,
                        target.getX(), target.getY() + target.getBbHeight() * 0.5, target.getZ(),
                        5, 0.1, 0.1, 0.1, 0.0);
            }
        }
        if (!targets.isEmpty()) {
            world.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(),
                    SoundEvents.PLAYER_ATTACK_SWEEP, attacker.getSoundSource(),
                    1.0F, 1.0F);
        }
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        consumer.accept(STAttributes.getRealitySuppression(), new AttributeModifier(id,"reality_suppression", 10.0f, AttributeModifier.Operation.ADDITION));
    }
}
