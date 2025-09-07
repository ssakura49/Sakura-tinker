package com.ssakura49.sakuratinker.client.component;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.AtomicDouble;
import com.mojang.blaze3d.font.GlyphInfo;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.library.interfaces.textcolor.TextColorInterface;
import com.ssakura49.sakuratinker.render.STRenderBuffers;
import com.ssakura49.sakuratinker.render.shader.cosmic.CosmicItemShaders;
import com.ssakura49.sakuratinker.utils.math.MathUtils;
import com.ssakura49.sakuratinker.utils.time.TimeContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.FontManager;
import net.minecraft.client.gui.font.FontSet;
import net.minecraft.client.gui.font.glyphs.BakedGlyph;
import net.minecraft.client.gui.font.glyphs.EmptyGlyph;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Matrix4f;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class MinecraftFont extends Font {
    public static FontManager fontManager = Minecraft.getInstance().fontManager;
    public static MinecraftFont INSTANCE;

    static {
        INSTANCE = new MinecraftFont((location) -> {
            return fontManager.fontSets.getOrDefault(fontManager.renames.getOrDefault(location, location), fontManager.missingFontSet);
        }, false, fontManager.fontSets);

    }

    public MinecraftFont(Function<ResourceLocation, FontSet> pFonts, boolean pFilterFishyGlyphs, Map<ResourceLocation, FontSet> f) {
        super(pFonts, pFilterFishyGlyphs);
    }

    public int drawInBatch(FormattedCharSequence pText, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pMatrix, MultiBufferSource pBuffer, Font.DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLightCoords) {
        return this.drawInternal(pText, pX, pY, pColor, pDropShadow, pMatrix, pBuffer, pDisplayMode, pBackgroundColor, pPackedLightCoords);
    }

    public int drawInternal(FormattedCharSequence pText, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pMatrix, MultiBufferSource pBuffer, Font.DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLightCoords) {
        pColor = adjustColor(pColor);
        Matrix4f matrix4f = new Matrix4f(pMatrix);
        if (pDropShadow) {
            this.renderText(pText, pX, pY, pColor, true, pMatrix, pBuffer, pDisplayMode, pBackgroundColor, pPackedLightCoords);
            matrix4f.translate(SHADOW_OFFSET);
        }

        pX = this.renderText(pText, pX, pY, pColor, false, matrix4f, pBuffer, pDisplayMode, pBackgroundColor, pPackedLightCoords);
        return (int) pX + (pDropShadow ? 1 : 0);
    }

    public float renderText(FormattedCharSequence pText, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pMatrix, MultiBufferSource pBuffer, Font.DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLightCoords) {
        MinecraftFont.StringRenderOutputVanilla font$stringrenderoutput = new MinecraftFont.StringRenderOutputVanilla(pBuffer, pX, pY, pColor, pDropShadow, pMatrix, pDisplayMode, pPackedLightCoords);
        pText.accept(font$stringrenderoutput);
        return font$stringrenderoutput.finish(pBackgroundColor, pX);
    }

    class StringRenderOutputVanilla extends StringRenderOutput {
        final MultiBufferSource bufferSource;
        private final boolean dropShadow;
        private final float dimFactor;
        private final float r;
        private final float g;
        private final float b;
        private final float a;
        private final Matrix4f pose;
        private final Font.DisplayMode mode;
        private final int packedLightCoords;
        float x;
        float y;
        @Nullable
        private List<BakedGlyph.Effect> effects;
        private CharStyle lastStyle = CharStyle.NONE;
        private VertexConsumer vertexConsumer = STRenderBuffers.getBufferSource().getBuffer(CosmicItemShaders.COSMIC_RENDER_TYPE);
        public StringRenderOutputVanilla(MultiBufferSource pBufferSource, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pPose, Font.DisplayMode pMode, int pPackedLightCoords) {
            super(pBufferSource, pX, pY, pColor, pDropShadow, pPose, pMode, pPackedLightCoords);
            this.bufferSource = pBufferSource;
            this.x = pX;
            this.y = pY;
            this.dropShadow = pDropShadow;
            this.dimFactor = pDropShadow ? 0.25F : 1.0F;
            this.r = (float) (pColor >> 16 & 255) / 255.0F * this.dimFactor;
            this.g = (float) (pColor >> 8 & 255) / 255.0F * this.dimFactor;
            this.b = (float) (pColor & 255) / 255.0F * this.dimFactor;
            this.a = (float) (pColor >> 24 & 255) / 255.0F;
            this.pose = pPose;
            this.mode = pMode;
            this.packedLightCoords = pPackedLightCoords;
        }

        public void addEffect(BakedGlyph.Effect pEffect) {
            if (this.effects == null) {
                this.effects = Lists.newArrayList();
            }

            this.effects.add(pEffect);
        }

        public boolean accept(int pPositionInCurrentSequence, Style style, int pCodePoint) {
            FontSet fontset = MinecraftFont.this.getFontSet(style.getFont());
            GlyphInfo glyphinfo = fontset.getGlyphInfo(pCodePoint, MinecraftFont.this.filterFishyGlyphs);
            BakedGlyph bakedglyph = style.isObfuscated() && pCodePoint != 32 ? fontset.getRandomGlyph(glyphinfo) : fontset.getGlyph(pCodePoint);
            boolean flag = style.isBold();
            float f3 = this.a;
            TextColor textcolor = style.getColor();
            float f;
            float f1;
            float f2;
            if (textcolor != null) {
                int i = textcolor.getValue();
                f = (float) (i >> 16 & 255) / 255.0F * this.dimFactor;
                f1 = (float) (i >> 8 & 255) / 255.0F * this.dimFactor;
                f2 = (float) (i & 255) / 255.0F * this.dimFactor;
            } else {
                f = this.r;
                f1 = this.g;
                f2 = this.b;
            }
            if (!(bakedglyph instanceof EmptyGlyph)) {
                float f5 = flag ? glyphinfo.getBoldOffset() : 0.0F;
                float f4 = this.dropShadow ? glyphinfo.getShadowOffset() : 0.0F;
                TextColorInterface codeChecker = (TextColorInterface) (Object) textcolor;
                char code = codeChecker == null ? ' ' : codeChecker.st$getCode();
                AtomicDouble atomicX = new AtomicDouble(x);
                VertexConsumer vertexconsumer = this.bufferSource.getBuffer(bakedglyph.renderType(this.mode));
                if (code == ChatFormattingContext.ST_COSMIC_CODE) {
                    vertexConsumer = STRenderBuffers.getBufferSource().getBuffer(CosmicItemShaders.COSMIC_RENDER_TYPE);
                    float alpha = Mth.abs(MathUtils.sin(TimeContext.Client.getCommonDegrees() / 20F) * 0.5F) + 0.11F - .6F + a;
                    if (alpha < 0F) alpha = 0F;
                    if (alpha > 1F) alpha = 1F;
                    int col = FastColor.ARGB32.color((int) (255 * alpha), 53, 63, 81);
                    f = (float) (col >> 16 & 255) / 255.0F * this.dimFactor;
                    f1 = (float) (col >> 8 & 255) / 255.0F * this.dimFactor;
                    f2 = (float) (col & 255) / 255.0F * this.dimFactor;
                    MinecraftFont.this.renderChar(bakedglyph, flag, style.isItalic(), f5, this.x + f4, this.y + f4, this.pose, vertexconsumer, f, f1, f2, alpha, this.packedLightCoords);
                    Minecraft.getInstance().renderBuffers.bufferSource().endBatch();
                    CosmicItemShaders.cosmicExternalScale.set(25F);
                    MinecraftFont.this.renderChar0(bakedglyph, flag, style.isItalic(), f5, this.x + f4, this.y + f4, this.pose, vertexConsumer, 1.0F, 1.0F, 1.0F, alpha, 0xF000F0);
                    CosmicItemShaders.updateShaderData(ItemDisplayContext.THIRD_PERSON_LEFT_HAND);
                    STRenderBuffers.getBufferSource().endBatch();
                    lastStyle = CharStyle.ST_COSMIC;
                } else {
                    if (code == ChatFormattingContext.ST_ORIGIN_CODE) {
                        style = style.withColor(adjustColor(0x596460));
                        textcolor = style.getColor();
                        if (textcolor != null) {
                            int i = textcolor.getValue();
                            float r = (float) (i >> 16 & 255) / 255.0F * this.dimFactor;
                            float g = (float) (i >> 8 & 255) / 255.0F * this.dimFactor;
                            float b = (float) (i & 255) / 255.0F * this.dimFactor;
                            for (int j = -1; j <= 1; ++j) {
                                for (int k = -1; k <= 1; ++k) {
                                    if (j != 0 || k != 0) {
                                        float x8x = atomicX.floatValue() + (float) j * glyphinfo.getShadowOffset();
                                        float y8x = y + (float) k * glyphinfo.getShadowOffset();
                                        MinecraftFont.this.renderChar(bakedglyph, flag, style.isItalic(), f5, x8x + f4, y8x + f4, this.pose, vertexconsumer, r, g, b, f3, this.packedLightCoords);
                                    }
                                }
                            }
                        }
                        lastStyle = CharStyle.ST_ORIGIN;
                    }
                    else if (code == ChatFormattingContext.ST_DP_CODE) {
                        int colorDark = FastColor.ARGB32.color((int) (a * 255), (int) (107 * 1F), (int) (30 * 1F), (int) (189 * 1F));

                        style = style.withColor(adjustColor(colorDark));
                        textcolor = style.getColor();
                        if (textcolor != null) {
                            int i = textcolor.getValue();
                            float r = (float) (i >> 16 & 255) / 255.0F * this.dimFactor;
                            float g = (float) (i >> 8 & 255) / 255.0F * this.dimFactor;
                            float b = (float) (i & 255) / 255.0F * this.dimFactor;
                            for (int j = -1; j <= 1; ++j) {
                                for (int k = -1; k <= 1; ++k) {
                                    if (j != 0 || k != 0) {
                                        float x8x = atomicX.floatValue() + (float) j * glyphinfo.getShadowOffset();
                                        float y8x = y + (float) k * glyphinfo.getShadowOffset();
                                        MinecraftFont.this.renderChar(bakedglyph, flag, style.isItalic(), f5, x8x + f4, y8x + f4, this.pose, vertexconsumer, r, g, b, f3, this.packedLightCoords);
                                    }
                                }
                            }
                        }
                        lastStyle = CharStyle.ST_DP;
                    }
                    else lastStyle = CharStyle.NONE;
                    MinecraftFont.this.renderChar(bakedglyph, flag, style.isItalic(), f5, this.x + f4, this.y + f4, this.pose, vertexconsumer, f, f1, f2, f3, this.packedLightCoords);
                }
            }

            float f6 = glyphinfo.getAdvance(flag);
            float f7 = this.dropShadow ? 1.0F : 0.0F;
            if (style.isStrikethrough()) {
                this.addEffect(new BakedGlyph.Effect(this.x + f7 - 1.0F, this.y + f7 + 4.5F, this.x + f7 + f6, this.y + f7 + 4.5F - 1.0F, 0.01F, f, f1, f2, f3));
            }

            if (style.isUnderlined()) {
                this.addEffect(new BakedGlyph.Effect(this.x + f7 - 1.0F, this.y + f7 + 9.0F, this.x + f7 + f6, this.y + f7 + 9.0F - 1.0F, 0.01F, f, f1, f2, f3));
            }

            this.x += f6;
            return true;
        }

        public float finish(int pBackgroundColor, float pX) {
            if (pBackgroundColor != 0) {
                float f = (float) (pBackgroundColor >> 24 & 255) / 255.0F;
                float f1 = (float) (pBackgroundColor >> 16 & 255) / 255.0F;
                float f2 = (float) (pBackgroundColor >> 8 & 255) / 255.0F;
                float f3 = (float) (pBackgroundColor & 255) / 255.0F;
                this.addEffect(new BakedGlyph.Effect(pX - 1.0F, this.y + 9.0F, this.x + 1.0F, this.y - 1.0F, 0.01F, f1, f2, f3, f));
            }

            if (this.effects != null) {
                BakedGlyph bakedglyph = MinecraftFont.this.getFontSet(Style.DEFAULT_FONT).whiteGlyph();
                VertexConsumer vertexconsumer = this.bufferSource.getBuffer(bakedglyph.renderType(this.mode));

                for (BakedGlyph.Effect bakedglyph$effect : this.effects) {
                    bakedglyph.renderEffect(bakedglyph$effect, this.pose, vertexconsumer, this.packedLightCoords);
                }
            }

            return this.x;
        }

        public CharStyle getLastStyle() {
            return lastStyle;
        }
    }
    public void renderChar0(BakedGlyph bakedGlyph, boolean flag, boolean pItalic, float f5, float pX, float pY, Matrix4f pMatrix, VertexConsumer pBuffer, float pRed, float pGreen, float pBlue, float pAlpha, int pPackedLight) {
        bakedGlyph = STFont.STBakedGlyph.createByEmpty(bakedGlyph);
        bakedGlyph.render(pItalic, pX, pY, pMatrix, pBuffer, pRed, pGreen, pBlue, pAlpha, pPackedLight);
        if (flag) {
            bakedGlyph.render(pItalic, pX + f5, pY, pMatrix, pBuffer, pRed, pGreen, pBlue, pAlpha, pPackedLight);
        }
    }
    public enum CharStyle {
        NONE,
        ST_DP,
        ST_COSMIC,
        ST_ORIGIN
    }
}
