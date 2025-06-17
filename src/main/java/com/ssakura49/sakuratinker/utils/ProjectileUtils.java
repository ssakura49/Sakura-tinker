package com.ssakura49.sakuratinker.utils;

import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileBuild;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileKnockBack;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeDamageModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Random;

public class ProjectileUtils {
    public ProjectileUtils(){}

    public static void homingTowardLookDirection(Entity projectile, Entity shooter, float strength) {
        double velocity = projectile.getDeltaMovement().length();
        Vec3 movementDirection = projectile.getDeltaMovement().normalize().scale(1.0 / (1.0 + strength));
        Vec3 lookDirection = shooter.getLookAngle().normalize();
        Vec3 accelerate = lookDirection.scale(1.0 + strength);
        Vec3 newMovement = movementDirection.add(accelerate).normalize().scale(velocity);
        projectile.setDeltaMovement(newMovement);
    }

    public static boolean dealDefaultDamage(Projectile projectile, LivingEntity attacker, Entity target, float damage) {
        return target.hurt(LegacyDamageSource.projectile(projectile, attacker), damage);
    }

    public static boolean attackEntity(Item weapon, Projectile projectile, IToolStackView tool, LivingEntity attackerLiving, Entity targetEntity, boolean isExtraAttack) {
        boolean wasCritical = false;
        if (projectile instanceof IProjectileCritical) {
            wasCritical = ((IProjectileCritical)projectile).getCritical();
        }

        LivingEntity targetLiving = targetEntity instanceof LivingEntity ? (LivingEntity)targetEntity : null;
        if (attackerLiving == null) {
            return false;
        } else {
            ToolAttackContext context = new ToolAttackContext(attackerLiving, attackerLiving instanceof Player ? (Player)attackerLiving : null, InteractionHand.MAIN_HAND, targetEntity, targetLiving, wasCritical, 1.0F, isExtraAttack);
            float damage;
            if (projectile instanceof IProjectileDamage) {
                damage = ((IProjectileDamage)projectile).getDamage();
            } else {
                damage = (float)tool.getDamage();
            }

            float baseDamage = damage;

            for(ModifierEntry entry : tool.getModifierList()) {
                damage = ((MeleeDamageModifierHook)entry.getHook(ModifierHooks.MELEE_DAMAGE)).getMeleeDamage(tool, entry, context, baseDamage, damage);
            }

            float knockback;
            if (projectile instanceof IProjectileKnockBack) {
                knockback = ((IProjectileKnockBack)projectile).getKnockback();
            } else {
                knockback = 0.4F;
            }

            float baseKnockback = knockback;

            for(ModifierEntry entry : tool.getModifierList()) {
                knockback = ((MeleeHitModifierHook)entry.getHook(ModifierHooks.MELEE_HIT)).beforeMeleeHit(tool, entry, context, damage, baseKnockback, knockback);
            }

            float oldHealth = 0.0F;
            if (targetLiving != null && targetEntity != null) {
                oldHealth = targetLiving.getHealth();
            }

            boolean didHit;
            if (!isExtraAttack && weapon instanceof IProjectileBuild) {
                didHit = ((IProjectileBuild)weapon).dealDamageProjectile(projectile, tool, context, damage);
            } else {
                didHit = dealDefaultDamage(projectile, attackerLiving, targetEntity, damage);
            }

            if (didHit) {
                float damageDealt = damage;
                if (targetEntity != null && targetLiving != null) {
                    damageDealt = oldHealth - targetLiving.getHealth();
                }

                if (knockback > 0.0F) {
                    Vec3 vec3 = projectile.getDeltaMovement().normalize().scale(knockback * 0.6);
                    if (vec3.lengthSqr() > 0.0) {
                        targetEntity.push(vec3.x, vec3.y, vec3.z); // 保留Y轴分量
                    }
                }

                for(ModifierEntry entry : tool.getModifierList()) {
                    ((MeleeHitModifierHook)entry.getHook(ModifierHooks.MELEE_HIT)).afterMeleeHit(tool, entry, context, damageDealt);
                }

                return true;
            } else {
                for(ModifierEntry entry : tool.getModifierList()) {
                    ((MeleeHitModifierHook)entry.getHook(ModifierHooks.MELEE_HIT)).failedMeleeHit(tool, entry, context, damage);
                }

                return !isExtraAttack;
            }
        }
    }

