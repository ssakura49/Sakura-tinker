package com.ssakura49.sakuratinker.common.entity;

import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileRange;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import com.ssakura49.sakuratinker.register.STEntities;
import com.ssakura49.sakuratinker.utils.ProjectileUtils;
import com.ssakura49.sakuratinker.utils.entity.EntityUtil;
import com.ssakura49.sakuratinker.utils.tinker.AttackUtil;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.*;

public class LaserProjectileEntity extends Projectile implements IEntityAdditionalSpawnData, IProjectileCritical, IProjectileDamage, IProjectileRange {
    private static final EntityDataAccessor<Vector3f> START_POS = SynchedEntityData.defineId(LaserProjectileEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Vector3f> END_POS = SynchedEntityData.defineId(LaserProjectileEntity.class, EntityDataSerializers.VECTOR3);
    public int lifeTime = 0;
    public int aliveTicks = 0;
    private float range;
    private ItemStack laserStack;
    private ToolStack toolStack;
    private StatsNBT statsNBT;
    private float bonusDamage = 0.0F;
    private boolean critical = false;
    public boolean hasHit = false;
    private LivingEntity shooter;
    private Vec3 startPos;
    private Vec3 endPos;
    private int penetrationCount;

    public LaserProjectileEntity(EntityType<? extends LaserProjectileEntity> type, Level level) {
        super(type, level);
        this.laserStack = ItemStack.EMPTY;
        this.toolStack = null;
        this.statsNBT = StatsNBT.EMPTY;
    }

    public LaserProjectileEntity(Level world, LivingEntity shooter) {
        super(STEntities.LASER_PROJECTILE.get(), world);
        this.laserStack = ItemStack.EMPTY;
        this.toolStack = null;
        this.statsNBT = StatsNBT.EMPTY;
        this.shooter = shooter;
        this.setOwner(shooter);
        this.setPos(shooter.getX(), shooter.getEyeY() - 0.1, shooter.getZ());
        this.setBoundingBox(new AABB(this.getX() - 0.25, this.getY() - 0.25, this.getZ() - 0.25, this.getX() + 0.25, this.getY() + 0.25, this.getZ() + 0.25));
        this.setTool(shooter.getMainHandItem());
        this.startPos = shooter.getEyePosition();
        Vec3 look = shooter.getLookAngle().normalize();
        this.endPos = startPos.add(look.scale(range));
        this.penetrationCount = 3;
        if (!level.isClientSide) {
            hitEntities();
        }
//        if (level.isClientSide) {
//            spawnBeamParticles(startPos, endPos);
//        }
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
        } else {
            this.laserStack = ItemStack.EMPTY;
            this.toolStack = null;
            this.statsNBT = StatsNBT.EMPTY;
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

    public void setPenetrationCount(int count) {
        this.penetrationCount = count;
    }

    public int getPenetrationCount() {
        return penetrationCount;
    }

    public void setStartPos(Vec3 pos) {
        this.entityData.set(START_POS, new Vector3f((float) pos.x, (float) pos.y, (float) pos.z));
    }

    public Vec3 getStartPos() {
        Vector3f vec = this.entityData.get(START_POS);
        return new Vec3(vec.x(), vec.y(), vec.z());
    }

    public void setEndPos(Vec3 pos) {
        this.entityData.set(END_POS, new Vector3f((float) pos.x, (float) pos.y, (float) pos.z));
    }

    public Vec3 getEndPos() {
        Vector3f vec = this.entityData.get(END_POS);
        return new Vec3(vec.x(), vec.y(), vec.z());
    }


    public void hitEntities() {
        Vec3 start = getStartPos();
        Vec3 end = getEndPos();
        int penetrated = 0;
        int maxPenetration = getPenetrationCount();
        List<EntityHitResult> hits = EntityUtil.getEntityHitResults(
                level,
                shooter,
                getStartPos(),
                getEndPos(),
                e -> e != shooter && e.isAlive(),
                1.0
        );
        for (EntityHitResult hit : hits) {
            if (hit.getEntity() instanceof LivingEntity target) {
                AttackUtil.attackEntity(
                        toolStack,
                        shooter,
                        InteractionHand.MAIN_HAND,
                        target,
                        () -> 1,
                        false,
                        EquipmentSlot.MAINHAND,
                        true,
                        getDamage(),
                        false
                );
                penetrated++;
                if (penetrated >= maxPenetration) {
                    this.hasHit = true;
                    this.discard();
                    break;
                }
            }
        }
    }

    private void spawnBeamParticles(Vec3 start, Vec3 end) {
        double distance = start.distanceTo(end);
        Vec3 direction = end.subtract(start).normalize();

        double step = 0.1;
        int count = (int)(distance / step);

        for (int i = 0; i <= count; i++) {
            Vec3 pos = start.add(direction.scale(i * step));
            level().addParticle(
                    new DustParticleOptions(new Vector3f(1.0F, 0.0F, 0.0F), 2.0F),
                    pos.x, pos.y, pos.z,
                    (Math.random() - 0.5) * 0.1,
                    (Math.random() - 0.5) * 0.1,
                    (Math.random() - 0.5) * 0.1
            );
        }
    }


    @Override
    public void tick() {
        super.tick();
        if (this.shooter != null && !this.level.isClientSide) {
            this.startPos = shooter.getEyePosition();
            Vec3 look = shooter.getLookAngle().normalize();
            this.endPos = startPos.add(look.scale(range));

            setStartPos(this.startPos);
            setEndPos(this.endPos);

            if (!hasHit) {
                hitEntities();
            }
        }
//        if (level.isClientSide) {
//            spawnBeamParticles(getStartPos(), getEndPos());
//        }
        if (this.tickCount > lifeTime) {
            this.discard();
        }
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
    protected void defineSynchedData() {
        this.entityData.define(START_POS, new Vector3f(0, 0, 0));
        this.entityData.define(END_POS, new Vector3f(0, 0, 0));
    }

    public boolean hasHit() {
        return hasHit;
    }
}