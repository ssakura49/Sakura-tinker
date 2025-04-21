package com.ssakura49.sakuratinker.library.interfaces;

import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.armor.DamageBlockModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ModifyDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.OnAttackedModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.armor.ProtectionModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface ArmorFactory extends DamageBlockModifierHook, OnAttackedModifierHook, ModifyDamageModifierHook, ProtectionModifierHook {
    default void initArmorHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.DAMAGE_BLOCK);
        builder.addHook(this, ModifierHooks.ON_ATTACKED);
        builder.addHook(this, ModifierHooks.MODIFY_DAMAGE);
        builder.addHook(this, ModifierHooks.PROTECTION);
    }
    /**
     *检测伤害是否被阻挡
     */
    default boolean isDamageBlocked(IToolStackView armor, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slot, DamageSource source, float amount) {
        return false;
    }
    /**
     *被攻击时触发
     */
    default void onAttacked(IToolStackView armor, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slot, DamageSource source, float amount, boolean isDirectDamage) {
        this.onTakeAttack(armor, new AttackData(source, context.getEntity(), context, slot), modifier.getLevel(), amount);
    }
    default void onTakeAttack(IToolStackView armor, AttackData data, int level, float amount) {
    }
    /**
     *修改受到的伤害
     */
    default float modifyDamageTaken(IToolStackView armor, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slot, DamageSource source, float amount, boolean isDirectDamage) {
        return this.onModifyTakeDamage(armor, new AttackData(source, context.getEntity(), context, slot), modifier.getLevel(), amount);
    }
    default float onModifyTakeDamage(IToolStackView armor, AttackData data, int level, float amount) {
        return amount;
    }
    /**
     *调整盔甲的伤害减免效果
     */
    default float getProtectionModifier(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slot, DamageSource source, float modifierValue) {
        return modifierValue;
    }
}
