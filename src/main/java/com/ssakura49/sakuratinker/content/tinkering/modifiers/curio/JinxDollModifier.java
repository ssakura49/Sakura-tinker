package com.ssakura49.sakuratinker.content.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import top.theillusivec4.curios.api.SlotContext;

import java.util.*;

public class JinxDollModifier extends CurioModifier {
    private static final int DEBUFF_REMOVAL_DELAY = 100;
    private static final Map<MobEffect, Integer> debuffTimers = new HashMap<MobEffect, Integer>();

    private static final List<MobEffect> POSITIVE_EFFECTS = Arrays.asList(
            MobEffects.MOVEMENT_SPEED,
            MobEffects.DIG_SPEED,
            MobEffects.DAMAGE_BOOST,
            MobEffects.HEAL,
            MobEffects.JUMP,
            MobEffects.REGENERATION,
            MobEffects.DAMAGE_RESISTANCE,
            MobEffects.FIRE_RESISTANCE,
            MobEffects.WATER_BREATHING,
            MobEffects.INVISIBILITY
    );
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public void onCurioTick(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack stack) {
        if (!(entity instanceof Player player)) {
            return;
        }
        List<MobEffectInstance> effectsToProcess = new ArrayList<>(player.getActiveEffects());
        for (MobEffectInstance effect : effectsToProcess) {
            if (effect != null) {
                MobEffect mobEffect = effect.getEffect();
                if (!mobEffect.isBeneficial()) {
                    if (!debuffTimers.containsKey(mobEffect)) {
                        debuffTimers.put(mobEffect, player.tickCount);
                    }
                    if (player.tickCount - debuffTimers.get(mobEffect) >= DEBUFF_REMOVAL_DELAY) {
                        player.removeEffect(mobEffect);
                        MobEffect randomPositiveEffect = getRandomPositiveEffect();
                        if (randomPositiveEffect != null) {
                            player.addEffect(new MobEffectInstance(
                                    randomPositiveEffect,
                                    200,
                                    0
                            ));
                        }
                        debuffTimers.remove(mobEffect);
                    }
                }
            }
        }
    }

    private MobEffect getRandomPositiveEffect() {
        if (POSITIVE_EFFECTS.isEmpty()) {
            return null;
        }
        Random random = new Random();
        return POSITIVE_EFFECTS.get(random.nextInt(POSITIVE_EFFECTS.size()));
    }
}
