package com.ssakura49.sakuratinker.auto;

import com.ssakura49.sakuratinker.utils.java.NonNullHashMap;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public abstract class AbsAutoRegisterHandler<T> {
    public final DeferredRegister<T> deferredRegister;
    public final String packageName;
    public final NonNullHashMap<Class<?>, RegistryObject<T>> c2roMap = new NonNullHashMap<>();
    public List<String> objectClassesNames = new ArrayList<>();
    public abstract void classInit(String path) throws ClassNotFoundException;
    public abstract void handle();
    public abstract T get(Class<?> clazz);

    public abstract Stream<T> getAll();
    public abstract RegistryObject<T> getRegistryObject(Class<?> clazz);
    public AbsAutoRegisterHandler(DeferredRegister<T> deferredRegister, String packageName) {
        this.deferredRegister = deferredRegister;
        this.packageName = packageName;
        AutoRegisterManager.INSTANCE().getHandlers().add(this);
    }
}