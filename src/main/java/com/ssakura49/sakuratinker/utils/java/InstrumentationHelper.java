package com.ssakura49.sakuratinker.utils.java;

import com.ssakura49.sakuratinker.coremod.SakuraTinkerCore;
import com.ssakura49.sakuratinker.utils.MinecraftInstHandler;
import sun.misc.Unsafe;

import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

public final class InstrumentationHelper {
    public static int times = 0;
    public static Unsafe unsafe;
    public static Instrumentation inst;

    public static MethodHandles.Lookup IMPL_LOOKUP() throws NoSuchFieldException, IllegalAccessException {
        //return (MethodHandles.Lookup) getDsDamage(MethodHandles.Lookup.class, "IMPL_LOOKUP", MethodHandles.Lookup.class);
        Field f = MethodHandles.Lookup.class.getDeclaredField("IMPL_LOOKUP");
        Field uf = Unsafe.class.getDeclaredField("theUnsafe");
        uf.setAccessible(true);
        Unsafe unsafe = (Unsafe) uf.get(null);
        return (MethodHandles.Lookup) unsafe.getObject(MethodHandles.Lookup.class, unsafe.staticFieldOffset(f));
        //long offset = UnsafeUtil.getUnsafe().staticFieldOffset(f);
        //return (MethodHandles.Lookup) UnsafeUtil.getUnsafe().getObject(UnsafeUtil.getUnsafe().staticFieldBase(f), offset);
    }

    public static Unsafe unsafe() {
        try {
            if (unsafe == null) {
                Field field = Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                Unsafe u = (Unsafe) field.get(null);
                unsafe = u;
            }
            return unsafe;
        } catch (Throwable throwable) {
            throw new RuntimeException(throwable);
        }
    }

    public static Instrumentation getInstrumentation() {
        /*
        try {
            PathSetter.appendToClassPathForInstrumentation();
            Class<?> real = ClassLoader.getSystemClassLoader().loadClass("com.mega.uom.util.java.InstrumentationHelper");
            allowAttachSelfAndInject();
            ModSource.out("Agent Attach Task succeeded");
            times++;
            VarHandle varHandle = IMPL_LOOKUP().findStaticVarHandle(real, "inst", Instrumentation.class);
            return (Instrumentation) varHandle.get();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
         */
        return MinecraftInstHandler.getInstrumentation();
    }

    public static void allowAttachSelf() throws IllegalAccessException, ClassNotFoundException, NoSuchFieldException {
        Field unsafeF = Unsafe.class.getDeclaredField("theUnsafe");
        unsafeF.setAccessible(true);
        Unsafe unsafe = (Unsafe) unsafeF.get(null);
        Class<?> vmClass = Class.forName("sun.tools.attach.HotSpotVirtualMachine");
        Field allowAttachSelf = vmClass.getDeclaredField("ALLOW_ATTACH_SELF");
        unsafe.putObject(unsafe.staticFieldBase(allowAttachSelf), unsafe.staticFieldOffset(allowAttachSelf), Boolean.valueOf(true));
        SakuraTinkerCore.stream.println("Allowed attach self VM.");
        System.out.println("Allowed attach self VM.");
    }

    public static AccessibleObject setAccessible(AccessibleObject f, boolean flag) {
        try {
            MethodHandles.Lookup IMPL_LOOKUP = IMPL_LOOKUP();
            MethodHandle mh = IMPL_LOOKUP.findVirtual(AccessibleObject.class, "setAccessible0", MethodType.methodType(boolean.class, boolean.class));
            mh.bindTo(f).invoke(flag);
            return f;
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
}
