package com.ssakura49.sakuratinker.event;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.register.STAttributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID)
public class RealitySuppressHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRealitySuppressionDamage(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        LivingEntity target = event.getEntity();
        AttributeInstance suppressionAttr = attacker.getAttribute(STAttributes.getRealitySuppression());
        AttributeInstance resistanceAttr = target.getAttribute(STAttributes.getRealitySuppressionResistance());
        if (suppressionAttr == null || resistanceAttr == null) return;
        double power = suppressionAttr.getValue();
        double resistance = resistanceAttr.getValue();
        double multiplier = Math.max(0.1, 1 + (power - resistance) * 0.25);
        event.setAmount(event.getAmount() * (float) multiplier);
    }
}
