package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class ArmorTwilightRemembranceModifier extends BaseModifier {
    public ArmorTwilightRemembranceModifier() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    @Override
    public boolean isNoLevels() {
        return true;
    }
    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        if (entity instanceof Player) {
            entity.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, Integer.MAX_VALUE, 0, false, false));
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, Integer.MAX_VALUE, 0, false, false));
        }
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        if (entity instanceof Player) {
            entity.removeEffect(MobEffects.NIGHT_VISION);
            entity.removeEffect(MobEffects.FIRE_RESISTANCE);
        }
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity instanceof Player player) {
            for (ItemStack stack : player.getArmorSlots()) {
                IToolStackView tool = ToolStack.from(stack);
                if (tool.getModifierLevel(this) > 0) {
                    if (player.getRandom().nextFloat() < 0.1f) {
                        event.setAmount(2.0f);
                    }
                }
            }
        }
    }
}
