package com.ssakura49.sakuratinker.client.baked.tinker;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.jetbrains.annotations.NotNull;

public class MaskRedVertexConsumer implements VertexConsumer {
    private final VertexConsumer parent;

    public MaskRedVertexConsumer(VertexConsumer parent) {
        this.parent = parent;
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        return parent.color(255, g, b, a); // 强制 red = 255
    }

    // 委托其余方法
    @Override public @NotNull VertexConsumer vertex(double x, double y, double z) { return parent.vertex(x, y, z); }
    @Override public @NotNull VertexConsumer uv(float u, float v) { return parent.uv(u, v); }
    @Override public @NotNull VertexConsumer overlayCoords(int u, int v) { return parent.overlayCoords(u, v); }
    @Override public @NotNull VertexConsumer uv2(int u, int v) { return parent.uv2(u, v); }
    @Override public @NotNull VertexConsumer normal(float x, float y, float z) { return parent.normal(x, y, z); }
    @Override public void endVertex() { parent.endVertex(); }
    @Override public void defaultColor(int r, int g, int b, int a) { parent.defaultColor(r, g, b, a); }
    @Override public void unsetDefaultColor() { parent.unsetDefaultColor(); }
}
