package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import javax.annotation.Nullable;
import java.util.List;

public class AbsorptionModifier extends BaseModifier {

    private static final int TICK_INTERVAL = 200;
    private static final float ABSORPTION_PERCENTAGE = 0.10f;

    private int getTotalModifierLevel(LivingEntity entity) {
        int totalLevel = 0;
        for (ItemStack stack : entity.getArmorSlots()) {
            if (!stack.isEmpty()) {
                IToolStackView tool = ToolStack.from(stack);
                totalLevel += tool.getModifiers().getLevel(this.getId());
            }
        }

        return totalLevel;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity entity, int slot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!level.isClientSide && isCorrectSlot && entity.tickCount % TICK_INTERVAL == 0) {
            float maxAbsorption = entity.getMaxHealth() * getTotalModifierLevel(entity);
            float currentAbsorption = entity.getAbsorptionAmount();
            float absorptionToAdd = entity.getMaxHealth() * ABSORPTION_PERCENTAGE;
            if (currentAbsorption + absorptionToAdd > maxAbsorption) {
                absorptionToAdd = maxAbsorption - currentAbsorption;
            }
            if (absorptionToAdd > 0) {
                entity.setAbsorptionAmount(currentAbsorption + absorptionToAdd);
            }
        }
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        if (entity instanceof Player player && modifier.getLevel() > 0) {
            player.setAbsorptionAmount(0);
        }
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifier, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        if (player != null) {
            tooltip.add(Component.translatable("modifier.sakuratinker.absorption.max")
                    .append(Component.literal(": " + getTotalModifierLevel(player) * player.getMaxHealth())));
        }
    }
}
