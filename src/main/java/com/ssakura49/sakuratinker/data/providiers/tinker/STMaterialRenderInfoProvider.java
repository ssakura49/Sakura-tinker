package com.ssakura49.sakuratinker.data.providiers.tinker;

import com.ssakura49.sakuratinker.data.STMaterialId;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialRenderInfoProvider;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;

public class STMaterialRenderInfoProvider extends AbstractMaterialRenderInfoProvider {
    public STMaterialRenderInfoProvider(PackOutput packOutput, @Nullable AbstractMaterialSpriteProvider materialSprites, @Nullable ExistingFileHelper existingFileHelper) {
        super(packOutput, materialSprites, existingFileHelper);
    }

    @Override
    protected void addMaterialRenderInfo() {
        buildRenderInfo(STMaterialId.TwilightForest.raven_feather).color(0xFF777c82).fallbacks("leaf");
        buildRenderInfo(STMaterialId.pyrothium).color(0xFFdf9750).fallbacks("metal");
        buildRenderInfo(STMaterialId.DreadSteel.dread_steel).color(0xFF4d1e29).fallbacks("metal");
        buildRenderInfo(STMaterialId.Goety.cursed_metal).color(0xFF35586f).fallbacks("metal");
        buildRenderInfo(STMaterialId.Goety.dark_metal).color(0xFF3c4447).fallbacks("metal");
        buildRenderInfo(STMaterialId.Goety.unholy_alloy).color(0xFF633634).fallbacks("metal");
        buildRenderInfo(STMaterialId.Botania.mana_steel).color(0xFF67b9ee).fallbacks("metal");
        buildRenderInfo(STMaterialId.Botania.terra_steel).color(0xFF6ae862).fallbacks("metal");
        buildRenderInfo(STMaterialId.Botania.elementium).color(0xFFe084a5).fallbacks("metal");
        buildRenderInfo(STMaterialId.Botania.gaia).color(0xFFafda89).fallbacks("metal");
        buildRenderInfo(STMaterialId.IceAndFire.amphithere_feather).color(0xFF347d4b).fallbacks("leaf");
    }

    @Override
    public String getName() {
        return "Sakura Tinker Material Info Provider";
    }
}
