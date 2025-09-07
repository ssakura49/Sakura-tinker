package com.ssakura49.sakuratinker.library.hooks.curio.armor;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Collection;

public interface CurioEquipmentChangeModifierHook {

    default void onCurioEquip(IToolStackView curio, ModifierEntry entry, SlotContext context, LivingEntity entity, ItemStack prevStack, ItemStack stack) {
    }
    default void onCurioUnequip(IToolStackView curio, ModifierEntry entry, SlotContext context, LivingEntity entity, ItemStack newStack, ItemStack stack) {
    }

    public static record AllMerger(Collection<CurioEquipmentChangeModifierHook> modules) implements CurioEquipmentChangeModifierHook {
        public AllMerger(Collection<CurioEquipmentChangeModifierHook> modules) {
            this.modules = modules;
        }

        public void onCurioEquip(IToolStackView curio, ModifierEntry entry, SlotContext context, LivingEntity entity, ItemStack prevStack, ItemStack stack) {
            for(CurioEquipmentChangeModifierHook module : this.modules) {
                module.onCurioEquip(curio,entry, context, entity, prevStack, stack);
            }

        }

        public void onCurioUnequip(IToolStackView curio, ModifierEntry entry, SlotContext context, LivingEntity entity, ItemStack newStack, ItemStack stack) {
            for(CurioEquipmentChangeModifierHook module : this.modules) {
                module.onCurioUnequip(curio,entry, context, entity, newStack, stack);
            }

        }

        public Collection<CurioEquipmentChangeModifierHook> modules() {
            return this.modules;
        }
    }
}
