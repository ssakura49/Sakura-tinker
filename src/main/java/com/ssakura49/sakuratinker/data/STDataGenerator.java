package com.ssakura49.sakuratinker.data;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.data.providiers.STBlockStateProvider;
import com.ssakura49.sakuratinker.data.providiers.STFluidTagProvider;
import com.ssakura49.sakuratinker.data.providiers.STItemModelProvider;
import com.ssakura49.sakuratinker.data.providiers.tcon.STFluidEffectProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.fluids.data.FluidBucketModelProvider;

import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class STDataGenerator {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new STBlockStateProvider(output, helper));
        generator.addProvider(event.includeClient(), new STFluidTagProvider(output, lookupProvider, helper));
        generator.addProvider(event.includeClient(), new STItemModelProvider(output, helper));
        generator.addProvider(event.includeClient(), new STFluidEffectProvider(output));
        generator.addProvider(event.includeClient(), new FluidBucketModelProvider(output, SakuraTinker.MODID));
    }
}
