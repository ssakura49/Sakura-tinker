package com.ssakura49.sakuratinker.library.interfaces;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.interaction.InventoryTickModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

public interface BuilderFactory extends ToolStatsModifierHook, ConditionalStatModifierHook, InventoryTickModifierHook {
    default void initBuilderHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this, ModifierHooks.CONDITIONAL_STAT);
        builder.addHook(this, ModifierHooks.TOOL_STATS);
        builder.addHook(this, ModifierHooks.INVENTORY_TICK);
    }
    /**
     *添加工具属性
     */
    default void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
    }
    /**
     *物品栏每tick触发
     */
    default void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
    }
    /**
     *修改属性
     */
    default float modifyStat(IToolStackView tool, ModifierEntry modifier, LivingEntity entity, FloatToolStat stat, float baseValue, float multiplier) {
        return baseValue;
    }
}
