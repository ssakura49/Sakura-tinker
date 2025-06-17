package com.ssakura49.sakuratinker.compat.EnigmaticLegacy.spellstone;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.UUID;
import java.util.function.BiConsumer;

public class WillOfOceanModifier extends BaseModifier {
    private static final UUID SWIM_SPEED_UUID = UUID.fromString("b54bc4d8-a4c5-410b-9ba7-8e71acfc01fa");
    private static final String SWIM_SPEED_NAME = "will_ocean_swim_speed";

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!level.isClientSide() && holder.isInWaterRainOrBubble() && isCorrectSlot) {
            if (holder instanceof Player player) {
                player.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 220, 2, true, false));
                player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 220, 0, true, false));
            }
            holder.setNoGravity(true);
        } else {
            holder.setNoGravity(false);
        }
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        if (slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST ||
                slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET) {
            consumer.accept(Attributes.MOVEMENT_SPEED, new AttributeModifier(
                    SWIM_SPEED_UUID,
                    SWIM_SPEED_NAME,
                    1.0,
                    AttributeModifier.Operation.MULTIPLY_TOTAL
            ));
        }
    }

    @Override
    public float onModifyTakeDamage(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        if (context.getEntity().isInWaterRainOrBubble()) {
            return amount * 0.6f;
        }
        return amount;
    }
}
