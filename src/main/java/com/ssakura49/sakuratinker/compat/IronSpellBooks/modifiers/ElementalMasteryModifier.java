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

public class ElementalMasteryModifier extends BaseModifier {

    private static final UUID FIRE_SPELL_POWER = UUID.fromString("2fc05d10-9abf-4076-9143-043362c62351");
    private static final UUID ICE_SPELL_POWER = UUID.fromString("d09d3411-1b5e-4880-836e-a55f2a988f95");
    private static final UUID LIGHTNING_SPELL_POWER = UUID.fromString("cbca1965-6b50-474a-a888-8a671add1371");
    private static final UUID HOLY_SPELL_POWER = UUID.fromString("5cc332ef-9c10-466a-ad6c-ffc9c9507187");
    private static final UUID NATURE_SPELL_POWER = UUID.fromString("d8ebc2ff-1d85-478f-bf71-f73cdd2d3900");

    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        Player player = context.getEntity() instanceof Player ? (Player) context.getEntity() : null;
        if (player != null) {
            int level = modifier.getLevel();
            double fire = 0.1 * level;
            double ice = 0.1 * level;
            double lightning = 0.1 * level;
            double holy = 0.1 * level;
            double nature = 0.1 * level;
            addAttributeModifier(player, AttributeRegistry.FIRE_SPELL_POWER.get(), FIRE_SPELL_POWER, "ISS Fire SP", fire, AttributeModifier.Operation.MULTIPLY_TOTAL);
            addAttributeModifier(player, AttributeRegistry.ICE_SPELL_POWER.get(), ICE_SPELL_POWER, "ISS Ice SP", ice, AttributeModifier.Operation.MULTIPLY_TOTAL);
            addAttributeModifier(player, AttributeRegistry.LIGHTNING_SPELL_POWER.get(), LIGHTNING_SPELL_POWER, "ISS Lightning SP", lightning, AttributeModifier.Operation.MULTIPLY_TOTAL);
            addAttributeModifier(player, AttributeRegistry.HOLY_SPELL_POWER.get(), HOLY_SPELL_POWER, "ISS Holy SP", holy, AttributeModifier.Operation.MULTIPLY_TOTAL);
            addAttributeModifier(player, AttributeRegistry.NATURE_SPELL_POWER.get(), NATURE_SPELL_POWER, "ISS Nature SP", nature, AttributeModifier.Operation.MULTIPLY_TOTAL);
        }
    }

    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        Player player = context.getEntity() instanceof Player ? (Player) context.getEntity() : null;
        if (player != null) {
            removeAttributeModifier(player, AttributeRegistry.FIRE_SPELL_POWER.get(), FIRE_SPELL_POWER);
            removeAttributeModifier(player, AttributeRegistry.ICE_SPELL_POWER.get(), ICE_SPELL_POWER);
            removeAttributeModifier(player, AttributeRegistry.LIGHTNING_SPELL_POWER.get(), LIGHTNING_SPELL_POWER);
            removeAttributeModifier(player, AttributeRegistry.HOLY_SPELL_POWER.get(), HOLY_SPELL_POWER);
            removeAttributeModifier(player, AttributeRegistry.NATURE_SPELL_POWER.get(), NATURE_SPELL_POWER);
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
