package com.ssakura49.sakuratinker.utils.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.ssakura49.sakuratinker.utils.other.ResourceUtils;
import com.ssakura49.sakuratinker.utils.render.color.Colour;
import com.ssakura49.sakuratinker.utils.render.color.ColourARGB;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class TextureUtils {
    public TextureUtils() {
    }

    public static int[] loadTextureData(ResourceLocation resource) {
        BufferedImage img = loadBufferedImage(resource);
        if (img == null) {
            return new int[0];
        } else {
            int w = img.getWidth();
            int h = img.getHeight();
            int[] data = new int[w * h];
            img.getRGB(0, 0, w, h, data, 0, w);
            return data;
        }
    }

    public static Colour[] loadTextureColours(ResourceLocation resource) {
        int[] idata = loadTextureData(resource);
        Colour[] data = new Colour[idata.length];

        for (int i = 0; i < data.length; ++i) {
            data[i] = new ColourARGB(idata[i]);
        }

        return data;
    }

    public static BufferedImage loadBufferedImage(ResourceLocation textureFile) {
        try {
            return loadBufferedImage(ResourceUtils.getResourceAsStream(textureFile));
        } catch (Exception var2) {
            System.err.println("Failed to load texture file: " + textureFile);
            var2.printStackTrace();
            return null;
        }
    }

    public static BufferedImage loadBufferedImage(InputStream in) throws IOException {
        BufferedImage img = ImageIO.read(in);
        in.close();
        return img;
    }

    public static void copySubImg(int[] fromTex, int fromWidth, int fromX, int fromY, int width, int height, int[] toTex, int toWidth, int toX, int toY) {
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                int fp = (y + fromY) * fromWidth + x + fromX;
                int tp = (y + toX) * toWidth + x + toX;
                toTex[tp] = fromTex[fp];
            }
        }

    }

    public static void prepareTexture(int target, int texture, int min_mag_filter, int wrap) {
        GlStateManager._texParameter(target, 10241, min_mag_filter);
        GlStateManager._texParameter(target, 10240, min_mag_filter);
        if (target == 3553) {
            GlStateManager._bindTexture(target);
        } else {
            GL11.glBindTexture(target, texture);
        }

        switch (target) {
            case 32879:
                GlStateManager._texParameter(target, 32882, wrap);
            case 3553:
                GlStateManager._texParameter(target, 10243, wrap);
            case 3552:
                GlStateManager._texParameter(target, 10242, wrap);
            default:
        }
    }

    public static TextureManager getTextureManager() {
        return Minecraft.getInstance().getTextureManager();
    }

    public static TextureAtlas getTextureMap() {
        return Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
    }

    public static TextureAtlasSprite getMissingSprite() {
        return getTextureMap().getSprite(MissingTextureAtlasSprite.getLocation());
    }

    public static TextureAtlasSprite getTexture(String location) {
        return getTextureMap().getSprite(ResourceLocation.parse(location));
    }

    public static TextureAtlasSprite getTexture(ResourceLocation location) {
        return getTextureMap().getSprite(location);
    }

    public static TextureAtlasSprite getBlockTexture(String string) {
        return getBlockTexture(ResourceLocation.parse(string));
    }

    public static TextureAtlasSprite getBlockTexture(ResourceLocation location) {
        return getTexture(ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "block/" + location.getPath()));
    }

    public static TextureAtlasSprite getItemTexture(String string) {
        return getItemTexture(ResourceLocation.parse(string));
    }

    public static TextureAtlasSprite getItemTexture(ResourceLocation location) {
        return getTexture(ResourceLocation.fromNamespaceAndPath(location.getNamespace(), "items/" + location.getPath()));
    }
}