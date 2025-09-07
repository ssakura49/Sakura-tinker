package com.ssakura49.sakuratinker.client.baked.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.render.STRenderBuffers;
import com.ssakura49.sakuratinker.render.shader.cosmic.CosmicItemShaders;
import com.ssakura49.sakuratinker.utils.render.TransformUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.*;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class FantasyCosmicBakedModel extends WrappedItemModel implements IItemRenderer {
    private final List<ResourceLocation> maskSprite;

    public FantasyCosmicBakedModel(BakedModel wrapped, List<ResourceLocation> maskSprite) {
        super(wrapped);
        this.maskSprite = maskSprite;
    }

    public void renderItem(ItemStack stack, ItemDisplayContext transformType, PoseStack pStack, MultiBufferSource source, int light, int overlay) {
        if (stack.getItem() instanceof TieredItem) {
            this.parentState = TransformUtils.HANDHELD;
        } else if (stack.getItem() instanceof BowItem) {
            this.parentState = TransformUtils.DEFAULT_BOW;
        } else if (stack.getItem() instanceof BlockItem) {
            this.parentState = TransformUtils.DEFAULT_BLOCK;
        } else this.parentState = TransformUtils.DEFAULT_ITEM;
        source = STRenderBuffers.getBufferSource();
        this.renderWrapped(stack, pStack, source, light, overlay, true, Function.identity());
        if (STConfig.Client.enable_cosmic_renderer) {
            if (source instanceof MultiBufferSource.BufferSource bs)
                bs.endBatch();
            this.renderWrappedCosmic(stack, pStack, source, light, overlay, true, Function.identity());
            Minecraft mc = Minecraft.getInstance();
            CosmicItemShaders.updateShaderData(transformType);
            VertexConsumer cons = source.getBuffer(CosmicItemShaders.COSMIC_RENDER_TYPE);
            List<TextureAtlasSprite> atlasSprite = new ArrayList<>();

            for (ResourceLocation res : this.maskSprite) {
                TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(res);

                atlasSprite.add(sprite);
            }
            mc.getItemRenderer().renderQuadList(pStack, cons, bakeItem(atlasSprite), stack, light, overlay);
            STRenderBuffers.getBufferSource().endBatch();
        }
    }

    public @Nullable PerspectiveModelState getModelState() {
        return (PerspectiveModelState) this.parentState;
    }

    public boolean isCosmic() {
        return true;
    }
}
