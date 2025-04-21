package com.ssakura49.sakuratinker.library.hooks.combat;

import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.Collection;

public interface ModifyDamageSourceModifierHook {
    default LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source){
        return source;
    }
    default LegacyDamageSource modifyArrowDamageSource(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, AbstractArrow arrow, @Nullable LivingEntity attacker, @Nullable Entity target, LegacyDamageSource source){
        return source;
    }
    record AllMerger(Collection<ModifyDamageSourceModifierHook> modules) implements ModifyDamageSourceModifierHook {
        @Override
        public LegacyDamageSource modifyDamageSource(IToolStackView tool, ModifierEntry entry, LivingEntity attacker, InteractionHand hand, Entity target, EquipmentSlot sourceSlot, boolean isFullyCharged, boolean isExtraAttack, boolean isCritical, LegacyDamageSource source) {
            LegacyDamageSource damageSource =source;
            for (ModifyDamageSourceModifierHook module:this.modules){
                damageSource =module.modifyDamageSource(tool, entry,attacker,hand,target,sourceSlot,isFullyCharged,isExtraAttack,isCritical,damageSource);
            }
            return damageSource;
        }
        @Override
        public LegacyDamageSource modifyArrowDamageSource(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, @NotNull AbstractArrow arrow, @Nullable LivingEntity attacker, @Nullable Entity target, LegacyDamageSource source){
            LegacyDamageSource damageSource =source;
            for (ModifyDamageSourceModifierHook module:this.modules){
                damageSource =module.modifyArrowDamageSource(modifiers, persistentData,modifier,arrow,attacker,target,damageSource);
            }
            return damageSource;
        }
    }
}
