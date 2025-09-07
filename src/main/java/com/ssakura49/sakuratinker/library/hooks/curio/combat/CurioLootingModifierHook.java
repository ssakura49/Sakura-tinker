package com.ssakura49.sakuratinker.library.hooks.curio.combat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Collection;

public interface CurioLootingModifierHook {
    default int onCurioGetLooting(IToolStackView curio, ModifierEntry entry, SlotContext slotContext, DamageSource source, LivingEntity target, ItemStack stack, int looting) {
        return looting;
    }

    public static record AllMerger(Collection<CurioLootingModifierHook> modules) implements CurioLootingModifierHook {
        public AllMerger(Collection<CurioLootingModifierHook> modules) {
            this.modules = modules;
        }

        public int onCurioGetLooting(IToolStackView curio, ModifierEntry entry, SlotContext slotContext, DamageSource source, LivingEntity target, ItemStack stack, int looting) {
            for(CurioLootingModifierHook module : this.modules) {
                module.onCurioGetLooting(curio,entry, slotContext, source, target, stack, looting);
            }

            return looting;
        }

        public Collection<CurioLootingModifierHook> modules() {
            return this.modules;
        }
    }
}
