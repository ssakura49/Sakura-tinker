package com.ssakura49.sakuratinker.content.entity.terraprisma;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.server.ServerLifecycleHooks;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class STMemoryModules {
    private static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULES =
            DeferredRegister.create(Registries.MEMORY_MODULE_TYPE, SakuraTinker.MODID);

    //索引记忆
    public static final RegistryObject<MemoryModuleType<Integer>> PRISM_INDEX =
            register("prism_index", () -> new MemoryModuleType<>(Optional.of(Codec.INT)));

    //总数记忆
    public static final RegistryObject<MemoryModuleType<Integer>> TOTAL_PRISMS =
            register("total_prisms", () -> new MemoryModuleType<>(Optional.of(Codec.INT)));

    //主人记忆
    public static final RegistryObject<MemoryModuleType<LivingEntity>> OWNER =
            register("owner", () -> new MemoryModuleType<>(Optional.of(createLivingEntityCodec())));

    private static <T> RegistryObject<MemoryModuleType<T>> register(String name, Supplier<MemoryModuleType<T>> supplier) {
        return MEMORY_MODULES.register(name, supplier);
    }

    //实体引用编解码器
    private static Codec<LivingEntity> createLivingEntityCodec() {
        return RecordCodecBuilder.create(instance ->
                instance.group(
                        Level.RESOURCE_KEY_CODEC.fieldOf("dim").forGetter(e -> ((Entity)e).level().dimension()),
                        Codec.LONG.fieldOf("uuid_most").forGetter(e -> e.getUUID().getMostSignificantBits()),
                        Codec.LONG.fieldOf("uuid_least").forGetter(e -> e.getUUID().getLeastSignificantBits())
                ).apply(instance, (dim, most, least) -> {
                    ServerLevel level = ServerLifecycleHooks.getCurrentServer().getLevel(dim);
                    return level != null ?
                            (LivingEntity)level.getEntity(new UUID(most, least)) :
                            null;
                })
        );
    }

    public static void register(IEventBus eventBus) {
        MEMORY_MODULES.register(eventBus);
    }
}
