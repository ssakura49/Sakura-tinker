package com.ssakura49.sakuratinker.common.items.curios;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

import java.util.Map;

public class CuriosBaseItem extends Item implements ICurioItem {
    public CuriosBaseItem(Item.Properties properties) {
        super(properties);
    }

    @NotNull
    public ICurio.@NotNull SoundInfo getEquipSound(SlotContext slotContext, ItemStack stack) {
        return new ICurio.SoundInfo(SoundEvents.ARMOR_EQUIP_CHAIN, 1.0F, 1.0F);
    }
    public boolean isEquippedBy(LivingEntity entity) {
        return CuriosApi.getCuriosInventory(entity).map(handler -> {
            for (Map.Entry<String, ICurioStacksHandler> entry : handler.getCurios().entrySet()) {
                ICurioStacksHandler stacksHandler = entry.getValue();
                for (int i = 0; i < stacksHandler.getSlots(); i++) {
                    ItemStack stack = stacksHandler.getStacks().getStackInSlot(i);
                    if (stack.getItem() == this) {
                        return true;
                    }
                }
            }
            return false;
        }).orElse(false);
    }
}
