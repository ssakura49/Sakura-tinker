package com.ssakura49.sakuratinker.event.event.client;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.model.FirstFractalBakedModels;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientModelInit {
    @SubscribeEvent
    public static void onModelRegister(ModelEvent.RegisterAdditional event) {
        for(int i = 0; i < FirstFractalBakedModels.firstFractalWeaponModels.length; i++) {
            event.register(ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "item/misc/first_fractal_" + i));
        }
    }

    @SubscribeEvent
    public static void onModelBake(ModelEvent.BakingCompleted event) {
        FirstFractalBakedModels.init(event.getModelManager());
    }
}
