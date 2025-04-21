package com.ssakura49.sakuratinker.compat.IceAndFireCompat;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.IceAndFireCompat.modifiers.*;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class IAFCompat {
    public static ModifierDeferredRegister IAF_MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);

    public static StaticModifier<DragonMartyrModifier> DragonMartyr;
    public static StaticModifier<BreathResistanceModifier> BreathResistance;

    static {
        DragonMartyr = IAF_MODIFIERS.register("dragon_martyr", DragonMartyrModifier::new);
        BreathResistance = IAF_MODIFIERS.register("breath_resistance", BreathResistanceModifier::new);
    }
}
