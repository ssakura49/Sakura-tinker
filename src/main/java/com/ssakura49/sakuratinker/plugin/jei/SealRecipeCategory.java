package com.ssakura49.sakuratinker.plugin.jei;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.recipes.SoulSakuraSealRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.library.tools.item.IModifiable;
import slimeknights.tconstruct.library.tools.part.IToolPart;
import slimeknights.tconstruct.plugin.jei.TConstructJEIConstants;
import slimeknights.tconstruct.plugin.jei.modifiers.ModifierIngredientRenderer;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.item.CreativeSlotItem;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class SealRecipeCategory implements IRecipeCategory<SoulSakuraSealRecipe> {
    public static final RecipeType<SoulSakuraSealRecipe> SEAL = RecipeType.create(SakuraTinker.MODID, "soul_sakura_seal", SoulSakuraSealRecipe.class);
    protected static final ResourceLocation BACKGROUND_LOC = TConstruct.getResource("textures/gui/jei/tinker_station.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable[] slotIcons;
    private final ModifierIngredientRenderer modifierRenderer;

    public SealRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(BACKGROUND_LOC, 0, 0, 128, 77);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, CreativeSlotItem.withSlot(new ItemStack(TinkerModifiers.creativeSlotItem), SlotType.UPGRADE));
        this.slotIcons = new IDrawable[6];
        this.modifierRenderer = new ModifierIngredientRenderer(124, 10);
    }

    public void draw(SoulSakuraSealRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics, double mouseX, double mouseY) {
        this.drawSlot(graphics, recipe, 0, 2, 32);
        this.drawSlot(graphics, recipe, 1, 24, 14);
        this.drawSlot(graphics, recipe, 2, 46, 32);
        this.drawSlot(graphics, recipe, 3, 42, 57);
        this.drawSlot(graphics, recipe, 4, 6, 57);
    }

    private void drawSlot(GuiGraphics graphics, SoulSakuraSealRecipe recipe, int slot, int x, int y) {
        List<ItemStack> stacks = recipe.getDisplayItems(slot);
        if (stacks.isEmpty()) {
            this.slotIcons[slot].draw(graphics, x + 1, y + 1);
        }

    }

    @Override
    public RecipeType<SoulSakuraSealRecipe> getRecipeType() {
        return SEAL;
    }

    @Nullable
    public SlotType getSlotType() {
        SlotType.SlotCount count = this.getSlots();
        return count == null ? null : count.type();
    }
    @Nullable
    public SlotType.SlotCount getSlots() {
        return null;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("jei.sakuratinker.soul_sakura_seal");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SoulSakuraSealRecipe recipe, IFocusGroup focuses) {

        builder.addSlot(RecipeIngredientRole.INPUT, 3, 33).addItemStack(recipe.getSlimeEarthInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 25, 15).addItemStack(recipe.getSlimeSkyInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 47, 33).addItemStack(recipe.getSlimeNetherInput());
        builder.addSlot(RecipeIngredientRole.INPUT, 43, 58).addItemStack(recipe.getGoldBlockInput());
        List<ItemStack> allParts = ForgeRegistries.ITEMS.getValues()
                .stream()
                .filter(i -> i instanceof IToolPart)
                .map(ItemStack::new)
                .collect(Collectors.toList());
        builder.addSlot(RecipeIngredientRole.INPUT, 7, 58).addItemStacks(allParts);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 3, 3)
                .setCustomRenderer(TConstructJEIConstants.MODIFIER_TYPE, modifierRenderer)
                .addIngredient(TConstructJEIConstants.MODIFIER_TYPE, recipe.getDisplayResult());
        List<ItemStack> allModifiableTools = ForgeRegistries.ITEMS.getValues()
                .stream()
                .filter(item -> item instanceof IModifiable)
                .map(ItemStack::new)
                .toList();

        builder.addSlot(RecipeIngredientRole.CATALYST, 25, 38).addItemStacks(allModifiableTools);
        builder.addSlot(RecipeIngredientRole.CATALYST, 105, 34).addItemStacks(allModifiableTools);

    }
}
