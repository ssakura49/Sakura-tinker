package com.ssakura49.sakuratinker.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.gui.screens.inventory.tooltip.TooltipRenderUtil;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.IForgeGuiGraphics;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Vector2ic;

import java.util.List;
import java.util.stream.Collectors;

@OnlyIn(Dist.CLIENT)
public class STGuiGraphics extends GuiGraphics implements IForgeGuiGraphics {
    public static int BACKGROUND_COLOR = -267386864;
    public static int BORDER_COLOR_TOP = 1347420415;
    public static int BORDER_COLOR_BOTTOM = 1344798847;
    public final Minecraft minecraft;
    public final PoseStack pose;
    public final MultiBufferSource.BufferSource bufferSource;
    public final ItemStack tooltipStack = ItemStack.EMPTY;

    public STGuiGraphics(Minecraft minecraft, PoseStack poseStack, MultiBufferSource.BufferSource bufferSource) {
        super(minecraft, bufferSource);
        this.minecraft = minecraft;
        this.pose = poseStack;
        this.bufferSource = bufferSource;
    }

    public STGuiGraphics(Minecraft minecraft, MultiBufferSource.BufferSource bufferSource) {
        this(minecraft, new PoseStack(), bufferSource);
    }

    public STGuiGraphics(GuiGraphics guiGraphics) {
        this(guiGraphics.minecraft, guiGraphics.pose, guiGraphics.bufferSource);
    }

    public static STGuiGraphics create() {
        return new STGuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
    }

    public static void renderTooltipBackground(STGuiGraphics graphics, float x, float y, float wide, float height, float z) {
        renderTooltipBackground(graphics, x, y, wide, height, z, BACKGROUND_COLOR, BACKGROUND_COLOR, BORDER_COLOR_TOP, BORDER_COLOR_BOTTOM);
    }

    public static void renderTooltipBackground(STGuiGraphics graphics, float x, float y, float wide, float height, float z, float backgroundTop, float backgroundBottom, float borderTop, float borderBottom) {
        float i = x - 3;
        float j = y - 3;
        float k = wide + 3 + 3;
        float l = height + 3 + 3;
        renderHorizontalLine(graphics, i, j - 1, k, z, backgroundTop);
        renderHorizontalLine(graphics, i, j + l, k, z, backgroundBottom);
        renderRectangle0(graphics, i, j, k, l, z, backgroundTop, backgroundBottom);
        renderVerticalLineGradient(graphics, i - 1, j, l, z, backgroundTop, backgroundBottom);
        renderVerticalLineGradient(graphics, i + k, j, l, z, backgroundTop, backgroundBottom);
        renderFrameGradient(graphics, i, j + 1, k, l, z, borderTop, borderBottom);
    }

    public static void renderFrameGradient(STGuiGraphics graphics, float x, float y, float wide, float height, float z, float upColor, float downColor) {
        renderVerticalLineGradient(graphics, x, y, height - 2, z, upColor, downColor);
        renderVerticalLineGradient(graphics, x + wide - 1, y, height - 2, z, upColor, downColor);
        renderHorizontalLine(graphics, x, y - 1, wide, z, upColor);
        renderHorizontalLine(graphics, x, y - 1 + height - 1, wide, z, downColor);
    }

    public static void renderVerticalLine(STGuiGraphics graphics, float x, float y, float height, float pColorFrom, float colorTo) {
        graphics.fill(x, y, x + 1, y + height, pColorFrom, colorTo);
    }

    public static void renderVerticalLineGradient(STGuiGraphics graphics, float x, float y, float height, float z, float pColorFrom, float colorTo) {
        graphics.fillGradient(x, y, x + 1, y + height, z, pColorFrom, colorTo);
    }

    public static void renderHorizontalLine(STGuiGraphics graphics, float x, float y, float wide, float pColorFrom, float colorTo) {
        graphics.fill(x, y, x + wide, y + 1, pColorFrom, colorTo);
    }

    public static void renderRectangle0(STGuiGraphics graphics, float x, float y, float wide, float height, float z, float pColorFrom, float colorTo) {
        graphics.fillGradient(x, y, x + wide, y + height, z, pColorFrom, colorTo);
    }

    @Deprecated
    public static void renderRectangle(GuiGraphics graphics, int x, int y, int wide, int height, int z, int pColorFrom) {
        renderRectangle(graphics, x, y, wide, height, z, pColorFrom, pColorFrom);
    }

    // Forge: Allow specifying a gradient instead of a single color for the background
    public static void renderRectangle(GuiGraphics graphics, int x, int y, int wide, int height, int z, int pColorFrom, int colorTo) {
        graphics.fillGradient(x, y, x + wide, y + height, z, pColorFrom, colorTo);
    }

