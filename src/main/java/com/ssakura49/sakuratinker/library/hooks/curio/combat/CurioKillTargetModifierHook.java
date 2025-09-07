package com.ssakura49.sakuratinker.library.hooks.curio.combat;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;
/**
 * Hook - 攻击方 击杀目标
 * 调用时机：目标即将死亡时触发（LivingDeathEvent）。
 * 适用场景：掉落额外战利品、击杀回血、触发连击技能等。
 */
public interface CurioKillTargetModifierHook {
    default void onCurioToKillTarget(IToolStackView curio, ModifierEntry entry, LivingDeathEvent event, LivingEntity attacker, LivingEntity target) {
    }

    public static record AllMerger(Collection<CurioKillTargetModifierHook> modules) implements CurioKillTargetModifierHook {
        public AllMerger(Collection<CurioKillTargetModifierHook> modules) {
            this.modules = modules;
        }

        public void onCurioToKillTarget(IToolStackView curio, ModifierEntry entry, LivingDeathEvent event, LivingEntity attacker, LivingEntity target) {
            for(CurioKillTargetModifierHook module : this.modules) {
                module.onCurioToKillTarget(curio,entry, event, attacker, target);
            }

        }


        public Collection<CurioKillTargetModifierHook> modules() {
            return this.modules;
        }
    }
}
