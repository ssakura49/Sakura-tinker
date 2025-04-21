package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.entity.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;

public class STEntities {
    public static final EntityTypeDeferredRegister ENTITIES = new EntityTypeDeferredRegister(SakuraTinker.MODID);

    public static final RegistryObject<EntityType<GhostKnife>> GHOST_KNIFE = ENTITIES.register("ghost_knife", ()->
            EntityType.Builder.<GhostKnife>of(GhostKnife::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
//                    .setCustomClientFactory(((spawnEntity, level) -> new GhostKnife(level, 1)))
//                    .setCustomClientFactory(GhostKnife::new)
                    .setTrackingRange(8)
                    .setShouldReceiveVelocityUpdates(true)
                    .setUpdateInterval(4));
    public static final RegistryObject<EntityType<CelestialBladeProjectile>> CELESTIAL_BLADE = ENTITIES.register("celestial_blade", () ->
            EntityType.Builder.<CelestialBladeProjectile>of(CelestialBladeProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(32)
                    .setShouldReceiveVelocityUpdates(true)
                    .updateInterval(-1));
}
