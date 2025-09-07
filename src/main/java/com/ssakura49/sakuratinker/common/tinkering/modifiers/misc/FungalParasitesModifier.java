package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.register.STEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FungalParasitesModifier extends BaseModifier {
    public FungalParasitesModifier() {
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (target != null && !target.level().isClientSide() && damageDealt <= 0) {
            target.addEffect(new MobEffectInstance(
                    STEffects.FungalParasites.get(),
                    100,
                    0,
                    false,
                    true,
                    true
            ));
        }
    }
}
