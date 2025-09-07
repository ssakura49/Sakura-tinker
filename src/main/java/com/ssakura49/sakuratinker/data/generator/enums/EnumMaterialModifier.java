package com.ssakura49.sakuratinker.data.generator.enums;

import com.c2h6s.etstlib.register.EtSTLibModifier;
import com.ssakura49.sakuratinker.common.tools.stats.FletchingMaterialStats;
import com.ssakura49.sakuratinker.common.tools.stats.SpellClothMaterialStats;
import com.ssakura49.sakuratinker.compat.Botania.init.BotaniaModifier;
import com.ssakura49.sakuratinker.compat.BuddyCards.BuddyCardCompat;
import com.ssakura49.sakuratinker.compat.DreadSteel.DreadSteelCompat;
import com.ssakura49.sakuratinker.compat.EnigmaticLegacy.ELCompat;
import com.ssakura49.sakuratinker.compat.ExtraBotany.init.ExtraBotanyModifiers;
import com.ssakura49.sakuratinker.compat.Goety.init.GoetyModifiers;
import com.ssakura49.sakuratinker.compat.GoetyRevelation.init.GRModifiers;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.ISSCompat;
import com.ssakura49.sakuratinker.compat.TwilightForest.TFCompat;
import com.ssakura49.sakuratinker.compat.YoukaiHomeComing.YKHCCompat;
import com.ssakura49.sakuratinker.library.tinkering.tools.STMaterialStats;
import com.ssakura49.sakuratinker.register.STModifiers;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierId;
import slimeknights.tconstruct.tools.TinkerModifiers;

public enum EnumMaterialModifier {
    soul_sakura_default(null, entry(STModifiers.Ruination.getId(), 1), entry(STModifiers.Mortal_Wound.getId(), 1), entry(STModifiers.BloodBurn.getId(),1)),
    soul_sakura_armor(MaterialRegistry.ARMOR, entry(STModifiers.Storm_Veil.getId(), 1)),
    soul_sakura_charm(STMaterialStats.CHARM, entry(STModifiers.Greedy.getId(), 1)),

    nihilite_default(null, entry(STModifiers.SoulDevourer.getId(), 1)),
    nihilite_armor(MaterialRegistry.ARMOR, entry(STModifiers.Shielding.getId(), 3)),
    nihilite_charm(STMaterialStats.CHARM, entry(STModifiers.WorldBottom.getId(), 1)),

    eezo_default(null, entry(STModifiers.Dissloving.getId(),1)),
    eezo_charm(STMaterialStats.CHARM, entry(STModifiers.HighFrequencyBarrier.getId(), 1)),

    blood_bound_steel_default(null, entry(STModifiers.Torturing.getId(), 1)),

    steady_alloy_default(null, entry(STModifiers.ReapersBlessing.getId(),1)),
    steady_alloy_armor(MaterialRegistry.ARMOR, entry(STModifiers.Rootedness.getId(), 1)),
    steady_alloy_charm(STMaterialStats.CHARM, entry(STModifiers.SteadyCurio.getId(),1)),

    south_star_default(null, entry(STModifiers.EnchantedBlade.getId(),1)),
    south_star_armor(MaterialRegistry.ARMOR, entry(STModifiers.MagicShield.getId(), 1)),

    terracryst_default(null, entry(STModifiers.HeavyWeight.getId(),1)),
    terracryst_armor(MaterialRegistry.ARMOR, entry(STModifiers.Kobold.getId(), 1)),
    terracryst_charm(STMaterialStats.CHARM, entry(STModifiers.EssenceEarth.getId(),1)),

    prometheum_default(null, entry(STModifiers.DoubleStrike.getId(),1)),
    prometheum_armor(MaterialRegistry.ARMOR, entry(STModifiers.ResilientGuard.getId(),1)),

    orichalcum_default(null, entry(STModifiers.Shielding.getId(),1)),

