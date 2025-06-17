package com.ssakura49.sakuratinker.generic;

import com.ssakura49.sakuratinker.library.hooks.curio.CurioArrowHook;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioBehaviorHook;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioBuilderHook;
import com.ssakura49.sakuratinker.library.hooks.curio.CurioCombatHook;
import com.ssakura49.sakuratinker.library.tinkering.tools.STHooks;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ConditionalStatModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.TinkerDataCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import top.theillusivec4.curios.api.SlotContext;

public abstract class CurioModifier extends BaseModifier implements
        CurioBuilderHook,
        CurioBehaviorHook,
        CurioCombatHook,
        CurioArrowHook,
        ToolStatsModifierHook,
        ConditionalStatModifierHook {
    public CurioModifier(){}

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        builder.addHook(this,
                STHooks.CURIO_BUILDER,
                STHooks.CURIO_BEHAVIOR,
                STHooks.CURIO_COMBAT,
                STHooks.CURIO_ARROW,
                ModifierHooks.CONDITIONAL_STAT,
                ModifierHooks.TOOL_STATS);
    }

    @Override
    public float modifyStat(IToolStackView curio, ModifierEntry modifier, LivingEntity entity, FloatToolStat stat, float baseValue, float multiplier) {
        return baseValue;
    }

    @Override
    public void addToolStats(IToolContext curio, ModifierEntry modifier, ModifierStatsBuilder builder) {
    }

    @Override
    public void onCurioEquip(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack prevStack, ItemStack stack) {
        this.onUseKeyEquip(entity, level, false);
    }

    @Override
    public void onCurioUnequip(IToolStackView curio, SlotContext context, LivingEntity entity, int level, ItemStack newStack, ItemStack stack) {
        this.onUseKeyUnequip(entity);
    }

    public TinkerDataCapability.TinkerDataKey<Integer> useKey() {
        return null;
    }

    public void onUseKeyEquip(LivingEntity entity, int level, boolean toAdd) {
        if (this.useKey() != null) {
            entity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((holder) -> {
                int def = (Integer)holder.get(this.useKey(), 0);
                if (def < level && !toAdd) {
                    holder.put(this.useKey(), level);
                } else {
                    holder.put(this.useKey(), def + level);
                }
            });
        }
    }

    public void onUseKeyUnequip(LivingEntity entity) {
        if (this.useKey() != null) {
            entity.getCapability(TinkerDataCapability.CAPABILITY).ifPresent((holder) -> holder.remove(this.useKey()));
        }
    }
}
