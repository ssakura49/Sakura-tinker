package com.ssakura49.sakuratinker.content.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class FireImmunityModifier extends BaseModifier {
    @Override
    public boolean isNoLevels(){
        return true;
    }
    @Override
    public void onInventoryTick(IToolStackView tool, ModifierEntry modifier, Level world, LivingEntity entity, int index, boolean isSelected, boolean isCorrectSlot, ItemStack stack) {
        if (entity instanceof Player player && isCorrectSlot) {
            player.clearFire();
        }
    }
}
