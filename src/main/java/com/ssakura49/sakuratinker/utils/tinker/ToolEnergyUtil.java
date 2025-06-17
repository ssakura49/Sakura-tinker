package com.ssakura49.sakuratinker.utils.tinker;

import com.ssakura49.sakuratinker.content.tools.capability.ForgeEnergyCapability;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability.*;

public class ToolEnergyUtil {
    public ToolEnergyUtil() {}

    public static int receiveEnergy(IToolStackView tool, int maxReceive, boolean simulate) {
        if (maxReceive <= 0) {
            return 0;
        } else {
            int current = ForgeEnergyCapability.getEnergy(tool);
            int filled = Math.min(ForgeEnergyCapability.getMaxEnergy(tool) - current, maxReceive);
            if (!simulate) {
                ForgeEnergyCapability.setEnergy(tool, current + filled);
            }
            return filled;
        }
    }

    public static int extractEnergy(IToolStackView tool, int maxExtract, boolean simulate) {
        if (maxExtract <= 0) {
            return 0;
        } else {
            int current = ForgeEnergyCapability.getEnergy(tool);
            if (current <= 0) {
                return 0;
            } else {
                int drained = Math.min(current, maxExtract);
                if (!simulate) {
                    ForgeEnergyCapability.setEnergy(tool, current - drained);
                }
                return drained;
            }
        }
    }
}
