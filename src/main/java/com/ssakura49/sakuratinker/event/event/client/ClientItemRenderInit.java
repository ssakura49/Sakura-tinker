package com.ssakura49.sakuratinker.event.event.client;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.renderer.ColorHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientItemRenderInit {

    @SubscribeEvent
    public static void registerBlockColors(RegisterColorHandlersEvent.Block evt) {
        ColorHandler.submitBlocks(evt::register);
    }

    @SubscribeEvent
    public static void registerItemColors(RegisterColorHandlersEvent.Item evt) {
        ColorHandler.submitItems(evt::register);
    }
}
