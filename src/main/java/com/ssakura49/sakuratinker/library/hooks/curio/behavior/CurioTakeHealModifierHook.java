package com.ssakura49.sakuratinker.library.hooks.curio.behavior;

import com.ssakura49.sakuratinker.library.events.ItemStackDamageEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CurioTakeHealModifierHook {
    default void onCurioTakeHeal(IToolStackView curio, ModifierEntry entry, LivingHealEvent event, LivingEntity entity) {
    }

    public static record AllMerger(Collection<CurioTakeHealModifierHook> modules) implements CurioTakeHealModifierHook {
        public AllMerger(Collection<CurioTakeHealModifierHook> modules) {
            this.modules = modules;
        }


        public void onCurioTakeHeal(IToolStackView curio, ModifierEntry entry, LivingHealEvent event, LivingEntity entity) {
            for(CurioTakeHealModifierHook module : this.modules) {
                module.onCurioTakeHeal(curio,entry, event, entity);
            }
        }
        public Collection<CurioTakeHealModifierHook> modules() {
            return this.modules;
        }
    }
}
