package com.ssakura49.sakuratinker.client;

import com.ssakura49.sakuratinker.auto.CustomRendererAttributes;
import net.minecraft.world.phys.Vec3;

public class InCommandCustomRenderer {
    public Object renderer;
    public Vec3 vec3;

    public InCommandCustomRenderer(Object renderer) {
        this.renderer = renderer;
        this.vec3 = new Vec3(0, 0, 0);
    }

    public static Vec3 getFromInLevel(Object obj) {
        ICustomInLevelRenderTask o = (ICustomInLevelRenderTask) obj;
        return new Vec3(o.getX(), o.getY(), o.getZ());
    }

    public static Vec3 getFromParticle(Object obj) {
        ICustomParticleRenderTask o = (ICustomParticleRenderTask) obj;
        return new Vec3(o.getX(), o.getY(), o.getZ());
    }

    public boolean isInLevelType() {
        return renderer instanceof ICustomInLevelRenderTask;
    }

    public boolean isParticleType() {
        return renderer instanceof ICustomParticleRenderTask;
    }

    public boolean both() {
        return isInLevelType() && isParticleType();
    }

    public void checkCommandUsage() {
        if (renderer.getClass().isAnnotationPresent(CustomRendererAttributes.class)) {
            CustomRendererAttributes ann = renderer.getClass().getDeclaredAnnotation(CustomRendererAttributes.class);
            if (!ann.canCommandUse())
                throw new NullPointerException("Selected custom renderer " + renderer.getClass() + " cannot be used in command!");
        } else
            throw new NullPointerException("Selected custom renderer " + renderer.getClass() + " doesn't has Annotation CustomRendererAttributes!");
    }

    public String name() {
        CustomRendererAttributes ann = renderer.getClass().getDeclaredAnnotation(CustomRendererAttributes.class);
        return ann.name();
    }

    public void setLoc(Vec3 vec3) {
        this.vec3 = vec3;
    }

    public Vec3 pos() {
        CustomRendererAttributes ann = renderer.getClass().getDeclaredAnnotation(CustomRendererAttributes.class);
        if (ann.canCommandUse())
            return vec3;
        return isInLevelType() ? getFromInLevel(renderer) : getFromParticle(getFromParticle(renderer));
    }
}
