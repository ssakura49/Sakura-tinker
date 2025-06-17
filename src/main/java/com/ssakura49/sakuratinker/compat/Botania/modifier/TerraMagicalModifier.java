package com.ssakura49.sakuratinker.compat.Botania.modifier;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import vazkii.botania.api.mana.ManaItem;
import vazkii.botania.api.mana.ManaItemHandler;

public class TerraMagicalModifier extends BaseModifier {
    private static final int MANA_PER_TICK = 2;
    private static final int HEAL_INTERVAL = 40;

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (level.isClientSide || !(holder instanceof Player player)) return;
        for (ItemStack stack : ManaItemHandler.instance().getManaAccesories(player)) {
            if (!stack.isEmpty() && stack.getItem() instanceof ManaItem manaItem
                    && manaItem.canReceiveManaFromItem(stack)) {
                boolean success = ManaItemHandler.instance().dispatchManaExact(stack, player, MANA_PER_TICK * modifier.getLevel(), true);
                if (success) break;
            }
        }
        for (ItemStack stack : player.getInventory().items) {
            if (!stack.isEmpty() && stack.getItem() instanceof ManaItem manaItem
                    && manaItem.canReceiveManaFromItem(stack)) {
                boolean success = ManaItemHandler.instance().dispatchManaExact(stack, player, MANA_PER_TICK * modifier.getLevel(), true);
                if (success) break;
            }
        }
        if (level.getGameTime() % HEAL_INTERVAL == 0 && player.getHealth() < player.getMaxHealth()) {
            player.heal(1.0F * modifier.getLevel());
        }
    }

}
