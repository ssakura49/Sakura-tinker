package com.ssakura49.sakuratinker.library.hooks.armor;

import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface WearerKnockBackHook {
//    WearerKnockBackHook EMPTY = new WearerKnockBackHook() {
//    };

    default void onKnockBack(IToolStackView armor, LivingKnockBackEvent event, EquipmentContext context, int level) {
    }

    public static record AllMerger(Collection<WearerKnockBackHook> modules) implements WearerKnockBackHook {
        public AllMerger(Collection<WearerKnockBackHook> modules) {
            this.modules = modules;
        }

        public void onKnockBack(IToolStackView armor, LivingKnockBackEvent event, EquipmentContext context, int level) {
            for(WearerKnockBackHook module : this.modules) {
                module.onKnockBack(armor, event, context, level);
            }

        }

        public Collection<WearerKnockBackHook> modules() {
            return this.modules;
        }
    }
}
