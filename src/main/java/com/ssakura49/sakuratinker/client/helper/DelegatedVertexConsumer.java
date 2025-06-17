package com.ssakura49.sakuratinker.client.helper;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

public class DelegatedVertexConsumer implements VertexConsumer {
    private final VertexConsumer delegate;

    protected DelegatedVertexConsumer(VertexConsumer delegate) {
        this.delegate = delegate;
    }

    public @NotNull VertexConsumer vertex(double x, double y, double z) {
        return this.delegate.vertex(x, y, z);
    }

    public @NotNull VertexConsumer color(int red, int green, int blue, int alpha) {
        return this.delegate.color(red, green, blue, alpha);
    }

    public @NotNull VertexConsumer uv(float u, float v) {
        return this.delegate.uv(u, v);
    }

    public @NotNull VertexConsumer overlayCoords(int u, int v) {
        return this.delegate.overlayCoords(u, v);
    }

    public @NotNull VertexConsumer uv2(int u, int v) {
        return this.delegate.uv2(u, v);
    }

    public @NotNull VertexConsumer normal(float x, float y, float z) {
        return this.delegate.normal(x, y, z);
    }

    public void endVertex() {
        this.delegate.endVertex();
    }

    public void defaultColor(int red, int green, int blue, int alpha) {
        this.delegate.defaultColor(red, green, blue, alpha);
    }

    public void unsetDefaultColor() {
        this.delegate.unsetDefaultColor();
    }
}
