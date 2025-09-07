package com.ssakura49.sakuratinker.client;

import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.Mth;

/**
 * 按照{@link net.minecraft.client.Minecraft#timer } {@link net.minecraft.client.Timer#msPerTick}的速度更新
 * 实现接口后，无需手动添加{@link com.ssakura49.sakuratinker.utils.helper.TimeHelper}了
 */
public interface ICustomTickrateRenderer extends ICustomInLevelRenderTask {
    void tick(ClientLevel level, Camera camera, float partialTicks);

    int tickCount();

    /**
     * @param partial partial ticks
     * @param max     max tick count
     * @return [0, 1]
     */
    default float percent(float partial, int max) {
        return Mth.lerp(partial, tickCount(), tickCount() + 1) / max;
    }

    /**
     * @param partial partial ticks
     * @return partial tickCount
     */
    default float partial(float partial) {
        return Mth.lerp(partial, tickCount(), tickCount() + 1);
    }

    @Override
    default void addTask() {
        ICustomInLevelRenderTask.super.addTask();
        CustomInLevelRendererDispatcher.tickRate.add(this);
    }
}
