package com.ssakura49.sakuratinker.data.generator.enums;

import com.ssakura49.sakuratinker.common.tools.stats.*;
import slimeknights.tconstruct.library.materials.stats.IMaterialStats;

public enum EnumTconExtraStat {
    bone(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0.02f, 8, 0.05f, 0.03f, 0.05f, 0f),
            new PhantomCoreMaterialStats(1, 8),
            new AxleMaterialStats(100, 30, 3.0f),
            new YoYoRingMaterialStats(1)
    ),
    cobalt(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0.4f, 18, 0.1f, 0.1f, 0.15f, 0.15f),
            new EnergyUnitMaterialStats(50000, 0.3f),
            new LaserMediumMaterialStats(15, 0.6f, 8),
            new SpellClothMaterialStats(10.0f,0.25f, 0.25f, 10f),
            new PhantomCoreMaterialStats(2, 16),
            new AxleMaterialStats(180, 20, 2.5f),
            new YoYoRingMaterialStats(1),
            new ChordMaterialStats(6)
    ),
    copper(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0f, 8, 0.05f, 0f, 0.05f, 0.05f),
            new EnergyUnitMaterialStats(15000, 0.15f),
            new LaserMediumMaterialStats(8, 1.5f, 2),
            new PhantomCoreMaterialStats(1, 8),
            new AxleMaterialStats(80, 30, 3.5f),
            new YoYoRingMaterialStats(1)
    ),
    manyullyn(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0.05f, 30, 0.25f, 0.1f, 0.2f, 0.2f),
            new EnergyUnitMaterialStats(80000, 0.6f),
            new LaserMediumMaterialStats(40, 0.4f, 12),
            new PhantomCoreMaterialStats(2, 16),
            new SpellClothMaterialStats(25.0f,0.25f, 0.15f, 5f),
            new AxleMaterialStats(280, 15, 4.5f),
            new YoYoRingMaterialStats(1),
            new ChordMaterialStats(8)
    ),
    wood(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0.1f, 8, 0.05f, 0f, 0f, 0f),
            new EnergyUnitMaterialStats(5000, 0.1f),
            new LaserMediumMaterialStats(5, 2.0f, 1),
            new SpellClothMaterialStats(5.0f,-0.25f, 0.2f, 2f),
            new BattleFlagMaterialStats(4, 20, 20, 120),
            new AxleMaterialStats(60, 40, 1.5f),
            new YoYoRingMaterialStats(1),
            new SoulGathererMaterialStats(1, 0.0f)
    ),
    skyslime_vine(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(-0.1f, 8, 0f, 0f, 0.05f, 0.05f),
            new ChordMaterialStats(8)
    ),
    nahualt(
            STStatlessMaterialStats.CHARM_CORE,
            new CharmChainMaterialStats(0.05f, 14, 0.05f, 0.05f, 0.1f, 0.1f),
            new PhantomCoreMaterialStats(2, 16)
    ),
    iron(
            new PhantomCoreMaterialStats(1, 8)
    ),
    string(
            new ChordMaterialStats(8)
    ),
    bamboo(
            new SpellClothMaterialStats(10.0f, 0.15f, -0.1f, 3.0f)
    ),
    obsidian(
            new SpellClothMaterialStats(25.0f, -0.1f, -0.1f, -3.0f)
    ),
    ;
    public final IMaterialStats[] stats;

    EnumTconExtraStat(IMaterialStats... stats) {
        this.stats = stats;
    }
}
