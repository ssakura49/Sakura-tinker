package com.ssakura49.sakuratinker.client.core;

import net.minecraft.client.Minecraft;

public class ClientTickHandler {
    private ClientTickHandler() {}

    public static int ticksInGame = 0;
    public static float partialTicks = 0;

    public static float total() {
        return ticksInGame + partialTicks;
    }

    public static void renderTick(float renderTickTime) {
        partialTicks = renderTickTime;
    }

    public static void clientTickEnd(Minecraft mc) {
        if (!mc.isPaused()) {
            ticksInGame++;
            partialTicks = 0;
        }
    }
}
