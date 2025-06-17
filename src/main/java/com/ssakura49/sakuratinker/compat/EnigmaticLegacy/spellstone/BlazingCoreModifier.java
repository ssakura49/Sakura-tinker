package com.ssakura49.sakuratinker.compat.EnigmaticLegacy.spellstone;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.ArrayList;
import java.util.List;

public class BlazingCoreModifier extends BaseModifier {

    public BlazingCoreModifier() {
    }

    private static final int MAX_OVERHEAT = 200;
    private static final int OVERHEAT_COOLDOWN_RATE = 2;
    private static final String OVERHEAT_TAG = "blazing_core_overheat";

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float onModifyTakeDamage(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (source.is(DamageTypes.IN_FIRE)) {
            return 0;
        }
        LivingEntity entity = context.getEntity();
        if (source.is(DamageTypes.LAVA)) {
            if (entity instanceof Player player) {
                int overheat = player.getPersistentData().getInt(OVERHEAT_TAG);
                if (overheat < MAX_OVERHEAT) {
                    player.getPersistentData().putInt(OVERHEAT_TAG, overheat + 10);
                    return 0;
                }
            }
            return amount;
        }
        return amount;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (holder != null) {
            if (holder.isOnFire()) {
                holder.clearFire();
            }
            if (holder instanceof Player player && !holder.isInLava()) {
                int overheat = player.getPersistentData().getInt(OVERHEAT_TAG);
                if (overheat > 0) {
                    int newOverheat = Math.max(0, overheat - OVERHEAT_COOLDOWN_RATE);
                    player.getPersistentData().putInt(OVERHEAT_TAG, newOverheat);
                }
            }
        }
    }

    @Override
    public void modifierOnAttacked(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (isDirectDamage && source.getDirectEntity() instanceof LivingEntity attacker) {
            attacker.setSecondsOnFire(20);
        }
    }

    @Override
    public void onAfterMeleeHit(IToolStackView tool, int level, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float damageDealt) {
        if (attacker != null && target != null) {
            if (attacker instanceof Player player && target != player) {
                target.setSecondsOnFire(3);
            }
        }
    }
    @Override
    public void onModifierDamageDealt(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, LivingEntity entity, DamageSource damageSource, float amount, boolean isDirectDamage) {
        if (entity != null) {
            if (entity instanceof Player player) {
                List<MobEffectInstance> effectsToProcess = new ArrayList<>(player.getActiveEffects());
                for (MobEffectInstance effect : effectsToProcess) {
                    MobEffectInstance newEffect = new MobEffectInstance(
                            effect.getEffect(),
                            effect.getDuration() / 2,
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.isVisible(),
                            effect.showIcon()
                    );
                    player.removeEffect(effect.getEffect());
                    player.addEffect(newEffect);
                }
            }
        }
    }
}
