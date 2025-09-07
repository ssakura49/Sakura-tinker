package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public class TwilightRemembranceModifier extends BaseModifier {
    public TwilightRemembranceModifier() {

    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (target != null) {
            float splashDamage = damageDealt * 0.10f * modifier.getLevel();
            Level world = attacker.level();
            AABB area = new AABB(target.getX() - 3, target.getY() - 1, target.getZ() - 3,
                    target.getX() + 3, target.getY() + 2, target.getZ() + 3);
            List<LivingEntity> nearbyEntities = world.getEntitiesOfClass(LivingEntity.class, area);
            DamageSource damageSource = world.damageSources().mobAttack(attacker);
            for (LivingEntity entity : nearbyEntities) {
                if (entity != target && entity != attacker && !(entity instanceof Player player)) {
                    entity.hurt(damageSource, splashDamage);
                }
            }
        }
    }
}
