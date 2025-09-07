package com.ssakura49.sakuratinker.compat.EnigmaticLegacy.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class UltraHostility extends BaseModifier {

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int level = modifier.getLevel();
        float bonus = 0.2f * level;
        ToolStats.MINING_SPEED.multiply(builder, 1 + bonus * modifier.getLevel());
        ToolStats.ATTACK_SPEED.multiply(builder, 1 + bonus * modifier.getLevel());
        ToolStats.ATTACK_DAMAGE.multiply(builder, 1 + bonus * modifier.getLevel());
        ToolStats.VELOCITY.multiply(builder, 1 + bonus * modifier.getLevel());
        ToolStats.ARMOR.multiply(builder, 1 + bonus * modifier.getLevel());
        ToolStats.ARMOR_TOUGHNESS.multiply(builder, 1 + bonus * modifier.getLevel());
        ToolStats.DURABILITY.multiply(builder, 1 + bonus * modifier.getLevel());
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (holder != null && holder.isOnFire()) {
            if (isCorrectSlot || isSelected) {
                holder.clearFire();
            }
        }
    }
}
