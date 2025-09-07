package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class RejuvenatingModifier extends BaseModifier {
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float actualDamage) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (target != null && !target.level().isClientSide() && baseDamage > 0) {
            return actualDamage/ (2.0f + modifier.getLevel());
        }
        return actualDamage;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (target != null && !target.level().isClientSide() && damageDealt > 0) {
           if (context.isFullyCharged()) {
                target.heal(damageDealt / 2.0f);
           }
        }
    }
}
