package com.ssakura49.sakuratinker.generic;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.CustomBarDisplayModifierHook;
import com.ssakura49.sakuratinker.utils.MathUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.phys.Vec2;
import org.jetbrains.annotations.Nullable;
import slimeknights.mantle.client.TooltipKey;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ToolStatsModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;

import java.util.List;

import static com.ssakura49.sakuratinker.content.tools.capability.ForgeEnergyCapability.*;

@Deprecated(forRemoval = true, since = "1.2.8")
public abstract class ForgeEnergyModifier extends BaseModifier implements ModifierRemovalHook, TooltipModifierHook, ToolStatsModifierHook, CustomBarDisplayModifierHook, ValidateModifierHook {
    @Override
    public int getPriority() {
        return 25;
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this, ModifierHooks.TOOLTIP, ModifierHooks.REMOVE, ModifierHooks.TOOL_STATS, EtSTLibHooks.CUSTOM_BAR);
    }

    @Nullable
    @Override
    public Component validate(IToolStackView tool, ModifierEntry entry) {
        checkEnergy(tool);
        return null;
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView iToolStackView, Modifier modifier) {
        return null;
    }

    @Override
    public void addTooltip(IToolStackView tool, ModifierEntry entry, @Nullable Player player, List<Component> list, TooltipKey tooltipKey, TooltipFlag tooltipFlag) {
        Component component = Component
                .translatable("tooltip.sakuratinker.energy_storage")
                .append(":")
                .append(" " + MathUtil.getEnergyString(getEnergy(tool)) + "/"+MathUtil.getEnergyString(getMaxEnergy(tool))).withStyle(Style.EMPTY.withColor(0x0030FF));
        list.add(component);
    }

    @Override
    public void addToolStats(IToolContext iToolContext, ModifierEntry modifierEntry, ModifierStatsBuilder modifierStatsBuilder) {
        ToolEnergyCapability.MAX_STAT.add(modifierStatsBuilder,getCapacity(modifierEntry));
    }
    public abstract int getCapacity(ModifierEntry modifierEntry);

    @Override
    public boolean showBar(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return getEnergy(tool)>0;
    }

    @Override
    public int getBarRGB(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return 0xFF4169E1;
    }

    @Override
    public Vec2 getBarXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        int FE = getEnergy(tool);
        int maxStorage = getMaxEnergy(tool);
        if (maxStorage>0) {
            return new Vec2(Math.min(13, 13 * FE / maxStorage), 1);
        }
        return new Vec2(0,0);
    }

    @Override
    public String barId(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return "sakuratinker:fe_bar";
    }
}
