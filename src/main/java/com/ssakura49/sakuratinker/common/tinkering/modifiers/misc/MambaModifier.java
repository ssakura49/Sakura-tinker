package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.logic.helper.FlyingHelper;
import com.ssakura49.sakuratinker.register.STEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Map;
import java.util.WeakHashMap;

public class MambaModifier extends BaseModifier {
    private static final int CHECK_INTERVAL = 20;
    private static final float HEALTH_THRESHOLD = 0.5f;
    private static final float HEAL_PERCENT = 0.02f;
    private static final int VAMPIRING_DURATION = 30 * 20;
    private static final int COOLDOWN = 5 * 60 * 20;
    private final Map<LivingEntity, Integer> cooldowns = new WeakHashMap<>();

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int slot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!level.isClientSide && entity instanceof Player player) {
            float healthPercent = player.getHealth() / player.getMaxHealth();
            boolean isAboveThreshold = healthPercent > HEALTH_THRESHOLD;
            int currentCooldown = cooldowns.getOrDefault(player, 0);
            if (currentCooldown > 0) {
                cooldowns.put(player, currentCooldown - CHECK_INTERVAL);
            }
            if (isSelected || isCorrectSlot) {
                if (!player.isCreative() && !player.isSpectator() && !isAboveThreshold) {
                    if (currentCooldown <= 0 && !player.hasEffect(STEffects.VAMPIRING.get())) {
                        player.addEffect(new MobEffectInstance(
                                STEffects.VAMPIRING.get(),
                                VAMPIRING_DURATION,
                                0,
                                false,
                                true,
                                true
                        ));
                        cooldowns.put(player, COOLDOWN);
                    }
                } else {
                    FlyingHelper.tickFlying(player);
                }
            }
        }
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry entry, ToolAttackContext context, float damageDealt) {
        LivingEntity attacker = context.getAttacker();
        LivingEntity target = context.getLivingTarget();
        if (!attacker.level().isClientSide && attacker.hasEffect(STEffects.VAMPIRING.get())) {
            float healAmount = attacker.getMaxHealth() * HEAL_PERCENT;
            attacker.heal(healAmount);
        }
    }
}
