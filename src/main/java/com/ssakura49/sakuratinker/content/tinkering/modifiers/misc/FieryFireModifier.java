package com.ssakura49.sakuratinker.content.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class FieryFireModifier extends BaseModifier {
    @Override
    public void onAfterMeleeHit(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float damageDealt) {
        if (target != null) {
            target.setSecondsOnFire(40 * level);
        }
    }

    @Override
    public float onModifyMeleeDamage(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        if (target != null) {
            return target.isOnFire() ? baseDamage + 4 * level : baseDamage;
        }
        return baseDamage;
    }

    @Override
    public void onProjectileHitTarget(ModifierNBT modifiers, ModDataNBT persistentData, int level, Projectile projectile, AbstractArrow arrow, EntityHitResult hit, LivingEntity attacker, LivingEntity target) {
        if (attacker != null && target != null) {
            target.setSecondsOnFire(40 * level);
            if (target.isOnFire()) {
                arrow.setBaseDamage(arrow.getBaseDamage() + (1.5 * (float)level));
            }
        }
    }
}
