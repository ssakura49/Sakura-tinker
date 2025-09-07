package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class FieryFireModifier extends BaseModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (target != null) {
            target.setSecondsOnFire(40 * entry.getLevel());
        }
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float actualDamage) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (target != null) {
            return target.isOnFire() ? baseDamage + 4 * modifier.getLevel() : baseDamage;
        }
        return baseDamage;
    }

    @Override
    public void onProjectileHitTarget(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry entry, Projectile projectile, AbstractArrow arrow, EntityHitResult hit, LivingEntity attacker, LivingEntity target) {
        if (attacker != null && target != null) {
            int level = entry.getLevel();
            target.setSecondsOnFire(40 * level);
            if (target.isOnFire()) {
                arrow.setBaseDamage(arrow.getBaseDamage() + (1.5 * (float)level));
            }
        }
    }
}
