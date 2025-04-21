package com.ssakura49.sakuratinker.content.entity;

import com.ssakura49.sakuratinker.content.entity.base.VisualScaledProjectile;
import com.ssakura49.sakuratinker.register.STEntities;
import com.ssakura49.sakuratinker.utils.AttackUtil;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class GhostKnife extends VisualScaledProjectile {
    public static final EntityDataAccessor<Float> DATA_LENGTH = SynchedEntityData.defineId(GhostKnife.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> DATA_RENDER = SynchedEntityData.defineId(GhostKnife.class, EntityDataSerializers.BOOLEAN);

    private ToolStack tool;  // 匠魂工具
    private EquipmentSlot slot;  // 装备槽位（主手/副手）
    private InteractionHand hand;

    // 生命周期常量
    public static final int LIFETIME = 100; // 5秒生命周期 (20 ticks/秒)
    private int age = 0;



    // 吸血回调接口
    public interface HitCallback {
        void onHit(LivingEntity attacker, LivingEntity target, float damageDealt);
    }
    private HitCallback hitCallback;

    public boolean offHand;

    public float getLifetime() {
        return LIFETIME;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_LENGTH, 0f);
        this.entityData.define(DATA_RENDER, false);
    }

    public boolean readyToRender() {
        return this.entityData.get(DATA_RENDER);
    }

    public void setDataLength(float amount) {
        this.entityData.set(DATA_LENGTH, amount);
    }

    public float getDataLength() {
        return this.entityData.get(DATA_LENGTH);
    }

    public void setOnHitCallback(HitCallback callback) {
        this.hitCallback = callback;
    }

    public GhostKnife(EntityType<? extends VisualScaledProjectile> entityType, Level level, float scale) {
        super(entityType, level);
        this.setScale(scale);
    }

    public GhostKnife(EntityType<? extends VisualScaledProjectile> entityType, Level level) {
        this(entityType, level, 1f);
    }

    public GhostKnife(Level level, float scale, ToolStack tool, InteractionHand hand) {
        this(STEntities.GHOST_KNIFE.get(), level, scale);
        this.tool = tool;
        this.hand = hand;
        this.slot = hand == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
    }


    @Override
    public void tick() {
        super.tick();

        // 更新旋转角度以匹配运动方向
        Vec3 movement = this.getDeltaMovement();
        if (movement.lengthSqr() > 0.001) {
            double horizontalDistance = movement.horizontalDistance();
            this.setYRot((float)(Mth.atan2(movement.x, movement.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(movement.y, horizontalDistance) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }

        if (!this.level().isClientSide) {
            this.age++;

            // 生命周期结束后消失
            if (this.age >= LIFETIME) {
                this.discard();
                return;
            }

            // 每tick执行碰撞检测
            performCollisionDetection();

            // 持续移动
            this.setPos(this.getX() + movement.x, this.getY() + movement.y, this.getZ() + movement.z);
        }
    }

    private void performCollisionDetection() {
//        System.out.println("DEBUG: 执行碰撞检测"); // 调试输出
        Vec3 startPos = this.position();
        Vec3 endPos = startPos.add(this.getDeltaMovement());

        AABB aabb = this.getBoundingBox().inflate(0.5).expandTowards(this.getDeltaMovement());
        List<Entity> entities = this.level().getEntities(this, aabb, e ->
                e != this.getOwner() && e instanceof LivingEntity && !(e instanceof Player)
        );

//        System.out.println("DEBUG: 检测到实体数量: " + entities.size()); // 调试输出
        for (Entity entity : entities) {
//            System.out.println("DEBUG: 处理实体: " + entity.getName().getString()); // 调试输出
            if (entity instanceof LivingEntity living) {
                handleCollision(new EntityHitResult(living));
                return;
            }
        }

        HitResult blockHit = this.level().clip(new ClipContext(
                startPos, endPos,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                this
        ));

        if (blockHit.getType() != HitResult.Type.MISS) {
//            System.out.println("DEBUG: 碰撞到方块，销毁实体"); // 调试输出
            this.discard();
        }
    }

    private void handleCollision(EntityHitResult hitResult) {
//        System.out.println("DEBUG: 进入 handleCollision 方法"); // 调试输出
        LivingEntity target = (LivingEntity) hitResult.getEntity();
        Entity owner = this.getOwner();

        if (owner instanceof Player player) {
//            System.out.println("DEBUG: 攻击者是玩家: " + player.getName().getString()); // 调试输出
            if (tool != null && !tool.isBroken()) {
//                System.out.println("DEBUG: 使用匠魂工具攻击，伤害值: " + baseDamage); // 调试输出
                boolean success = AttackUtil.attackEntity(
                        tool,
                        player,
                        hand,
                        target,
                        () -> 1.0f,
                        false,
                        slot,
                        true,
                        baseDamage,
                        false
                );
//                System.out.println("DEBUG: 攻击结果: " + (success ? "成功" : "失败")); // 调试输出

                if (success && hitCallback != null) {
                    float damageDealt = Math.min(baseDamage, target.getHealth());
                    hitCallback.onHit(player, target, damageDealt);
                    spawnHealParticles(target);
                }
            } else {
//                System.out.println("DEBUG: 使用原版伤害逻辑"); // 调试输出
                target.hurt(
                        this.damageSources().indirectMagic(this, player),
                        baseDamage
                );
            }
        }

        this.discard();
    }

    private void spawnHealParticles(LivingEntity target) {
        if (level() instanceof ServerLevel serverLevel) {
            Vec3 pos = target.position().add(0, target.getBbHeight() / 2, 0);
            serverLevel.sendParticles(ParticleTypes.HEART,
                    pos.x, pos.y, pos.z, 5, 0.3, 0.3, 0.3, 0.2);
        }
    }

    private void spawnTrailEffect(Vec3 initialPos, Vec3 path, Player player, float scale) {
        Vec3 offset = player.getLookAngle().cross(new Vec3(0, 1, 0)).normalize().scale(0.6f);
        if (offHand) offset = offset.reverse();

        this.setDeltaMovement(path.subtract(offset).normalize());
        this.setPos(initialPos.add(offset));
        this.entityData.set(DATA_RENDER, true);
    }
}
