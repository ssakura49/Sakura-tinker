package com.ssakura49.sakuratinker.library.logic.handler;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.library.events.AttackSpeedModifyEvent;
import com.ssakura49.sakuratinker.library.events.LivingCalculateAbsEvent;
import com.ssakura49.sakuratinker.library.events.TinkerToolCriticalEvent;
import com.ssakura49.sakuratinker.library.hooks.combat.*;
import com.ssakura49.sakuratinker.library.tinkering.tools.STHooks;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CombatHandler {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        ToolStack tool = ToolUtil.getToolInHand(attacker);
        if (ToolUtil.isNotBrokenOrNull(tool)) {
            float baseDamage = event.getAmount();
            float currentDamage = baseDamage;
            for (ModifierEntry entry : tool.getModifierList()) {
                CauseDamageModifierHook hook = entry.getHook(STHooks.CAUSE_DAMAGE);
                currentDamage = hook.onCauseDamage(
                        tool,
                        entry,
                        event,
                        attacker,
                        event.getEntity(),
                        baseDamage,
                        currentDamage
                );
            }
            event.setAmount(Math.max(0, currentDamage));
        };

    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        ToolStack tool = ToolUtil.getToolInHand(entity);
        if (ToolUtil.isNotBrokenOrNull(tool)) {
            tool.getModifierList().forEach((e) -> {
                HolderDamageTakeHook hook = (HolderDamageTakeHook)e.getHook(STHooks.HOLDER_DAMAGE_TAKE);
                hook.onHolderTakeDamage(tool, event, entity, event.getSource(), e.getLevel());
            });
        }
    }

    @SubscribeEvent
    public static void onLivingCalculate(LivingCalculateAbsEvent event) {
        LivingEntity attacker = event.getLivingAttacker();
        if (attacker != null) {
            ToolStack tool = ToolUtil.getToolInHand(attacker);
            if (ToolUtil.isNotBrokenOrNull(tool)) {
                tool.getModifierList().forEach((e) -> {
                    GenericCombatHook hook = (GenericCombatHook)e.getHook(STHooks.GENERIC_COMBAT);
                    hook.onCalculateDamage(tool, event, attacker, event.getEntity(), e.getLevel());
                });
            }
        }
    }

    @SubscribeEvent
    public static void onLivingKill(LivingDeathEvent event) {
        Entity entity = event.getSource().getEntity();
        if (entity instanceof LivingEntity attacker) {
            ToolStack tool = ToolUtil.getToolInHand(attacker);
            if (!ToolUtil.isNotBrokenOrNull(tool)) {
                return;
            }

            tool.getModifierList().forEach((e) -> {
                GenericCombatHook hook = (GenericCombatHook)e.getHook(STHooks.GENERIC_COMBAT);
                hook.onKillLivingTarget(tool, event, attacker, event.getEntity(), e.getLevel());
            });
        }

    }

    @SubscribeEvent
    public static void onCriticalHit(TinkerToolCriticalEvent event) {
        LivingEntity attacker = event.getContext().getAttacker();
        LivingEntity target = event.getContext().getLivingTarget();
        if (target != null) {
            event.getTool().getModifierList().forEach((e) -> {
                GenericCombatHook hook = (GenericCombatHook)e.getHook(STHooks.GENERIC_COMBAT);
                hook.onMeleeCriticalHit(event.getTool(), event, attacker, target, e.getLevel());
            });
        }
    }

    @SubscribeEvent
    public static void onShieldBlock(ShieldBlockEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            if (player.getUseItem().isEmpty()) {
                return;
            }

            ToolStack tool = ToolStack.from(player.getUseItem());
            tool.getModifierList().forEach((e) -> {
                ShieldBlockingHook hook = (ShieldBlockingHook)e.getHook(STHooks.SHIELD_BLOCKING);
                hook.onShieldBlocked(tool, event, player, event.getDamageSource(), e.getLevel());
            });
        }

    }

    @SubscribeEvent
    public static void onModifyAttackCooldown(AttackSpeedModifyEvent event) {
        Player player = event.getPlayer();
        ToolStack tool = ToolUtil.getToolInHand(player);
        if (ToolUtil.isNotBrokenOrNull(tool)) {
            tool.getModifierList().forEach((e) -> {
                MeleeCooldownHook hook = (MeleeCooldownHook)e.getHook(STHooks.MELEE_COOLDOWN);
                hook.modifyAttackCooldown(tool, player, event, e.getLevel());
            });
        }
    }
}
