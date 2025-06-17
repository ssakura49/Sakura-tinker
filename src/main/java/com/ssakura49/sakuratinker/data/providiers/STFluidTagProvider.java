package com.ssakura49.sakuratinker.data.providiers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.data.STTagKeys;
import com.ssakura49.sakuratinker.register.STFluids;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.common.TinkerTags;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class STFluidTagProvider extends FluidTagsProvider {
    public STFluidTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup,@Nullable ExistingFileHelper helper) {
        super(output, lookup, SakuraTinker.MODID, helper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
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
        tag(STTagKeys.Fluids.molten_crystal_matrix).add(STFluids.molten_crystal_matrix.get());
        tag(STTagKeys.Fluids.molten_blood_bound_steel).add(STFluids.molten_blood_bound_steel.get());
        tag(STTagKeys.Fluids.molten_blood).add(STFluids.molten_blood.get());
        tag(STTagKeys.Fluids.molten_dragon_fire_steel).add(STFluids.molten_dragon_fire_steel.get());
        tag(STTagKeys.Fluids.molten_dragon_ice_steel).add(STFluids.molten_dragon_ice_steel.get());
        tag(STTagKeys.Fluids.molten_dragon_lightning_steel).add(STFluids.molten_dragon_lightning_steel.get());
        tag(STTagKeys.Fluids.molten_steady_alloy).add(STFluids.molten_steady_alloy.get());
        tag(STTagKeys.Fluids.molten_south_star).add(STFluids.molten_south_star.get());
        tag(STTagKeys.Fluids.molten_terracryst).add(STFluids.molten_terracryst.get());
        tag(STTagKeys.Fluids.molten_prometheum).add(STFluids.molten_prometheum.get());
        tag(STTagKeys.Fluids.molten_orichalcum).add(STFluids.molten_orichalcum.get());
        tag(STTagKeys.Fluids.molten_aurumos).add(STFluids.molten_aurumos.get());
        tag(STTagKeys.Fluids.molten_bear_interest).add(STFluids.molten_bear_interest.get());
        tag(STTagKeys.Fluids.molten_mana_steel).add(STFluids.molten_mana_steel.get());
        tag(STTagKeys.Fluids.molten_goozma).add(STFluids.molten_goozma.get());
        tag(STTagKeys.Fluids.molten_frost_slimesteel).add(STFluids.molten_frost_slimesteel.get());
        tag(STTagKeys.Fluids.molten_echo_slimesteel).add(STFluids.molten_echo_slimesteel.get());
        tag(STTagKeys.Fluids.molten_mycelium_slimesteel).add(STFluids.molten_mycelium_slimesteel.get());
        tag(STTagKeys.Fluids.molten_orichalcos).add(STFluids.molten_orichalcos.get());
        tag(STTagKeys.Fluids.molten_pyrothium).add(STFluids.molten_pyrothium.get());
        tag(STTagKeys.Fluids.molten_cursed_metal).add(STFluids.molten_cursed_metal.get());
        tag(STTagKeys.Fluids.molten_dark_metal).add(STFluids.molten_dark_metal.get());
        tag(STTagKeys.Fluids.molten_unholy_alloy).add(STFluids.molten_unholy_alloy.get());
        tag(STTagKeys.Fluids.molten_dread_steel).add(STFluids.molten_dread_steel.get());
        tag(STTagKeys.Fluids.molten_terra_steel).add(STFluids.molten_terra_steel.get());

        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_arcane_salvage.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_colorful.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_arcane_alloy.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_eezo.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_etherium.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_fiery_crystal.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_infinity.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_neutron.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_nihilite.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_soul_sakura.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_youkai.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_crystal_matrix.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_blood_bound_steel.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_blood.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_dragon_fire_steel.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_dragon_ice_steel.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_dragon_lightning_steel.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_steady_alloy.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_south_star.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_terracryst.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_prometheum.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_orichalcum.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_aurumos.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_bear_interest.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_mana_steel.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_goozma.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_frost_slimesteel.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_echo_slimesteel.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_mycelium_slimesteel.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_orichalcos.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_pyrothium.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_cursed_metal.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_dark_metal.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_unholy_alloy.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_dread_steel.get());
        tag(TinkerTags.Fluids.METAL_TOOLTIPS).replace(false).add(STFluids.molten_terra_steel.get());

//        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_arcane_salvage.getId());
//        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_colorful.getId());
//        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_arcane_alloy.getId());
//        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_eezo.getId());
//        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_etherium.getId());
//        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_fiery_crystal.getId());
//        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_infinity.getId());
//        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_neutron.getId());
//        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_nihilite.getId());
//        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_soul_sakura.getId());
//        tag(TinkerTags.Fluids.METAL_TOOLTIPS).addOptional(STFluids.molten_youkai.getId());
    }
}
