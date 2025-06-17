package com.ssakura49.sakuratinker.client.fx;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import org.jetbrains.annotations.NotNull;

public class FXWisp extends TextureSheetParticle {
    private final boolean depthTest;
    private final float moteParticleScale;
    private final int moteHalfLife;
    public static final ParticleRenderType NORMAL_RENDER = new ParticleRenderType() {
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            FXWisp.beginRenderCommon(bufferBuilder, textureManager);
            RenderSystem.enableDepthTest();
        }

        public void end(Tesselator tessellator) {
            tessellator.end();
            FXWisp.endRenderCommon();
        }

        public String toString() {
            return "sakuratinker:wisp";
        }
    };
    public static final ParticleRenderType DIW_RENDER = new ParticleRenderType() {
        public void begin(BufferBuilder bufferBuilder, TextureManager textureManager) {
            beginRenderCommon(bufferBuilder, textureManager);
            RenderSystem.disableDepthTest();
        }

        public void end(Tesselator tessellator) {
            tessellator.end();
            RenderSystem.enableDepthTest();
            FXWisp.endRenderCommon();
        }

        public String toString() {
            return "sakuratinker:depth_ignoring_wisp";
        }
    };

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

    public float getQuadSize(float p_217561_1_) {
        float agescale = (float)this.age / (float)this.moteHalfLife;
        if (agescale > 1.0F) {
            agescale = 2.0F - agescale;
        }

        this.quadSize = this.moteParticleScale * agescale * 0.5F;
        return this.quadSize;
    }

    protected int getLightColor(float partialTicks) {
        return 15728880;
    }

    public @NotNull ParticleRenderType getRenderType() {
        return this.depthTest ? NORMAL_RENDER : DIW_RENDER;
    }

    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        }

        this.yd -= (double)this.gravity;
        this.move(this.xd, this.yd, this.zd);
        if (this.gravity == 0.0F) {
            this.xd *= 0.98;
            this.yd *= 0.98;
            this.zd *= 0.98;
        }

    }

    public void setGravity(float value) {
        this.gravity = value;
    }

    private static void beginRenderCommon(BufferBuilder bufferBuilder, TextureManager textureManager) {
        Minecraft.getInstance().gameRenderer.lightTexture().turnOnLightLayer();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1);
        RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
        AbstractTexture tex = textureManager.getTexture(TextureAtlas.LOCATION_PARTICLES);
        tex.setFilter(true, false);
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
    }

    private static void endRenderCommon() {
        AbstractTexture tex = Minecraft.getInstance().getTextureManager().getTexture(TextureAtlas.LOCATION_PARTICLES);
        tex.setFilter(false, false);
        RenderSystem.disableBlend();
        RenderSystem.depthMask(true);
    }
}
