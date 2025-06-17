package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.model.TerraPrismaModel;
import com.ssakura49.sakuratinker.content.entity.terraprisma.TerraPrismEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class TerraPrismaRenderer extends EntityRenderer<TerraPrismEntity> {
    private final TerraPrismaModel<TerraPrismEntity> model;
    private final ResourceLocation TEXTURE = new ResourceLocation(SakuraTinker.MODID, "textures/model/terra_prisma.png");

    public TerraPrismaRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new TerraPrismaModel<>(context.bakeLayer(TerraPrismaModel.LAYER_LOCATION));
    }

    @Override
    public void render(TerraPrismEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();

        // 平滑跟踪位置
        Vec3 smoothedPos = entity.getSmoothedPosition(partialTicks);
        poseStack.translate(
                smoothedPos.x - entity.getX(),
                smoothedPos.y - entity.getY(),
                smoothedPos.z - entity.getZ()
        );

        // 轻微抬高飞行位置
        poseStack.translate(0, 0.4, 0);

        // 朝向移动方向旋转
        Vec3 motion = entity.getDeltaMovement();
        if (motion.lengthSqr() > 0.001) {
            Vec3 norm = motion.normalize();
            float yaw = (float)Math.atan2(norm.x, norm.z);
            float pitch = (float)Math.asin(norm.y);
            poseStack.mulPose(Axis.YP.rotation(yaw));
            poseStack.mulPose(Axis.XP.rotation(-pitch));
        }

        // 动画状态驱动变换
        switch (entity.getAnimationState()) {
            case "charge" -> {
                float progress = entity.getAnimationProgress(partialTicks);
                // 冲刺
                poseStack.scale(1.4f, 0.7f, 1.4f);
                poseStack.mulPose(Axis.YP.rotation(progress * Mth.TWO_PI * 2));
            }
            case "slash" -> {
                float progress = entity.getAnimationProgress(partialTicks);
                // 挥砍椭圆轨迹
                poseStack.mulPose(Axis.ZP.rotation(Mth.sin(progress * Mth.PI) * 0.8f));
                poseStack.scale(1.2f, 1.2f, 1.2f);
            }
            default -> {
                // 待机状态，上下浮动
                float idleFloat = Mth.sin((entity.tickCount + partialTicks) * 0.15f) * 0.2f;
                float idleRotate = (entity.tickCount + partialTicks) * 0.4f;
                poseStack.translate(0, idleFloat, 0);
                poseStack.mulPose(Axis.YP.rotationDegrees(idleRotate));
            }
        }

        // 渲染模型
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY,
                1.0f, 1.0f, 1.0f, 1.0f);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(TerraPrismEntity entity) {
        return TEXTURE;
    }
}
