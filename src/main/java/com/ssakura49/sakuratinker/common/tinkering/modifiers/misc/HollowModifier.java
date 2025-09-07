package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.EntityHitResult;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

public class HollowModifier extends BaseModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (target instanceof Player) {
            return;
        }
        if (target instanceof Mob mob) {
            int level = modifier.getLevel();
            float chance = 0.1f + 0.1f * level;
            if (mob.level().random.nextFloat() < chance) {
                mob.setNoAi(true);
            }
        }
    }
    @Override
    public void onProjectileHitTarget(ModifierNBT modifiers, ModDataNBT persistentData, ModifierEntry entry, Projectile projectile, AbstractArrow arrow, EntityHitResult hit, LivingEntity attacker, LivingEntity target) {
        if (target != null) {
            if (target instanceof Player) return;
            if (target instanceof Mob mob) {
                float chance = 0.1f + 0.1f * entry.getLevel();
                if (mob.level().random.nextFloat() < chance) {
                    mob.setNoAi(true);
                }
            }
        }
    }
}
