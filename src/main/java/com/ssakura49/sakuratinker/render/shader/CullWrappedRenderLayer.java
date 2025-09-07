package com.ssakura49.sakuratinker.render.shader;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.Optional;

@OnlyIn(Dist.CLIENT)
public class CullWrappedRenderLayer extends RenderType {
    private final RenderType delegate;

    public CullWrappedRenderLayer(RenderType delegate) {
        super("magic" + delegate.toString() + "_with_cull", delegate.format(), delegate.mode(), delegate.bufferSize(), true, delegate.isOutline(), () -> {
            delegate.setupRenderState();
            RenderSystem.enableCull();
        }, () -> {
            RenderSystem.disableCull();
            delegate.clearRenderState();
        });
        this.delegate = delegate;
    }

    public @NotNull Optional<RenderType> outline() {
        return this.delegate.outline();
    }

    public boolean equals(@Nullable Object other) {
        return (other instanceof CullWrappedRenderLayer && this.delegate
                .equals(((CullWrappedRenderLayer) other).delegate));
    }

    public int hashCode() {
        return Objects.hash(this.delegate);
    }
}