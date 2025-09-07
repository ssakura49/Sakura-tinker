package com.ssakura49.sakuratinker.coremod;

import com.ssakura49.sakuratinker.utils.java.InstrumentationHelper;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

public class PathSetter {
    private static final String p = (new File(PathSetter.class.getProtectionDomain().getCodeSource().getLocation().getPath())).getAbsolutePath();

    public static String JAR = p.substring(0, p.lastIndexOf("%"));

    public static void appendToClassPathForInstrumentation() throws Throwable {
        appendToClassPathForInstrumentation(JAR);
    }

    public static void appendToClassPathForInstrumentation(String path) throws Throwable {
        Class<?> loaderClass = ClassLoader.getSystemClassLoader().getClass();
        MethodHandle methodHandle = InstrumentationHelper.IMPL_LOOKUP().findVirtual(loaderClass, "appendToClassPathForInstrumentation", MethodType.methodType(void.class, String.class));
        methodHandle.bindTo(ClassLoader.getSystemClassLoader()).invoke(path);
    }
}
