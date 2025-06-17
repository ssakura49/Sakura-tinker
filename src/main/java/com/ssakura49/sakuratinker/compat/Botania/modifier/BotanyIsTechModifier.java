package com.ssakura49.sakuratinker.compat.Botania.modifier;

import com.ssakura49.sakuratinker.content.tools.capability.ForgeEnergyCapability;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import com.ssakura49.sakuratinker.register.STModifiers;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.mana.ManaItemHandler;
import vazkii.botania.common.item.BotaniaItems;

public class BotanyIsTechModifier extends BaseModifier {

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (tool.getDamage() > 0 && modifier.getLevel() > 0 && tool.getModifierLevel(STModifiers.EnergyDisplay.get()) > 0 && holder instanceof Player player) {
            if (!world.isClientSide && ManaItemHandler.INSTANCE.requestManaExactForTool(new ItemStack(BotaniaItems.terraSword), player, 200, true)) {
                int energyToAdd = 500 * modifier.getLevel();
                int currentEnergy = ForgeEnergyCapability.getEnergy(tool);
                int maxEnergy = ForgeEnergyCapability.getMaxEnergy(tool);
                int newEnergy = Math.min(currentEnergy + energyToAdd, maxEnergy);
                ForgeEnergyCapability.setEnergy(tool, newEnergy);
            }
        }
    }
}
