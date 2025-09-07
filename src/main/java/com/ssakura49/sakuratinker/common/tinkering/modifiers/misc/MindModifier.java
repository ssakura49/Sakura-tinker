package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class MindModifier extends BaseModifier {

    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int level = modifier.getLevel();
        if (context.hasTag(TinkerTags.Items.ARMOR)) {
            builder.multiplier(ToolStats.ARMOR, 1.5f * level);
            builder.multiplier(ToolStats.ARMOR_TOUGHNESS, 1.5f * level);
            builder.multiplier(ToolStats.KNOCKBACK_RESISTANCE, 1.5f * level);
            builder.multiplier(ToolStats.DURABILITY, 1.5f * level);
        } else {
            builder.multiplier(ToolStats.DURABILITY, 1.5f * level);
            builder.multiplier(ToolStats.MINING_SPEED, 1.5f * level);
            builder.multiplier(ToolStats.ATTACK_DAMAGE, 1.5f * level);
            builder.multiplier(ToolStats.ATTACK_SPEED, 1.5f * level);
            builder.multiplier(ToolStats.ACCURACY, 1.5f * level);
            builder.multiplier(ToolStats.VELOCITY, 1.5f * level);
            builder.multiplier(ToolStats.DRAW_SPEED, 1.5f * level);
        }
    }
}
