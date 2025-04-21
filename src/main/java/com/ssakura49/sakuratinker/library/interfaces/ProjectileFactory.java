package com.ssakura49.sakuratinker.library.interfaces;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileHitModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.ranged.ProjectileLaunchModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public interface ProjectileFactory extends ProjectileHitModifierHook, ProjectileLaunchModifierHook {
    default void initProjectileHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this,
                ModifierHooks.PROJECTILE_LAUNCH,
                ModifierHooks.PROJECTILE_HIT);
    }
    /**
     *箭矢击中目标，使用onProjectileHitTarget
     */
    default boolean onProjectileHitEntity(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, EntityHitResult hit, @javax.annotation.Nullable LivingEntity attacker, @javax.annotation.Nullable LivingEntity target) {
        if (target != null && attacker != null && !attacker.level().isClientSide() && projectile instanceof AbstractArrow arrow) {
            this.onProjectileHitTarget(modifiers, persistentData, modifier.getLevel(), projectile, arrow, hit, attacker, target);
        }
        return false;
    }
    default void onProjectileHitTarget(ModifierNBT modifiers, ModDataNBT persistentData, int level, Projectile projectile, AbstractArrow arrow, EntityHitResult hit, LivingEntity attacker, LivingEntity target) {
    }
    /**
     *箭矢击中方块，使用onProjectileHitBlock
     */
    default void onProjectileHitBlock(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry modifier, Projectile projectile, BlockHitResult hit, @Nullable LivingEntity attacker) {
        if (attacker != null && !attacker.level().isClientSide() && projectile instanceof AbstractArrow arrow) {
            this.onProjectileHitBlock(modifiers, persistentData, modifier.getLevel(), projectile, arrow, hit, attacker);
        }
    }
    default void onProjectileHitBlock(ModifierNBT modifiers, ModDataNBT persistentData, int level, Projectile projectile, AbstractArrow arrow, BlockHitResult hit, LivingEntity attacker) {
    }

    /**
     *箭矢发射触发，使用onProjectileShoot
     */
    default void onProjectileLaunch(IToolStackView tool, ModifierEntry modifier, LivingEntity shooter, Projectile projectile, @Nullable AbstractArrow arrow, ModDataNBT modDataNBT, boolean primary) {
        if (arrow != null) {
            this.onProjectileShoot(tool, modifier.getLevel(), shooter, projectile, arrow, modDataNBT, primary);
        }
    }
    default void onProjectileShoot(IToolStackView bow, int level, LivingEntity shooter, Projectile projectile, AbstractArrow arrow, ModDataNBT modDataNBT, boolean primary) {
    }
}
