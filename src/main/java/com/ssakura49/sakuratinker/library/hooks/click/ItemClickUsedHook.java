package com.ssakura49.sakuratinker.library.hooks.click;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Collection;

public interface ItemClickUsedHook {

    default void onInputKey(ToolStack tool, Player player, int level, ItemStack stack) {
    }

    public static record AllMerge(Collection<ItemClickUsedHook> modules) implements ItemClickUsedHook {
        public AllMerge(Collection<ItemClickUsedHook> modules) {
            this.modules = modules;
        }
        public void onInputKey(ToolStack tool, Player player, int level, ItemStack stack) {
            for(ItemClickUsedHook module : this.modules) {
                module.onInputKey(tool, player, level, stack);
            }

        }
        public Collection<ItemClickUsedHook> modules() {
            return this.modules;
        }
    }
}
