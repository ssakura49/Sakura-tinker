package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class KoboldModifier extends BaseModifier {
    private static final float HEALTH_THRESHOLD = 40.0f;

    @Override
    public float onModifyMeleeDamage(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, LivingEntity attacker, LivingEntity target, float baseDamage, float actualDamage) {
        if (attacker != null && target != null) {
            if (attacker instanceof Player player) {
                float max = player.getMaxHealth();
                if (max > HEALTH_THRESHOLD) {
                    return baseDamage * (1.0f + 0.1f * modifier.getLevel());
                }
            }
        }
        return baseDamage;
    }

    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!level.isClientSide()){
            if (entity instanceof Player player && isCorrectSlot) {
                float max = player.getMaxHealth();
                if (max > HEALTH_THRESHOLD) {
                    player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 60, 0, true, true));
                }
            }
        }

    }
}
