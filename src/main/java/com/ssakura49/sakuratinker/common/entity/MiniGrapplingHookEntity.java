package com.ssakura49.sakuratinker.common.entity;

import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.register.STEntities;
import com.ssakura49.sakuratinker.utils.ProjectileUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class MiniGrapplingHookEntity extends Projectile implements IEntityAdditionalSpawnData, IProjectileCritical, IProjectileDamage {
    private static final EntityDataAccessor<Boolean> DATA_HOOKED = SynchedEntityData.defineId(MiniGrapplingHookEntity.class, EntityDataSerializers.BOOLEAN);
    private static final double PULL_SPEED = 0.2;
    private static final double PULL_DISTANCE = 2.0;
    private static final double PLAYER_PULL_SPEED = 0.3;
    private static final int DAMAGE_INTERVAL = 20;
    private static final int MAX_LIFETIME = 600;

    private ItemStack hookStack;
    private ToolStack toolStack;
    private StatsNBT stats;
    private float bonusDamage = 0.0F;
    private boolean critical = false;
    private int ticksSinceHit = 0;
    private LivingEntity hookedEntity;
    private BlockPos hookedBlockPos;
    private Vec3 hookedPoint;
    private boolean hasHitEntity = false;

    public MiniGrapplingHookEntity(EntityType<? extends MiniGrapplingHookEntity> type, Level level) {
        super(type, level);
        this.hookStack = ItemStack.EMPTY;
        this.toolStack = null;
        this.stats = StatsNBT.EMPTY;
    }

    public MiniGrapplingHookEntity(Level level, LivingEntity shooter) {
        super(STEntities.MINI_GRAPPLING_HOOK.get(), level);
        this.hookStack = ItemStack.EMPTY;
        this.toolStack = null;
        this.stats = StatsNBT.EMPTY;
        this.setOwner(shooter);
        this.setPos(this.getX(), this.getY(), this.getZ());
        this.setBoundingBox(new AABB(this.getX() - 0.25, this.getY() - 0.25, this.getZ() - 0.25,
                this.getX() + 0.25, this.getY() + 0.25, this.getZ() + 0.25));
    }

    public MiniGrapplingHookEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        super(STEntities.MINI_GRAPPLING_HOOK.get(), level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_HOOKED, false);
    }

    public boolean isHooked() {
        return this.entityData.get(DATA_HOOKED);
    }

    private void setHooked(boolean hooked) {
        this.entityData.set(DATA_HOOKED, hooked);
    }

    public void setTool(ItemStack tool) {
        if (!tool.isEmpty()) {
            this.hookStack = tool.copy();
            this.toolStack = ToolStack.from(tool);
            this.stats = this.toolStack.getStats();
        } else {
            this.hookStack = ItemStack.EMPTY;
            this.toolStack = null;
            this.stats = StatsNBT.EMPTY;
        }
    }
    public ToolStack getTool() {
        return this.toolStack;
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
        return this.stats.get(ToolStats.ATTACK_DAMAGE) + this.bonusDamage + 0.5F;
    }


    public void setHasHitEntity(boolean hit) {
        this.hasHitEntity = hit;
    }

    public boolean hasHitEntity() {
        return hasHitEntity;
    }

    private int ropeColor = 0xA0522D;

    public void setRopeColor(int color) {
        this.ropeColor = color;
    }

    public int getRopeColor() {
        return this.ropeColor;
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        int vlevel = this.getTool().getStats().getInt(ToolStats.VELOCITY);
        int alevel = this.getTool().getStats().getInt(ToolStats.ACCURACY);
        velocity *= (1.0F + vlevel * 0.2F);
        inaccuracy *= (0.75F + alevel * 0.1F);
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount > MAX_LIFETIME) {
            this.discard();
            return;
        }

        if (this.hookedEntity != null && !this.hookedEntity.isAlive()) {
            this.discard();
            return;
        }

        Vec3 motion = this.getDeltaMovement();

        if (!this.isHooked()) {
            Vec3 start = this.position();
            Vec3 end = start.add(motion);

            // 方块命中
            HitResult blockResult = this.level().clip(new ClipContext(
                    start,
                    end,
                    ClipContext.Block.COLLIDER,
                    ClipContext.Fluid.NONE,
                    this
            ));
            Vec3 blockHitLoc = blockResult.getType() != HitResult.Type.MISS ? blockResult.getLocation() : end;

            EntityHitResult entityResult = net.minecraft.world.entity.projectile.ProjectileUtil.getEntityHitResult(
                    this.level(), this, start, end,
                    this.getBoundingBox().expandTowards(motion).inflate(0.3),  // 可调大
                    this::canHitEntity
            );

            if (entityResult != null) {
                double entityDist = start.distanceToSqr(entityResult.getLocation());
                double blockDist = start.distanceToSqr(blockHitLoc);
                if (entityDist < blockDist || blockResult.getType() == HitResult.Type.MISS) {
                    this.onHit(entityResult);
                } else {
                    this.onHit(blockResult);
                }
            } else if (blockResult.getType() != HitResult.Type.MISS) {
                this.onHit(blockResult);
            }

            this.setDeltaMovement(motion.scale(0.98));
            this.move(MoverType.SELF, this.getDeltaMovement());
        }

        if (this.hookedEntity != null) {
            handleEntityPulling();
            this.setPos(
                    this.hookedEntity.getX(),
                    this.hookedEntity.getY() + this.hookedEntity.getBbHeight() * 0.5,
                    this.hookedEntity.getZ()
            );
            if (this.ticksSinceHit % DAMAGE_INTERVAL == 0) {
                applyDamage();
            }
            this.ticksSinceHit++;
        } else if (this.hookedBlockPos != null) {
            handlePlayerPulling();
        }
    }

    private void handleEntityPulling() {
        Entity owner = this.getOwner();
        if (owner == null || !owner.isAlive() || (this.hookedEntity != null && !this.hookedEntity.isAlive())) {
            this.discard();
            return;
        }

        Vec3 ownerPos = owner.position();
        Vec3 targetPos = this.hookedEntity.position();
        Vec3 direction = ownerPos.subtract(targetPos).normalize();
        double distance = ownerPos.distanceTo(targetPos);

        if (distance > PULL_DISTANCE) {
            double pullStrength = Math.min(PULL_SPEED, distance - PULL_DISTANCE);
            Vec3 velocity = direction.scale(pullStrength);

            this.hookedEntity.setDeltaMovement(
                    this.hookedEntity.getDeltaMovement().add(velocity)
            );
        }
//        else {
//            this.discard();
//        }
    }

    private void handlePlayerPulling() {
        Entity owner = this.getOwner();
        if (!(owner instanceof LivingEntity livingOwner)) {
            this.discard();
            return;
        }

        if (!isBlockStillValid()) {
            this.discard();
            return;
        }

        Vec3 ownerPos = owner.position();
        Vec3 targetPos = this.hookedPoint;
        Vec3 direction = targetPos.subtract(ownerPos).normalize();
        double distance = ownerPos.distanceTo(targetPos);

        if (distance > 1.0) {
            double pullStrength = Math.min(PLAYER_PULL_SPEED, distance * 0.1);
            Vec3 velocity = direction.scale(pullStrength);

            owner.setDeltaMovement(
                    owner.getDeltaMovement().multiply(0.2, 0.5, 0.2).add(velocity)
            );

            if (owner.getDeltaMovement().y < 0) {
                owner.setDeltaMovement(owner.getDeltaMovement().multiply(1.0, 0.8, 1.0));
            }
        }
//        else {
//            this.discard();
//        }
    }

    private boolean isBlockStillValid() {
        if (this.hookedBlockPos == null) return false;

        BlockState state = this.level().getBlockState(this.hookedBlockPos);
        return !state.isAir() && !state.getCollisionShape(this.level(), this.hookedBlockPos).isEmpty();
    }

    private void applyDamage() {
        if (this.toolStack == null || this.hookedEntity == null || this.getOwner() == null) {
            return;
        }
        ProjectileUtils.attackEntity(
                this.hookStack.getItem(),
                this,
                this.toolStack,
                (LivingEntity) getOwner(),
                hookedEntity,
                false
        );

//        AttackUtils.attackEntity(
//                this.toolStack,
//                (LivingEntity) this.getOwner(),
//                InteractionHand.MAIN_HAND,
//                this.hookedEntity,
//                () -> 1.0f,
//                false,
//                EquipmentSlot.MAINHAND,
//                true,
//                getDamage(),
//                true
//        );
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        if (target == this.getOwner() || !(target instanceof LivingEntity livingEntity)) {
            return;
        }

        this.hookedEntity = livingEntity;
        this.hookedBlockPos = null;
        this.setHooked(true);
        this.setDeltaMovement(Vec3.ZERO);
        setHasHitEntity(true);

        this.playSound(SoundEvents.LEASH_KNOT_PLACE, 1.0F, 1.0F);
        applyDamage();
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult result) {
        if (this.hookedEntity == null && this.hookedBlockPos == null) {
            this.hookedBlockPos = result.getBlockPos();
            this.hookedPoint = result.getLocation();
            this.setHooked(true);
            this.setDeltaMovement(Vec3.ZERO);

            this.playSound(SoundEvents.CHAIN_PLACE, 1.0F, 1.0F);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Tool", hookStack.serializeNBT());
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
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItem(this.hookStack);
        buffer.writeFloat(this.bonusDamage);
        buffer.writeBoolean(this.critical);
    }
    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.setTool(buffer.readItem());
        this.bonusDamage = buffer.readFloat();
        this.critical = buffer.readBoolean();
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public Vec3 getHookedPoint() {
        if (this.hookedEntity != null) {
            return this.hookedEntity.position().add(0, this.hookedEntity.getBbHeight() * 0.5, 0);
        } else if (this.hookedBlockPos != null) {
            return this.hookedPoint != null ? this.hookedPoint : Vec3.atCenterOf(this.hookedBlockPos);
        }
        return this.position();
    }

    @Override
    protected boolean canHitEntity(@NotNull Entity entity) {
        return super.canHitEntity(entity) && this.getOwner() != null && !entity.is(this.getOwner());
    }

}
