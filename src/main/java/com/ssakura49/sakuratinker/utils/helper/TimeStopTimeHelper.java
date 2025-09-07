package com.ssakura49.sakuratinker.utils.helper;

import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TimeStopTimeHelper {
    public static final List<TimeStopTimeHelper> set = Collections.synchronizedList(new ArrayList<>());
    public TimeStopTimeHelperTask task;
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

    public TimeStopTimeHelper(int min, int max) {
        this();
        min_i = min;
        max_i = max;
    }

    public TimeStopTimeHelper(double min, double max) {
        this();
        min_d = min;
        max_d = max;
    }

    public TimeStopTimeHelper() {
        set.add(this);
    }

    public static TimeStopTimeHelper create(TimeStopTimeHelper timeHelper, int min, int max) {
        if (timeHelper == null)
            return new TimeStopTimeHelper(min, max).setRunning(true);
        return timeHelper;
    }

    public TimeStopTimeHelper setOnlyIncrease(boolean b) {
        onlyIncrease = b;
        return this;
    }

    public TimeStopTimeHelper setTask(TimeStopTimeHelperTask task) {
        this.task = task;
        return this;
    }

    public TimeStopTimeHelper setRunning(boolean running) {
        this.running = running;
        return this;
    }

    @Override
    public String toString() {
        if (min_d != 0d && max_d != 0D)
            return "TimeHelper[" + min_d + "," + max_d + "]";
        return "TimeHelper[" + min_i + "," + max_i + "]";
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