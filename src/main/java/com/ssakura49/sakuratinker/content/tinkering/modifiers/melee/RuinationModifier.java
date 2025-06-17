package com.ssakura49.sakuratinker.content.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class RuinationModifier extends BaseModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        if (target != null && !target.level().isClientSide) {
            float damageFactor = STConfig.COMMON.RUINATION_DAMAGE_FACTOR.get().floatValue();
            float extraDamage = target.getHealth() * (damageFactor * modifier.getLevel());
            target.hurt(target.damageSources().generic(), extraDamage);
        }
    }
}
