package com.ssakura49.sakuratinker.data.generator;

import com.ssakura49.sakuratinker.data.generator.providiers.*;
import com.ssakura49.sakuratinker.data.generator.providiers.curios.STCuriosProvider;
import com.ssakura49.sakuratinker.data.generator.providiers.tinker.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.fluids.data.FluidBucketModelProvider;
import slimeknights.tconstruct.library.client.data.material.MaterialPartTextureGenerator;
import slimeknights.tconstruct.tools.data.sprite.TinkerMaterialSpriteProvider;
import slimeknights.tconstruct.tools.data.sprite.TinkerPartSpriteProvider;

import java.util.concurrent.CompletableFuture;

import static com.ssakura49.sakuratinker.SakuraTinker.MODID;

@Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class STDataGenerator {
    @SubscribeEvent
    public static void gatherData(@NotNull GatherDataEvent event) {
        boolean server = event.includeServer();
        boolean client = event.includeClient();
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper helper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(client, new STFluidTextureProvider(output));
        generator.addProvider(client, new STBlockStateProvider(output, helper));
//        generator.addProvider(client, new STFluidTagProvider(output, lookupProvider, helper));
        generator.addProvider(client, new STItemModelProvider(output, helper));
//        generator.addProvider(client, new STFluidEffectProvider(output));
        generator.addProvider(server, new STMaterialRecipeProvider(output));
        generator.addProvider(client, new STMaterialProvider(output));
        generator.addProvider(client, new STMaterialStatProvider(output));
        generator.addProvider(client, new STMaterialModifierProvider(output));
        generator.addProvider(server, new STMaterialTagProvider(output,helper));
        generator.addProvider(server, new STModifierProvider(output));
        generator.addProvider(server, new STModifierRecipeProvider(output));
        generator.addProvider(event.includeClient(),new STMaterialRenderInfoProvider(output,new STMaterialSpriteProvider(),helper));
        generator.addProvider(client, new MaterialPartTextureGenerator(output,helper,new STPartSpriteProvider(),new TinkerMaterialSpriteProvider(),new STMaterialSpriteProvider()));
        generator.addProvider(client, new MaterialPartTextureGenerator(output,helper,new TinkerPartSpriteProvider(),new STMaterialSpriteProvider()));
        generator.addProvider(client, new FluidBucketModelProvider(output, MODID));
        STBlockTagProvider blockTags = new STBlockTagProvider(output, lookupProvider, helper);
        generator.addProvider(client,blockTags);
        generator.addProvider(server, new STItemTagProvider(output, lookupProvider, blockTags.contentsGetter(), helper));
        generator.addProvider(server, new STCuriosProvider(MODID, output,helper,lookupProvider));
    }

    public STDataGenerator(){}
}

