package com.ssakura49.sakuratinker.library.hooks.combat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface ShieldBlockingHook {
//    ShieldBlockingHook EMPTY = new ShieldBlockingHook() {
//    };

    default void onShieldBlocked(IToolStackView shield, ShieldBlockEvent event, Player player, DamageSource source, int level) {
    }

    public static record AllMerge(Collection<ShieldBlockingHook> modules) implements ShieldBlockingHook {
        public AllMerge(Collection<ShieldBlockingHook> modules) {
            this.modules = modules;
        }

        public void onShieldBlocked(IToolStackView shield, ShieldBlockEvent event, Player player, DamageSource source, int level) {
            for(ShieldBlockingHook module : this.modules) {
                module.onShieldBlocked(shield, event, player, source, level);
            }

        }

        public Collection<ShieldBlockingHook> modules() {
            return this.modules;
        }
    }
}
