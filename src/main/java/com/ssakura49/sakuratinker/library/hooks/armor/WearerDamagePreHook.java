package com.ssakura49.sakuratinker.library.hooks.armor;

import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface WearerDamagePreHook {
//    WearerDamagePreHook EMPTY = new WearerDamagePreHook() {
//    };

    default void onDamagePre(IToolStackView armor, LivingHurtEvent event, AttackData data, int level) {
    }

    public static record AllMerger(Collection<WearerDamagePreHook> modules) implements WearerDamagePreHook {
        public AllMerger(Collection<WearerDamagePreHook> modules) {
            this.modules = modules;
        }

        public void onDamagePre(IToolStackView armor, LivingHurtEvent event, AttackData data, int level) {
            for(WearerDamagePreHook module : this.modules) {
                module.onDamagePre(armor, event, data, level);
            }

        }

        public Collection<WearerDamagePreHook> modules() {
            return this.modules;
        }
    }
}