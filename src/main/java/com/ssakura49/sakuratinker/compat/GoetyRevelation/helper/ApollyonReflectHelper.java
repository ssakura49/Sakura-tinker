package com.ssakura49.sakuratinker.compat.GoetyRevelation.helper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ApollyonReflectHelper {
    private static Class<?> apollyonInterfaceClass;
    private static Method getHitCooldownMethod;
    private static Method setHitCooldownMethod;
    private static Byte reviveEventValue;

    private static Class<?> apollyonAbilityHelperClass;
    private static Method isApollyonMethod;
    private static Method getHitCooldownAbilityMethod;
    private static Method setHitCooldownAbilityMethod;
    private static Method getApollyonTimeMethod;
    private static Method setApollyonTimeMethod;

    static {
        try {
            apollyonInterfaceClass = Class.forName("com.mega.revelationfix.safe.entity.Apollyon2Interface");
            getHitCooldownMethod = apollyonInterfaceClass.getMethod("revelaionfix$getHitCooldown");
            setHitCooldownMethod = apollyonInterfaceClass.getMethod("revelaionfix$setHitCooldown", int.class);
            Class<?> ctxClass = Class.forName("com.mega.revelationfix.safe.OdamanePlayerExpandedContext");
            Field reviveField = ctxClass.getField("REVIVE_EVENT");
            Object value = reviveField.get(null);
            if (value instanceof Byte b) {
                reviveEventValue = b;
            } else {
                System.err.println("[Sakura Tinker:ApollyonReflectHelper] REVIVE_EVENT 不是 byte 类型！");
            }
            apollyonAbilityHelperClass = Class.forName("z1gned.goetyrevelation.util.ApollyonAbilityHelper");
            isApollyonMethod = apollyonAbilityHelperClass.getMethod("allTitlesApostle_1_20_1$isApollyon");
            getHitCooldownAbilityMethod = apollyonAbilityHelperClass.getMethod("allTitlesApostle_1_20_1$getHitCooldown");
            setHitCooldownAbilityMethod = apollyonAbilityHelperClass.getMethod("allTitlesApostle_1_20_1$setHitCooldown", int.class);
            getApollyonTimeMethod = apollyonAbilityHelperClass.getMethod("getApollyonTime");
            setApollyonTimeMethod = apollyonAbilityHelperClass.getMethod("setApollyonTime", int.class);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static int getHitCooldown(Object apollyonEntity) {
        try {
            return (int) getHitCooldownMethod.invoke(apollyonEntity);
        } catch (Throwable t) {
            t.printStackTrace();
            return 0;
        }
    }

    public static void setHitCooldown(Object apollyonEntity, int cooldown) {
        try {
            setHitCooldownMethod.invoke(apollyonEntity, cooldown);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    /** 获取 REVIVE_EVENT 对应的 byte */
    public static byte getReviveEvent() {
        if (reviveEventValue != null) {
            return reviveEventValue;
        }
        return (byte) 0; // fallback
    }

    public static boolean isApollyon(Object apollyonEntity) {
        try {
            return (boolean) isApollyonMethod.invoke(apollyonEntity);
        } catch (Throwable t) {
            t.printStackTrace();
            return false;
        }
    }

    public static int getAbilityHitCooldown(Object apollyonEntity) {
        try {
            return (int) getHitCooldownAbilityMethod.invoke(apollyonEntity);
        } catch (Throwable t) {
            t.printStackTrace();
            return 0;
        }
    }

    public static void setAbilityHitCooldown(Object apollyonEntity, int cooldown) {
        try {
            setHitCooldownAbilityMethod.invoke(apollyonEntity, cooldown);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static int getApollyonTime(Object apollyonEntity) {
        try {
            return (int) getApollyonTimeMethod.invoke(apollyonEntity);
        } catch (Throwable t) {
            t.printStackTrace();
            return 0;
        }
    }

    public static void setApollyonTime(Object apollyonEntity, int time) {
        try {
            setApollyonTimeMethod.invoke(apollyonEntity, time);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
