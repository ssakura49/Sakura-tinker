package com.ssakura49.sakuratinker.library.hooks.armor;

import net.minecraftforge.event.entity.living.LivingHealEvent;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface WearerTakeHealHook {
//    WearerTakeHealHook EMPTY = new WearerTakeHealHook() {
//    };

    default void onTakeHeal(IToolStackView armor, LivingHealEvent event, EquipmentContext context, int level) {
    }

    public static record AllMerger(Collection<WearerTakeHealHook> modules) implements WearerTakeHealHook {
        public AllMerger(Collection<WearerTakeHealHook> modules) {
            this.modules = modules;
        }

        public void onTakeHeal(IToolStackView armor, LivingHealEvent event, EquipmentContext context, int level) {
            for(WearerTakeHealHook module : this.modules) {
                module.onTakeHeal(armor, event, context, level);
            }

        }

        public Collection<WearerTakeHealHook> modules() {
            return this.modules;
        }
    }
}
