package com.ssakura49.sakuratinker.compat.ExtraBotany.init;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.ExtraBotany.modifiers.*;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class ExtraBotanyModifiers {
    public static ModifierDeferredRegister EXBOT_MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);

    public static StaticModifier<BodyModifier> Body;
    public static StaticModifier<FirstFractalModifier> FirstFractal;
    public static StaticModifier<ShadowEvasionModifier> ShadowEvasion;
    public static StaticModifier<BornofLightModifier> BornofLight;
    public static StaticModifier<LightEnduranceModifier> LightEndurance;

    static {
        Body = EXBOT_MODIFIERS.register("body", BodyModifier::new);
        FirstFractal = EXBOT_MODIFIERS.register("first_fractal", FirstFractalModifier::new);
        ShadowEvasion = EXBOT_MODIFIERS.register("shadow_evasion",ShadowEvasionModifier::new);
        BornofLight = EXBOT_MODIFIERS.register("born_of_light",BornofLightModifier::new);
        LightEndurance = EXBOT_MODIFIERS.register("light_endurance",LightEnduranceModifier::new);
    }
}
