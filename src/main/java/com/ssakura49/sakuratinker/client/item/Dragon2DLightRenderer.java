package com.ssakura49.sakuratinker.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.library.client.Easing;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;

public class Dragon2DLightRenderer {
    public static int START = 0x60f00fff;
    public static int END = 0x00ff00ff;

    public static void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec2 pos, float size, float time, int startC, int endC) {
        VertexConsumer buffer = bufferSource.getBuffer(STRenderType.guiOverlayNoCull());
        poseStack.pushPose();
        float alpha = Math.max(0F, time * 0.03F - .2F);
        float scale = alpha <= 1.0F ? Easing.OUT_ELASTIC.calculate(alpha) : 1F;
        startC = (int) ((startC >>> 24) * scale) << 24 | startC >> 16 & 255 << 16 | startC >> 8 & 255 << 8 | startC & 255;
        poseStack.scale(scale, scale, scale);
        for (float i = 0F; i < 1F; i += 0.125F) {
            float r = (i - time * 0.1F * 0.083333F) * Mth.TWO_PI;
            poseStack.mulPose(Axis.ZP.rotation(r));
            Matrix4f matrix4f = poseStack.last().pose();
            buffer.vertex(matrix4f, pos.x, pos.y, -0).color(0x50FFFFFF).endVertex();
            buffer.vertex(matrix4f, pos.x, pos.y + 0.01F, -0).color(0x50FFFFFF).endVertex();
            buffer.vertex(matrix4f, pos.x + 0.0625F * 4F * size, pos.y + 0.01F + 0.0625F * size, -0).color(0).endVertex();
            buffer.vertex(matrix4f, pos.x + 0.0625F * 4F * size, pos.y - 0.0625F * size, -0).color(0).endVertex();
            buffer.vertex(matrix4f, pos.x, pos.y, -0).color(startC).endVertex();
            buffer.vertex(matrix4f, pos.x, pos.y + 0.01F, -0).color(startC).endVertex();
            buffer.vertex(matrix4f, pos.x + 0.0625F * 4F * size, pos.y + 0.01F + 0.0625F * size, -0).color(endC).endVertex();
            buffer.vertex(matrix4f, pos.x + 0.0625F * 4F * size, pos.y - 0.0625F * size, -0).color(endC).endVertex();
            poseStack.mulPose(Axis.ZP.rotation(-r));
        }
        poseStack.popPose();
    }
}
