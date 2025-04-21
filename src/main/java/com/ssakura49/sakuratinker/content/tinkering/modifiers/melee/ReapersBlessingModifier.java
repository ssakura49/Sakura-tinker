package com.ssakura49.sakuratinker.content.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class ReapersBlessingModifier extends BaseModifier {
    private static final double max_bonus = STConfig.COMMON.Reapers_Blessing_MAX_BONUS.get();
    private static final double bonus_per_health = STConfig.COMMON.Reapers_Blessing_BONUS_PER_HEALTH.get();

    @Override
    public boolean isNoLevels(){
        return true;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity attacker = context.getAttacker();
        float maxHealth = attacker.getMaxHealth();
        float missingHealth = maxHealth - attacker.getHealth();
        float healthRatio = Math.min(missingHealth / maxHealth, 1.0f);
        float damageBonus = (float) Math.min(healthRatio * bonus_per_health * maxHealth, max_bonus);

        return damage * (1.0f + damageBonus);
    }
}
