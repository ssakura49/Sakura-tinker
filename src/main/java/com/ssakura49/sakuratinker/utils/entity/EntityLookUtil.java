package com.ssakura49.sakuratinker.utils.entity;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Predicate;

public class EntityLookUtil {
    public EntityLookUtil() {
    }

    @Nullable
    public static Entity getEntityLookedAt(Player player, double range) {
        Level level = player.level();
        Vec3 eyePos = player.getEyePosition(1.0F); // 玩家眼睛位置
        Vec3 lookVec = player.getLookAngle();      // 玩家视线方向
        Vec3 reachVec = eyePos.add(lookVec.scale(range)); // 终点位置

        AABB aabb = player.getBoundingBox().expandTowards(lookVec.scale(range)).inflate(1.0D);
        EntityHitResult result = ProjectileUtil.getEntityHitResult(
                player, eyePos, reachVec, aabb,
                entity -> !entity.isSpectator() && entity.isPickable() && entity != player,
                range * range
        );

        return result != null ? result.getEntity() : null;
    }
}
