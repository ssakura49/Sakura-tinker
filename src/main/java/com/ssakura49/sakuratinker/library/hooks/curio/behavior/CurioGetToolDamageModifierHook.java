package com.ssakura49.sakuratinker.library.hooks.curio.behavior;

import com.ssakura49.sakuratinker.library.events.ItemStackDamageEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CurioGetToolDamageModifierHook {

    default void onCurioGetToolDamage(IToolStackView curio, ModifierEntry entry, LivingEntity entity, ItemStackDamageEvent event) {
    }

    public static record AllMerger(Collection<CurioGetToolDamageModifierHook> modules) implements CurioGetToolDamageModifierHook {
        public AllMerger(Collection<CurioGetToolDamageModifierHook> modules) {
            this.modules = modules;
        }

        public void onCurioGetToolDamage(IToolStackView curio, ModifierEntry entry, LivingEntity entity, ItemStackDamageEvent event) {
            for(CurioGetToolDamageModifierHook module : this.modules) {
                module.onCurioGetToolDamage(curio,entry, entity, event);
            }
        }
        public Collection<CurioGetToolDamageModifierHook> modules() {
            return this.modules;
        }
    }
}
