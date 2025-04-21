package com.ssakura49.sakuratinker.compat.IronSpellBooks.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.UUID;

public class FountainMagicModifier extends BaseModifier {

    private static final UUID MAX_MANA_UUID = UUID.fromString("16fd3709-40ba-4456-8da6-9f1eedd48af4");
    private static final UUID MANA_REGEN_UUID = UUID.fromString("e9d33d91-03d0-42b6-9d3c-37edb07095a1");

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        Player player = context.getEntity() instanceof Player ? (Player) context.getEntity() : null;
        if (player != null) {
            int level = getTotalModifierLevel(player);
            double addMana = 800 * level;
            double multiplierRegen = 0.1 * level;
            addAttributeModifier(player, AttributeRegistry.MAX_MANA.get(), MAX_MANA_UUID, "ISS Max Mana", addMana, AttributeModifier.Operation.ADDITION);
            addAttributeModifier(player, AttributeRegistry.MANA_REGEN.get(), MANA_REGEN_UUID, "ISS Mana Regen", multiplierRegen, AttributeModifier.Operation.MULTIPLY_TOTAL);
        }
    }

    private int getTotalModifierLevel(LivingEntity entity) {
        int totalLevel = 0;
        for (ItemStack stack : entity.getArmorSlots()) {
            if (!stack.isEmpty()) {
                IToolStackView tool = ToolStack.from(stack);
                totalLevel += tool.getModifiers().getLevel(this.getId());
            }
        }

        return totalLevel;
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        Player player = context.getEntity() instanceof Player ? (Player) context.getEntity() : null;
        if (player != null) {
            removeAttributeModifier(player, AttributeRegistry.MAX_MANA.get(), MAX_MANA_UUID);
            removeAttributeModifier(player, AttributeRegistry.MANA_REGEN.get(), MANA_REGEN_UUID);
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
