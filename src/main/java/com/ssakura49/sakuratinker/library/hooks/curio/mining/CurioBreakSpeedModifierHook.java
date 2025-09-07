package com.ssakura49.sakuratinker.library.hooks.curio.mining;

import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CurioBreakSpeedModifierHook {

    default void onCurioBreakSpeed(IToolStackView curio, ModifierEntry entry, PlayerEvent.BreakSpeed event, Player player) {
    }

    public static record AllMerger(Collection<CurioBreakSpeedModifierHook> modules) implements CurioBreakSpeedModifierHook {
        public AllMerger(Collection<CurioBreakSpeedModifierHook> modules) {
            this.modules = modules;
        }


        public void onCurioBreakSpeed(IToolStackView curio, ModifierEntry entry, PlayerEvent.BreakSpeed event, Player player) {
            for(CurioBreakSpeedModifierHook module : this.modules) {
                module.onCurioBreakSpeed(curio,entry, event, player);
            }

        }

        public Collection<CurioBreakSpeedModifierHook> modules() {
            return this.modules;
        }
    }
}
