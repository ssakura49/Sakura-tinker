package com.ssakura49.sakuratinker.data.generator;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.resources.ResourceLocation;
import slimeknights.tconstruct.library.materials.definition.MaterialId;

public class STMaterialId {
    private static MaterialId createMaterial(String name) {return new MaterialId(ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, name));}
    public static final MaterialId soul_sakura = createMaterial("soul_sakura");
    public static final MaterialId nihilite = createMaterial("nihilite");
    public static final MaterialId eezo = createMaterial("eezo");
    public static final MaterialId blood_bound_steel = createMaterial("blood_bound_steel");
    public static final MaterialId steady_alloy = createMaterial("steady_alloy");
    public static final MaterialId south_star = createMaterial("south_star");
    public static final MaterialId terracryst = createMaterial("terracryst");
    public static final MaterialId prometheum = createMaterial("prometheum");
    public static final MaterialId orichalcum = createMaterial("orichalcum");
    public static final MaterialId aurumos = createMaterial("aurumos");
    public static final MaterialId bear_interest = createMaterial("bear_interest");
    public static final MaterialId mycelium_slimesteel = createMaterial("mycelium_slimesteel");
    public static final MaterialId frost_slimesteel = createMaterial("frost_slimesteel");
    public static final MaterialId echo_slimesteel = createMaterial("echo_slimesteel");
    public static final MaterialId goozma = createMaterial("goozma");
    public static final MaterialId pyrothium = createMaterial("pyrothium");
    public static final MaterialId chimera_gamma = createMaterial("chimera_gamma");
    public static final MaterialId dragon_sinew = createMaterial("dragon_sinew");
    public static final MaterialId delusion = createMaterial("delusion");
    public static final MaterialId gluttonous = createMaterial("gluttonous");
    public static final MaterialId cold_iron_alloy = createMaterial("cold_iron_alloy");
    public static final MaterialId paper = createMaterial("paper");

    public static class YoukaiHomeComing {
        public static final MaterialId youkai = createMaterial("youkai");
        public static final MaterialId fairy_ice_crystal = createMaterial("fairy_ice_crystal");
    }
    public static class TwilightForest {
        public static final MaterialId fiery_crystal = createMaterial("fiery_crystal");
        public static final MaterialId raven_feather = createMaterial("raven_feather");
    }
    public static class EnigmaticLegacy {
        public static final MaterialId etherium = createMaterial("etherium");
        public static final MaterialId nefarious = createMaterial("nefarious");
    }
    public static class ReAvaritia {
        public static final MaterialId infinity = createMaterial("infinity");
        public static final MaterialId neutron = createMaterial("neutron");
        public static final MaterialId colorful = createMaterial("colorful");
        public static final MaterialId crystal_matrix = createMaterial("crystal_matrix");
    }
    public static class IronSpellBook {
        public static final MaterialId arcane_alloy = createMaterial("arcane_alloy");
    }
    public static class Botania{
        public static final MaterialId mana_steel = createMaterial("mana_steel");
        public static final MaterialId terra_steel = createMaterial("terra_steel");
        public static final MaterialId elementium = createMaterial("elementium");
        public static final MaterialId gaia = createMaterial("gaia");
        public static final MaterialId mana_string = createMaterial("mana_string");
        public static final MaterialId living_wood = createMaterial("living_wood");
        public static final MaterialId living_rock = createMaterial("living_rock");
    }
    public static class ExtraBotany{
        public static final MaterialId orichalcos = createMaterial("orichalcos");
        public static final MaterialId aerialite = createMaterial("aerialite");
        public static final MaterialId shadowium = createMaterial("shadowium");
        public static final MaterialId photonium = createMaterial("photonium");
        public static final MaterialId the_end = createMaterial("the_end");
    }
    public static class ClouderTinker{
        public static final MaterialId pyrothium = createMaterial("pyrothium");
    }
    public static class DreadSteel{
        public static final MaterialId dread_steel = createMaterial("dread_steel");
    }
    public static class Goety{
        public static final MaterialId cursed_metal = createMaterial("cursed_metal");
        public static final MaterialId dark_metal = createMaterial("dark_metal");
        public static final MaterialId unholy_alloy = createMaterial("unholy_alloy");
    }
    public static class IceAndFire{
        public static final MaterialId amphithere_feather = createMaterial("amphithere_feather");

        public static final MaterialId dragon_fire_steel = createMaterial("dragon_fire_steel");
        public static final MaterialId dragon_ice_steel = createMaterial("dragon_ice_steel");
        public static final MaterialId dragon_lightning_steel = createMaterial("dragon_lightning_steel");
    }
    public static class BuddyCard{
        public static final MaterialId buddysteel = createMaterial("buddysteel");
        public static final MaterialId charged_buddysteel = createMaterial("charged_buddysteel");
        public static final MaterialId crimson_buddysteel = createMaterial("crimson_buddysteel");
        public static final MaterialId void_buddysteel = createMaterial("void_buddysteel");
        public static final MaterialId perfect_buddysteel = createMaterial("perfect_buddysteel");
        public static final MaterialId true_perfect_buddysteel = createMaterial("true_perfect_buddysteel");
    }

    public static class GoetyRevelation {
        public static final MaterialId apocalyptium = createMaterial("apocalyptium");
    }

}
