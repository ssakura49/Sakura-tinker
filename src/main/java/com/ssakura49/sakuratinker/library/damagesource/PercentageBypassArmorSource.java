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

public class PercentageBypassArmorSource extends LegacyDamageSource{
    private final float percentage;
    public PercentageBypassArmorSource(Holder<DamageType> holder, @Nullable Entity directEntity, @Nullable Entity causingEntity, @Nullable Vec3 sourcePos, float percentage) {
        super(holder, directEntity, causingEntity, sourcePos);
        this.percentage=Math.min(1,percentage);
    }
    public PercentageBypassArmorSource(Holder<DamageType> holder, @Nullable Entity directEntity, @Nullable Entity causingEntity,float percentage) {
        this(holder, directEntity, causingEntity, null,percentage);
    }
    public PercentageBypassArmorSource(Holder<DamageType> holder, @Nullable Entity directEntity,float percentage) {
        this(holder, directEntity, directEntity, null,percentage);
    }

    public float getPercentage(){
        return this.percentage;
    }


    public static PercentageBypassArmorSource playerAttack(@NotNull Player player, float percentage){
        return new PercentageBypassArmorSource(player.damageSources().playerAttack(player).typeHolder(),player,percentage);
    }
    public static PercentageBypassArmorSource mobAttack(@NotNull LivingEntity living, float percentage){
        return new PercentageBypassArmorSource(living.damageSources().mobAttack(living).typeHolder(),living,percentage);
    }
    public static PercentageBypassArmorSource projectileHit(@NotNull Projectile projectile, float percentage){
        return new PercentageBypassArmorSource(projectile.damageSources().mobProjectile(projectile,projectile.getOwner() instanceof LivingEntity living?living:null).typeHolder(),projectile,projectile.getOwner(),percentage);
    }
    public static PercentageBypassArmorSource Any(Holder<DamageType> holder, Entity directEntity, Entity causingEntity, float percentage){
        return new PercentageBypassArmorSource(holder,directEntity,causingEntity,percentage);
    }
}
