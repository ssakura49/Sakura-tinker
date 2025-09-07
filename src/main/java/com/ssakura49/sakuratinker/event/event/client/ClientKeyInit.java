package com.ssakura49.sakuratinker.event.event.client;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STKeys;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientKeyInit {
    @SubscribeEvent
    public static void registerKeys(RegisterKeyMappingsEvent event) {
        STKeys.registerKeyBindings();
        STKeys.KEY_MAPPINGS.values().forEach(event::register);
    }
}
