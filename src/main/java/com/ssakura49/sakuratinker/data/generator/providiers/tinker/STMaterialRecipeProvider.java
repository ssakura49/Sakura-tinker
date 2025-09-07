package com.ssakura49.sakuratinker.data.generator.providiers.tinker;

import com.Polarice3.Goety.common.items.ModItems;
import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.github.alexthe666.iceandfire.block.IafBlockRegistry;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.data.generator.STMaterialId;
import com.ssakura49.sakuratinker.register.STFluids;
import com.ssakura49.sakuratinker.register.STItems;
import com.wildcard.buddycards.registries.BuddycardsItems;
import io.github.lounode.extrabotany.common.item.ExtraBotanyItems;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.mindoth.dreadsteel.registries.DreadsteelItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import net.minecraftforge.common.crafting.conditions.OrCondition;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.recipe.condition.TagFilledCondition;
import slimeknights.mantle.recipe.helper.FluidOutput;
import slimeknights.mantle.recipe.helper.ItemOutput;
import slimeknights.mantle.recipe.ingredient.FluidIngredient;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.json.ConfigEnabledCondition;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.materials.definition.MaterialVariantId;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.material.MaterialFluidRecipeBuilder;
import slimeknights.tconstruct.library.recipe.fuel.MeltingFuelBuilder;
import slimeknights.tconstruct.library.recipe.material.MaterialRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.IMeltingRecipe;
import slimeknights.tconstruct.library.recipe.melting.MaterialMeltingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;
import twilightforest.init.TFItems;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

import static com.ssakura49.sakuratinker.utils.SafeClassUtil.Modid.*;