    aurumos_default(null, entry(STModifiers.SolarBurning.getId(),1),entry(STModifiers.Polishing.getId(), 2)),

    bear_interest_default(null, entry(STModifiers.Rejuvenating.getId(),1)),

    mycelium_slimesteel_default(null, entry(TinkerModifiers.overslime.getId(),1),entry(TinkerModifiers.overworked.getId(),1), entry(STModifiers.FungalParasites.getId(),1)),
    mycelium_slimesteel_armor(MaterialRegistry.ARMOR, entry(TinkerModifiers.overslime.getId(),1),entry(TinkerModifiers.overworked.getId(),1),entry(STModifiers.StrengthenDecomposition.getId(),1)),

    frost_slimesteel_default(null, entry(TinkerModifiers.overslime.getId(),1),entry(TinkerModifiers.overworked.getId(),1), entry(STModifiers.Coagulation.getId(),1)),

    echo_slimesteel_default(null, entry(TinkerModifiers.overslime.getId(),1),entry(TinkerModifiers.overworked.getId(),2),entry(STModifiers.PrecisionStrike.getId(),1),entry(EtSTLibModifier.photosynthesis_guide.getId(),2)),
    echo_slimesteel_armor(MaterialRegistry.ARMOR, entry(TinkerModifiers.overslime.getId(),1),entry(TinkerModifiers.overworked.getId(),2),entry(STModifiers.DangerSense.getId(),1)),

    goozma_default(null, entry(TinkerModifiers.overslime.getId(),1),entry(TinkerModifiers.overworked.getId(),3),entry(STModifiers.GodOfSlime.getId(),1),entry(STModifiers.FusionBurn.getId(),1),entry(STModifiers.IncompleteTransformation.getId(),1)),
    goozma_armor(MaterialRegistry.ARMOR, entry(TinkerModifiers.overslime.getId(),1),entry(TinkerModifiers.overworked.getId(),3),entry(STModifiers.MucilaginousShell.getId(),1),entry(STModifiers.IncompleteTransformation.getId(),1)),

    youkai_default(null, entry(STModifiers.Nether_Ghost.getId(),1)),
    youkai_armor(MaterialRegistry.ARMOR, entry(YKHCCompat.Youkaified.getId(), 1)),
    youkai_charm(STMaterialStats.CHARM, entry(STModifiers.JinxDoll.getId(),1)),

    fiery_crystal_default(null, entry(TFCompat.TwilightSparkle.getId(),1),entry(TFCompat.TwilightRemembrance.getId(), 1)),
    fiery_crystal_armor(MaterialRegistry.ARMOR, entry(TFCompat.ArmorTwilightRemembrance.getId(), 1)),

    fairy_ice_crystal_default(null, entry(YKHCCompat.DanmakuBall.getId(),1)),

    etherium_default(null, entry(STModifiers.Void.getId(),2)),
    etherium_armor(MaterialRegistry.ARMOR, entry(STModifiers.Celestial.getId(), 1)),
    etherium_charm(STMaterialStats.CHARM, entry(ELCompat.EtherProtect.getId(),1)),

    neutron_default(null, entry(STModifiers.NeutronCondense.getId(),1)),

    colorful_default(null, entry(STModifiers.PainResonance.getId(),1),entry(STModifiers.Infinitum.getId(),1)),
    colorful_armor(MaterialRegistry.ARMOR, entry(STModifiers.Absorption.getId(), 1),entry(STModifiers.Infinitum.getId(),1)),
    colorful_charm(STMaterialStats.CHARM, entry(STModifiers.Colorization.getId(),1)),

    arcane_alloy_armor(MaterialRegistry.ARMOR, entry(ISSCompat.Magician.getId(), 1),entry(ISSCompat.FountainMagic.getId(),1),entry(ISSCompat.ElementalMastery.getId(),1)),
    arcane_alloy_cloth(SpellClothMaterialStats.ID, entry(STModifiers.SpellPower.getId(),1)),

