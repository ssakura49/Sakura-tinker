package com.ssakura49.sakuratinker.content.tinkering.modifiers.melee;

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
    public float onModifyMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        if (attacker != null && target != null) {
            float targetMaxHealth = target.getMaxHealth();
            float attackerMaxHealth = attacker.getMaxHealth();
            if (attackerMaxHealth > 0) {
                float ratio = targetMaxHealth / attackerMaxHealth;
                actualDamage = baseDamage + baseDamage * (float) (ratio * STConfig.COMMON.LIFE_RATIO_PERCENT.get());
            }
        }

        return actualDamage;
    }
}
