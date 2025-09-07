package com.ssakura49.sakuratinker.common.effects;

import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.tools.modifiers.effect.NoMilkEffect;

public class BloodBurnEffect extends NoMilkEffect {
    private static final float LOW_HEALTH = 6.0f;

    public BloodBurnEffect() {
        super(MobEffectCategory.NEUTRAL, 0x8B0000, true);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.level().isClientSide) return;

        if (entity instanceof Player player) {
            float max = player.getMaxHealth();
            float damage = 0.05f * max * amplifier;
            if (player.getHealth() - damage <= LOW_HEALTH) {
                player.removeEffect(this);
                return;
            }
            LegacyDamageSource source = new LegacyDamageSource(entity.damageSources().thorns(entity)).setBypassArmor();
            player.hurt(source, damage);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
