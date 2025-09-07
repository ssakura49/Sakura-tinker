package com.ssakura49.sakuratinker.compat.BuddyCards.modifiers;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class PerfectionismModifier extends BaseModifier {
    @Override
    public boolean isNoLevels() {
        return true;
    }

    @Override
    public void addToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        if (context instanceof IToolStackView tool) {
            BuddyCardMasterModifier.PlayerProgressCache cache = BuddyCardMasterModifier.getProgressCache(tool);
            double bonus = 0.001f * cache.getStatsBonus();
            ToolStats.MINING_SPEED.multiply(builder, 1 + bonus);
            ToolStats.ATTACK_SPEED.multiply(builder, 1 + bonus);
            ToolStats.ATTACK_DAMAGE.multiply(builder, 1 + bonus);
            ToolStats.VELOCITY.multiply(builder, 1 + bonus);
            ToolStats.ARMOR.multiply(builder, 1 + bonus);
            ToolStats.ARMOR_TOUGHNESS.multiply(builder, 1 + bonus);
            ToolStats.DURABILITY.multiply(builder, 1 + bonus);
            ToolStats.DRAW_SPEED.multiply(builder, 1 + bonus);
            ToolStats.PROJECTILE_DAMAGE.multiply(builder, 1 + bonus);
        }
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (!(holder instanceof Player player) || level.isClientSide) return;
        ModDataNBT data = tool.getPersistentData();
        int counter = data.getInt(BuddyCardMasterModifier.getTickCounterKey());
        if (counter > 0) {
            data.putInt(BuddyCardMasterModifier.getTickCounterKey(), counter - 1);
            return;
        }
        ToolStack.from(itemStack).rebuildStats();
        data.putInt(BuddyCardMasterModifier.getTickCounterKey(), 5);
    }
}
