package com.ssakura49.sakuratinker.content.effects;

import com.ssakura49.sakuratinker.STConfig;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.tools.modifiers.effect.NoMilkEffect;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

public class TortureEffect extends NoMilkEffect {
    private static final Map<UUID, Vec3> LAST_POSITIONS = new HashMap<>();
    private static final Map<UUID, Double> ACCUMULATED_DISTANCE = new HashMap<>();

    private final Supplier<Double> baseDamageThreshold = () -> STConfig.COMMON.TORTURE_BASE_DAMAGE_THRESHOLD.get();
    private final Supplier<Double> damageMultiplier = () -> STConfig.COMMON.TORTURE_DAMAGE_MULTIPLIER.get();

    public TortureEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF0000,true);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) {
            return;
        }
        UUID uuid = entity.getUUID();
        Vec3 currentPos = entity.position();
        Vec3 lastPos = LAST_POSITIONS.computeIfAbsent(uuid, k -> currentPos);
        double distanceMoved = currentPos.distanceTo(lastPos);
        double accumulated = ACCUMULATED_DISTANCE.getOrDefault(uuid, 0.0) + distanceMoved;
        double actualThreshold = calculateDamageThreshold(amplifier);
        if (accumulated >= actualThreshold) {
            entity.hurt(entity.damageSources().magic(), calculateDamageAmount(accumulated, actualThreshold, amplifier));
            accumulated %= actualThreshold;
        }
        ACCUMULATED_DISTANCE.put(uuid, accumulated);
        LAST_POSITIONS.put(uuid, currentPos);
    }

    protected double calculateDamageThreshold(int amplifier) {
        double currentThreshold = baseDamageThreshold.get();
        double currentMultiplier = damageMultiplier.get();
        return currentThreshold / (1 + (amplifier * currentMultiplier));
    }

    protected int calculateDamageAmount(double accumulatedDistance, double threshold, int amplifier) {
        double baseDamage = 1.0 + (amplifier * 0.5);
        double multiplier = Math.floor(accumulatedDistance / threshold);
        return (int) (baseDamage * multiplier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributes, int amplifier) {
        super.removeAttributeModifiers(entity, attributes, amplifier);
        UUID uuid = entity.getUUID();
        LAST_POSITIONS.remove(uuid);
        ACCUMULATED_DISTANCE.remove(uuid);
    }
}
