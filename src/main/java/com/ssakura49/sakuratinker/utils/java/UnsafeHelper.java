package com.ssakura49.sakuratinker.utils.java;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Map;

public class UnsafeHelper {
    public static void putAnnValue(Annotation annotation, String key, Object value) throws NoSuchFieldException {
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(annotation);
        Field field = invocationHandler.getClass().getDeclaredField("memberValues");
        Map<String, Object> map = ((Map<String, Object>) InstrumentationHelper.unsafe().getObjectVolatile(invocationHandler, InstrumentationHelper.unsafe().objectFieldOffset(field)));
        map.put(key, value);
    }
}
