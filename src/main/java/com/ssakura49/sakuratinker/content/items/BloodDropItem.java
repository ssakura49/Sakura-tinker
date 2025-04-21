package com.ssakura49.sakuratinker.content.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class BloodDropItem extends Item {

    public BloodDropItem(Properties properties) {
        super(properties);
    }


    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 64;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 180, 0));
        }
        return super.finishUsingItem(stack, level, entity);
    }

}
