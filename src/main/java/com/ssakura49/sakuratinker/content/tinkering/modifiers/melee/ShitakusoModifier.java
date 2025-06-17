package com.ssakura49.sakuratinker.content.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ShitakusoModifier extends BaseModifier {
    @Override
    public float onModifyMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                                     LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        float attackerMaxHp = attacker.getMaxHealth();
        float targetMaxHp = target.getMaxHealth();

        if (targetMaxHp <= 0f) {
            return actualDamage;
        }

        double k = STConfig.COMMON.ShitakusoBonus.get();
        float ratio = attackerMaxHp / targetMaxHp;
        float multiplier = (float)(ratio * k);

        float bonus = actualDamage * multiplier;

        return actualDamage + bonus;
    }
}
