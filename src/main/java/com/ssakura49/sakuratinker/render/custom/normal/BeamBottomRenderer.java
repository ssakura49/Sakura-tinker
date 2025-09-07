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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector4f;

import java.util.List;

@CustomRendererAttributes(name = BeamBottomRenderer.NAME)
public class BeamBottomRenderer extends CustomTextureParticleRenderer {
    public static final String NAME = "normal:beam_base";
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/particle/beam_base.png");
    private static final RandomSource randomSource = RandomSource.create(49363999L);
    static Minecraft mc = Minecraft.getInstance();
    public final float finalScale = 1.5F;
    public final float rotationSpeed = 0.6F + randomSource.nextFloat() * 2F;
    public final float growingSize = 0.1F;
    public final float dyingSize = 0.8F;
    public float xR;
    public float yR;
    public float zR = randomSource.nextFloat() * 360F;
    public int MAX_LIFE = 100 / 5;
    protected int tickCount = 0;

    public BeamBottomRenderer(double x, double y, double z, float xRot, float yRot) {
        super();
        this.x = x;
        this.y = y;
        this.z = z;
        xR = xRot;
        yR = yRot;
        this.updatePos(x, y, z);
    }

    public BeamBottomRenderer() {
    }

    @Override
    public void doParticleRenderTask(VertexConsumer consumer, ClientLevel level, Camera camera, PoseStack matrix, int packedLight, float partialTicks) {
        matrix.pushPose();
        matrix.scale(finalScale, finalScale, finalScale);
        this.render(camera, matrix, partialTicks, consumer);
        matrix.popPose();
    }

    @Override
    public Runnable setupRenderState() {
        return STRenderType.PRT_BEAM_BOTTOM.setupState;
    }

    @Override
    public Runnable clearRenderState() {
        return STRenderType.PRT_BEAM_BOTTOM.clearState;
    }

    @Override
    public List<ResourceLocation> allTextures() {
        return List.of(TEXTURE);
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
    }

    @Override
    public int tickCount() {
        return tickCount;
    }

    @Override
    public boolean isEnable() {
        return tickCount() < MAX_LIFE;
    }

    public void render(Camera camera, PoseStack poseStack, float partialTicks, VertexConsumer consumer) {
        float percent = percent(partialTicks, MAX_LIFE);
        float scale = percent > dyingSize ? 1F - (percent - dyingSize) / (1F - dyingSize) : (percent < growingSize ? percent / growingSize : 1F);
        poseStack.pushPose();
        poseStack.scale(scale * finalScale, scale * finalScale, scale * finalScale);
        Quaternionf quaternionf = new Quaternionf().rotationYXZ(-yR * (Mth.PI / 180F), xR * (Mth.PI / 180F), (zR + partial(partialTicks)) * (Mth.PI / 180F));
        //Quaternionf quaternionf = new Quaternionf().rotationXYZ(-camera.getXRot()* ((float) Math.PI / 180F), 180-camera.getYRot()* ((float)Math.PI / 180F), (this.rotY + this.rotationSpeed * tickCount)* ((float)Math.PI / 180F));
        poseStack.mulPose(quaternionf);
        poseStack.scale(2F, 2F, 2F);
        this.drawParticle(poseStack, consumer);
        poseStack.popPose();
    }

    private void drawParticle(PoseStack stack, VertexConsumer consumer) {
        Matrix4f m = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        Vector4f vector4f = new Vector4f(1F);
        RendererUtils.vertex(consumer, m, matrix3f, 0xF000F0, 0.0F, 0, getU0(), getV1(), vector4f);
        RendererUtils.vertex(consumer, m, matrix3f, 0xF000F0, 1.0F, 0, getU1(), getV1(), vector4f);
        RendererUtils.vertex(consumer, m, matrix3f, 0xF000F0, 1.0F, 1, getU1(), getV0(), vector4f);
        RendererUtils.vertex(consumer, m, matrix3f, 0xF000F0, 0.0F, 1, getU0(), getV0(), vector4f);
    }

    @Override
    public InLevelRenderType inLevelType() {
        return InLevelRenderType.TEXTURE_PARTICLE;
    }
}
