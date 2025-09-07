package com.ssakura49.sakuratinker.client.particles.wish;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import org.jetbrains.annotations.NotNull;

public class FXWisp extends TextureSheetParticle {
    private final boolean depthTest;
    private final float moteParticleScale;
    private final int moteHalfLife;

    public FXWisp(ClientLevel world, double d, double d1, double d2, double xSpeed, double ySpeed, double zSpeed, float size, float red, float green, float blue, boolean depthTest, float maxAgeMul, boolean noClip, float g) {
        super(world, d, d1, d2);
        this.xd = xSpeed;
        this.yd = ySpeed;
        this.zd = zSpeed;
        this.rCol = red;
        this.gCol = green;
        this.bCol = blue;
        this.alpha = 0.375F;
        this.gravity = g;
        this.quadSize = (this.random.nextFloat() * 0.5F + 0.5F) * 2.0F * size;
        this.moteParticleScale = this.quadSize;
        this.lifetime = (int)((double)28.0F / (Math.random() * 0.3 + 0.7) * (double)maxAgeMul);
        this.depthTest = depthTest;
        this.moteHalfLife = this.lifetime / 2;
        this.setSize(0.01F, 0.01F);
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        this.hasPhysics = !noClip;
    }

    public float getQuadSize(float partialTicks) {
        float agescale = (float)this.age / (float)this.moteHalfLife;
        if (agescale > 1.0F) agescale = 2.0F - agescale;
        this.quadSize = this.moteParticleScale * agescale * 0.5F;
        return this.quadSize;
    }

    protected int getLightColor(float partialTicks) {
        return 15728880; // full brightness
    }

    @NotNull
    public ParticleRenderType getRenderType() {
        return this.depthTest ? ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT : ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) this.remove();

        this.yd -= this.gravity;
        this.move(this.xd, this.yd, this.zd);
        if (this.gravity == 0.0F) {
            this.xd *= 0.98;
            this.yd *= 0.98;
            this.zd *= 0.98;
        }
    }
}
