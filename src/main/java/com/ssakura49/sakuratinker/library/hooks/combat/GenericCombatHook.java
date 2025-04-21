package com.ssakura49.sakuratinker.library.hooks.combat;

import com.ssakura49.sakuratinker.library.events.LivingCalculateAbsEvent;
import com.ssakura49.sakuratinker.library.events.TinkerToolCriticalEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface GenericCombatHook {

    default void onMeleeCriticalHit(IToolStackView tool, TinkerToolCriticalEvent event, LivingEntity attacker, LivingEntity target, int level) {
    }

    default void onCalculateDamage(IToolStackView tool, LivingCalculateAbsEvent event, LivingEntity attacker, LivingEntity target, int level) {
    }

    default void onKillLivingTarget(IToolStackView tool, LivingDeathEvent event, LivingEntity attacker, LivingEntity target, int level) {
    }

    public static record AllMerge(Collection<GenericCombatHook> modules) implements GenericCombatHook {
        public AllMerge(Collection<GenericCombatHook> modules) {
            this.modules = modules;
        }

        public void onMeleeCriticalHit(IToolStackView tool, TinkerToolCriticalEvent event, LivingEntity attacker, LivingEntity target, int level) {
            for(GenericCombatHook module : this.modules) {
                module.onMeleeCriticalHit(tool, event, attacker, target, level);
            }

        }

        public void onCalculateDamage(IToolStackView tool, LivingCalculateAbsEvent event, LivingEntity attacker, LivingEntity target, int level) {
            for(GenericCombatHook module : this.modules) {
                module.onCalculateDamage(tool, event, attacker, target, level);
            }

        }

        public void onKillLivingTarget(IToolStackView tool, LivingDeathEvent event, LivingEntity attacker, LivingEntity target, int level) {
            for(GenericCombatHook module : this.modules) {
                module.onKillLivingTarget(tool, event, attacker, target, level);
            }

        }

        public Collection<GenericCombatHook> modules() {
            return this.modules;
        }
    }
}
