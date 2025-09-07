package com.ssakura49.sakuratinker.common.tools.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class BasicToolInventory implements IItemHandlerModifiable {

    private final ItemStack[] stacks;

    public BasicToolInventory(int slotCount) {
        this.stacks = new ItemStack[slotCount];
        for (int i = 0; i < slotCount; i++) {
            stacks[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public int getSlots() {
        return stacks.length;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= stacks.length) return ItemStack.EMPTY;
        return stacks[slot];
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        if (slot < 0 || slot >= stacks.length) return;
        stacks[slot] = stack;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return true;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (slot < 0 || slot >= stacks.length || stack.isEmpty()) return stack;
        ItemStack existing = stacks[slot];
        if (!existing.isEmpty()) return stack;

        if (!simulate) {
            stacks[slot] = stack.copy();
        }
        return ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < 0 || slot >= stacks.length) return ItemStack.EMPTY;
        ItemStack existing = stacks[slot];
        if (existing.isEmpty()) return ItemStack.EMPTY;

        int extractAmount = Math.min(amount, existing.getCount());
        ItemStack result = existing.copy();
        result.setCount(extractAmount);

        if (!simulate) {
            if (extractAmount >= existing.getCount()) {
                stacks[slot] = ItemStack.EMPTY;
            } else {
                existing.shrink(extractAmount);
            }
        }
        return result;
    }

    public boolean isEmpty(int slot) {
        return slot >= 0 && slot < stacks.length && stacks[slot].isEmpty();
    }
}

