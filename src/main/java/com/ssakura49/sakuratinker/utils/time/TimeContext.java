package com.ssakura49.sakuratinker.utils.time;

import com.ssakura49.sakuratinker.client.component.STFont;
import com.ssakura49.sakuratinker.render.RendererUtils;
import com.ssakura49.sakuratinker.utils.math.MathUtils;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Timer;
import net.minecraft.util.FastColor;
import net.minecraft.util.TimeSource;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector2i;
import org.joml.Vector4f;

import java.awt.*;
import java.util.concurrent.atomic.AtomicLong;

public class TimeContext {

    @OnlyIn(Dist.CLIENT)
    public static class Client {
        private static final AtomicLong SEED_UNIQUIFIER = new AtomicLong(8682522807148012L);
        public static Timer timer = new Timer(20.0F, 0L);
        /**
         * 永远每1/100s自增1
         */
        public static long count = 0L;
        public static long timeStopGLFW = 0L;
        public static float getCommonDegrees() {
            return timeStopGLFW / 100F;
        }

        public static float currentSeconds() {
            return count / 1000F;
        }

        public static float currentSecondsTS() {
            return timeStopGLFW / 1000F;
        }

        public static long generateUniqueSeed() {
            return (TimeStopUtils.isTimeStop && RendererUtils.isTimeStop_andSameDimension) ? (SEED_UNIQUIFIER.get() * 1181783497276652981L) ^ (TimeContext.Both.timeStopModifyMillis * 1000000L) : SEED_UNIQUIFIER.updateAndGet((p_224601_) -> {
                return p_224601_ * 1181783497276652981L;
            }) ^ (Both.timeStopModifyMillis * 1000000L);
        }

        public static float alwaysPartial() {
            return (TimeStopUtils.isTimeStop || Minecraft.getInstance().pause) ? TimeContext.Client.timer.partialTick : Minecraft.getInstance().getPartialTick();
        }

        public static Vector2i getBorderColor() {
            float colorr = (float) STFont.milliTime() / 3000.0F % 1.0F;
            float colorrStep = (float) STFont.rangeRemap(
                    MathUtils.sin(((float) STFont.milliTime() / 200.0F)) % 6.28318D, -0.9D, 2.5D, 0.025D, 0.15D);
            int color = Color.HSBtoRGB(colorr, 0.15F, 0.7F);
            int colorEnd = Color.HSBtoRGB(colorr + colorrStep * 5, 0.05F, 0.7F);
            return new Vector2i(color, colorEnd);
        }

        public static Vector2i getBorderColor(int alpha) {
            float colorr = (float) STFont.milliTime() / 3000.0F % 1.0F;
            float colorrStep = (float) STFont.rangeRemap(
                    MathUtils.sin(((float) STFont.milliTime() / 200.0F)) % 6.28318D, -0.9D, 2.5D, 0.025D, 0.15D);
            int color = Color.HSBtoRGB(colorr, 0.15F, 0.7F);
            int colorEnd = Color.HSBtoRGB(colorr + colorrStep * 5, 0.05F, 0.7F);
            int a1 = (int) (FastColor.ARGB32.alpha(color) * (float) (alpha / 255));
            int a2 = (int) (FastColor.ARGB32.alpha(colorEnd) * (float) (alpha / 255));
            return new Vector2i(
                    FastColor.ARGB32.color(a1, FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color)),
                    FastColor.ARGB32.color(a2, FastColor.ARGB32.red(colorEnd), FastColor.ARGB32.green(colorEnd), FastColor.ARGB32.blue(colorEnd)));
        }

        public static float getPartialTickCount(Entity owner, boolean always) {
            return owner.tickCount + (always ? alwaysPartial() : Minecraft.getInstance().getPartialTick());
        }
    }

    public static class Both {
        public static long timeStopModifyMillis = 0L;
        public static TimeSource.NanoTimeSource timeSource = System::nanoTime;

        public static long getRealMillis() {
            return getNanos() / 1000000L;
        }

        public static Color rainbow(float f0, float saturation, float lgiht) {
            float colorr = (float) milliTime() / f0 % 1.0F;
            Color color = Color.getHSBColor(colorr, saturation, lgiht);
            return color;
        }

        public static Vector4f rainbowV4(float f0, float saturation, float lgiht) {
            float colorr = (float) milliTime() / f0 % 1.0F;
            Color color = Color.getHSBColor(colorr, saturation, lgiht);
            return new Vector4f(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
        }

        public static int argbRainbow(float f0, float saturation, float lgiht) {
            Color color = rainbow(f0, saturation, lgiht);
            return FastColor.ARGB32.color(color.getAlpha(), color.getRed(), color.getGreen(), color.getBlue());
        }
        public static long getNanos() {
            return timeSource.getAsLong();
        }

        public static long milliTime() {
            return Util.getMillis();
        }
    }
    public static float safeClientPartialTicks() {
        return Minecraft.getInstance().getFrameTime();
    }
}
