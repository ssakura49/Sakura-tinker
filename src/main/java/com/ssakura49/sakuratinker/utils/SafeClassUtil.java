package com.ssakura49.sakuratinker.utils;

import net.irisshaders.iris.api.v0.IrisApi;
import net.minecraftforge.fml.ModList;

public class SafeClassUtil {
    public static class Modid {
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
        public static String ModernUI = "modernui";
        public static String Iris = "oculus";
        public static String BuddyCards = "buddycards";
        public static String GoetyRevelation = "goety_revelation";
        public static String Eeeabsmobs = "eeeabsmobs";
    }
    public static boolean AvaritiaLoaded = ModList.get().isLoaded(Modid.Avaritia);
    public static boolean YHKCLoaded = ModList.get().isLoaded(Modid.YHKC);
    public static boolean EnigmaticLegacyLoaded = ModList.get().isLoaded(Modid.EnigmaticLegacy);
    public static boolean ISSLoaded = ModList.get().isLoaded(Modid.ISS);
    public static boolean TFLoaded = ModList.get().isLoaded(Modid.TF);
    public static boolean CuriosLoaded = ModList.get().isLoaded(Modid.Curios);
    public static boolean DraconicEvolutionLoaded = ModList.get().isLoaded(Modid.DraconicEvolution);
    public static boolean IceAndFireLoaded = ModList.get().isLoaded(Modid.IceAndFire);
    public static boolean BotaniaLoaded = ModList.get().isLoaded(Modid.Botania);
    public static boolean TinkersCalibrationLoaded = ModList.get().isLoaded(Modid.TinkersCalibration);
    public static boolean ExtraBotanyLoaded = ModList.get().isLoaded(Modid.ExtraBotany);
    public static boolean ClouderTinkerLoaded = ModList.get().isLoaded(Modid.ClouderTinker);
    public static boolean DreadSteelLoaded = ModList.get().isLoaded(Modid.DreadSteel);
    public static boolean GoetyLoaded = ModList.get().isLoaded(Modid.Goety);
    public static boolean EmberLoaded = ModList.get().isLoaded(Modid.Ember);
    public static boolean ModernUILoaded = ModList.get().isLoaded(Modid.ModernUI);
    public static boolean IrisLoaded = ModList.get().isLoaded(Modid.Iris);
    public static boolean BuddyCardLoaded = ModList.get().isLoaded(Modid.BuddyCards);
    public static boolean GoetyRevelationLoaded = ModList.get().isLoaded(Modid.GoetyRevelation);
    public static boolean EeeabsmobsLoaded = ModList.get().isLoaded(Modid.Eeeabsmobs);

    public static boolean usingShaderPack() {
        if (IrisLoaded)
            return IrisApi.getInstance().isShaderPackInUse();
        else return false;
    }
}
