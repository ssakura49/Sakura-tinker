package com.ssakura49.sakuratinker.data.generator.providiers.tinker;

import com.ssakura49.sakuratinker.data.generator.STMaterialId;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
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
        buildRenderInfo(STMaterialId.chimera_gamma).color(0xFF386cc2).fallbacks("metal");
        buildRenderInfo(STMaterialId.dragon_sinew).color(0xFF2f2154).fallbacks("metal");
        buildRenderInfo(STMaterialId.BuddyCard.buddysteel).color(0xFF75bef7).fallbacks("metal");
        buildRenderInfo(STMaterialId.BuddyCard.charged_buddysteel).color(0xFF75bef7).fallbacks("metal");
        buildRenderInfo(STMaterialId.BuddyCard.crimson_buddysteel).color(0xFF75bef7).fallbacks("metal");
        buildRenderInfo(STMaterialId.BuddyCard.void_buddysteel).color(0xFF75bef7).fallbacks("metal");
        buildRenderInfo(STMaterialId.BuddyCard.perfect_buddysteel).color(0xFF75bef7).fallbacks("metal");
        buildRenderInfo(STMaterialId.Botania.mana_string).color(0xFF9dfff4).fallbacks("leaf");
        buildRenderInfo(STMaterialId.Botania.living_wood).color(0xFF421909).fallbacks("wood");
        buildRenderInfo(STMaterialId.Botania.living_rock).color(0xFFd1cbba).fallbacks("rock");
        buildRenderInfo(STMaterialId.ExtraBotany.aerialite).color(0xFF0cb0cb).fallbacks("metal");
        buildRenderInfo(STMaterialId.ExtraBotany.shadowium).color(0xFF797979).fallbacks("metal");
        buildRenderInfo(STMaterialId.ExtraBotany.photonium).color(0xFFebebeb).fallbacks("metal");
        buildRenderInfo(STMaterialId.ExtraBotany.the_end).color(0xFFff51a4).fallbacks("metal");
        buildRenderInfo(STMaterialId.delusion).color(0xFF3d769f).fallbacks("metal");
        buildRenderInfo(STMaterialId.gluttonous).color(0xFFd48144).fallbacks("metal");
        buildRenderInfo(STMaterialId.cold_iron_alloy).color(0xFF9d6bed).fallbacks("metal");
        buildRenderInfo(STMaterialId.paper).color(0xFFd6d6d6).fallbacks("leaf");
        buildRenderInfo(STMaterialId.EnigmaticLegacy.nefarious).color(0xFF42019d).fallbacks("metal");
        buildRenderInfo(STMaterialId.BuddyCard.true_perfect_buddysteel).color(0xFF75bef7).fallbacks("metal");
        buildRenderInfo(STMaterialId.GoetyRevelation.apocalyptium).color(0xFFffbe2e).fallbacks("metal");
    }

    @Override
    public @NotNull String getName() {
        return "Sakura Tinker Material Info Provider";
    }
}
