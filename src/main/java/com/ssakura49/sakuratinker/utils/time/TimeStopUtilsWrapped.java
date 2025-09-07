package com.ssakura49.sakuratinker.utils.time;

import com.ssakura49.sakuratinker.utils.data.LevelExpandedContext;
import com.ssakura49.sakuratinker.utils.intfce.LevelEC;
import net.minecraft.client.Minecraft;

//Client
public class TimeStopUtilsWrapped {
    public static void enable() {
        //ModSource.out("enable");
        if (!TimeStopUtils.isTimeStop) {
            Minecraft mc = Minecraft.getInstance();
            //ModSource.out("enable0");
            assert mc.level != null;
            mc.tick();
            mc.soundManager.pause();
            TimeStopUtils.isTimeStop = true;
            LevelExpandedContext lec = ((LevelEC) mc.level).st$levelECData();
        }
    }

    public static void disable() {
        //ModSource.out("disable");
        if (TimeStopUtils.isTimeStop) {
            Minecraft mc = Minecraft.getInstance();
            //ModSource.out("disable0");
            TimeStopUtils.isTimeStop = false;
            Minecraft.getInstance().soundManager.resume();
        }
    }
}
