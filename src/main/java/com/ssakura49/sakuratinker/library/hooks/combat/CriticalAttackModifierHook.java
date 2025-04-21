package com.ssakura49.sakuratinker.library.hooks.combat;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CriticalAttackModifierHook {
    boolean setCritical(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical);

    record FirstMerger(Collection<CriticalAttackModifierHook> modules) implements CriticalAttackModifierHook {
        @Override
        public boolean setCritical(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack,boolean isCritical) {
            for (CriticalAttackModifierHook module:this.modules){
                isCritical =module.setCritical(tool,entry,attacker,hand,target,sourceSlot,isFullyCharged,isExtraAttack,isCritical);
                if (isCritical) return isCritical;
            }
            return isCritical;
        }
    }

}
