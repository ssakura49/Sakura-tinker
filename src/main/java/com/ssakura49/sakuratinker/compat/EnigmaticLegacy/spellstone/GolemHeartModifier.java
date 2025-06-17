package com.ssakura49.sakuratinker.compat.EnigmaticLegacy.spellstone;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.library.logic.context.AttackData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.context.EquipmentContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;

public class GolemHeartModifier extends BaseModifier {
    private static final UUID BASE_ARMOR_UUID = UUID.fromString("b55bc4d8-a4c5-410b-9ba7-8e71acfc01fa");
    private static final UUID BONUS_ARMOR_UUID = UUID.fromString("a7fcdba5-52a1-4cd0-941d-ed46ba4139af");
    private static final UUID ARMOR_TOUGHNESS_UUID = UUID.fromString("915f4a26-1e97-425b-a43a-27692a9db22d");
    private static final UUID KNOCKBACK_RESISTANCE_UUID = UUID.fromString("2f1c9311-56f0-49c2-b4da-1c429d6e21c1");

    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public int getPriority() {
        return 25;
    }

    @Override
    public void addAttributes(IToolStackView tool, ModifierEntry modifier, EquipmentSlot slot, BiConsumer<Attribute, AttributeModifier> consumer) {
        consumer.accept(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(
                KNOCKBACK_RESISTANCE_UUID,
                "knockback_resist",
                1.0,
                AttributeModifier.Operation.ADDITION
        ));
    }

    @Override
    public float onModifyTakeDamage(IToolStackView tool, ModifierEntry modifier, EquipmentContext context, EquipmentSlot slotType, DamageSource source, float amount, boolean isDirectDamage) {
        LivingEntity entity = context.getEntity();
        double totalArmor = entity.getArmorValue();
        boolean hasLowArmor = totalArmor < 10;
        boolean wearingMultipleArmor = countWornArmor(entity) > 1;
        if (source.is(DamageTypes.MAGIC)) {
            return wearingMultipleArmor ? amount * 3.0f : amount * 1.25f;
        }
        else if (source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.PLAYER_ATTACK)) {
            return amount * 0.75f;
        }
        else if (source.is(DamageTypes.EXPLOSION)) {
            return hasLowArmor ? amount * 0.6f : amount;
        }
        else if (source.is(DamageTypes.FALLING_ANVIL) || source.is(DamageTypes.FALLING_BLOCK) || source.is(DamageTypes.CACTUS)) {
            return 0;
        }

        return amount;
    }

    private int countWornArmor(LivingEntity entity) {
        return (int) Arrays.stream(EquipmentSlot.values())
                .filter(slot -> slot.getType() == EquipmentSlot.Type.ARMOR)
                .filter(slot -> !entity.getItemBySlot(slot).isEmpty())
                .count();
    }

    @Override
    public void modifierOnEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        double totalArmor = entity.getArmorValue();
        boolean hasLowArmor = totalArmor < 10;
        if (hasLowArmor && !otherArmor(entity, context.getChangedSlot())) {
            Objects.requireNonNull(entity.getAttribute(Attributes.ARMOR)).addPermanentModifier(new AttributeModifier(
                BASE_ARMOR_UUID,
                "armor_toughness",
                15.0,
                AttributeModifier.Operation.ADDITION
        ));
            Objects.requireNonNull(entity.getAttribute(Attributes.ARMOR_TOUGHNESS)).addPermanentModifier(new AttributeModifier(
                    ARMOR_TOUGHNESS_UUID,
                    "armor_toughness",
                    5.0,
                    AttributeModifier.Operation.ADDITION
            ));
        }
    }

    private boolean otherArmor(LivingEntity entity, EquipmentSlot changedSlot) {
        return Arrays.stream(EquipmentSlot.values())
                .filter(slot -> slot.getType() == EquipmentSlot.Type.ARMOR)
                .anyMatch(slot -> (!slot.equals(changedSlot)) &&
                        !entity.getItemBySlot(slot).isEmpty());
    }

    @Override
    public void modifierOnUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        LivingEntity entity = context.getEntity();
        Objects.requireNonNull(entity.getAttribute(Attributes.ARMOR)).removeModifier(BONUS_ARMOR_UUID);
        Objects.requireNonNull(entity.getAttribute(Attributes.ARMOR_TOUGHNESS)).removeModifier(ARMOR_TOUGHNESS_UUID);
    }
}

