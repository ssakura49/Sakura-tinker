package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class GaleModifier extends BaseModifier {

    private boolean isArmorSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
    }

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        if (isArmorSlot(context.getChangedSlot()) && entity instanceof ServerPlayer player && !player.isSpectator()) {
            float boost = modifier.getLevel() * 0.1f;
            player.getAbilities().setFlyingSpeed(boost);
            player.onUpdateAbilities();
        }
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        if (isArmorSlot(context.getChangedSlot()) && entity instanceof ServerPlayer player && !player.isSpectator()) {
            float boost = modifier.getLevel() * 0.1f;
            player.getAbilities().setFlyingSpeed(Math.max(player.getAbilities().getFlyingSpeed() - boost, 0.05f));
            player.onUpdateAbilities();
        }
    }
}