    orichalcos_default(null,entry(ExtraBotanyModifiers.Body.getId(),1),entry(STModifiers.Mind.getId(),1),entry(STModifiers.Soul.getId(),1)),

    raven_feather_fletching(FletchingMaterialStats.ID,entry(STModifiers.Feather.getId(),1)),

    pyrothium_armor(MaterialRegistry.ARMOR,entry(STModifiers.HydraWill.getId(),1)),

    dread_steel_default(null, entry(DreadSteelCompat.DreadfulScythe.getId(),1),entry(STModifiers.DragonMartyr.getId(),3)),
    dread_steel_armor(MaterialRegistry.ARMOR, entry(DreadSteelCompat.Fear.getId(), 1),entry(STModifiers.BreathResistance.getId(),3)),

    cursed_metal_default(null, entry(GoetyModifiers.DevourSoul.getId(),2)),
    cursed_metal_armor(MaterialRegistry.ARMOR, entry(GoetyModifiers.SoulSeeker.getId(), 1)),

    dark_metal_default(null, entry(GoetyModifiers.DevourSoul.getId(),1),entry(GoetyModifiers.SoulIntake.getId(),1)),
    dark_metal_armor(MaterialRegistry.ARMOR, entry(GoetyModifiers.SoulSeeker.getId(), 1),entry(GoetyModifiers.SoulIntake.getId(),1)),

    unholy_alloy_default(null, entry(GoetyModifiers.DevourSoul.getId(),2),entry(GoetyModifiers.SoulIntake.getId(),1),entry(STModifiers.ExceptionallyTalented.getId(),1),entry(GoetyModifiers.SoulErosion.getId(),1)),

    mana_steel_default(null,entry(EtSTLibModifier.EtSTLibModifierBOT.mana_repair.getId(),1)),

    terra_steel_default(null,entry(EtSTLibModifier.EtSTLibModifierBOT.mana_repair.getId(),2),entry(BotaniaModifier.ManaRay.getId(),2)),
    terra_steel_armor(MaterialRegistry.ARMOR,entry(EtSTLibModifier.EtSTLibModifierBOT.mana_repair.getId(),2),entry(BotaniaModifier.TerraMagical.getId(),1),entry(STModifiers.Natural_Will.getId(),1)),

    elementium_default(null,entry(EtSTLibModifier.EtSTLibModifierBOT.mana_repair.getId(),2),entry(BotaniaModifier.Pixie.getId(),1)),
    elementium_armor(MaterialRegistry.ARMOR,entry(EtSTLibModifier.EtSTLibModifierBOT.mana_repair.getId(),2),entry(BotaniaModifier.PixieArmor.getId(),1)),

    gaia_default(null,entry(EtSTLibModifier.EtSTLibModifierBOT.mana_repair.getId(),2), entry(BotaniaModifier.GaiaWrath.getId(),1),entry(BotaniaModifier.GaiaSoul.getId(),1),entry(BotaniaModifier.ManaRay.getId(),3)),
    gaia_armor(MaterialRegistry.ARMOR,entry(EtSTLibModifier.EtSTLibModifierBOT.mana_repair.getId(),2),entry(BotaniaModifier.GaiaGuardian.getId(),1),entry(BotaniaModifier.TerraMagical.getId(),2),entry(STModifiers.Natural_Will.getId(),1)),

    amphithere_feather_default(null,entry(STModifiers.DoubleStrike.getId(),1)),

    chimera_gamma_default(null,entry(STModifiers.HyperBurst.getId(),1)),
    chimera_gamma_armor(MaterialRegistry.ARMOR, entry(STModifiers.IngaRyu.getId(),1)),

    dragon_sinew_range(MaterialRegistry.RANGED, entry(STModifiers.CrackShot.getId(),1)),

