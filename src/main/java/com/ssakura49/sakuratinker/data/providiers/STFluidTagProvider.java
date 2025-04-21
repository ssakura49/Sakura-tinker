package com.ssakura49.sakuratinker.data.providiers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.data.STTagKeys;
import com.ssakura49.sakuratinker.register.STFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.common.TinkerTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class STFluidTagProvider extends FluidTagsProvider {
    public STFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup,@Nullable ExistingFileHelper helper) {
        super(output, lookup, SakuraTinker.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(STTagKeys.Fluids.molten_arcane_salvage).add(STFluids.molten_arcane_salvage.get());
        tag(STTagKeys.Fluids.molten_colorful).add(STFluids.molten_colorful.get());
        tag(STTagKeys.Fluids.molten_arcane_alloy).add(STFluids.molten_arcane_alloy.get());
        tag(STTagKeys.Fluids.molten_eezo).add(STFluids.molten_eezo.get());
        tag(STTagKeys.Fluids.molten_etherium).add(STFluids.molten_etherium.get());
        tag(STTagKeys.Fluids.molten_fiery_crystal).add(STFluids.molten_fiery_crystal.get());
        tag(STTagKeys.Fluids.molten_infinity).add(STFluids.molten_infinity.get());
        tag(STTagKeys.Fluids.molten_neutron).add(STFluids.molten_neutron.get());
        tag(STTagKeys.Fluids.molten_nihilite).add(STFluids.molten_nihilite.get());
        tag(STTagKeys.Fluids.molten_soul_sakura).add(STFluids.molten_soul_sakura.get());
        tag(STTagKeys.Fluids.molten_youkai).add(STFluids.molten_youkai.get());

        tag(TinkerTags.Fluids.METAL_TOOLTIPS).add(STFluids.molten_arcane_salvage.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).add(STFluids.molten_colorful.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).add(STFluids.molten_arcane_alloy.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).add(STFluids.molten_eezo.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).add(STFluids.molten_etherium.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).add(STFluids.molten_fiery_crystal.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).add(STFluids.molten_infinity.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).add(STFluids.molten_neutron.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).add(STFluids.molten_nihilite.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).add(STFluids.molten_soul_sakura.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).add(STFluids.molten_youkai.get());

        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_arcane_salvage.getId());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_colorful.getId());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_arcane_alloy.getId());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_eezo.getId());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_etherium.getId());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_fiery_crystal.getId());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_infinity.getId());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_neutron.getId());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_nihilite.getId());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_soul_sakura.getId());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_youkai.getId());
    }
}
