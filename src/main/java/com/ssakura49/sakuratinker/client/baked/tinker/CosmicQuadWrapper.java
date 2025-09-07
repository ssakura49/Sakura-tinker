package com.ssakura49.sakuratinker.client.baked.tinker;

import net.minecraft.client.renderer.block.model.BakedQuad;

import java.util.ArrayList;
import java.util.List;

public class CosmicQuadWrapper {

    /**
     * Wraps a list of quads to inject mask.r = 1.0 into their color.
     */
    public static List<BakedQuad> wrap(List<BakedQuad> baseQuads) {
        List<BakedQuad> result = new ArrayList<>(baseQuads.size());
        for (BakedQuad quad : baseQuads) {
            result.add(injectMaskR(quad));
        }
        return result;

    }

    /**
     * Copy a BakedQuad and set mask.r = 1.0 (e.g. red channel = 255).
     */
    private static BakedQuad injectMaskR(BakedQuad original) {
        int[] originalData = original.getVertices();
        int[] newData = originalData.clone();

        int vertexCount = 4;
        int stride = originalData.length / vertexCount;

        for (int i = 0; i < vertexCount; i++) {
            int colorIndex = i * stride + 3; // ARGB
            int color = newData[colorIndex];

            // Extract channels
            int a = (color >> 24) & 0xFF;
            int r = (color >> 16) & 0xFF;
            int g = (color >> 8) & 0xFF;
            int b = color & 0xFF;

            // Set red = 255 to mark mask.r = 1.0
            r = 255;

            int newColor = (a << 24) | (r << 16) | (g << 8) | b;
            newData[colorIndex] = newColor;
        }
        return new BakedQuad(
                newData,
                original.getTintIndex(),
                original.getDirection(),
                original.getSprite(),
                original.isShade()
        );

    }
}
