package com.ssakura49.sakuratinker.common.entity;

import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.register.STEntities;
import com.ssakura49.sakuratinker.register.STItems;
import com.ssakura49.sakuratinker.utils.tinker.AttackUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class CelestialBladeProjectile extends Projectile implements IEntityAdditionalSpawnData, IProjectileCritical, IProjectileDamage, ItemSupplier {

    public static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(CelestialBladeProjectile.class, EntityDataSerializers.ITEM_STACK);

    private float acceleration = 1.0F;
    private Vec3 originalDirection = Vec3.ZERO;
    private byte tickCount;
    private byte life = 20;
    private Vec3 originalPosition = Vec3.ZERO;
    private float curvature;
    private float radius;
    private float yVelocity;
    private float baseDamage = 5.0f;
    private boolean critical = false;
    private Vec3 viewCenter;
    private float horizontalOffset;
    private final boolean isReturning = false;
    private ItemStack itemStack;
    private ToolStack toolStack;
    private StatsNBT stats;
    private InteractionHand hand;
    private EquipmentSlot slot;

    public byte getLife() {
        return this.life;
    }

    public byte getTickCount() {
        return this.tickCount;
    }

    public float getCurvature() {
        return this.curvature;
    }

    public CelestialBladeProjectile(EntityType<? extends CelestialBladeProjectile> type, Level level) {
        super(type, level);
        this.curvature = 0.5F + this.random.nextFloat() * 2.0F;
        updateRadius();
        this.noPhysics = true;
    }

    public CelestialBladeProjectile(Level level, Player player, float baseDamage, ToolStack tool, InteractionHand hand) {
        this(STEntities.CELESTIAL_BLADE.get(), level);
        this.baseDamage = baseDamage;
        this.itemStack = ItemStack.EMPTY;
        this.toolStack = tool;
        this.hand = hand;
        this.slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;

        this.setOwner(player);
        this.setPos(player.getX(), player.getY() + 0.8D, player.getZ());
        this.originalDirection = calculateDirectionVector(player.getYRot());
        this.originalPosition = this.position();
        this.setYRot(player.getYRot() + 90.0F);
        this.yRotO = this.getYRot();

        this.viewCenter = player.getLookAngle();
        this.horizontalOffset = 1.0f;

        this.acceleration = (float)(20.0 * Math.cos(Math.toRadians(player.getXRot())) / this.radius);
        this.yVelocity = (float)(-Math.sin(Math.toRadians(player.getXRot())) * 4.0F);
        this.xRotO = player.getXRot();
    }

    public void setTool(ItemStack tool) {
        if (!tool.isEmpty()) {
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

    private void updateRadius() {
        this.radius = (float)((this.life * this.curvature) / Math.PI / 2.0f);
    }

    private Vec3 calculateDirectionVector(float yaw) {
        return new Vec3(
                -Mth.sin(yaw * Mth.DEG_TO_RAD),
                0.0D,
                Mth.cos(yaw * Mth.DEG_TO_RAD)
        ).normalize();
    }

    public void setItem(ItemStack pStack) {
        if (!pStack.is(this.getDefaultItem()) || pStack.hasTag()) {
            this.getEntityData().set(DATA_ITEM_STACK, pStack.copyWithCount(1));
        }

    }

    public Item getDefaultItem() {
        return STItems.yoyo.get();
    };

    public ItemStack getItemRaw() {
        return (ItemStack)this.getEntityData().get(DATA_ITEM_STACK);
    }

    @Override
    public @NotNull ItemStack getItem() {
        ItemStack stack = this.getItemRaw();
        return stack.isEmpty() ? new ItemStack(this.getDefaultItem()) : stack;
    }

    @Override
    public void tick() {
        super.tick();

        if (tickCount == life / 2) {
            this.horizontalOffset = 2.5f;
            this.yVelocity *= -1.0F;
            this.acceleration *= 1.2F;
        }

        if (!level().isClientSide) {
            Entity owner = this.getOwner();
            if (owner instanceof Player) {
//                if (tickCount % 5 == 0) {
                    checkHitEntities((Player)owner);
//                }
            }

            if (tickCount < life) {
                Vec3 movement = calculateMovement();

                if (isReturning && getOwner() != null) {
                    Vec3 targetPos = getOwner().position().add(0, 0.8D, 0)
                            .add(calculateDirectionVector(getOwner().getYRot()).scale(-1.0D));
                    Vec3 toOwner = targetPos.subtract(this.position());
                    if (toOwner.length() > 80.0D) {
                        discard();
                        return;
                    }
                    double returnSpeed = Math.min(0.5, toOwner.length() / (life - tickCount));
                    Vec3 returnDirection = toOwner.normalize().scale(returnSpeed);
                    movement = movement.add(returnDirection);
                }

                this.setDeltaMovement(movement);
                this.move(MoverType.SELF, movement);
                this.setYRot(getYRot() - 360.0F / life);
            } else if (tickCount > life + 1) {
                discard();
            }
        }
        tickCount++;
    }

    private Vec3 calculateMovement() {
        if (isReturning) {
            return calculateDirectionVector(this.getYRot())
                    .scale(this.curvature * 0.5f)
                    .add(0, yVelocity * 0.8f, 0);
        } else {
            return calculateDirectionVector(this.getYRot())
                    .scale(this.curvature)
                    .add(originalDirection.scale(
                            Math.sin(Math.toRadians(360.0F / life * tickCount)) *
                                    (acceleration - 1.0F) * curvature))
                    .add(0, yVelocity, 0);
        }
    }

    private void checkHitEntities(Player owner) {
        AABB aabb = new AABB(this.position(), this.position()).inflate(2.5);

        level().getEntities(this, aabb, e ->
                e instanceof LivingEntity && e != owner && e.isAlive()
        ).forEach(e -> {
            LivingEntity target = (LivingEntity) e;
            target.invulnerableTime = 0;

            if (toolStack != null && !toolStack.isBroken()) {
                AttackUtil.attackEntity(
                        toolStack,
                        owner,
                        hand,
                        target,
                        () -> 1.0f,
                        false,
                        slot,
                        false,
                        baseDamage,
                        false
                );
            } else {
                target.hurt(
                        this.damageSources().playerAttack(owner),
                        baseDamage
                );
            }
        });
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        ItemStack $$1 = this.getItemRaw();
        if (!$$1.isEmpty()) {
            tag.put("Item", $$1.save(new CompoundTag()));
        }
        tag.put("Tool", itemStack.serializeNBT());
        tag.putBoolean("Critical", critical);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        ItemStack item = ItemStack.of(tag.getCompound("Item"));
        this.setItem(item);
        this.setTool(ItemStack.of(tag.getCompound("Tool")));
        this.critical = tag.getBoolean("Critical");
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItem(this.itemStack);
        buffer.writeFloat(acceleration);
        buffer.writeByte(life);
        buffer.writeFloat(curvature);
        buffer.writeByte(tickCount);
        buffer.writeFloat(yVelocity);
        buffer.writeDouble(originalDirection.x());
        buffer.writeDouble(originalDirection.y());
        buffer.writeDouble(originalDirection.z());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        this.setTool(buffer.readItem());
        this.acceleration = buffer.readFloat();
        this.life = buffer.readByte();
        this.curvature = buffer.readFloat();
        this.tickCount = buffer.readByte();
        this.yVelocity = buffer.readFloat();
        this.originalDirection = new Vec3(
                buffer.readDouble(),
                buffer.readDouble(),
                buffer.readDouble()
        );
        updateRadius();
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
    }

    public Vec3 getViewCenter() {
        return viewCenter;
    }

    public Vec3 getOriginalPosition() {
        return originalPosition;
    }

    public void setCritical(boolean critical) {
        this.critical = critical;
    }
    public boolean getCritical() {
        return this.critical;
    }
    public void setDamage(float damage) {
        this.baseDamage = damage;
    }
    public float getDamage() {
        return this.stats.get(ToolStats.ATTACK_DAMAGE) + this.baseDamage + 0.5F;
    }
}
