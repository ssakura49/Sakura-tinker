package com.ssakura49.sakuratinker.library.hooks.curio.ranged;

import com.ssakura49.sakuratinker.library.logic.context.ProjectileImpactContent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.Collection;

public interface CurioArrowHitHook {

    //箭击中时触发
    default void onCurioArrowHit(IToolStackView curio, ModifierEntry entry, LivingEntity shooter, ProjectileImpactContent data) {
    }

    public static record AllMerger(Collection<CurioArrowHitHook> modules) implements CurioArrowHitHook {
        public AllMerger(Collection<CurioArrowHitHook> modules) {
            this.modules = modules;
        }


        public void onCurioArrowHit(IToolStackView curio, ModifierEntry entry, LivingEntity shooter, ProjectileImpactContent data) {
            for(CurioArrowHitHook module : this.modules) {
                module.onCurioArrowHit(curio, entry, shooter, data);
            }

        }

        public Collection<CurioArrowHitHook> modules() {
            return this.modules;
        }
    }
}
