package com.ssakura49.sakuratinker.event.event.forge;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STAttributes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModAttributeHandler {
    public static final DeferredRegister<Attribute> ATTRIBUTES = STAttributes.ATTRIBUTES;

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        ForgeRegistries.ENTITY_TYPES.getValues().stream()
                .filter(EntityType::canSerialize) // 避免非生物或无效实体
                .filter(type -> LivingEntity.class.isAssignableFrom(type.getBaseClass()))
                .forEach(entityType -> {
                    @SuppressWarnings("unchecked")
                    EntityType<? extends LivingEntity> casted = (EntityType<? extends LivingEntity>) entityType;
                    ATTRIBUTES.getEntries().forEach(attributeRegistryObject -> {
                        event.add(casted, attributeRegistryObject.get());
                    });
                });
    }
}