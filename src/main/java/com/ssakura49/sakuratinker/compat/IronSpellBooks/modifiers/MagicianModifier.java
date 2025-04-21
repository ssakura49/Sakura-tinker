package com.ssakura49.sakuratinker.compat.IronSpellBooks.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.UUID;

public class MagicianModifier extends BaseModifier {
    private static final UUID CAST_TIME_UUID = UUID.fromString("cad2c9a0-98e5-4131-9fd1-c5bed139bf8d");
    private static final UUID COOLDOWN_REDUCTION = UUID.fromString("19bfe93c-9f39-447f-a12e-9b3f810008db");
    //private static final UUID SPELL_POWER = UUID.fromString("b0512917-2d92-42f1-952d-28c548468af7");

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        Player player = context.getEntity() instanceof Player ? (Player) context.getEntity() : null;
        if (player != null) {
            int level = modifier.getLevel();
            double multiplierCastTimeReduction = 0.1 * level;
            double multiplierCooldownReduction = 0.1 * level;
            addAttributeModifier(player, AttributeRegistry.COOLDOWN_REDUCTION.get(), COOLDOWN_REDUCTION,"ISS CD", multiplierCooldownReduction, AttributeModifier.Operation.MULTIPLY_TOTAL);
            addAttributeModifier(player, AttributeRegistry.CAST_TIME_REDUCTION.get(), CAST_TIME_UUID, "ISS CTR", multiplierCastTimeReduction, AttributeModifier.Operation.MULTIPLY_TOTAL);
            //addAttributeModifier(player, AttributeRegistry.CAST_TIME_REDUCTION.get(), SPELL_POWER, "ISS SP", multiplierSpellPower, AttributeModifier.Operation.MULTIPLY_TOTAL);
        }
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        Player player = context.getEntity() instanceof Player ? (Player) context.getEntity() : null;
        if (player != null) {
            removeAttributeModifier(player, AttributeRegistry.CAST_TIME_REDUCTION.get(), CAST_TIME_UUID);
            removeAttributeModifier(player, AttributeRegistry.COOLDOWN_REDUCTION.get(), COOLDOWN_REDUCTION);
            //removeAttributeModifier(player, AttributeRegistry.SPELL_POWER.get(), SPELL_POWER);
        }
    }

    private void addAttributeModifier(Player player, Attribute attribute, UUID uuid, String name, double value, AttributeModifier.Operation operation) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null && !instance.hasModifier(new AttributeModifier(uuid, name, value, operation))) {
            instance.addTransientModifier(new AttributeModifier(uuid, name, value, operation));
        }
    }
    private void removeAttributeModifier(Player player, Attribute attribute, UUID uuid) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(uuid);
        }
    }
}
