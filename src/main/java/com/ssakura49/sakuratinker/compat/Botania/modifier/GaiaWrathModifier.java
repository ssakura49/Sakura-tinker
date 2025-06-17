package com.ssakura49.sakuratinker.compat.Botania.modifier;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.damagesource.LegacyDamageSource;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class GaiaWrathModifier extends BaseModifier {
    @Override
    public float onModifyMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context,
                                     LivingEntity attacker, LivingEntity target,
                                     float baseDamage, float actualDamage) {
        if (!attacker.level().isClientSide && target != null && modifier.getLevel() > 0) {
            float percent = 0.03f * modifier.getLevel();
            float bonusDamage = target.getMaxHealth() * percent;
            LegacyDamageSource source = LegacyDamageSource.indirectMagic(attacker).setBypassArmor().setMagic();
            target.hurt(source, bonusDamage);
        }
        return actualDamage;
    }
}
