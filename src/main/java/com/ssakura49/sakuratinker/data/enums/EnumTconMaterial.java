package com.ssakura49.sakuratinker.data.enums;

import slimeknights.tconstruct.library.materials.definition.MaterialId;
import slimeknights.tconstruct.tools.data.material.MaterialIds;

public enum EnumTconMaterial {
    bone(EnumTconExtraStat.bone, MaterialIds.bone),
    cobalt(EnumTconExtraStat.cobalt, MaterialIds.cobalt),
    copper(EnumTconExtraStat.copper, MaterialIds.copper),
    manyullyn(EnumTconExtraStat.manyullyn, MaterialIds.manyullyn),
    wood(EnumTconExtraStat.wood, MaterialIds.wood),
    skyslime_vine(EnumTconExtraStat.skyslime_vine, MaterialIds.skyslimeVine),
    nahualt(EnumTconExtraStat.nahualt, MaterialIds.nahuatl),
    iron(EnumTconExtraStat.iron, MaterialIds.iron),
    ;
    public final EnumTconExtraStat stats;
    public final EnumMaterialModifier[] modifiers;
    public final MaterialId id;

    EnumTconMaterial(EnumTconExtraStat stats, MaterialId id, EnumMaterialModifier... modifiers) {
        this.stats = stats;
        this.modifiers = modifiers;
        this.id = id;
    }
}
