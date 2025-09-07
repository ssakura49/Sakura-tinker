package com.ssakura49.sakuratinker.library.hooks.curio.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;
/**
 * Hook - 攻击方 最终伤害后
 * 调用时机：在伤害值已经确定并即将应用到目标生命值之后触发（LivingDamageEvent）。
 * 适用场景：附加流血、中毒等持续性效果。
 */
public interface CurioDamageTargetPostModifierHook {
    default void onCurioDamageTargetPost(IToolStackView curio, ModifierEntry entry, LivingDamageEvent event, LivingEntity attacker, LivingEntity target) {
    }

    public static record AllMerger(Collection<CurioDamageTargetPostModifierHook> modules) implements CurioDamageTargetPostModifierHook {
        public AllMerger(Collection<CurioDamageTargetPostModifierHook> modules) {
            this.modules = modules;
        }

        public void onCurioDamageTargetPost(IToolStackView curio, ModifierEntry entry, LivingDamageEvent event, LivingEntity attacker, LivingEntity target) {
            for(CurioDamageTargetPostModifierHook module : this.modules) {
                module.onCurioDamageTargetPost(curio,entry, event, attacker, target);
            }

        }

        public Collection<CurioDamageTargetPostModifierHook> modules() {
            return this.modules;
        }
    }
}
