package com.ssakura49.sakuratinker.data.providiers.tinker;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.data.STMaterialId;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.library.data.tinkering.AbstractMaterialTagProvider;

public class STMaterialTagProvider extends AbstractMaterialTagProvider {
    public STMaterialTagProvider(PackOutput packOutput, ExistingFileHelper existingFileHelper) {
        super(packOutput, SakuraTinker.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        tag(TinkerTags.Materials.EXCLUDE_FROM_LOOT).replace(false).addOptional(
                STMaterialId.goozma,
                STMaterialId.ReAvaritia.colorful,
                STMaterialId.ReAvaritia.infinity,
                STMaterialId.nihilite,
                STMaterialId.steady_alloy,
                STMaterialId.Botania.elementium,
                STMaterialId.Botania.terra_steel,
                STMaterialId.Botania.gaia
        );
        tag(TinkerTags.Materials.NETHER).replace(false).addOptional(

        );
    }

    @Override
    public String getName() {
        return "Sakura Tinker Material Tag Provider";
    }
}
