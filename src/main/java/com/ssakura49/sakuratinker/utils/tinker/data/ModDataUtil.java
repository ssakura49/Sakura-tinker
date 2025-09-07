package com.ssakura49.sakuratinker.utils.tinker.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;

/**
 * 工具类：用于安全读写 ModDataNBT 中的数据
 * 支持常见数据类型：long、float、int、boolean、String
 * 原理是使用 getCopy() 拷贝一份 CompoundTag，修改后再通过 copyFrom() 写回。
 */
public class ModDataUtil {

    /**
     * 读取 long 类型数据
     */
    public static long getLong(ModDataNBT data, ResourceLocation key) {
        if (!data.contains(key, 4)) {
            return 0L;
        }
        return data.get(key, CompoundTag::getLong);
    }

    /**
     * 写入 long 类型数据
     */
    public static void putLong(ModDataNBT data, ResourceLocation key, long value) {
        CompoundTag tag = data.getCopy();
        tag.putLong(key.toString(), value);
        data.copyFrom(tag);
    }

    /**
     * 读取 float 类型数据
     */
    public static float getFloat(ModDataNBT data, ResourceLocation key) {
        return data.getCopy().getFloat(key.toString());
    }

    /**
     * 写入 float 类型数据
     */
    public static void putFloat(ModDataNBT data, ResourceLocation key, float value) {
        CompoundTag tag = data.getCopy();
        tag.putFloat(key.toString(), value);
        data.copyFrom(tag);
    }

    /**
     * 读取 int 类型数据
     */
    public static int getInt(ModDataNBT data, ResourceLocation key) {
        return data.getCopy().getInt(key.toString());
    }

    /**
     * 写入 int 类型数据
     */
    public static void putInt(ModDataNBT data, ResourceLocation key, int value) {
        CompoundTag tag = data.getCopy();
        tag.putInt(key.toString(), value);
        data.copyFrom(tag);
    }

    /**
     * 读取 boolean 类型数据
     */
    public static boolean getBoolean(ModDataNBT data, ResourceLocation key) {
        return data.getCopy().getBoolean(key.toString());
    }

    /**
     * 写入 boolean 类型数据
     */
    public static void putBoolean(ModDataNBT data, ResourceLocation key, boolean value) {
        CompoundTag tag = data.getCopy();
        tag.putBoolean(key.toString(), value);
        data.copyFrom(tag);
    }

    /**
     * 读取字符串类型数据
     */
    @Nullable
    public static String getString(ModDataNBT data, ResourceLocation key) {
        CompoundTag tag = data.getCopy();
        return tag.contains(key.toString()) ? tag.getString(key.toString()) : null;
    }

    /**
     * 写入字符串类型数据
     */
    public static void putString(ModDataNBT data, ResourceLocation key, String value) {
        CompoundTag tag = data.getCopy();
        tag.putString(key.toString(), value);
        data.copyFrom(tag);
    }

    /**
     * 移除指定键的数据
     */
    public static void remove(ModDataNBT data, ResourceLocation key) {
        CompoundTag tag = data.getCopy();
        tag.remove(key.toString());
        data.copyFrom(tag);
    }

    /**
     * 判断是否包含某个键
     */
    public static boolean contains(ModDataNBT data, ResourceLocation key) {
        return data.getCopy().contains(key.toString());
    }
}
