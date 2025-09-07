package com.ssakura49.sakuratinker.data.generator.base;

import com.google.gson.JsonObject;
import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.critereon.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.*;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.recipe.data.IRecipeHelper;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public abstract class BaseRecipeProvider implements DataProvider,IConditionBuilder, IRecipeHelper {
    protected final PackOutput.PathProvider recipePathProvider;
    protected final PackOutput.PathProvider advancementPathProvider;

    public BaseRecipeProvider(PackOutput output) {
        this.recipePathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "recipes");
        this.advancementPathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "advancements");
        SakuraTinker.sealSakuraTinkerClass(this, "BaseRecipeProvider", "Please define it in your mod package.");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Set<ResourceLocation> recipeIds = new HashSet<>();
        List<CompletableFuture<?>> futures = new ArrayList<>();

        this.buildRecipes(recipe -> {
            ResourceLocation id = recipe.getId();
            if (!recipeIds.add(id)) {
                throw new IllegalStateException("Duplicate recipe: " + id);
            }

            // 保存 recipe
            futures.add(DataProvider.saveStable(output, recipe.serializeRecipe(), recipePathProvider.json(id)));

            // 保存 advancement
            JsonObject advancementJson = recipe.serializeAdvancement();
            if (advancementJson != null) {
                ResourceLocation advId = recipe.getAdvancementId();
                futures.add(DataProvider.saveStable(output, advancementJson, advancementPathProvider.json(advId)));
            }
        });

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    @Override
    public abstract String getName();

    protected abstract void buildRecipes(Consumer<FinishedRecipe> consumer);


    @Override
    public String getModId() {
        return SakuraTinker.MODID;
    }

    protected @Nullable CompletableFuture<?> saveAdvancement(CachedOutput output, FinishedRecipe finishedRecipe, JsonObject advancementJson) {
        return DataProvider.saveStable(output, advancementJson, this.advancementPathProvider.json(finishedRecipe.getAdvancementId()));
    }

    protected CompletableFuture<?> buildAdvancement(CachedOutput pOutput, ResourceLocation pAdvancementOutputDir, Advancement.Builder pAdvancementBuilder) {
        return DataProvider.saveStable(pOutput, pAdvancementBuilder.serializeToJson(), this.advancementPathProvider.json(pAdvancementOutputDir));
    }

    protected static void oneToOneConversionRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pResult, ItemLike pIngredient, @javax.annotation.Nullable String pGroup) {
        oneToOneConversionRecipe(pFinishedRecipeConsumer, pResult, pIngredient, pGroup, 1);
    }

    protected static void oneToOneConversionRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pResult, ItemLike pIngredient, @javax.annotation.Nullable String pGroup, int pResultCount) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, pResult, pResultCount).requires(pIngredient).group(pGroup).unlockedBy(getHasName(pIngredient), has(pIngredient)).save(pFinishedRecipeConsumer, getConversionRecipeName(pResult, pIngredient));
    }

    protected static void oreSmelting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.SMELTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup) {
        oreCooking(pFinishedRecipeConsumer, RecipeSerializer.BLASTING_RECIPE, pIngredients, pCategory, pResult, pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{itemlike}), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike)).save(pFinishedRecipeConsumer, getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }

    }

    protected static void netheriteSmithing(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item pIngredientItem, RecipeCategory pCategory, Item pResultItem) {
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(new ItemLike[]{Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE}), Ingredient.of(new ItemLike[]{pIngredientItem}), Ingredient.of(new ItemLike[]{Items.NETHERITE_INGOT}), pCategory, pResultItem).unlocks("has_netherite_ingot", has(Items.NETHERITE_INGOT)).save(pFinishedRecipeConsumer, getItemName(pResultItem) + "_smithing");
    }

    protected static void trimSmithing(Consumer<FinishedRecipe> pFinishedRecipeConsumer, Item pIngredientItem, ResourceLocation pLocation) {
        SmithingTrimRecipeBuilder.smithingTrim(Ingredient.of(new ItemLike[]{pIngredientItem}), Ingredient.of(ItemTags.TRIMMABLE_ARMOR), Ingredient.of(ItemTags.TRIM_MATERIALS), RecipeCategory.MISC).unlocks("has_smithing_trim_template", has(pIngredientItem)).save(pFinishedRecipeConsumer, pLocation);
    }

    protected static void twoByTwoPacker(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pPacked, ItemLike pUnpacked) {
        ShapedRecipeBuilder.shaped(pCategory, pPacked, 1).define('#', pUnpacked).pattern("##").pattern("##").unlockedBy(getHasName(pUnpacked), has(pUnpacked)).save(pFinishedRecipeConsumer);
    }

    protected static void threeByThreePacker(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pPacked, ItemLike pUnpacked, String pCriterionName) {
        ShapelessRecipeBuilder.shapeless(pCategory, pPacked).requires(pUnpacked, 9).unlockedBy(pCriterionName, has(pUnpacked)).save(pFinishedRecipeConsumer);
    }

    protected static void threeByThreePacker(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pPacked, ItemLike pUnpacked) {
        threeByThreePacker(pFinishedRecipeConsumer, pCategory, pPacked, pUnpacked, getHasName(pUnpacked));
    }

    protected static void planksFromLog(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pPlanks, TagKey<Item> pLogs, int pResultCount) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pPlanks, pResultCount).requires(pLogs).group("planks").unlockedBy("has_log", has(pLogs)).save(pFinishedRecipeConsumer);
    }

    protected static void planksFromLogs(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pPlanks, TagKey<Item> pLogs, int pResultCount) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pPlanks, pResultCount).requires(pLogs).group("planks").unlockedBy("has_logs", has(pLogs)).save(pFinishedRecipeConsumer);
    }

    protected static void woodFromLogs(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pWood, ItemLike pLog) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pWood, 3).define('#', pLog).pattern("##").pattern("##").group("bark").unlockedBy("has_log", has(pLog)).save(pFinishedRecipeConsumer);
    }

    protected static void woodenBoat(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pBoat, ItemLike pMaterial) {
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, pBoat).define('#', pMaterial).pattern("# #").pattern("###").group("boat").unlockedBy("in_water", insideOf(Blocks.WATER)).save(pFinishedRecipeConsumer);
    }

    protected static void chestBoat(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pBoat, ItemLike pMaterial) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, pBoat).requires(Blocks.CHEST).requires(pMaterial).group("chest_boat").unlockedBy("has_boat", has(ItemTags.BOATS)).save(pFinishedRecipeConsumer);
    }

    protected static RecipeBuilder buttonBuilder(ItemLike pButton, Ingredient pMaterial) {
        return ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, pButton).requires(pMaterial);
    }

    protected static RecipeBuilder doorBuilder(ItemLike pDoor, Ingredient pMaterial) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pDoor, 3).define('#', pMaterial).pattern("##").pattern("##").pattern("##");
    }

    protected static RecipeBuilder fenceBuilder(ItemLike pFence, Ingredient pMaterial) {
        int i = pFence == Blocks.NETHER_BRICK_FENCE ? 6 : 3;
        Item item = pFence == Blocks.NETHER_BRICK_FENCE ? Items.NETHER_BRICK : Items.STICK;
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pFence, i).define('W', pMaterial).define('#', item).pattern("W#W").pattern("W#W");
    }

    protected static RecipeBuilder fenceGateBuilder(ItemLike pFenceGate, Ingredient pMaterial) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pFenceGate).define('#', Items.STICK).define('W', pMaterial).pattern("#W#").pattern("#W#");
    }

    protected static void pressurePlate(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pPressurePlate, ItemLike pMaterial) {
        pressurePlateBuilder(RecipeCategory.REDSTONE, pPressurePlate, Ingredient.of(new ItemLike[]{pMaterial})).unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    protected static RecipeBuilder pressurePlateBuilder(RecipeCategory pCategory, ItemLike pPressurePlate, Ingredient pMaterial) {
        return ShapedRecipeBuilder.shaped(pCategory, pPressurePlate).define('#', pMaterial).pattern("##");
    }

    protected static void slab(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pPressurePlate, ItemLike pMaterial) {
        slabBuilder(pCategory, pPressurePlate, Ingredient.of(new ItemLike[]{pMaterial})).unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    protected static RecipeBuilder slabBuilder(RecipeCategory pCategory, ItemLike pSlab, Ingredient pMaterial) {
        return ShapedRecipeBuilder.shaped(pCategory, pSlab, 6).define('#', pMaterial).pattern("###");
    }

    protected static RecipeBuilder stairBuilder(ItemLike pStairs, Ingredient pMaterial) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pStairs, 4).define('#', pMaterial).pattern("#  ").pattern("## ").pattern("###");
    }

    protected static RecipeBuilder trapdoorBuilder(ItemLike pTrapdoor, Ingredient pMaterial) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, pTrapdoor, 2).define('#', pMaterial).pattern("###").pattern("###");
    }

    protected static RecipeBuilder signBuilder(ItemLike pSign, Ingredient pMaterial) {
        return ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pSign, 3).group("sign").define('#', pMaterial).define('X', Items.STICK).pattern("###").pattern("###").pattern(" X ");
    }

    protected static void hangingSign(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pSign, ItemLike pMaterial) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pSign, 6).group("hanging_sign").define('#', pMaterial).define('X', Items.CHAIN).pattern("X X").pattern("###").pattern("###").unlockedBy("has_stripped_logs", has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    protected static void colorBlockWithDye(Consumer<FinishedRecipe> pFinishedRecipeConsumer, List<Item> pDyes, List<Item> pDyeableItems, String pGroup) {
        for(int i = 0; i < pDyes.size(); ++i) {
            Item item = (Item)pDyes.get(i);
            Item item1 = (Item)pDyeableItems.get(i);
            ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, item1).requires(item).requires(Ingredient.of(pDyeableItems.stream().filter((p_288265_) -> !p_288265_.equals(item1)).map(ItemStack::new))).group(pGroup).unlockedBy("has_needed_dye", has(item)).save(pFinishedRecipeConsumer, "dye_" + getItemName(item1));
        }

    }

    protected static void carpet(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pCarpet, ItemLike pMaterial) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pCarpet, 3).define('#', pMaterial).pattern("##").group("carpet").unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    protected static void bedFromPlanksAndWool(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pBed, ItemLike pWool) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pBed).define('#', pWool).define('X', ItemTags.PLANKS).pattern("###").pattern("XXX").group("bed").unlockedBy(getHasName(pWool), has(pWool)).save(pFinishedRecipeConsumer);
    }

    protected static void banner(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pBanner, ItemLike pMaterial) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pBanner).define('#', pMaterial).define('|', Items.STICK).pattern("###").pattern("###").pattern(" | ").group("banner").unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    protected static void stainedGlassFromGlassAndDye(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pStainedGlass, ItemLike pDye) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pStainedGlass, 8).define('#', Blocks.GLASS).define('X', pDye).pattern("###").pattern("#X#").pattern("###").group("stained_glass").unlockedBy("has_glass", has(Blocks.GLASS)).save(pFinishedRecipeConsumer);
    }

    protected static void stainedGlassPaneFromStainedGlass(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pStainedGlassPane, ItemLike pStainedGlass) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pStainedGlassPane, 16).define('#', pStainedGlass).pattern("###").pattern("###").group("stained_glass_pane").unlockedBy("has_glass", has(pStainedGlass)).save(pFinishedRecipeConsumer);
    }

    protected static void stainedGlassPaneFromGlassPaneAndDye(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pStainedGlassPane, ItemLike pDye) {
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, pStainedGlassPane, 8).define('#', Blocks.GLASS_PANE).define('$', pDye).pattern("###").pattern("#$#").pattern("###").group("stained_glass_pane").unlockedBy("has_glass_pane", has(Blocks.GLASS_PANE)).unlockedBy(getHasName(pDye), has(pDye)).save(pFinishedRecipeConsumer, getConversionRecipeName(pStainedGlassPane, Blocks.GLASS_PANE));
    }

    protected static void coloredTerracottaFromTerracottaAndDye(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pColoredTerracotta, ItemLike pDye) {
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, pColoredTerracotta, 8).define('#', Blocks.TERRACOTTA).define('X', pDye).pattern("###").pattern("#X#").pattern("###").group("stained_terracotta").unlockedBy("has_terracotta", has(Blocks.TERRACOTTA)).save(pFinishedRecipeConsumer);
    }

    protected static void concretePowder(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pDyedConcretePowder, ItemLike pDye) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, pDyedConcretePowder, 8).requires(pDye).requires(Blocks.SAND, 4).requires(Blocks.GRAVEL, 4).group("concrete_powder").unlockedBy("has_sand", has(Blocks.SAND)).unlockedBy("has_gravel", has(Blocks.GRAVEL)).save(pFinishedRecipeConsumer);
    }

    protected static void candle(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pCandle, ItemLike pDye) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, pCandle).requires(Blocks.CANDLE).requires(pDye).group("dyed_candle").unlockedBy(getHasName(pDye), has(pDye)).save(pFinishedRecipeConsumer);
    }

    protected static void wall(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pWall, ItemLike pMaterial) {
        wallBuilder(pCategory, pWall, Ingredient.of(new ItemLike[]{pMaterial})).unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    protected static RecipeBuilder wallBuilder(RecipeCategory pCategory, ItemLike pWall, Ingredient pMaterial) {
        return ShapedRecipeBuilder.shaped(pCategory, pWall, 6).define('#', pMaterial).pattern("###").pattern("###");
    }

    protected static void polished(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial) {
        polishedBuilder(pCategory, pResult, Ingredient.of(new ItemLike[]{pMaterial})).unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    protected static RecipeBuilder polishedBuilder(RecipeCategory pCategory, ItemLike pResult, Ingredient pMaterial) {
        return ShapedRecipeBuilder.shaped(pCategory, pResult, 4).define('S', pMaterial).pattern("SS").pattern("SS");
    }

    protected static void cut(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pCutResult, ItemLike pMaterial) {
        cutBuilder(pCategory, pCutResult, Ingredient.of(new ItemLike[]{pMaterial})).unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    protected static ShapedRecipeBuilder cutBuilder(RecipeCategory pCategory, ItemLike pCutResult, Ingredient pMaterial) {
        return ShapedRecipeBuilder.shaped(pCategory, pCutResult, 4).define('#', pMaterial).pattern("##").pattern("##");
    }

    protected static void chiseled(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pChiseledResult, ItemLike pMaterial) {
        chiseledBuilder(pCategory, pChiseledResult, Ingredient.of(new ItemLike[]{pMaterial})).unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    protected static void mosaicBuilder(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial) {
        ShapedRecipeBuilder.shaped(pCategory, pResult).define('#', pMaterial).pattern("#").pattern("#").unlockedBy(getHasName(pMaterial), has(pMaterial)).save(pFinishedRecipeConsumer);
    }

    protected static ShapedRecipeBuilder chiseledBuilder(RecipeCategory pCategory, ItemLike pChiseledResult, Ingredient pMaterial) {
        return ShapedRecipeBuilder.shaped(pCategory, pChiseledResult).define('#', pMaterial).pattern("#").pattern("#");
    }

    protected static void stonecutterResultFromBase(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial) {
        stonecutterResultFromBase(pFinishedRecipeConsumer, pCategory, pResult, pMaterial, 1);
    }

    protected static void stonecutterResultFromBase(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pCategory, ItemLike pResult, ItemLike pMaterial, int pResultCount) {
        SingleItemRecipeBuilder var10000 = SingleItemRecipeBuilder.stonecutting(Ingredient.of(new ItemLike[]{pMaterial}), pCategory, pResult, pResultCount).unlockedBy(getHasName(pMaterial), has(pMaterial));
        String var10002 = getConversionRecipeName(pResult, pMaterial);
        var10000.save(pFinishedRecipeConsumer, var10002 + "_stonecutting");
    }

    protected static void smeltingResultFromBase(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pResult, ItemLike pIngredient) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(new ItemLike[]{pIngredient}), RecipeCategory.BUILDING_BLOCKS, pResult, 0.1F, 200).unlockedBy(getHasName(pIngredient), has(pIngredient)).save(pFinishedRecipeConsumer);
    }

    protected static void nineBlockStorageRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked) {
        nineBlockStorageRecipes(pFinishedRecipeConsumer, pUnpackedCategory, pUnpacked, pPackedCategory, pPacked, getSimpleRecipeName(pPacked), (String)null, getSimpleRecipeName(pUnpacked), (String)null);
    }

    protected static void nineBlockStorageRecipesWithCustomPacking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked, String pPackedName, String pPackedGroup) {
        nineBlockStorageRecipes(pFinishedRecipeConsumer, pUnpackedCategory, pUnpacked, pPackedCategory, pPacked, pPackedName, pPackedGroup, getSimpleRecipeName(pUnpacked), (String)null);
    }

    protected static void nineBlockStorageRecipesRecipesWithCustomUnpacking(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked, String pUnpackedName, String pUnpackedGroup) {
        nineBlockStorageRecipes(pFinishedRecipeConsumer, pUnpackedCategory, pUnpacked, pPackedCategory, pPacked, getSimpleRecipeName(pPacked), (String)null, pUnpackedName, pUnpackedGroup);
    }

    protected static void nineBlockStorageRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, RecipeCategory pUnpackedCategory, ItemLike pUnpacked, RecipeCategory pPackedCategory, ItemLike pPacked, String pPackedName, @javax.annotation.Nullable String pPackedGroup, String pUnpackedName, @javax.annotation.Nullable String pUnpackedGroup) {
        ShapelessRecipeBuilder.shapeless(pUnpackedCategory, pUnpacked, 9).requires(pPacked).group(pUnpackedGroup).unlockedBy(getHasName(pPacked), has(pPacked)).save(pFinishedRecipeConsumer, ResourceLocation.parse(pUnpackedName));
        ShapedRecipeBuilder.shaped(pPackedCategory, pPacked).define('#', pUnpacked).pattern("###").pattern("###").pattern("###").group(pPackedGroup).unlockedBy(getHasName(pUnpacked), has(pUnpacked)).save(pFinishedRecipeConsumer, ResourceLocation.parse(pPackedName));
    }

    protected static void copySmithingTemplate(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pResult, TagKey<Item> pBaseItem) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pResult, 2).define('#', Items.DIAMOND).define('C', pBaseItem).define('S', pResult).pattern("#S#").pattern("#C#").pattern("###").unlockedBy(getHasName(pResult), has(pResult)).save(pFinishedRecipeConsumer);
    }

    protected static void copySmithingTemplate(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pResult, ItemLike pBaseItem) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pResult, 2).define('#', Items.DIAMOND).define('C', pBaseItem).define('S', pResult).pattern("#S#").pattern("#C#").pattern("###").unlockedBy(getHasName(pResult), has(pResult)).save(pFinishedRecipeConsumer);
    }

    protected static void cookRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer, String pCookingMethod, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, int pCookingTime) {
        simpleCookingRecipe(pFinishedRecipeConsumer, pCookingMethod, pCookingSerializer, pCookingTime, Items.BEEF, Items.COOKED_BEEF, 0.35F);
        simpleCookingRecipe(pFinishedRecipeConsumer, pCookingMethod, pCookingSerializer, pCookingTime, Items.CHICKEN, Items.COOKED_CHICKEN, 0.35F);
        simpleCookingRecipe(pFinishedRecipeConsumer, pCookingMethod, pCookingSerializer, pCookingTime, Items.COD, Items.COOKED_COD, 0.35F);
        simpleCookingRecipe(pFinishedRecipeConsumer, pCookingMethod, pCookingSerializer, pCookingTime, Items.KELP, Items.DRIED_KELP, 0.1F);
        simpleCookingRecipe(pFinishedRecipeConsumer, pCookingMethod, pCookingSerializer, pCookingTime, Items.SALMON, Items.COOKED_SALMON, 0.35F);
        simpleCookingRecipe(pFinishedRecipeConsumer, pCookingMethod, pCookingSerializer, pCookingTime, Items.MUTTON, Items.COOKED_MUTTON, 0.35F);
        simpleCookingRecipe(pFinishedRecipeConsumer, pCookingMethod, pCookingSerializer, pCookingTime, Items.PORKCHOP, Items.COOKED_PORKCHOP, 0.35F);
        simpleCookingRecipe(pFinishedRecipeConsumer, pCookingMethod, pCookingSerializer, pCookingTime, Items.POTATO, Items.BAKED_POTATO, 0.35F);
        simpleCookingRecipe(pFinishedRecipeConsumer, pCookingMethod, pCookingSerializer, pCookingTime, Items.RABBIT, Items.COOKED_RABBIT, 0.35F);
    }

    protected static void simpleCookingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, String pCookingMethod, RecipeSerializer<? extends AbstractCookingRecipe> pCookingSerializer, int pCookingTime, ItemLike pIngredient, ItemLike pResult, float pExperience) {
        SimpleCookingRecipeBuilder var10000 = SimpleCookingRecipeBuilder.generic(Ingredient.of(new ItemLike[]{pIngredient}), RecipeCategory.FOOD, pResult, pExperience, pCookingTime, pCookingSerializer).unlockedBy(getHasName(pIngredient), has(pIngredient));
        String var10002 = getItemName(pResult);
        var10000.save(pFinishedRecipeConsumer, var10002 + "_from_" + pCookingMethod);
    }


    protected static Block getBaseBlock(BlockFamily pFamily, BlockFamily.Variant pVariant) {
        if (pVariant == BlockFamily.Variant.CHISELED) {
            if (!pFamily.getVariants().containsKey(BlockFamily.Variant.SLAB)) {
                throw new IllegalStateException("Slab is not defined for the family.");
            } else {
                return pFamily.get(BlockFamily.Variant.SLAB);
            }
        } else {
            return pFamily.getBaseBlock();
        }
    }

    protected static EnterBlockTrigger.TriggerInstance insideOf(Block pBlock) {
        return new EnterBlockTrigger.TriggerInstance(ContextAwarePredicate.ANY, pBlock, StatePropertiesPredicate.ANY);
    }

    protected static InventoryChangeTrigger.TriggerInstance has(MinMaxBounds.Ints pCount, ItemLike pItem) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(new ItemLike[]{pItem}).withCount(pCount).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance has(ItemLike pItemLike) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(new ItemLike[]{pItemLike}).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance has(TagKey<Item> pTag) {
        return inventoryTrigger(ItemPredicate.Builder.item().of(pTag).build());
    }

    protected static InventoryChangeTrigger.TriggerInstance inventoryTrigger(ItemPredicate... pPredicates) {
        return new InventoryChangeTrigger.TriggerInstance(ContextAwarePredicate.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, pPredicates);
    }

    protected static String getHasName(ItemLike pItemLike) {
        return "has_" + getItemName(pItemLike);
    }

    protected static String getItemName(ItemLike pItemLike) {
        return BuiltInRegistries.ITEM.getKey(pItemLike.asItem()).getPath();
    }

    protected static String getSimpleRecipeName(ItemLike pItemLike) {
        return getItemName(pItemLike);
    }

    protected static String getConversionRecipeName(ItemLike pResult, ItemLike pIngredient) {
        String var10000 = getItemName(pResult);
        return var10000 + "_from_" + getItemName(pIngredient);
    }

    protected static String getSmeltingRecipeName(ItemLike pItemLike) {
        return getItemName(pItemLike) + "_from_smelting";
    }

    protected static String getBlastingRecipeName(ItemLike pItemLike) {
        return getItemName(pItemLike) + "_from_blasting";
    }

}
