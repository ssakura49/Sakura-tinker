package com.ssakura49.sakuratinker.common.entity;

import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.register.STEntities;
import com.ssakura49.sakuratinker.register.STItems;
import com.ssakura49.sakuratinker.utils.ProjectileUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nullable;

public class ShurikenEntity extends ThrowableItemProjectile implements IEntityAdditionalSpawnData, IProjectileCritical, IProjectileDamage {
    private ItemStack shurikenStack;
    private ToolStack toolStack;
    private StatsNBT stats;
    private float bonusDamage = 0.0F;
    private boolean critical = false;
    private boolean inGround;
    protected int inGroundTime;
    @Nullable
    private BlockState lastState;
    private int numTicks = 0;
    private ItemStack renderItem;


    public ShurikenEntity(EntityType<? extends ShurikenEntity> entityType, Level level) {
        super(entityType, level);
        this.shurikenStack = null;
        this.toolStack = null;
        this.stats = StatsNBT.EMPTY;
    }

    public ShurikenEntity(Level level, double x, double y, double z) {
        super(STEntities.SHURIKEN_ENTITY.get(), level);
    }

    public ShurikenEntity(Level level, LivingEntity entity) {
        super(STEntities.SHURIKEN_ENTITY.get(), entity, level);
    }

    public void setTool(ItemStack tool) {
        if (tool != null) {
            this.shurikenStack = tool.copy();
            this.toolStack = ToolStack.from(tool);
            this.stats = this.toolStack.getStats();
        } else {
            this.shurikenStack = null;
            this.toolStack = null;
            this.stats = StatsNBT.EMPTY;
        }
    }

    protected void tickDeSpawn() {
        ++this.numTicks;
        if (this.numTicks >= 1200) {
            this.discard();
        }
    }

    private boolean shouldFall() {
        return this.inGround && this.level().noCollision((new AABB(this.position(), this.position())).inflate(0.06D));
    }

    private void startFalling() {
        this.inGround = false;
        Vec3 vec3 = this.getDeltaMovement();
        this.setDeltaMovement(vec3.multiply((double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F), (double) (this.random.nextFloat() * 0.2F)));
        this.numTicks = 0;
    }

    @Override
    protected @NotNull Item getDefaultItem() {
        return STItems.shuriken.get();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Tool", this.shurikenStack.serializeNBT());
        tag.putFloat("BonusDamage", this.bonusDamage);
        tag.putBoolean("Critical", this.critical);
        tag.putInt("TickCount", numTicks);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setTool(ItemStack.of(tag.getCompound("Tool")));
        this.bonusDamage = tag.getFloat("BonusDamage");
        this.critical = tag.getBoolean("Critical");
        this.numTicks = tag.getInt("TickCount");
    }

    @Override
    public void tick() {
        super.tick();
        BlockPos blockPos = this.blockPosition();
        BlockState blockState = this.level().getBlockState(blockPos);
        if (!blockState.isAir()) {
            VoxelShape voxelShape = blockState.getCollisionShape(this.level(), blockPos);
            if (!voxelShape.isEmpty()) {
                Vec3 vec3 = this.position();
                for (AABB aabb : voxelShape.toAabbs()) {
                    if (aabb.move(blockPos).contains(vec3)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }
        if (this.inGround) {
            if (!this.shouldFall()) {
                this.setDeltaMovement(Vec3.ZERO);
            }
            else if (this.lastState!= blockState && this.shouldFall()) {
                this.startFalling();
            }
            if (!this.level().isClientSide) {
                this.tickDeSpawn();
            }
        }
        else {
            numTicks++;
            this.inGroundTime = 0;
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        Entity player = this.getOwner();
        if (player != null && target instanceof LivingEntity entity) {
            ((LivingEntity)player).setLastHurtByMob(entity);
        }

        LivingEntity livingOwner = this.getOwner() instanceof LivingEntity ? (LivingEntity)this.getOwner() : null;
        if (!this.level().isClientSide()) {
            if (toolStack != null && !this.toolStack.isBroken()){
                 ProjectileUtils.attackEntity(
                        this.shurikenStack.getItem(),
                        this,
                        this.toolStack,
                        livingOwner,
                        target,
                        false
                );
            }
            this.discard();
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        this.lastState = this.level().getBlockState(this.blockPosition());
        Vec3 positionDifference = result.getLocation().subtract(this.getX(), this.getY(), this.getZ());
        this.setDeltaMovement(positionDifference);
        Vec3 vec3 = positionDifference.normalize().scale(0.05F);
        this.setPosRaw(this.getX() - vec3.x, this.getY() - vec3.y, this.getZ() - vec3.z);
        this.inGround = true;
        this.setCritical(false);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeUUID(this.getUUID());
        buffer.writeItem(this.shurikenStack);
    }
    @Override
    public void readSpawnData(FriendlyByteBuf additionalData) {
        this.setUUID(additionalData.readUUID());
        this.setTool(additionalData.readItem());
        this.bonusDamage = additionalData.readFloat();
        this.critical = additionalData.readBoolean();
    }

    public boolean isInGround() {
        return inGround;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }

    public boolean getCritical() {
        return this.critical;
    }

    public void setDamage(float damage) {
        this.bonusDamage = damage;
    }

    public float getDamage() {
        float result = (Float)this.stats.get(ToolStats.ATTACK_DAMAGE) + this.bonusDamage + 1.0F;
        if (this.critical) {
            result *= 1.5F;
        }
        return result;
    }

    @Nullable
    public BlockState getLastState() {
        return lastState;
    }
}
