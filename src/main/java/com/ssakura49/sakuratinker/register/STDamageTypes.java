package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class STDamageTypes {
    public static final ResourceKey<DamageType> SOLAR_DAMAGE_TYPE = create("solar_damage_type");
    public static final ResourceKey<DamageType> OVERLOAD_DAMAGE_TYPE = create("overload_damage_type");
    public static final ResourceKey<DamageType> INFINITY_DAMAGE_TYPE = create("infinity_damage_type");

    private static ResourceKey<DamageType> create(String string) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation(SakuraTinker.MODID, string));
    }
}
