package com.ssakura49.sakuratinker.utils;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;

public class RegisterAccessUtil {
    /**
     * 获取 MobEffect 的注册名
     */
    public static ResourceLocation getMobEffectId(RegistryAccess access, MobEffect effect) {
        if (access == null || effect == null) return null;
        Registry<MobEffect> registry = access.registryOrThrow(Registries.MOB_EFFECT);
        return registry.getKey(effect);
    }
}
