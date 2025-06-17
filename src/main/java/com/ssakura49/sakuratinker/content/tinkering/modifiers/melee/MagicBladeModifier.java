package com.ssakura49.sakuratinker.content.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class MagicBladeModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void onFailedMeleeHit(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float damageAttempted) {
        if (target != null && !target.isInvulnerable() && damageAttempted > 0) {
            DamageSource source = attacker.damageSources().magic();
            target.hurt(source, damageAttempted);
        }
    }
}
