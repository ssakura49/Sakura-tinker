package com.ssakura49.sakuratinker.library.hooks.combat;

import com.ssakura49.sakuratinker.library.events.AttackSpeedModifyEvent;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface MeleeCooldownHook {

    default void modifyAttackCooldown(IToolStackView tool, Player player, AttackSpeedModifyEvent event, int level) {
    }

    public static record AllMerger(Collection<MeleeCooldownHook> modules) implements MeleeCooldownHook {
        public AllMerger(Collection<MeleeCooldownHook> modules) {
            this.modules = modules;
        }

        public void modifyAttackCooldown(IToolStackView tool, Player player, AttackSpeedModifyEvent event, int level) {
            for(MeleeCooldownHook module : this.modules) {
                module.modifyAttackCooldown(tool, player, event, level);
            }

        }

        public Collection<MeleeCooldownHook> modules() {
            return this.modules;
        }
    }
}
