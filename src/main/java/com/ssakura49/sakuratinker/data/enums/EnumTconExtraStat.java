package com.ssakura49.sakuratinker.data.enums;

import com.ssakura49.sakuratinker.content.tools.stats.*;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;

public enum EnumTconExtraStat {
    bone(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0.02f, 8, 0.05f, 0.03f, 0.05f, 0f),
            new PhantomCoreMaterialStats(1, 8),
            STStatlessMaterialStats.FOX_MASK_MAIN,
            STStatlessMaterialStats.FOX_MASK_CORE
    ),
    cobalt(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0.4f, 18, 0.1f, 0.1f, 0.15f, 0.15f),
            new EnergyUnitMaterialStats(50000, 0.3f),
            new LaserMediumMaterialStats(15, 0.6f),
            new SpellClothMaterialStats(0.25f, 0.25f, 0.15f),
            new PhantomCoreMaterialStats(2, 16),
            STStatlessMaterialStats.FOX_MASK_MAIN,
            STStatlessMaterialStats.FOX_MASK_CORE
    ),
    copper(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0f, 8, 0.05f, 0f, 0.05f, 0.05f),
            new EnergyUnitMaterialStats(15000, 0.15f),
            new LaserMediumMaterialStats(8, 1.5f),
            new PhantomCoreMaterialStats(1, 8),
            STStatlessMaterialStats.FOX_MASK_MAIN,
            STStatlessMaterialStats.FOX_MASK_CORE
    ),
    manyullyn(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0.05f, 30, 0.25f, 0.1f, 0.2f, 0.2f),
            new EnergyUnitMaterialStats(80000, 0.6f),
            new LaserMediumMaterialStats(40, 0.4f),
            new PhantomCoreMaterialStats(2, 16),
            STStatlessMaterialStats.FOX_MASK_MAIN,
            STStatlessMaterialStats.FOX_MASK_CORE
    ),
    wood(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0.1f, 8, 0.05f, 0f, 0f, 0f),
            new EnergyUnitMaterialStats(5000, 0.1f),
            new LaserMediumMaterialStats(5, 2.0f),
            new SpellClothMaterialStats(-0.25f, 0.2f, 0.1f),
            new BattleFlagMaterialStats(4, 20, 20, 120),
            STStatlessMaterialStats.FOX_MASK_MAIN,
            STStatlessMaterialStats.FOX_MASK_CORE
    ),
    skyslime_vine(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(-0.1f, 8, 0f, 0f, 0.05f, 0.05f)
    ),
    nahualt(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0.05f, 14, 0.05f, 0.05f, 0.1f, 0.1f),
            new PhantomCoreMaterialStats(2, 16),
            STStatlessMaterialStats.FOX_MASK_MAIN,
            STStatlessMaterialStats.FOX_MASK_CORE
    ),
    iron(
            new PhantomCoreMaterialStats(1, 8),
            STStatlessMaterialStats.FOX_MASK_MAIN,
            STStatlessMaterialStats.FOX_MASK_CORE
    ),
    ;
    public final IMaterialStats[] stats;

    EnumTconExtraStat(IMaterialStats... stats) {
        this.stats = stats;
    }
}
