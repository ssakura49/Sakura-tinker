package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.entity.GhostKnife;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class GhostKnifeRenderer extends EntityRenderer<GhostKnife> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(SakuraTinker.MODID, "textures/entity/ghost_knife.png");

    // 模型尺寸常量
    private static final float BLADE_WIDTH = 0.5f;
    private static final float BLADE_HEIGHT = 0.1f;
    private static final float BLADE_LENGTH = 1.2f;
    private static final float BLADE_THICKNESS = 0.05f;

    public GhostKnifeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(GhostKnife entity, Frustum camera, double camX, double camY, double camZ) {
        // 简化的距离检查 (64格内渲染)
        Vec3 cameraPos = new Vec3(camX, camY, camZ);
        return entity.position().distanceToSqr(cameraPos) < 64 * 64;
    }

    @Override
    public void render(GhostKnife entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.tickCount < 2) return; // 前2tick不渲染

        poseStack.pushPose();

        // 计算动态参数
        float age = entity.tickCount + partialTicks;
        float scale = entity.getScale();
        float alpha = calculateAlpha(age, entity.getLifetime());

        // 设置变换
        setupTransforms(poseStack, entity, age, scale);

        // 获取渲染矩阵
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();

        // 渲染飞刀模型
        renderModel(poseMatrix, normalMatrix, buffer, scale, alpha, packedLight);

        poseStack.popPose();
    }

    // 计算透明度 (最后30%生命周期淡出)
    private float calculateAlpha(float currentAge, float totalLifetime) {
        // 前10%生命周期淡入
        float fadeIn = Mth.clamp(currentAge / (totalLifetime * 0.1f), 0f, 1f);
        // 后30%生命周期淡出
        float fadeOut = 1.0f - Mth.clamp((currentAge - totalLifetime * 0.7f) / (totalLifetime * 0.3f), 0f, 1f);
        return fadeIn * fadeOut;
    }

    // 设置位置、旋转和缩放
    private void setupTransforms(PoseStack poseStack, GhostKnife entity, float age, float scale) {
        // 1. 调整位置（稍微抬高）
        poseStack.translate(0, 0.1, 0);

        // 2. 让飞刀朝向运动方向（Yaw和Pitch旋转）
        Vec3 movement = entity.getDeltaMovement().normalize();
        float yaw = (float) Math.toDegrees(Math.atan2(movement.x, movement.z));
        float pitch = (float) Math.toDegrees(Math.asin(movement.y));
//        poseStack.mulPose(Axis.YP.rotationDegrees(-yaw)); // 偏航
//        poseStack.mulPose(Axis.XP.rotationDegrees(pitch)); // 俯仰

        // 3. 绕Z轴旋转（与视角平行的自转）
        float spin = age * 30.0f; // 旋转速度（每秒720度）
        poseStack.mulPose(Axis.ZN.rotationDegrees(spin));

        // 4. 脉动效果
        float pulse = Mth.sin(age * 0.3f) * 0.05f + 0.95f;
        poseStack.scale(scale * pulse, scale * pulse, scale * pulse);
    }

    // 主渲染方法
    private void renderModel(Matrix4f poseMatrix, Matrix3f normalMatrix,
                             MultiBufferSource buffer, float scale,
                             float alpha, int packedLight) {
        // 计算实际尺寸
        float width = BLADE_WIDTH * scale;
        float height = BLADE_HEIGHT * scale;
        float length = BLADE_LENGTH * scale;
        float thickness = BLADE_THICKNESS * scale;

        // 主刀身 (半透明)
        VertexConsumer mainConsumer = buffer.getBuffer(RenderType.entityTranslucentCull(TEXTURE));
        renderBlade(poseMatrix, normalMatrix, mainConsumer,
                width, height, length, thickness,
                0.4f, 0.6f, 1.0f, alpha * 0.8f, packedLight);

        // 发光轮廓 (全亮度)
        VertexConsumer glowConsumer = buffer.getBuffer(RenderType.eyes(TEXTURE));
        renderBlade(poseMatrix, normalMatrix, glowConsumer,
                width * 1.1f, height * 1.1f, length * 1.05f, thickness * 1.2f,
                0.7f, 0.9f, 1.0f, alpha * 0.5f, 15728640);
    }

    // 渲染完整飞刀模型 (6个面+刀尖)
    private void renderBlade(Matrix4f poseMatrix, Matrix3f normalMatrix,
                             VertexConsumer consumer,
                             float width, float height, float length, float thickness,
                             float r, float g, float b, float a, int light) {
        // 前侧面 (Z=0)
        renderQuad(poseMatrix, normalMatrix, consumer,
                -width, -height, 0,          // 左下前
                -width, height, 0,           // 左上前
                width, height, 0,            // 右上前
                width, -height, 0,           // 右下前
                0, 0, -1,                   // 法线 (朝前)
                r, g, b, a, light);

        // 后侧面 (Z=-thickness)
        renderQuad(poseMatrix, normalMatrix, consumer,
                -width, -height, -thickness, // 左下后
                width, -height, -thickness,  // 右下后
                width, height, -thickness,   // 右上后
                -width, height, -thickness,  // 左上后
                0, 0, 1,                    // 法线 (朝后)
                r, g, b, a, light);

        // 上侧面 (Y=height)
        renderQuad(poseMatrix, normalMatrix, consumer,
                -width, height, 0,          // 左上前
                -width, height, -thickness,  // 左上后
                width, height, -thickness,   // 右上后
                width, height, 0,           // 右上前
                0, 1, 0,                    // 法线 (朝上)
                r, g, b, a, light);

        // 下侧面 (Y=-height)
        renderQuad(poseMatrix, normalMatrix, consumer,
                -width, -height, 0,         // 左下前
                width, -height, 0,          // 右下前
                width, -height, -thickness, // 右下后
                -width, -height, -thickness,// 左下后
                0, -1, 0,                   // 法线 (朝下)
                r, g, b, a, light);

        // 左侧面 (X=-width)
        renderQuad(poseMatrix, normalMatrix, consumer,
                -width, -height, 0,         // 左下前
                -width, -height, -thickness, // 左下后
                -width, height, -thickness,  // 左上后
                -width, height, 0,           // 左上前
                -1, 0, 0,                   // 法线 (朝左)
                r, g, b, a, light);

        // 右侧面 (X=width)
        renderQuad(poseMatrix, normalMatrix, consumer,
                width, -height, 0,          // 右下前
                width, height, 0,           // 右上前
                width, height, -thickness,  // 右上后
                width, -height, -thickness, // 右下后
                1, 0, 0,                    // 法线 (朝右)
                r, g, b, a, light);

        // 刀尖部分 (锥形延伸)
        float tipWidth = width * 0.6f;
        float tipHeight = height * 0.6f;
        renderQuad(poseMatrix, normalMatrix, consumer,
                -tipWidth, -tipHeight, -length,  // 刀尖左下
                -tipWidth, tipHeight, -length,    // 刀尖左上
                tipWidth, tipHeight, -length,     // 刀尖右上
                tipWidth, -tipHeight, -length,    // 刀尖右下
                0, 0, -1,                        // 法线 (朝前)
                r, g, b, a, light);
    }

    // 渲染单个四边形面
    private void renderQuad(Matrix4f poseMatrix, Matrix3f normalMatrix,
                            VertexConsumer consumer,
                            float x1, float y1, float z1,  // 顶点1
                            float x2, float y2, float z2,  // 顶点2
                            float x3, float y3, float z3,  // 顶点3
                            float x4, float y4, float z4,  // 顶点4
                            float nx, float ny, float nz,  // 法线
                            float r, float g, float b, float a, int light) {
        // 顶点1
        consumer.vertex(poseMatrix, x1, y1, z1)
                .color(r, g, b, a)
                .uv(calculateU(x1, z1), calculateV(y1, z1))
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();

        // 顶点2
        consumer.vertex(poseMatrix, x2, y2, z2)
                .color(r, g, b, a)
                .uv(calculateU(x2, z2), calculateV(y2, z2))
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();

        // 顶点3
        consumer.vertex(poseMatrix, x3, y3, z3)
                .color(r, g, b, a)
                .uv(calculateU(x3, z3), calculateV(y3, z3))
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();

        // 顶点4
        consumer.vertex(poseMatrix, x4, y4, z4)
                .color(r, g, b, a)
                .uv(calculateU(x4, z4), calculateV(y4, z4))
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normalMatrix, nx, ny, nz)
                .endVertex();
    }

    // UV坐标计算 (简单投影映射)
    private float calculateU(float x, float z) {
        return (x / BLADE_WIDTH + 1.0f) * 0.5f; // 标准化到[0,1]
    }

    private float calculateV(float y, float z) {
        return (y / BLADE_HEIGHT + 1.0f) * 0.5f; // 标准化到[0,1]
    }

    @Override
    protected int getBlockLightLevel(GhostKnife entity, BlockPos pos) {
        return 15; // 最大亮度
    }

    @Override
    protected int getSkyLightLevel(GhostKnife entity, BlockPos pos) {
        return 15; // 最大亮度
    }

    @Override
    public ResourceLocation getTextureLocation(GhostKnife entity) {
        return TEXTURE;
    }
}
