package com.ssakura49.sakuratinker.library.logic.context;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

public record ToolData(IToolStackView tool, ItemStack stack) {
    public ToolData(IToolStackView tool, ItemStack stack) {
        this.tool = tool;
        this.stack = stack;
    }

    public Item getItem() {
        return this.stack.getItem();
    }

    public ModDataNBT getNBT() {
        return this.tool.getPersistentData();
    }

    public int getModifierLevel(Modifier modifier) {
        return this.tool.getModifierLevel(modifier);
    }

    public IToolStackView tool() {
        return this.tool;
    }

    public ItemStack stack() {
        return this.stack;
    }
}
