package com.ssakura49.sakuratinker.content.entity;

import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileRange;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import com.ssakura49.sakuratinker.register.STEntities;
import com.ssakura49.sakuratinker.utils.ProjectileUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.List;
import java.util.Optional;

public class LaserProjectileEntity extends Projectile implements IEntityAdditionalSpawnData, IProjectileCritical, IProjectileDamage, IProjectileRange {
    public int lifeTime;
    public int aliveTicks = 0;
    private float range;
    private ItemStack laserStack;
    private ToolStack toolStack;
    private StatsNBT statsNBT;
    private float bonusDamage = 0.0F;
    private boolean critical = false;
    public boolean hasHit = false;


    public LaserProjectileEntity(EntityType<? extends LaserProjectileEntity> type, Level level) {
        super(type, level);
        this.laserStack = ItemStack.EMPTY;
        this.toolStack = null;
        this.statsNBT = StatsNBT.EMPTY;
        this.lifeTime = 100;
    }

    public LaserProjectileEntity(Level world, LivingEntity shooter) {
        super(STEntities.LASER_PROJECTILE.get(), world);
        this.laserStack = ItemStack.EMPTY;
        this.toolStack = null;
        this.statsNBT = StatsNBT.EMPTY;
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
        this.setBoundingBox(new AABB(this.getX() - 0.25, this.getY() - 0.25, this.getZ() - 0.25, this.getX() + 0.25, this.getY() + 0.25, this.getZ() + 0.25));
        this.setTool(shooter.getMainHandItem());
    }


    public LaserProjectileEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        super(STEntities.LASER_PROJECTILE.get(), level);
    }

    public void setTool(ItemStack tool) {
        if (!tool.isEmpty()) {
            this.laserStack = tool.copy();
            this.toolStack = ToolStack.from(tool);
            this.statsNBT = this.toolStack.getStats();
            this.range = this.statsNBT.getInt(STToolStats.RANGE);
            float speed = (float) this.getDeltaMovement().length();
            this.lifeTime = (int) range;
        } else {
            this.laserStack = ItemStack.EMPTY;
            this.toolStack = null;
            this.statsNBT = StatsNBT.EMPTY;
            this.lifeTime = 100;
            this.range = 16;
        }
    }

    @Override
    public void setCritical(boolean critical) {
        this.critical = critical;
    }
    @Override
    public boolean getCritical() {
        return this.critical;
    }
    @Override
    public void setDamage(float damage) {
        this.bonusDamage = damage;
    }
    @Override
    public float getDamage() {
        return this.statsNBT.get(ToolStats.ATTACK_DAMAGE) + this.bonusDamage + 0.5F;
    }
    @Override
    public void setRange(float range) {
        this.range = (int)range;
    }
    @Override
    public float getRange() {
        return this.range;
    }
    public ToolStack getTool() {
        return this.toolStack;
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    @Override
    public void tick() {
        super.tick();
        aliveTicks++;

        if (aliveTicks >= lifeTime) {
            this.discard();
        }
        if (this.toolStack == null || this.toolStack.isBroken()) {
            this.discard();
            return;
        }
        if (this.hasHit) {
            return;
        }

        Vec3 motion = this.getDeltaMovement();
        Vec3 start = this.position();
        Vec3 end = start.add(motion.scale(1.5));

        HitResult blockHit = this.level().clip(new ClipContext(start, end, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this));
        EntityHitResult entityHit = findHitEntity(start, end);
        if (entityHit != null) {
            this.onHit(entityHit);
            return;
        } else if (blockHit.getType() != HitResult.Type.MISS) {
            this.onHit(blockHit);
            return;
        }

        this.setDeltaMovement(motion);
        this.move(MoverType.SELF, motion);

        this.level();
        if (!this.level().isClientSide()) {
            this.applyDamage();
        }
    }

    private @Nullable EntityHitResult findHitEntity(Vec3 start, Vec3 end) {
        List<Entity> entities = this.level().getEntities(
                this,
                this.getBoundingBox().expandTowards(this.getDeltaMovement()),
                entity -> entity != this.getOwner() && entity.isAlive()
        );
        double nearestDistance = Double.MAX_VALUE;
        EntityHitResult result = null;
        for (Entity entity : entities) {
            AABB targetAABB = entity.getBoundingBox().inflate(0.3);
            Optional<Vec3> hitPos = targetAABB.clip(start, end);
            if (hitPos.isPresent()) {
                double distance = start.distanceToSqr(hitPos.get());
                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    result = new EntityHitResult(entity, hitPos.get());
                }
            }
        }
        return result;
    }

    private void applyDamage() {
        Vec3 start = this.position().subtract(this.getDeltaMovement());
        Vec3 end = this.position();
        AABB area = new AABB(start, end).inflate(0.5);
        for (LivingEntity entity : this.level().getEntitiesOfClass(
                LivingEntity.class, area, this::canHitEntity)) {
            ProjectileUtils.attackEntity(
                    this.laserStack.getItem(),
                    this,
                    this.toolStack,
                    (LivingEntity)this.getOwner(),
                    entity,
                    false
            );
        }
    }
    @Override
    protected void onHitEntity(EntityHitResult result) {
        if (this.toolStack == null || !(result.getEntity() instanceof LivingEntity target) || !(this.getOwner() instanceof LivingEntity attacker)) {
            return;
        }
        ProjectileUtils.attackEntity(
                this.laserStack.getItem(),
                this,
                this.toolStack,
                attacker,
                target,
                false
        );
        this.discard();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        this.hasHit = true;
        this.discard();
    }

    @Override
    public boolean isInWall() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Tool", laserStack.serializeNBT());
        tag.putFloat("BonusDamage", bonusDamage);
        tag.putBoolean("Critical", critical);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setTool(ItemStack.of(tag.getCompound("Tool")));
        this.bonusDamage = tag.getFloat("BonusDamage");
        this.critical = tag.getBoolean("Critical");
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buf) {
        buf.writeItem(this.laserStack);
        buf.writeFloat(this.bonusDamage);
        buf.writeBoolean(this.critical);
        buf.writeInt(this.lifeTime);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buf) {
        this.setTool(buf.readItem());
        this.bonusDamage = buf.readFloat();
        this.critical = buf.readBoolean();
        this.lifeTime = buf.readInt();
    }


    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected boolean canHitEntity(@NotNull net.minecraft.world.entity.Entity entity) {
        return super.canHitEntity(entity) && entity != this.getOwner();
    }

    @Override
    protected void defineSynchedData() {
    }

    public boolean hasHit() {
        return hasHit;
    }
}