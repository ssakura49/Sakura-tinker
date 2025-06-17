package com.ssakura49.sakuratinker.compat.ExtraBotany.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import vazkii.botania.api.mana.ManaItemHandler;

public class BodyModifier extends BaseModifier {
    private static final int REPAIR_COST = 200;
    private static final int BROKEN_COST = 200000;

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (!(holder instanceof Player player) || world.isClientSide) return;

        if (tool.getDamage() > 0) {
            if (ManaItemHandler.INSTANCE.requestManaExactForTool(stack, player, REPAIR_COST, true)) {
                tool.setDamage(tool.getDamage() - 1);
            }
        }
        ToolStack toolStack = ToolStack.from(stack);
        if (tool.isBroken()) {
            if (ManaItemHandler.INSTANCE.requestManaExactForTool(stack, player, BROKEN_COST, true)) {
                CompoundTag tag = stack.getTag();
                if (tag != null && tag.contains("tic_broken")) {
                    tag.remove("tic_broken");
                }
            }
        }
    }
}
