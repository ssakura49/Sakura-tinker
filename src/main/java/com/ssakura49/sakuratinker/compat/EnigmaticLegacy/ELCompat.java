package com.ssakura49.sakuratinker.compat.EnigmaticLegacy;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.EnigmaticLegacy.spellstone.*;
import com.ssakura49.sakuratinker.content.tinkering.modifiers.curio.EtherProtectModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;


public class ELCompat {
    public static ModifierDeferredRegister EL_MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);


    public static StaticModifier<EtherProtectModifier> EtherProtect;
    public static StaticModifier<GolemHeartModifier> GolemHeart;
    public static StaticModifier<BlazingCoreModifier> BlazingCore;
    public static StaticModifier<AngelBlessingModifier> AngelBlessing;
    public static StaticModifier<WillOfOceanModifier> WillOfOcean;
    public static StaticModifier<VoidPearlModifier> VoidPearl;

    static {
        EtherProtect = EL_MODIFIERS.register("ether_protect", EtherProtectModifier::new);
        GolemHeart = EL_MODIFIERS.register("golem_heart", GolemHeartModifier::new);
        BlazingCore = EL_MODIFIERS.register("blazing_core", BlazingCoreModifier::new);
        AngelBlessing = EL_MODIFIERS.register("angel_blessing", AngelBlessingModifier::new);
        WillOfOcean = EL_MODIFIERS.register("will_of_ocean", WillOfOceanModifier::new);
        VoidPearl = EL_MODIFIERS.register("void_pearl", VoidPearlModifier::new);
    }
}
