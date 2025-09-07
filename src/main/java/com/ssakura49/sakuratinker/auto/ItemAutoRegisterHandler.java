package com.ssakura49.sakuratinker.auto;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.auto.anno.AutoItemRegister;
import com.ssakura49.sakuratinker.utils.java.InstrumentationHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ClipContext;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.lang.invoke.MethodType;
import java.util.*;
import java.util.stream.Stream;

public class ItemAutoRegisterHandler extends AbsAutoRegisterHandler<Item> {

    public ItemAutoRegisterHandler(DeferredRegister<Item> deferredRegister, String packageName) {
        super(deferredRegister, packageName);
    }

    public void classInit(String path) throws ClassNotFoundException {
        List<String> names = SakuraTinker.autoRegNamesInPackage(path);
        for (String className : names) {
            className = SakuraTinker.transformClassName(className);
            SakuraTinker.out(className);
            Class<?> c = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            if (c.isAnnotationPresent(AutoItemRegister.class) && ModList.get().isLoaded(c.getDeclaredAnnotation(AutoItemRegister.class).dependsOn())) {
                objectClassesNames.add(className);
                SakuraTinker.out("Visit new Auto Item Register Class : %s, class loader %s", className, c.getClassLoader());
            }
        }
    }


    public void handle() {
        try {
            classInit(packageName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        objectClassesNames.forEach(s -> {
            try {
                Class<?> clazz = Class.forName(s);
                if (!c2roMap.containsKey(clazz)) {
                    if (checkAnnotation(clazz)) {
                        SakuraTinker.out("[WARNING]%s is a block item, will not registered in this handler %s.", clazz.getSimpleName(), this);
                    } else {
                        RegistryObject<Item> itemRegistryObject = deferredRegister.register(clazz.getDeclaredAnnotation(AutoItemRegister.class).value(), () -> {
                            try {
                                return (Item) InstrumentationHelper.IMPL_LOOKUP().findConstructor(clazz, MethodType.methodType(void.class)).invoke();
                            } catch (Throwable e) {
                                return null;
                            }
                        });
                        c2roMap.put(clazz, itemRegistryObject);
                        SakuraTinker.out("%s registered.", clazz);
                    }
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                System.out.println(s);
            }
        });
    }
    private static boolean isBlockItem(Class<?> clazz) {
        try {
            clazz.getConstructor(ClipContext.Block.class);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
    public static boolean checkAnnotation(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(AutoItemRegister.class))
            throw new NullPointerException("Class " + clazz + " doesn't has AutoItemRegister annotation");
        if (clazz.getDeclaredAnnotation(AutoItemRegister.class).isBlockItem())
            return true;
        return false;
    }

    public Item get(Class<?> clazz) {
        return c2roMap.get(clazz).get();
    }

    @Override
    public Stream<Item> getAll() {
        return c2roMap.values().stream().map((ro)-> ro.orElseGet(()-> null)).filter(Objects::nonNull).sorted(Comparator.comparingInt(l -> l.getClass().getDeclaredAnnotation(AutoItemRegister.class).priority()));
    }

    public RegistryObject<Item> getRegistryObject(Class<?> clazz) {
        return c2roMap.get(clazz);
    }

    public Stream<Item> sorted2getAll(String tabName) {
        final Map<Class<?>, RegistryObject<Item>> copy = new HashMap<>();
        synchronized (c2roMap) {
            for (Class<?> key : c2roMap.keySet()) {
                try {
                    if (key.getDeclaredAnnotation(AutoItemRegister.class).tabName().equals(tabName))
                        copy.put(key, c2roMap.get(key));
                } catch (Throwable e) {
                    SakuraTinker.out("ErrorClass:"+key);
                    SakuraTinker.out("Annotations:%s", Arrays.toString(key.getDeclaredAnnotations()));
                }
            }
        }
        return copy.values().stream()
                .map((ro)-> ro.orElseGet(()-> null))
                .filter(Objects::nonNull)
                .filter(item -> item.getClass().getDeclaredAnnotation(AutoItemRegister.class).addIntoTabs())
                .sorted(Comparator.comparing(o -> o.getClass().getSimpleName()))
                .sorted(Comparator.comparingInt(l -> l.getClass().getDeclaredAnnotation(AutoItemRegister.class).priority()));
    }
}