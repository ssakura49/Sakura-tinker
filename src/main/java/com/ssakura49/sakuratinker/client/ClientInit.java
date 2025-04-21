package com.ssakura49.sakuratinker.client;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.renderer.CelestialBladeRenderer;
import com.ssakura49.sakuratinker.client.renderer.GhostKnifeRenderer;
import com.ssakura49.sakuratinker.register.STEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientInit {
    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers evt) {
        evt.registerEntityRenderer(STEntities.GHOST_KNIFE.get(), GhostKnifeRenderer::new);
        evt.registerEntityRenderer(STEntities.CELESTIAL_BLADE.get(), CelestialBladeRenderer::new);
    }

//    @SubscribeEvent
//    public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
//        if (ModListUtil.Botania) {
//            LayerDefinitions.init(event::registerLayerDefinition);
//        }
//    }

    public static void init() {
    }
}
