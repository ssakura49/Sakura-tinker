package com.ssakura49.sakuratinker.compat.TwilightForest;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.tinkering.modifiers.melee.TwilightRemembranceModifier;
import com.ssakura49.sakuratinker.content.tinkering.modifiers.melee.TwilightSparkleModifier;
import com.ssakura49.sakuratinker.content.tinkering.modifiers.armor.ArmorTwilightRemembranceModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class TFCompat {
    public static ModifierDeferredRegister TF_MODIFIERS = ModifierDeferredRegister.create(SakuraTinker.MODID);

    public static StaticModifier<TwilightSparkleModifier> TwilightSparkle = TF_MODIFIERS.register("twilight_sparkle", TwilightSparkleModifier::new);
    public static StaticModifier<ArmorTwilightRemembranceModifier> ArmorTwilightRemembrance = TF_MODIFIERS.register("armor_twilight_remembrance", ArmorTwilightRemembranceModifier::new);
    public static StaticModifier<TwilightRemembranceModifier> TwilightRemembrance = TF_MODIFIERS.register("twilight_remembrance", TwilightRemembranceModifier::new);

}
