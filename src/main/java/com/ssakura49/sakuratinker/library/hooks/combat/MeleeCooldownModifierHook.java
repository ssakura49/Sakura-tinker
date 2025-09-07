package com.ssakura49.sakuratinker.library.hooks.combat;

import com.ssakura49.sakuratinker.library.events.AttackSpeedModifyEvent;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface MeleeCooldownModifierHook {

    default void modifyAttackCooldown(IToolStackView tool, ModifierEntry entry, Player player, AttackSpeedModifyEvent event) {
    }

    public static record AllMerger(Collection<MeleeCooldownModifierHook> modules) implements MeleeCooldownModifierHook {
        public AllMerger(Collection<MeleeCooldownModifierHook> modules) {
            this.modules = modules;
        }

        public void modifyAttackCooldown(IToolStackView tool, ModifierEntry entry, Player player, AttackSpeedModifyEvent event) {
            for(MeleeCooldownModifierHook module : this.modules) {
                module.modifyAttackCooldown(tool, entry, player, event);
            }

        }

        public Collection<MeleeCooldownModifierHook> modules() {
            return this.modules;
        }
    }
}
