package com.ssakura49.sakuratinker.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;

public class PulseGlowRenderer {
    public static int START = 0x60f00fff;
    public static int END = 0x00ff00ff;
    public static void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec2 center, float size, float time, int startColor, int endColor) {
        VertexConsumer buffer = bufferSource.getBuffer(STRenderType.guiOverlayNoCull());
        poseStack.pushPose();

        float pulse = 0.5F + 0.5F * Mth.sin(time * 0.25F);
        float scale = 1.0F + 0.2F * pulse;
        float alpha = pulse;
        poseStack.scale(scale, scale, scale);

        Matrix4f matrix = poseStack.last().pose();
        int startC = ((int)(alpha * 255) << 24) | (startColor & 0xFFFFFF);
        int endC = ((int)(alpha * 64) << 24) | (endColor & 0xFFFFFF);

        for (int i = 0; i < 8; i++) {
            float angle = i * Mth.TWO_PI / 8;
            poseStack.mulPose(Axis.ZP.rotation(angle));

            float width = 0.0625F * size * 2F;
            float height = 0.0625F * size;

            buffer.vertex(matrix, center.x, center.y, 0).color(startC).endVertex();
            buffer.vertex(matrix, center.x + width, center.y + height, 0).color(endC).endVertex();
            buffer.vertex(matrix, center.x + width, center.y - height, 0).color(endC).endVertex();

            poseStack.mulPose(Axis.ZP.rotation(-angle));
        }

        poseStack.popPose();
    }
}