    buddysteel_default(null, entry(BuddyCardCompat.BuddyCardMaster.getId(),1)),
    buddysteel_armor(MaterialRegistry.ARMOR, entry(BuddyCardCompat.BuddyCardMasterArmor.getId(),1)),
    charged_buddysteel_default(null, entry(BuddyCardCompat.BuddyCardMaster.getId(),1)),
    charged_buddysteel_armor(MaterialRegistry.ARMOR, entry(BuddyCardCompat.BuddyCardMasterArmor.getId(),1)),
    crimson_buddysteel_default(null, entry(BuddyCardCompat.BuddyCardMaster.getId(),1)),
    crimson_buddysteel_armor(MaterialRegistry.ARMOR, entry(BuddyCardCompat.BuddyCardMasterArmor.getId(),1)),
    void_buddysteel_default(null, entry(BuddyCardCompat.BuddyCardMaster.getId(),1)),
    void_buddysteel_armor(MaterialRegistry.ARMOR, entry(BuddyCardCompat.BuddyCardMasterArmor.getId(),1)),
    perfect_buddysteel_default(null, entry(BuddyCardCompat.BuddyCardMaster.getId(),2)),
    perfect_buddysteel_armor(MaterialRegistry.ARMOR, entry(BuddyCardCompat.BuddyCardMasterArmor.getId(),2)),
    true_perfect_buddysteel_default(null, entry(BuddyCardCompat.BuddyCardMaster.getId(),2),entry(BuddyCardCompat.Perfectionism.getId())),
    true_perfect_buddysteel_armor(MaterialRegistry.ARMOR, entry(BuddyCardCompat.BuddyCardMasterArmor.getId(),2),entry(BuddyCardCompat.Perfectionism.getId())),

    mana_string_bowstring(null, entry(BotaniaModifier.ManaArrow.getId(),1)),

    living_wood(null, entry(BotaniaModifier.ManaTransform.getId(),1)),

    living_rock(null, entry(BotaniaModifier.ManaTransform.getId(),1)),

    aerialite_default(null,entry(STModifiers.BreakSpeed.getId(),1)),
    aerialite_armor(MaterialRegistry.ARMOR, entry(STModifiers.Agile.getId(),1)),

    shadowium_default(null,entry(STModifiers.Relentless.getId(),1)),
    shadowium_armor(MaterialRegistry.ARMOR, entry(ExtraBotanyModifiers.ShadowEvasion.getId(),1)),

    photonium_default(null,entry(ExtraBotanyModifiers.LightEndurance.getId(),1)),
    photonium_armor(MaterialRegistry.ARMOR, entry(ExtraBotanyModifiers.BornofLight.getId(),1)),

    the_end(null, entry(STModifiers.Hollow.getId(),1)),

    delusion_default(null,entry(STModifiers.HeavenHasFallen.getId(),1)),

    gluttonous_default(null,entry(STModifiers.Treasure.getId(),1)),

    cold_iron_alloy_default(null, entry((STModifiers.Relentless.getId()),1)),

    paper_default(null, entry(STModifiers.Engraving.getId(),1)),

    nefarious_default(null,entry(ELCompat.UltraHostility.getId(),1)),
    nefarious_armor(MaterialRegistry.ARMOR, entry(ELCompat.UltraHostility.getId(),1)),


//    apocalyptium_default(null,entry(GoetyModifiers.SoulErosion.getId(),1),entry(GRModifiers.RealityPiercer.getId(),1)),
//    apocalyptium_armor(MaterialRegistry.ARMOR, entry(GRModifiers.Apocalyptium.getId(),1)),


    ;
    public final ModifierEntry[] modifiers;
    public final MaterialStatsId statType;
    EnumMaterialModifier(MaterialStatsId statType, ModifierEntry... modifiers){
        this.modifiers = modifiers;
        this.statType = statType;
    }
    public static ModifierEntry entry(ModifierId id, int level){
        return new ModifierEntry(id,level);
    }
    public static ModifierEntry entry(ModifierId id){
        return new ModifierEntry(id,1);
    }
}
