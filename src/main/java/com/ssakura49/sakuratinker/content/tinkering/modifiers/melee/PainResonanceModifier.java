package com.ssakura49.sakuratinker.content.tinkering.modifiers.melee;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.register.STEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.*;

public class PainResonanceModifier extends BaseModifier {
    private static final int DEBUFF_DURATION = 1200;
    private static final float DAMAGE_MULTIPLIER_PER_DEBUFF = 0.3f;
    private static final int MAX_DEBUFF_LEVEL = 5;
    private static final Random RANDOM = new Random();

    @Override
    public boolean isNoLevels() {
        return true;
    }

    private static final MobEffect[] NEGATIVE_EFFECTS = {
            MobEffects.WEAKNESS,
            MobEffects.MOVEMENT_SLOWDOWN,
            MobEffects.DIG_SLOWDOWN,
            MobEffects.POISON,
            MobEffects.BLINDNESS,
            MobEffects.GLOWING,
            MobEffects.LEVITATION,
            MobEffects.HUNGER,
            MobEffects.DARKNESS,
            STEffects.TORTURE.get()
    };

    @Override
    public float getMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float baseDamage, float damage) {
        LivingEntity target = context.getLivingTarget();
        if (target != null && context.getAttacker() instanceof Player) {
            applySmartDebuff(target);
            int weightedDebuffCount = getWeightedDebuffCount(target);
            return damage * (float)Math.pow(1 + DAMAGE_MULTIPLIER_PER_DEBUFF, weightedDebuffCount);
        }
        return damage;
    }

    private void applySmartDebuff(LivingEntity target) {
        Map<MobEffect, Integer> currentDebuffs = new HashMap<>();
        for (MobEffectInstance effect : target.getActiveEffects()) {
            if (isTrueNegativeEffect(effect.getEffect())) {
                currentDebuffs.put(effect.getEffect(), effect.getAmplifier() + 1);
            }
        }

        List<MobEffect> upgradable = new ArrayList<>();
        for (Map.Entry<MobEffect, Integer> entry : currentDebuffs.entrySet()) {
            if (entry.getValue() < MAX_DEBUFF_LEVEL) {
                upgradable.add(entry.getKey());
            }
        }

        if (!upgradable.isEmpty()) {
            MobEffect toUpgrade = upgradable.get(RANDOM.nextInt(upgradable.size()));
            upgradeDebuff(target, toUpgrade, currentDebuffs.get(toUpgrade));
            return;
        }

        applyNewDebuff(target, currentDebuffs.keySet());
    }

    private void upgradeDebuff(LivingEntity target, MobEffect effect, int currentLevel) {
        target.removeEffect(effect);
        target.addEffect(new MobEffectInstance(
                effect,
                DEBUFF_DURATION,
                Math.min(currentLevel, MAX_DEBUFF_LEVEL - 1),
                false, false, true
        ));
    }

    private void applyNewDebuff(LivingEntity target, Set<MobEffect> existing) {
        List<MobEffect> available = new ArrayList<>();
        for (MobEffect effect : NEGATIVE_EFFECTS) {
            if (!existing.contains(effect)) {
                available.add(effect);
            }
        }

        if (!available.isEmpty()) {
            MobEffect newEffect = available.get(RANDOM.nextInt(available.size()));
            target.addEffect(new MobEffectInstance(newEffect, DEBUFF_DURATION, 0));
        } else {
            MobEffect maxedEffect = NEGATIVE_EFFECTS[RANDOM.nextInt(NEGATIVE_EFFECTS.length)];
            upgradeDebuff(target, maxedEffect, MAX_DEBUFF_LEVEL);
        }
    }

    private int getWeightedDebuffCount(LivingEntity target) {
        int totalWeight = 0;
        for (MobEffectInstance effect : target.getActiveEffects()) {
            if (isTrueNegativeEffect(effect.getEffect())) {
                totalWeight += (effect.getAmplifier() + 1);
            }
        }
        return totalWeight;
    }

    private boolean isTrueNegativeEffect(MobEffect effect) {
        return effect.getCategory() == MobEffectCategory.HARMFUL ||
                Arrays.asList(NEGATIVE_EFFECTS).contains(effect);
    }
}
