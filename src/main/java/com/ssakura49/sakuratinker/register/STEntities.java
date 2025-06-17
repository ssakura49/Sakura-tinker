package com.ssakura49.sakuratinker.register;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.entity.*;
import com.ssakura49.sakuratinker.content.entity.terraprisma.TerraPrismEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.registries.RegistryObject;
import slimeknights.mantle.registration.deferred.EntityTypeDeferredRegister;

import java.util.function.BiConsumer;

public class STEntities {
    public static final EntityTypeDeferredRegister ENTITIES = new EntityTypeDeferredRegister(SakuraTinker.MODID);

    public static final RegistryObject<EntityType<GhostKnife>> GHOST_KNIFE = ENTITIES.register("ghost_knife", ()->
            EntityType.Builder.<GhostKnife>of(GhostKnife::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .setCustomClientFactory(GhostKnife::new)
                    .setTrackingRange(8)
                    .setShouldReceiveVelocityUpdates(true)
                    .setUpdateInterval(4));
    public static final RegistryObject<EntityType<CelestialBladeProjectile>> CELESTIAL_BLADE = ENTITIES.register("celestial_blade", () ->
            EntityType.Builder.<CelestialBladeProjectile>of(CelestialBladeProjectile::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(32)
                    .setShouldReceiveVelocityUpdates(true)
                    .updateInterval(-1));
    public static final RegistryObject<EntityType<ShurikenEntity>> SHURIKEN_ENTITY = ENTITIES.register("shuriken", () ->
            EntityType.Builder.<ShurikenEntity>of(ShurikenEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .clientTrackingRange(32)
                    .setShouldReceiveVelocityUpdates(true)
                    .updateInterval(4));
    public static final RegistryObject<EntityType<TinkerArrowEntity>> TINKER_ARROW_ENTITY = ENTITIES.register("tinker_arrow_entity", () ->
            EntityType.Builder.<TinkerArrowEntity>of(TinkerArrowEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .setCustomClientFactory(TinkerArrowEntity::new)
                    .clientTrackingRange(32)
                    .setShouldReceiveVelocityUpdates(true)
                    .updateInterval(4));
    public static final RegistryObject<EntityType<MiniGrapplingHookEntity>> MINI_GRAPPLING_HOOK = ENTITIES.register("mini_grappling_hook", () ->
            EntityType.Builder.<MiniGrapplingHookEntity>of(MiniGrapplingHookEntity::new, MobCategory.MISC)
                    .sized(0.6f, 0.6f)
                    .setCustomClientFactory(MiniGrapplingHookEntity::new)
                    .clientTrackingRange(32)
                    .setShouldReceiveVelocityUpdates(true)
                    .updateInterval(-1));
    public static final RegistryObject<EntityType<LaserProjectileEntity>> LASER_PROJECTILE = ENTITIES.register("laser_projectile", () ->
            EntityType.Builder.<LaserProjectileEntity>of(LaserProjectileEntity::new, MobCategory.MISC)
                    .sized(0.2f, 0.2f)
                    .setCustomClientFactory(LaserProjectileEntity::new)
                    .clientTrackingRange(32)
                    .setShouldReceiveVelocityUpdates(true)
                    .updateInterval(-1));
    public static final RegistryObject<EntityType<PhantomSwordEntity>> PHANTOM_SWORD = ENTITIES.register("phantom_sword", () ->
            EntityType.Builder.<PhantomSwordEntity>of(PhantomSwordEntity::new, MobCategory.MISC)
                    .sized(0.2f, 0.2f)
                    .setCustomClientFactory(PhantomSwordEntity::new)
                    .clientTrackingRange(32)
                    .setShouldReceiveVelocityUpdates(true)
                    .updateInterval(-1));
    public static final RegistryObject<EntityType<ManaRayEntity>> MANA_RAY = ENTITIES.register("mana_ray", () ->
            EntityType.Builder.<ManaRayEntity>of(ManaRayEntity::new, MobCategory.MISC)
                    .sized(0.2f, 0.2f)
                    .setCustomClientFactory(ManaRayEntity::new)
                    .clientTrackingRange(32)
                    .setShouldReceiveVelocityUpdates(true)
                    .updateInterval(-1));
    public static final RegistryObject<EntityType<TerraPrismEntity>> TERRA_PRISMA = ENTITIES.register("terra_prisma", () ->
            EntityType.Builder.<TerraPrismEntity>of(TerraPrismEntity::new, MobCategory.CREATURE)
                    .sized(1.2f, 1.2f)
                    .setCustomClientFactory(TerraPrismEntity::new)
                    .clientTrackingRange(64)
                    .setShouldReceiveVelocityUpdates(true)
                    .updateInterval(-1));


    public static void registerAttributes(BiConsumer<EntityType<? extends LivingEntity>, AttributeSupplier.Builder> consumer) {
        consumer.accept(TERRA_PRISMA.get(), TerraPrismEntity.createTerraPrismaAttributes());

    }
}
