package com.ssakura49.sakuratinker.client.menu;

import com.ssakura49.sakuratinker.common.tools.capability.RevolverInventoryCapability;
import com.ssakura49.sakuratinker.common.tools.item.RevolverItem;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableBulletItem;
import com.ssakura49.sakuratinker.register.STMenus;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nonnull;

public class RevolverMenu extends AbstractContainerMenu {
    private final Player player;
    private final ToolStack tool;
    private final RevolverInventoryCapability capability;

    public static final int SLOT_SIZE = 6;

    public RevolverMenu(int id, Inventory playerInv, ItemStack stack) {
        super(STMenus.REVOLVER.get(), id);
        this.player = playerInv.player;
        this.tool = ToolStack.from(stack);
        this.capability = stack.getCapability(ForgeCapabilities.ITEM_HANDLER)
                .map(h -> (RevolverInventoryCapability) h)
                .orElseGet(() -> new RevolverInventoryCapability(() -> this.tool));

        for (int i = 0; i < SLOT_SIZE; i++) {
            int x = 8 + i * 18;
            int y = 20;
            this.addSlot(new SlotItemHandler(capability, i, x, y) {
                @Override
                public boolean mayPlace(@Nonnull ItemStack stack) {
                    return stack.getItem() instanceof ModifiableBulletItem;
                }

                @Override
                public int getMaxStackSize() {
                    return 1;
                }
            });
        }

        int startX = 8;
        int startY = 50;
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(playerInv, col + row * 9 + 9, startX + col * 18, startY + row * 18));
            }
        }

        int hotBarY = 108;
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInv, col, startX + col * 18, hotBarY));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return player.getItemInHand(player.getUsedItemHand()).getItem() instanceof RevolverItem;
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot == null || !slot.hasItem()) return ItemStack.EMPTY;

        ItemStack stackInSlot = slot.getItem();
        ItemStack copy = stackInSlot.copy();

        if (index < SLOT_SIZE) {
            // 子弹槽 -> 玩家背包
            if (!moveItemStackTo(stackInSlot, SLOT_SIZE, this.slots.size(), true)) return ItemStack.EMPTY;
        } else {
            // 玩家背包 -> 子弹槽
            if (!moveItemStackTo(stackInSlot, 0, SLOT_SIZE, false)) return ItemStack.EMPTY;
        }

        if (stackInSlot.isEmpty()) slot.set(ItemStack.EMPTY);
        else slot.setChanged();

        return copy;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        // capability已经写NBT
    }

    public Player getPlayer() {
        return player;
    }
}