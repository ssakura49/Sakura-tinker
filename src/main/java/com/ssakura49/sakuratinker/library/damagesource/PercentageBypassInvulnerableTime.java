package com.ssakura49.sakuratinker.library.damagesource;

import net.minecraft.core.Holder;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PercentageBypassInvulnerableTime extends LegacyDamageSource{
    private final float bypassPercentage;
    /**
     * 构造方法
     * @param holder 伤害类型Holder
     * @param directEntity 直接造成伤害的实体
     * @param causingEntity 间接造成伤害的实体
     * @param sourcePos 伤害源位置
     * @param bypassPercentage 穿透无敌帧的百分比(0-1)
     */
    public PercentageBypassInvulnerableTime(Holder<DamageType> holder, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 sourcePos, float bypassPercentage) {
        super(holder, directEntity, causingEntity, sourcePos);
        this.bypassPercentage = Math.min(1, Math.max(0, bypassPercentage));
        this.setBypassInvulnerableTime();
    }

    public PercentageBypassInvulnerableTime(Holder<DamageType> holder, @Nullable Entity directEntity, @Nullable Entity causingEntity, float bypassPercentage) {
        this(holder, directEntity, causingEntity, null, bypassPercentage);
    }

    public PercentageBypassInvulnerableTime(Holder<DamageType> holder, @Nullable Entity directEntity, float bypassPercentage) {
        this(holder, directEntity, directEntity, null, bypassPercentage);
    }

    /**
     * 获取穿透无敌帧的百分比
     */
    public float getBypassPercentage() {
        return this.bypassPercentage;
    }

    /**
     * 创建玩家攻击伤害源
     */
    public static PercentageBypassInvulnerableTime playerAttack(@NotNull Player player, float bypassPercentage) {
        return new PercentageBypassInvulnerableTime(player.damageSources().playerAttack(player).typeHolder(), player, bypassPercentage);
    }

    /**
     * 创建生物攻击伤害源
     */
    public static PercentageBypassInvulnerableTime mobAttack(@NotNull LivingEntity living, float bypassPercentage) {
        return new PercentageBypassInvulnerableTime(
                living.damageSources().mobAttack(living).typeHolder(),
                living,
                bypassPercentage
        );
    }

    /**
     * 创建投射物伤害源
     */
    public static PercentageBypassInvulnerableTime projectileHit(@NotNull Projectile projectile, float bypassPercentage) {
        return new PercentageBypassInvulnerableTime(projectile.damageSources().mobProjectile(projectile, projectile.getOwner() instanceof LivingEntity living ? living : null).typeHolder(), projectile, projectile.getOwner(), bypassPercentage);
    }
    /**
     * 通用创建方法
     */
    public static PercentageBypassInvulnerableTime any(Holder<DamageType> holder, Entity directEntity, Entity causingEntity, float bypassPercentage) {
        return new PercentageBypassInvulnerableTime(holder, directEntity, causingEntity, bypassPercentage);
    }
}
