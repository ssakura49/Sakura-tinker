package com.ssakura49.sakuratinker.compat.IronSpellBooks;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.modifiers.ElementalMasteryModifier;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.modifiers.FountainMagicModifier;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.modifiers.MagicianModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class ISSCompat {
    public static ModifierDeferredRegister ISS_MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);

    public static final StaticModifier<MagicianModifier> Magician = ISS_MODIFIERS.register("magician", MagicianModifier::new);
    public static final StaticModifier<FountainMagicModifier> FountainMagic = ISS_MODIFIERS.register("fountain_magic", FountainMagicModifier::new);
    public static final StaticModifier<ElementalMasteryModifier> ElementalMastery = ISS_MODIFIERS.register("elemental_mastery", ElementalMasteryModifier::new);

}
