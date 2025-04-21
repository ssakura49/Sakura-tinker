package com.ssakura49.sakuratinker.data;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public class STMaterialId {
    private static MaterialId createMaterial(String name) {return new MaterialId(new ResourceLocation(SakuraTinker.MODID, name));}
    public static final MaterialId soul_sakura = createMaterial("soul_sakura");
    public static final MaterialId nihilite = createMaterial("nihilite");
    public static final MaterialId eezo = createMaterial("eezo");

    public static class YoukaiHomeComing {
        public static final MaterialId youkai = createMaterial("youkai");
        public static final MaterialId fairy_ice_crystal = createMaterial("fairy_ice_crystal");
    }
    public static class TwilightForest {
        public static final MaterialId fiery_crystal = createMaterial("fiery_crystal");
    }
    public static class EnigmaticLegacy {
        public static final MaterialId etherium = createMaterial("etherium");
    }
    public static class ReAvaritia {
        public static final MaterialId infinity = createMaterial("infinity");
        public static final MaterialId neutron = createMaterial("neutron");
        public static final MaterialId colorful = createMaterial("colorful");
    }
    public static class IronSpellBook {
        public static final MaterialId arcane_alloy = createMaterial("arcane_alloy");
    }
}
