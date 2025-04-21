package com.ssakura49.sakuratinker.content.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class MambaModifier extends BaseModifier {
    private static final int CHECK_INTERVAL = 20;
    private static final float HEALTH_THRESHOLD = 0.5f;

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int slot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!level.isClientSide && entity instanceof Player player && player.tickCount % CHECK_INTERVAL == 0) {
            float healthPercent = player.getHealth() / player.getMaxHealth();
            boolean isAboveThreshold = healthPercent > HEALTH_THRESHOLD;

            if (isSelected) {
                if (player.getAbilities().mayfly != isAboveThreshold) {
                    player.getAbilities().mayfly = isAboveThreshold;

                    if (!isAboveThreshold && player.getAbilities().flying) {
                        player.getAbilities().flying = false;
                        player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 100, 0)); // 改用缓降效果更友好
                    }
                    player.onUpdateAbilities();
                }
            }
            else if (player.getAbilities().mayfly && !player.isCreative() && !player.getAbilities().instabuild) {
                player.getAbilities().mayfly = false;
                player.getAbilities().flying = false;
                player.onUpdateAbilities();
            }
        }
    }
}
