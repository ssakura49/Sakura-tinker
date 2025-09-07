package com.ssakura49.sakuratinker.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;

public interface ICustomParticleRenderTask {
    double getX();

    double getY();

    double getZ();

    void doParticleRenderTask(Camera camera, PoseStack stack, float partialTicks);

    void enable();

    void disable();

    boolean isEnable();
}
