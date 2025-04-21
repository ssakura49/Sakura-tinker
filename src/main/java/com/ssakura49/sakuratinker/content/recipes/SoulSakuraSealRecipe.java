package com.ssakura49.sakuratinker.content.recipes;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STItems;
import com.ssakura49.sakuratinker.register.STRecipes;
import com.ssakura49.sakuratinker.utils.ToolUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.recipe.RecipeResult;
import slimeknights.tconstruct.library.recipe.TinkerRecipeTypes;
import slimeknights.tconstruct.library.recipe.tinkerstation.IMutableTinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationContainer;
import slimeknights.tconstruct.library.recipe.tinkerstation.ITinkerStationRecipe;
import slimeknights.tconstruct.library.tools.definition.module.material.ToolPartsHook;
import slimeknights.tconstruct.library.tools.nbt.LazyToolStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import slimeknights.tconstruct.shared.TinkerMaterials;

import java.util.List;

public class SoulSakuraSealRecipe implements ITinkerStationRecipe {
    private final ResourceLocation id;
    public static final ResourceLocation SOUL_SAKURA_KEY = SakuraTinker.location("soul_sakura_seal_modifiable");
    public static final ResourceLocation SOUL_SAKURA_META_KEY = SakuraTinker.location("soul_sakura_seal_meta");

    public SoulSakuraSealRecipe(ResourceLocation id) {
        this.id = id;
    }

    private boolean checkMaterials(ITinkerStationContainer inv) {
        ItemStack eezoIngot = ItemStack.EMPTY;
        ItemStack nihiliteIngot = ItemStack.EMPTY;
        ItemStack goldBlock = ItemStack.EMPTY;
        ItemStack manyullynIngot = ItemStack.EMPTY;
        ItemStack toolPart = ItemStack.EMPTY;

        Item eezoItem = STConfig.getEezoIngot().get();
        Item nihiliteItem = STConfig.getNihiliteIngot().get();
        Item goldBlockItem = STConfig.getGoldBlock().get();
        Item manyullynItem = STConfig.getManyullynIngot().get();

        for (int i = 0; i < inv.getInputCount(); ++i) {
            ItemStack input = inv.getInput(i);
            Item item = input.getItem();

            if (item == eezoItem) {
                eezoIngot = input;
            } else if (item == nihiliteItem) {
                nihiliteIngot = input;
            } else if (item == goldBlockItem) {
                goldBlock = input;
            } else if (item == manyullynItem) {
                manyullynIngot = input;
            } else if (item instanceof IToolPart) {
                toolPart = input;
            }
        }
        return !eezoIngot.isEmpty() && !nihiliteIngot.isEmpty() && !goldBlock.isEmpty() && !manyullynIngot.isEmpty() && !toolPart.isEmpty();
    }
    @Override
    public boolean matches(ITinkerStationContainer inv, Level level) {
        ToolStack tool = inv.getTinkerable();
        ItemStack stack = inv.getTinkerableStack();
        // 检查是否是匠魂多部件工具
        if (!stack.isEmpty() && stack.is(slimeknights.tconstruct.common.TinkerTags.Items.MULTIPART_TOOL)) {
            // 检查是否已经有魂樱刻印
            if (tool.getPersistentData().getBoolean(SOUL_SAKURA_KEY)) {
                return false;
            }
//            if (ToolUtils.hasMetaIn(tool, "sakuratinker:soul_sakura")) {
                // 检查材料是否齐全
                if (checkMaterials(inv)) {
                    // 检查是否有有效的工具部件
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
//            }
        }
        return false;
    }

    @Override
    public RecipeResult<LazyToolStack> getValidatedResult(ITinkerStationContainer inv, RegistryAccess registryAccess) {
        ToolStack tool = inv.getTinkerable();
        ToolStack newTool = tool.copy();

        // 查找输入的工具部件并添加其特性
        for (int i = 0; i < inv.getInputCount(); ++i) {
            ItemStack input = inv.getInput(i);
            if (input.getItem() instanceof IToolPart part) {
                // 添加部件材料的所有特性
                for (ModifierEntry trait : MaterialRegistry.getInstance()
                        .getTraits(part.getMaterial(input).getId(), part.getStatType())) {
                    newTool.addModifier(trait.getId(), trait.getLevel());
                }

                // 设置魂樱刻印标记
                newTool.getPersistentData().putBoolean(SOUL_SAKURA_KEY, true);
                newTool.getPersistentData().putString(SOUL_SAKURA_META_KEY,
                        part.getMaterial(input).getId().toString());

                return ITinkerStationRecipe.success(newTool, inv);
            }
        }

        return RecipeResult.pass();
    }

    @Override
    public void updateInputs(LazyToolStack result, IMutableTinkerStationContainer inv, boolean isServer) {
        // 消耗所有输入槽的物品
        for (int i = 0; i < inv.getInputCount(); ++i) {
            inv.shrinkInput(i, 1);
        }
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(ITinkerStationContainer inv) {
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
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return STRecipes.SOUL_SAKURA_SEAL_RECIPE.get();
    }

    @Override
    public RecipeType<?> getType() {
        return TinkerRecipeTypes.TINKER_STATION.get();
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }
}
