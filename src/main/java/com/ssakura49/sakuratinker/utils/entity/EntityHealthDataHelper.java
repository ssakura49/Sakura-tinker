package com.ssakura49.sakuratinker.utils.entity;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityHealthDataHelper {
    public static final EntityDataAccessor<Float> DATA_HEALTH_ID = getHealthDataAccessor();

    private static EntityDataAccessor<Float> getHealthDataAccessor() {
        try {
            Field field = LivingEntity.class.getDeclaredField("f_20961_");
            field.setAccessible(true);
            if (EntityDataAccessor.class.isAssignableFrom(field.getType())) {
                @SuppressWarnings("unchecked")
                EntityDataAccessor<Float> accessor = (EntityDataAccessor<Float>) field.get(null);
                return accessor;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public static void reflectionPenetratingDamage(Entity target, Player player, float value) {
        if (!(target instanceof LivingEntity living)) return;
        if (DATA_HEALTH_ID == null) return;
        float currentHealth = living.getEntityData().get(DATA_HEALTH_ID);
        float newHealth = currentHealth-value;
        living.getEntityData().set(DATA_HEALTH_ID, newHealth);
        if (living.getHealth() <= 0.0F) {
            living.die(player.damageSources().playerAttack(player));
        }
    }
}
