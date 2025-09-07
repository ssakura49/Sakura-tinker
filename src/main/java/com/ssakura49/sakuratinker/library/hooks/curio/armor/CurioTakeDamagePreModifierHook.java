package com.ssakura49.sakuratinker.library.hooks.curio.armor;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;
/**
 * Hook - 防御方 受伤前
 * 调用时机：在防御方即将受到伤害前触发（LivingHurtEvent）。
 * 适用场景：伤害减免、免疫特定伤害类型等。
 */
public interface CurioTakeDamagePreModifierHook {
    default void onCurioTakeDamagePre(IToolStackView curio, ModifierEntry entry, LivingHurtEvent event, LivingEntity entity, DamageSource source) {
    }

    public static record AllMerger(Collection<CurioTakeDamagePreModifierHook> modules) implements CurioTakeDamagePreModifierHook {
        public AllMerger(Collection<CurioTakeDamagePreModifierHook> modules) {
            this.modules = modules;
        }


        public void onCurioTakeDamagePre(IToolStackView curio, ModifierEntry entry, LivingHurtEvent event, LivingEntity entity, DamageSource source) {
            for(CurioTakeDamagePreModifierHook module : this.modules) {
                module.onCurioTakeDamagePre(curio,entry, event, entity, source);
            }

        }

        public Collection<CurioTakeDamagePreModifierHook> modules() {
            return this.modules;
        }
    }
}
