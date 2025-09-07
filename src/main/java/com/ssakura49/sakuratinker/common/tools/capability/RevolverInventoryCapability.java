package com.ssakura49.sakuratinker.common.tools.capability;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.common.tools.item.RevolverItem;
import com.ssakura49.sakuratinker.library.tinkering.tools.item.ModifiableBulletItem;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

public record RevolverInventoryCapability(Supplier<? extends IToolStackView> tool) implements IItemHandlerModifiable {

    private static final int MAX_BULLETS = 6;

    public static final ResourceLocation BULLET_1 = SakuraTinker.location("bullet_1");
    public static final ResourceLocation BULLET_2 = SakuraTinker.location("bullet_2");
    public static final ResourceLocation BULLET_3 = SakuraTinker.location("bullet_3");
    public static final ResourceLocation BULLET_4 = SakuraTinker.location("bullet_4");
    public static final ResourceLocation BULLET_5 = SakuraTinker.location("bullet_5");
    public static final ResourceLocation BULLET_6 = SakuraTinker.location("bullet_6");

    public static final ResourceLocation[] BULLET_KEYS = new ResourceLocation[] {
            BULLET_1, BULLET_2, BULLET_3, BULLET_4, BULLET_5, BULLET_6
    };

    private static ModDataNBT getData(IToolStackView tool) {
        return tool.getPersistentData();
    }

    /** 获取槽位的子弹 ItemStack */
    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        if (slot < 0 || slot >= MAX_BULLETS) return ItemStack.EMPTY;
        ModDataNBT data = getData(tool.get());
        if (data.contains(BULLET_KEYS[slot])) {
            return ItemStack.of(data.getCompound(BULLET_KEYS[slot]));
        }
        return ItemStack.EMPTY;
    }

    /** 设置槽位的子弹 ItemStack */
    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
        if (slot < 0 || slot >= MAX_BULLETS) return;
        ModDataNBT data = getData(tool.get());
        if (stack.isEmpty()) {
            data.remove(BULLET_KEYS[slot]);
        } else {
            data.put(BULLET_KEYS[slot], stack.save(new CompoundTag()));
        }
    }

    @Override
    public int getSlots() {
        return MAX_BULLETS;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (stack.isEmpty() || slot < 0 || slot >= MAX_BULLETS) return stack;
        ItemStack current = getStackInSlot(slot);
        if (!current.isEmpty()) return stack; // 只允许空槽插入

        if (!simulate) setStackInSlot(slot, stack.copy());
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1; // 每格只能放一颗子弹
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (slot < 0 || slot >= MAX_BULLETS || stack.isEmpty()) return false;
        return stack.getItem() instanceof ModifiableBulletItem;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (slot < 0 || slot >= MAX_BULLETS) return ItemStack.EMPTY;
        ItemStack current = getStackInSlot(slot);
        if (current.isEmpty()) return ItemStack.EMPTY;

        int take = Math.min(amount, current.getCount());
        ItemStack result = ItemHandlerHelper.copyStackWithSize(current, take);
        if (!simulate) {
            current.shrink(take);
            setStackInSlot(slot, current.isEmpty() ? ItemStack.EMPTY : current);
        }
        return result;
    }

    public static class Provider implements ToolCapabilityProvider.IToolCapabilityProvider {
        private final LazyOptional<RevolverInventoryCapability> cap;

        public Provider(Supplier<? extends IToolStackView> tool) {
            this.cap = LazyOptional.of(() -> new RevolverInventoryCapability(tool));
        }

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(IToolStackView tool, @NotNull Capability<T> capability) {
            if (capability == ForgeCapabilities.ITEM_HANDLER) {
                Item item = tool.getItem();
                if (item instanceof RevolverItem) {
                    return cap.cast();
                }
             }
            return LazyOptional.empty();
        }
    }

    public static int getMaxBullets() {
        return MAX_BULLETS;
    }
}
