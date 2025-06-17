package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.content.entity.TinkerArrowEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class TinkerArrowEntityRenderer extends ArrowRenderer<TinkerArrowEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/projectiles/arrow.png");

    public TinkerArrowEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public @NotNull ResourceLocation getTextureLocation(TinkerArrowEntity arrow) {
        return TEXTURE;
    }
    @Override
    public void render(TinkerArrowEntity arrow, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, arrow.yRotO, arrow.getYRot()) - 90.0F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, arrow.xRotO, arrow.getXRot())));
        poseStack.scale(0.05625F, 0.05625F, 0.05625F);
        poseStack.translate(-4.0F, 0.0F, 0.0F);
        poseStack.popPose();
        super.render(arrow, yaw, partialTicks, poseStack, bufferSource, packedLight);
    }
}
