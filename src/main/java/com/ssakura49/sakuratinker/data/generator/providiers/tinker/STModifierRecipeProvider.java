package com.ssakura49.sakuratinker.data.generator.providiers.tinker;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.data.generator.STModifierId;
import com.ssakura49.sakuratinker.data.generator.base.BaseRecipeProvider;
import com.ssakura49.sakuratinker.register.STTags;
import com.ssakura49.sakuratinker.utils.SafeClassUtil;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CompoundIngredient;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.recipe.data.IRecipeHelper;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.recipe.modifiers.adding.IncrementalModifierRecipeBuilder;
import slimeknights.tconstruct.library.tools.SlotType;
import vazkii.botania.common.block.BotaniaBlocks;
import vazkii.botania.common.item.BotaniaItems;

import java.util.function.Consumer;

public class STModifierRecipeProvider extends BaseRecipeProvider {
    public STModifierRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }

    @Override
    public @NotNull String getName() {
        return "Sakura Tinker Modifier Recipes";
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        addModifierRecipe(consumer);
    }

    private void addModifierRecipe(Consumer<FinishedRecipe> consumer) {
        // modifiers
        String upgradeFolder = "tools/modifiers/upgrade/";
        String abilityFolder = "tools/modifiers/ability/";
        String slotlessFolder = "tools/modifiers/slotless/";
        String defenseFolder = "tools/modifiers/defense/";
        String compatFolder = "tools/modifiers/compat/";
        String worktableFolder = "tools/modifiers/worktable/";
        // salvage
        String salvageFolder = "tools/modifiers/salvage/";
        String upgradeSalvage = salvageFolder + "upgrade/";
        String abilitySalvage = salvageFolder + "ability/";
        String defenseSalvage = salvageFolder + "defense/";
        String compatSalvage = salvageFolder + "compat/";
        Consumer<FinishedRecipe> issConsumer = this.withCondition(consumer, this.modLoaded(SafeClassUtil.Modid.ISS));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.ENDER_ATTR)
                .setTools(ingredientFromTags(STTags.Items.TINKER_SPELL_BOOK))
                .setInput(ItemRegistry.ENDER_UPGRADE_ORB.get(), 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(issConsumer, prefix(STModifierId.ENDER_ATTR, upgradeSalvage))
                .save(issConsumer, prefix(STModifierId.ENDER_ATTR, upgradeFolder));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.FIRE_ATTR)
                .setTools(ingredientFromTags(STTags.Items.TINKER_SPELL_BOOK))
                .setInput(ItemRegistry.FIRE_UPGRADE_ORB.get(), 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(issConsumer, prefix(STModifierId.FIRE_ATTR, upgradeSalvage))
                .save(issConsumer, prefix(STModifierId.FIRE_ATTR, upgradeFolder));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.LIGHTNING_ATTR)
                .setTools(ingredientFromTags(STTags.Items.TINKER_SPELL_BOOK))
                .setInput(ItemRegistry.LIGHTNING_UPGRADE_ORB.get(), 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(issConsumer, prefix(STModifierId.LIGHTNING_ATTR, upgradeSalvage))
                .save(issConsumer, prefix(STModifierId.LIGHTNING_ATTR, upgradeFolder));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.HOLY_ATTR)
                .setTools(ingredientFromTags(STTags.Items.TINKER_SPELL_BOOK))
                .setInput(ItemRegistry.HOLY_UPGRADE_ORB.get(), 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(issConsumer, prefix(STModifierId.HOLY_ATTR, upgradeSalvage))
                .save(issConsumer, prefix(STModifierId.HOLY_ATTR, upgradeFolder));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.BLOOD_ATTR)
                .setTools(ingredientFromTags(STTags.Items.TINKER_SPELL_BOOK))
                .setInput(ItemRegistry.BLOOD_UPGRADE_ORB.get(), 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(issConsumer, prefix(STModifierId.BLOOD_ATTR, upgradeSalvage))
                .save(issConsumer, prefix(STModifierId.BLOOD_ATTR, upgradeFolder));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.ICE_ATTR)
                .setTools(ingredientFromTags(STTags.Items.TINKER_SPELL_BOOK))
                .setInput(ItemRegistry.ICE_UPGRADE_ORB.get(), 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(issConsumer, prefix(STModifierId.ICE_ATTR, upgradeSalvage))
                .save(issConsumer, prefix(STModifierId.ICE_ATTR, upgradeFolder));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.EVOCATION_ATTR)
                .setTools(ingredientFromTags(STTags.Items.TINKER_SPELL_BOOK))
                .setInput(ItemRegistry.EVOCATION_UPGRADE_ORB.get(), 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(issConsumer, prefix(STModifierId.EVOCATION_ATTR, upgradeSalvage))
                .save(issConsumer, prefix(STModifierId.EVOCATION_ATTR, upgradeFolder));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.NATURE_ATTR)
                .setTools(ingredientFromTags(STTags.Items.TINKER_SPELL_BOOK))
                .setInput(ItemRegistry.NATURE_UPGRADE_ORB.get(), 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(issConsumer, prefix(STModifierId.NATURE_ATTR, upgradeSalvage))
                .save(issConsumer, prefix(STModifierId.NATURE_ATTR, upgradeFolder));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.MANA_ATTR)
                .setTools(ingredientFromTags(STTags.Items.TINKER_SPELL_BOOK))
                .setInput(ItemRegistry.MANA_UPGRADE_ORB.get(), 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(issConsumer, prefix(STModifierId.MANA_ATTR, upgradeSalvage))
                .save(issConsumer, prefix(STModifierId.MANA_ATTR, upgradeFolder));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.SPELL_COOLDOWN_ATTR)
                .setTools(ingredientFromTags(STTags.Items.TINKER_SPELL_BOOK))
                .setInput(ItemRegistry.COOLDOWN_UPGRADE_ORB.get(), 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(issConsumer, prefix(STModifierId.SPELL_COOLDOWN_ATTR, upgradeSalvage))
                .save(issConsumer, prefix(STModifierId.SPELL_COOLDOWN_ATTR, upgradeFolder));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.SPELL_PROTECTION_ATTR)
                .setTools(ingredientFromTags(STTags.Items.TINKER_SPELL_BOOK))
                .setInput(ItemRegistry.PROTECTION_UPGRADE_ORB.get(), 1, 2)
                .setSlots(SlotType.UPGRADE, 1)
                .saveSalvage(issConsumer, prefix(STModifierId.SPELL_PROTECTION_ATTR, upgradeSalvage))
                .save(issConsumer, prefix(STModifierId.SPELL_PROTECTION_ATTR, upgradeFolder));

        Consumer<FinishedRecipe> botanyConsumer = this.withCondition(consumer, this.modLoaded(SafeClassUtil.Modid.Botania));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.BOTANY_ID_TECH_MOD)
                .setTools(ingredientFromTags(TinkerTags.Items.MODIFIABLE))
                .setInput(BotaniaItems.manaSteel, 1, 24)
                .setSlots(SlotType.ABILITY, 1)
                .setMaxLevel(5)
                .saveSalvage(botanyConsumer, prefix(STModifierId.BOTANY_ID_TECH_MOD, abilitySalvage))
                .save(botanyConsumer, wrap(STModifierId.BOTANY_ID_TECH_MOD, abilityFolder, "_from_ingot"));
        IncrementalModifierRecipeBuilder.modifier(STModifierId.BOTANY_ID_TECH_MOD)
                .setTools(ingredientFromTags(TinkerTags.Items.MODIFIABLE))
                .setInput(BotaniaBlocks.manasteelBlock, 9, 24)
                .setSlots(SlotType.ABILITY, 1)
                .setLeftover(BotaniaItems.manaSteel)
                .setMaxLevel(5)
                .disallowCrystal()
                .save(botanyConsumer, wrap(STModifierId.BOTANY_ID_TECH_MOD, abilityFolder, "_from_block"));
    }

    @SafeVarargs
    private static Ingredient ingredientFromTags(TagKey<Item>... tags) {
        Ingredient[] tagIngredients = new Ingredient[tags.length];
        for (int i = 0; i < tags.length; i++) {
            tagIngredients[i] = Ingredient.of(tags[i]);
        }
        return CompoundIngredient.of(tagIngredients);
    }

}
