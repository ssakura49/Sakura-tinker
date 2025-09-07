package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ShitakusoModifier extends BaseModifier {
    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                                     float baseDamage, float actualDamage) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        float attackerMaxHp = attacker.getMaxHealth();
        float targetMaxHp = target.getMaxHealth();

        if (targetMaxHp <= 0f) {
            return actualDamage;
        }

        double k = STConfig.Common.SHITAKUSO_BONUS.get();
        float ratio = attackerMaxHp / targetMaxHp;
        float multiplier = (float)(ratio * k);

        float bonus = actualDamage * multiplier;

        return actualDamage + bonus;
    }
}
