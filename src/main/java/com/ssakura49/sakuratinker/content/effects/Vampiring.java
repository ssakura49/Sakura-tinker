package com.ssakura49.sakuratinker.content.effects;

import net.minecraft.world.effect.MobEffectCategory;
import slimeknights.tconstruct.tools.modifiers.effect.NoMilkEffect;

public class Vampiring extends NoMilkEffect {
    public Vampiring() {
        super(MobEffectCategory.HARMFUL, 0x8B0000, true);
    }
}
