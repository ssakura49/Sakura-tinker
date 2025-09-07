package com.ssakura49.sakuratinker.client.component;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.mixin.EmptyGlyphAccessor;
import com.ssakura49.sakuratinker.render.RendererUtils;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import com.ssakura49.sakuratinker.utils.math.MathUtils;
import com.ssakura49.sakuratinker.utils.time.TimeContext;
import com.ssakura49.sakuratinker.utils.time.TimeStopRandom;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.GlyphRenderTypes;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Map;
import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class STFont extends Font {
    public Map<ResourceLocation, FontSet> fontSets;
    public Function<ResourceLocation, FontSet> fontSetFunction;
    public static RandomSource randomSource = new TimeStopRandom(TimeContext.Client.generateUniqueSeed());
    public static FontManager fontManager = Minecraft.getInstance().fontManager;
    public static STFont INSTANCE;

    static {
        INSTANCE = new STFont((location) -> {
            return fontManager.fontSets.getOrDefault(fontManager.renames.getOrDefault(location, location), fontManager.missingFontSet);
        }, false, fontManager.fontSets);

    }

    public STFont(Function<ResourceLocation, FontSet> function, boolean pFilterFishyGlyphs, Map<ResourceLocation, FontSet> map) {
        super(function, pFilterFishyGlyphs);
        this.fontSets = map;
        this.fontSetFunction = function;
    }

    public static long milliTime() {
        return Util.getMillis();
    }

    public static int adjustColor(int color) {
        return (color & -67108864) == 0 ? color | -16777216 : color;
    }

    public static double rangeRemap(double value, double low1, double high1, double low2, double high2) {
        return low2 + (value - low1) * (high2 - low2) / (high1 - low1);
    }
    public static void drawRenderTypeRect(float posX, float posY, float width, float height, RenderType renderType, Matrix4f matrix4f) {
        VertexConsumer vertexconsumer = Minecraft.getInstance().renderBuffers.bufferSource().getBuffer(renderType);
        posX -= 0.05F;
        posY -= 0.05F;
        float x2 = posX + width;
        float y2 = posY + height;
        x2 += 0.05F;
        y2 += 0.05F;
        vertexconsumer.vertex(matrix4f, posX, posY, (float) 0).color(0, 0, 0, 0).uv(0.0F, 1.0F).uv2(0, 0).normal(0, 0, 0).endVertex();
        vertexconsumer.vertex(matrix4f, posX, y2, (float) 0).color(0, 0, 0, 0).uv(0.0F, 1.0F).uv2(0, 0).normal(0, 0, 0).endVertex();
        vertexconsumer.vertex(matrix4f, x2, y2, (float) 0).color(0, 0, 0, 0).uv(0.0F, 1.0F).uv2(0, 0).normal(0, 0, 0).endVertex();
        vertexconsumer.vertex(matrix4f, x2, posY, (float) 0).color(0, 0, 0, 0).uv(0.0F, 1.0F).uv2(0, 0).normal(0, 0, 0).endVertex();
    }

    public static boolean doRainbowRendering(String drawText) {
        return drawText.equals(Component.translatable("item.sakuratinker.goozma").getString())
                //||drawText.equals(Component.translatable("item.sakuratinker.chimera_gamma").getString())
                ;
    }

    public static boolean doFlashingEffect(String drawText) {
        return drawText.endsWith(Component.translatable("attribute.name.sakuratinker.reality_suppression").getString())
                ||drawText.endsWith(Component.translatable("attribute.name.sakuratinker.reality_suppression_resistance").getString())
                ||drawText.endsWith(Component.translatable("modifier.sakuratinker.inga_ryu.flavor").getString())
                ;

    }

    public static boolean doCosmicRendering(String drawText) {
        return drawText.contains(Component.translatable("item.sakuratinker.cosmic_common_lore").getString())
                || drawText.equals(Component.translatable("item.sakuratinker.chimera_gamma_contributors1").getString());
    }

    public static boolean doCosmicBackground(String drawText) {
        return drawText.contains(Component.translatable("item.sakuratinker.cosmic_background_lore").getString())
                ;
    }
    public static boolean doGoldBlueGradient(String drawText) {
        return drawText.equals(Component.translatable("item.sakuratinker.chimera_gamma").getString())
                ||drawText.equals(Component.translatable("item.sakuratinker.molten_chimera_gamma_bucket").getString())
                ||drawText.equals(Component.translatable("fluid.tconstruct.molten_chimera_gamma").getString())
                ||drawText.equals(Component.translatable("fluid.tconstruct.flowing_molten_chimera_gamma").getString())
                ;
    }

    public static FontSet getFontSet(Font font, ResourceLocation resourceLocation) {
        return font.getFontSet(resourceLocation);
    }

    public static void renderChar(STBakedGlyph stBakedGlyph, boolean pBold, boolean pItalic, float pBoldOffset, float pX, float pY, Matrix4f pMatrix, VertexConsumer pBuffer, float pRed, float pGreen, float pBlue, float pAlpha, int pPackedLight) {
        stBakedGlyph.render(pItalic, pX, pY, pMatrix, pBuffer, pRed, pGreen, pBlue, pAlpha, pPackedLight);
        if (pBold) {
            stBakedGlyph.render(pItalic, pX + pBoldOffset, pY, pMatrix, pBuffer, pRed, pGreen, pBlue, pAlpha, pPackedLight);
        }

    }

    public static boolean filterFishyGlyphs(Font font) {
        return font.filterFishyGlyphs;
    }

    public static int getDarkColor(int i) {
        double d0 = 0.4D;
        int j = (int) ((double) FastColor.ARGB32.red(i) * d0);
        int k = (int) ((double) FastColor.ARGB32.green(i) * d0);
        int l = (int) ((double) FastColor.ARGB32.blue(i) * d0);
        return FastColor.ARGB32.color(0, j, k, l);
    }

    @Override
    public int drawInBatch(@NotNull String pText, float pX, float pY, int pColor, boolean pDropShadow, @NotNull Matrix4f pMatrix, @NotNull MultiBufferSource pBuffer, @NotNull DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLightCoords) {
        return this.drawInBatch(pText, pX, pY, pColor, pDropShadow, pMatrix, pBuffer, pDisplayMode, pBackgroundColor, pPackedLightCoords, this.isBidirectional());
    }

    @Override
    public int drawInBatch(@NotNull FormattedCharSequence charSequence, float startX, float startY, int iColor, boolean p_273674_, @NotNull Matrix4f matrix4f, @NotNull MultiBufferSource bufferSource, @NotNull DisplayMode displayMode, int overlay, int light) {
        float colorr = (float) milliTime() / 400.0F % 1.0F;
        float colorrStep = (float) rangeRemap(
                MathUtils.sin(((float) milliTime() / 200.0F)) % 6.28318D, -0.9D, 2.5D, 0.025D, 0.15D);
        float posX = startX;
        float posY = startY;
        String drawText = FontTextBuilder.formattedCharSequenceToString(charSequence);
        if (doRainbowRendering(drawText)) {
            for (int i = 0; i < drawText.length(); i++) {
                float yOffset = MathUtils.sin((i * (0.5F) + (float) milliTime() / 200.0F));
                int value = iColor & 0xFF000000 | Color.HSBtoRGB(colorr, 0.15F, 1.0F);
                int c = iColor >>> 24 << 24 | ((value >> 16) & 0xFF << 16) | ((value >> 8) & 0xFF << 8) | (value & 0xFF);
                posX = super.drawInBatch(String.valueOf(drawText.charAt(i)), posX, startY + yOffset, c, p_273674_, matrix4f, bufferSource, displayMode, overlay, light);
                colorr += colorrStep;
                colorr %= 1.0F;
            }
            return (int) posX;
        } else if
        (doCosmicBackground(drawText)) {
            int width = width(drawText);
            int height = lineHeight;
            drawRenderTypeRect(posX, posY, width, height-1, STRenderType.END_PORTAL(RendererUtils.cosmic), matrix4f);
            if (bufferSource instanceof MultiBufferSource.BufferSource source) source.endBatch();
            super.drawInBatch(charSequence, startX + .35F + randomSource.nextFloat() * .1f, startY + .35F, iColor, p_273674_, matrix4f, bufferSource, displayMode, overlay, light);//for (int i=0;i<4;i++)
        } else if
        (doGoldBlueGradient(drawText)) {
            // 起始颜色 dd944b → (221, 148, 75)
            // 结束颜色 54bce9 → (84, 188, 233)
            int r1 = 221, g1 = 148, b1 = 75;
            int r2 = 84, g2 = 188, b2 = 233;
            long time = milliTime();
            float animTime = (float) time / 200.0F;
            for (int i = 0; i < drawText.length(); i++) {
                float t = (float) i / (float) (drawText.length() - 1);
                int r = (int) (r1 + t * (r2 - r1));
                int g = (int) (g1 + t * (g2 - g1));
                int b = (int) (b1 + t * (b2 - b1));
                int a = iColor >>> 24;
                int c = FastColor.ARGB32.color(a, r, g, b);
                float yOffset = MathUtils.sin(i * 0.5F + animTime) * 1.1F;  // 跳动感
                posX = super.drawInBatch(String.valueOf(drawText.charAt(i)), posX, startY + yOffset, c, p_273674_, matrix4f, bufferSource, displayMode, overlay, light);
            }
            return (int) posX;
        }
        if (doFlashingEffect(drawText)) {
            super.drawInBatch(drawText, startX + .35F, startY + .35F, FastColor.ARGB32.color(130, randomSource.nextInt(255), randomSource.nextInt(255), randomSource.nextInt(255)), false, matrix4f, bufferSource, displayMode, overlay, light);//for (int i=0;i<4;i++)
            super.drawInBatch(drawText, startX - .3F, startY - .3F, FastColor.ARGB32.color(130, randomSource.nextInt(255), randomSource.nextInt(255), randomSource.nextInt(255)), false, matrix4f, bufferSource, displayMode, overlay, light);//for (int i=0;i<4;i++)
            super.drawInBatch(drawText, startX + .35F, startY + .35F, FastColor.ARGB32.color(130, randomSource.nextInt(255), randomSource.nextInt(255), randomSource.nextInt(255)), false, matrix4f, bufferSource, displayMode, overlay, light);//for (int i=0;i<4;i++)
            super.drawInBatch(drawText, startX - .3F, startY - .3F, FastColor.ARGB32.color(130, randomSource.nextInt(255), randomSource.nextInt(255), randomSource.nextInt(255)), false, matrix4f, bufferSource, displayMode, overlay, light);//for (int i=0;i<4;i++)
            return super.drawInBatch(charSequence, startX, startY, iColor, p_273674_, matrix4f, bufferSource, displayMode, overlay, light);
        }
        return super.drawInBatch(charSequence, startX, startY, iColor, p_273674_, matrix4f, bufferSource, displayMode, overlay, light);
    }

    public static class STBakedGlyph extends BakedGlyph {
        private final GlyphRenderTypes renderTypes;
        private final float u0;
        private final float u1;
        private final float v0;
        private final float v1;
        private final float left;
        private final float right;
        private final float up;
        private final float down;

        public STBakedGlyph(GlyphRenderTypes pRenderTypes, float u0, float u1, float v0, float v1, float left, float right, float up, float down) {
            super(pRenderTypes, u0, u1, v0, v1, left, right, up, down);
            this.renderTypes = pRenderTypes;
            this.u0 = u0;
            this.u1 = u1;
            this.v0 = v0;
            this.v1 = v1;
            this.left = left;
            this.right = right;
            this.up = up;
            this.down = down;
        }

        public static STBakedGlyph createByEmpty(BakedGlyph glyph) {
            EmptyGlyphAccessor accessor = (EmptyGlyphAccessor) glyph;
            return new STBakedGlyph(accessor.renderTypes(), accessor.u0(), accessor.u1(), accessor.v0(), accessor.v1(), accessor.left(), accessor.right(), accessor.up(), accessor.down());
        }

        public void render(boolean pItalic, float pX, float pY, @NotNull Matrix4f pMatrix, VertexConsumer pBuffer, float pRed, float pGreen, float pBlue, float pAlpha, int pPackedLight) {
            int i = 3;
            float f = pX + this.left;
            float f1 = pX + this.right;
            float f2 = this.up - 3.0F;
            float f3 = this.down - 3.0F;
            float f4 = pY + f2;
            float f5 = pY + f3;
            float f6 = pItalic ? 1.0F - 0.25F * f2 : 0.0F;
            float f7 = pItalic ? 1.0F - 0.25F * f3 : 0.0F;
            pBuffer.vertex(pMatrix, f + f6, f4, 0.0F).color(pRed, pGreen, pBlue, pAlpha).uv(this.u0, this.v0).uv2(pPackedLight).normal(0F, 0F, -1F).endVertex();
            pBuffer.vertex(pMatrix, f + f7, f5, 0.0F).color(pRed, pGreen, pBlue, pAlpha).uv(this.u0, this.v1).uv2(pPackedLight).normal(0F, 0F, -1F).endVertex();
            pBuffer.vertex(pMatrix, f1 + f7, f5, 0.0F).color(pRed, pGreen, pBlue, pAlpha).uv(this.u1, this.v1).uv2(pPackedLight).normal(0F, 0F, -1F).endVertex();
            pBuffer.vertex(pMatrix, f1 + f6, f4, 0.0F).color(pRed, pGreen, pBlue, pAlpha).uv(this.u1, this.v0).uv2(pPackedLight).normal(0F, 0F, -1F).endVertex();
        }

        public void renderEffect(STBakedGlyph.Effect effect, Matrix4f matrix4f, VertexConsumer vertexConsumer, int pPackedLight) {
            vertexConsumer.vertex(matrix4f, effect.x0, effect.y0, effect.depth).color(effect.r, effect.g, effect.b, effect.a).uv(this.u0, this.v0).uv2(pPackedLight).normal(0F, 0F, -1F).endVertex();
            vertexConsumer.vertex(matrix4f, effect.x1, effect.y0, effect.depth).color(effect.r, effect.g, effect.b, effect.a).uv(this.u0, this.v1).uv2(pPackedLight).normal(0F, 0F, -1F).endVertex();
            vertexConsumer.vertex(matrix4f, effect.x1, effect.y1, effect.depth).color(effect.r, effect.g, effect.b, effect.a).uv(this.u1, this.v1).uv2(pPackedLight).normal(0F, 0F, -1F).endVertex();
            vertexConsumer.vertex(matrix4f, effect.x0, effect.y1, effect.depth).color(effect.r, effect.g, effect.b, effect.a).uv(this.u1, this.v0).uv2(pPackedLight).normal(0F, 0F, -1F).endVertex();
        }

        public @NotNull RenderType renderType(Font.@NotNull DisplayMode mode) {
            return this.renderTypes.select(mode);
        }

        @OnlyIn(Dist.CLIENT)
        public static class Effect {
            protected final float x0;
            protected final float y0;
            protected final float x1;
            protected final float y1;
            protected final float depth;
            protected final float r;
            protected final float g;
            protected final float b;
            protected final float a;

            public Effect(float x0, float y0, float x1, float y1, float depth, float r, float g, float b, float a) {
                this.x0 = x0;
                this.y0 = y0;
                this.x1 = x1;
                this.y1 = y1;
                this.depth = depth;
                this.r = r;
                this.g = g;
                this.b = b;
                this.a = a;
            }
        }
    }
}
