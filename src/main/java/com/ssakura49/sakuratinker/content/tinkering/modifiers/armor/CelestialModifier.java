package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.context.EquipmentChangeContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class CelestialModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }


    @Override
    public void onEquip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if (isArmorSlot(context.getChangedSlot()) && context.getEntity() instanceof Player player) {
            player.getAbilities().mayfly = true;
            player.getAbilities().flying = true;
            player.onUpdateAbilities();
        }
    }
    @Override
    public void onUnequip(IToolStackView tool, ModifierEntry modifier, EquipmentChangeContext context) {
        if (isArmorSlot(context.getChangedSlot())) {
                if (context.getEntity() instanceof Player player) {
                    if (!hasCelestialArmor(player))  {
                        if (!player.isCreative() && !player.isSpectator()) {
                            player.getAbilities().mayfly = false;
                            player.getAbilities().flying = false;
                            player.onUpdateAbilities();
                        }
                    }
            }
        }
    }
    private boolean isArmorSlot(EquipmentSlot slot) {
        return slot == EquipmentSlot.HEAD || slot == EquipmentSlot.CHEST || slot == EquipmentSlot.LEGS || slot == EquipmentSlot.FEET;
    }
    private boolean hasCelestialArmor(Player player) {
        for (ItemStack stack : player.getInventory().armor) {
            if (!stack.isEmpty()) {
                IToolStackView armorTool = ToolStack.from(stack);
                for (ModifierEntry entry : armorTool.getModifiers().getModifiers()) {
                    if (entry.getModifier() == this) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
