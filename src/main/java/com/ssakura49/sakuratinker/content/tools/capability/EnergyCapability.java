package com.ssakura49.sakuratinker.content.tools.capability;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.library.tools.STToolStats;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import slimeknights.tconstruct.library.tools.nbt.IModDataView;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

import java.util.function.Supplier;

public class EnergyCapability implements ToolCapabilityProvider.IToolCapabilityProvider, IEnergyStorage {
    public static final ResourceLocation MAX_ENERGY = SakuraTinker.location("max_energy");
    public static final ResourceLocation STORED_ENERGY = SakuraTinker.location("stored_energy");
    public static final ResourceLocation ENERGY_OWNER = SakuraTinker.location("energy_owner");
    protected final Supplier<? extends IToolStackView> tool;
    private final LazyOptional<IEnergyStorage> capOptional;

    public EnergyCapability(ItemStack stack, Supplier<? extends IToolStackView> toolStack) {
        this.tool = toolStack;
        this.capOptional = LazyOptional.of(() -> this);
    }

    public static int receiveEnergy(IToolStackView tool, int maxReceive, boolean simulate) {
        int energyStored = getEnergyStored(tool);
        int energyReceived = Math.min(getMaxEnergyStored(tool) - energyStored, Math.min(1000, maxReceive));
        if (!simulate) {
            ModDataNBT persistentData = tool.getPersistentData();
            persistentData.putInt(STORED_ENERGY, energyStored + energyReceived);
        }
        return energyReceived;
    }

    public static boolean removeEnergy(IToolStackView tool, int energyRemoved, boolean simulate, boolean drain) {
        int energyStored = getEnergyStored(tool);
        if (energyStored < energyRemoved) {
            if (drain && !simulate) {
                ModDataNBT persistentData = tool.getPersistentData();
                persistentData.putInt(STORED_ENERGY, 0);
            }
            return false;
        } else {
            if (!simulate) {
                ModDataNBT persistentData = tool.getPersistentData();
                persistentData.putInt(STORED_ENERGY, energyStored - energyRemoved);
            }
            return true;
        }
    }

    public static int getEnergyStored(IToolStackView tool) {
        ModDataNBT persistentData = tool.getPersistentData();
        return persistentData.contains(STORED_ENERGY, 3) ? persistentData.getInt(STORED_ENERGY) : 0;
    }

    public static int getMaxEnergyStored(IToolStackView tool) {
        IModDataView volatileData = tool.getVolatileData();
        if (volatileData.contains(MAX_ENERGY, 3)) {
            int energy_store = tool.getStats().getInt(STToolStats.ENERGY_STORE);
            return energy_store > 0 ? volatileData.getInt(MAX_ENERGY) + energy_store : volatileData.getInt(MAX_ENERGY);
        } else {
            return 0;
        }
    }

    public <T> LazyOptional<T> getCapability(IToolStackView tool, Capability<T> cap) {
        return tool.getVolatileData().getInt(MAX_ENERGY) > 0 && cap == ForgeCapabilities.ENERGY ? ForgeCapabilities.ENERGY.orEmpty(cap, this.capOptional) : LazyOptional.empty();
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        return receiveEnergy(this.tool.get(), maxReceive, simulate);
    }

    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    public int getEnergyStored() {
        return getEnergyStored(this.tool.get());
    }

    public int getMaxEnergyStored() {
        return getMaxEnergyStored(this.tool.get());
    }

    public boolean canExtract() {
        return false;
    }

    public boolean canReceive() {
        return true;
    }
}
