package com.ssakura49.sakuratinker.library.hooks.combat;

import com.ssakura49.sakuratinker.library.events.LivingCalculateAbsEvent;
import com.ssakura49.sakuratinker.library.events.TinkerToolCriticalEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface GenericCombatModifierHook {

    default void onMeleeCriticalHit(IToolStackView tool, ModifierEntry entry, TinkerToolCriticalEvent event, LivingEntity attacker, LivingEntity target) {
    }

    default void onCalculateDamage(IToolStackView tool, ModifierEntry entry, LivingCalculateAbsEvent event, LivingEntity attacker, LivingEntity target) {
    }

    default void onKillLivingTarget(IToolStackView tool, ModifierEntry entry, LivingDeathEvent event, LivingEntity attacker, LivingEntity target) {
    }

    public static record AllMerge(Collection<GenericCombatModifierHook> modules) implements GenericCombatModifierHook {
        public AllMerge(Collection<GenericCombatModifierHook> modules) {
            this.modules = modules;
        }

        public void onMeleeCriticalHit(IToolStackView tool, ModifierEntry entry, TinkerToolCriticalEvent event, LivingEntity attacker, LivingEntity target) {
            for(GenericCombatModifierHook module : this.modules) {
                module.onMeleeCriticalHit(tool, entry, event, attacker, target);
            }

        }

        public void onCalculateDamage(IToolStackView tool, ModifierEntry entry, LivingCalculateAbsEvent event, LivingEntity attacker, LivingEntity target) {
            for(GenericCombatModifierHook module : this.modules) {
                module.onCalculateDamage(tool, entry, event, attacker, target);
            }

        }

        public void onKillLivingTarget(IToolStackView tool, ModifierEntry entry, LivingDeathEvent event, LivingEntity attacker, LivingEntity target) {
            for(GenericCombatModifierHook module : this.modules) {
                module.onKillLivingTarget(tool, entry, event, attacker, target);
            }

        }

        public Collection<GenericCombatModifierHook> modules() {
            return this.modules;
        }
    }
}
