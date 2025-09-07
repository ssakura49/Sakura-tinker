package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;
import java.util.Random;

public class SpectralModifier extends BaseModifier {
    private static final Random RANDOM = new Random();

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public float modifyDamageTaken(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        float dodgeChance = 0.08f + (modifier.getLevel() - 1) * 0.02f;
        if (RANDOM.nextFloat() < dodgeChance) {
            return 0;
        }
        return amount;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry modifierEntry, @Nullable Player player, List<Component> tooltip, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        float chance = 0.08f + (modifierEntry.getLevel() - 1) * 0.02f;
        tooltip.add(Component.translatable("modifier.sakuratinker.spectral.tooltip", String.format("%.0f%%", chance * 100)));
    }
}
