package com.ssakura49.sakuratinker.library.interfaces.projectile;

import com.ssakura49.sakuratinker.utils.ProjectileUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public interface IProjectileBuild {

    Projectile createProjectile(ItemStack var1, Level var2, LivingEntity var3);

    void addInfoToProjectile(Projectile var1, ItemStack var2, Level var3, LivingEntity var4);

    default boolean dealDamageProjectile(Projectile projectile, IToolStackView stack, ToolAttackContext context, float damage) {
        return ProjectileUtils.dealDefaultDamage(projectile, context.getAttacker(), context.getTarget(), damage);
    }

    default boolean dealArrowDamageProjectile(AbstractArrow arrow, IToolStackView stack, ToolAttackContext context, float damage) {
        return ProjectileUtils.dealArrowDefaultDamage(arrow, context.getAttacker(), context.getTarget(), damage);
    }
}
