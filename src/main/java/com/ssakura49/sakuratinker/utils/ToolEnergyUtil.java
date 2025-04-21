package com.ssakura49.sakuratinker.utils;

import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import static slimeknights.tconstruct.library.tools.capability.ToolEnergyCapability.*;

public class ToolEnergyUtil {
    public static int receiveEnergy(IToolStackView tool, int maxReceive, boolean simulate) {
        if (maxReceive <= 0) {
            return 0;
        } else {
            int current = getEnergy(tool);
            int filled = Math.min(getMaxEnergy(tool) - current, maxReceive);
            if (!simulate) {
                setEnergy(tool, current + filled);
            }

            return filled;
        }
    }

    public static int extractEnergy(IToolStackView tool, int maxExtract, boolean simulate) {
        if (maxExtract <= 0) {
            return 0;
        } else {
            int current = getEnergy(tool);
            if (current <= 0) {
                return 0;
            } else {
                int drained = maxExtract;
                if (current < drained) {
                    drained = current;
                }

                if (!simulate) {
                    setEnergy(tool, current - drained);
                }

                return drained;
            }
        }
    }
}
