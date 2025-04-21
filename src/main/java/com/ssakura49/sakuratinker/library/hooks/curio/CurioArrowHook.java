package com.ssakura49.sakuratinker.library.hooks.curio;

import com.ssakura49.sakuratinker.library.logic.context.ImpactData;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.Collection;

public interface CurioArrowHook {

    //射箭时触发
    default void onCurioShootArrow(IToolStackView curio, LivingEntity shooter, AbstractArrow arrow, ModDataNBT persistentData, int level) {
    }
    //箭击中时触发
    default void onCurioArrowHit(IToolStackView curio, LivingEntity shooter, ImpactData data, int level) {
    }

    public static record AllMerger(Collection<CurioArrowHook> modules) implements CurioArrowHook {
        public AllMerger(Collection<CurioArrowHook> modules) {
            this.modules = modules;
        }

        public void onCurioShootArrow(IToolStackView curio, LivingEntity shooter, AbstractArrow arrow, ModDataNBT persistentData, int level) {
            for(CurioArrowHook module : this.modules) {
                module.onCurioShootArrow(curio, shooter, arrow, persistentData, level);
            }

        }

        public void onCurioArrowHit(IToolStackView curio, LivingEntity shooter, ImpactData data, int level) {
            for(CurioArrowHook module : this.modules) {
                module.onCurioArrowHit(curio, shooter, data, level);
            }

        }

        public Collection<CurioArrowHook> modules() {
            return this.modules;
        }
    }
}
