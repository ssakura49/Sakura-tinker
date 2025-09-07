package com.ssakura49.sakuratinker.compat.GoetyRevelation.init;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.GoetyRevelation.modifiers.ApocalyptiumModifier;
import com.ssakura49.sakuratinker.compat.GoetyRevelation.modifiers.RealityPiercerModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class GRModifiers {
    public static ModifierDeferredRegister MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);

    public static StaticModifier<ApocalyptiumModifier> Apocalyptium;
    public static StaticModifier<RealityPiercerModifier> RealityPiercer;

    static {
        Apocalyptium = MODIFIERS.register("apocalyptium", ApocalyptiumModifier::new);
        RealityPiercer = MODIFIERS.register("reality_piercer",RealityPiercerModifier::new);
    }
}
