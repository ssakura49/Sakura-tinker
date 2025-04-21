package com.ssakura49.sakuratinker.content.effects;

import net.minecraft.world.effect.MobEffectCategory;
import slimeknights.tconstruct.tools.modifiers.effect.NoMilkEffect;

public class ComboEffect extends NoMilkEffect {
    public ComboEffect() {
        super(MobEffectCategory.HARMFUL, 0x000000, false);
    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
