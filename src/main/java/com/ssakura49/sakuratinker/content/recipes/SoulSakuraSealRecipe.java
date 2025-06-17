package com.ssakura49.sakuratinker.content.recipes;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STModifiers;
import com.ssakura49.sakuratinker.register.STRecipes;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.tinkerstation.IMutableTinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.definition.module.material.ToolPartsHook;
import slimeknights.tconstruct.library.tools.nbt.LazyToolStack;
import slimeknights.tconstruct.library.tools.nbt.ToolDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SoulSakuraSealRecipe implements ITinkerStationRecipe {
    private final ResourceLocation id;
    public static final ResourceLocation SOUL_SAKURA_KEY = SakuraTinker.location("soul_sakura_seal_modifiable");
    public static final ResourceLocation SOUL_SAKURA_META_KEY = SakuraTinker.location("soul_sakura_seal_meta");

    public static final ResourceLocation SEAL_TOOLTIP_KEY = SakuraTinker.location("seal_tooltip");
    public static final ResourceLocation SEAL_MODIFIER_LIST = SakuraTinker.location("seal_modifiers");

    public SoulSakuraSealRecipe(ResourceLocation id) {
        this.id = id;
    }

    private boolean checkMaterials(ITinkerStationContainer inv) {
        ItemStack input_1 = ItemStack.EMPTY;
        ItemStack input_2 = ItemStack.EMPTY;
        ItemStack input_3 = ItemStack.EMPTY;
        ItemStack goldBlock = ItemStack.EMPTY;
        ItemStack toolPart = ItemStack.EMPTY;

        Item slime_earth = STConfig.slime_crystal_earth().get();
        Item slime_sky = STConfig.slime_crystal_sky().get();
        Item slime_nether = STConfig.slime_crystal_nether().get();
        Item goldBlockItem = STConfig.getGoldBlock().get();

        for (int i = 0; i < inv.getInputCount(); ++i) {
            ItemStack input = inv.getInput(i);
            Item item = input.getItem();

            if (item == slime_earth) {
                input_1 = input;
            } else if (item == slime_sky) {
                input_2 = input;
            } else if (item == slime_nether) {
                input_3 = input;
            } else if (item == goldBlockItem) {
                goldBlock = input;
            } else if (item instanceof IToolPart) {
                toolPart = input;
            }
        }
        return !input_1.isEmpty() && !input_2.isEmpty() && !input_3.isEmpty() && !goldBlock.isEmpty() && !toolPart.isEmpty();
    }

    @Override
    public boolean matches(ITinkerStationContainer inv, @NotNull Level level) {
        ToolStack tool = inv.getTinkerable();
        ItemStack stack = inv.getTinkerableStack();
        if (!stack.isEmpty() && stack.is(slimeknights.tconstruct.common.TinkerTags.Items.MULTIPART_TOOL)) {
            if (tool.getPersistentData().getBoolean(SOUL_SAKURA_KEY)) {
                return false;
            }
            if (checkMaterials(inv)) {
                for (int i = 0; i < inv.getInputCount(); ++i) {
                    ItemStack input = inv.getInput(i);
                    if (input.getItem() instanceof IToolPart part) {
                        List<IToolPart> parts = ToolPartsHook.parts(tool.getDefinition());
                        if (parts.isEmpty()) {
                            return false;
                        }
                        return parts.stream().anyMatch(p -> p == input.getItem());
                    }
                }
            }
        }
        return false;
    }

    @Override
    public @NotNull RecipeResult<LazyToolStack> getValidatedResult(ITinkerStationContainer inv, @NotNull RegistryAccess registryAccess) {
        ToolStack tool = inv.getTinkerable();
        ToolStack newTool = tool.copy();

        for (int i = 0; i < inv.getInputCount(); ++i) {
            ItemStack input = inv.getInput(i);
            if (input.getItem() instanceof IToolPart part) {

                List<String> modifierNames = new ArrayList<>();
                for (ModifierEntry trait : MaterialRegistry.getInstance()
                        .getTraits(part.getMaterial(input).getId(), part.getStatType())) {
                    newTool.addModifier(trait.getId(), trait.getLevel());
                    modifierNames.add(trait.getModifier().getDisplayName().getString());
                }

                ToolDataNBT persistentData = newTool.getPersistentData();
                persistentData.putBoolean(SOUL_SAKURA_KEY, true);
                persistentData.putString(SOUL_SAKURA_META_KEY, part.getMaterial(input).getId().toString());

                ListTag modifiersTag = new ListTag();
                for (String name : modifierNames) {
                    modifiersTag.add(StringTag.valueOf(name));
                }
                newTool.addModifier(STModifiers.SealTooltip.getId(), 1);
                persistentData.put(SEAL_MODIFIER_LIST, modifiersTag);
                persistentData.putBoolean(SEAL_TOOLTIP_KEY, true);

                return ITinkerStationRecipe.success(newTool, inv);
            }
        }

        return RecipeResult.pass();
    }

    @Override
    public void updateInputs(@NotNull LazyToolStack result, IMutableTinkerStationContainer inv, boolean isServer) {
        for (int i = 0; i < inv.getInputCount(); ++i) {
            inv.shrinkInput(i, 1);
        }
    }

    @Override
    public @NotNull NonNullList<ItemStack> getRemainingItems(@NotNull ITinkerStationContainer inv) {
        return NonNullList.of(ItemStack.EMPTY, new ItemStack[0]);
    }

    @Override
    public ItemStack assemble(ITinkerStationContainer inv, RegistryAccess access) {
        return this.getResultItem(access).copy();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public @NotNull ResourceLocation getId() {
        return this.id;
    }

    @Override
    public @NotNull RecipeSerializer<?> getSerializer() {
        return STRecipes.SOUL_SAKURA_SEAL_RECIPE.get();
    }
    
    @Override
    public @NotNull RecipeType<?> getType() {
        return TinkerRecipeTypes.TINKER_STATION.get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    public ItemStack getSlimeEarthInput() {
        return new ItemStack(STConfig.slime_crystal_earth().get());
    }

    public ItemStack getSlimeSkyInput() {
        return new ItemStack(STConfig.slime_crystal_sky().get());
    }

    public ItemStack getSlimeNetherInput() {
        return new ItemStack(STConfig.slime_crystal_nether().get());
    }

    public ItemStack getGoldBlockInput() {
        return new ItemStack(STConfig.getGoldBlock().get());
    }

    public List<ItemStack> getAllToolPartInputs() {
        return ForgeRegistries.ITEMS.getValues()
                .stream()
                .filter(i -> i instanceof IToolPart)
                .map(ItemStack::new)
                .collect(Collectors.toList());
    }
    public List<ItemStack> getDisplayItems(int slot) {
        return switch (slot) {
            case 0 -> List.of(getSlimeEarthInput());  // 包装单个ItemStack为List
            case 1 -> List.of(getSlimeSkyInput());
            case 2 -> List.of(getSlimeNetherInput());
            case 3 -> List.of(getGoldBlockInput());
            case 4 -> getAllToolPartInputs();
            default -> Collections.emptyList();
        };
    }

    public ModifierEntry getDisplayResult() {
        return new ModifierEntry(STModifiers.SealTooltip.get(), 1);
    }
}
