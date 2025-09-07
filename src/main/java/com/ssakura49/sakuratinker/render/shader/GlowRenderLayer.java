package com.ssakura49.sakuratinker.render.shader;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderType;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

public class GlowRenderLayer extends RenderType {
    public static RenderTarget fbo = null;
    private final RenderType delegate;

    public GlowRenderLayer(RenderType delegate, float[] rgba, float softness) {
        this(delegate, rgba, softness, true);
    }

    public GlowRenderLayer(RenderType delegate, float[] rgba, float softness, boolean lighting) {
        super("magic" + delegate.toString() + "_with_framebuffer", delegate.format(), delegate.mode(), delegate.bufferSize(), true, delegate.isOutline(), () -> {
            delegate.setupRenderState();
            if (lighting) {
                RenderSystem.enableBlend();
                if (softness == 1.0F)         RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_COLOR, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
                else RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            }
            GL11.glDepthFunc(GL11.GL_LESS);
            GL11.glDepthMask(false);
        }, () -> {
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glDepthMask(true);
            if (lighting) {
                RenderSystem.defaultBlendFunc();
                RenderSystem.disableBlend();
                delegate.clearRenderState();
            }
        });
        this.delegate = delegate;
    }

    public Optional<RenderType> outline() {
        return this.delegate.outline();
    }

    public boolean equals(@Nullable Object other) {
        return (other instanceof GlowRenderLayer && this.delegate
                .equals(((GlowRenderLayer) other).delegate));
    }

    public int hashCode() {
        return Objects.hash(this.delegate);
    }
}
