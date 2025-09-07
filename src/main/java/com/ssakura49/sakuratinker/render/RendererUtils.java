package com.ssakura49.sakuratinker.render;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.mojang.math.MatrixUtil;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.component.STFont;
import com.ssakura49.sakuratinker.event.event.client.ClientModHandler;
import com.ssakura49.sakuratinker.library.client.Easing;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import com.ssakura49.sakuratinker.utils.helper.TimeHelper;
import com.ssakura49.sakuratinker.utils.java.InstrumentationHelper;
import com.ssakura49.sakuratinker.utils.math.MathUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HalfTransparentBlock;
import net.minecraft.world.level.block.StainedGlassPaneBlock;
import net.minecraft.world.level.material.FogType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderNameTagEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.joml.*;

import javax.annotation.Nullable;
import java.awt.*;
import java.lang.Math;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;

public class RendererUtils {
    public static final int MAX_RUNE_COUNT = 10;
    public static final ModelResourceLocation TRIDENT_MODEL = ModelResourceLocation.vanilla("trident", "inventory");
    public static final ModelResourceLocation TRIDENT_IN_HAND_MODEL = ModelResourceLocation.vanilla("trident_in_hand", "inventory");
    public static final ModelResourceLocation SPYGLASS_MODEL = ModelResourceLocation.vanilla("spyglass", "inventory");
    public static final ModelResourceLocation SPYGLASS_IN_HAND_MODEL = ModelResourceLocation.vanilla("spyglass_in_hand", "inventory");
    public static final ResourceLocation MOD_ICONS = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/gui/icons.png");
    public static final ResourceLocation star2 = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/shader/star2.png");
    public static final ResourceLocation cosmic = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/shader/cosmictexture.png");
    public static final ResourceLocation star1 = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/shader/star1.png");
    public static final ResourceLocation star3 = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/shader/star3.png");
    public static final ResourceLocation beam = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID,"textures/particle/white.png");
    public static final ResourceLocation runes = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID,"textures/particle/runes.png");
    public static final ResourceLocation wave = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID,"textures/particle/shockwave.png");
    public static boolean isTimeStop_andSameDimension = false;
    public static TimeHelper h = new TimeHelper(0D, 10000D).setRunning(true);
    public static ItemRenderer getItemRenderer() {
        return Minecraft.getInstance().getItemRenderer();
    }
    static Map<ParticleRenderType, Queue<Particle>> ps = null;
    private static float HALF_SQRT_3 = 0.1f;

    static {
    }

    public static float hTime() {
        return RendererUtils.h.integer_time / 10000F;
    }

    public static MethodHandles.Lookup IMPL_LOOKUP() throws NoSuchFieldException, IllegalAccessException {
        return InstrumentationHelper.IMPL_LOOKUP();
    }

    public static void renderWave(PoseStack stack, VertexConsumer consumer, Vector4f rgba) {
        stack.scale(64, 64, 64);
        Matrix4f m = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        vertex(consumer, m, matrix3f, 0xF000F0, 0.0F, 0, 0, 1, rgba);
        vertex(consumer, m, matrix3f, 0xF000F0, 1.0F, 0, 1, 1, rgba);
        vertex(consumer, m, matrix3f, 0xF000F0, 1.0F, 1, 1, 0, rgba);
        vertex(consumer, m, matrix3f, 0xF000F0, 0.0F, 1, 0, 0, rgba);
    }

    public static void renderRing(PoseStack matrix, MultiBufferSource.BufferSource buffer, double cy, float flatness, float radius, float height, int num_segments, int lx, int ly, float r, float g, float b, float a, RenderType type) {
        Matrix4f positionMatrix = matrix.last().pose();
        VertexConsumer bb = buffer.getBuffer(type);
        double theta = 6.2831852D / num_segments;
        double q = height * MathUtils.sin(Math.toRadians(flatness));
        double p = radius + height * MathUtils.cos(Math.toRadians(flatness));
        double x = 0.0D;
        double y = 0.0D;
        double z = 0.0D;
        double xb = 0.0D;
        double yb = 0.0D;
        double zb = 0.0D;
        float squeeze = 3.0F;
        float texx = 0.0F;
        for (int i = 0; i < num_segments + 1; i++) {
            texx += squeeze / (num_segments + 1);
            if (texx >= 1.0F) {
                texx = squeeze / (num_segments + 1);
                bb.vertex(positionMatrix, (float) x, (float) y, (float) z).color(r, g, b, a).uv(0.0F, 0.0F).uv2(lx, ly).endVertex();
                bb.vertex(positionMatrix, (float) xb, (float) yb, (float) zb).color(r, g, b, a).uv(0.0F, 1.0F).uv2(lx, ly).endVertex();
            }
            double tt = i * theta;
            x = -radius * MathUtils.sin(tt);
            y = cy;
            z = radius * MathUtils.cos(tt);
            xb = -p * MathUtils.sin(tt);
            yb = cy - q;
            zb = p * MathUtils.cos(tt);
            bb.vertex(positionMatrix, (float) x, (float) y, (float) z).color(r, g, b, a).uv(texx, 0.0F).uv2(lx, ly).endVertex();
            bb.vertex(positionMatrix, (float) xb, (float) yb, (float) zb).color(r, g, b, a).uv(texx, 1.0F).uv2(lx, ly).endVertex();
        }
        buffer.endBatch(type);
    }
    public static void renderRing0(PoseStack matrix, MultiBufferSource.BufferSource buffer, double cy, float flatness, float radius, float height, int num_segments, int lx, int ly, float r, float g, float b, float a, RenderType type) {
        Matrix4f positionMatrix = matrix.last().pose();
        VertexConsumer bb = buffer.getBuffer(type);
        double theta = 6.2831852D / num_segments;
        double q = height * MathUtils.sin(Math.toRadians(flatness));
        double p = radius + height * MathUtils.cos(Math.toRadians(flatness));
        double x = 0.0D;
        double y = 0.0D;
        double z = 0.0D;
        double xb = 0.0D;
        double yb = 0.0D;
        double zb = 0.0D;
        float squeeze = 3.0F;
        float texx = 0.0F;
        for (int i = 0; i < num_segments + 1; i++) {
            texx += squeeze / (num_segments + 1);
            if (texx >= 1.0F) {
                texx = squeeze / (num_segments + 1);
                bb.vertex(positionMatrix, (float) x, (float) y, (float) z).color(r, g, b, a).uv(0.0F, 0.0F).uv2(lx, ly).endVertex();
                bb.vertex(positionMatrix, (float) xb, (float) yb, (float) zb).color(r, g, b, a).uv(0.0F, 1.0F).uv2(lx, ly).endVertex();
            }
            double tt = i * theta;
            x = -radius * MathUtils.sin(tt);
            y = cy;
            z = radius * MathUtils.cos(tt);
            xb = -p * MathUtils.sin(tt);
            yb = cy - q;
            zb = p * MathUtils.cos(tt);
            bb.vertex(positionMatrix, (float) x, (float) y, (float) z).color(r, g, b, a).uv(texx, 0.0F).uv2(lx, ly).endVertex();
            bb.vertex(positionMatrix, (float) xb, (float) yb, (float) zb).color(r, g, b, a).uv(texx, 1.0F).uv2(lx, ly).endVertex();
        }
    }
    public static void renderSphere(PoseStack matrix, MultiBufferSource buf, float radius, float gradation, int packedLight, float r, float g, float b, float a, RenderType type, float percentage, boolean isInLevel) {
        float PI = 3.141592653589792F;
        VertexConsumer bb = buf.getBuffer(type);
        Matrix4f m = matrix.last().pose();
        Matrix3f m3 = matrix.last().normal();
        a = Math.max(0.0F, a);
        float alpha;
        for (alpha = 0.0F; alpha <= PI; alpha += PI / gradation) {
            float beta;
            for (beta = 0.0F; beta <= PI * 2 * percentage; beta += PI / gradation) {
                float x = radius * MathUtils.cos(beta) * MathUtils.sin(alpha);
                float y = radius * MathUtils.sin(beta) * MathUtils.sin(alpha);
                float z = radius * MathUtils.cos(alpha);
                bb.vertex(m, x, y, z).color(r, g, b, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(m3, 0, 1f, 0f).endVertex();
                var sin = MathUtils.sin((alpha + PI / gradation));
                x = radius * MathUtils.cos(beta) * sin;
                y = radius * MathUtils.sin(beta) * sin;
                z = radius * MathUtils.cos((alpha + PI / gradation));
                bb.vertex(m, x, y, z).color(r, g, b, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(m3, 0, 1f, 0f).endVertex();

            }
        }
        if (isInLevel) if (buf instanceof MultiBufferSource.BufferSource bs) bs.endBatch(type);
    }
    public static void renderSphere0(PoseStack matrix, MultiBufferSource buf, float radius, float gradation, int packedLight, float r, float g, float b, float a, RenderType type, float percentage, boolean isInLevel) {
        float PI = 3.141592653589792F;
        VertexConsumer bb = buf.getBuffer(type);
        Matrix4f m = matrix.last().pose();
        Matrix3f m3 = matrix.last().normal();
        a = Math.max(0.0F, a);
        float alpha;
        for (alpha = 0.0F; alpha <= PI; alpha += PI / gradation) {
            float beta;
            for (beta = 0.0F; beta <= PI * 2 * percentage; beta += PI / gradation) {
                float x = radius * MathUtils.cos(beta) * MathUtils.sin(alpha);
                float y = radius * MathUtils.sin(beta) * MathUtils.sin(alpha);
                float z = radius * MathUtils.cos(alpha);
                bb.vertex(m, x, y, z).color(r, g, b, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(m3, 0, 1f, 0f).endVertex();
                var sin = MathUtils.sin((alpha + PI / gradation));
                x = radius * MathUtils.cos(beta) * sin;
                y = radius * MathUtils.sin(beta) * sin;
                z = radius * MathUtils.cos((alpha + PI / gradation));
                bb.vertex(m, x, y, z).color(r, g, b, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(m3, 0, 1f, 0f).endVertex();

            }
        }
    }
    public static void renderRainbowSphere(PoseStack matrix, MultiBufferSource buf, float radius, float gradation, int packedLight, float r, float g, float b, float a, RenderType type, float percentage) {
        float PI = 3.141592653589792F;
        VertexConsumer bb = buf.getBuffer(type);
        Matrix4f m = matrix.last().pose();
        Matrix3f m3 = matrix.last().normal();
        a = Math.max(0.0F, a);
        float colorr = (float) STFont.milliTime() / 700.0F % 1.0F;
        float colorrStep = (float) STFont.rangeRemap(
                MathUtils.sin(((float) STFont.milliTime() / 800.0F)) % 6.28318D, -0.9D, 2.5D, 0.025D, 0.15D);
        float alpha;
        int col = Color.HSBtoRGB(colorr, 1F, 1F);
        for (alpha = 0.0F; alpha <= PI; alpha += PI / gradation) {
            float beta;
            for (beta = 0.0F; beta <= PI * 2 * percentage; beta += PI / gradation) {
                float x = radius * MathUtils.cos(beta) * MathUtils.sin(alpha);
                float y = radius * MathUtils.sin(beta) * MathUtils.sin(alpha);
                float z = radius * MathUtils.cos(alpha);
                bb.vertex(m, x, y, z).color(FastColor.ARGB32.red(col) / 255F, FastColor.ARGB32.green(col) / 255F, FastColor.ARGB32.blue(col) / 255F, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(m3, 0, 1f, 0f).endVertex();
                colorr += colorrStep;
                col = Color.HSBtoRGB(colorr, 0.15F, 0.7F);
                var sin = MathUtils.sin((alpha + PI / gradation));
                x = radius * MathUtils.cos(beta) * sin;
                y = radius * MathUtils.sin(beta) * sin;
                z = radius * MathUtils.cos((alpha + PI / gradation));
                bb.vertex(m, x, y, z).color(FastColor.ARGB32.red(col) / 255F, FastColor.ARGB32.green(col) / 255F, FastColor.ARGB32.blue(col) / 255F, a).uv(0.0F, 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(m3, 0, 1f, 0f).endVertex();
                colorr += colorrStep;
                col = Color.HSBtoRGB(colorr, 0.15F, 0.7F);
            }
        }
        if (buf instanceof MultiBufferSource.BufferSource bs) bs.endBatch(type);
    }

    public static void renderSphere(PoseStack matrix, MultiBufferSource buf, float radius, float gradation, int packedLight, float r, float g, float b, float a, RenderType type, boolean inLevel) {
        renderSphere(matrix, buf, radius, gradation, packedLight, r, g, b, a, type, 1.0F, inLevel);
    }
    public static void renderSphere0(PoseStack matrix, MultiBufferSource buf, float radius, float gradation, int packedLight, float r, float g, float b, float a, RenderType type, boolean inLevel) {
        renderSphere0(matrix, buf, radius, gradation, packedLight, r, g, b, a, type, 1.0F, inLevel);
    }
    public static void renderRegularPolygon(PoseStack stack, MultiBufferSource.BufferSource source, float radius, float sides, float width, int packedLight, float r, float g, float b, float a, RenderType renderType, float percentage, boolean isInLevel) {
        float PI = 3.1415926F;
        sides /= 2;
        VertexConsumer bb = source.getBuffer(renderType);
        Matrix4f m = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        Vector4f color = new Vector4f(r, g, b, a);
        float alpha;
        for (alpha = 0.0F; alpha < 2F * PI; alpha += PI / sides) {
            if (percentage < 1.0F && alpha / (2F * PI) >= percentage)
                break;
            double cos = MathUtils.cos(alpha);
            double sin = MathUtils.sin(alpha);
            double cos_ = MathUtils.cos(alpha + PI / sides);
            double sin_ = MathUtils.sin(alpha + PI / sides);
            float x = (float) (radius * cos);
            float y = (float) (radius * sin);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0, 0, color);
            x = (float) (radius * cos_);
            y = (float) (radius * sin_);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0, 0, color);
            x = (float) ((radius - width) * cos_);
            y = (float) ((radius - width) * sin_);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0, 0, color);
            x = (float) ((radius - width) * cos);
            y = (float) ((radius - width) * sin);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0, 0, color);
        }
        if (isInLevel) source.endBatch(renderType);
    }
    public static void renderRegularPolygon0(PoseStack stack, MultiBufferSource.BufferSource source, float radius, float sides, float width, int packedLight, float r, float g, float b, float a, RenderType renderType, float percentage, boolean isInLevel) {
        float PI = 3.1415926F;
        sides /= 2;
        VertexConsumer bb = source.getBuffer(renderType);
        Matrix4f m = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        Vector4f color = new Vector4f(r, g, b, a);
        float alpha;
        for (alpha = 0.0F; alpha < 2F * PI; alpha += PI / sides) {
            if (percentage < 1.0F && alpha / (2F * PI) >= percentage)
                break;
            double cos = MathUtils.cos(alpha);
            double sin = MathUtils.sin(alpha);
            double cos_ = MathUtils.cos(alpha + PI / sides);
            double sin_ = MathUtils.sin(alpha + PI / sides);
            float x = (float) (radius * cos);
            float y = (float) (radius * sin);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0, 0, color);
            x = (float) (radius * cos_);
            y = (float) (radius * sin_);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0, 0, color);
            x = (float) ((radius - width) * cos_);
            y = (float) ((radius - width) * sin_);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0, 0, color);
            x = (float) ((radius - width) * cos);
            y = (float) ((radius - width) * sin);
            vertexRP(bb, m, matrix3f, packedLight, x, y, 0, 0, color);
        }
    }
    public static void renderRegularPolygon(PoseStack stack, MultiBufferSource.BufferSource source, float radius, int sides, float width, int packedLight, float r, float g, float b, float a, RenderType renderType, boolean isInLevel) {
        renderRegularPolygon(stack, source, radius, sides, width, packedLight, r, g, b, a, renderType, 1.0F, isInLevel);
    }

    public static void renderRegularPolygon(PoseStack stack, MultiBufferSource.BufferSource source, float radius, int sides, float width, int packedLight, float r, float g, float b, float a, boolean isInLevel) {
        renderRegularPolygon(stack, source, radius, sides, width, packedLight, r, g, b, a, STRenderType.glow(beam), 1.0F, isInLevel);
    }

    public static void renderRegularPolygon(PoseStack stack, MultiBufferSource.BufferSource source, float radius, int sides, float width, int packedLight, float r, float g, float b, float a, float percentage, boolean isInLevel) {
        renderRegularPolygon(stack, source, radius, sides, width, packedLight, r, g, b, a, STRenderType.glow(beam), percentage, isInLevel);
    }

    public static void renderRegularPolygon(PoseStack stack, MultiBufferSource.BufferSource source, float radius, float sides, float width, int packedLight, float r, float g, float b, float a, float percentage, boolean isInLevel) {
        renderRegularPolygon(stack, source, radius, sides, width, packedLight, r, g, b, a, STRenderType.glow(beam), percentage, isInLevel);
    }

