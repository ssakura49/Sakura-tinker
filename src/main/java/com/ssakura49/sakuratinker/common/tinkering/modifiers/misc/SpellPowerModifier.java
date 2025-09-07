package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public class SpellPowerModifier extends BaseModifier {
    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        STToolStats.SPELL_POWER.add(builder, 0.1f * modifier.getLevel());
    }
}