    /**
     * @deprecated
     */
    @Deprecated
    public void drawManaged(Runnable p_286277_) {
        this.flush();
        p_286277_.run();
        this.flush();
    }

    public int guiWidth() {
        return this.minecraft.getWindow().getGuiScaledWidth();
    }

    public int guiHeight() {
        return this.minecraft.getWindow().getGuiScaledHeight();
    }

    public PoseStack pose() {
        return this.pose;
    }

    public MultiBufferSource.BufferSource bufferSource() {
        return this.bufferSource;
    }

    public void flush() {
        RenderSystem.disableDepthTest();
        this.bufferSource.endBatch();
        RenderSystem.enableDepthTest();
    }

    public void fill(float pMinX, float pMinY, float pMaxX, float pMaxY, float pZ, float pColor) {
        this.fill(RenderType.gui(), pMinX, pMinY, pMaxX, pMaxY, pZ, pColor);
    }

    public void fill(RenderType renderType, float pMinX, float pMinY, float pMaxX, float pMaxY, float pZ, float pColor) {
        Matrix4f matrix4f = this.pose.last().pose();
        float j;
        if (pMinX < pMaxX) {
            j = pMinX;
            pMinX = pMaxX;
            pMaxX = j;
        }

        if (pMinY < pMaxY) {
            j = pMinY;
            pMinY = pMaxY;
            pMaxY = j;
        }

        float f3 = (float) FastColor.ARGB32.alpha((int) pColor) / 255.0F;
        float f = (float) FastColor.ARGB32.red((int) pColor) / 255.0F;
        float f1 = (float) FastColor.ARGB32.green((int) pColor) / 255.0F;
        float f2 = (float) FastColor.ARGB32.blue((int) pColor) / 255.0F;
        VertexConsumer vertexconsumer = this.bufferSource.getBuffer(renderType);
        vertexconsumer.vertex(matrix4f, pMinX, pMinY, pZ).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, pMinX, pMaxY, pZ).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, pMaxX, pMaxY, pZ).color(f, f1, f2, f3).endVertex();
        vertexconsumer.vertex(matrix4f, pMaxX, pMinY, pZ).color(f, f1, f2, f3).endVertex();
        this.flushIfUnmanaged();
    }

    public void fillGradient(float pX1, float pY1, float pX2, float pY2, float pZ, float pColorFrom, float pColorTo) {
        this.fillGradient(RenderType.gui(), pX1, pY1, pX2, pY2, pColorFrom, pColorTo, pZ);
    }

    public void fillGradient(RenderType renderType, float pX1, float pY1, float pX2, float pY2, float pColorFrom, float pColorTo, float pZ) {
        VertexConsumer vertexconsumer = this.bufferSource.getBuffer(renderType);
        this.fillGradient(vertexconsumer, pX1, pY1, pX2, pY2, pZ, pColorFrom, pColorTo);
        this.flushIfUnmanaged();
    }

    public void fillGradient(VertexConsumer p_286862_, float pX1, float pY1, float pX2, float pY2, float pZ, float pColorFrom, float pColorTo) {
        float f = (float) FastColor.ARGB32.alpha((int) pColorFrom) / 255.0F;
        float f1 = (float) FastColor.ARGB32.red((int) pColorFrom) / 255.0F;
        float f2 = (float) FastColor.ARGB32.green((int) pColorFrom) / 255.0F;
        float f3 = (float) FastColor.ARGB32.blue((int) pColorFrom) / 255.0F;
        float f4 = (float) FastColor.ARGB32.alpha((int) pColorTo) / 255.0F;
        float f5 = (float) FastColor.ARGB32.red((int) pColorTo) / 255.0F;
        float f6 = (float) FastColor.ARGB32.green((int) pColorTo) / 255.0F;
        float f7 = (float) FastColor.ARGB32.blue((int) pColorTo) / 255.0F;
        Matrix4f matrix4f = this.pose.last().pose();
        p_286862_.vertex(matrix4f, pX1, pY1, pZ).color(f1, f2, f3, f).endVertex();
        p_286862_.vertex(matrix4f, pX1, pY2, pZ).color(f5, f6, f7, f4).endVertex();
        p_286862_.vertex(matrix4f, pX2, pY2, pZ).color(f5, f6, f7, f4).endVertex();
        p_286862_.vertex(matrix4f, pX2, pY1, pZ).color(f1, f2, f3, f).endVertex();
    }

    public void renderTooltip(Font font, Component component, int x, int y, int background, int borderStart, int borderEnd) {
        this.renderTooltip(font, List.of(component.getVisualOrderText()), x, y, background, borderStart, borderEnd);
    }

    public void renderTooltip(Font font, List<? extends FormattedCharSequence> list, int x, int y, int background, int borderStart, int borderEnd) {
        this.renderTooltipInternal(font, list.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), x, y, DefaultTooltipPositioner.INSTANCE, background, borderStart, borderEnd);
    }

    public void renderTooltip(Font font, List<FormattedCharSequence> list, ClientTooltipPositioner positioner, int x, int y, int background, int borderStart, int borderEnd) {
        this.renderTooltipInternal(font, list.stream().map(ClientTooltipComponent::create).collect(Collectors.toList()), x, y, positioner, background, borderStart, borderEnd);
    }

    public void renderTooltipInternal(Font font, List<ClientTooltipComponent> list, int x, int y, ClientTooltipPositioner positioner, int background, int borderStart, int borderEnd) {
        if (!list.isEmpty()) {
            int i = 0;
            int j = list.size() == 1 ? -2 : 0;

            for (ClientTooltipComponent clienttooltipcomponent : list) {
                int k = clienttooltipcomponent.getWidth(font);
                if (k > i) {
                    i = k;
                }

                j += clienttooltipcomponent.getHeight();
            }

            int i2 = i;
            int j2 = j;
            Vector2ic vector2ic = positioner.positionTooltip(this.guiWidth(), this.guiHeight(), x, y, i2, j2);
            int l = vector2ic.x();
            int i1 = vector2ic.y();
            this.pose.pushPose();
            int j1 = 400;
            this.drawManaged(() -> {
                net.minecraftforge.client.event.RenderTooltipEvent.Color colorEvent = net.minecraftforge.client.ForgeHooksClient.onRenderTooltipColor(this.tooltipStack, new GuiGraphics(minecraft, bufferSource), l, i1, font, list);
                TooltipRenderUtil.renderTooltipBackground(new GuiGraphics(minecraft, bufferSource), l, i1, i2, j2, 400, background, background, borderStart, borderEnd);
            });
            this.pose.translate(0.0F, 0.0F, 400.0F);
            int k1 = i1;

            for (int l1 = 0; l1 < list.size(); ++l1) {
                ClientTooltipComponent clienttooltipcomponent1 = list.get(l1);
                clienttooltipcomponent1.renderText(font, l, k1, this.pose.last().pose(), this.bufferSource);
                k1 += clienttooltipcomponent1.getHeight() + (l1 == 0 ? 2 : 0);
            }

            k1 = i1;

            for (int k2 = 0; k2 < list.size(); ++k2) {
                ClientTooltipComponent clienttooltipcomponent2 = list.get(k2);
                clienttooltipcomponent2.renderImage(font, l, k1, new GuiGraphics(minecraft, bufferSource));
                k1 += clienttooltipcomponent2.getHeight() + (k2 == 0 ? 2 : 0);
            }

            this.pose.popPose();
        }
    }


    public void blit(int pX, int pY, int pBlitOffset, int pWidth, int pHeight, TextureAtlasSprite pSprite) {
        this.innerBlit(pSprite.atlasLocation(), pX, pX + pWidth, pY, pY + pHeight, pBlitOffset, pSprite.getU0(), pSprite.getU1(), pSprite.getV0(), pSprite.getV1());
    }

    public void blit(int pX, int pY, int pBlitOffset, int pWidth, int pHeight, TextureAtlasSprite pSprite, float pRed, float pGreen, float pBlue, float pAlpha) {
        this.innerBlit(pSprite.atlasLocation(), pX, pX + pWidth, pY, pY + pHeight, pBlitOffset, pSprite.getU0(), pSprite.getU1(), pSprite.getV0(), pSprite.getV1(), pRed, pGreen, pBlue, pAlpha);
    }

    public void renderOutline(int pX, int pY, int pWidth, int pHeight, int pColor) {
        this.fill(pX, pY, pX + pWidth, pY + 1, pColor);
        this.fill(pX, pY + pHeight - 1, pX + pWidth, pY + pHeight, pColor);
        this.fill(pX, pY + 1, pX + 1, pY + pHeight - 1, pColor);
        this.fill(pX + pWidth - 1, pY + 1, pX + pWidth, pY + pHeight - 1, pColor);
    }

    public void blit(ResourceLocation pAtlasLocation, int pX, int pY, int pUOffset, int pVOffset, int pUWidth, int pVHeight) {
        this.blit(pAtlasLocation, pX, pY, 0, (float)pUOffset, (float)pVOffset, pUWidth, pVHeight, 256, 256);
    }

    public void blit(ResourceLocation pAtlasLocation, int pX, int pY, int pBlitOffset, float pUOffset, float pVOffset, int pUWidth, int pVHeight, int pTextureWidth, int pTextureHeight) {
        this.blit(pAtlasLocation, pX, pX + pUWidth, pY, pY + pVHeight, pBlitOffset, pUWidth, pVHeight, pUOffset, pVOffset, pTextureWidth, pTextureHeight);
    }

    public void blit(ResourceLocation pAtlasLocation, int pX, int pY, int pWidth, int pHeight, float pUOffset, float pVOffset, int pUWidth, int pVHeight, int pTextureWidth, int pTextureHeight) {
        this.blit(pAtlasLocation, pX, pX + pWidth, pY, pY + pHeight, 0, pUWidth, pVHeight, pUOffset, pVOffset, pTextureWidth, pTextureHeight);
    }

    public void blit(ResourceLocation pAtlasLocation, int pX, int pY, float pUOffset, float pVOffset, int pWidth, int pHeight, int pTextureWidth, int pTextureHeight) {
        this.blit(pAtlasLocation, pX, pY, pWidth, pHeight, pUOffset, pVOffset, pWidth, pHeight, pTextureWidth, pTextureHeight);
    }

    public void blit(ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, int pUWidth, int pVHeight, float pUOffset, float pVOffset, int pTextureWidth, int pTextureHeight) {
        this.innerBlit(pAtlasLocation, pX1, pX2, pY1, pY2, pBlitOffset, (pUOffset + 0.0F) / (float)pTextureWidth, (pUOffset + (float)pUWidth) / (float)pTextureWidth, (pVOffset + 0.0F) / (float)pTextureHeight, (pVOffset + (float)pVHeight) / (float)pTextureHeight);
    }

    public void innerBlit(ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV) {
        RenderSystem.setShaderTexture(0, pAtlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        Matrix4f matrix4f = this.pose.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY1, (float)pBlitOffset).uv(pMinU, pMinV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY2, (float)pBlitOffset).uv(pMinU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY2, (float)pBlitOffset).uv(pMaxU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY1, (float)pBlitOffset).uv(pMaxU, pMinV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    public void innerBlit(ResourceLocation pAtlasLocation, int pX1, int pX2, int pY1, int pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV, float pRed, float pGreen, float pBlue, float pAlpha) {
        RenderSystem.setShaderTexture(0, pAtlasLocation);
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = this.pose.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY1, (float)pBlitOffset).color(pRed, pGreen, pBlue, pAlpha).uv(pMinU, pMinV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX1, (float)pY2, (float)pBlitOffset).color(pRed, pGreen, pBlue, pAlpha).uv(pMinU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY2, (float)pBlitOffset).color(pRed, pGreen, pBlue, pAlpha).uv(pMaxU, pMaxV).endVertex();
        bufferbuilder.vertex(matrix4f, (float)pX2, (float)pY1, (float)pBlitOffset).color(pRed, pGreen, pBlue, pAlpha).uv(pMaxU, pMinV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }

    public void drawCenteredString(Font pFont, Component pText, int pX, int pY, int pColor) {
        FormattedCharSequence formattedcharsequence = pText.getVisualOrderText();
        this.drawString(pFont, formattedcharsequence, pX - pFont.width(formattedcharsequence) / 2, pY, pColor);
    }

    public int drawString(Font pFont, @Nullable String pText, int pX, int pY, int pColor) {
        return this.drawString(pFont, pText, pX, pY, pColor, true);
    }

    public void drawCenteredString(Font pFont, FormattedCharSequence pText, int pX, int pY, int pColor) {
        this.drawString(pFont, pText, pX - pFont.width(pText) / 2, pY, pColor);
    }

    public int drawString(Font pFont, @Nullable String pText, float pX, float pY, int pColor) {
        return this.drawString(pFont, pText, pX, pY, pColor, true);
    }

    public int drawString(Font pFont, @Nullable String pText, int pX, int pY, int pColor, boolean pDropShadow) {
        return this.drawString(pFont, pText, (float)pX, (float)pY, pColor, pDropShadow);
    }

    public void drawCenteredString8x8(Font pFont, String pText, float pX, float pY, int pColor, int out) {
        this.drawString8x8(pFont, pText, pX - pFont.width(pText) / 2F, pY, pColor, out);
    }

    public void drawString8x8(Font pFont, @Nullable String pText, int pX, int pY, int pColor, int out) {
        this.drawString8x8(pFont, pText, (float) pX, (float) pY, pColor, out);
    }

    public void drawString8x8(Font pFont, @Nullable String pText, float pX, float pY, int pColor, int out) {
        if (pText != null) {
            pFont.drawInBatch8xOutline(FormattedCharSequence.forward(pText, Style.EMPTY), pX, pY, out, pColor, this.pose.last().pose(), this.bufferSource, 15728880);
            this.flushIfUnmanaged();
        }
    }
}
