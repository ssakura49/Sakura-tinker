package com.ssakura49.sakuratinker.common.tinkering.modifiers.armor;

import com.ssakura49.sakuratinker.generic.BaseModifier;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class AgileModifier extends BaseModifier {
    @Override
    public void modifierOnInventoryTick(IToolStackView tool, ModifierEntry modifier, Level level, LivingEntity holder,
                                        int itemSlot, boolean isSelected, boolean isCorrectSlot, ItemStack itemStack) {
        if (level.isClientSide || isCorrectSlot || !(holder instanceof Player player)) return;
        boolean hasWaterBucket = player.getMainHandItem().is(Items.WATER_BUCKET) || player.getOffhandItem().is(Items.WATER_BUCKET);
        if (hasWaterBucket) {
            player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 10, 1, true, false, true));
        }
    }
}
