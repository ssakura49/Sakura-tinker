package com.ssakura49.sakuratinker.client.baked.tinker;

import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import slimeknights.tconstruct.library.client.model.tools.ToolModel;

import java.util.function.Function;

public class CosmicToolModelGeometry implements IUnbakedGeometry<CosmicToolModelGeometry> {
    private final ToolModel toolModel;

    public CosmicToolModelGeometry(ToolModel toolModel) {
        this.toolModel = toolModel;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context,
                           ModelBaker baker,
                           Function<Material, TextureAtlasSprite> spriteGetter,
                           ModelState modelState,
                           ItemOverrides overrides,
                           ResourceLocation modelLocation) {
        BakedModel baked = toolModel.bake(context, baker, spriteGetter, modelState, overrides, modelLocation);
        return new CosmicToolModelWrapper(baked, SakuraModelTransforms.TOOL);
    }
}