package com.ssakura49.sakuratinker.utils.tinker;

import com.ssakura49.sakuratinker.library.client.IArmorModel;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfo;
import slimeknights.tconstruct.library.client.materials.MaterialRenderInfoLoader;
import slimeknights.tconstruct.library.materials.definition.MaterialVariant;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.Optional;

public record TextureInformation(ResourceLocation resourceLocation, int color) {
    public static TextureInformation getTexture(ToolStack tool, int partIndex) {
        MaterialVariant material = tool.getMaterial(partIndex);
        Optional<MaterialRenderInfo> optional = MaterialRenderInfoLoader.INSTANCE.getRenderInfo(material.getVariant());
        if (optional.isPresent()) {
            MaterialRenderInfo info = optional.get();
            return new TextureInformation(((IArmorModel)tool.getItem()).getModelTexture(partIndex), info.vertexColor());
        }
        return new TextureInformation(null, -1);
    }
}
