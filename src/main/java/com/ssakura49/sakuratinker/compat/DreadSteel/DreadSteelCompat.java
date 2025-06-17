package com.ssakura49.sakuratinker.compat.DreadSteel;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.DreadSteel.modifiers.DreadfulScytheModifier;
import com.ssakura49.sakuratinker.compat.DreadSteel.modifiers.FearModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class DreadSteelCompat {
    public static ModifierDeferredRegister DE_MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);

    public static StaticModifier<DreadfulScytheModifier> DreadfulScythe;
    public static StaticModifier<FearModifier> Fear;

    static {
        DreadfulScythe = DE_MODIFIERS.register("dreadful_scythe", DreadfulScytheModifier::new);
        Fear = DE_MODIFIERS.register("fear", FearModifier::new);
    }
}
