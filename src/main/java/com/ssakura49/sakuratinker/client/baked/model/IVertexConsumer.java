package com.ssakura49.sakuratinker.client.baked.model;

import com.mojang.blaze3d.vertex.VertexFormat;
import com.ssakura49.sakuratinker.utils.render.VertexUtils;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public interface IVertexConsumer {

    VertexFormat getVertexFormat();

    void setQuadTint(int tint);

    void setQuadOrientation(Direction orientation);

    void setApplyDiffuseLighting(boolean diffuse);

    void setTexture(TextureAtlasSprite texture);

    void put(int element, float... data);

    /**
     * Assumes the data is already completely unpacked.
     * You must always copy the data from the quad provided to an internal cache.
     * basically:
     * this.quad.put(quad);
     *
     * @param quad The quad to copy data from.
     */
    void put(Quad quad);

    default void put(BakedQuad quad) {
        VertexUtils.putQuad(this, quad);
    }
}