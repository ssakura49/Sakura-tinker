package com.ssakura49.sakuratinker.library.hooks.combat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface HolderDamageTakeModifierHook {
//    HolderDamageTakeHook EMPTY = new HolderDamageTakeHook() {
//    };

    default void onHolderTakeDamage(IToolStackView tool, ModifierEntry entry, LivingDamageEvent event, LivingEntity entity, DamageSource source) {
    }

    public static record AllMerge(Collection<HolderDamageTakeModifierHook> modules) implements HolderDamageTakeModifierHook {
        public AllMerge(Collection<HolderDamageTakeModifierHook> modules) {
            this.modules = modules;
        }

        public void onHolderTakeDamage(IToolStackView tool, ModifierEntry entry, LivingDamageEvent event, LivingEntity entity, DamageSource source) {
            for(HolderDamageTakeModifierHook module : this.modules) {
                module.onHolderTakeDamage(tool, entry, event, entity, source);
            }

        }

        public Collection<HolderDamageTakeModifierHook> modules() {
            return this.modules;
        }
    }
}
