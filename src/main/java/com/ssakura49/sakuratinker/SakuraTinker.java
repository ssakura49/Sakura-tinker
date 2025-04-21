package com.ssakura49.sakuratinker;

import com.ssakura49.sakuratinker.client.ClientInit;
import com.ssakura49.sakuratinker.compat.IceAndFireCompat.IAFCompat;
import com.ssakura49.sakuratinker.content.tinkering.modifiers.curio.SoulBoundCurioModifier;
import com.ssakura49.sakuratinker.content.tools.capability.EnergyCapability;
import com.ssakura49.sakuratinker.content.tools.tiers.InfinityTiers;
import com.ssakura49.sakuratinker.compat.DraconicEvolution.DECompat;
import com.ssakura49.sakuratinker.compat.EnigmaticLegacy.ELCompat;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.ISSCompat;
import com.ssakura49.sakuratinker.compat.ReAvaritia.ReAvaritiaCompat;
import com.ssakura49.sakuratinker.compat.TwilightForest.TFCompat;
import com.ssakura49.sakuratinker.compat.YoukaiHomeComing.YKHCCompat;
import com.ssakura49.sakuratinker.library.logic.handler.PlayerClickHandler;
import com.ssakura49.sakuratinker.library.tools.STMaterialStats;
import com.ssakura49.sakuratinker.network.handler.PacketHandler;
import com.ssakura49.sakuratinker.register.*;
import com.ssakura49.sakuratinker.utils.ModListUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.List;

@Mod(SakuraTinker.MODID)
@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SakuraTinker {
    public static final String MODID = "sakuratinker";
    public static final Logger LOGGER = LogManager.getLogger("sakuratinker");
    public static ResourceLocation location(String string) {
        return new ResourceLocation(MODID, string);
    }

    public SakuraTinker() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus modEventBus = context.getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::getCurioSlots);
        forgeEventBus.addListener(PlayerClickHandler::onLeftClick);
        forgeEventBus.addListener(PlayerClickHandler::onLeftClickBlock);
        STConfig.init();
        STItems.ITEMS.register(modEventBus);
        STItems.TINKER_ITEMS.register(modEventBus);
        STGroups.CREATIVE_MODE_TABS.register(modEventBus);
        STBlocks.BLOCKS.register(modEventBus);
        STFluids.FLUIDS.register(modEventBus);
        STEffects.EFFECT.register(modEventBus);
        STModifiers.MODIFIERS.register(modEventBus);
        STEntities.ENTITIES.register(modEventBus);
        STSounds.SOUNDS.register(modEventBus);
        STRecipes.init(modEventBus);
        SoulBoundCurioModifier.addCuriosDropListener();
        STSlots.init();

        PacketHandler.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientInit::init);

        if (ModListUtil.YHKCLoaded) {
            YKHCCompat.YKHC_MODIFIERS.register(modEventBus);
            STItems.YKHC_ITEMS.register(modEventBus);
            STFluids.YKHC_FLUIDS.register(modEventBus);
            LOGGER.info("Found YouKai Home Coming, integration initializing……");
        }
        if (ModListUtil.EnigmaticLegacyLoaded) {
            ELCompat.EL_MODIFIERS.register(modEventBus);
            STItems.EL_ITEMS.register(modEventBus);
            STFluids.EL_FLUIDS.register(modEventBus);
            LOGGER.info("Found Enigmatic Legacy, integration initializing……");
        }
        if (ModListUtil.ISSLoaded) {
            ISSCompat.ISS_MODIFIERS.register(modEventBus);
            STItems.ISS_ITEMS.register(modEventBus);
            STFluids.ISS_FLUIDS.register(modEventBus);
            LOGGER.info("Found Iron's Spellbooks, integration initializing……");
        }
        if (ModListUtil.AvaritiaLoaded) {
            ReAvaritiaCompat.REA_MODIFIERS.register(modEventBus);
            STItems.REA_ITEMS.register(modEventBus);
            STFluids.REA_FLUIDS.register(modEventBus);
            LOGGER.info("Found Re:Avaritia, integration initializing……");
        }
        if (ModListUtil.TFLoaded) {
           TFCompat.TF_MODIFIERS.register(modEventBus);
           STItems.TF_ITEMS.register(modEventBus);
           STFluids.TF_FLUIDS.register(modEventBus);
           LOGGER.info("Found Twilight Forest, integration initializing……");
        }
        if (ModListUtil.DraconicEvolution) {
            DECompat.DE_MODIFIERS.register(modEventBus);
            STItems.DE_ITEMS.register(modEventBus);
            STFluids.DE_FLUIDS.register(modEventBus);
            LOGGER.info("Found Draconic Evolution, integration initializing……");
        }
        if (ModListUtil.IceAndFire) {
            IAFCompat.IAF_MODIFIERS.register(modEventBus);
            STItems.IAF_ITEMS.register(modEventBus);
            STFluids.IAF_FLUIDS.register(modEventBus);
            LOGGER.info("Found Ice And Fire, integration initializing……");
        }
    }
    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(STMaterialStats::setup);
        ToolCapabilityProvider.register(EnergyCapability::new);
        TierSortingRegistry.registerTier(InfinityTiers.instance, new ResourceLocation("sakuratinker:infinity"), List.of(Tiers.NETHERITE), List.of());
    }

    public void getCurioSlots(InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.CHARM.getMessageBuilder().build());
    }

//    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
//    public static class ClientModEvents
//    {
//        @SubscribeEvent
//        public static void registerEntityRenderer(EntityRenderersEvent.RegisterRenderers event){
//            event.registerEntityRenderer(STEntities.GHOST_KNIFE.get(), GhostKnifeRenderer::new);
//            event.registerEntityRenderer(STEntities.CELESTIAL_BLADE.get(), CelestialBladeRenderer::new);
//        }
//    }
}