//    public static void renderTriangle(PoseStack matrix, MultiBufferSource.BufferSource buf, Vector4f rgba) {
//        RenderType renderType = STRenderType.glow(new ResourceLocation("fantasy_ending:textures/cil_particle/triangle.png"));
//        VertexConsumer bb = buf.getBuffer(renderType);
//        Matrix3f matrix3f = matrix.last().normal();
//        Matrix4f m = matrix.last().pose();
//        vertex(bb, m, matrix3f, 1, 0.0F, 0, 0, 1, rgba);
//        vertex(bb, m, matrix3f, 1, 1.0F, 0, 1, 1, rgba);
//        vertex(bb, m, matrix3f, 1, 1.0F, 1, 1, 0, rgba);
//        vertex(bb, m, matrix3f, 1, 0.0F, 1, 0, 0, rgba);
//        buf.endBatch(renderType);
//    }

    public static void renderStar(PoseStack stack, MultiBufferSource.BufferSource buf, Vector4f rgba, boolean isInLevel) {
        renderStar(stack, buf, rgba, null, isInLevel);
    }

    public static void renderStar(PoseStack stack, MultiBufferSource.BufferSource buf, Vector4f rgba, RenderType type, boolean isInLevel) {
        RenderType renderType = type == null ? STRenderType.PRT_STAR : type;
        VertexConsumer bb = buf.getBuffer(renderType);
        stack.scale(64, 64, 64);
        Matrix4f m = stack.last().pose();
        Matrix3f matrix3f = stack.last().normal();
        vertex(bb, m, matrix3f, 0xF000F0, 0.0F, 0, 0, 1, rgba);
        vertex(bb, m, matrix3f, 0xF000F0, 1.0F, 0, 1, 1, rgba);
        vertex(bb, m, matrix3f, 0xF000F0, 1.0F, 1, 1, 0, rgba);
        vertex(bb, m, matrix3f, 0xF000F0, 0.0F, 1, 0, 0, rgba);
        if (isInLevel) buf.endBatch(renderType);
    }

    public static Vector4f intToVec4f(int rgb) {
        int red = (rgb >> 16) & 0xFF;
        int green = (rgb >> 8) & 0xFF;
        int blue = rgb & 0xFF;
        return new Vector4f(red, green, blue, 1.0f);
    }

    public static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int light, float x, float y, float u, float v, Vector4f color) {
        if (vertexConsumer instanceof IBufferBuilder builder) {
            builder
                    .vertex(x - 0.5F, y - 0.5F, 0.0F)
                    .color(color.x, color.y, color.z, color.w)
                    .uv(u, v);
            //builder.progress(matrix4f).endVertex();
        } else vertexConsumer.vertex(matrix4f, x - 0.5F, y - 0.5F, 0.0F).color(color.x, color.y, color.z, color.w).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int light, float x, float y, float z, float u, float v, Vector4f color) {
        vertexConsumer.vertex(matrix4f, x - 0.5F, y - 0.5F, z).color(color.x, color.y, color.z, color.w).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int light, Vector3f pos, float u, float v, Vector4f color) {
        vertexConsumer.vertex(matrix4f, pos.x - 0.5F, pos.y - 0.5F, pos.z).color(color.x, color.y, color.z, color.w).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void vertex(VertexConsumer vertexConsumer, Matrix3f matrix3f, int light, Vector3f pos, float u, float v, Vector4f color) {
        vertexConsumer.vertex(pos.x - 0.5F, pos.y - 0.5F, pos.z).color(color.x, color.y, color.z, color.w).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static void vertexRP(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int light, float x, float y, float u, float v, Vector4f color) {
        vertexConsumer.vertex(matrix4f, x, y, 0.0F).color(color.x, color.y, color.z, color.w).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }
    public static void vertex(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f matrix3f, int p_254296_, float x, float y, float z, int p_254171_, int p_254026_, Vector4f color) {
        vertexConsumer.vertex(matrix4f, x, y, z).color(color.x, color.y, color.z, color.w).uv((float) p_254171_, (float) p_254026_).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(p_254296_).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public static Quaternionf getQuaternion(float x, float y, float z, float w, boolean isRad) {
        if (isRad)
            w *= 0.017453292519943295F;
        float f = (float) Math.sin(w / 2.0F);
        float i = x * f;
        float j = y * f;
        float k = z * f;
        float r = MathUtils.cos(w / 2.0F);
        return new Quaternionf(i, j, k, r);
    }

    public static void rotateQ(float w, float x, float y, float z, PoseStack matrix) {
        matrix.mulPose(getQuaternion(x, y, z, w, true));
    }

    public static HashSet<Particle> getNoRenderParticles(ParticleEngine manager) {
        if (ps == null)
            ps = manager.particles;
        Queue<Particle> q = ps.get(ParticleRenderType.NO_RENDER);
        if (q != null)
            return new HashSet<>(q);
        return new HashSet<>();
    }

    public static <T extends LivingEntity, M extends EntityModel<T>> void render(LivingEntityRenderer<T, M> renderer, T entity, float partialTick, float animationProgress, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        renderer.model.attackTime = renderer.getAttackAnim(entity, animationProgress);
        boolean shouldSit = entity.isPassenger() && entity.getVehicle() != null && entity.getVehicle().shouldRiderSit();
        renderer.model.riding = shouldSit;
        renderer.model.young = entity.isBaby();
        float f = Mth.rotLerp(animationProgress, entity.yBodyRotO, entity.yBodyRot);
        float f1 = Mth.rotLerp(animationProgress, entity.yHeadRotO, entity.yHeadRot);
        float f2 = f1 - f;
        float f7;
        if (shouldSit && entity.getVehicle() instanceof LivingEntity livingentity) {
            f = Mth.rotLerp(animationProgress, livingentity.yBodyRotO, livingentity.yBodyRot);
            f2 = f1 - f;
            f7 = Mth.wrapDegrees(f2);
            if (f7 < -85.0F) {
                f7 = -85.0F;
            }

            if (f7 >= 85.0F) {
                f7 = 85.0F;
            }

            f = f1 - f7;
            if (f7 * f7 > 2500.0F) {
                f += f7 * 0.2F;
            }

            f2 = f1 - f;
        }

        float f6 = Mth.lerp(animationProgress, entity.xRotO, entity.getXRot());
        if (LivingEntityRenderer.isEntityUpsideDown(entity)) {
            f6 *= -1.0F;
            f2 *= -1.0F;
        }

        float f8;
        if (entity.hasPose(Pose.SLEEPING)) {
            Direction direction = entity.getBedOrientation();
            if (direction != null) {
                f8 = entity.getEyeHeight(Pose.STANDING) - 0.1F;
                poseStack.translate((float) (-direction.getStepX()) * f8, 0.0F, (float) (-direction.getStepZ()) * f8);
            }
        }

        f7 = renderer.getBob(entity, animationProgress);
        renderer.setupRotations(entity, poseStack, f7, f, animationProgress);
        poseStack.scale(-1.0F, -1.0F, 1.0F);
        renderer.scale(entity, poseStack, animationProgress);
        poseStack.translate(0.0F, -1.501F, 0.0F);
        f8 = 0.0F;
        float f5 = 0.0F;
        if (!shouldSit && entity.isAlive()) {
            f8 = entity.walkAnimation.speed(animationProgress);
            f5 = entity.walkAnimation.position(animationProgress);
            if (entity.isBaby()) {
                f5 *= 3.0F;
            }

            if (f8 > 1.0F) {
                f8 = 1.0F;
            }
        }

        renderer.model.prepareMobModel(entity, f5, f8, animationProgress);
        renderer.model.setupAnim(entity, f5, f8, f7, f2, f6);
        Minecraft minecraft = Minecraft.getInstance();
        boolean flag = renderer.isBodyVisible(entity);
        boolean flag1 = !flag && !entity.isInvisibleTo(minecraft.player);
        boolean flag2 = minecraft.shouldEntityAppearGlowing(entity);
        RenderType rendertype = renderer.getRenderType(entity, flag, flag1, flag2);
        if (rendertype != null) {
            VertexConsumer vertexconsumer = bufferSource.getBuffer(rendertype);
            int i = LivingEntityRenderer.getOverlayCoords(entity, renderer.getWhiteOverlayProgress(entity, animationProgress));
            renderer.model.renderToBuffer(poseStack, vertexconsumer, packedLight, i, 1.0F, 1.0F, 1.0F, flag1 ? 0.15F : 1.0F);
        }

        if (!entity.isSpectator()) {
            Iterator<RenderLayer<T, M>> var24 = renderer.layers.iterator();

            while (var24.hasNext()) {
                RenderLayer<T, M> renderlayer = var24.next();
                renderlayer.render(poseStack, bufferSource, packedLight, entity, f5, f8, animationProgress, f7, f2, f6);
            }
        }

        poseStack.popPose();
        renderEntity(renderer, entity, animationProgress, poseStack, bufferSource, packedLight);
    }

    public static <T extends Entity> void renderEntity(EntityRenderer<T> renderer, T entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        RenderNameTagEvent renderNameTagEvent = new RenderNameTagEvent(entity, entity.getDisplayName(), renderer, poseStack, bufferSource, packedLight, partialTick);
        MinecraftForge.EVENT_BUS.post(renderNameTagEvent);
        if (renderNameTagEvent.getResult() != net.minecraftforge.eventbus.api.Event.Result.DENY && (renderNameTagEvent.getResult() == Event.Result.ALLOW || renderer.shouldShowName(entity))) {
            renderer.renderNameTag(entity, renderNameTagEvent.getContent(), poseStack, bufferSource, packedLight);
        }
    }

    public static void cameraStackTranslate(PoseStack stack, Vec3 cameraPos, Vec3 mcPos) {
        Vec3 newVec3 = mcPos.subtract(cameraPos);
        stack.translate(newVec3.x, newVec3.y, newVec3.z);
    }

    public static void captureFrustum(LevelRenderer rr, Matrix4f i1, Matrix4f i2, double i3, double i4, double i5, Frustum i6) {
        rr.captureFrustum(i1, i2, i3, i4, i5, i6);
    }

    public static boolean captureFrustum(LevelRenderer renderer) {
        return renderer.captureFrustum;
    }

    public static void setCaptureFrustum(LevelRenderer renderer, boolean b) {
        renderer.captureFrustum = b;
    }

    public static Frustum capturedFrustum(LevelRenderer renderer) {
        return renderer.cullingFrustum;
    }

    public static Frustum cullingFrustum(LevelRenderer renderer) {
        return renderer.cullingFrustum;
    }

    public static Vector3d frustumPos(LevelRenderer renderer) {
        return renderer.frustumPos;
    }

    public static float zoom(GameRenderer renderer) {
        return renderer.zoom;
    }

    public static float zoomX(GameRenderer renderer) {
        return renderer.zoomX;
    }

    public static float zoomY(GameRenderer renderer) {
        return renderer.zoomY;
    }

    public static float fov(GameRenderer renderer) {
        return renderer.fov;
    }

    public static float oldFov(GameRenderer renderer) {
        return renderer.oldFov;
    }

    public static boolean panoramicMode(GameRenderer renderer) {
        return renderer.panoramicMode;
    }

    public static Matrix4f getProjectionMatrix(GameRenderer renderer, double fov) {
        PoseStack posestack = new PoseStack();
        posestack.last().pose().identity();
        float z = zoom(renderer);
        if (z != 1.0F) {
            float zoomX = zoomX(renderer);
            float zoomY = zoomY(renderer);
            posestack.translate(zoomX, -zoomY, 0.0F);
            posestack.scale(z, z, 1.0F);
        }

        posestack.last().pose().mul((new Matrix4f()).setPerspective((float) (fov * (double) ((float) Math.PI / 180F)), (float) Minecraft.getInstance().getWindow().getWidth() / (float) Minecraft.getInstance().getWindow().getHeight(), 0.05F, renderer.getDepthFar()));
        return posestack.last().pose();
    }

    public static double getFov(GameRenderer renderer, Camera camera, float partialTick, boolean fov) {
        if (panoramicMode(renderer)) {
            return 90.0D;
        } else {
            double d0 = 70.0D;
            if (fov) {
                d0 = Minecraft.getInstance().options.fov().get();
                d0 *= Mth.lerp(partialTick, oldFov(renderer), fov(renderer));
            }

            if (camera.getEntity() instanceof LivingEntity && ((LivingEntity) camera.getEntity()).isDeadOrDying()) {
                float f = Math.min((float) ((LivingEntity)camera.getEntity()).deathTime + partialTick, 20.0F);
                d0 /= (1.0F - 500.0F / (f + 500.0F)) * 2.0F + 1.0F;
            }

            FogType fogtype = camera.getFluidInCamera();
            if (fogtype == FogType.LAVA || fogtype == FogType.WATER) {
                d0 *= Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0D, 0.85714287F);
            }
            return net.minecraftforge.client.ForgeHooksClient.getFieldOfView(renderer, camera, partialTick, d0, fov);
        }
    }

    public static double normalFov(GameRenderer renderer, Camera camera, float partialTick, boolean fov) {
        if (panoramicMode(renderer)) {
            return 90.0D;
        } else {
            double d0 = 70.0D;
            if (camera.getEntity() instanceof LivingEntity && ((LivingEntity) camera.getEntity()).isDeadOrDying()) {
                float f = Math.min((float) ((LivingEntity) camera.getEntity()).deathTime + partialTick, 20.0F);
                d0 /= (1.0F - 500.0F / (f + 500.0F)) * 2.0F + 1.0F;
            }

            FogType fogtype = camera.getFluidInCamera();
            if (fogtype == FogType.LAVA || fogtype == FogType.WATER) {
                d0 *= Mth.lerp(Minecraft.getInstance().options.fovEffectScale().get(), 1.0D, 0.85714287F);
            }
            return net.minecraftforge.client.ForgeHooksClient.getFieldOfView(renderer, camera, partialTick, d0, fov);
        }
    }

    private static void vertex01(VertexConsumer vertexConsumer, Matrix4f matrix4f, float alpha) {
        vertexConsumer.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(1F, 1F, 1F, alpha).endVertex();
    }

    private static void vertex2(VertexConsumer vertexConsumer, Matrix4f matrix4f, float y, float scale) {
        vertexConsumer.vertex(matrix4f, -HALF_SQRT_3 * scale, y, -0.5F * scale).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex3(VertexConsumer vertexConsumer, Matrix4f matrix4f, float y, float scale) {
        vertexConsumer.vertex(matrix4f, HALF_SQRT_3 * scale, y, -0.5F * scale).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex4(VertexConsumer vertexConsumer, Matrix4f matrix4f, float y, float scale) {
        vertexConsumer.vertex(matrix4f, 0.0F, y, scale).color(255, 0, 255, 0).endVertex();
    }

    private static void vertex010(VertexConsumer vertexConsumer, Matrix4f matrix4f, float alpha, float r, float g, float b) {
        vertexConsumer.vertex(matrix4f, 0.0F, 0.0F, 0.0F).color(r, g, b, alpha).endVertex();
    }

    private static void vertex20(VertexConsumer vertexConsumer, Matrix4f matrix4f, float y, float scale, float r, float g, float b, float a) {
        vertexConsumer.vertex(matrix4f, -HALF_SQRT_3 * scale, y, -0.5F * scale).color(r, g, b, a).endVertex();
    }

    private static void vertex30(VertexConsumer vertexConsumer, Matrix4f matrix4f, float y, float scale, float r, float g, float b, float a) {
        vertexConsumer.vertex(matrix4f, HALF_SQRT_3 * scale, y, -0.5F * scale).color(r, g, b, a).endVertex();
    }

    private static void vertex40(VertexConsumer vertexConsumer, Matrix4f matrix4f, float y, float scale, float r, float g, float b, float a) {
        vertexConsumer.vertex(matrix4f, 0.0F, y, scale).color(r, g, b, a).endVertex();
    }

    public static void dragonDeathLight(float time, PoseStack stack) {
        float f5 = time ;
        HALF_SQRT_3 = 0.05F;
        float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
        RandomSource randomsource = RandomSource.create(432L);
        RenderType renderType = STRenderType.LIGHTING_ITEM_ENTITY();
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers.bufferSource();
        VertexConsumer vertexconsumer2 = bufferSource.getBuffer(renderType);
        stack.pushPose();
        float lerp = f7;
        for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
            stack.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
            stack.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F));
            stack.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
            stack.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F + f5 * 90.0F));
            float f3 = 5.0F + (1 - lerp) * 10.0F;
            float f4 = 1.0F + (1 - lerp) * 2.0F;
            Matrix4f matrix4f = stack.last().pose();
            float j = 1.0F - f7;
            vertex01(vertexconsumer2, matrix4f, j);
            vertex2(vertexconsumer2, matrix4f, f3, f4);
            vertex3(vertexconsumer2, matrix4f, f3, f4);
            vertex01(vertexconsumer2, matrix4f, j);
            vertex3(vertexconsumer2, matrix4f, f3, f4);
            vertex4(vertexconsumer2, matrix4f, f3, f4);
            vertex01(vertexconsumer2, matrix4f, j);
            vertex4(vertexconsumer2, matrix4f, f3, f4);
            vertex2(vertexconsumer2, matrix4f, f3, f4);
        }
        stack.popPose();
        bufferSource.endBatch(renderType);
    }

    public static void dragonDeathLight2(float time, PoseStack stack, Vector3f mid, Vector4f tail) {
        float f5 = time % 1;
        HALF_SQRT_3 = 0.05F;
        float f7 = Math.min(f5 > 0.8F ? (f5 - 0.8F) / 0.2F : 0.0F, 1.0F);
        RandomSource randomsource = RandomSource.create(432L);
        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers.bufferSource();
        RenderType renderType = STRenderType.LIGHTING_ITEM_ENTITY();
        VertexConsumer vertexconsumer2 = source.getBuffer(renderType);
        stack.pushPose();
        float lerp = f7;
        for (int i = 0; (float) i < (f5 + f5 * f5) / 2.0F * 60.0F; ++i) {
            stack.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
            stack.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F));
            stack.mulPose(Axis.XP.rotationDegrees(randomsource.nextFloat() * 360.0F));
            stack.mulPose(Axis.YP.rotationDegrees(randomsource.nextFloat() * 360.0F));
            stack.mulPose(Axis.ZP.rotationDegrees(randomsource.nextFloat() * 360.0F + f5 * 90.0F));
            float f3 = 5.0F + (1 - lerp) * 10.0F;
            float f4 = 1.0F + (1 - lerp) * 2.0F;
            Matrix4f matrix4f = stack.last().pose();
            float j = 1.0F - f7;
            vertex010(vertexconsumer2, matrix4f, j, mid.x, mid.y, mid.z);
            vertex20(vertexconsumer2, matrix4f, f3, f4, tail.x, tail.y, tail.z, tail.w);
            vertex30(vertexconsumer2, matrix4f, f3, f4, tail.x, tail.y, tail.z, tail.w);
            vertex010(vertexconsumer2, matrix4f, j, mid.x, mid.y, mid.z);
            vertex30(vertexconsumer2, matrix4f, f3, f4, tail.x, tail.y, tail.z, tail.w);
            vertex40(vertexconsumer2, matrix4f, f3, f4, tail.x, tail.y, tail.z, tail.w);
            vertex010(vertexconsumer2, matrix4f, j, mid.x, mid.y, mid.z);
            vertex40(vertexconsumer2, matrix4f, f3, f4, tail.x, tail.y, tail.z, tail.w);
            vertex20(vertexconsumer2, matrix4f, f3, f4, tail.x, tail.y, tail.z, tail.w);
        }
        stack.popPose();
        source.endBatch(renderType);
    }

    public static void renderStatic(ItemStack itemStack, ItemDisplayContext displayContext, int packedLight, int packedOverlay, PoseStack poseStack, MultiBufferSource bufferSource, @Nullable Level world, int seed) {
        renderStatic(null, itemStack, displayContext, false, poseStack, bufferSource, world, packedLight, packedOverlay, seed);
    }

    public static void renderStatic(@Nullable LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, @Nullable Level world, int packedLight, int packedOverlay, int seed) {
        if (!itemStack.isEmpty()) {
            BakedModel bakedmodel = getItemRenderer().getModel(itemStack, world, entity, seed);
            render(itemStack, displayContext, leftHand, poseStack, bufferSource, packedLight, packedOverlay, bakedmodel);
        }
    }

    public static void render(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, BakedModel bakedModel) {
        if (!itemStack.isEmpty()) {
            poseStack.pushPose();
            boolean flag = displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.GROUND || displayContext == ItemDisplayContext.FIXED;
            if (flag) {
                if (itemStack.is(Items.TRIDENT)) {
                    bakedModel = getItemRenderer().getItemModelShaper().getModelManager().getModel(TRIDENT_MODEL);
                } else if (itemStack.is(Items.SPYGLASS)) {
                    bakedModel = getItemRenderer().getItemModelShaper().getModelManager().getModel(SPYGLASS_MODEL);
                }
            }

            bakedModel = net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(poseStack, bakedModel, displayContext, leftHand);
            poseStack.translate(-0.5F, -0.5F, -0.5F);
            if (!bakedModel.isCustomRenderer() && (!itemStack.is(Items.TRIDENT) || flag)) {
                boolean flag1;
                if (displayContext != ItemDisplayContext.GUI && !displayContext.firstPerson() && itemStack.getItem() instanceof BlockItem) {
                    Block block = ((BlockItem) itemStack.getItem()).getBlock();
                    flag1 = !(block instanceof HalfTransparentBlock) && !(block instanceof StainedGlassPaneBlock);
                } else {
                    flag1 = true;
                }
                for (var model : bakedModel.getRenderPasses(itemStack, flag1)) {
                    for (var rendertype : model.getRenderTypes(itemStack, flag1)) {
                        VertexConsumer vertexconsumer;
                        if (hasAnimatedTexture(itemStack) && itemStack.hasFoil()) {
                            poseStack.pushPose();
                            PoseStack.Pose posestack$pose = poseStack.last();
                            if (displayContext == ItemDisplayContext.GUI) {
                                MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.5F);
                            } else if (displayContext.firstPerson()) {
                                MatrixUtil.mulComponentWise(posestack$pose.pose(), 0.75F);
                            }

                            if (flag1) {
                                vertexconsumer = ItemRenderer.getCompassFoilBufferDirect(bufferSource, rendertype, posestack$pose);
                            } else {
                                vertexconsumer = ItemRenderer.getCompassFoilBuffer(bufferSource, rendertype, posestack$pose);
                            }

                            poseStack.popPose();
                        } else if (flag1) {
                            vertexconsumer = ItemRenderer.getFoilBufferDirect(bufferSource, rendertype, true, itemStack.hasFoil());
                        } else {
                            vertexconsumer = ItemRenderer.getFoilBuffer(bufferSource, rendertype, true, itemStack.hasFoil());
                        }

                        getItemRenderer().renderModelLists(model, itemStack, packedLight, packedOverlay, poseStack, vertexconsumer);
                    }
                }
            } else {
                net.minecraftforge.client.extensions.common.IClientItemExtensions.of(itemStack).getCustomRenderer().renderByItem(itemStack, displayContext, poseStack, bufferSource, packedLight, packedOverlay);
            }

            poseStack.popPose();
        }
    }

    public static boolean hasAnimatedTexture(ItemStack itemStack) {
        return itemStack.is(ItemTags.COMPASSES) || itemStack.is(Items.CLOCK);
    }

    public static void renderLineBox(VertexConsumer vertexConsumer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float r, float g, float b, float a) {
        renderLineBox(new PoseStack(), vertexConsumer, (float) minX, (float) minY, (float) minZ, (float) maxX, (float) maxY, (float) maxZ, r, g, b, a, r, g, b);
    }

    public static void renderLineBox(PoseStack poseStack, VertexConsumer vertexConsumer, AABB aabb, float r, float g, float b, float a) {
        renderLineBox(poseStack, vertexConsumer, (float) aabb.minX, (float) aabb.minY, (float) aabb.minZ, (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ, r, g, b, a, r, g, b);
    }

    public static void renderLineBox(PoseStack poseStack, VertexConsumer vertexConsumer, double minX, double minY, double minZ, double maxX, double maxY, double maxZ, float r, float g, float b, float a) {
        renderLineBox(poseStack, vertexConsumer, (float) minX, (float) minY, (float) minZ, (float) maxX, (float) maxY, (float) maxZ, r, g, b, a, r, g, b);
    }
    public static void vertexRPNormal(VertexConsumer vertexConsumer, Matrix4f matrix4f, Matrix3f normal, int light, float x, float y, float z, float u, float v, float nX, float nY, float nZ, float r, float g, float b, float a) {
        vertexConsumer.vertex(matrix4f, x, y, 0.0F).color(r, g, b ,a).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(light).normal(normal, nX, nY, nZ).endVertex();
    }
    public static void renderLineBox(PoseStack poseStack, VertexConsumer vertexConsumer, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, float r, float g, float b, float a, float r2, float g2, float b2) {
        Matrix4f matrix4f = poseStack.last().pose();
        Matrix3f matrix3f = poseStack.last().normal();
        vertexConsumer.vertex(matrix4f, minX, minY, minZ).color(r, g2, b2, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, minZ).color(r, g2, b2, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, minY, minZ).color(r2, g, b2, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, minZ).color(r2, g, b2, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, minY, minZ).color(r2, g2, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, minY, maxZ).color(r2, g2, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, minZ).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, minZ).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, minZ).color(r, g, b, a).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, minZ).color(r, g, b, a).normal(matrix3f, -1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, minZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, minY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, -1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, minY, maxZ).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, maxZ).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, minZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, -1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, minX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 1.0F, 0.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, minY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, minZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
        vertexConsumer.vertex(matrix4f, maxX, maxY, maxZ).color(r, g, b, a).normal(matrix3f, 0.0F, 0.0F, 1.0F).endVertex();
    }
    public static void renderRegularPolygon5(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float radius, int packedLight, float r, float g, float b, float a, float percentage, float depth) {
        float PI = 3.1415926F;
        float angel = PI * 0.4F;
        float alpha;
        for (alpha = 0.0F; alpha < 2F * PI; alpha += angel) {
            double cos = MathUtils.cos(alpha);
            double sin = MathUtils.sin(alpha);
            float aDelta = angel * percentage;
            double cos_ = MathUtils.cos(alpha + aDelta);
            double sin_ = MathUtils.sin(alpha + aDelta);
            float x = (float) (radius * cos);
            float y = (float) (radius * sin);
            float x2 = (float) (radius * cos_);
            float y2 = (float) (radius * sin_);
            matrix4f.translate(0, 0, depth);
            vertexRPNormal(vertexConsumer, matrix4f, matrix3f, packedLight, x, y, 0, 0, 0, x2-x, 1.0F, y2-y, r, g ,b, a);
            vertexRPNormal(vertexConsumer, matrix4f, matrix3f, packedLight, x2, y2, 0, 1, 0, x2-x, 1.0F, y2-y, r, g ,b, a);
            matrix4f.translate(0, 0, -depth);
        }
    }
    public static void renderRegularPolygon5div1(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float radius, int packedLight, float r, float g, float b, float a, float percentage, float depth) {
        float PI = Mth.PI;
        float angel = PI * 0.4F;
        float alpha;
        for (alpha = 0.0F; alpha < 2F * PI; alpha += angel) {
            if (Float.floatToIntBits(alpha) == Float.floatToIntBits(angel*2F)) continue;
            double cos = MathUtils.cos(alpha);
            double sin = MathUtils.sin(alpha);
            double cos_ = Mth.lerp(percentage, cos, MathUtils.cos(alpha + angel));
            double sin_ = Mth.lerp(percentage, sin, MathUtils.sin(alpha + angel));
            float x = (float) (radius * cos);
            float y = (float) (radius * sin);
            float x2 = (float) (radius * cos_);
            float y2 = (float) (radius * sin_);
            matrix4f.translate(0, 0, depth);
            vertexRPNormal(vertexConsumer, matrix4f, matrix3f, packedLight, x, y, 0, 0, 0, x2-x, 1.0F, y2-y, r, g ,b, a);
            vertexRPNormal(vertexConsumer, matrix4f, matrix3f, packedLight, x2, y2, 0, 1, 0, x2-x, 1.0F, y2-y, r, g ,b, a);
            matrix4f.translate(0, 0, -depth);
        }
    }
    public static void renderRegularIcosahedron(PoseStack matrix, MultiBufferSource.BufferSource bufferSource, float radius, int packedLight, float r, float g, float b, float a, float percent) {
        float PI = 3.1415926535F;
        RenderType type = RenderType.lines();
        VertexConsumer bb = bufferSource.getBuffer(type);
        a = Math.max(0.0F, a);
        //x
        float alpha = 1.1074114103904023F;
        //y
        float beta = PI * 0.4F;
        float ri = 1.309016994F;
        float l = 1F;
        matrix.pushPose();
        matrix.mulPose(Axis.XP.rotation(-PI/2F));
        matrix.scale(radius, radius, radius);
        renderRegularPolygon5(matrix.last().pose(), matrix.last().normal(), bb, l, packedLight, r, g ,b ,a, percent, ri);
        for (int i=0;i<5;i++) {
            matrix.mulPose(Axis.ZP.rotation(beta));
            matrix.pushPose();
            matrix.mulPose(Axis.ZP.rotation(PI));
            matrix.mulPose(Axis.YP.rotation(alpha));
            renderRegularPolygon5div1(matrix.last().pose(), matrix.last().normal(), bb, l, packedLight, r, g ,b ,a, percent, ri);
            matrix.popPose();
        }
        matrix.mulPose(Axis.YP.rotation(PI));
        renderRegularPolygon5(matrix.last().pose(), matrix.last().normal(), bb, l, packedLight, r, g ,b ,a, percent, ri);
        for (int i=0;i<5;i++) {
            matrix.mulPose(Axis.ZP.rotationDegrees(72));
            matrix.pushPose();
            matrix.mulPose(Axis.ZP.rotation(PI));
            matrix.mulPose(Axis.YP.rotation(alpha));
            renderRegularPolygon5div1(matrix.last().pose(), matrix.last().normal(), bb, l, packedLight, r, g ,b ,a, percent, ri);
            matrix.popPose();
        }

        matrix.popPose();
        bufferSource.endBatch(type);
    }
    public static float getRenderRotation() {
        float time = (float) Blaze3D.getTime() * 40F;
        ClientModHandler.rotationStar = Easing.OUT_ELASTIC.interpolate((time % 90.0F) / 90f, 0F, 1F) * 90F + (int)(time / 90.0F) * 90.0F + time/2.5F;

        return ClientModHandler.rotationStar;
    }

    public static Vec2 perpendicularTrailPoints(Vector4f start, Vector4f end, float width) {
        float x = -start.x();
        float y = -start.y();
        float normalize;
        if (Math.abs(start.z()) > 0.0F) {
            normalize = end.z() / start.z();
            x = end.x() + x * normalize;
            y = end.y() + y * normalize;
        } else if (Math.abs(end.z()) <= 0.0F) {
            x += end.x();
            y += end.y();
        }

        if (start.z() > 0.0F) {
            x = -x;
            y = -y;
        }

        if (x * x + y * y > 0.0F) {
            normalize = width * 0.5F / distance(x, y);
            x *= normalize;
            y *= normalize;
        }

        return new Vec2(-y, x);
    }

    public static float distSqr(float... a) {
        float d = 0.0F;
        float[] var2 = a;
        int var3 = a.length;

        for (int var4 = 0; var4 < var3; ++var4) {
            float f = var2[var4];
            d += f * f;
        }

        return d;
    }

    public static float distance(float... a) {
        return Mth.sqrt(distSqr(a));
    }
    public static Vector4f midpoint(Vector4f a, Vector4f b) {
        return new Vector4f((a.x() + b.x()) * 0.5F, (a.y() + b.y()) * 0.5F, (a.z() + b.z()) * 0.5F, (a.w() + b.w()) * 0.5F);
    }
}
