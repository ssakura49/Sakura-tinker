package com.ssakura49.sakuratinker.content.effects;

import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.tools.modifiers.effect.NoMilkEffect;

public class FusionBurnEffect extends NoMilkEffect {
    private static LegacyDamageSource FUSION_BURN_DAMAGE_SOURCE = null;

    private static final int BASE_INTERVAL = 20;
    private static final int MIN_INTERVAL = 1;

    public FusionBurnEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF4500, true);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        int interval = calculateInterval(amplifier);
        return duration % interval == 0;
    }

    private int calculateInterval(int amplifier) {
        int interval = BASE_INTERVAL;
        for (int i = 0; i < amplifier; i++) {
            interval /= 2;
            if (interval <= MIN_INTERVAL) {
                return MIN_INTERVAL;
            }
        }
        return interval;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (amplifier > 5) {
            amplifier = 5;
        }
        if (entity.level().isClientSide) return;
        if (FUSION_BURN_DAMAGE_SOURCE == null) {
            FUSION_BURN_DAMAGE_SOURCE = new LegacyDamageSource(entity.damageSources().generic())
                    .setBypassInvulnerableTime()
                    .setMsgId("fusion_burn");
//            FUSION_BURN_DAMAGE_SOURCE = LegacyDamageSource.overLoad(entity);
        }
//        float damage = 1.0f + (amplifier * 0.5f);
        float damage = (float)Math.pow(3, amplifier + 1);
        if (!(entity instanceof Player player) || !player.isCreative()) {
            entity.hurt(FUSION_BURN_DAMAGE_SOURCE, damage);
        }
        spawnParticles(entity);
    }

    private void spawnParticles(LivingEntity entity) {
        Level level = entity.level();
        RandomSource random = level.random;

        for (int i = 0; i < 5; i++) {
            level.addParticle(ParticleTypes.LAVA,
                    entity.getX() + (random.nextDouble() - 0.5) * entity.getBbWidth(),
                    entity.getY() + random.nextDouble() * entity.getBbHeight(),
                    entity.getZ() + (random.nextDouble() - 0.5) * entity.getBbWidth(),
                    0, 0.1, 0);
        }
    }
}
