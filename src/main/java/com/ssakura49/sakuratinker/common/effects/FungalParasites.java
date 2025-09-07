package com.ssakura49.sakuratinker.common.effects;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import slimeknights.tconstruct.tools.modifiers.effect.NoMilkEffect;

import java.util.UUID;

public class FungalParasites extends NoMilkEffect {
    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("77ab7e19-9955-4308-bfca-c01540e1070e");
    private static final UUID ATTACK_MODIFIER_UUID = UUID.fromString("17ab7e19-9955-4308-bfca-c01540e1070e");

    public FungalParasites() {
        super(MobEffectCategory.HARMFUL, 0xdbddc1, true);
        this.addAttributeModifier(
                Attributes.MOVEMENT_SPEED,
                SPEED_MODIFIER_UUID.toString(),
                -0.5D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
        this.addAttributeModifier(
                Attributes.ATTACK_DAMAGE,
                ATTACK_MODIFIER_UUID.toString(),
                -0.4D,
                AttributeModifier.Operation.MULTIPLY_TOTAL
        );
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.tickCount % 40 == 0) {
            entity.hurt(entity.damageSources().magic(), 1.0F);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @SubscribeEvent
    public void onLivingTick(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        if (entity.hasEffect(this) && entity.isOnFire()) {
            int currentFire = entity.getRemainingFireTicks();
            if (currentFire > 0 && currentFire <= 20) {
                entity.setRemainingFireTicks(currentFire * 2);
            }
        }
    }
}
