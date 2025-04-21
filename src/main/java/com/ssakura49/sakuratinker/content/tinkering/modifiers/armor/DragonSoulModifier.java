package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class DragonSoulModifier extends BaseModifier {
    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        int level = modifier.getLevel();
        ToolStats.DURABILITY.add(builder, 500f * level);
        ToolStats.ARMOR.add(builder, (double) 3.0f * level);
        ToolStats.ARMOR_TOUGHNESS.add(builder, (double) 2.0f * level);
    }
}
