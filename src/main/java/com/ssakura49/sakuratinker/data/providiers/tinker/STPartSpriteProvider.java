package com.ssakura49.sakuratinker.data.providiers.tinker;

import com.ssakura49.sakuratinker.content.tools.stats.FletchingMaterialStats;
import com.ssakura49.sakuratinker.content.tools.stats.PhantomCoreMaterialStats;
import com.ssakura49.sakuratinker.content.tools.stats.STStatlessMaterialStats;
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
        addSprite("part/fox_mask_main/fox_mask_main", STStatlessMaterialStats.FOX_MASK_MAIN.getIdentifier());
        addSprite("part/fox_mask_core/fox_mask_core", STStatlessMaterialStats.FOX_MASK_CORE.getIdentifier());
    }
}
