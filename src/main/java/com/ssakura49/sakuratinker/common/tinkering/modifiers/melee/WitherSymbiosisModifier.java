package com.ssakura49.sakuratinker.common.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Random;

public class WitherSymbiosisModifier extends BaseModifier {
    private static final Random RANDOM = new Random();
    private static final float SELF_WITHER_CHANCE = 0.1f;

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        LivingEntity target = context.getLivingTarget();
        LivingEntity attack = context.getAttacker();
        if (target != null) {
            target.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, modifier.getLevel() - 1));
            if (target.hasEffect(MobEffects.WITHER)) {
                MobEffectInstance witherEffect = target.getEffect(MobEffects.WITHER);
                int witherLevel = 0;
                if (witherEffect != null) {
                    witherLevel = witherEffect.getAmplifier() + 1;
                }
                int materialLevel = tool.getModifierLevel(this);
                float extraDamage = witherLevel * materialLevel * 5;
                target.hurt(target.damageSources().magic(), extraDamage);
                if (witherLevel > 1) {
                    target.addEffect(new MobEffectInstance(MobEffects.WITHER, witherEffect.getDuration(), witherLevel - 2));
                } else {
                    target.removeEffect(MobEffects.WITHER);
                }
                if (materialLevel >= 2) {
                    if (RANDOM.nextFloat() < SELF_WITHER_CHANCE) {
                        attack.addEffect(new MobEffectInstance(MobEffects.WITHER, 20, 0));
                    }
                }
            }
        }
    }

}
