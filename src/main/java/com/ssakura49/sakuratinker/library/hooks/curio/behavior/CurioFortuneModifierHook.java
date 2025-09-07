package com.ssakura49.sakuratinker.library.hooks.curio.behavior;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

import java.util.Collection;

public interface CurioFortuneModifierHook {

    default int onCurioGetFortune(IToolStackView curio, ModifierEntry entry, SlotContext slotContext, LootContext lootContext, ItemStack stack, int fortune) {
        return fortune;
    }

    public static record AllMerger(Collection<CurioFortuneModifierHook> modules) implements CurioFortuneModifierHook {
        public AllMerger(Collection<CurioFortuneModifierHook> modules) {
            this.modules = modules;
        }

        public int onCurioGetFortune(IToolStackView curio, ModifierEntry entry, SlotContext slotContext, LootContext lootContext, ItemStack stack, int fortune) {
            for(CurioFortuneModifierHook module : this.modules) {
                module.onCurioGetFortune(curio,entry, slotContext, lootContext, stack, fortune);
            }

            return fortune;
        }

        public Collection<CurioFortuneModifierHook> modules() {
            return this.modules;
        }
    }
}