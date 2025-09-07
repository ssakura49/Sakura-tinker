package com.ssakura49.sakuratinker.event.event.client;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.model.FoxCurioModel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModelLayersInit {
    @SubscribeEvent
    public static void onRegisterLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(FoxCurioModel.LAYER_LOCATION, FoxCurioModel::createLayer);
//        event.registerLayerDefinition(TerraPrismaModel.LAYER_LOCATION, TerraPrismaModel::createLayer);
    }
}
