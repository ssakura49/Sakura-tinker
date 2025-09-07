package com.ssakura49.sakuratinker.common.entity.item;

import com.ssakura49.sakuratinker.common.entity.BulletEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BulletItem extends Item {

    public BulletItem(Properties pProperties) {
        super(pProperties);
    }


    public BulletEntity createBullet(Level level, ItemStack stack, LivingEntity shooter) {
        BulletEntity entity = new BulletEntity(level, shooter);
        entity.setTool(stack);
        return entity;
    }
}
