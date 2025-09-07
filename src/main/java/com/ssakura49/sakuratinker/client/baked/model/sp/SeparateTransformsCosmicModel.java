package com.ssakura49.sakuratinker.client.baked.model.sp;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.client.baked.model.PerspectiveModelState;
import com.ssakura49.sakuratinker.render.STRenderBuffers;
import com.ssakura49.sakuratinker.render.shader.cosmic.VanillaCosmicShaders;
import com.ssakura49.sakuratinker.utils.render.TransformUtils;
import io.redspace.ironsspellbooks.item.SpellBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;

import java.util.function.Function;

public class SeparateTransformsCosmicModel extends STWrappedModel {
    public final boolean isCosmic;
    public SeparateTransformsCosmicModel(BakedModel baseModel, ImmutableMap<ItemDisplayContext, BakedModel> perspectives, boolean isCosmic) {
        super(baseModel, perspectives);
        this.isCosmic = isCosmic;
    }

    public void renderItem(ItemStack stack, ItemDisplayContext transformType, PoseStack pStack, MultiBufferSource source, int light, int overlay) {
        if (mainParentState == null) mainParentState = TransformUtils.DEFAULT_BLOCK;
        if (stack.getItem() instanceof SpellBook) mainParentState = TransformUtils.SPELL_BOOK;
        source = STRenderBuffers.getBufferSource();
        this.renderWrapped(stack, pStack, source, light, overlay, true, Function.identity());
        if (STConfig.Client.enable_cosmic_renderer && isCosmic) {
            if (source instanceof MultiBufferSource.BufferSource bs)
                bs.endBatch();
            this.renderWrappedCosmic(stack, pStack, source, light, overlay, true, Function.identity());
            Minecraft mc = Minecraft.getInstance();
            VanillaCosmicShaders.updateShaderData(transformType);
            VertexConsumer cons = source.getBuffer(VanillaCosmicShaders.COSMIC_RENDER_TYPE);
            mc.getItemRenderer().renderQuadList(pStack, cons, wrapped.getQuads(null, null, RandomSource.create(42L)), stack, light, overlay);
            STRenderBuffers.getBufferSource().endBatch();
        }
    }

    public @Nullable PerspectiveModelState getModelState() {
        return (PerspectiveModelState) this.mainParentState;
    }

    @Override
    public boolean isCosmic() {
        return true;
    }

    @Override
    public @NotNull BakedModel applyTransform(@NotNull ItemDisplayContext cameraTransformType, @NotNull PoseStack pStack, boolean applyLeftHandTransform) {
        if (perspectives.containsKey(cameraTransformType)) {
            BakedModel p = perspectives.get(cameraTransformType);
            return p.applyTransform(cameraTransformType, pStack, applyLeftHandTransform);
        } else {
            PerspectiveModelState modelState = this.getModelState();
            if (modelState != null) {
                Transformation transform = this.getModelState().getTransform(cameraTransformType);
                Vector3f trans = transform.getTranslation();
                Vector3f scale = transform.getScale();
                pStack.translate(trans.x(), trans.y(), trans.z());
                pStack.mulPose(transform.getLeftRotation());
                pStack.scale(scale.x(), scale.y(), scale.z());
                pStack.mulPose(transform.getRightRotation());
                if (applyLeftHandTransform) {
                    pStack.mulPose(Axis.YN.rotationDegrees(180.0F));
                }
                return this;
            } else {
                this.getTransforms().getTransform(cameraTransformType).apply(applyLeftHandTransform, pStack);
                return this;
            }
        }
    }

    @Override
    public boolean isCustomRenderer() {
        return true;
    }
}
