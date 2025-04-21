package com.ssakura49.sakuratinker.content.entity;

import com.ssakura49.sakuratinker.register.STEntities;
import com.ssakura49.sakuratinker.utils.AttackUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

public class CelestialBladeProjectile extends Projectile implements IEntityAdditionalSpawnData {
    private float acceleration = 1.0F;
    private Vec3 originalDirection = Vec3.ZERO;
    private byte tickCount;
    private byte life = 20;
    private Vec3 originalPosition = Vec3.ZERO;
    private float curvature;
    private float radius;
    private float yVelocity;
    private float baseDamage = 5.0f;
    private Vec3 viewCenter;
    private float horizontalOffset;
    private boolean isReturning = false;
    private ToolStack tool;  // 匠魂工具
    private InteractionHand hand;  // 交互手
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

        this.tool = tool;
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
                if (tickCount % 5 == 0) {
                    checkHitEntities((Player)owner);
                }
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
            // 返回阶段简化运动计算
            return calculateDirectionVector(this.getYRot())
                    .scale(this.curvature * 0.5f) // 减小曲线影响
                    .add(0, yVelocity * 0.8f, 0); // 减小垂直速度
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
            target.invulnerableTime = 0;  // 防止无敌帧影响多次命中

            if (tool != null && !tool.isBroken()) {
                // 使用 AttackUtil 攻击
                AttackUtil.attackEntity(
                        tool,                   // 匠魂工具
                        owner,                 // 攻击者
                        hand,                  // 使用的 InteractionHand
                        target,                // 目标
                        () -> 1.0f,            // 冷却时间
                        false,                 // 是否额外攻击
                        slot,                  // 装备槽位
                        true,                  // 是否固定伤害
                        baseDamage,            // 伤害值
                        false                  // 是否不消耗耐久
                );
//                System.out.println("攻击伤害: " + baseDamage);
            } else {
                // 后备逻辑：如果没有工具，使用原版伤害
                target.hurt(
                        this.damageSources().playerAttack(owner),
                        baseDamage
                );
            }
        });
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
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
    public void readSpawnData(FriendlyByteBuf additionalData) {
        acceleration = additionalData.readFloat();
        life = additionalData.readByte();
        curvature = additionalData.readFloat();
        tickCount = additionalData.readByte();
        yVelocity = additionalData.readFloat();
        originalDirection = new Vec3(
                additionalData.readDouble(),
                additionalData.readDouble(),
                additionalData.readDouble()
        );
        updateRadius();
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void defineSynchedData() {}
}
