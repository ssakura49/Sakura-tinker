package com.ssakura49.sakuratinker.library.logic.handler;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.library.events.ItemStackDamageEvent;
import com.ssakura49.sakuratinker.library.events.LivingCalculateAbsEvent;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioArrowHook;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioBehaviorHook;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioCombatHook;
import com.ssakura49.sakuratinker.library.logic.context.ImpactData;
import com.ssakura49.sakuratinker.library.tools.STHooks;
import com.ssakura49.sakuratinker.library.tools.STToolStats;
import com.ssakura49.sakuratinker.utils.ToolUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.tools.capability.PersistentDataCapability;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CurioHandler {
    public CurioHandler (){}

    @SubscribeEvent
    public static void onDamageStack(ItemStackDamageEvent event) {
        LivingEntity entity = event.getEntity();
        ToolUtils.Curios.getStacks(entity).forEach((stack) -> {
            ToolStack curio = ToolStack.from(stack);
            curio.getModifierList().forEach((e) -> {
                CurioBehaviorHook hook = (CurioBehaviorHook)e.getHook(STHooks.CURIO_BEHAVIOR);
                hook.onCurioGetToolDamage(curio, entity, event, e.getLevel());
            });
        });
    }

    private static void onHurtEntity(LivingHurtEvent event) {
        Entity var2 = event.getSource().getEntity();
        if (var2 instanceof LivingEntity attacker) {
            ToolUtils.Curios.getStacks(attacker).forEach((stack) -> {
                ToolStack curio = ToolStack.from(stack);
                curio.getModifierList().forEach((e) -> {
                    CurioCombatHook hook = (CurioCombatHook)e.getHook(STHooks.CURIO_COMBAT);
                    hook.onCurioToDamagePre(curio, event, attacker, event.getEntity(), e.getLevel());
                });
            });
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        onHurtEntity(event);
        ToolUtils.Curios.getStacks(entity).forEach((stack) -> {
            ToolStack curio = ToolStack.from(stack);
            curio.getModifierList().forEach((e) -> {
                CurioCombatHook hook = (CurioCombatHook)e.getHook(STHooks.CURIO_COMBAT);
                hook.onCurioTakeDamagePre(curio, event, entity, event.getSource(), e.getLevel());
            });
        });
    }

    private static void onDamageEntity(LivingDamageEvent event) {
        Entity var2 = event.getSource().getEntity();
        if (var2 instanceof LivingEntity attacker) {
            ToolUtils.Curios.getStacks(attacker).forEach((stack) -> {
                ToolStack curio = ToolStack.from(stack);
                curio.getModifierList().forEach((e) -> {
                    CurioCombatHook hook = (CurioCombatHook)e.getHook(STHooks.CURIO_COMBAT);
                    hook.onCurioToDamagePost(curio, event, attacker, event.getEntity(), e.getLevel());
                });
            });
        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        onDamageEntity(event);
        ToolUtils.Curios.getStacks(entity).forEach((stack) -> {
            ToolStack curio = ToolStack.from(stack);
            curio.getModifierList().forEach((e) -> {
                CurioCombatHook hook = (CurioCombatHook)e.getHook(STHooks.CURIO_COMBAT);
                hook.onCurioTakeDamagePost(curio, event, entity, event.getSource(), e.getLevel());
            });
        });
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        Entity var2 = event.getSource().getEntity();
        if (var2 instanceof LivingEntity attacker) {
            ToolUtils.Curios.getStacks(attacker).forEach((stack) -> {
                ToolStack curio = ToolStack.from(stack);
                curio.getModifierList().forEach((e) -> {
                    CurioCombatHook hook = (CurioCombatHook)e.getHook(STHooks.CURIO_COMBAT);
                    hook.onCurioToKillTarget(curio, event, attacker, event.getEntity(), e.getLevel());
                });
            });
        }
    }

    @SubscribeEvent
    public static void onLivingCalculate(LivingCalculateAbsEvent event) {
        LivingEntity attacker = event.getLivingAttacker();
        if (attacker != null) {
            ToolUtils.Curios.getStacks(attacker).forEach((stack) -> {
                ToolStack curio = ToolStack.from(stack);
                curio.getModifierList().forEach((e) -> {
                    CurioCombatHook hook = (CurioCombatHook)e.getHook(STHooks.CURIO_COMBAT);
                    hook.onCurioCalculateDamage(curio, event, attacker, event.getEntity(), e.getLevel());
                });
            });
        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        ToolUtils.Curios.getStacks(entity).forEach((stack) -> {
            ToolStack curio = ToolStack.from(stack);
            curio.getModifierList().forEach((e) -> {
                CurioBehaviorHook hook = (CurioBehaviorHook)e.getHook(STHooks.CURIO_BEHAVIOR);
                hook.onCurioTakeHeal(curio, event, entity, e.getLevel());
            });
        });
    }

    public static void arrowStatAdd(ToolStack curio, LivingEntity entity, AbstractArrow arrow) {
        float arrow_damage = ConditionalStatModifierHook.getModifiedStat(curio, entity, STToolStats.ARROW_DAMAGE);
        arrow.setBaseDamage(arrow.getBaseDamage() * (double)(1.0F + arrow_damage));
    }

    @SubscribeEvent
    public static void onShootArrow(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof AbstractArrow arrow) {
            Entity var3 = arrow.getOwner();
            if (var3 instanceof LivingEntity livingEntity) {
                ToolUtils.Curios.getStacks(livingEntity).forEach((stack) -> {
                    ToolStack curio = ToolStack.from(stack);
                    curio.getModifierList().forEach((e) -> {
                        arrowStatAdd(curio, livingEntity, arrow);
                        CurioArrowHook hook = (CurioArrowHook)e.getHook(STHooks.CURIO_ARROW);
                        hook.onCurioShootArrow(curio, livingEntity, arrow, PersistentDataCapability.getOrWarn(arrow), e.getLevel());
                    });
                });
            }
        }
    }

    @SubscribeEvent
    public static void onArrowHit(ProjectileImpactEvent event) {
        Projectile shooter = event.getProjectile();
        if (shooter instanceof AbstractArrow arrow) {
            Entity var3 = arrow.getOwner();
            if (var3 instanceof LivingEntity shooter1) {
                ToolUtils.Curios.getStacks(shooter1).forEach((stack) -> {
                    ToolStack curio = ToolStack.from(stack);
                    curio.getModifierList().forEach((e) -> {
                        CurioArrowHook hook = (CurioArrowHook)e.getHook(STHooks.CURIO_ARROW);
                        hook.onCurioArrowHit(curio, shooter1, new ImpactData(event, arrow), e.getLevel());
                    });
                });
            }
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        ToolUtils.Curios.getStacks(event.getEntity()).forEach((stack) -> {
            ToolStack curio = ToolStack.from(stack);
            curio.getModifierList().forEach((e) -> {
                CurioBehaviorHook hook = (CurioBehaviorHook)e.getHook(STHooks.CURIO_BEHAVIOR);
                hook.onCurioBreakSpeed(curio, event, event.getEntity(), e.getLevel());
            });
        });
    }
}
