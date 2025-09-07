package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class SolidModifier extends BaseModifier {
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int level = modifier.getLevel();
        ToolStats.ARMOR_TOUGHNESS.add(builder, level);
        ToolStats.KNOCKBACK_RESISTANCE.add(builder, 0.1 * level);
        ToolStats.DURABILITY.multiply(builder, 1 + 0.1 * level);
    }
}
