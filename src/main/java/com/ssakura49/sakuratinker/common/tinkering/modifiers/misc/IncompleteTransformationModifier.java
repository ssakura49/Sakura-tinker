package com.ssakura49.sakuratinker.common.tinkering.modifiers.misc;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.stat.ModifierStatsBuilder;
import slimeknights.tconstruct.tools.TinkerModifiers;
import slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier;

import static slimeknights.tconstruct.tools.modifiers.slotless.OverslimeModifier.OVERSLIME_STAT;

public class IncompleteTransformationModifier extends BaseModifier {
    @Override
    public void modifierAddToolStats(IToolContext context, ModifierEntry modifier, ModifierStatsBuilder builder) {
        OVERSLIME_STAT.add(builder, 1000 * modifier.getLevel());
    }

    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity holder, int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        OverslimeModifier overSlime = TinkerModifiers.overslime.get();
        if (world.getGameTime() % 100 == 0) {
            int current = overSlime.getShield(tool);
            int max = overSlime.getShieldCapacity(tool, modifier);
            if (current < max) {
                overSlime.addOverslime(tool, modifier, (int) (0.1 * modifier.getLevel() * max));
            }
        }
    }
}
