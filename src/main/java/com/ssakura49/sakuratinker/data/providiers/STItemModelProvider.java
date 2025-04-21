package com.ssakura49.sakuratinker.data.providiers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.object.FluidObject;

public class STItemModelProvider extends ItemModelProvider {
    public static final String ITEM = "item/generated";
    public static final String BUCKET_FLUID = "forge:item/bucket_drip";

    public STItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, SakuraTinker.MODID, helper);
    }

    public void generateItemModel(RegistryObject<Item> object, String typePath) {
        withExistingParent(object.getId().getPath(), ITEM).texture("layer0", getItemLocation(object.getId().getPath(), typePath));
    }
    public void generateBlockItemModel(RegistryObject<BlockItem> object) {
        withExistingParent(object.getId().getPath(), getBlockItemLocation(object.getId().getPath()));
    }
    public void generateBucketItemModel(FluidObject<ForgeFlowingFluid> object, boolean flip) {
        withExistingParent(object.getId().getPath() + "_bucket", BUCKET_FLUID).customLoader(((itemModelBuilder, helper) -> DynamicFluidContainerModelBuilder
                .begin(itemModelBuilder, helper)
                .fluid(object.get())
                .flipGas(flip)
        ));
    }

    public ResourceLocation getItemLocation(String path, String typePath) {
        return new ResourceLocation(SakuraTinker.MODID, "item/" + typePath + "/" + path);
    }
    public ResourceLocation getBlockItemLocation(String path) {
        return new ResourceLocation(SakuraTinker.MODID, "block/" + path);
    }

    @Override
    protected void registerModels() {
        for (RegistryObject<Item> object : STItems.getListSimpleModel()) {
            generateItemModel(object, "material");
        }
        for (RegistryObject<BlockItem> object : STItems.getListSimpleBlock()) {
            generateBlockItemModel(object);
        }
    }
}
