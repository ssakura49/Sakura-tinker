package com.ssakura49.sakuratinker.auto;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.CustomInLevelRendererDispatcher;
import com.ssakura49.sakuratinker.client.CustomTextureParticleRenderer;
import com.ssakura49.sakuratinker.client.InCommandCustomRenderer;
import com.ssakura49.sakuratinker.register.RendererRegister;
import com.ssakura49.sakuratinker.utils.java.InstrumentationHelper;

import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CILAutoRegisterHandler {
    public static ArrayList<String> ItemClassesName = new ArrayList<>();
    public static Map<Class<?>, InCommandCustomRenderer> map = new HashMap<>();
    public static Map<Class<? extends CustomTextureParticleRenderer>, CustomTextureParticleRenderer> textureParticles = new HashMap<>();

    static void classInit(String path) throws ClassNotFoundException {
        List<String> names = SakuraTinker.autoRegNamesInPackage(path);
        for (String className : names) {
            Class<?> c = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            if (c.isAnnotationPresent(CustomRendererAttributes.class)) {
                ItemClassesName.add(className.replace('/', '.'));
                SakuraTinker.out("Visit new CustomRenderer Register Class : %s, class loader %s", className, c.getClassLoader());
            }
        }
    }

    public static void handle(String packageResource) {
        try {
            classInit(packageResource);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        ItemClassesName.forEach(s -> {
            try {
                Class<?> clazz = Class.forName(s);
                if (!map.containsKey(clazz)) {
                    checkAnnotation(clazz);
                    try {
                        Object target = InstrumentationHelper.IMPL_LOOKUP().findConstructor(clazz, MethodType.methodType(void.class)).invoke();
                        InCommandCustomRenderer renderer = new InCommandCustomRenderer(target);
                        RendererRegister.registerCustomRenderer(renderer);
                        map.put(clazz, renderer);
                        if (clazz.getSuperclass().getName().equals(CustomTextureParticleRenderer.class.getName())) {
                            Class<? extends CustomTextureParticleRenderer> textureParticleClass = (Class<? extends CustomTextureParticleRenderer>) clazz;
                            textureParticles.put(textureParticleClass, (CustomTextureParticleRenderer) target);
                            CustomInLevelRendererDispatcher.initTextureParticle(textureParticleClass);
                        }
                    } catch (Throwable e) {
                        throw new RuntimeException(e);
                    }
                    SakuraTinker.out("%s registered.", clazz);
                }
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public static void checkAnnotation(Class<?> clazz) {
        if (!clazz.isAnnotationPresent(CustomRendererAttributes.class))
            throw new NullPointerException("Class " + clazz + " doesn't has CustomRendererAttributes annotation");
    }

    public static InCommandCustomRenderer get(Class<?> clazz) {
        return map.get(clazz);
    }
}
