package com.ssakura49.sakuratinker.library.hooks.armor;

import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface WearerDamageTakeHook {
//    WearerDamageTakeHook EMPTY = new WearerDamageTakeHook() {
//    };

    default void onTakeDamagePre(IToolStackView armor, LivingHurtEvent event, AttackData data, int level) {
    }

    default void onTakeDamagePost(IToolStackView armor, LivingDamageEvent event, AttackData data, int level) {
    }

    public static record AllMerger(Collection<WearerDamageTakeHook> modules) implements WearerDamageTakeHook {
        public AllMerger(Collection<WearerDamageTakeHook> modules) {
            this.modules = modules;
        }

        public void onTakeDamagePre(IToolStackView armor, LivingHurtEvent event, AttackData data, int level) {
            for(WearerDamageTakeHook module : this.modules) {
                module.onTakeDamagePre(armor, event, data, level);
            }

        }

        public void onTakeDamagePost(IToolStackView armor, LivingDamageEvent event, AttackData data, int level) {
            for(WearerDamageTakeHook module : this.modules) {
                module.onTakeDamagePost(armor, event, data, level);
            }

        }

        public Collection<WearerDamageTakeHook> modules() {
            return this.modules;
        }
    }
}
