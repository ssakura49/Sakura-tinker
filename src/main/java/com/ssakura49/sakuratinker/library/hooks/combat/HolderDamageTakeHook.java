package com.ssakura49.sakuratinker.library.hooks.combat;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface HolderDamageTakeHook {
//    HolderDamageTakeHook EMPTY = new HolderDamageTakeHook() {
//    };

    default void onHolderTakeDamage(IToolStackView tool, LivingDamageEvent event, LivingEntity entity, DamageSource source, int level) {
    }

    public static record AllMerge(Collection<HolderDamageTakeHook> modules) implements HolderDamageTakeHook {
        public AllMerge(Collection<HolderDamageTakeHook> modules) {
            this.modules = modules;
        }

        public void onHolderTakeDamage(IToolStackView tool, LivingDamageEvent event, LivingEntity entity, DamageSource source, int level) {
            for(HolderDamageTakeHook module : this.modules) {
                module.onHolderTakeDamage(tool, event, entity, source, level);
            }

        }

        public Collection<HolderDamageTakeHook> modules() {
            return this.modules;
        }
    }
}
