package com.ssakura49.sakuratinker.library.interfaces;

import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public interface CuriosFactory extends ToolStatsModifierHook, ConditionalStatModifierHook, TooltipModifierHook {
    default void initCuriosHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.TOOLTIP);
        builder.addHook(this, ModifierHooks.CONDITIONAL_STAT);
        builder.addHook(this, ModifierHooks.TOOL_STATS);
    }
    /**
     *修改属性
     */
    default float modifyStat(IToolStackView curio, ModifierEntry modifier, LivingEntity entity, FloatToolStat stat, float baseValue, float multiplier) {
        return baseValue;
    }
    /**
     *添加属性
     */
    default void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
    }
    /**
     *添加tooltip
     */
    default void addTooltip(IToolStackView curio, ModifierEntry modifier, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
    }
}
