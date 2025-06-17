package com.ssakura49.sakuratinker.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.client.helper.DelegatedVertexConsumer;
import com.ssakura49.sakuratinker.mixin.ItemRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RenderUtil {
    public static void renderItemCustomColor(LivingEntity entity, ItemStack stack, int color, PoseStack ms, MultiBufferSource buffers, int light, int overlay, @Nullable BakedModel model) {
        ms.pushPose();
        if (model == null) {
            model = Minecraft.getInstance().getItemRenderer().getModel(stack, entity.level(), entity, entity.getId());
        }

        model.getTransforms().getTransform(ItemDisplayContext.NONE).apply(false, ms);
        ms.translate((double)-0.5F, (double)-0.5F, (double)-0.5F);
        if (!model.isCustomRenderer() && !stack.is(Items.TRIDENT)) {
            RenderType rendertype = ItemBlockRenderTypes.getRenderType(stack, true);
            VertexConsumer ivertexbuilder = ItemRenderer.getFoilBufferDirect(buffers, rendertype, true, stack.hasFoil());
            renderBakedItemModel(model, stack, color, light, overlay, ms, ivertexbuilder);
            ms.popPose();
        } else {
            throw new IllegalArgumentException("Custom renderer items not supported");
        }
    }
    private static void renderBakedItemModel(BakedModel model, ItemStack stack, int color, int light, int overlay, PoseStack ms, VertexConsumer buffer) {
        RandomSource random = RandomSource.create();
        long i = 42L;

        for(Direction direction : Direction.values()) {
            random.setSeed(42L);
            renderBakedItemQuads(ms, buffer, color, model.getQuads((BlockState)null, direction, random), stack, light, overlay);
        }

        random.setSeed(42L);
        renderBakedItemQuads(ms, buffer, color, model.getQuads((BlockState)null, (Direction)null, random), stack, light, overlay);
    }

    private static void renderBakedItemQuads(PoseStack ms, VertexConsumer buffer, int color, List<BakedQuad> quads, ItemStack stack, int light, int overlay) {
        final float a = (float)(color >> 24 & 255) / 255.0F;
        final float r = (float)(color >> 16 & 255) / 255.0F;
        final float g = (float)(color >> 8 & 255) / 255.0F;
        final float b = (float)(color & 255) / 255.0F;
        VertexConsumer var11 = new DelegatedVertexConsumer(buffer) {
            public VertexConsumer color(float red, float green, float blue, float alpha) {
                return super.color(r, g, b, a);
            }
        };
        ((ItemRendererAccessor)Minecraft.getInstance().getItemRenderer()).callRenderQuadList(ms, var11, quads, stack, light, overlay);
    }
}
