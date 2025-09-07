package com.ssakura49.sakuratinker.data.generator.providiers;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.compat.ExtraBotany.init.ExtraBotanyItems;
import com.ssakura49.sakuratinker.compat.Goety.init.GoetyItems;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.ISSCompat;
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
import slimeknights.tconstruct.common.data.model.MaterialModelBuilder;
import slimeknights.tconstruct.common.registration.CastItemObject;
import slimeknights.tconstruct.library.tools.part.MaterialItem;

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
            "terra_prisma",
            "bullet_ammo"
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
        return ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "item/" + typePath + "/" + path);
    }
    public ResourceLocation getBlockItemLocation(String path) {
        return ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "block/" + path);
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
        this.part(ExtraBotanyItems.phantom_core);
        this.part(STItems.axle);
        this.part(STItems.yoyo_body);
        this.part(STItems.yoyo_ring);
        this.part(STItems.chord);
        this.part(STItems.scythe_blade);
        this.part(GoetyItems.soul_gatherer);
        // Generate models for cast items
        this.cast(STItems.charmChainCast);
        this.cast(STItems.charmCoreCast);
        this.cast(STItems.swiftBladeCast);
        this.cast(STItems.swiftGuardCast);
        this.cast(STItems.barrelCast);
        this.cast(STItems.bladeCast);
        this.cast(ISSCompat.bookCoverCast);
        this.cast(ISSCompat.spellClothCast);
        this.cast(STItems.energyUnitCast);
        this.cast(STItems.laserMediumCast);
        this.cast(STItems.barrelCast);
        this.cast(STItems.arrowHeadCast);
        this.cast(STItems.arrowShaftCast);
        //TODO: maybe add fletching cast
        this.cast(STItems.greatBladeCast);
        this.cast(STItems.shellCast);
        this.cast(STItems.flagCast);
        this.cast(ExtraBotanyItems.phantomCoreCast);
        this.cast(STItems.axleCast);
        this.cast(STItems.yoyoBodyCast);
        this.cast(STItems.yoyoRingCast);
        this.cast(STItems.chordCast);
        this.cast(STItems.scytheBladeCast);
        this.cast(GoetyItems.soulGathererCast);
    }


    private ResourceLocation id(ItemLike item) {
        return BuiltInRegistries.ITEM.getKey(item.asItem());
    }

    private ItemModelBuilder generated(ResourceLocation item, ResourceLocation texture) {
        return (ItemModelBuilder)((ItemModelBuilder)((ItemModelBuilder)this.getBuilder(item.toString())).parent(this.GENERATED)).texture("layer0", texture);
    }

    private ItemModelBuilder generated(ResourceLocation item, String texture) {
        return this.generated(item, ResourceLocation.fromNamespaceAndPath(item.getNamespace(), texture));
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

    private void cast(CastItemObject cast) {
        String name = cast.getName().getPath();
        this.basicItem(cast.getId(), "cast/" + name);
        this.basicItem((ItemLike)cast.getSand(), "sand_cast/" + name);
        this.basicItem((ItemLike)cast.getRedSand(), "red_sand_cast/" + name);
    }
}
