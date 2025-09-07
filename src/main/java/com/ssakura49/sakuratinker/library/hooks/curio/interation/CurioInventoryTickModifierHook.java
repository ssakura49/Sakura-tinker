package com.ssakura49.sakuratinker.library.hooks.curio.interation;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Collection;

public interface CurioInventoryTickModifierHook {
    default void onCurioTick(IToolStackView curio, ModifierEntry entry, SlotContext context, LivingEntity entity, ItemStack stack) {
    }

    public static record AllMerger(Collection<CurioInventoryTickModifierHook> modules) implements CurioInventoryTickModifierHook {
        public AllMerger(Collection<CurioInventoryTickModifierHook> modules) {
            this.modules = modules;
        }

        public void onCurioTick(IToolStackView curio, ModifierEntry entry, SlotContext context, LivingEntity entity, ItemStack stack) {
            for(CurioInventoryTickModifierHook module : this.modules) {
                module.onCurioTick(curio,entry, context, entity, stack);
            }

        }
        public Collection<CurioInventoryTickModifierHook> modules() {
            return this.modules;
        }
    }
}
