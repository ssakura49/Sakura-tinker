package com.ssakura49.sakuratinker.utils;

import net.minecraftforge.fml.ModList;

public class ModListUtil {
    public static class modName{
        public static String Avaritia = "avaritia";
        public static String YHKC = "youkaishomecoming";
        public static String EnigmaticLegacy = "enigmaticlegacy";
        public static String ISS = "irons_spellbooks";
        public static String TF = "twilightforest";
        public static String Curios = "curios";
        public static String DraconicEvolution = "draconicevolution";
        public static String IceAndFire = "iceandfire";
        public static String Botania = "botania";
        public static String TinkersCalibration = "tinkerscalibration";
        public static String ExtraBotany = "extrabotany";
        public static String ClouderTinker = "cloudertinker";
        public static String DreadSteel = "dreadsteel";
        public static String Goety = "goety";
        public static String Ember = "embers";
    }
    public static boolean AvaritiaLoaded = ModList.get().isLoaded(modName.Avaritia);
    public static boolean YHKCLoaded = ModList.get().isLoaded(modName.YHKC);
    public static boolean EnigmaticLegacyLoaded = ModList.get().isLoaded(modName.EnigmaticLegacy);
    public static boolean ISSLoaded = ModList.get().isLoaded(modName.ISS);
    public static boolean TFLoaded = ModList.get().isLoaded(modName.TF);
    public static boolean CuriosLoaded = ModList.get().isLoaded(modName.Curios);
    public static boolean DraconicEvolution = ModList.get().isLoaded(modName.DraconicEvolution);
    public static boolean IceAndFire = ModList.get().isLoaded(modName.IceAndFire);
    public static boolean Botania = ModList.get().isLoaded(modName.Botania);
    public static boolean TinkersCalibration = ModList.get().isLoaded(modName.TinkersCalibration);
    public static boolean ExtraBotany = ModList.get().isLoaded(modName.ExtraBotany);
    public static boolean ClouderTinker = ModList.get().isLoaded(modName.ClouderTinker);
    public static boolean DreadSteel = ModList.get().isLoaded(modName.DreadSteel);
    public static boolean Goety = ModList.get().isLoaded(modName.Goety);
    public static boolean Ember = ModList.get().isLoaded(modName.Ember);
}
