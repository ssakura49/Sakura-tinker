package com.ssakura49.sakuratinker.render.custom.normal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.auto.CustomRendererAttributes;
import com.ssakura49.sakuratinker.client.CustomTextureParticleRenderer;
import com.ssakura49.sakuratinker.client.InLevelRenderType;
import com.ssakura49.sakuratinker.render.RendererUtils;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

import java.util.List;

@CustomRendererAttributes(name = AwayParticleRenderer.NAME)
public class AwayParticleRenderer extends CustomTextureParticleRenderer {
    public static final String NAME = "normal:away1_2";
    public static final ResourceLocation[] TEXTURES = new ResourceLocation[]{
            ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/particle/away.png"),
            ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/particle/away2.png")};
    private static final RandomSource randomSource = RandomSource.create(49363999L);
    static Minecraft mc = Minecraft.getInstance();
    public final float finalScale = 0.3F + randomSource.nextFloat() / 2F;
    public final float rotationSpeed = 0.6F + randomSource.nextFloat() * 2F;
    public final float growingSize = 0.1F;
    public final float dyingSize = 0.7F;
    public float mx;
    public float my;
    public float mz;
    public float mxOld;
    public float myOld;
    public float mzOld;
    public int MAX_LIFE = 250 / 5;
    public boolean mirrored = false;
    protected float rotYOld;
    protected int tickCount = 0;
    protected int mirroredI = 0;
    private float rotY = randomSource.nextFloat() * 360F;

    public AwayParticleRenderer(double x, double y, double z, boolean mirrored) {
        this(x, y, z, 0, 0, 0, mirrored);
    }

    public AwayParticleRenderer(double x, double y, double z, float mx, float my, float mz, boolean mirrored) {
        super();
        this.xOld = this.x = x;
        this.yOld = this.y = y;
        this.zOld = this.z = z;
        this.mx = mx;
        this.my = my;
        this.mz = mz;
        this.mirrored = mirrored;
        this.mirroredI = this.mirrored ? 1 : -1;
        this.setSprite(getCurrentSpriteSet().get((mirroredI + 1) / 2));
    }

    public AwayParticleRenderer() {
    }

    @Override
    public void doLevelRenderTask(MultiBufferSource.BufferSource bufferSource, ClientLevel level, Camera camera, PoseStack stack, int packedLight, float partialTicks) {
    }

    @Override
    public void enable() {
        this.addTask();
    }

    @Override
    public void disable() {
    }

    @Override
    public void tick(ClientLevel level, Camera camera, float partialTicks) {
        tickCount++;
        mxOld = mx;
        myOld = my;
        mzOld = mz;
        mx -= mx * 0.1F;
        my -= my * 0.1F;
        mz -= mz * 0.1F;
        this.updatePos(
                this.x + mx,
                this.y + my,
                this.z + mz);
        this.rotYOld = this.rotY;
        this.rotY += this.rotationSpeed * mirroredI;
    }

    @Override
    public int tickCount() {
        return tickCount;
    }

    @Override
    public boolean isEnable() {
        return tickCount() < MAX_LIFE;
    }

    public void render(Camera camera, PoseStack poseStack, VertexConsumer consumer, int packedLight, boolean mirrored, float partialTicks) {
        float percent = percent(partialTicks, MAX_LIFE);
        float scale = percent > dyingSize ? 1F - (percent - dyingSize) / (1F - dyingSize) : (percent < growingSize ? percent / growingSize : 1F);
        poseStack.scale(scale, scale, scale);
        //Quaternionf quaternionf = new Quaternionf().rotationXYZ(-camera.getXRot()* ((float) Math.PI / 180F), 180-camera.getYRot()* ((float)Math.PI / 180F), (this.rotY + this.rotationSpeed * tickCount)* ((float)Math.PI / 180F));
        Quaternionf quaternionf = new Quaternionf(camera.rotation());
        quaternionf.rotateZ(Mth.lerp(partialTicks, this.rotYOld, this.rotY) * Mth.DEG_TO_RAD);
        poseStack.mulPose(quaternionf);
        this.drawParticle(poseStack, consumer, packedLight);
    }

    private void drawParticle(PoseStack stack, VertexConsumer consumer, int packedLight) {
        Matrix4f m = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        Vector4f vector4f = new Vector4f(1F, 1f, 1f, 0.5F);
        RendererUtils.vertex(consumer, m, matrix3f, packedLight, 0.0F, 0, getU0(), getV1(), vector4f);
        RendererUtils.vertex(consumer, m, matrix3f, packedLight, 1.0F, 0, getU1(), getV1(), vector4f);
        RendererUtils.vertex(consumer, m, matrix3f, packedLight, 1.0F, 1, getU1(), getV0(), vector4f);
        RendererUtils.vertex(consumer, m, matrix3f, packedLight, 0.0F, 1, getU0(), getV0(), vector4f);
    }

    @Override
    public InLevelRenderType inLevelType() {
        return InLevelRenderType.TEXTURE_PARTICLE;
    }

    @Override
    public void doParticleRenderTask(VertexConsumer consumer, ClientLevel level, Camera camera, PoseStack matrix, int packedLight, float partialTicks) {
        matrix.pushPose();
        matrix.scale(finalScale, finalScale, finalScale);
        this.render(camera, matrix, consumer, packedLight, mirrored, partialTicks);
        matrix.popPose();
    }

    @Override
    public Runnable setupRenderState() {
        return renderType().setupState;
    }

    @Override
    public Runnable clearRenderState() {
        return renderType().clearState;
    }

    @Override
    public List<ResourceLocation> allTextures() {
        return List.of(TEXTURES);
    }

    private RenderType renderType() {
        return STRenderType.PRT_AWAY_PARTICLE[(mirroredI + 1) / 2];
    }
}