public class STMaterialRecipeProvider extends RecipeProvider implements ISmelteryRecipeHelper {
    public STMaterialRecipeProvider(PackOutput generator) {
        super(generator);
    }
    public static final TagKey<Item> GEM_MULTICAST = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), TConstruct.getResource("casts/multi_use/gem"));
    public static final TagKey<Item> GEM_SINGLECAST = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), TConstruct.getResource("casts/single_use/gem"));
    public static final TagKey<Item> INGOT_MULTICAST = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), TConstruct.getResource("casts/multi_use/ingot"));
    public static final TagKey<Item> INGOT_SINGLECAST = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), TConstruct.getResource("casts/single_use/ingot"));
    public static final TagKey<Item> PLATE_MULTICAST = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), TConstruct.getResource("casts/multi_use/plate"));
    public static final TagKey<Item> PLATE_SINGLECAST = TagKey.create(ForgeRegistries.ITEMS.getRegistryKey(), TConstruct.getResource("casts/single_use/plate"));

    public static final ResourceLocation baseFolder = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID,"materials/");
    public static ResourceLocation namedFolder(String name){
        return ResourceLocation.tryParse(baseFolder+name+"/"+name);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ResourceLocation folder;
        Consumer<FinishedRecipe> conditional;

        folder = namedFolder("dragon_sinew");
        materialRecipe(STMaterialId.dragon_sinew,Ingredient.of(STItems.dragon_sinew.get()),1,1, consumer,folder);

        folder = namedFolder("steady_alloy");
        materialRecipe(STMaterialId.steady_alloy,Ingredient.of(STItems.steady_alloy.get()),1,1, consumer,folder);
        melt1Ingot(STFluids.molten_steady_alloy.get(), STItems.steady_alloy.get(),2000, consumer,folder);
        meltMaterial(STFluids.molten_steady_alloy.get(),90,STMaterialId.steady_alloy,2000, consumer,folder);
        AlloyRecipeBuilder.alloy(FluidOutput.fromStack( new FluidStack(STFluids.molten_steady_alloy.get(),180)),2000)
                .addInput(FluidIngredient.of(TinkerFluids.moltenNetherite.get(),90))
                .addInput(FluidIngredient.of(TinkerFluids.moltenDiamond.get(),100))
                .addInput(FluidIngredient.of(STFluids.molten_aurumos.get(),90))
                .save(consumer,ResourceLocation.parse(folder+"_alloy"));

        folder = namedFolder("chimera_gamma");
        materialRecipe(STMaterialId.chimera_gamma,Ingredient.of(STItems.chimera_gamma.get()),1,1, consumer,folder);
        melt1Ingot(STFluids.molten_chimera_gamma.get(), STItems.chimera_gamma.get(),2000, consumer,folder);
        meltMaterial(STFluids.molten_chimera_gamma.get(),90,STMaterialId.chimera_gamma,2000, consumer,folder);
        AlloyRecipeBuilder.alloy(FluidOutput.fromStack( new FluidStack(STFluids.molten_chimera_gamma.get(),90)),2000)
                .addInput(FluidIngredient.of(TinkerFluids.moltenCobalt.get(),90))
                .addInput(FluidIngredient.of(TinkerFluids.moltenManyullyn.get(),90))
                .addInput(FluidIngredient.of(STFluids.molten_soul_sakura.get(),90))
                .addInput(FluidIngredient.of(STFluids.molten_steady_alloy.get(), 90))
                .save(consumer,ResourceLocation.parse(folder+"_alloy"));
        //TODO 更改配方
        folder = namedFolder("delusion");
        materialRecipe(STMaterialId.delusion,Ingredient.of(STItems.delusion.get()),1,1, consumer,folder);
        melt1Ingot(STFluids.molten_delusion.get(), STItems.delusion.get(),4000, consumer,folder);
        meltMaterial(STFluids.molten_delusion.get(),90,STMaterialId.delusion,4000, consumer,folder);
        AlloyRecipeBuilder.alloy(FluidOutput.fromStack( new FluidStack(STFluids.molten_delusion.get(),90)),4000)
                .addInput(FluidIngredient.of(TinkerFluids.moltenNetherite.get(),90))
                .addInput(FluidIngredient.of(TinkerFluids.moltenDiamond.get(),90))
                .addInput(FluidIngredient.of(TinkerFluids.moltenGold.get(),180))
                .addInput(FluidIngredient.of(STFluids.molten_terracryst.get(),90))
                .save(consumer,ResourceLocation.parse(folder+"_alloy"));
        //TODO 更改配方
        folder = namedFolder("gluttonous");
        materialRecipe(STMaterialId.gluttonous,Ingredient.of(STItems.gluttonous.get()),1,1, consumer,folder);
        melt1Ingot(STFluids.molten_gluttonous.get(), STItems.gluttonous.get(),2500, consumer,folder);
        meltMaterial(STFluids.molten_gluttonous.get(),90,STMaterialId.gluttonous,2500, consumer,folder);
        AlloyRecipeBuilder.alloy(FluidOutput.fromStack( new FluidStack(STFluids.molten_gluttonous.get(),90)),2500)
                .addInput(FluidIngredient.of(TinkerFluids.moltenManyullyn.get(),90))
                .addInput(FluidIngredient.of(STFluids.molten_nihilite.get(),90))
                .save(consumer,ResourceLocation.parse(folder+"_alloy"));

        folder = namedFolder("cold_iron_alloy");
        materialRecipe(STMaterialId.cold_iron_alloy,Ingredient.of(STItems.cold_iron_alloy.get()),1,1, consumer,folder);
        melt1Ingot(STFluids.molten_cold_iron_alloy.get(), STItems.cold_iron_alloy.get(),2000, consumer,folder);
        meltMaterial(STFluids.molten_cold_iron_alloy.get(),90,STMaterialId.cold_iron_alloy,2000, consumer,folder);
        AlloyRecipeBuilder.alloy(FluidOutput.fromStack( new FluidStack(STFluids.molten_cold_iron_alloy.get(),90)),2000)
                .addInput(FluidIngredient.of(TinkerFluids.moltenIron.get(),180))
                .addInput(FluidIngredient.of(STFluids.molten_orichalcum.get(),180))
                .addInput(FluidIngredient.of(Fluids.WATER,1000))
                .save(consumer,ResourceLocation.parse(folder+"_alloy"));

        /*
           神秘遗物
             */
        folder = namedFolder("nefarious");
        materialRecipe(STMaterialId.EnigmaticLegacy.nefarious,Ingredient.of(EnigmaticItems.EVIL_INGOT),1,1, consumer,folder);
        melt1Ingot(STFluids.molten_nefarious.get(), EnigmaticItems.EVIL_INGOT,2000, consumer,folder);
        meltMaterial(STFluids.molten_nefarious.get(),90,STMaterialId.EnigmaticLegacy.nefarious,2000, consumer,folder);


        /*
           暮色森林
             */
        folder = namedFolder("raven_feather");
        conditional = withCondition(consumer,modLoaded(TF));
        materialRecipe(STMaterialId.TwilightForest.raven_feather,Ingredient.of(TFItems.RAVEN_FEATHER.get()),1,1, conditional,folder);
        conditional = withCondition(consumer,modLoaded(IceAndFire));
        folder = namedFolder("amphithere_feather");
        materialRecipe(STMaterialId.IceAndFire.amphithere_feather,Ingredient.of(IafItemRegistry.AMPHITHERE_FEATHER.get()),1,1, conditional,folder);

//        conditional = withCondition(consumer,modLoaded(ExtraBotany));
//        folder = namedFolder("orichalcos");
//        materialRecipe(STMaterialId.ExtraBotany.orichalcos,Ingredient.of(ExtraBotanyItems.orichalcos),1,1, conditional,folder);
//        melt1Ingot(STFluids.molten_orichalcos.get(),ExtraBotanyItems.orichalcos,2000, conditional,folder);
//        meltMaterial(STFluids.molten_orichalcos.get(),90,STMaterialId.ExtraBotany.orichalcos,2000, conditional,folder);
//
//        conditional = withCondition(consumer,modLoaded(ClouderTinker));
//        folder = namedFolder("pyrothium");
//        materialRecipe(STMaterialId.ClouderTinker.pyrothium,Ingredient.of(STItems.pyrothium.get()),1,1, conditional,folder);
//        melt1Ingot(STFluids.molten_pyrothium.get(),STItems.pyrothium.get(),1500, conditional,folder);
//        meltMaterial(STFluids.molten_pyrothium.get(),90,STMaterialId.ClouderTinker.pyrothium,1500, conditional,folder);
//        AlloyRecipeBuilder.alloy(FluidOutput.fromStack(new FluidStack(STFluids.molten_pyrothium.get(), 90)),1500)
//                .addInput(STFluids.molten_bear_interest.get(), 90)
//                .addInput(CloudertinkerFluid.molten_fiery.get(),180)
//                .save(conditional,new ResourceLocation(folder+"_alloy"));
        /*
           铁魔法
             */
        conditional = withCondition(consumer,modLoaded(ISS));
        folder = namedFolder("arcane_alloy");
        materialRecipe(STMaterialId.IronSpellBook.arcane_alloy,Ingredient.of(STItems.arcane_alloy.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_arcane_alloy.get(), STItems.arcane_alloy.get(),1500, conditional,folder);
        meltMaterial(STFluids.molten_arcane_alloy.get(),90,STMaterialId.IronSpellBook.arcane_alloy,1500, conditional,folder);
        AlloyRecipeBuilder.alloy(FluidOutput.fromStack( new FluidStack(STFluids.molten_arcane_alloy.get(),90)),1500)
                .addInput(FluidIngredient.of(TinkerFluids.moltenGold.get(),360))
                .addInput(FluidIngredient.of(STFluids.molten_arcane_salvage.get(),360))
                .save(consumer,ResourceLocation.parse(folder+"_alloy"));
        folder = namedFolder("arcane_salvage");
        melt1Ingot(STFluids.molten_arcane_salvage.get(), ItemRegistry.ARCANE_SALVAGE.get(),1500, conditional,folder);

        conditional = withCondition(consumer,modLoaded(DreadSteel));
        folder = namedFolder("dread_steel");
        materialRecipe(STMaterialId.DreadSteel.dread_steel,Ingredient.of(DreadsteelItems.DREADSTEEL_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_dread_steel.get(), DreadsteelItems.DREADSTEEL_INGOT.get(),1500, conditional,folder);
        meltMaterial(STFluids.molten_dread_steel.get(),90,STMaterialId.DreadSteel.dread_steel,1500, conditional,folder);

        /*
           诡厄巫法
             */
        conditional = withCondition(consumer,modLoaded(Goety));
        folder = namedFolder("cursed_metal");
        materialRecipe(STMaterialId.Goety.cursed_metal,Ingredient.of(ModItems.CURSED_METAL_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_cursed_metal.get(), ModItems.CURSED_METAL_INGOT.get(),1000, conditional,folder);
        meltMaterial(STFluids.molten_cursed_metal.get(),90,STMaterialId.Goety.cursed_metal,1000, conditional,folder);
        folder = namedFolder("dark_metal");
        materialRecipe(STMaterialId.Goety.dark_metal,Ingredient.of(ModItems.DARK_METAL_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_dark_metal.get(), ModItems.DARK_METAL_INGOT.get(),1000, conditional,folder);
        meltMaterial(STFluids.molten_dark_metal.get(),90,STMaterialId.Goety.dark_metal,1000, conditional,folder);
        folder = namedFolder("unholy_alloy");
        materialRecipe(STMaterialId.Goety.unholy_alloy,Ingredient.of(STItems.unholy_alloy.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_unholy_alloy.get(), STItems.unholy_alloy.get(),1500, conditional,folder);
        meltMaterial(STFluids.molten_unholy_alloy.get(),90,STMaterialId.Goety.unholy_alloy,1500, conditional,folder);

        /*
           植物魔法
             */
        conditional = withCondition(consumer,modLoaded(Botania));
        folder = namedFolder("mana_steel");
        materialRecipe(STMaterialId.Botania.mana_steel,Ingredient.of(BotaniaItems.manaSteel),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_mana_steel.get(), BotaniaItems.manaSteel,1000, conditional,folder);
        meltMaterial(STFluids.molten_mana_steel.get(),90,STMaterialId.Botania.mana_steel,1000, conditional,folder);
        melt9Ingot(STFluids.molten_mana_steel.get(), BotaniaBlocks.manasteelBlock, 1000, conditional, folder);
        folder = namedFolder("terra_steel");
        materialRecipe(STMaterialId.Botania.terra_steel,Ingredient.of(BotaniaItems.terrasteel),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_terra_steel.get(), BotaniaItems.terrasteel,1500, conditional,folder);
        meltMaterial(STFluids.molten_terra_steel.get(),90,STMaterialId.Botania.terra_steel,1500, conditional,folder);
        melt9Ingot(STFluids.molten_terra_steel.get(), BotaniaBlocks.terrasteelBlock, 1500, conditional, folder);
        folder = namedFolder("elementium");
        materialRecipe(STMaterialId.Botania.elementium,Ingredient.of(BotaniaItems.elementium),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_elementium.get(), BotaniaItems.elementium,1200, conditional,folder);
        meltMaterial(STFluids.molten_elementium.get(),90,STMaterialId.Botania.elementium,1200, conditional,folder);
        melt9Ingot(STFluids.molten_elementium.get(), BotaniaBlocks.elementiumBlock, 1200, conditional, folder);
        folder = namedFolder("gaia");
        materialRecipe(STMaterialId.Botania.gaia,Ingredient.of(BotaniaItems.gaiaIngot),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_gaia.get(), BotaniaItems.gaiaIngot,2000, conditional,folder);
        meltMaterial(STFluids.molten_gaia.get(),90,STMaterialId.Botania.gaia,2000, conditional,folder);
        folder = namedFolder("mana_string");
        materialRecipe(STMaterialId.Botania.mana_string,Ingredient.of(BotaniaItems.manaString),1,1, conditional,folder);
        folder = namedFolder("living_wood");
        materialRecipe(STMaterialId.Botania.living_wood,Ingredient.of(BotaniaBlocks.livingwood.asItem()),1,1, conditional,folder);
        folder = namedFolder("living_rock");
        materialRecipe(STMaterialId.Botania.living_rock,Ingredient.of(BotaniaBlocks.livingrock.asItem()),1,1, conditional,folder);

        /*
           额外植物学
             */
        conditional = withCondition(consumer,modLoaded(ExtraBotany));
        folder = namedFolder("aerialite");
        materialRecipe(STMaterialId.ExtraBotany.aerialite,Ingredient.of(ExtraBotanyItems.aerialite),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_aerialite.get(), ExtraBotanyItems.aerialite,1200, conditional,folder);
        meltMaterial(STFluids.molten_aerialite.get(),90,STMaterialId.ExtraBotany.aerialite,1200, conditional,folder);
        folder = namedFolder("shadowium");
        materialRecipe(STMaterialId.ExtraBotany.shadowium,Ingredient.of(ExtraBotanyItems.shadowium),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_shadowium.get(), ExtraBotanyItems.shadowium,1200, conditional,folder);
        meltMaterial(STFluids.molten_shadowium.get(),90,STMaterialId.ExtraBotany.shadowium,1200, conditional,folder);
        folder = namedFolder("photonium");
        materialRecipe(STMaterialId.ExtraBotany.photonium,Ingredient.of(ExtraBotanyItems.photonium),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_photonium.get(), ExtraBotanyItems.photonium,1200, conditional,folder);
        meltMaterial(STFluids.molten_photonium.get(),90,STMaterialId.ExtraBotany.photonium,1200, conditional,folder);
        folder = namedFolder("the_end");
        materialRecipe(STMaterialId.ExtraBotany.the_end,Ingredient.of(ExtraBotanyItems.theEnd),1,1, conditional,folder);


            /*
              冰火
             */
        conditional = withCondition(consumer,modLoaded(IceAndFire));
        folder = namedFolder("fire_dragon_steel");
        materialRecipe(STMaterialId.IceAndFire.dragon_fire_steel,Ingredient.of(IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_dragon_fire_steel.get(), IafItemRegistry.DRAGONSTEEL_FIRE_INGOT.get(),2000, conditional,folder);
        meltMaterial(STFluids.molten_dragon_fire_steel.get(),90,STMaterialId.IceAndFire.dragon_fire_steel,2000, conditional,folder);
        melt9Ingot(STFluids.molten_dragon_fire_steel.get(), IafBlockRegistry.DRAGONSTEEL_FIRE_BLOCK.get(), 2000, conditional, folder);
        AlloyRecipeBuilder.alloy(FluidOutput.fromStack( new FluidStack(STFluids.molten_dragon_fire_steel.get(),90)),2000)
                .addInput(FluidIngredient.of(TinkerFluids.moltenIron.get(),90))
                .addInput(FluidIngredient.of(STFluids.molten_fire_dragon_blood.get(),500))
                .save(consumer,ResourceLocation.parse(folder+"_alloy"));
        folder = namedFolder("ice_dragon_steel");
        materialRecipe(STMaterialId.IceAndFire.dragon_ice_steel,Ingredient.of(IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_dragon_ice_steel.get(), IafItemRegistry.DRAGONSTEEL_ICE_INGOT.get(),2000, conditional,folder);
        meltMaterial(STFluids.molten_dragon_ice_steel.get(),90,STMaterialId.IceAndFire.dragon_ice_steel,2000, conditional,folder);
        melt9Ingot(STFluids.molten_dragon_ice_steel.get(), IafBlockRegistry.DRAGONSTEEL_ICE_BLOCK.get(), 2000, conditional, folder);
        AlloyRecipeBuilder.alloy(FluidOutput.fromStack( new FluidStack(STFluids.molten_dragon_ice_steel.get(),90)),2000)
                .addInput(FluidIngredient.of(TinkerFluids.moltenIron.get(),90))
                .addInput(FluidIngredient.of(STFluids.molten_ice_dragon_blood.get(),500))
                .save(consumer,ResourceLocation.parse(folder+"_alloy"));
        folder = namedFolder("lightning_dragon_steel");
        materialRecipe(STMaterialId.IceAndFire.dragon_lightning_steel,Ingredient.of(IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_dragon_lightning_steel.get(), IafItemRegistry.DRAGONSTEEL_LIGHTNING_INGOT.get(),2000, conditional,folder);
        meltMaterial(STFluids.molten_dragon_lightning_steel.get(),90,STMaterialId.IceAndFire.dragon_lightning_steel,2000, conditional,folder);
        melt9Ingot(STFluids.molten_dragon_lightning_steel.get(), IafBlockRegistry.DRAGONSTEEL_LIGHTNING_BLOCK.get(), 2000, conditional, folder);
        AlloyRecipeBuilder.alloy(FluidOutput.fromStack( new FluidStack(STFluids.molten_dragon_lightning_steel.get(),90)),2000)
                .addInput(FluidIngredient.of(TinkerFluids.moltenIron.get(),90))
                .addInput(FluidIngredient.of(STFluids.molten_lightning_dragon_blood.get(),500))
                .save(consumer,ResourceLocation.parse(folder+"_alloy"));
        folder = namedFolder("molten_fire_dragon_blood");
        customCastRecipe(IafItemRegistry.FIRE_DRAGON_BLOOD.get(), Items.GLASS_BOTTLE, STFluids.molten_fire_dragon_blood.get(), 125,2000,100,conditional,folder);
        folder = namedFolder("molten_ice_dragon_blood");
        customCastRecipe(IafItemRegistry.ICE_DRAGON_BLOOD.get(), Items.GLASS_BOTTLE, STFluids.molten_ice_dragon_blood.get(), 125,2000,100,conditional,folder);
        folder = namedFolder("molten_lightning_dragon_blood");
        customCastRecipe(IafItemRegistry.LIGHTNING_DRAGON_BLOOD.get(), Items.GLASS_BOTTLE, STFluids.molten_lightning_dragon_blood.get(), 125,2000,100,conditional,folder);

        /*
          巴迪卡牌
         */
        conditional = withCondition(consumer,modLoaded(BuddyCards));
        folder = namedFolder("buddysteel");
        materialRecipe(STMaterialId.BuddyCard.buddysteel,Ingredient.of(BuddycardsItems.BUDDYSTEEL_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_buddysteel.get(), BuddycardsItems.BUDDYSTEEL_INGOT.get(),800, conditional,folder);
        meltMaterial(STFluids.molten_buddysteel.get(),90,STMaterialId.BuddyCard.buddysteel,800, conditional,folder);

        folder = namedFolder("charged_buddysteel");
        materialRecipe(STMaterialId.BuddyCard.charged_buddysteel,Ingredient.of(BuddycardsItems.CHARGED_BUDDYSTEEL_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_charged_buddysteel.get(), BuddycardsItems.CHARGED_BUDDYSTEEL_INGOT.get(),1000, conditional,folder);
        meltMaterial(STFluids.molten_charged_buddysteel.get(),90,STMaterialId.BuddyCard.charged_buddysteel,1000, conditional,folder);

        folder = namedFolder("crimson_buddysteel");
        materialRecipe(STMaterialId.BuddyCard.crimson_buddysteel,Ingredient.of(BuddycardsItems.CRIMSON_BUDDYSTEEL_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_crimson_buddysteel.get(), BuddycardsItems.CRIMSON_BUDDYSTEEL_INGOT.get(),1500, conditional,folder);
        meltMaterial(STFluids.molten_crimson_buddysteel.get(),90,STMaterialId.BuddyCard.crimson_buddysteel,1500, conditional,folder);

        folder = namedFolder("void_buddysteel");
        materialRecipe(STMaterialId.BuddyCard.void_buddysteel,Ingredient.of(BuddycardsItems.VOID_BUDDYSTEEL_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_void_buddysteel.get(), BuddycardsItems.VOID_BUDDYSTEEL_INGOT.get(),1800, conditional,folder);
        meltMaterial(STFluids.molten_void_buddysteel.get(),90,STMaterialId.BuddyCard.void_buddysteel,1800, conditional,folder);

        folder = namedFolder("perfect_buddysteel");
        materialRecipe(STMaterialId.BuddyCard.perfect_buddysteel,Ingredient.of(BuddycardsItems.PERFECT_BUDDYSTEEL_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_perfect_buddysteel.get(), BuddycardsItems.PERFECT_BUDDYSTEEL_INGOT.get(),2000, conditional,folder);
        meltMaterial(STFluids.molten_perfect_buddysteel.get(),90,STMaterialId.BuddyCard.perfect_buddysteel,2000, conditional,folder);

        folder = namedFolder("true_perfect_buddysteel");
        materialRecipe(STMaterialId.BuddyCard.true_perfect_buddysteel,Ingredient.of(BuddycardsItems.TRUE_PERFECT_BUDDYSTEEL_INGOT.get()),1,1, conditional,folder);
        melt1Ingot(STFluids.molten_true_perfect_buddysteel.get(), BuddycardsItems.TRUE_PERFECT_BUDDYSTEEL_INGOT.get(),2200, conditional,folder);
        meltMaterial(STFluids.molten_true_perfect_buddysteel.get(),90,STMaterialId.BuddyCard.true_perfect_buddysteel,2200, conditional,folder);

//        /*
//          启示录
//         */
//        conditional = withCondition(consumer,modLoaded(GoetyRevelation));
//        folder = namedFolder("apocalyptium");
//        materialRecipe(STMaterialId.GoetyRevelation.apocalyptium,Ingredient.of(GRItems.APOCALYPTIUM_INGOT.get()),1,1, conditional,folder);
//        melt1Ingot(STFluids.molten_apocalyptium.get(), GRItems.APOCALYPTIUM_INGOT.get(),4000, conditional,folder);
//        meltMaterial(STFluids.molten_apocalyptium.get(),90,STMaterialId.GoetyRevelation.apocalyptium,4000, conditional,folder);

    }

    @Override
    public @NotNull String getModId() {
        return SakuraTinker.MODID;
    }
    // 熔炼1个块(1000mb)并生成将流体重新铸造成块的配方
    public void melt1B(Fluid fluid, ItemLike ingredient, int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,1000),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(1000))).save(consumer, ResourceLocation.parse(location+"_melting_1b"));
        ItemCastingRecipeBuilder.basinRecipe(ingredient).setFluid(FluidIngredient.of(new FluidStack(fluid,1000))).setCoolingTime(temperature,1000).save(consumer,ResourceLocation.parse(location+"_casting_1b"));
    }
    // 同上，但使用物品标签而非单个物品
    public void melt1B(Fluid fluid, TagKey<Item> ingredient, int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,1000),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(1000))).save(consumer, ResourceLocation.parse(location+"_melting_1b"));
        ItemCastingRecipeBuilder.basinRecipe(ingredient).setFluid(FluidIngredient.of(new FluidStack(fluid,1000))).setCoolingTime(temperature,1000).save(consumer,ResourceLocation.parse(location+"_casting_1b"));
    }
    // 熔炼9个宝石(900mb)并生成宝石块的铸造配方
    public void melt9Gem(Fluid fluid,ItemLike ingredient,int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,900),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(900))).save(consumer, ResourceLocation.parse(location+"_melting_gem_block"));
        ItemCastingRecipeBuilder.basinRecipe(ingredient).setFluid(FluidIngredient.of(new FluidStack(fluid,900))).setCoolingTime(temperature,900).save(consumer,ResourceLocation.parse(location+"_casting_gem_block"));
    }
    // 同上，但使用物品标签而非单个物品
    public void melt9Gem(Fluid fluid,TagKey<Item> ingredient,int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,900),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(900))).save(consumer, ResourceLocation.parse(location+"_melting_gem_block"));
        ItemCastingRecipeBuilder.basinRecipe(ingredient).setFluid(FluidIngredient.of(new FluidStack(fluid,900))).setCoolingTime(temperature,900).save(consumer,ResourceLocation.parse(location+"_casting_gem_block"));
    }
    // 熔炼9个锭(810mb)并生成金属块的铸造配方
    public void melt9Ingot(Fluid fluid,ItemLike ingredient,int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,810),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(810))).save(consumer,ResourceLocation.parse(location+"_melting_metal_block"));
        ItemCastingRecipeBuilder.basinRecipe(ingredient).setFluid(FluidIngredient.of(new FluidStack(fluid,810))).setCoolingTime(temperature,810).save(consumer,ResourceLocation.parse(location+"_casting_metal_block"));
    }
    // 同上，但使用物品标签而非单个物品
    public void melt9Ingot(Fluid fluid,TagKey<Item> ingredient,int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,810),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(810))).save(consumer,ResourceLocation.parse(location+"_melting_metal_block"));
        ItemCastingRecipeBuilder.basinRecipe(ingredient).setFluid(FluidIngredient.of(new FluidStack(fluid,810))).setCoolingTime(temperature,810).save(consumer,ResourceLocation.parse(location+"_casting_metal_block"));
    }
    // 熔炼1个粘液球(250mb)并生成工作台铸造配方
    public void melt1Slimeball(Fluid fluid,ItemLike ingredient,int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,250),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(250))).save(consumer,ResourceLocation.parse(location+"_melting_250mb"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setFluid(FluidIngredient.of(new FluidStack(fluid,250))).setCoolingTime(temperature,250).save(consumer,ResourceLocation.parse(location+"_casting_250mb"));
    }
    // 同上，但使用物品标签而非单个物品
    public void melt1Slimeball(Fluid fluid,TagKey<Item> ingredient,int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,250),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(250))).save(consumer,ResourceLocation.parse(location+"_melting_250mb"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setFluid(FluidIngredient.of(new FluidStack(fluid,250))).setCoolingTime(temperature,250).save(consumer,ResourceLocation.parse(location+"_casting_250mb"));
    }
    // 熔炼1个宝石(100mb)并生成单次/多次宝石铸造配方
    public void melt1Gem(Fluid fluid,ItemLike ingredient,int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,100),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(100))).save(consumer,ResourceLocation.parse(location+"_melting_gem"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,100).setFluid(FluidIngredient.of(new FluidStack(fluid,100))).setCast(GEM_MULTICAST,false).save(consumer,ResourceLocation.parse(location+"_casting_gem_multi"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,100).setFluid(FluidIngredient.of(new FluidStack(fluid,100))).setCast(GEM_SINGLECAST,true).save(consumer,ResourceLocation.parse(location+"_casting_gem_single"));
    }
    // 同上，但使用物品标签而非单个物品
    public void melt1Gem(Fluid fluid,TagKey<Item> ingredient,int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,100),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(100))).save(consumer,ResourceLocation.parse(location+"_melting_gem"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,100).setFluid(FluidIngredient.of(new FluidStack(fluid,100))).setCast(GEM_MULTICAST,false).save(consumer,ResourceLocation.parse(location+"_casting_gem_multi"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,100).setFluid(FluidIngredient.of(new FluidStack(fluid,100))).setCast(GEM_SINGLECAST,true).save(consumer,ResourceLocation.parse(location+"_casting_gem_single"));
    }
    // 熔炼1个锭(90mb)并生成单次/多次锭铸造配方
    public void melt1Ingot(Fluid fluid, ItemLike ingredient, int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,90),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(90))).save(consumer,ResourceLocation.parse(location+"_melting_ingot"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,90).setFluid(FluidIngredient.of(new FluidStack(fluid,90))).setCast(INGOT_MULTICAST,false).save(consumer,ResourceLocation.parse(location+"_casting_ingot_single"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,90).setFluid(FluidIngredient.of(new FluidStack(fluid,90))).setCast(INGOT_SINGLECAST,true).save(consumer,ResourceLocation.parse(location+"_casting_ingot_multi"));
    }
    // 熔炼1个板(90mb)并生成单次/多次板铸造配方
    public void melt1Plate(Fluid fluid, ItemLike ingredient, int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,90),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(90))).save(consumer,ResourceLocation.parse(location+"_melting_plate"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,90).setFluid(FluidIngredient.of(new FluidStack(fluid,90))).setCast(PLATE_MULTICAST,false).save(consumer,ResourceLocation.parse(location+"_casting_plate_single"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,90).setFluid(FluidIngredient.of(new FluidStack(fluid,90))).setCast(PLATE_SINGLECAST,true).save(consumer,ResourceLocation.parse(location+"_casting_plate_multi"));
    }
    // 同上，但使用物品标签而非单个物品
    public void melt1Ingot(Fluid fluid, TagKey<Item> ingredient, int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),new FluidStack(fluid,90),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(90))).save(consumer,ResourceLocation.parse(location+"_melting_ingot"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,90).setFluid(FluidIngredient.of(new FluidStack(fluid,90))).setCast(INGOT_MULTICAST,false).save(consumer,ResourceLocation.parse(location+"_casting_ingot_single"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,90).setFluid(FluidIngredient.of(new FluidStack(fluid,90))).setCast(INGOT_SINGLECAST,true).save(consumer,ResourceLocation.parse(location+"_casting_ingot_multi"));
    }
    // 同上，但使用流体标签而非特定流体
    public void melt1Ingot(TagKey<Fluid> fluid, TagKey<Item> ingredient, int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MeltingRecipeBuilder.melting(Ingredient.of(ingredient),FluidOutput.fromTag(fluid,90),temperature, IMeltingRecipe.calcTime(temperature, IMeltingRecipe.calcTimeFactor(90))).save(consumer,ResourceLocation.parse(location+"_melting_ingot"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,90).setFluid(FluidIngredient.of(fluid,90)).setCast(INGOT_MULTICAST,false).save(consumer,ResourceLocation.parse(location+"_casting_ingot_single"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,90).setFluid(FluidIngredient.of(fluid,90)).setCast(INGOT_SINGLECAST,true).save(consumer,ResourceLocation.parse(location+"_casting_ingot_multi"));
    }
    // 创建使用流体标签的材料熔炼和铸造配方
    public void cast1Ingot(Fluid fluid, ItemLike ingredient, int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,90).setFluid(FluidIngredient.of(new FluidStack(fluid,90))).setCast(INGOT_MULTICAST,false).save(consumer,ResourceLocation.parse(location+"_casting_ingot_single"));
        ItemCastingRecipeBuilder.tableRecipe(ingredient).setCoolingTime(temperature,90).setFluid(FluidIngredient.of(new FluidStack(fluid,90))).setCast(INGOT_SINGLECAST,true).save(consumer,ResourceLocation.parse(location+"_casting_ingot_multi"));
    }

    // 创建使用特定流体的材料熔炼和铸造配方
    public void meltMaterial(TagKey<Fluid> fluid, int amount, MaterialVariantId id, int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MaterialMeltingRecipeBuilder.material(id,temperature, FluidOutput.fromTag(fluid,amount)).save(consumer, ResourceLocation.parse(location+"_material_melt"));
        MaterialFluidRecipeBuilder.material(id).setFluid(fluid,amount).setTemperature(temperature).save(consumer, ResourceLocation.parse(location+"_material_cast"));
    }
    // 创建基础材料配方(将物品处理成材料)
    public void meltMaterial(Fluid fluid,int amount, MaterialVariantId id, int temperature, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MaterialMeltingRecipeBuilder.material(id,temperature, FluidOutput.fromFluid(fluid,amount)).save(consumer, ResourceLocation.parse(location+"_material_melt"));
        MaterialFluidRecipeBuilder.material(id).setTemperature(temperature).setFluid(FluidIngredient.of(new FluidStack(fluid,amount))).save(consumer, ResourceLocation.parse(location+"_material_cast"));
    }
    // 创建带有剩余物品的材料配方
    public void materialRecipe(MaterialVariantId id, Ingredient ingredient, int needed, int value, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MaterialRecipeBuilder.materialRecipe(id).setIngredient(ingredient).setNeeded(needed).setValue(value).save(consumer,ResourceLocation.parse(location+"_material"+needed+value));
    }
    public void materialRecipe(MaterialVariantId id, Ingredient ingredient,TagKey<Item> leftOver, int needed, int value, Consumer<FinishedRecipe> consumer, ResourceLocation location){
        MaterialRecipeBuilder.materialRecipe(id).setIngredient(ingredient).setNeeded(needed).setValue(value).setLeftover(ItemOutput.fromTag(leftOver)).save(consumer,ResourceLocation.parse(location+"_material"+needed+value));
    }
    // 创建冶炼厂的燃料配方
    public void fuel(String name,FluidIngredient ingredient,int duration,int temp,Consumer<FinishedRecipe> consumer){
        MeltingFuelBuilder.fuel(ingredient,duration,temp).save(consumer,ResourceLocation.parse(namedFolder("fuel")+"_"+name+"fuel"));
    }
    /**
     * 创建自定义铸模的铸造配方
     * @param output 产出的物品
     * @param cast 使用的铸模物品
     * @param fluid 需要的流体
     * @param fluidAmount 需要的流体量(mB)
     * @param temperature 流体温度
     * @param coolingTime 冷却时间(ticks)
     * @param consumer 配方消费者
     * @param location 配方注册路径
     */
    public void customCastRecipe(ItemLike output, ItemLike cast, Fluid fluid, int fluidAmount,
                                 int temperature, int coolingTime,
                                 Consumer<FinishedRecipe> consumer, ResourceLocation location) {
        ItemCastingRecipeBuilder.tableRecipe(output)
                .setFluid(FluidIngredient.of(new FluidStack(fluid, fluidAmount)))
                        .setCoolingTime(temperature, coolingTime)
                        .setCast(cast, true)  // true表示铸模会被消耗
                        .save(consumer, location);
    }

    /**
     * 重载版本，使用流体标签而非特定流体
     */
    public void customCastRecipe(ItemLike output, ItemLike cast, TagKey<Fluid> fluid, int fluidAmount,
                                 int temperature, int coolingTime,
                                 Consumer<FinishedRecipe> consumer, ResourceLocation location) {
        ItemCastingRecipeBuilder.tableRecipe(output)
                .setFluid(FluidIngredient.of(fluid, fluidAmount))
                .setCoolingTime(temperature, coolingTime)
                .setCast(cast, true)
                .save(consumer, location);
    }

    /**
     * 重载版本，铸模不会被消耗
     */
    public void customCastRecipe(ItemLike output, ItemLike cast, Fluid fluid, int fluidAmount,
                                 int temperature, int coolingTime, boolean consumeCast,
                                 Consumer<FinishedRecipe> consumer, ResourceLocation location) {
        ItemCastingRecipeBuilder.tableRecipe(output)
                .setFluid(FluidIngredient.of(new FluidStack(fluid, fluidAmount)))
                        .setCoolingTime(temperature, coolingTime)
                        .setCast(cast, consumeCast)
                        .save(consumer, location);
    }

    public static ICondition modLoaded(String modId){
        return new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS,new ModLoadedCondition(modId));
    }
    public static ICondition tagFilled(TagKey<Item> tagKey){
        return new OrCondition(ConfigEnabledCondition.FORCE_INTEGRATION_MATERIALS,new TagFilledCondition<>(tagKey));
    }
}
