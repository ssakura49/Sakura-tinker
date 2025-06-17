package com.ssakura49.sakuratinker.data.enums;

import com.ssakura49.sakuratinker.data.STMaterialId;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

import static com.ssakura49.sakuratinker.utils.ModListUtil.modName.*;
import static com.ssakura49.sakuratinker.data.enums.EnumMaterialModifier.*;

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
    south_star(STMaterialId.south_star,4,false,false,EnumMaterialStats.south_star,null,south_star_default,south_star_armor),
    mycelium_slimesteel(STMaterialId.mycelium_slimesteel,4,false,false,EnumMaterialStats.mycelium_slimesteel,null,mycelium_slimesteel_default,mycelium_slimesteel_armor),
    frost_slimesteel(STMaterialId.frost_slimesteel,4,false,false,EnumMaterialStats.frost_slimesteel,null,frost_slimesteel_default),
    echo_slimesteel(STMaterialId.echo_slimesteel,4,false,false,EnumMaterialStats.echo_slimesteel,null,echo_slimesteel_default,echo_slimesteel_armor),
//    goozma(STMaterialId.goozma,7,false,false,EnumMaterialStats.goozma,null,goozma_default,goozma_armor),

    youkai(STMaterialId.YoukaiHomeComing.youkai, 2,false,false,EnumMaterialStats.youkai,modLoaded(YHKC),youkai_default,youkai_armor,youkai_charm),
    fairy_ice_crystal(STMaterialId.YoukaiHomeComing.fairy_ice_crystal,1,false,false,EnumMaterialStats.fairy_ice_crystal,modLoaded(YHKC),fairy_ice_crystal_default),

    fiery_crystal(STMaterialId.TwilightForest.fiery_crystal,4,false,false,EnumMaterialStats.fiery_crystal,modLoaded(TF),fiery_crystal_default,fiery_crystal_armor),
    raven_feather(STMaterialId.TwilightForest.raven_feather,1,false,false,EnumMaterialStats.raven_feather,modLoaded(TF),raven_feather_fletching),

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

    amphithere_feather(STMaterialId.IceAndFire.amphithere_feather,3,false,false,EnumMaterialStats.amphithere_feather,modLoaded(IceAndFire),amphithere_feather_default),


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
