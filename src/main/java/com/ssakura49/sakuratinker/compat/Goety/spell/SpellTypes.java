package com.ssakura49.sakuratinker.compat.Goety.spell;

import com.ssakura49.sakuratinker.utils.SafeClassUtil;
import net.minecraft.network.chat.Component;

import java.lang.reflect.Method;
import java.util.Locale;

public class SpellTypes {
    public record SpellTypeWrapper(Object backing, Component name) {
        public String id() {
            return backing instanceof Enum<?> e ? e.name().toLowerCase(Locale.ROOT) : String.valueOf(backing);
        }
    }

    public static SpellTypeWrapper TINKER;

    public static void init() {
        if (SafeClassUtil.GoetyLoaded) {
            try {
                Class<?> enumClass = Class.forName("com.Polarice3.Goety.api.magic.SpellType");
                Method createMethod = enumClass.getDeclaredMethod("create", String.class, String.class);
                Object enumInstance = createMethod.invoke(null, "tinker", "tinker");

                TINKER = new SpellTypeWrapper(enumInstance,
                        Component.translatable("spell.goety.tinker"));
            } catch (Exception e) {
                e.printStackTrace();
                fallback();
            }
        } else {
            fallback();
        }
    }

    private static void fallback() {
        TINKER = new SpellTypeWrapper("tinker", Component.literal("tinker"));
    }
}
