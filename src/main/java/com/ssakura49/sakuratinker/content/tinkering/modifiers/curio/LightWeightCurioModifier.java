package com.ssakura49.sakuratinker.content.tinkering.modifiers.curio;

import com.ssakura49.sakuratinker.generic.CurioModifier;
import com.ssakura49.sakuratinker.library.tools.STToolStats;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public class LightWeightCurioModifier extends CurioModifier {
    public LightWeightCurioModifier(){}

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        STToolStats.MOVEMENT_SPEED.add(builder, (double)(0.05F * (float)modifier.getLevel()));
    }

    @Override
    public void onCurioBreakSpeed(IToolStackView curio, PlayerEvent.BreakSpeed event, Player player, int level) {
        event.setNewSpeed(event.getNewSpeed() * (1.0F + 0.07F * (float)level));
    }
}
