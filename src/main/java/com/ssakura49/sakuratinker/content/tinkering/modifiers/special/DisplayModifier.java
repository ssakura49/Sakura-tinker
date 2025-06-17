/*
package com.ssakura49.sakuratinker.content.tinkering.modifiers.special;

import com.c2h6s.etstlib.util.MathUtil;
import com.ssakura49.sakuratinker.content.tools.capability.ForgeEnergyCapability;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.awt.*;

public class EnergyDisplayModifier extends NoLevelsModifier {
    @Override
    public @NotNull Component getDisplayName(IToolStackView tool, ModifierEntry entry, @Nullable RegistryAccess access) {
        int current = ForgeEnergyCapability.getEnergy(tool);
        int max = ForgeEnergyCapability.getMaxEnergy(tool);

        String energyInfo = MathUtil.getEnergyString(current) + " / " + MathUtil.getEnergyString(max);
        String percentage = MathUtil.toPercentage((double)current/max, 1);

        return Component.translatable("tooltip.sakuratinker.energy_display",
                        energyInfo, percentage)
                .withStyle(style -> style.withColor(getDynamicColor(current, max)));
    }

    private static int getDynamicColor(int current, int max) {
        float ratio = (float)current / max;
        return Color.HSBtoRGB(ratio * 0.35f, 0.9f, 0.9f); // 红→黄→绿渐变
    }
}

 */
