package com.ssakura49.sakuratinker.client;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.core.ClientTickHandler;
import com.ssakura49.sakuratinker.client.renderer.FoxCurioModelRenderer;
import com.ssakura49.sakuratinker.register.STItems;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSetupEventInit {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        CuriosRendererRegistry.register(STItems.fox_mask.get(), () -> FoxCurioModelRenderer.INSTANCE);
        var bus = MinecraftForge.EVENT_BUS;
        bus.addListener((TickEvent.ClientTickEvent e) -> {
            if (e.phase == TickEvent.Phase.END) {
                ClientTickHandler.clientTickEnd(Minecraft.getInstance());
            }
        });
        bus.addListener((TickEvent.RenderTickEvent e) -> {
            if (e.phase == TickEvent.Phase.START) {
                ClientTickHandler.renderTick(e.renderTickTime);
            }
        });
    }
}
