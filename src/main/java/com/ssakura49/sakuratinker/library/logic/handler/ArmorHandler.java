package com.ssakura49.sakuratinker.library.logic.handler;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.library.hooks.armor.WearerDamagePreHook;
import com.ssakura49.sakuratinker.library.hooks.armor.WearerDamageTakeHook;
import com.ssakura49.sakuratinker.library.hooks.armor.WearerKnockBackHook;
import com.ssakura49.sakuratinker.library.hooks.armor.WearerTakeHealHook;
import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import com.ssakura49.sakuratinker.library.tools.STHooks;
import com.ssakura49.sakuratinker.utils.ToolUtils;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingKnockBackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.definition.ModifiableArmorMaterial;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ArmorHandler {
    public ArmorHandler(){}

    public static void onHurtEntity(LivingHurtEvent event, LivingEntity entity) {
        Entity e = event.getSource().getEntity();
        if (e instanceof LivingEntity attacker) {
            EquipmentContext context = new EquipmentContext(attacker);
            if (context.hasModifiableArmor()) {
                for(EquipmentSlot slot : ModifiableArmorMaterial.ARMOR_SLOTS) {
                    IToolStackView armor = context.getToolInSlot(slot);
                    if (ToolUtils.isNotBrokenOrNull(armor)) {
                        armor.getModifierList().forEach((entry) -> {
                            AttackData data = new AttackData(event.getSource(), entity, context, slot);
                            ((WearerDamagePreHook)entry.getHook(STHooks.WEARER_DAMAGE_PRE)).onDamagePre(armor, event, data, entry.getLevel());
                        });
                    }
                }

            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        LivingEntity entity = event.getEntity();
        onHurtEntity(event, entity);
        EquipmentContext context = new EquipmentContext(entity);
        if (context.hasModifiableArmor()) {
            for(EquipmentSlot slot : ModifiableArmorMaterial.ARMOR_SLOTS) {
                IToolStackView armor = context.getToolInSlot(slot);
                if (ToolUtils.isNotBrokenOrNull(armor)) {
                    armor.getModifierList().forEach((e) -> {
                        AttackData data = new AttackData(event.getSource(), entity, context, slot);
                        ((WearerDamageTakeHook)e.getHook(STHooks.WEARER_DAMAGE_TAKE)).onTakeDamagePre(armor, event, data, e.getLevel());
                    });
                }
            }

        }
    }

    @SubscribeEvent
    public static void onLivingDamage(LivingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        EquipmentContext context = new EquipmentContext(entity);
        if (context.hasModifiableArmor()) {
            for(EquipmentSlot slot : ModifiableArmorMaterial.ARMOR_SLOTS) {
                IToolStackView armor = context.getToolInSlot(slot);
                if (ToolUtils.isNotBrokenOrNull(armor)) {
                    armor.getModifierList().forEach((e) -> {
                        AttackData data = new AttackData(event.getSource(), entity, context, slot);
                        ((WearerDamageTakeHook)e.getHook(STHooks.WEARER_DAMAGE_TAKE)).onTakeDamagePost(armor, event, data, e.getLevel());
                    });
                }
            }

        }
    }

    @SubscribeEvent
    public static void onLivingHeal(LivingHealEvent event) {
        LivingEntity entity = event.getEntity();
        EquipmentContext context = new EquipmentContext(entity);
        if (context.hasModifiableArmor()) {
            for(EquipmentSlot slotType : ModifiableArmorMaterial.ARMOR_SLOTS) {
                IToolStackView armor = context.getToolInSlot(slotType);
                if (ToolUtils.isNotBrokenOrNull(armor)) {
                    armor.getModifierList().forEach((e) -> ((WearerTakeHealHook)e.getHook(STHooks.WEARER_TAKE_HEAL)).onTakeHeal(armor, event, context, e.getLevel()));
                }
            }

        }
    }

    @SubscribeEvent
    public static void onKnockBack(LivingKnockBackEvent event) {
        LivingEntity entity = event.getEntity();
        EquipmentContext context = new EquipmentContext(entity);
        if (context.hasModifiableArmor()) {
            for(EquipmentSlot slotType : ModifiableArmorMaterial.ARMOR_SLOTS) {
                IToolStackView armor = context.getToolInSlot(slotType);
                if (ToolUtils.isNotBrokenOrNull(armor)) {
                    armor.getModifierList().forEach((e) -> ((WearerKnockBackHook)e.getHook(STHooks.WEARER_KNOCK_BACK)).onKnockBack(armor, event, context, e.getLevel()));
                }
            }

        }
    }
}
