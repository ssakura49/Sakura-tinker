package com.ssakura49.sakuratinker.compat.ExtraBotany.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class LightEnduranceModifier extends BaseModifier {
    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        int level = modifier.getLevel();
        float chance = 0.2f + 0.1f * level;
        if (attacker.level().random.nextFloat() < chance) {
            MobEffectInstance effect = new MobEffectInstance(MobEffects.ABSORPTION, 100, 0, true, true, true);
            attacker.addEffect(effect);
        }
    }
}
