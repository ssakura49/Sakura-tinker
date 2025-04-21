package com.ssakura49.sakuratinker.event;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STItems;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MobDrops {
    private static final Random random = new Random();
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        String entityName = EntityType.getKey(event.getEntity().getType()).toString();
        String configMobs = STConfig.COMMON.BLADE_CONVERGENCE_DROP_MOBS.get();
        List<String> allowedMobs = Arrays.asList(configMobs.split(","));
        if (allowedMobs.contains(entityName)) {
            if (random.nextDouble() <= STConfig.COMMON.BLADE_CONVERGENCE_DROP_CHANCE.get()) {
                ItemStack bladeConvergence = new ItemStack(STItems.blade_convergence.get());
                event.getEntity().spawnAtLocation(bladeConvergence);
            }
        }
    }
}
