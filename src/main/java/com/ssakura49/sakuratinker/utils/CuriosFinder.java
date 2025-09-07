package com.ssakura49.sakuratinker.utils;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;
import top.theillusivec4.curios.mixin.CuriosImplMixinHooks;

import java.util.Optional;
import java.util.function.Predicate;

public class CuriosFinder {
    public static boolean hasCurio(LivingEntity livingEntity, Predicate<ItemStack> filter) {
        return !findCurio(livingEntity, filter).isEmpty();
    }
    public static ItemStack findCurio(LivingEntity livingEntity, Predicate<ItemStack> filter) {
        ItemStack foundStack = ItemStack.EMPTY;
        if (livingEntity != null ) {
            Optional<SlotResult> slotResult = CuriosApi.getCuriosInventory(livingEntity).map((inv) -> inv.findFirstCurio(filter)).orElse(Optional.empty());
            if (slotResult.isPresent()) {
                foundStack = slotResult.get().stack();
                if (!CuriosImplMixinHooks.isStackValid(slotResult.get().slotContext(), foundStack))
                    return ItemStack.EMPTY;
            }
        }

        return foundStack;
    }
}