    public static boolean dealArrowDefaultDamage(AbstractArrow arrow, LivingEntity attacker, Entity target, float damage) {
        return target.hurt(LegacyDamageSource.projectile(arrow, attacker), damage);
    }

    public static boolean arrowAttackEntity(Item weapon, AbstractArrow arrow, IToolStackView tool, LivingEntity attackerLiving, Entity targetEntity, float velocity, float arrowKnockback, boolean isExtraAttack) {
        boolean isCritical = false;
        if (arrow instanceof IProjectileCritical criticalInterface) {
            isCritical = criticalInterface.getCritical();
        }

        Random random = new Random();
        LivingEntity target = (targetEntity instanceof LivingEntity living) ? living : null;
        if (attackerLiving == null) {
            return false;
        }

        ToolAttackContext context = new ToolAttackContext(
                attackerLiving,
                attackerLiving instanceof Player player ? player : null,
                InteractionHand.MAIN_HAND,
                targetEntity,
                target,
                isCritical,
                1.0F,
                isExtraAttack
        );

        float damage;
        if (arrow instanceof IProjectileDamage damageInterface) {
            damage = damageInterface.getDamage();
        } else {
            damage = (float) tool.getDamage();
        }
        float baseDamage = damage;

        for (ModifierEntry entry : tool.getModifierList()) {
            damage = ((MeleeDamageModifierHook) entry.getHook(ModifierHooks.MELEE_DAMAGE))
                    .getMeleeDamage(tool, entry, context, baseDamage, damage);
        }

        float arrowBase = (arrow instanceof IProjectileDamage pDamage) ? pDamage.getDamage() : 0F;
        int finalInt = Mth.floor(velocity * (0.8F * damage + arrowBase));
        finalInt = Mth.clamp(finalInt, 0, Integer.MAX_VALUE);
        damage = (float) finalInt;

        if (arrow instanceof IProjectileCritical critical && critical.getCritical()) {
            long extra = random.nextInt((int)(damage / 2.0F + 2.0F));
            damage = Math.min(extra + damage, Integer.MAX_VALUE);
        }

        float knockback;
        if (arrow instanceof IProjectileKnockBack) {
            knockback = ((IProjectileKnockBack)arrow).getKnockback();
        } else {
            knockback = 0.4F;
        }
        float baseKnockback = knockback;
        for(ModifierEntry entry : tool.getModifierList()) {
            knockback = ((MeleeHitModifierHook)entry.getHook(ModifierHooks.MELEE_HIT)).beforeMeleeHit(tool, entry, context, damage, baseKnockback, knockback);
        }
        knockback += arrowKnockback;

        float oldHealth = (target != null) ? target.getHealth() : 0F;
        boolean didHit;
        if (!isExtraAttack && weapon instanceof IProjectileBuild buildInterface) {
            didHit = buildInterface.dealArrowDamageProjectile(arrow, tool, context, damage);
        } else {
            didHit = dealArrowDefaultDamage(arrow, attackerLiving, targetEntity, damage);
        }

        if (didHit) {
            float damageDealt = damage;
            if (target != null) {
                damageDealt = oldHealth - target.getHealth();
            }
            if (knockback > 0.0F && target != null) {
                Vec3 motion = arrow.getDeltaMovement().normalize().scale(knockback * 0.6);
                if (motion.lengthSqr() > 0.0) {
                    target.push(motion.x, 0.1, motion.z);
                }
            }
            for (ModifierEntry entry : tool.getModifierList()) {
                ((MeleeHitModifierHook) entry.getHook(ModifierHooks.MELEE_HIT)).afterMeleeHit(tool, entry, context, damageDealt);
            }
            return true;
        }
        else {
            for (ModifierEntry entry : tool.getModifierList()) {
                ((MeleeHitModifierHook) entry.getHook(ModifierHooks.MELEE_HIT)).failedMeleeHit(tool, entry, context, damage);
            }
            return !isExtraAttack;
        }
    }
}
