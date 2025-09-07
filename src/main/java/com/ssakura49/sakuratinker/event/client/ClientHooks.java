package com.ssakura49.sakuratinker.event.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.client.component.FontTextBuilder;
import com.ssakura49.sakuratinker.client.component.STFont;
import com.ssakura49.sakuratinker.client.particles.LightningEffect;
import com.ssakura49.sakuratinker.client.particles.LightningParticleOptions;
import com.ssakura49.sakuratinker.event.custom.STRenderTooltipEvent;
import com.ssakura49.sakuratinker.event.event.client.ClientModHandler;
import com.ssakura49.sakuratinker.library.interfaces.item.FadedTooltipExpends;
import com.ssakura49.sakuratinker.mixin.ClientTextTooltipAccessor;
import com.ssakura49.sakuratinker.render.RendererUtils;
import com.ssakura49.sakuratinker.render.STGuiGraphics;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import com.ssakura49.sakuratinker.utils.math.MathUtils;
import com.ssakura49.sakuratinker.utils.render.ColorUtil;
import com.ssakura49.sakuratinker.utils.time.TimeContext;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2i;
import org.joml.Vector2ic;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

/**
 * GUI内z深度越大离屏幕越近
 */
public class ClientHooks {
    public static boolean isSTItemTooltipNow;
    public static int lastTick;
    public static int tick;
    public static void tooltipOlSpItem(STRenderTooltipEvent.Post event) {
        try {
            GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource());
            PoseStack stack = guiGraphics.pose;
            int i = 0;
            int j = event.getComponents().size() == 1 ? -2 : 0;
            for (ClientTooltipComponent clienttooltipcomponent : event.getComponents()) {
                int k = clienttooltipcomponent.getWidth(event.getFont());
                if (k > i)
                    i = k;
                j += clienttooltipcomponent.getHeight();
            }
            int maxTextWidth = i;
            int totalTextHeight = j;
            Vector2ic vector2ic = event.positioner.positionTooltip(event.getGraphics().guiWidth(), event.getGraphics().guiHeight(), event.getX(), event.getY(), maxTextWidth, totalTextHeight);

            {
                Vector2i color = TimeContext.Client.getBorderColor();
                STGuiGraphics.renderTooltipBackground(new STGuiGraphics(event.getGraphics()), vector2ic.x(), vector2ic.y(), maxTextWidth, j - 1, 399.5F, 0xf0100010, 0xf0100010, color.x, color.y);
            }

            stack.pushPose();
            stack.translate(vector2ic.x() + maxTextWidth / 1.5F, vector2ic.y() + totalTextHeight / 2F, 400);
            stack.scale(2.5f * maxTextWidth / 100, 2.5f * maxTextWidth / 100, 2.5f * maxTextWidth / 100);
            stack.mulPose(Axis.ZP.rotationDegrees(RendererUtils.getRenderRotation()));
            Vector4f color1 = TimeContext.Both.rainbowV4(4936.0F, 0.3F, 2.0F);
//            RendererUtils.renderStar(stack, guiGraphics.bufferSource(), new Vector4f(color1.x, color1.y, color1.z, 1F), false);
            stack.popPose();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    static String CTCtoString(ClientTooltipComponent component) {
        if (component instanceof ClientTextTooltip)
            return FontTextBuilder.formattedCharSequenceToString(((ClientTextTooltipAccessor) component).text());
        return "";
    }

    public static int STTooltipExpends(ItemStack stack, ClientTooltipComponent component, List<ClientTooltipComponent> list) {
        int expend = 0;
        if (stack.getItem() instanceof FadedTooltipExpends expends)
            expend = expends.tooltips();
        return expend;
    }

    public static int STTooltipBaseExpends(ItemStack stack, List<ClientTooltipComponent> list) {
        int expend = 2;
        if (stack.getItem() instanceof FadedTooltipExpends expends) {
            if (expends.crossFadedCheck()) return 1 + expends.expends();
            for (int i = 0; i < list.size(); i++) {
                if (STFont.doCosmicRendering(CTCtoString(list.get(i)))) {
                    expend = i + expends.expends();
                    break;
                }
            }
        }
        return expend;
    }

    public static void tooltipSTItem(STRenderTooltipEvent.PrePre event) {
        int i = 0;
        int j = event.getComponents().size() == 1 ? -2 : 0;
        int j_ = j;
        int times = 0;
        int expend = STTooltipBaseExpends(event.getItemStack(), event.getComponents());
        for (ClientTooltipComponent clienttooltipcomponent : event.getComponents()) {
            int k = clienttooltipcomponent.getWidth(event.getFont());
            if (k > i)
                i = k;
            j += clienttooltipcomponent.getHeight();
            if (times < (expend + (Screen.hasShiftDown() ? STTooltipExpends(event.getItemStack(), clienttooltipcomponent, event.getComponents()) : 0))) {
                times++;
                j_ += clienttooltipcomponent.getHeight();
            }
        }
        int i2 = i;
        int j2 = j;
        int j3 = j_;
        Vector2ic vector2ic = event.getTooltipPositioner().positionTooltip(event.getScreenWidth(), event.getScreenHeight(), event.getX(), event.getY(), i2, j2);
        PoseStack stack = event.getPoseStack();
        float posX = vector2ic.x() + i2 / 2F - 4;
        float posY = vector2ic.y() + j2 / 2F;
        float scale = 32F;
        float rp7sidesWidth = 5F;
        int scissorX = (int) (posX-rp7sidesWidth*scale) - 1;
        int scissorY = (int) (posY-rp7sidesWidth*scale) - 1;
        float percent = Mth.lerp(TimeContext.Client.alwaysPartial(), lastTick, tick) / 20.0F;
        STGuiGraphics myGuiGraphics = new STGuiGraphics(event.getGraphics());
        myGuiGraphics.enableScissor(0, 0, (int) ((event.getScreenWidth() - scissorX) * percent + scissorX), (int) ((event.getScreenHeight() - scissorY)*percent + scissorY));
        myGuiGraphics.drawManaged(() -> {
            {
                Vector2i color = TimeContext.Client.getBorderColor();
                STGuiGraphics.renderTooltipBackground(myGuiGraphics, vector2ic.x(), vector2ic.y(), i2, j3 - 1, 399.5F, 0xf0100010, 0xf0100010, color.x, color.y);
            }

            float rotation = (Util.getMillis()) / 39.99F;

            //big triangle
            stack.pushPose();
            stack.translate(posX, posY, 399.2F);
            stack.scale(scale, scale, scale);
            stack.mulPose(Axis.ZP.rotationDegrees(rotation));
            RendererUtils.renderRegularPolygon(stack, Minecraft.getInstance().renderBuffers.bufferSource(), 3.0F, 3, 3.0F, 1, 1F, 1F, 1F, 1F, STRenderType.END_PORTAL_TRANSLUCENT(RendererUtils.star2), false);
            stack.popPose();

            //small triangle 1
            stack.pushPose();
            stack.translate(posX - 109, posY + 15, 399.2F);
            stack.scale(8, 15, 2);
            stack.mulPose(Axis.XP.rotationDegrees(rotation));
            stack.mulPose(Axis.YP.rotationDegrees(rotation * 0.4F));
            RendererUtils.renderRegularPolygon(stack, Minecraft.getInstance().renderBuffers.bufferSource(), 3.0F, 3, 3.0F, 1, 1F, 1F, 1F, 1F, STRenderType.END_PORTAL(RendererUtils.star2), false);
            stack.popPose();

            //small triangle 2
            stack.pushPose();
            stack.translate(posX + 109, posY - 25, 399.2F);
            stack.scale(19, 10, 2);
            stack.mulPose(Axis.XP.rotationDegrees(rotation * 0.7F));
            stack.mulPose(Axis.YP.rotationDegrees(rotation * 1.1F));
            RendererUtils.renderRegularPolygon(stack, Minecraft.getInstance().renderBuffers.bufferSource(), 3.0F, 3, 3.0F, 1, 1F, 1F, 1F, 1F, STRenderType.END_PORTAL(RendererUtils.star2), false);
            stack.popPose();

            //xyz rotation 7 sides
            stack.pushPose();
            stack.translate(posX, posY, 399.1F);
            stack.scale(scale, scale, scale);
            stack.mulPose(Axis.ZP.rotationDegrees((float) (rotation * (1F + Math.PI * 0.15F))));
            stack.mulPose(Axis.YP.rotationDegrees((float) (rotation * (1F + Math.PI * 0.15F))));
            stack.mulPose(Axis.XP.rotationDegrees((float) (rotation * (1F + Math.PI * 0.15F))));
            RendererUtils.renderRegularPolygon(stack, Minecraft.getInstance().renderBuffers.bufferSource(), 5.0F, 7, 0.7F, 1, 1F, 1F, 1F, 1F, STRenderType.END_PORTAL(RendererUtils.star1), false);
            stack.popPose();
            {
                STGuiGraphics.renderTooltipBackground(myGuiGraphics, vector2ic.x()+3, vector2ic.y()+j3+6+2, i2-6, j2-j3-3, 399.5F, 0x30101010, 0x30101010, 0, 0);
            }
        });
        myGuiGraphics.disableScissor();
    }

    public static BufferBuilder.RenderedBuffer drawStars(BufferBuilder builder, CallbackInfoReturnable<BufferBuilder.RenderedBuffer> cir) {
        RandomSource randomsource = RandomSource.create(10842L);
        RenderSystem.setShaderTexture(0, RendererUtils.star1);
        RenderSystem.setShader(GameRenderer::getRendertypeEndPortalShader);
        builder.begin(VertexFormat.Mode.TRIANGLE_STRIP, DefaultVertexFormat.POSITION_COLOR_TEX);

        for (int i = 0; i < 1500; ++i) {
            double d0 = randomsource.nextFloat() * 2.0F - 1.0F;
            double d1 = randomsource.nextFloat() * 2.0F - 1.0F;
            double d2 = randomsource.nextFloat() * 2.0F - 1.0F;
            double d3 = 0.15F + randomsource.nextFloat() * 0.1F;
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            if (d4 < 1.0D && d4 > 0.01D) {
                d4 = 1.0D / Math.sqrt(d4);
                d0 *= d4;
                d1 *= d4;
                d2 *= d4;
                double d5 = d0 * 100.0D;
                double d6 = d1 * 100.0D;
                double d7 = d2 * 100.0D;
                double d8 = Math.atan2(d0, d2);
                double d9 = MathUtils.sin(d8);
                double d10 = MathUtils.cos(d8);
                double d11 = Math.atan2(Math.sqrt(d0 * d0 + d2 * d2), d1);
                double d12 = MathUtils.sin(d11);
                double d13 = MathUtils.cos(d11);
                double d14 = randomsource.nextDouble() * Math.PI * 2.0D;
                double d15 = MathUtils.sin(d14);
                double d16 = MathUtils.cos(d14);

                for (int j = 0; j < 4; ++j) {
                    double d17 = 0.0D;
                    double d18 = (double) ((j & 2) - 1) * d3;
                    double d19 = (double) ((j + 1 & 2) - 1) * d3;
                    double d20 = 0.0D;
                    double d21 = d18 * d16 - d19 * d15;
                    double d22 = d19 * d16 + d18 * d15;
                    double d23 = d21 * d12 + 0.0D * d13;
                    double d24 = 0.0D * d12 - d21 * d13;
                    double d25 = d24 * d9 - d22 * d10;
                    double d26 = d22 * d9 + d24 * d10;
                    builder.vertex(d5 + d25, d6 + d23, d7 + d26)
                            .color(randomsource.nextInt(255), randomsource.nextInt(255), randomsource.nextInt(255), randomsource.nextInt(255))
                            .uv(1F, 1F)
                            .endVertex();
                }
            }
        }
        return (builder.end());
    }

    public static <T extends LivingEntity> void afterLevelBeforeRenderStackModify(PoseStack stack, LivingEntityRenderer<T, ?> renderer, float partial, boolean crouching, T living) {
        if (living.hasPose(Pose.SLEEPING)) {
            Direction direction = living.getBedOrientation();
            if (direction != null) {
                float f4 = living.getEyeHeight(Pose.STANDING) - 0.1F;
                stack.translate((float) (-direction.getStepX()) * f4, 0.0F, (float) (-direction.getStepZ()) * f4);
            }
        }
        renderer.setupRotations(living, stack, renderer.getBob(living, partial), Mth.lerp(partial, living.yBodyRotO, living.yBodyRot), partial);
        stack.scale(-1.0F, -1.0F, 1.0F);
        renderer.scale(living, stack, partial);
        stack.translate(0.0F, -1.501F, 0.0F);

        if (crouching) {
            stack.mulPose(Axis.XP.rotation(.55F));
        }
    }


    public static void shock(Vec3 vectorStart, Vec3 vectorEnd, ColorUtil colorUtil, int lifespan) {
        if (Minecraft.getInstance().level == null){
            return;
        }
        LightningEffect.INSTANCE.add(Minecraft.getInstance().level, new LightningParticleOptions(LightningParticleOptions.BoltRenderInfo.shock(colorUtil), vectorStart, vectorEnd, lifespan).size(0.04F), ClientModHandler.TickEventsHandler.PARTIAL_TICK);
    }

    public static void thunderBolt(Vec3 vectorStart, Vec3 vectorEnd, ColorUtil colorUtil, int lifespan) {
        if (Minecraft.getInstance().level == null){
            return;
        }
        LightningEffect.INSTANCE.add(Minecraft.getInstance().level, new LightningParticleOptions(LightningParticleOptions.BoltRenderInfo.thunderBolt(colorUtil), vectorStart, vectorEnd, lifespan).size(0.25F), ClientModHandler.TickEventsHandler.PARTIAL_TICK);
    }

    public static void lightningBolt(Vec3 vectorStart, Vec3 vectorEnd, ColorUtil colorUtil, int lifespan){
        if (Minecraft.getInstance().level == null){
            return;
        }
        LightningEffect.INSTANCE.add(Minecraft.getInstance().level, new LightningParticleOptions(LightningParticleOptions.BoltRenderInfo.thunderBolt(new ColorUtil(100, 100, 220, 1.0F)).noise(1.0F, 0.001F), vectorStart, vectorEnd, lifespan).size(0.5F), ClientModHandler.TickEventsHandler.PARTIAL_TICK);
    }
}