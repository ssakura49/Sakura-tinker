package com.ssakura49.sakuratinker.library.hooks.curio.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

/**
 * Hook - 攻击方 伤害前
 * 调用时机：在攻击方即将对目标造成伤害前触发（LivingHurtEvent）。
 * 适用场景：修改即将造成的伤害值、附加特殊效果等。
 */
public interface CurioDamageTargetPreModifierHook {
    default void onDamageTargetPre(IToolStackView curio, ModifierEntry entry, LivingHurtEvent event, LivingEntity attacker, LivingEntity target) {}

    record AllMerger(Collection<CurioDamageTargetPreModifierHook> modules) implements CurioDamageTargetPreModifierHook {
        @Override
        public void onDamageTargetPre(IToolStackView curio, ModifierEntry entry, LivingHurtEvent event, LivingEntity attacker, LivingEntity target) {
            for (CurioDamageTargetPreModifierHook module : modules) {
                module.onDamageTargetPre(curio, entry, event, attacker, target);
            }
        }

        public Collection<CurioDamageTargetPreModifierHook> modules() {
            return this.modules;
        }
    }
}
