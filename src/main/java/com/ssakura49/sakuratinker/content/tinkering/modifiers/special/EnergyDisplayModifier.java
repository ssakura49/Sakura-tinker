package com.ssakura49.sakuratinker.content.tinkering.modifiers.special;

import com.c2h6s.etstlib.register.EtSTLibHooks;
import com.c2h6s.etstlib.tool.hooks.CustomBarDisplayModifierHook;
import com.c2h6s.etstlib.util.MathUtil;
import com.ssakura49.sakuratinker.content.tools.capability.ForgeEnergyCapability;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.Modifier;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.build.ModifierRemovalHook;
import slimeknights.tconstruct.library.modifiers.hook.build.ValidateModifierHook;
import slimeknights.tconstruct.library.modifiers.hook.display.TooltipModifierHook;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.nbt.*;

import java.awt.*;

import static slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability.MAX_STAT;

public class EnergyDisplayModifier extends BaseModifier implements ValidateModifierHook, ModifierRemovalHook, CustomBarDisplayModifierHook, TooltipModifierHook {
    public EnergyDisplayModifier() {
    }

    @Override
    protected void registerHooks(ModuleHookMap.Builder builder) {
        super.registerHooks(builder);
        builder.addHook(this,
                ModifierHooks.VALIDATE,
                ModifierHooks.TOOLTIP,
                ModifierHooks.REMOVE,
                EtSTLibHooks.CUSTOM_BAR);
    }

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Nullable
    @Override
    public Component validate(IToolStackView tool, ModifierEntry modifier) {
        ForgeEnergyCapability.checkEnergy(tool);
        return null;
    }

    @Nullable
    @Override
    public Component onRemoved(IToolStackView tool, Modifier modifier) {
        ForgeEnergyCapability.checkEnergy(tool);
        return null;
    }

    @Override
    public @NotNull Component getDisplayName(IToolStackView tool, ModifierEntry entry, @Nullable RegistryAccess access) {
        int current = ForgeEnergyCapability.getEnergy(tool);
        int max = ForgeEnergyCapability.getMaxEnergy(tool);

        String energyInfo = MathUtil.getEnergyString(current) + " / " + MathUtil.getEnergyString(max);
        String percentage = MathUtil.toPercentage((double)current/max, 1);

        return Component.translatable("tooltip.sakuratinker.energy_storage", energyInfo, percentage).withStyle(style -> style.withColor(getDynamicColor(current, max)));
    }

    private static int getDynamicColor(int current, int max) {
        float ratio = (float)current / max;
        return Color.HSBtoRGB(ratio * 0.35f, 0.9f, 0.9f);
    }

    @Override
    public String barId(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return "sakuratinker:energy_bar";
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public boolean showBar(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return tool.getStats().getInt(MAX_STAT) + tool.getStats().getInt(STToolStats.ENERGY_STORAGE) > 0;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public Vec2 getBarXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        int currentEnergy = getEnergy(tool);
        int maxEnergy = getMaxEnergy(tool);
        if (maxEnergy > 0) {
            int width = Math.min(13, 13 * currentEnergy / maxEnergy);
            return new Vec2(width, 1);
        }
        return new Vec2(0, 0);
    }

    @Override
    public Vec2 getBarXYPos(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return new Vec2(2, 14 - barsHadBeenShown * 3);
    }

    @Override
    public int getBarRGB(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        float percent = (float) getEnergy(tool) / getMaxEnergy(tool);
        int r = (int) (0x1E + (0x3C - 0x1E) * percent);
        int g = (int) (0x90 + (0xB3 - 0x90) * percent);
        int b = (int) (0xFF + (0x71 - 0xFF) * percent);
        return FastColor.ARGB32.color(255, r, g, b);
    }

    @Override
    public Vec2 getShadowXYSize(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return barsHadBeenShown > 0 ? new Vec2(13, 1) : new Vec2(13, 2);
    }

    @Override
    public Vec2 getShadowXYOffset(IToolStackView tool, ModifierEntry entry, int barsHadBeenShown) {
        return new Vec2(0, 0.5f);
    }

    private int getEnergy(IToolStackView tool) {
        return ForgeEnergyCapability.getEnergy(tool);
    }

    private int getMaxEnergy(IToolStackView tool) {
        return ForgeEnergyCapability.getMaxEnergy(tool);
    }
}
