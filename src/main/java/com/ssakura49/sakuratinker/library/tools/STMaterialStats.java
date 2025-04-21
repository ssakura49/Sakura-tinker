package com.ssakura49.sakuratinker.library.tools;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.tools.stats.BatteryCellMaterialStats;
import com.ssakura49.sakuratinker.content.tools.stats.CharmChainMaterialStats;
import com.ssakura49.sakuratinker.content.tools.stats.LaserMediumMaterialStats;
import com.ssakura49.sakuratinker.content.tools.stats.STExtraMaterialStats;
import slimeknights.tconstruct.library.materials.IMaterialRegistry;
import slimeknights.tconstruct.library.materials.MaterialRegistry;
import slimeknights.tconstruct.library.materials.stats.MaterialStatsId;

public class STMaterialStats {
    public static final MaterialStatsId CHARM = new MaterialStatsId(SakuraTinker.location("charm"));
    public static final MaterialStatsId LASER_GUN = new MaterialStatsId(SakuraTinker.location("laser_gun"));

    public STMaterialStats(){}

    public static void setup() {
        IMaterialRegistry registry = MaterialRegistry.getInstance();
        registry.registerStatType(CharmChainMaterialStats.TYPE, CHARM);
        registry.registerStatType(STExtraMaterialStats.CHARM_CORE.getType(), CHARM);
        registry.registerStatType(LaserMediumMaterialStats.TYPE, LASER_GUN);
        registry.registerStatType(BatteryCellMaterialStats.TYPE, LASER_GUN);
    }
}
