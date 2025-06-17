package com.ssakura49.sakuratinker.utils.tinker;

import java.util.UUID;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.modules.behavior.AttributeModule;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class ItemUtil {
    public ItemUtil() {
    }

    public static boolean noCooldown(LivingEntity entity, Item item) {
        if (entity instanceof Player player) {
            return !player.getCooldowns().isOnCooldown(item);
        } else {
            return false;
        }
    }

    public static boolean noCooldown(LivingEntity entity, ItemStack stack) {
        return noCooldown(entity, stack.getItem());
    }

    public static boolean noCooldown(LivingEntity entity, ToolStack toolStack) {
        return noCooldown(entity, toolStack.getItem());
    }

    public static void addCooldown(LivingEntity entity, Item item, int time) {
        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(item, time);
        }

    }

    public static void addCooldown(LivingEntity entity, ItemStack stack, int time) {
        addCooldown(entity, stack.getItem(), time);
    }

    public static void addCooldown(LivingEntity entity, ToolStack toolStack, int time) {
        addCooldown(entity, toolStack.getItem(), time);
    }

    public static UUID getUUIDFromString(String name) {
        return UUID.nameUUIDFromBytes(name.getBytes());
    }

    public static UUID getUUID(EquipmentSlot slot) {
        return AttributeModule.getUUID("sakura.modifier", slot);
    }

    public static String getAttrName(String name, EquipmentSlot slot) {
        return name + "." + slot.getName();
    }

    public static AttributeModifier addAttr(EquipmentSlot slot, String name, double val) {
        return new AttributeModifier(getUUID(slot), name, val, Operation.ADDITION);
    }

    public static AttributeModifier mulBaseAttr(EquipmentSlot slot, String name, double val) {
        return new AttributeModifier(getUUID(slot), name, val, Operation.MULTIPLY_BASE);
    }
}
