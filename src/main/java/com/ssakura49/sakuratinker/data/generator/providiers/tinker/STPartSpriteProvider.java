package com.ssakura49.sakuratinker.data.generator.providiers.tinker;

import com.ssakura49.sakuratinker.common.tools.stats.FletchingMaterialStats;
import com.ssakura49.sakuratinker.common.tools.stats.PhantomCoreMaterialStats;
import slimeknights.tconstruct.library.client.data.material.AbstractPartSpriteProvider;

import static com.ssakura49.sakuratinker.SakuraTinker.MODID;

public class STPartSpriteProvider extends AbstractPartSpriteProvider {
    public STPartSpriteProvider() {
        super(MODID);
    }

    @Override
    public String getName() {
        return "Sakura Tinker Part Sprite Provider";
    }

    @Override
    protected void addAllSpites() {
        addSprite("part/fletching/fletching", FletchingMaterialStats.ID);
        addSprite("part/phantom_core/phantom_core", PhantomCoreMaterialStats.ID);
    }
}
