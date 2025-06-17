package com.ssakura49.sakuratinker;

import com.ssakura49.sakuratinker.client.ClientEntityRendererInit;
import com.ssakura49.sakuratinker.client.ClientGuiRendererInit;
import com.ssakura49.sakuratinker.compat.Botania.BotaniaCompat;
import com.ssakura49.sakuratinker.compat.DreadSteel.DreadSteelCompat;
import com.ssakura49.sakuratinker.compat.ExtraBotany.ExtraBotanyCompat;
import com.ssakura49.sakuratinker.compat.Goety.GoetyCompat;
import com.ssakura49.sakuratinker.compat.IceAndFireCompat.IAFCompat;
import com.ssakura49.sakuratinker.content.entity.terraprisma.STMemoryModules;
import com.ssakura49.sakuratinker.content.tinkering.modifiers.curio.SoulBoundCurioModifier;
import com.ssakura49.sakuratinker.content.tinkering.modules.EnvironmentalAdaptationModule;
import com.ssakura49.sakuratinker.content.tinkering.modules.MultiCurioAttributeModule;
import com.ssakura49.sakuratinker.content.tools.capability.ForgeEnergyCapability;
import com.ssakura49.sakuratinker.content.tools.tiers.DreadSteelTiers;
import com.ssakura49.sakuratinker.content.tools.tiers.InfinityTiers;
import com.ssakura49.sakuratinker.compat.DraconicEvolution.DECompat;
import com.ssakura49.sakuratinker.compat.EnigmaticLegacy.ELCompat;
import com.ssakura49.sakuratinker.compat.IronSpellBooks.ISSCompat;
import com.ssakura49.sakuratinker.compat.ReAvaritia.ReAvaritiaCompat;
import com.ssakura49.sakuratinker.compat.TwilightForest.TFCompat;
import com.ssakura49.sakuratinker.compat.YoukaiHomeComing.YKHCCompat;
import com.ssakura49.sakuratinker.library.logic.handler.PlayerClickHandler;
import com.ssakura49.sakuratinker.library.logic.handler.SpellBookHandler;
import com.ssakura49.sakuratinker.library.tinkering.tools.STMaterialStats;
import com.ssakura49.sakuratinker.network.handler.PacketHandler;
import com.ssakura49.sakuratinker.register.*;
import com.ssakura49.sakuratinker.utils.ModListUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.tools.capability.ToolCapabilityProvider;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Mod(SakuraTinker.MODID)
@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SakuraTinker {
    public static final String MODID = "sakuratinker";
    public static final Logger LOGGER = LogManager.getLogger("sakuratinker");
    public static StringBuilder stringBuilder = new StringBuilder();
    public static ResourceLocation location(String string) {
        return new ResourceLocation(MODID, string);
    }

    public SakuraTinker() {
        FMLJavaModLoadingContext context = FMLJavaModLoadingContext.get();
        IEventBus modEventBus = context.getModEventBus();
        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        MinecraftForge.EVENT_BUS.register(this);
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onRegisterTiers);
        modEventBus.addListener(this::registerSerializers);
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
        STParticles.PARTICLES.register(modEventBus);
        STRecipes.init(modEventBus);
        SoulBoundCurioModifier.addCuriosDropListener();
        STSlots.init();
        modEventBus.addListener((EntityAttributeCreationEvent e) -> STEntities.registerAttributes((type, builder) -> e.put(type, builder.build())));
        STMemoryModules.register(modEventBus);

        PacketHandler.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientEntityRendererInit::init);
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientGuiRendererInit::init);



        if (ModListUtil.YHKCLoaded) {
            YKHCCompat.YKHC_MODIFIERS.register(modEventBus);
            LOGGER.info("Found YouKai Home Coming, integration initializing……");
        }
        if (ModListUtil.EnigmaticLegacyLoaded) {
            ELCompat.EL_MODIFIERS.register(modEventBus);
            LOGGER.info("Found Enigmatic Legacy, integration initializing……");
        }
        if (ModListUtil.ISSLoaded) {
            ISSCompat.ISS_MODIFIERS.register(modEventBus);
            ISSCompat.TINKER_ISS_ITEMS.register(modEventBus);
            forgeEventBus.addListener(SpellBookHandler::onSpellDamage);
            forgeEventBus.addListener(SpellBookHandler::onCastSpell);
            LOGGER.info("Found Iron's Spellbooks, integration initializing……");
        }
        if (ModListUtil.AvaritiaLoaded) {
            ReAvaritiaCompat.REA_MODIFIERS.register(modEventBus);
            LOGGER.info("Found Re:Avaritia, integration initializing……");
        }
        if (ModListUtil.TFLoaded) {
           TFCompat.TF_MODIFIERS.register(modEventBus);
           LOGGER.info("Found Twilight Forest, integration initializing……");
        }
        if (ModListUtil.DraconicEvolution) {
            DECompat.DE_MODIFIERS.register(modEventBus);
            LOGGER.info("Found Draconic Evolution, integration initializing……");
        }
        if (ModListUtil.IceAndFire) {
            IAFCompat.IAF_MODIFIERS.register(modEventBus);
            LOGGER.info("Found Ice And Fire, integration initializing……");
        }
        if (ModListUtil.Botania) {
            BotaniaCompat.BOTANIA_MODIFIERS.register(modEventBus);
            LOGGER.info("Found Botania, integration initializing……");
        }
        if (ModListUtil.ExtraBotany) {
            ExtraBotanyCompat.EXBOT_MODIFIERS.register(modEventBus);
            ExtraBotanyCompat.TINKER_ITEMS.register(modEventBus);
            LOGGER.info("Found Extra Botany, integration initializing……");
        }
        if (ModListUtil.DreadSteel) {
            DreadSteelCompat.DE_MODIFIERS.register(modEventBus);
            LOGGER.info("Found Dread Steel, integration initializing……");
        }
        if (ModListUtil.Goety) {
            GoetyCompat.MODIFIERS.register(modEventBus);
//            GoetyCompat.TINKER_ITEMS.register(modEventBus);
//            forgeEventBus.addListener(SpellAttackHandler::onLivingHurt);
            LOGGER.info("Found Goety, integration initializing……");
        }
    }

    @SubscribeEvent
    public void onRegisterTiers(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.ITEM)) {
            TierSortingRegistry.registerTier(
                    InfinityTiers.instance,
                    new ResourceLocation(MODID, "infinity"),
                    List.of(Tiers.NETHERITE),
                    List.of()
            );
            TierSortingRegistry.registerTier(
                    DreadSteelTiers.instance,
                    new ResourceLocation(MODID, "dread_steel"),
                    List.of(Tiers.NETHERITE),
                    List.of(InfinityTiers.instance)
            );
        }
    }
    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(STMaterialStats::init);
        ToolCapabilityProvider.register((stack, tool) -> new ForgeEnergyCapability.Provider(tool));
//        if(ModListUtil.Ember) {
//            ToolCapabilityProvider.register((stack, tool) -> new EmberEnergyCapability.Provider(tool));
//        }
    }
    @SubscribeEvent
    public void registerSerializers(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            ModifierModule.LOADER.register(location("environmental_adaptation"), EnvironmentalAdaptationModule.LOADER);
            ModifierModule.LOADER.register(location("multi_curio_attribute"), MultiCurioAttributeModule.LOADER);
        }
    }

    public static String makeDescriptionId(String type, String name) {
        return type + ".sakuratinker." + name;
    }
    public static synchronized void outArray(Object[] o) {
        System.out.println(stringBuilder.toString() + "[SakuraTinker: " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "]" + Arrays.toString(o));
    }
    public static synchronized void out(Object o) {
        System.out.println(stringBuilder.toString() + "[SakuraTinker: " + LocalDateTime.now().getHour() + ":" + LocalDateTime.now().getMinute() + "]" + o);
    }
}