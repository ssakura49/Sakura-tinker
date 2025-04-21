package com.ssakura49.sakuratinker.library.interfaces;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface CombatFactory extends MeleeDamageModifierHook, MeleeHitModifierHook {
    default void initCombatHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_DAMAGE, ModifierHooks.MELEE_HIT);
    }
    /**
     *武器击中之前，使用onBeforeMeleeHit
     **/
    default float beforeMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damage, float baseKnockback, float knockback) {
        return !context.getAttacker().level().isClientSide() && context.getLivingTarget() != null ? this.onBeforeMeleeHit(tool, modifier.getLevel(), context, context.getAttacker(), context.getLivingTarget(), damage, baseKnockback, knockback) : knockback;
    }
    default float onBeforeMeleeHit(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float damage, float baseKnockback, float knockback) {
        return knockback;
    }
    /**
     *武器击中之后，使用onAfterMeleeHit
     **/
    default void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        if (!context.getAttacker().level().isClientSide() && context.getLivingTarget() != null) {
            this.onAfterMeleeHit(tool, modifier.getLevel(), context, context.getAttacker(), context.getLivingTarget(), damageDealt);
        }
    }
    default void onAfterMeleeHit(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float damageDealt) {
    }
    /**
     *武器无法击中，使用onFailedMeleeHit
     **/
    default void failedMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageAttempted) {
        if (!context.getAttacker().level().isClientSide() && context.getLivingTarget() != null) {
            this.onFailedMeleeHit(tool, modifier.getLevel(), context, context.getAttacker(), context.getLivingTarget(), damageAttempted);
        }
    }
    default void onFailedMeleeHit(IToolStackView tool, int level, ToolAttackContext context,LivingEntity attacker, LivingEntity target, float damageAttempted) {
    }
    /**
     *计算武器伤害
     **/
    default float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return !context.getAttacker().level().isClientSide() && context.getLivingTarget() != null ? this.onModifyMeleeDamage(tool, modifier.getLevel(), context, context.getAttacker(), context.getLivingTarget(), baseDamage, damage) : damage;
    }
    default float onModifyMeleeDamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        return actualDamage;
    }
    /**
     *造成伤害触发
     **/
    default void onDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, LivingEntity target, DamageSource source, float amount, boolean isDirectDamage) {
        this.onModifierDamageDealt(tool, modifier, context, slotType, target, source, amount, isDirectDamage);
    }
    default void onModifierDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, LivingEntity entity, DamageSource damageSource, float amount, boolean isDirectDamage) {
    }
}
