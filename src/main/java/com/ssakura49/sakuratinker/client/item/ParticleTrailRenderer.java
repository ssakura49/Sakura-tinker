package com.ssakura49.sakuratinker.client.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParticleTrailRenderer {
    public static int START = 0x60f00fff;
    public static int END = 0x00ff00ff;

    private static final List<LineParticle> PARTICLES = new ArrayList<>();

    public static void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, Vec2 center, float time, int startColor, int endColor) {
        VertexConsumer buffer = bufferSource.getBuffer(STRenderType.guiOverlayNoCull());
        spawnParticles(center, time);
        Matrix4f matrix = poseStack.last().pose();
        renderParticles(matrix, buffer, time, startColor, endColor);
    }

    private static void spawnParticles(Vec2 center, float time) {
        if (time % 3 < 0.1F && PARTICLES.size() < 30) {
            float angle = 0;
            if (Minecraft.getInstance().level != null) {
                angle = Mth.randomBetween(Minecraft.getInstance().level.random, 0, Mth.TWO_PI);
            }
            float dist = 0.25F;
            Vec2 offset = new Vec2((float)Math.cos(angle) * dist, (float)Math.sin(angle) * dist);
            Vec2 from = center.add(offset);
            Vec2 to = center;
            PARTICLES.add(new LineParticle(from, to, 0.4F));
        }
    }

    private static void renderParticles(Matrix4f matrix, VertexConsumer buffer, float time, int startColor, int endColor) {
        Iterator<LineParticle> it = PARTICLES.iterator();
        while (it.hasNext()) {
            LineParticle p = it.next();
            if (p.isDead()) {
                it.remove();
                continue;
            }
            p.update();

            int alpha = (int)(p.alpha * 255);
            int colorStart = (alpha << 24) | (startColor & 0xFFFFFF);
            int colorEnd = ((int)(p.alpha * 80) << 24) | (endColor & 0xFFFFFF);

            Vec2 start = p.getCurrent();
            Vec2 end = p.getPrevious();

            buffer.vertex(matrix, start.x, start.y, 0).color(colorStart).endVertex();
            buffer.vertex(matrix, end.x, end.y, 0).color(colorEnd).endVertex();
        }
    }

    public static class LineParticle {
        private final Vec2 from, to;
        private float life;
        public float alpha = 1.0F;
        private Vec2 last;

        public LineParticle(Vec2 from, Vec2 to, float life) {
            this.from = from;
            this.to = to;
            this.life = life;
            this.last = from;
        }

        public void update() {
            last = getCurrent();
            life -= 0.02F;
            alpha = life * 2.5F;
        }

        public boolean isDead() {
            return life <= 0;
        }

        public Vec2 getCurrent() {
            return lerp(from, to, 1.0F - life);
        }

        public Vec2 getPrevious() {
            return last;
        }

        private Vec2 lerp(Vec2 a, Vec2 b, float t) {
            return new Vec2(Mth.lerp(t, a.x, b.x), Mth.lerp(t, a.y, b.y));
        }
    }
}
