package com.ssakura49.sakuratinker.render.shader.cosmic;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CosmicTextures {
    public static TextureAtlasSprite[] COSMIC_SPRITES = new TextureAtlasSprite[10];

    public static void init() {
        for (int i = 0; i < COSMIC_SPRITES.length; i++) {
            COSMIC_SPRITES[i] = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(rl(i));
        }
    }

    public static ResourceLocation rl(int i) {
        return rl("item/misc/cosmic_" + i);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, path);
    }
}
