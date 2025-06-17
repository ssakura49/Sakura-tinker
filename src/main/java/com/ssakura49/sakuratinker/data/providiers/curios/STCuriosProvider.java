package com.ssakura49.sakuratinker.data.providiers.curios;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class STCuriosProvider extends CuriosDataProvider {
    public STCuriosProvider(String modId, PackOutput output, ExistingFileHelper fileHelper, CompletableFuture<HolderLookup.Provider> registries) {
        super(modId, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        this.createSlot("tinker_charm")
                .replace(false)
                .size(1)
                .operation("SET")
                .order(10)
                .icon(ResourceLocation.parse("curios:slot/empty_charm_slot"))
                .addCosmetic(false)
                .useNativeGui(true)
                .renderToggle(true)
                .dropRule(ICurio.DropRule.DEFAULT);
        this.createSlot("fox_mask")
                .replace(false)
                .size(1)
                .operation("SET")
                .order(10)
                .icon(ResourceLocation.parse("sakuratinker:slot/empty_fox_mask_slot"))
                .addCosmetic(false)
                .useNativeGui(true)
                .renderToggle(true)
                .dropRule(ICurio.DropRule.DEFAULT);
        this.createEntities("player_curios").addSlots("tinker_charm").addSlots("fox_mask").addPlayer();
    }


}
