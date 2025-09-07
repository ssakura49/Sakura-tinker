package com.ssakura49.sakuratinker.client.particles.wish;

import com.mojang.serialization.Codec;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleType;
import org.jetbrains.annotations.NotNull;

public class WispParticleType extends ParticleType<WispParticleData> {
    public WispParticleType() {
        super(false, WispParticleData.DESERIALIZER);
    }

    public @NotNull Codec<WispParticleData> codec() {
        return WispParticleData.CODEC;
    }

    public static class Factory implements ParticleProvider<WispParticleData> {
        private final SpriteSet sprite;

        public Factory(SpriteSet sprite) {
            this.sprite = sprite;
        }

        public Particle createParticle(WispParticleData data, ClientLevel world, double x, double y, double z, double mx, double my, double mz) {
            FXWisp ret = new FXWisp(world, x, y, z, mx, my, mz, data.size, data.r, data.g, data.b, data.depthTest, data.maxAgeMul, data.noClip, data.gravity);
            ret.pickSprite(this.sprite);
            return ret;
        }
    }
}
