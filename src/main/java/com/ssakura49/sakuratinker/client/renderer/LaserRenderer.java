package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.entity.LaserProjectileEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class LaserRenderer extends EntityRenderer<LaserProjectileEntity> {
    private static final ResourceLocation LASER_TEXTURE = new ResourceLocation(SakuraTinker.MODID, "textures/entity/laser.png");
    private static final RenderType LASER_RENDER_TYPE = RenderType.entityTranslucent(LASER_TEXTURE);

    //侧边纹理
    private static final float SIDE_U0 = 0f / 64f;
    private static final float SIDE_V0 = 0f / 64f;
    private static final float SIDE_U1 = 22f / 64f;
    private static final float SIDE_V1 = 2f / 64f;

    //顶面/底面纹理
    private static final float TOP_BOTTOM_U0 = 0f / 64f;
    private static final float TOP_BOTTOM_V0 = 4f / 64f;
    private static final float TOP_BOTTOM_U1 = 2f / 64f;
    private static final float TOP_BOTTOM_V1 = 6f / 64f;

    //尺寸
    private static final float LASER_LENGTH = 22f;
    private static final float LASER_WIDTH = 2f;
    private static final float SCALE_FACTOR = 1f / 32f;
    private static final float OFFSET_DISTANCE = 0.5f;

    public LaserRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull LaserProjectileEntity entity) {
        return LASER_TEXTURE;
    }

    private void addVertex(Matrix4f pose, Matrix3f normal, VertexConsumer builder,
                           float x, float y, float z, float u, float v, float alpha) {
        builder.vertex(pose, x, y, z)
                .color(1, 0.2f, 0.8f, alpha)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(normal, 0, 1, 0)
                .endVertex();
    }

    @Override
    public void render(LaserProjectileEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.hasHit()) return;
        float lerpProgress = (entity.aliveTicks + partialTicks) / entity.lifeTime;
        float length = LASER_LENGTH * SCALE_FACTOR;
        float alpha = 0.8f * (1 - lerpProgress);
        float width = LASER_WIDTH * SCALE_FACTOR;

        poseStack.pushPose();
        Vec3 motion = entity.getDeltaMovement().normalize();
        poseStack.translate(
                motion.x * OFFSET_DISTANCE,
                motion.y * OFFSET_DISTANCE,
                motion.z * OFFSET_DISTANCE
        );
        float pitch = (float) Math.asin(-motion.y);
        float yawRad = (float) Math.atan2(motion.x, motion.z);
        poseStack.mulPose(Axis.YP.rotation(yawRad));
        poseStack.mulPose(Axis.XP.rotation(pitch));

        VertexConsumer vertexBuilder = buffer.getBuffer(LASER_RENDER_TYPE);
        Matrix4f poseMatrix = poseStack.last().pose();
        Matrix3f normalMatrix = poseStack.last().normal();

        for (int i = 0; i < 4; i++) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.YP.rotation((float) (Math.PI / 2 * i + Math.PI))); // 添加180度旋转修复方向

            //前面矩形（逆时针）
            addVertex(poseMatrix, normalMatrix, vertexBuilder, -width/2, -width/2, 0, SIDE_U0, SIDE_V1, alpha);
            addVertex(poseMatrix, normalMatrix, vertexBuilder, -width/2, width/2, 0, SIDE_U0, SIDE_V0, alpha);
            addVertex(poseMatrix, normalMatrix, vertexBuilder, width/2, width/2, 0, SIDE_U1, SIDE_V0, alpha);
            addVertex(poseMatrix, normalMatrix, vertexBuilder, width/2, -width/2, 0, SIDE_U1, SIDE_V1, alpha);

            //侧面
            addVertex(poseMatrix, normalMatrix, vertexBuilder, width/2, -width/2, 0, SIDE_U0, SIDE_V1, alpha);
            addVertex(poseMatrix, normalMatrix, vertexBuilder, width/2, width/2, 0, SIDE_U0, SIDE_V0, alpha);
            addVertex(poseMatrix, normalMatrix, vertexBuilder, width/2, width/2, length, SIDE_U1, SIDE_V0, alpha);
            addVertex(poseMatrix, normalMatrix, vertexBuilder, width/2, -width/2, length, SIDE_U1, SIDE_V1, alpha);

            poseStack.popPose();
        }

        addVertex(poseMatrix, normalMatrix, vertexBuilder, -width/2, -width/2, 0, TOP_BOTTOM_U0, TOP_BOTTOM_V1, alpha);
        addVertex(poseMatrix, normalMatrix, vertexBuilder, -width/2, -width/2, length, TOP_BOTTOM_U1, TOP_BOTTOM_V1, alpha);
        addVertex(poseMatrix, normalMatrix, vertexBuilder, width/2, -width/2, length, TOP_BOTTOM_U1, TOP_BOTTOM_V0, alpha);
        addVertex(poseMatrix, normalMatrix, vertexBuilder, width/2, -width/2, 0, TOP_BOTTOM_U0, TOP_BOTTOM_V0, alpha);

        addVertex(poseMatrix, normalMatrix, vertexBuilder, -width/2, width/2, 0, TOP_BOTTOM_U0, TOP_BOTTOM_V0, alpha);
        addVertex(poseMatrix, normalMatrix, vertexBuilder, width/2, width/2, 0, TOP_BOTTOM_U1, TOP_BOTTOM_V0, alpha);
        addVertex(poseMatrix, normalMatrix, vertexBuilder, width/2, width/2, length, TOP_BOTTOM_U1, TOP_BOTTOM_V1, alpha);
        addVertex(poseMatrix, normalMatrix, vertexBuilder, -width/2, width/2, length, TOP_BOTTOM_U0, TOP_BOTTOM_V1, alpha);

        poseStack.popPose();
    }
}
