package com.ssakura49.sakuratinker.data.generator.enums;

import com.ssakura49.sakuratinker.data.generator.STMaterialId;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

import static com.ssakura49.sakuratinker.data.generator.enums.EnumMaterialModifier.*;
import static com.ssakura49.sakuratinker.utils.SafeClassUtil.Modid.*;

public enum EnumMaterial {
    soul_sakura(STMaterialId.soul_sakura, 4, false, false, EnumMaterialStats.soul_sakura, null, soul_sakura_default, soul_sakura_armor, soul_sakura_charm),
    nihilite(STMaterialId.nihilite, 4, false, false, EnumMaterialStats.nihilite, null, nihilite_default, nihilite_armor, nihilite_charm),
    eezo(STMaterialId.eezo, 4, false, false, EnumMaterialStats.eezo, null, eezo_default, eezo_charm),
    blood_bound_steel(STMaterialId.blood_bound_steel, 3, false, false, EnumMaterialStats.blood_bound_steel, null, blood_bound_steel_default),
    steady_alloy(STMaterialId.steady_alloy, 4, false,false,EnumMaterialStats.steady_alloy, null, steady_alloy_default,steady_alloy_armor,steady_alloy_charm),
    terracryst(STMaterialId.terracryst, 4, false, false, EnumMaterialStats.terracryst,null,terracryst_default,terracryst_armor,terracryst_charm),
    prometheum(STMaterialId.prometheum, 4,false,false,EnumMaterialStats.prometheum,null,prometheum_default,prometheum_armor),
    orichalcum(STMaterialId.orichalcum,3,false,false,EnumMaterialStats.orichalcum,null,orichalcum_default),
    aurumos(STMaterialId.aurumos,4,false,false,EnumMaterialStats.aurumos,null,aurumos_default),
    bear_interest(STMaterialId.bear_interest,4,false,false,EnumMaterialStats.bear_interest,null,bear_interest_default),
    south_star(STMaterialId.south_star,4,false,false,EnumMaterialStats.south_star,modLoaded(ISS),south_star_default,south_star_armor),
    mycelium_slimesteel(STMaterialId.mycelium_slimesteel,4,false,false,EnumMaterialStats.mycelium_slimesteel,null,mycelium_slimesteel_default,mycelium_slimesteel_armor),
    frost_slimesteel(STMaterialId.frost_slimesteel,4,false,false,EnumMaterialStats.frost_slimesteel,null,frost_slimesteel_default),
    echo_slimesteel(STMaterialId.echo_slimesteel,4,false,false,EnumMaterialStats.echo_slimesteel,null,echo_slimesteel_default,echo_slimesteel_armor),
//    goozma(STMaterialId.goozma,7,false,false,EnumMaterialStats.goozma,null,goozma_default,goozma_armor),

    youkai(STMaterialId.YoukaiHomeComing.youkai, 2,false,false,EnumMaterialStats.youkai,modLoaded(YHKC),youkai_default,youkai_armor,youkai_charm),
    fairy_ice_crystal(STMaterialId.YoukaiHomeComing.fairy_ice_crystal,1,true,false,EnumMaterialStats.fairy_ice_crystal,modLoaded(YHKC),fairy_ice_crystal_default),

    fiery_crystal(STMaterialId.TwilightForest.fiery_crystal,4,false,false,EnumMaterialStats.fiery_crystal,modLoaded(TF),fiery_crystal_default,fiery_crystal_armor),
    raven_feather(STMaterialId.TwilightForest.raven_feather,1,true,false,EnumMaterialStats.raven_feather,modLoaded(TF),raven_feather_fletching),

    etherium(STMaterialId.EnigmaticLegacy.etherium,4,false,false,EnumMaterialStats.etherium,modLoaded(EnigmaticLegacy),etherium_default,etherium_armor,etherium_charm),

    neutron(STMaterialId.ReAvaritia.neutron,4,false,false,EnumMaterialStats.neutron,modLoaded(Avaritia),neutron_default),
//    colorful(STMaterialId.ReAvaritia.colorful,7,false,false,EnumMaterialStats.colorful,null,colorful_default,colorful_armor,colorful_charm),

    arcane_alloy(STMaterialId.IronSpellBook.arcane_alloy,4,false,false,EnumMaterialStats.arcane_alloy,modLoaded(ISS),arcane_alloy_armor,arcane_alloy_cloth),

    orichalcos(STMaterialId.ExtraBotany.orichalcos,4,false,false,EnumMaterialStats.orichalcos,modLoaded(ExtraBotany),orichalcos_default),

    pyrothium(STMaterialId.ClouderTinker.pyrothium,4,false,false,EnumMaterialStats.pyrothium,modLoaded(ClouderTinker),pyrothium_armor),

    dread_steel(STMaterialId.DreadSteel.dread_steel,4,false,false,EnumMaterialStats.dread_steel,modLoaded(DreadSteel),dread_steel_default,dread_steel_armor),

    cursed_metal(STMaterialId.Goety.cursed_metal,2,false,false,EnumMaterialStats.cursed_metal,modLoaded(Goety),cursed_metal_default,cursed_metal_armor),

    dark_metal(STMaterialId.Goety.dark_metal,3,false,false,EnumMaterialStats.dark_metal,modLoaded(Goety),dark_metal_default,dark_metal_armor),

    unholy_alloy(STMaterialId.Goety.unholy_alloy,4,false,false,EnumMaterialStats.unholy_alloy,modLoaded(Goety),unholy_alloy_default),

    mana_steel(STMaterialId.Botania.mana_steel,2,false,false,EnumMaterialStats.mana_steel,modLoaded(Botania),mana_steel_default),

