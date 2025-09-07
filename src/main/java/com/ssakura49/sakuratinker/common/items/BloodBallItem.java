package com.ssakura49.sakuratinker.common.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class BloodBallItem extends Item {

    public BloodBallItem(Properties properties) {
        super(properties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
        if (!level.isClientSide) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 20 * 45, 2));
            entity.addEffect(new MobEffectInstance(MobEffects.HEALTH_BOOST, 120, 0));
        }
        return super.finishUsingItem(stack, level, entity);
    }
}
