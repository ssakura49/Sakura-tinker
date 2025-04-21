package com.ssakura49.sakuratinker.content.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class MortalWoundEffect extends MobEffect {
    public MortalWoundEffect() {
        super(MobEffectCategory.HARMFUL, 0xFF0000);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
