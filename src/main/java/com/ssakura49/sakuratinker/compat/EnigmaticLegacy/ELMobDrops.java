package com.ssakura49.sakuratinker.compat.EnigmaticLegacy;

import com.aizistral.enigmaticlegacy.registries.EnigmaticItems;
import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.utils.ModListUtil;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ELMobDrops {

    private static final Random random = new Random();
    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event) {
        if (!ModListUtil.EnigmaticLegacyLoaded) {
            return;
        }
        String entityName = EntityType.getKey(event.getEntity().getType()).toString();
        String voidPearlMobs = STConfig.COMMON.VOID_PEARL_DROP_MOBS.get();
        List<String> voidPearlAllowedMobs = Arrays.asList(voidPearlMobs.split(","));
        if (voidPearlAllowedMobs.contains(entityName)) {
            if (random.nextDouble() <= STConfig.COMMON.VOID_PEARL_DROP_CHANCE.get()) {
                if (ModListUtil.EnigmaticLegacyLoaded) {
                    ItemStack voidPearl = new ItemStack(EnigmaticItems.VOID_PEARL);
                    event.getEntity().spawnAtLocation(voidPearl);
                }
            }
        }
    }
}
