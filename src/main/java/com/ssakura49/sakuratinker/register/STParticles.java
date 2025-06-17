package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.fx.WispParticleData;
import com.ssakura49.sakuratinker.client.fx.WispParticleType;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class STParticles {
    public static final DeferredRegister<ParticleType<?>> PARTICLES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, SakuraTinker.MODID);

    public static final RegistryObject<ParticleType<WispParticleData>> WISP =
            PARTICLES.register("wisp", WispParticleType::new);

    public static void registerParticles(BiConsumer<ParticleType<?>, ResourceLocation> r) {
        r.accept(WISP.get(), SakuraTinker.location("wisp"));
    }

    public static class FactoryHandler {
        public FactoryHandler() {
        }
        public static void registerFactories(Consumer consumer) {
            consumer.register(STParticles.WISP.get(), WispParticleType.Factory::new);
        }

        public interface Consumer {
            <T extends ParticleOptions> void register(ParticleType<T> type, Function<SpriteSet, ParticleProvider<T>> provider);
        }
    }
}
