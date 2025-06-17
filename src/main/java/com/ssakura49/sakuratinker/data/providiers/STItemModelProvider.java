package com.ssakura49.sakuratinker.data.providiers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.ExtraBotany.ExtraBotanyCompat;
import com.ssakura49.sakuratinker.register.STItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.loaders.DynamicFluidContainerModelBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.mantle.registration.object.ItemObject;
import slimeknights.tconstruct.TConstruct;
import slimeknights.tconstruct.common.data.model.MaterialModelBuilder;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.part.MaterialItem;
import slimeknights.tconstruct.library.tools.part.PartCastItem;

import java.util.Set;

public class STItemModelProvider extends ItemModelProvider {
    public static final String ITEM = "item/generated";
    public static final String BUCKET_FLUID = "forge:item/bucket_drip";
    private final ModelFile.UncheckedModelFile GENERATED = new ModelFile.UncheckedModelFile("item/generated");

    public STItemModelProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, SakuraTinker.MODID, helper);
    }
    private static final Set<String> ITEMS = Set.of(
            "colorful_ingot",
            "goozma",
            "ghost_knife",
            "grappling_blade",
            "zenith_first_fractal",
            "terra_prisma"
    );

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
    public void generateCastModel(ItemObject<PartCastItem> cast) {
        withExistingParent(cast.getId().getPath(), ITEM).texture("layer0", new ResourceLocation(SakuraTinker.MODID, "item/cast/" + cast.getId().getPath()));
    }


    @Override
    protected void registerModels() {
        for (RegistryObject<Item> object : STItems.getListSimpleModel()) {
            String path = object.getId().getPath();
            if (ITEMS.contains(path)) continue;
            generateItemModel(object, "material");
        }

        for (RegistryObject<BlockItem> object : STItems.getListSimpleBlock()) {
            generateBlockItemModel(object);
        }
        this.part(ExtraBotanyCompat.phantom_core);

        // Generate models for cast items
        generateCastModel(STItems.charm_chain_cast);
        generateCastModel(STItems.charm_chain_red_sand_cast);
        generateCastModel(STItems.charm_chain_sand_cast);
        generateCastModel(STItems.charm_core_cast);
        generateCastModel(STItems.charm_core_red_sand_cast);
        generateCastModel(STItems.charm_core_sand_cast);
        generateCastModel(STItems.swift_blade_cast);
        generateCastModel(STItems.swift_blade_red_sand_cast);
        generateCastModel(STItems.swift_blade_sand_cast);
        generateCastModel(STItems.swift_guard_cast);
        generateCastModel(STItems.swift_guard_red_sand_cast);
        generateCastModel(STItems.swift_guard_sand_cast);
        generateCastModel(STItems.barrel_cast);
        generateCastModel(STItems.barrel_red_sand_cast);
        generateCastModel(STItems.barrel_sand_cast);
        generateCastModel(STItems.energy_unit_cast);
        generateCastModel(STItems.energy_unit_red_sand_cast);
        generateCastModel(STItems.energy_unit_sand_cast);
        generateCastModel(STItems.laser_medium_cast);
        generateCastModel(STItems.laser_medium_red_sand_cast);
        generateCastModel(STItems.laser_medium_sand_cast);
        generateCastModel(STItems.blade_cast);
        generateCastModel(STItems.blade_red_sand_cast);
        generateCastModel(STItems.blade_sand_cast);
        generateCastModel(STItems.arrow_head_cast);
        generateCastModel(STItems.arrow_head_red_sand_cast);
        generateCastModel(STItems.arrow_head_sand_cast);
        generateCastModel(STItems.arrow_shaft_cast);
        generateCastModel(STItems.arrow_shaft_red_sand_cast);
        generateCastModel(STItems.arrow_shaft_sand_cast);
        generateCastModel(STItems.great_blade_cast);
        generateCastModel(STItems.great_blade_red_sand_cast);
        generateCastModel(STItems.great_blade_sand_cast);
        generateCastModel(STItems.shell_cast);
        generateCastModel(STItems.shell_red_sand_cast);
        generateCastModel(STItems.shell_sand_cast);
        generateCastModel(STItems.flag_cast);
        generateCastModel(STItems.flag_red_sand_cast);
        generateCastModel(STItems.flag_sand_cast);
        generateCastModel(STItems.fox_mask_main_cast);
        generateCastModel(STItems.fox_mask_main_red_sand_cast);
        generateCastModel(STItems.fox_mask_main_sand_cast);
        generateCastModel(STItems.fox_mask_core_cast);
        generateCastModel(STItems.fox_mask_core_red_sand_cast);
        generateCastModel(STItems.fox_mask_core_sand_cast);
        generateCastModel(ExtraBotanyCompat.phantom_core_cast);
        generateCastModel(ExtraBotanyCompat.phantom_core_red_sand_cast);
        generateCastModel(ExtraBotanyCompat.phantom_core_sand_cast);
    }


    private ResourceLocation id(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    private ItemModelBuilder generated(ResourceLocation item, ResourceLocation texture) {
        return (ItemModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(item.toString())).parent(this.GENERATED)).texture("layer0", texture);
    }

    private ItemModelBuilder generated(ResourceLocation item, String texture) {
        return this.generated(item, new ResourceLocation(item.getNamespace(), texture));
    }

    private ItemModelBuilder generated(ItemLike item, String texture) {
        return this.generated(this.id(item), texture);
    }

    private ItemModelBuilder basicItem(ResourceLocation item, String texture) {
        return this.generated(item, "item/" + texture);
    }

    private ItemModelBuilder basicItem(ItemLike item, String texture) {
        return this.basicItem(this.id(item), texture);
    }

    private MaterialModelBuilder<ItemModelBuilder> part(ResourceLocation part, String texture) {
        return (MaterialModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.withExistingParent(part.getPath(), "forge:item/default")).texture("texture", SakuraTinker.location("item/tool/" + texture))).customLoader(MaterialModelBuilder::new);
    }

    private MaterialModelBuilder<ItemModelBuilder> part(Item item, String texture) {
        return this.part(this.id(item), texture);
    }

    private MaterialModelBuilder<ItemModelBuilder> part(ItemObject<? extends MaterialItem> part, String texture) {
        return this.part(part.getId(), texture);
    }

    private void part(ItemObject<? extends MaterialItem> part) {
        this.part(part, "part/" + part.getId().getPath() + "/" + part.getId().getPath());
    }
}
