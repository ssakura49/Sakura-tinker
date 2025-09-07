package com.ssakura49.sakuratinker.library.hooks.curio.ranged;

import com.ssakura49.sakuratinker.library.logic.context.ProjectileImpactContent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.Collection;

public interface CurioArrowShootHook {

    //射箭时触发
    default void onCurioShootArrow(IToolStackView curio, ModifierEntry entry, LivingEntity shooter, AbstractArrow arrow, ModDataNBT persistentData) {
    }

    public static record AllMerger(Collection<CurioArrowShootHook> modules) implements CurioArrowShootHook {
        public AllMerger(Collection<CurioArrowShootHook> modules) {
            this.modules = modules;
        }

        public void onCurioShootArrow(IToolStackView curio, ModifierEntry entry, LivingEntity shooter, AbstractArrow arrow, ModDataNBT persistentData) {
            for(CurioArrowShootHook module : this.modules) {
                module.onCurioShootArrow(curio,entry, shooter, arrow, persistentData);
            }
        }

        public Collection<CurioArrowShootHook> modules() {
            return this.modules;
        }
    }
}
