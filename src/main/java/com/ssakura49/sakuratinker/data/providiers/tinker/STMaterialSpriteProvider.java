package com.ssakura49.sakuratinker.data.providiers.tinker;

import com.ssakura49.sakuratinker.content.tools.stats.FletchingMaterialStats;
import com.ssakura49.sakuratinker.data.STMaterialId;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.client.data.material.AbstractMaterialSpriteProvider;
import slimeknights.tconstruct.library.client.data.spritetransformer.GreyToColorMapping;
import slimeknights.tconstruct.library.materials.MaterialRegistry;

public class STMaterialSpriteProvider extends AbstractMaterialSpriteProvider {
    @Override
    public @NotNull String getName() {
        return "Sakura Tinker Material Sprite Provider";
    }
    @Override
    protected void addAllMaterials() {
        this.buildMaterial(STMaterialId.TwilightForest.raven_feather).statType(FletchingMaterialStats.ID).fallbacks("vine","leaf").colorMapper(GreyToColorMapping.builder()
                .addARGB(0,0xFF000000)
                .addARGB(63,0xFF0f151e)
                .addARGB(102,0xFF141d27)
                .addARGB(140,0xFF2d353e)
                .addARGB(178,0xFF4e555d)
                .addARGB(216,0xFF636870)
                .addARGB(255,0xFF777c82).build());
        this.buildMaterial(STMaterialId.pyrothium).armor().maille().fallbacks("metal").colorMapper(GreyToColorMapping.builder()
                .addARGB(0,0xFF000000)
                .addARGB(63,0xFFda7600)
                .addARGB(102,0xFFffd83a)
                .addARGB(140,0xFFb06535)
                .addARGB(178,0xFFc96734)
                .addARGB(216,0xFFdf9750)
                .addARGB(255,0xFFffda7f).build());
        this.buildMaterial(STMaterialId.DreadSteel.dread_steel).armor().maille().fallbacks("metal").colorMapper(GreyToColorMapping.builder()
                .addARGB(0,0xFF000000)
                .addARGB(63,0xFF0f0709)
                .addARGB(102,0xFF180b0e)
                .addARGB(140,0xFF3f2229)
                .addARGB(178,0xFF572834)
                .addARGB(216,0xFF794b56)
                .addARGB(255,0xFF6d4e55).build());
        this.buildMaterial(STMaterialId.Goety.cursed_metal).armor().maille().fallbacks("metal").colorMapper(GreyToColorMapping.builder()
                .addARGB(0,0xFF000000)
                .addARGB(63,0xFF2a2434)
                .addARGB(102,0xFF2a2434)
                .addARGB(140,0xFF182b38)
                .addARGB(178,0xFF223949)
                .addARGB(216,0xFF2e4d62)
                .addARGB(255,0xFF35586f).build());
        this.buildMaterial(STMaterialId.Goety.dark_metal).armor().maille().fallbacks("metal").colorMapper(GreyToColorMapping.builder()
                .addARGB(0,0xFF000000)
                .addARGB(63,0xFF000000)
                .addARGB(102,0xFF030405)
                .addARGB(140,0xFF0e1213)
                .addARGB(178,0xFF1c2225)
                .addARGB(216,0xFF31393b)
                .addARGB(255,0xFF343a3d).build());
        this.buildMaterial(STMaterialId.Goety.unholy_alloy).meleeHarvest().repairKit().ranged().fallbacks("metal").colorMapper(GreyToColorMapping.builder()
                .addARGB(0,0xFF000000)
                .addARGB(63,0xFF000000)
                .addARGB(102,0xFF030405)
                .addARGB(140,0xFF4d2727)
                .addARGB(178,0xFF542c2c)
                .addARGB(216,0xFF633634)
                .addARGB(255,0xFF6b403c).build());
        this.buildMaterial(STMaterialId.Botania.mana_steel).meleeHarvest().ranged().armor().fallbacks("metal").colorMapper(GreyToColorMapping.builder()
                .addARGB(0,0xFF000000)
                .addARGB(63,0xFF160539)
                .addARGB(102,0xFF14084f)
                .addARGB(140,0xFF2e199f)
                .addARGB(178,0xFF1f20b9)
                .addARGB(216,0xFF3a63da)
                .addARGB(255,0xFF67b9ee).build());
        this.buildMaterial(STMaterialId.Botania.mana_steel).meleeHarvest().ranged().armor().fallbacks("metal").colorMapper(GreyToColorMapping.builder()
                .addARGB(0,0xFF000000)
                .addARGB(63,0xFF001e11)
                .addARGB(102,0xFF043c1c)
                .addARGB(140,0xFF0c7227)
                .addARGB(178,0xFF2bb93b)
                .addARGB(216,0xFF6ae862)
                .addARGB(255,0xFFccffb5).build());
        this.buildMaterial(STMaterialId.Botania.elementium).meleeHarvest().ranged().armor().fallbacks("metal").colorMapper(GreyToColorMapping.builder()
                .addARGB(0,0xFF000000)
                .addARGB(63,0xFF150136)
                .addARGB(102,0xFF3f0766)
                .addARGB(140,0xFF7a1892)
                .addARGB(178,0xFFc543a8)
                .addARGB(216,0xFFe084a5)
                .addARGB(255,0xFFf5c7c4).build());
        this.buildMaterial(STMaterialId.Botania.gaia).meleeHarvest().ranged().armor().fallbacks("metal").colorMapper(GreyToColorMapping.builder()
                .addARGB(0,0xFF000000)
                .addARGB(63,0xFF0c0f09)
                .addARGB(102,0xFF1a2014)
                .addARGB(140,0xFF5c7248)
                .addARGB(178,0xFF84a568)
                .addARGB(216,0xFFafda89)
                .addARGB(255,0xFFcdffa1).build());
        this.buildMaterial(STMaterialId.IceAndFire.amphithere_feather).statType(FletchingMaterialStats.ID).fallbacks("leaf").colorMapper(GreyToColorMapping.builder()
                .addARGB(0,0xFF000000)
                .addARGB(63,0xFF222c0d)
                .addARGB(102,0xFF194122)
                .addARGB(140,0xFF51763d)
                .addARGB(178,0xFF347d4b)
                .addARGB(216,0xFF27825a)
                .addARGB(255,0xFF1a977d).build());
    }
}
