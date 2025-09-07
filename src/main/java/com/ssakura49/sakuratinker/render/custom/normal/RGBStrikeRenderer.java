package com.ssakura49.sakuratinker.render.custom.normal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.auto.CustomRendererAttributes;
import com.ssakura49.sakuratinker.client.CustomTextureParticleRenderer;
import com.ssakura49.sakuratinker.client.InLevelRenderType;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.util.List;

@CustomRendererAttributes(name = RGBStrikeRenderer.NAME)
public class RGBStrikeRenderer extends CustomTextureParticleRenderer {
    public static final String NAME = "normal:rgb_strike";
    public static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/particle/rgb_strike_1.png"),
            ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/particle/rgb_strike_2.png"),
            ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/particle/rgb_strike_3.png"),
            ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/particle/rgb_strike_4.png")};
    private static final RandomSource randomSource = RandomSource.create(49363999L);
    public static TextureAtlasSprite[] sprites = null;
    public final float finalScale = 1F;
    public final boolean mirrored = Math.random() > 0.5F;
    public int[] colors = new int[]{
            randomSource.nextInt(255), randomSource.nextInt(255), randomSource.nextInt(255),
            randomSource.nextInt(255), randomSource.nextInt(255), randomSource.nextInt(255),
            randomSource.nextInt(255), randomSource.nextInt(255), randomSource.nextInt(255),
            randomSource.nextInt(255), randomSource.nextInt(255), randomSource.nextInt(255)};
    public Vector2f rotationXY;
    public float width;
    public float height;
    protected int tickCount;

    public RGBStrikeRenderer(float x, float y, float z, float rotationX, float rotationY, float width, float height) {
        super();
        this.updatePos(x, y, z);
        this.rotationXY = new Vector2f(rotationX, rotationY);
        this.width = width;
        this.height = height;
        this.setSpriteFromAge(8);
        if (sprites == null) {
            sprites = new TextureAtlasSprite[4];
            sprites[0] = getCurrentSpriteSet().get(0);
            sprites[1] = getCurrentSpriteSet().get(1);
            sprites[2] = getCurrentSpriteSet().get(2);
            sprites[3] = getCurrentSpriteSet().get(3);
        }
    }

    public RGBStrikeRenderer() {
    }

    @Override
    public void doLevelRenderTask(MultiBufferSource.BufferSource bufferSource, ClientLevel level, Camera camera, PoseStack stack, int packedLight, float partialTicks) {
    }

    @Override
    public void doParticleRenderTask(VertexConsumer consumer, ClientLevel level, Camera camera, PoseStack matrix, int packedLight, float partialTicks) {
        matrix.pushPose();
        matrix.scale(finalScale, finalScale, finalScale);
        this.render(this.rotationXY, width, height, matrix, consumer, mirrored);
        matrix.popPose();
    }

    @Override
    public Runnable setupRenderState() {
        return STRenderType.PRT_RGB_STRIKE[0].setupState;
    }

    @Override
    public Runnable clearRenderState() {
        return STRenderType.PRT_RGB_STRIKE[0].clearState;
    }

    @Override
    public List<ResourceLocation> allTextures() {
        return List.of(TEXTURES);
    }

    @Override
    public void enable() {
        this.addTask();
    }

    @Override
    public void disable() {
    }

    @Override
    public boolean isEnable() {
        return tickCount < 8;
    }

    /**
     * @author iron's spell book
     */
    public void render(Vector2f rotationXY, float bbWidth, float bbHeight, PoseStack poseStack, VertexConsumer consumer, boolean mirrored) {
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - rotationXY.y()));
        poseStack.mulPose(Axis.XP.rotationDegrees(-rotationXY.x()));
        this.drawSlash(pose, consumer, bbWidth * 1.5F, bbHeight, mirrored);
        poseStack.popPose();
    }

    @Override
    public double getY(float partialTicks) {
        return y;
    }

    @Override
    public double getX(float partialTicks) {
        return x;
    }

    @Override
    public double getZ(float partialTicks) {
        return z;
    }

    /**
     * @author iron's spell book
     */
    private void drawSlash(PoseStack.Pose pose, VertexConsumer consumer, float width, float bbHeight, boolean mirrored) {

        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        float halfWidth = width * 0.5F;
        float height = bbHeight * 0.5F;
        consumer.vertex(poseMatrix, -halfWidth, height, -halfWidth)
                .color(colors[0], colors[1], colors[2], 200)
                .uv(getU0(), mirrored ? getV0() : getV1())
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();
        consumer.vertex(poseMatrix, halfWidth, height, -halfWidth)
                .color(colors[3], colors[4], colors[5], 200)
                .uv(getU1(), mirrored ? getV0() : getV1())
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();
        consumer.vertex(poseMatrix, halfWidth, height, halfWidth)
                .color(colors[6], colors[7], colors[8], 200)
                .uv(getU1(), mirrored ? getV1() : getV0())
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();
        consumer.vertex(poseMatrix, -halfWidth, height, halfWidth)
                .color(colors[9], colors[10], colors[11], 200)
                .uv(getU0(), mirrored ? getV1() : getV0())
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(15728880)
                .normal(normalMatrix, 0.0F, 1.0F, 0.0F)
                .endVertex();
    }

    @Override
    public void tick(ClientLevel level, Camera camera, float partialTicks) {
        this.tickCount++;
        if (sprites == null) {
            sprites = new TextureAtlasSprite[4];
            sprites[0] = getCurrentSpriteSet().get(0);
            sprites[1] = getCurrentSpriteSet().get(1);
            sprites[2] = getCurrentSpriteSet().get(2);
            sprites[3] = getCurrentSpriteSet().get(3);
        }
        this.setSprite(sprites[(tickCount - 1) / 2]);
    }

    @Override
    public int tickCount() {
        return this.tickCount;
    }

    @Override
    public InLevelRenderType inLevelType() {
        return InLevelRenderType.TEXTURE_PARTICLE;
    }
}
