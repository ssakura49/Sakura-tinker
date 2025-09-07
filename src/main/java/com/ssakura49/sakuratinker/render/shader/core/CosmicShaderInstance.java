package com.ssakura49.sakuratinker.render.shader.core;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.ssakura49.sakuratinker.render.shader.cosmic.CosmicItemShaders;
import com.ssakura49.sakuratinker.render.shader.cosmic.CosmicTextures;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.minecraft.world.inventory.InventoryMenu;

import java.io.IOException;

public class CosmicShaderInstance extends STShaderInstance{
    public CosmicShaderInstance(ResourceProvider resourceProvider, ResourceLocation shaderLocation, VertexFormat format) throws IOException {
        super(resourceProvider, shaderLocation, format);
    }

    public static CosmicShaderInstance create(ResourceProvider resourceProvider, ResourceLocation loc, VertexFormat format) {
        try {
            return new CosmicShaderInstance(resourceProvider, loc, format);
        } catch (IOException var4) {
            throw new RuntimeException("Failed to initialize shader.", var4);
        }
    }

    public void setSampler(int index, ResourceLocation texture) {
        TextureAtlasSprite atlasSprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        super.setSampler("CosmicSampler" + index, Minecraft.getInstance().getTextureManager().getTexture(atlasSprite.atlasLocation()).getId());
        CosmicItemShaders.COSMIC_UVS[index * 4] = atlasSprite.getU0();
        CosmicItemShaders.COSMIC_UVS[index * 4 + 1] = atlasSprite.getV0();
        CosmicItemShaders.COSMIC_UVS[index * 4 + 2] = atlasSprite.getU1();
        CosmicItemShaders.COSMIC_UVS[index * 4 + 3] = atlasSprite.getV1();
    }

    public void setCosmicIcon() {
        for (int i = 0; i < 10; i++) {
            setSampler(i, CosmicTextures.rl(i));
            float[] uvs = new float[]{CosmicItemShaders.COSMIC_UVS[i * 4], CosmicItemShaders.COSMIC_UVS[i * 4 + 1], CosmicItemShaders.COSMIC_UVS[i * 4 + 2], CosmicItemShaders.COSMIC_UVS[i * 4 + 3]};
            RenderSystem.applyModelViewMatrix();
            switch (i) {
                case 0:
                    if (CosmicItemShaders.cosmicUVs != null) CosmicItemShaders.cosmicUVs.set(uvs);
                case 1:
                    if (CosmicItemShaders.cosmicUVs1 != null) CosmicItemShaders.cosmicUVs1.set(uvs);
                case 2:
                    if (CosmicItemShaders.cosmicUVs2 != null) CosmicItemShaders.cosmicUVs2.set(uvs);
                case 3:
                    if (CosmicItemShaders.cosmicUVs3 != null) CosmicItemShaders.cosmicUVs3.set(uvs);
                case 4:
                    if (CosmicItemShaders.cosmicUVs4 != null) CosmicItemShaders.cosmicUVs4.set(uvs);
                case 5:
                    if (CosmicItemShaders.cosmicUVs5 != null) CosmicItemShaders.cosmicUVs5.set(uvs);
                case 6:
                    if (CosmicItemShaders.cosmicUVs6 != null) CosmicItemShaders.cosmicUVs6.set(uvs);
                case 7:
                    if (CosmicItemShaders.cosmicUVs7 != null) CosmicItemShaders.cosmicUVs7.set(uvs);
                case 8:
                    if (CosmicItemShaders.cosmicUVs8 != null) CosmicItemShaders.cosmicUVs8.set(uvs);
                case 9:
                    if (CosmicItemShaders.cosmicUVs9 != null) CosmicItemShaders.cosmicUVs9.set(uvs);
            }
        }

    }
}