    terra_steel(STMaterialId.Botania.terra_steel,4,false,false,EnumMaterialStats.terra_steel,modLoaded(Botania),terra_steel_default,terra_steel_armor),

    elementium(STMaterialId.Botania.elementium,3,false,false,EnumMaterialStats.elementium,modLoaded(Botania),elementium_default,elementium_armor),

    gaia(STMaterialId.Botania.gaia,4,false,false,EnumMaterialStats.gaia,modLoaded(Botania),gaia_default,gaia_armor),

    amphithere_feather(STMaterialId.IceAndFire.amphithere_feather,3,true,false,EnumMaterialStats.amphithere_feather,modLoaded(IceAndFire),amphithere_feather_default),

    chimera_gamma(STMaterialId.chimera_gamma,4,false,false,EnumMaterialStats.chimera_gamma,null,chimera_gamma_default,chimera_gamma_armor),

    dragon_sinew(STMaterialId.dragon_sinew,4,true,false,EnumMaterialStats.dragon_sinew,null,dragon_sinew_range),

    buddysteel(STMaterialId.BuddyCard.buddysteel,4,false,false,EnumMaterialStats.buddysteel,modLoaded(BuddyCards),buddysteel_default,buddysteel_armor),
    charged_buddysteel(STMaterialId.BuddyCard.charged_buddysteel,4,false,false,EnumMaterialStats.charged_buddysteel,modLoaded(BuddyCards),charged_buddysteel_default,charged_buddysteel_armor),
    crimson_buddysteel(STMaterialId.BuddyCard.crimson_buddysteel,4,false,false,EnumMaterialStats.crimson_buddysteel,modLoaded(BuddyCards),crimson_buddysteel_default,crimson_buddysteel_armor),
    void_buddysteel(STMaterialId.BuddyCard.void_buddysteel,4,false,false,EnumMaterialStats.void_buddysteel,modLoaded(BuddyCards),void_buddysteel_default,void_buddysteel_armor),
    perfect_buddysteel(STMaterialId.BuddyCard.perfect_buddysteel,4,false,false,EnumMaterialStats.perfect_buddysteel,modLoaded(BuddyCards),perfect_buddysteel_default,perfect_buddysteel_armor),
    true_perfect_buddysteel(STMaterialId.BuddyCard.true_perfect_buddysteel,5,false,false,EnumMaterialStats.perfect_buddysteel,modLoaded(BuddyCards),true_perfect_buddysteel_default,true_perfect_buddysteel_armor),

    mana_string(STMaterialId.Botania.mana_string,1,true,false,EnumMaterialStats.mana_string, modLoaded(Botania),mana_string_bowstring),

    living_wood(STMaterialId.Botania.living_wood,1,true,false,EnumMaterialStats.living_wood,modLoaded(Botania),EnumMaterialModifier.living_wood),

    living_rock(STMaterialId.Botania.living_rock,1,true,false,EnumMaterialStats.living_rock,modLoaded(Botania),EnumMaterialModifier.living_rock),

    aerialite(STMaterialId.ExtraBotany.aerialite,4,false,false,EnumMaterialStats.aerialite,modLoaded(ExtraBotany),aerialite_default,aerialite_armor),

    shadowium(STMaterialId.ExtraBotany.shadowium,4,false,false,EnumMaterialStats.shadowium,modLoaded(ExtraBotany),shadowium_default,shadowium_armor),

    photonium(STMaterialId.ExtraBotany.photonium,4,false,false,EnumMaterialStats.photonium,modLoaded(ExtraBotany),photonium_default,photonium_armor),

    the_end(STMaterialId.ExtraBotany.the_end,4,true,false,EnumMaterialStats.the_end,modLoaded(ExtraBotany),EnumMaterialModifier.the_end),

    delusion(STMaterialId.delusion, 5,false,false,EnumMaterialStats.delusion,null,delusion_default),

    gluttonous(STMaterialId.gluttonous,5,false,false,EnumMaterialStats.gluttonous,null,gluttonous_default),

    cold_iron_alloy(STMaterialId.cold_iron_alloy,4,false,false,EnumMaterialStats.cold_iron_alloy,null,cold_iron_alloy_default),

    paper(STMaterialId.paper,1,true,false,EnumMaterialStats.paper,null, paper_default),

    nefarious(STMaterialId.EnigmaticLegacy.nefarious,5, false,false,EnumMaterialStats.nefarious,modLoaded(EnigmaticLegacy), nefarious_default,nefarious_armor),

//    apocalyptium(STMaterialId.GoetyRevelation.apocalyptium,5,false,false,EnumMaterialStats.apocalyptium,modLoaded(GoetyRevelation),apocalyptium_default,apocalyptium_armor),


    ;
    public final MaterialId id;
    public final int tier;
    public final boolean craftable;
    public final boolean hidden;
    public final EnumMaterialStats stats;
    public final EnumMaterialModifier[] modifiers;
    public final ICondition condition;
    EnumMaterial(MaterialId id, int tier, boolean craftable, boolean hidden, EnumMaterialStats stats, ICondition condition, EnumMaterialModifier... modifiers){
        this.id = id;
        this.tier =tier;
        this.craftable = craftable;
        this.hidden = hidden;
        this.stats = stats;
        this.modifiers = modifiers;
        this.condition = condition;
    }
    public static ICondition modLoaded(String modId){
        return new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS,new ModLoadedCondition(modId));
    }
    public static ICondition tagFilled(TagKey<Item> tagKey){
        return new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS, new TagFilledCondition<>(tagKey));
    }
}
