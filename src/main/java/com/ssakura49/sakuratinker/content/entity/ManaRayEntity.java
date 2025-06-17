package com.ssakura49.sakuratinker.content.entity;

import com.ssakura49.sakuratinker.client.fx.WispParticleData;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.register.STEntities;
import com.ssakura49.sakuratinker.utils.ProjectileUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import javax.annotation.Nonnull;
import java.util.List;

public class ManaRayEntity extends ThrowableProjectile implements IEntityAdditionalSpawnData, IProjectileCritical, IProjectileDamage {

    private static final int LIVE_TICKS = 100;
    private static final EntityDataAccessor<Integer> MANA = SynchedEntityData.defineId(ManaRayEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> START_MANA = SynchedEntityData.defineId(ManaRayEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<ItemStack> SOURCE_LENS = SynchedEntityData.defineId(ManaRayEntity.class, EntityDataSerializers.ITEM_STACK);
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(ManaRayEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> GRAVITY = SynchedEntityData.defineId(ManaRayEntity.class, EntityDataSerializers.FLOAT);


    private boolean fullManaLastTick = true;
    private ItemStack itemStack;
    private ToolStack toolStack;
    private StatsNBT stats;
    private float bonusDamage = 0.0F;
    private boolean critical = false;

    public ManaRayEntity(EntityType<? extends ManaRayEntity> type, Level world) {
        super(type, world);
        this.itemStack =ItemStack.EMPTY;
        this.toolStack = null;
        this.stats = StatsNBT.EMPTY;
    }

    public ManaRayEntity(Level world) {
        super(STEntities.MANA_RAY.get(), world);
    }

    public ManaRayEntity(Level world, LivingEntity thrower) {
        super(STEntities.MANA_RAY.get(), thrower, world);
    }

    public ManaRayEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        super(STEntities.MANA_RAY.get(),level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(MANA, 0);
        this.entityData.define(GRAVITY, 0.0F);
        this.entityData.define(START_MANA, 0);
        this.entityData.define(SOURCE_LENS, ItemStack.EMPTY);
        this.entityData.define(COLOR, 0x55FFFF);
    }

    public void setTool(ItemStack tool) {
        if (tool != null) {
            this.itemStack = tool.copy();
            this.toolStack = ToolStack.from(tool);
            this.stats = this.toolStack.getStats();
        } else {
            this.itemStack = ItemStack.EMPTY;
            this.toolStack = null;
            this.stats = StatsNBT.EMPTY;
        }
    }

    public ToolStack getTool() {
        return this.toolStack;
    }

    public int getColor() {
        return this.entityData.get(COLOR);
    }

    public void setColor(int color) {
        this.entityData.set(COLOR, color);
    }

    public float getParticleSize() {
        return (float)this.getMana() / (float)this.getStartingMana();
    }

    public void setGravity(float gravity) {
        this.entityData.set(GRAVITY, gravity);
    }

    public float getGravity() {
        return this.entityData.get(GRAVITY);
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
        return this.toolStack != null ?
                this.toolStack.getStats().get(ToolStats.ATTACK_DAMAGE) + this.bonusDamage :
                this.bonusDamage;
    }

    public int getMana() {
        return this.entityData.get(MANA);
    }

    public void setMana(int mana) {
        this.entityData.set(MANA, mana);
    }

    public int getStartingMana() {
        return this.entityData.get(START_MANA);
    }

    public void setStartingMana(int mana) {
        this.entityData.set(START_MANA, mana);
    }

    @Override
    public void shoot(double x, double y, double z, float velocity, float inaccuracy) {
        super.shoot(x, y, z, velocity, inaccuracy);
    }

    @Override
    public void tick() {
        super.tick();

        if (this.tickCount >= LIVE_TICKS) {
            this.discard();
            return;
        }
        if (!this.isNoGravity()) {
            Vec3 motion = this.getDeltaMovement();
            this.setDeltaMovement(motion.x, motion.y - this.getGravity(), motion.z);
        }
        if (!this.level().isClientSide) {
            this.fullManaLastTick = this.getMana() == this.getStartingMana();
        } else {
            this.particles();
        }
        if (!this.level().isClientSide) {
            if (this.tickCount % 5 == 0) {
                this.setMana(this.getMana() - 1);
                if (this.getMana() <= 0) {
                    this.discard();
                    return;
                }
            }
            Vec3 from = this.position();
            Vec3 to = from.add(this.getDeltaMovement());
            EntityHitResult hit = ProjectileUtil.getEntityHitResult(this.level(), this, from, to, this.getBoundingBox().expandTowards(this.getDeltaMovement()), this::isValidTarget);
            if (hit != null) {
                handleEntityCollision((LivingEntity) hit.getEntity());
            }
            checkEntityCollisions();
        }
    }

    private boolean isValidTarget(Entity entity) {
        if (!(entity instanceof LivingEntity)) return false;
        if (entity == this.getOwner()) return false;
        return entity.isAlive();
    }

    private void particles() {
        if (!level().isClientSide) return;

        Vec3 motion = getDeltaMovement();
        float speed = (float) motion.length();

        int color = getColor();
        float r = ((color >> 16) & 0xFF) / 255f;
        float g = ((color >> 8) & 0xFF) / 255f;
        float b = (color & 0xFF) / 255f;

        for (int i = 0; i < 4; i++) {
            float t = i / 4f;
            double px = getX() - motion.x * t;
            double py = getY() - motion.y * t;
            double pz = getZ() - motion.z * t;

            level().addParticle(
                    WispParticleData.wisp(
                            0.5f,  // 尺寸
                            r, g, b,    // 颜色
                            1.5f,       // 生命周期倍率
                            false
                    ),
                    px, py, pz,
                    0.0, 0.0, 0.0
            );
        }

        // 绘制头部粒子（更亮、略大）
        level().addParticle(
                WispParticleData.wisp(
                        0.5f,       // 尺寸
                        1.0f, 1.0f, 1.0f,  // 纯白发光头部
                        1.2f,        // 生命周期倍率
                        false
                ),
                getX(), getY(), getZ(),
                motion.x * 0.01, motion.y * 0.01, motion.z * 0.01
        );
    }

    private void checkEntityCollisions() {
        AABB axis = new AABB(this.getX(), this.getY(), this.getZ(),
                this.xOld, this.yOld, this.zOld)
                .inflate(1.5);

        List<LivingEntity> entities = this.level().getEntitiesOfClass(
                LivingEntity.class,
                axis,
                e -> isValidTarget(e, this.getOwner())
        );

        for (LivingEntity target : entities) {
            handleEntityCollision(target);
            break;
        }
    }

    private boolean isValidTarget(LivingEntity target, Entity owner) {
        if (target == owner || target.hurtTime > 0) {
            return false;
        }

        if (target instanceof Player targetPlayer &&
                owner instanceof Player ownerPlayer) {
            return ownerPlayer.canHarmPlayer(targetPlayer);
        }

        return true;
    }

    private void handleEntityCollision(LivingEntity target) {
        if (this.getOwner() instanceof LivingEntity attacker) {
            if (this.toolStack != null) {
                ProjectileUtils.attackEntity(
                        this.itemStack.getItem(),
                        this,
                        this.toolStack,
                        attacker,
                        target,
                        false
                );
            } else {
                target.hurt(target.damageSources().magic(), this.getDamage());
            }
            this.discard();
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide && result.getEntity() instanceof LivingEntity target) {
            handleEntityCollision(target);
        }
    }

    @Override
    protected void onHit(HitResult result) {
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Tool", this.itemStack.serializeNBT());
        tag.putInt("Mana", this.getMana());
        tag.putInt("StartingMana", this.getStartingMana());
        tag.putFloat("BonusDamage", this.bonusDamage);
        tag.putBoolean("Critical", this.critical);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setTool(ItemStack.of(tag.getCompound("Tool")));
        this.setMana(tag.getInt("Mana"));
        this.setStartingMana(tag.getInt("StartingMana"));
        this.bonusDamage = tag.getFloat("BonusDamage");
        this.critical = tag.getBoolean("Critical");
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItem(this.itemStack);
        buffer.writeInt(this.getMana());
        buffer.writeInt(this.getStartingMana());
        buffer.writeFloat(this.bonusDamage);
        buffer.writeBoolean(this.critical);
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.setTool(buffer.readItem());
        this.setMana(buffer.readInt());
        this.setStartingMana(buffer.readInt());
        this.bonusDamage = buffer.readFloat();
        this.critical = buffer.readBoolean();
    }

    @Nonnull
    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    public boolean isFullManaLastTick() {
        return fullManaLastTick;
    }
}
