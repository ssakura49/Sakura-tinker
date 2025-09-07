package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class ShieldingModifier extends BaseModifier {
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        ToolStats.ARMOR_TOUGHNESS.add(builder, 0.1f * modifier.getLevel());
        ToolStats.ARMOR.percent(builder, 0.1f * modifier.getLevel());
    }
}
