package com.ssakura49.sakuratinker.compat.GoetyRevelation.event;

import com.ssakura49.sakuratinker.compat.GoetyRevelation.modifiers.ApocalyptiumModifier;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.IEventBus;

public class GRModifierEventHandler {
    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(GRModifierEventHandler::legs);
        MinecraftForge.EVENT_BUS.addListener(GRModifierEventHandler::feet);
        MinecraftForge.EVENT_BUS.addListener(GRModifierEventHandler::source);
        MinecraftForge.EVENT_BUS.addListener(GRModifierEventHandler::onLivingDeath);
    }

    public static void legs(LivingHurtEvent event) {
       ApocalyptiumModifier.onLivingHurtLegs(event);
    }

    public static void feet(LivingHurtEvent event) {
        ApocalyptiumModifier.onLivingHurtFeet(event);
    }

    public static void source(LivingHurtEvent event) {
        ApocalyptiumModifier.damageSourceBlock(event);
    }

    public static void onLivingDeath(LivingDeathEvent event) {
        ApocalyptiumModifier.onLivingDeath(event);
    }
}
