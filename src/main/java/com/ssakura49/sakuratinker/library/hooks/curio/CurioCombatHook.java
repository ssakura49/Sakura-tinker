package com.ssakura49.sakuratinker.library.hooks.curio;

import com.ssakura49.sakuratinker.library.events.LivingCalculateAbsEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Collection;

public interface CurioCombatHook {
    //攻击者对目标造成伤害之前触发
    default void onDamageTargetPre(IToolStackView curio, LivingHurtEvent event, LivingEntity attacker, LivingEntity target, int level) {
    }
    //计算伤害时触发
    default void onCurioCalculateDamage(IToolStackView curio, LivingCalculateAbsEvent event, LivingEntity attacker, LivingEntity target, int level) {
    }
    //攻击者对目标造成伤害之后触发
    default void onCurioToDamagePost(IToolStackView curio, LivingDamageEvent event, LivingEntity attacker, LivingEntity target, int level) {
    }
    //攻击者击杀目标时触发
    default void onCurioToKillTarget(IToolStackView curio, LivingDeathEvent event, LivingEntity attacker, LivingEntity target, int level) {
    }
    //实体受到伤害之前触发
    default void onCurioTakeDamagePre(IToolStackView curio, LivingHurtEvent event, LivingEntity entity, DamageSource source, int level) {
    }
    //实体受到伤害之后触发
    default void onCurioTakeDamagePost(IToolStackView curio, LivingDamageEvent event, LivingEntity entity, DamageSource source, int level) {
    }

    public static record AllMerger(Collection<CurioCombatHook> modules) implements CurioCombatHook {
        public AllMerger(Collection<CurioCombatHook> modules) {
            this.modules = modules;
        }

        public void onDamageTargetPre(IToolStackView curio, LivingHurtEvent event, LivingEntity attacker, LivingEntity target, int level) {
            for(CurioCombatHook module : this.modules) {
                module.onDamageTargetPre(curio, event, attacker, target, level);
            }

        }

        public void onCurioCalculateDamage(IToolStackView curio, LivingCalculateAbsEvent event, LivingEntity attacker, LivingEntity target, int level) {
            for(CurioCombatHook module : this.modules) {
                module.onCurioCalculateDamage(curio, event, attacker, target, level);
            }

        }

        public void onCurioToDamagePost(IToolStackView curio, LivingDamageEvent event, LivingEntity attacker, LivingEntity target, int level) {
            for(CurioCombatHook module : this.modules) {
                module.onCurioToDamagePost(curio, event, attacker, target, level);
            }

        }

        public void onCurioToKillTarget(IToolStackView curio, LivingDeathEvent event, LivingEntity attacker, LivingEntity target, int level) {
            for(CurioCombatHook module : this.modules) {
                module.onCurioToKillTarget(curio, event, attacker, target, level);
            }

        }

        public void onCurioTakeDamagePre(IToolStackView curio, LivingHurtEvent event, LivingEntity entity, DamageSource source, int level) {
            for(CurioCombatHook module : this.modules) {
                module.onCurioTakeDamagePre(curio, event, entity, source, level);
            }

        }

        public void onCurioTakeDamagePost(IToolStackView curio, LivingDamageEvent event, LivingEntity entity, DamageSource source, int level) {
            for(CurioCombatHook module : this.modules) {
                module.onCurioTakeDamagePost(curio, event, entity, source, level);
            }

        }

        public Collection<CurioCombatHook> modules() {
            return this.modules;
        }
    }
}
