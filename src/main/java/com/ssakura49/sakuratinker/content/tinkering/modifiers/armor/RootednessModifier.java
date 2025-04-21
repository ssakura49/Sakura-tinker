package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class RootednessModifier extends BaseModifier {
    private boolean isValidSlot(EquipmentSlot slot) {
        return slot.isArmor();
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        LivingEntity entity = context.getEntity();
        if (isValidSlot(slotType) && !entity.level().isClientSide() && entity instanceof Player player) {
            float maxDamage = player.getMaxHealth() * 0.1f;
            if (amount > maxDamage) {
                return maxDamage;
            }
        }
        return amount;
    }
}
