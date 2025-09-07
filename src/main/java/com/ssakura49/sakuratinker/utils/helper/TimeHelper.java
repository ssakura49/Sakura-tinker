package com.ssakura49.sakuratinker.utils.helper;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class TimeHelper {
    public static final List<TimeHelper> set = Collections.synchronizedList(new ArrayList<>());
    public static final List<Runnable> clientSideQueue = Collections.synchronizedList(new ArrayList<>());
    public TimeHelperTask task;
    public int min_i;
    public int max_i;
    public double min_d;
    public double max_d;
    public int integer_time;
    public float float_time;
    public boolean integer_logic;
    public boolean double_logic;
    public boolean running = true;
    public boolean onlyIncrease = false;
    public String[] caller;

    public TimeHelper(int min, int max) {
        this();
        min_i = min;
        max_i = max;
    }

    public TimeHelper(double min, double max) {
        this();
        min_d = min;
        max_d = max;
    }

    public TimeHelper() {
        set.add(this);
        //caller = ModSource.getCallerClassNames().toArray(new String[0]);
    }

    public static TimeHelper create(TimeHelper timeHelper, int min, int max) {
        if (timeHelper == null)
            return new TimeHelper(min, max).setRunning(true);
        return timeHelper;
    }

    public TimeHelper setOnlyIncrease(boolean b) {
        onlyIncrease = b;
        return this;
    }

    public TimeHelper setTask(TimeHelperTask task) {
        this.task = task;
        return this;
    }

    public TimeHelper setRunning(boolean running) {
        this.running = running;
        return this;
    }

    @Override
    public String toString() {
        String s = caller == null ? "" : Arrays.toString(caller);
        if (min_d != 0d && max_d != 0D)
            return "TimeHelper[" + min_d + "," + max_d + "]" + s;
        return "TimeHelper[" + min_i + "," + max_i + "]" + s;
    }

    public boolean aSecond() {
        return integer_time % 100 == 0;
    }

    public void renderTick() {
        if (!running || (Minecraft.getInstance().pause))
            return;
        if (task != null)
            task.run(this);
        if (max_i > min_i) {
            if (onlyIncrease) {
                if (integer_time < max_i)
                    integer_time++;
            } else {
                if (!integer_logic) {
                    if (integer_time > min_i)
                        integer_time--;
                    if (integer_time <= min_i)
                        integer_logic = true;
                }
                if (integer_logic) {
                    if (integer_time < max_i)
                        integer_time++;
                    if (integer_time >= max_i)
                        integer_logic = false;
                }
            }
        } else integer_time++;
        if (max_d > min_d) {
            if (!double_logic) {
                if (float_time > min_d) {
                    float_time--;
                }
                if (float_time <= min_d)
                    double_logic = true;
            }
            if (double_logic) {
                if (float_time < max_d)
                    float_time++;
                if (float_time >= max_d)
                    double_logic = false;
            }
        } else float_time++;
    }
}
