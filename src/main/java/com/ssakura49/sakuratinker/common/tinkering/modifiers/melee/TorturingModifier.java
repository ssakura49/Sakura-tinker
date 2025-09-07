package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.register.STEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TorturingModifier extends BaseModifier {
    private static final float DAMAGE_PER_LEVEL = 10.0f;

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        return super.getMeleeDamage(tool, modifier, context, baseDamage, damage);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attacker = context.getAttacker();
        if (target != null && attacker != null && damageDealt > 0) {
            int effectLevel = (int)Math.floor(damageDealt / DAMAGE_PER_LEVEL);
            if (effectLevel > 0) {
                MobEffectInstance currentEffect = target.getEffect(STEffects.TORTURE.get());
                int newAmplifier = currentEffect != null ?
                        Math.min(currentEffect.getAmplifier() + effectLevel, 49) :
                        effectLevel - 1;
                target.addEffect(new MobEffectInstance(
                        STEffects.TORTURE.get(),
                        100 + (newAmplifier * 40),
                        newAmplifier,
                        false,
                        true,
                        true
                ), attacker);
            }
        }
    }
}
