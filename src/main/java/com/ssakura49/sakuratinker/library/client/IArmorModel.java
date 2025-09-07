package com.ssakura49.sakuratinker.library.client;

import net.minecraft.resources.ResourceLocation;

public interface IArmorModel {
    int textureSize();
    ResourceLocation getModelTexture(int partIndex);
}
