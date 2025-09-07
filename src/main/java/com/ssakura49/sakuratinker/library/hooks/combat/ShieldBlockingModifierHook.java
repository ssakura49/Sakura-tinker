package com.ssakura49.sakuratinker.library.hooks.combat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface ShieldBlockingModifierHook {
//    ShieldBlockingHook EMPTY = new ShieldBlockingHook() {
//    };

    default void onShieldBlocked(IToolStackView shield, ModifierEntry entry, ShieldBlockEvent event, Player player, DamageSource source) {
    }

    public static record AllMerge(Collection<ShieldBlockingModifierHook> modules) implements ShieldBlockingModifierHook {
        public AllMerge(Collection<ShieldBlockingModifierHook> modules) {
            this.modules = modules;
        }

        public void onShieldBlocked(IToolStackView shield, ModifierEntry entry, ShieldBlockEvent event, Player player, DamageSource source) {
            for(ShieldBlockingModifierHook module : this.modules) {
                module.onShieldBlocked(shield, entry, event, player, source);
            }

        }

        public Collection<ShieldBlockingModifierHook> modules() {
            return this.modules;
        }
    }
}
