package com.ssakura49.sakuratinker.content.entity.base;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;

public class VisualScaledProjectile extends Projectile {
    public static final String KEY_SCALE = "scale";
    public static final String KEY_SCALE_OLD = "scale_old";
    public static final String KEY_DAMAGE = "damage";
    public float baseDamage = 1;
    public static final EntityDataAccessor<Float> DATA_SCALE = SynchedEntityData.defineId(VisualScaledProjectile.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> DATA_SCALE_OLD = SynchedEntityData.defineId(VisualScaledProjectile.class, EntityDataSerializers.FLOAT);

    public VisualScaledProjectile(EntityType<? extends VisualScaledProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setScale(1f);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_SCALE,1f);
        this.entityData.define(DATA_SCALE_OLD,1f);
    }

    public float getScale(){
        return this.entityData.get(DATA_SCALE);
    }
    public void setScale(float amount){
        this.entityData.set(DATA_SCALE,amount);
    }
    public float getScaleOld(){
        return this.entityData.get(DATA_SCALE_OLD);
    }
    public void setScaleOld(float amount){
        this.entityData.set(DATA_SCALE_OLD,amount);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setScale(compoundTag.getFloat(KEY_SCALE));
        this.setScaleOld(compoundTag.getFloat(KEY_SCALE_OLD));
        this.baseDamage = compoundTag.getFloat(KEY_DAMAGE);
    }
    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putFloat(KEY_SCALE,this.getScale());
        compoundTag.putFloat(KEY_SCALE_OLD,this.getScaleOld());
        compoundTag.putFloat(KEY_DAMAGE,this.baseDamage);
    }
}
