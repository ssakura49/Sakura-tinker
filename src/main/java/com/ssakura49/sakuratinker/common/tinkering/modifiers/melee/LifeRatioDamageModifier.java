package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class LifeRatioDamageModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float actualDamage) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (target != null) {
            float targetMaxHealth = target.getMaxHealth();
            float attackerMaxHealth = attacker.getMaxHealth();
            if (attackerMaxHealth > 0) {
                float ratio = targetMaxHealth / attackerMaxHealth;
                actualDamage = baseDamage + baseDamage * (float) (ratio * STConfig.Common.LIFE_RATIO_PERCENT.get());
            }
        }

        return actualDamage;
    }
}
