package com.ssakura49.sakuratinker.event.event.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.auto.AutoRegisterManager;
import com.ssakura49.sakuratinker.client.baked.loader.*;
import com.ssakura49.sakuratinker.client.menu.RevolverScreen;
import com.ssakura49.sakuratinker.client.renderer.FoxCurioModelRenderer;
import com.ssakura49.sakuratinker.common.items.SpecialItemCheck;
import com.ssakura49.sakuratinker.event.ClientProgramTickEvent;
import com.ssakura49.sakuratinker.event.custom.STRenderTooltipEvent;
import com.ssakura49.sakuratinker.event.client.ClientHooks;
import com.ssakura49.sakuratinker.register.STItems;
import com.ssakura49.sakuratinker.register.STMenus;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import com.ssakura49.sakuratinker.render.shader.core.ModShaders;
import com.ssakura49.sakuratinker.render.shader.cosmic.CosmicItemShaders;
import com.ssakura49.sakuratinker.render.shader.cosmic.VanillaCosmicShaders;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;
import org.joml.Vector3f;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import top.theillusivec4.curios.api.client.ICurioRenderer;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientModHandler {
    public static final float faded_dragon_layer_alpha = 0.58F;
    public static float rotationStar = 0F;
    public static boolean inventoryRender = false;
//    public static ResourceLocation BAR = new ResourceLocation(SakuraTinker.MODID, "textures/entity/bossbar/base.png");
    public static Minecraft mc = Minecraft.getInstance();
    static Vector3f randColor = new Vector3f();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void drawScreenPre(ScreenEvent.Render.Pre e) {
        Screen screen = e.getScreen();
        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
            inventoryRender = true;
        }
    }
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void drawScreenPost(ScreenEvent.Render.Post e) {
        Screen screen = e.getScreen();
        if (screen instanceof InventoryScreen || screen instanceof CreativeModeInventoryScreen) {
            inventoryRender = false;
        }
    }

    @SubscribeEvent
    public static void modifyItemTooltip(STRenderTooltipEvent.PrePre event) {
        if (SpecialItemCheck.isSTItem(event.getItemStack())) {
            ClientHooks.tooltipSTItem(event);
            ClientHooks.isSTItemTooltipNow = true;
            return;
        }
        ClientHooks.isSTItemTooltipNow = false;
    }
    @SubscribeEvent
    public static void modifyItemTooltipDpSp(STRenderTooltipEvent.Post event) {
        if (SpecialItemCheck.isOutlineItem(event.getItemStack()) || SpecialItemCheck.isSpItem(event.getItemStack()))
            ClientHooks.tooltipOlSpItem(event);
    }

    @SubscribeEvent
    public static void cancelItemTooltipBackground(RenderTooltipEvent.Color event) {
        if (SpecialItemCheck.isSTItem(event.getItemStack()) || SpecialItemCheck.isOutlineItem(event.getItemStack()) || SpecialItemCheck.isSpItem(event.getItemStack())) {
            event.setBorderStart(0);
            event.setBorderEnd(0);
            event.setBackgroundStart(0);
            event.setBackgroundEnd(0);
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class TickEventsHandler {

        public static float PARTIAL_TICK = 0;

        @SubscribeEvent
        public static void renderTick(TickEvent.RenderTickEvent event){
            if (event.phase == TickEvent.Phase.START){
                PARTIAL_TICK = event.renderTickTime;
            }
        }

        @SubscribeEvent
        public static void clientTick(TickEvent.ClientTickEvent event) {
        }

        @SubscribeEvent
        public static void clientTick(ClientProgramTickEvent event) {
            if (event.phase == TickEvent.Phase.START) {
                if (mc.level != null) {
                    ClientHooks.lastTick = ClientHooks.tick;
                    if (ClientHooks.isSTItemTooltipNow) {
                        if (ClientHooks.tick < 20) {
                            ClientHooks.tick++;
                        }
                    } else ClientHooks.tick = 0;
                }
            }
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class FMLSetupClient {
        @SubscribeEvent
        public static void clientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                for (RegistryObject<Item> registryObject : AutoRegisterManager.ITEM_ARH().c2roMap.values()) {
                    if (registryObject.get() instanceof BowItem bow) {
                        ItemProperties.register(bow, ResourceLocation.parse("pulling"), (itemStack, clientWorld, livingEntity, i)
                                -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);
                        ItemProperties.register(bow, ResourceLocation.parse("pull"), (itemStack, clientWorld, livingEntity, i) -> {
                            if (livingEntity == null) {
                                return 0.0F;
                            } else {
                                return livingEntity.getUseItem() != itemStack ? 0.0F : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
                            }
                        });
                    }
                }
            });

            CuriosRendererRegistry.register(STItems.fox_mask.get(), () -> new ICurioRenderer() {
                @Override
                public <T extends LivingEntity, M extends EntityModel<T>> void render(ItemStack stack, SlotContext slotContext, PoseStack poseStack, RenderLayerParent<T, M> renderLayerParent, MultiBufferSource buffer, int light, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
                    LivingEntity entity = slotContext.entity();
                    if (entity.isInvisible()) return;
                    FoxCurioModelRenderer.INSTANCE.render(stack, slotContext, poseStack, renderLayerParent, buffer,light,limbSwing,limbSwingAmount,partialTicks,ageInTicks,netHeadYaw,headPitch);
                }
            });

            STRenderType.reloadParticleRenderTypes();

//            if (SafeClassUtil.GoetyLoaded) {
//                TinkerItemProperties.registerToolProperties(GoetyItems.tinker_wand.get());
//            }

            MenuScreens.register(STMenus.REVOLVER.get(), RevolverScreen::new);
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class PostEffectEvents {
        public static long time = 0L;


        @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
        public static class ClientModEvents {
            @SubscribeEvent
            public static void registerLoaders(ModelEvent.RegisterGeometryLoaders event) {
                event.register("cosmic_fantasy_loader", FantasyModelLoader.INSTANCE);
                event.register("cosmic_loader", CosmicModelLoader.INSTANCE);
                event.register("cosmic_st_loader", SeparateTransformsCosmicModelLoader.INSTANCE);
                event.register("halo", HaloModelLoader.INSTANCE);
                event.register("cosmic_tool_loader",CosmicToolModelLoader.INSTANCE);
            }

            @SubscribeEvent(priority = EventPriority.HIGHEST)
            public static void onRegisterShaders(RegisterShadersEvent event) {
                CosmicItemShaders.onRegisterShaders(event);
                VanillaCosmicShaders.onRegisterShaders(event);
                ModShaders.onRegisterShaders(event);
            }
        }
    }
}
