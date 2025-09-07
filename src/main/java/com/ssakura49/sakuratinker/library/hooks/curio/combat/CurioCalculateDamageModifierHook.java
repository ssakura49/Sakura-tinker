package com.ssakura49.sakuratinker.library.hooks.curio.combat;

import com.ssakura49.sakuratinker.library.events.LivingCalculateAbsEvent;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CurioCalculateDamageModifierHook {
    default void onCurioCalculateDamage(IToolStackView curio, ModifierEntry entry, LivingCalculateAbsEvent event, LivingEntity attacker, LivingEntity target) {
    }

    public static record AllMerger(Collection<CurioCalculateDamageModifierHook> modules) implements CurioCalculateDamageModifierHook {
        public AllMerger(Collection<CurioCalculateDamageModifierHook> modules) {
            this.modules = modules;
        }

        public void onCurioCalculateDamage(IToolStackView curio, ModifierEntry entry, LivingCalculateAbsEvent event, LivingEntity attacker, LivingEntity target) {
            for(CurioCalculateDamageModifierHook module : this.modules) {
                module.onCurioCalculateDamage(curio,entry, event, attacker, target);
            }

        }
        public Collection<CurioCalculateDamageModifierHook> modules() {
            return this.modules;
        }
    }
}
