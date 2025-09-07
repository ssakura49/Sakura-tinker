package com.ssakura49.sakuratinker.client;

import com.ssakura49.sakuratinker.auto.CustomRendererAttributes;
import com.ssakura49.sakuratinker.utils.java.InstrumentationHelper;
import com.ssakura49.sakuratinker.utils.java.UnsafeHelper;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IdGS {
    public static final Map<String, Integer> ids = new HashMap<>();
    public static volatile int currentID = 0;
    public static Field field;

    static {
        try {
            field = IdGS.class.getDeclaredField("currentID");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setID(Object renderer) {
        if (!isRenderer(renderer)) throw new NullPointerException("target " + renderer + " isn't a CustomRenderer");
        CustomRendererAttributes annotation = renderer instanceof Class<?> clazz ? clazz.getDeclaredAnnotation(CustomRendererAttributes.class) : renderer.getClass().getDeclaredAnnotation(CustomRendererAttributes.class);
        try {
            UnsafeHelper.putAnnValue(annotation, "id", currentID);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        InstrumentationHelper.unsafe().putIntVolatile(IdGS.class, InstrumentationHelper.unsafe().staticFieldOffset(field), InstrumentationHelper.unsafe().getIntVolatile(IdGS.class, InstrumentationHelper.unsafe().staticFieldOffset(field)) + 1);

    }

    public static int id(Class<?> rendererClass) {
        if (isRenderer(rendererClass)) {
            String name = rendererClass.getDeclaredAnnotation(CustomRendererAttributes.class).name();
            if (!ids.containsKey(name))
                ids.put(name, rendererClass.getDeclaredAnnotation(CustomRendererAttributes.class).id());
            return ids.get(name);
        }
        return -1;
    }

    public static int id(Object renderer) {
        if (isRenderer(renderer)) {
            String name = renderer.getClass().getDeclaredAnnotation(CustomRendererAttributes.class).name();
            if (!ids.containsKey(name))
                ids.put(name, renderer.getClass().getDeclaredAnnotation(CustomRendererAttributes.class).id());
            return ids.get(name);
        }
        return -1;
    }

    public static boolean isRenderer(Object o) {
        if (o instanceof Class<?> clazz) {
            return clazz.isAnnotationPresent(CustomRendererAttributes.class) &&
                    (Arrays.stream(clazz.getInterfaces())
                            .anyMatch(k -> (k.getName().equals(
                                    ICustomParticleRenderTask.class.getName()) ||
                                    k.getName().equals(ICustomInLevelRenderTask.class.getName())
                            ))
                    );
        }
        Class<?> clazz = o.getClass();
        return clazz.isAnnotationPresent(CustomRendererAttributes.class) && (
                o instanceof ICustomParticleRenderTask ||
                        o instanceof ICustomInLevelRenderTask);
    }
}
