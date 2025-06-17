package com.ssakura49.sakuratinker.client;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STParticles;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Function;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientParticleInit {
    @SubscribeEvent
    public static void registerParticleFactories(RegisterParticleProvidersEvent evt) {
        STParticles.FactoryHandler.registerFactories(new STParticles.FactoryHandler.Consumer() {
            @Override
            public <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> constructor) {
                evt.registerSpriteSet(type, constructor::apply);
            }
        });
    }
}
