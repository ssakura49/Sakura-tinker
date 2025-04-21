package com.ssakura49.sakuratinker.utils;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.ModList;

public class LoadingUtils {
    public LoadingUtils() {
    }

    public static void error(String info) {
        LogUtils.getLogger().error("sakuratinker_info {}", info);
    }

    public static void info(String info) {
        LogUtils.getLogger().info("sakuratinker_info {}", info);
    }

    public static boolean isLoad(String mod) {
        return ModList.get().isLoaded(mod);
    }

}
