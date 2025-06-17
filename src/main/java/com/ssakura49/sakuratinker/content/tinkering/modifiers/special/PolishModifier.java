package com.ssakura49.sakuratinker.content.tinkering.modifiers.special;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.library.tinkering.modifiers.ToolDamageModifier;
import com.ssakura49.sakuratinker.register.STModifiers;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.FloatToolStat;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStatId;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class PolishModifier extends ToolDamageModifier implements ToolStatsModifierHook {
    public static final FloatToolStat POLISH_STAT = ToolStats.register(new FloatToolStat(new ToolStatId(SakuraTinker.MODID, "polish"), 0xFF71DC85, 0, 0, Short.MAX_VALUE) {
        @Override
        public Float build(ModifierStatsBuilder parent, Object builderObj) {
            return super.build(parent, builderObj) *(1.0f + parent.getMultiplier(ToolStats.ATTACK_DAMAGE));
        }
    });

    @Override
    protected void registerHooks(ModuleHookMap.@NotNull Builder hookBuilder) {
        super.registerHooks(hookBuilder);
        hookBuilder.addHook(this, ModifierHooks.TOOL_STATS);
    }

//    @Override
//    public Component getDisplayName(int level) {
//        return super.getDisplayName();
//    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        POLISH_STAT.add(builder, 50);
    }

    /* Polish implementation */
    @Override
    protected ResourceLocation getPolishKey() {
        return getId();
    }

    @Override
    public int getPolishCapacity(IToolStackView tool, ModifierEntry modifier) {
        return tool.getStats().getInt(POLISH_STAT);
    }

    public void addPolishValue(IToolStackView tool, ModifierEntry entry, int amount) {
        this.addPolish(tool, entry, amount * (1 + tool.getModifierLevel(STModifiers.Whetstone.get())));
    }
}
