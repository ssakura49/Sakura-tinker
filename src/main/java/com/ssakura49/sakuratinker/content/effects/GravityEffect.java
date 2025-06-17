package com.ssakura49.sakuratinker.content.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.tools.modifiers.effect.NoMilkEffect;

public class GravityEffect extends NoMilkEffect {
    private static final String GRAVITY_UUID = "16fd3709-41ba-4456-8da6-9f1eedd48af4";

    public GravityEffect() {
        super(MobEffectCategory.HARMFUL, 0x4B0082, true); // 紫色效果
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, GRAVITY_UUID, -0.15F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.fallDistance > 0) {
            entity.hurt(entity.damageSources().fall(), entity.fallDistance * 0.5F * (amplifier + 1));
            entity.fallDistance = 0;
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
