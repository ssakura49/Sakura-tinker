package com.ssakura49.sakuratinker.client;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.renderer.*;
import com.ssakura49.sakuratinker.register.STEntities;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEntityRendererInit {

    @SubscribeEvent
    public static void onRegisterRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(STEntities.GHOST_KNIFE.get(), GhostKnifeRenderer::new);
        event.registerEntityRenderer(STEntities.CELESTIAL_BLADE.get(), CelestialBladeRenderer::new);
        event.registerEntityRenderer(STEntities.SHURIKEN_ENTITY.get(), ShurikenRenderer::new);
        event.registerEntityRenderer(STEntities.TINKER_ARROW_ENTITY.get(), TinkerArrowEntityRenderer::new);
        event.registerEntityRenderer(STEntities.MINI_GRAPPLING_HOOK.get(), MiniGrapplingHookRenderer::new);
        event.registerEntityRenderer(STEntities.LASER_PROJECTILE.get(), LaserRenderer::new);
        event.registerEntityRenderer(STEntities.PHANTOM_SWORD.get(), PhantomSwordRenderer::new);
        event.registerEntityRenderer(STEntities.MANA_RAY.get(), ManaRayRenderer::new);
        event.registerEntityRenderer(STEntities.TERRA_PRISMA.get(), TerraPrismaRenderer::new);
    }

    public ClientEntityRendererInit(){
    }
    public static void init() {
    }
}
