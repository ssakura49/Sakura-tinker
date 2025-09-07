package com.ssakura49.sakuratinker.coremod;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EarlyConfig {
    public static final boolean OPTIFINE_PRESENT;
    public static Set<String> modIds = null;


    static {
        modIds = new HashSet<>();
        LoadingModList loadingModList = FMLLoader.getLoadingModList();
        final List<List<IModInfo>> modInfos = loadingModList.getModFiles().stream()
                .map(ModFileInfo::getFile)
                .map(ModFile::getModInfos).toList();
        modIds = new HashSet<>();
        for (List<IModInfo> iModInfoList : modInfos) {
            for (IModInfo modInfo : iModInfoList)
                modIds.add(modInfo.getModId());
        }
        boolean hasOfClass = false;
        try {
            Class.forName("optifine.OptiFineTransformationService");
            hasOfClass = true;
        } catch(Throwable e) {
        }
        OPTIFINE_PRESENT = hasOfClass;
    }
    public static boolean isOptifinePresent() {
        return OPTIFINE_PRESENT || modIds.contains("optifine");
    }
}
