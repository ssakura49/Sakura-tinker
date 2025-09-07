package com.ssakura49.sakuratinker.render.custom.normal;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssakura49.sakuratinker.auto.CustomRendererAttributes;
import com.ssakura49.sakuratinker.client.CustomInLevelRendererDispatcher;
import com.ssakura49.sakuratinker.client.ICustomInLevelRenderTask;
import com.ssakura49.sakuratinker.client.ICustomTickrateRenderer;
import com.ssakura49.sakuratinker.client.InLevelRenderType;
import com.ssakura49.sakuratinker.render.RendererUtils;
import com.ssakura49.sakuratinker.render.shader.GlowRenderLayer;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import com.ssakura49.sakuratinker.utils.helper.TimeStopTimeHelper;
import com.ssakura49.sakuratinker.utils.time.TimeStopUtils;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

@CustomRendererAttributes(name = ShockWaveSkillRenderer.NAME)
public class ShockWaveSkillRenderer implements ICustomInLevelRenderTask, ICustomTickrateRenderer {
    public static final String NAME = "normal:shockwave_1";
    public final boolean forcedUpdate;
    protected final double x, y, z;
    public Entity entity;
    public TimeStopTimeHelper timeStopTimeHelper;
    public float scale = 1F;
    public int max = 77;
    protected int tickCount = 0;
    protected boolean isStatic = false;

    public ShockWaveSkillRenderer(Entity entity, boolean forcedUpdate) {
        this(0D, 0D, 0D, entity, forcedUpdate);
    }

    public ShockWaveSkillRenderer(Entity entity) {
        this(entity, false);
    }

    public ShockWaveSkillRenderer(double x, double y, double z, Entity entity, boolean forcedUpdate) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.entity = entity;
        this.forcedUpdate = forcedUpdate;
        isStatic = x != 0 || y != 0 || z != 0;
    }

    public ShockWaveSkillRenderer() {
        this(null);
    }

    @Override
    public double getX() {
        return entity == null ? x : Mth.lerp(Minecraft.getInstance().realPartialTick, entity.xOld, entity.getX());
    }

    @Override
    public double getY() {
        return entity == null ? y : Mth.lerp(Minecraft.getInstance().realPartialTick, entity.yOld, entity.getY());
    }

    @Override
    public double getZ() {
        return entity == null ? z : Mth.lerp(Minecraft.getInstance().realPartialTick, entity.zOld, entity.getZ());
    }

    @Override
    public void doLevelRenderTask(MultiBufferSource.BufferSource bufferSource, ClientLevel level, Camera camera, PoseStack stack, int packedLight, float partialTicks) {
        if (level != null) {
            if (!isStatic && (entity == null || !entity.isAddedToWorld() || (Minecraft.getInstance().level != null && !CustomInLevelRendererDispatcher.allTickingEntities.contains(entity))))
                disable();
            if (forcedUpdate && !TimeStopUtils.canMove(entity))
                disable();
            LocalPlayer player = Minecraft.getInstance().player;
            CameraType type = Minecraft.getInstance().options.getCameraType();
            float partialWhenNormal = (partial(partialTicks) * 5);
            int time = forcedUpdate ? timeStopTimeHelper.integer_time : (int) (partialWhenNormal * 5);
            if (forcedUpdate)
                if (player != null && entity.getUUID().equals(player.getUUID()) && type == CameraType.FIRST_PERSON && (time < 25 || time > max - 25))
                    return;
            stack.pushPose();
            MultiBufferSource.BufferSource buf = bufferSource;
            RenderType glowRenderLayer = new GlowRenderLayer((STRenderType.createSphereRenderType2(RendererUtils.beam)), null, 0, false);
            float partialTime = forcedUpdate ? Math.max(0.0F, Mth.lerp(partialTicks, Math.max(0, time - 1), time)) : partialWhenNormal;
            float size = partialTime;
            if (forcedUpdate) {
                size = Mth.clamp((partialTime > max / 2F ? max - partialTime : partialTime), 0F, max) * scale;
            }
            float alpha = 0.36F - Math.min((size - 20F) / (max - (forcedUpdate ? 70F : 20F)), 1F) * 0.4F;
            RendererUtils.renderSphere(stack, buf, size * scale, 25, packedLight, .3f, .3f, .3f, alpha, glowRenderLayer, true);
            //RendererUtils.renderSphere(matrix, buf, 0.00001F, 20, 240, 240, r, g, b, a, RenderType.glint());
            stack.popPose();
        }
    }


    @Override
    public void enable() {
        if (forcedUpdate) max = 200;
        if (forcedUpdate && timeStopTimeHelper == null)
            timeStopTimeHelper = new TimeStopTimeHelper(0, max + 3).setOnlyIncrease(true)
                    .setRunning(true);
        addTask();
    }

    @Override
    public void disable() {
        if (forcedUpdate) {
            timeStopTimeHelper.min_i = timeStopTimeHelper.max_i = 0;
            timeStopTimeHelper.setOnlyIncrease(true).setRunning(false);
            TimeStopTimeHelper.set.remove(timeStopTimeHelper);
        }

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
        Minecraft mc = Minecraft.getInstance();
        if (mc.player != null) {
            if (mc.player.isDeadOrDying() && mc.player.deathTime > 19)
                return false;
        }
        if (entity == null || !entity.isAddedToWorld() || (mc.level != null && !Minecraft.getInstance().level.tickingEntities.contains(entity)))
            return false;
        if (forcedUpdate && !TimeStopUtils.canMove(entity))
            return false;
        if (isStatic) {
            return tickCount < max / 5;
        }

        return forcedUpdate ? timeStopTimeHelper != null && timeStopTimeHelper.integer_time <= max : tickCount < max / 5;
    }

    @Override
    public boolean alwaysRender() {
        return true;
    }

    @Override
    public InLevelRenderType inLevelType() {
        return InLevelRenderType.CUSTOM;
    }
}
