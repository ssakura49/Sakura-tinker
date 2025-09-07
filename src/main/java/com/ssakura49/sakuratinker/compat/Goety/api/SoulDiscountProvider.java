package com.ssakura49.sakuratinker.compat.Goety.api;

import net.minecraft.world.entity.LivingEntity;

public interface SoulDiscountProvider {
    /**
     * @param living 持有者
     * @return 返回增加的折扣百分比（比如 20 = 20%）
     */
    int getSoulDiscount(LivingEntity living);
}
