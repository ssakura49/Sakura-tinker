package com.ssakura49.sakuratinker.utils.java;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.coremod.SakuraTinkerCore;
import io.netty.util.internal.shaded.org.jctools.util.UnsafeAccess;
import sun.misc.Unsafe;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class RuntimeEnumExtender<T extends Enum<?>> {
    private static final Unsafe unsafe = UnsafeAccess.UNSAFE;
    public Class<T> enumClass;
    public MHCall invoker;
    public volatile int countToAdd = 0;
    public RuntimeEnumExtender(Class<T> enumClass, MHCall invoker) {
        this.enumClass = enumClass;
        this.invoker = invoker;
    }
    public T createEnum(String id, Class<?>[] visibleArgTypes) throws Throwable {
        T[] VALUES = findEnumVALUES();
        for (T t : VALUES)
            if (t.name().equals(id))
                return t;
        Map<String, T> enumConstantDirectory = findEnumConstantDirectory();
        if (enumConstantDirectory != null) {
            cleanEnumCache(enumClass);
        }
        VALUES = Arrays.copyOf(VALUES, VALUES.length+1);
        T newElement ;
        if (visibleArgTypes == null)
            newElement = (T) findConstructor(enumClass, visibleArgTypes).invoke(id, VALUES.length-1);
        else newElement = (T) invoker.invoke(findConstructor(enumClass, visibleArgTypes), id, VALUES.length-1);
        VALUES[VALUES.length-1] = newElement;
        Field field = findEnumVALUESField();
        setField(field, enumClass, VALUES);
        countToAdd++;
        return newElement;
    }
    public T createEnum(String id, Class<?>[] visibleArgTypes, MHCall invoker) throws Throwable {
        this.invoker = invoker;
        return this.createEnum(id, visibleArgTypes);
    }
    public void dump() throws NoSuchFieldException, IllegalAccessException {
        SakuraTinker.outArray(findEnumVALUES());
        Map<String, T> enumConstantDirectory = findEnumConstantDirectory();
        if (enumConstantDirectory != null) {
            enumConstantDirectory.forEach((k, v) -> {
                SakuraTinker.out("Key:%s (%s) - Value:%s (%s)", k, k.getClass(), v, v.getClass());
            });
        }
    }
    public MethodHandle findConstructor(Class<?> enumClass, Class<?>[] visibleArgTypes) throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException {
        MethodHandle methodHandle;
        if (visibleArgTypes != null) {
            Class<?>[] copy = Arrays.copyOf(visibleArgTypes, visibleArgTypes.length+2);
            System.arraycopy(visibleArgTypes, 0, copy, 2, visibleArgTypes.length);
            copy[0] = String.class;
            copy[1] = int.class;
            methodHandle = InstrumentationHelper.IMPL_LOOKUP().findConstructor(enumClass, MethodType.methodType(void.class, copy));
        }
        else methodHandle = InstrumentationHelper.IMPL_LOOKUP().findConstructor(enumClass, MethodType.methodType(void.class, String.class, int.class));
        return methodHandle;
    }
    public T[] findEnumVALUES() throws NoSuchFieldException, IllegalAccessException {
        return (T[]) InstrumentationHelper.IMPL_LOOKUP().unreflectVarHandle(findEnumVALUESField()).get();
    }
    public Field findEnumVALUESField() throws NoSuchFieldException, IllegalAccessException {
        return enumClass.getDeclaredField("$VALUES");
    }
    public Map<String, T> findEnumConstantDirectory() throws NoSuchFieldException, IllegalAccessException {
        VarHandle varHandle = InstrumentationHelper.IMPL_LOOKUP().findVarHandle(Class.class, "enumConstantDirectory", Map.class);
        return (Map<String, T>) varHandle.get(enumClass);
    }
    public interface MHCall {
        Object invoke(MethodHandle methodHandle, final String id, final int ordinal) throws Throwable;
    }
    // Make sure we don't crash if any future versions change field names
    private static Optional<Field> findField(Class<?> clazz, String name)
    {
        for (Field f : clazz.getDeclaredFields())
        {
            if (f.getName().equals(name))
            {
                return Optional.of(f);
            }
        }
        return Optional.empty();
    }
    public static void setField(Field data, Class<?> object, Object value)  {
        try {
            long offset = unsafe.staticFieldOffset(data);
            unsafe.putObject(object, offset, value);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throwable.printStackTrace(SakuraTinkerCore.stream);
            System.exit(-1);
        }
    }
    public static void cleanEnumCache(Class<? extends Enum<?>> enumClass) throws Throwable
    {
        findField(Class.class, "enumConstantDirectory").ifPresent(f -> setField(f, enumClass, null));
        findField(Class.class, "enumConstants").ifPresent(f -> setField(f, enumClass, null));
    }
}
