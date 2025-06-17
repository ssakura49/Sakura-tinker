package com.ssakura49.sakuratinker.library.hooks.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CauseDamageModifierHook {
    float onCauseDamage(IToolStackView tool, ModifierEntry modifier, LivingHurtEvent event, LivingEntity attacker, LivingEntity target, float baseDamage, float currentDamage);

    public static record AllMerger(Collection<CauseDamageModifierHook> modules) implements CauseDamageModifierHook {
        public AllMerger(Collection<CauseDamageModifierHook> modules) {
            this.modules = modules;
        }

        public float onCauseDamage(IToolStackView tool, ModifierEntry modifier, LivingHurtEvent event, LivingEntity attacker, LivingEntity target, float baseDamage, float currentDamage) {
            for (CauseDamageModifierHook module : this.modules) {
                currentDamage = module.onCauseDamage(tool, modifier, event, attacker, target, baseDamage, currentDamage);
            }
            return currentDamage;
        }

        public Collection<CauseDamageModifierHook> modules() {
            return modules;
        }
    }
}
