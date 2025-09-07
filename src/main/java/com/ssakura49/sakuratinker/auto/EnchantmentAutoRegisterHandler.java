package com.ssakura49.sakuratinker.auto;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.auto.anno.AutoEnchantRegister;
import com.ssakura49.sakuratinker.utils.java.InstrumentationHelper;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.lang.invoke.MethodType;
import java.util.List;
import java.util.stream.Stream;

public class EnchantmentAutoRegisterHandler extends AbsAutoRegisterHandler<Enchantment> {
    public EnchantmentAutoRegisterHandler(DeferredRegister<Enchantment> deferredRegister, String packageName) {
        super(deferredRegister, packageName);
    }

    public void classInit(String path) throws ClassNotFoundException {
        List<String> names = SakuraTinker.autoRegNamesInPackage(path);
        for (String className : names) {
            className = SakuraTinker.transformClassName(className);
            SakuraTinker.out(className);
            Class<?> c = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            if (c.isAnnotationPresent(AutoEnchantRegister.class) && ModList.get().isLoaded(c.getDeclaredAnnotation(AutoEnchantRegister.class).dependsOn())) {
                objectClassesNames.add(className);
                SakuraTinker.out("Visit new Auto Enchantment Register Class : %s, class loader %s", className, c.getClassLoader());
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
                    checkAnnotation(clazz);
                    RegistryObject<Enchantment> enchantmentRegistryObject = deferredRegister.register(clazz.getDeclaredAnnotation(AutoEnchantRegister.class).value(), () -> {
                        try {
                            return (Enchantment) InstrumentationHelper.IMPL_LOOKUP().findConstructor(clazz, MethodType.methodType(void.class)).invoke();
                        } catch (Throwable e) {
                            throw new RuntimeException(e);
                        }
                    });
                    c2roMap.put(clazz, enchantmentRegistryObject);
                    SakuraTinker.out("%s registered.", clazz);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                System.out.println();
                System.out.println(s);
                System.out.println();
            }
        });
    }

    public static void checkAnnotation(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(AutoEnchantRegister.class))
            throw new NullPointerException("Class " + clazz + " doesn't has AutoEnchantRegister annotation");
    }

    public Enchantment get(Class<?> clazz) {
        return c2roMap.get(clazz).get();
    }

    public Stream<Enchantment> getAll() {
        return c2roMap.values().stream().map(RegistryObject::get);
    }

    @Override
    public RegistryObject<Enchantment> getRegistryObject(Class<?> clazz) {
        return c2roMap.get(clazz);
    }
}
