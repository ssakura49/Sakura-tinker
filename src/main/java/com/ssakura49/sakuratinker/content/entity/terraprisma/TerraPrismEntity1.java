/*
package com.ssakura49.sakuratinker.content.entity.terraprisma;

import com.ssakura49.sakuratinker.register.STEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

import javax.annotation.Nullable;
import java.util.*;

public class TerraPrismEntity1 extends PathfinderMob implements OwnableEntity {
    // 跟随相关参数
//    private static final float FOLLOW_RANGE = 3.0f;
    private static final float MIN_FOLLOW_DISTANCE = 1.0f;
    private static final float MAX_FOLLOW_DISTANCE = 2.5f; // 现在会在跟随目标中使用

    // 战斗相关参数
    private static final float ATTACK_RANGE = 5.0f;
    private static final float DETECTION_RANGE = 10.0f;
    private static final float CHARGE_SPEED = 1.8f;
    private static final int ATTACK_COOLDOWN = 20;
    // 飞行控制参数
    private static final float BASE_FLY_SPEED = 0.6f;
    private static final float FLYING_HEIGHT_VARIATION = 0.5f;


    private LivingEntity target;
    private int attackCooldown;

    // 位置历史相关
    private static final int POSITION_HISTORY_LENGTH = 10;
    private static final int POSITION_UPDATE_INTERVAL = 2;
    private final Vec3[] positionHistory = new Vec3[POSITION_HISTORY_LENGTH];
    private int positionUpdateTimer = 0;
    private UUID ownerUUID;
    // 动画相关
    private String animationState = "idle";
    private int animationTimer = 0;

    // 棱镜排列相关
    private int prismIndex = 0;
    private int totalPrisms = 1;

    protected static final EntityDataAccessor<Optional<UUID>> DATA_OWNERUUID_ID= SynchedEntityData.defineId(TerraPrismEntity1.class, EntityDataSerializers.OPTIONAL_UUID);;

    public TerraPrismEntity1(EntityType<? extends PathfinderMob> type, Level level) {
        super(type, level);
        this.noCulling = true;
        this.navigation = new FlyingPathNavigation(this, level);
        this.getBrain().setMemory(STMemoryModules.PRISM_INDEX.get(), 0); // 默认索引
        this.getBrain().setMemory(STMemoryModules.TOTAL_PRISMS.get(), 1); // 默认总
    }

    public TerraPrismEntity1(PlayMessages.SpawnEntity spawnEntity, Level level) {
        super(STEntities.TERRA_PRISMA.get(),level);
    }

    public int getPrismIndex() {
        return this.prismIndex;
    }

    public void setPrismIndex(int index) {
        this.prismIndex = index;
    }

    public int getTotalPrisms() {
        return this.totalPrisms;
    }

    public void setTotalPrisms(int total) {
        this.totalPrisms = total;
    }

    @Override
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

//    @Override
//    protected Brain.Provider<?> brainProvider() {
//        return new Brain.Provider<>(TerraPrismAI.MEMORY_TYPES, TerraPrismAI.SENSOR_TYPES);
//    }

//    @Override
//    protected Brain<?> makeBrain(Dynamic<?> dyn) {
//        return TerraPrismAI.makeBrain(this, dyn);
//    }
//
//    @SuppressWarnings("unchecked")
//    @Override
//    public Brain<TerraPrismEntity> getBrain() {
//        return (Brain<TerraPrismEntity>) super.getBrain();
//    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        // 追击优先级更高
        this.goalSelector.addGoal(1, new PrismChaseGoal(this, 1.2F));
        this.goalSelector.addGoal(2, new PrismChargeAttackGoal(this));

        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new PrismCopyOwnerTargetGoal(this,true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(
                this, LivingEntity.class, 10, true, false,
                e -> e instanceof Enemy
        ));
    }
//    @Override
//    protected void registerGoals() {
//        this.goalSelector.addGoal(0, new FloatGoal(this));
////        this.goalSelector.addGoal(1, new TerraPrismAttackGoal(this, 1.2));
////        this.goalSelector.addGoal(2, new TerraPrismFollowGoal(this, 1.0, MIN_FOLLOW_DISTANCE, MAX_FOLLOW_DISTANCE));
//        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
//
//        this.targetSelector.addGoal(1, new STOwnerTargetGoal.OwnerHurtByTargetGoal(this));
//        this.targetSelector.addGoal(2, new STOwnerTargetGoal.OwnerHurtTargetGoal(this));
//        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(
//                this,
//                LivingEntity.class,
//                10,
//                true,
//                false,
//                entity -> entity instanceof Enemy && !entity.isAlliedTo(this)
//        ));
//        this.goalSelector.getAvailableGoals().removeIf(goal -> goal.getGoal() instanceof BreedGoal||
//                goal.getGoal() instanceof FollowParentGoal);
//    }

    public static AttributeSupplier.Builder createTerraPrismaAttributes() {
        return FlyingMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.FLYING_SPEED, BASE_FLY_SPEED)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    @Override
    public void tick() {
        super.tick();
        updatePositionHistory();
        if (!level().isClientSide && tickCount % 10 == 0) {
            if (getOwner() instanceof Mob owner) {
                LivingEntity ownerTarget = owner.getTarget();
                if (ownerTarget != null && !ownerTarget.equals(getTarget())) {
                    setTarget(ownerTarget);
                }
            }
        }
        if (!level().isClientSide) {
            if (attackCooldown > 0) attackCooldown--;

            if (getOwner() instanceof Player) {
                updateTargeting();
                performAttacks();
            }
        }
    }

    // ========== 飞行行为控制 ==========
    @Override
    public void travel(Vec3 travelVec) {
        if (this.isControlledByLocalInstance()) {
            // 完全由Navigation控制移动，不应用任何物理
            this.setDeltaMovement(Vec3.ZERO);
            this.calculateEntityAnimation(false);
        }
    }

    @Override
    public boolean isNoGravity() {
        return true;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        if (this.getOwnerUUID() != null) {
            pCompound.putUUID("Owner", this.getOwnerUUID());
        }
    }
    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        UUID uuid;
        if (pCompound.hasUUID("Owner")) {
            uuid = pCompound.getUUID("Owner");
        } else {
            String s = pCompound.getString("Owner");
            uuid = OldUsersConverter.convertMobOwnerIfNecessary(Objects.requireNonNull(this.getServer()), s);
        }

        if (uuid != null) {
            try {
                this.setOwnerUUID(uuid);
            } catch (Throwable ignored) {
            }
        }
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    public void setOwnerUUID(@Nullable UUID pUuid) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(pUuid));
    }


    private void updatePositionHistory() {
        if (positionUpdateTimer-- <= 0) {
            positionUpdateTimer = 2;
            System.arraycopy(positionHistory, 0, positionHistory, 1, positionHistory.length - 1);
            positionHistory[0] = this.position();
        }
    }

//    private void updateTargeting() {
//        // 检查是否需要寻找新目标
//        if (target == null || !target.isAlive() || distanceToSqr(target) > DETECTION_RANGE * DETECTION_RANGE) {
//            findNewTarget();
//        }
//    }
private void updateTargeting() {
    // 从 Brain 获取当前目标
    LivingEntity currentTarget = getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);

    // 如果目标无效或超出范围，寻找新目标
    if (currentTarget == null || !currentTarget.isAlive() ||
            distanceToSqr(currentTarget) > DETECTION_RANGE * DETECTION_RANGE) {

        // 手动检测攻击目标
        List<LivingEntity> targets = level().getEntitiesOfClass(
                LivingEntity.class,
                new AABB(getX() - 10, getY() - 10, getZ() - 10,
                        getX() + 10, getY() + 10, getZ() + 10),
                entity -> entity instanceof Enemy && !entity.isAlliedTo(this)
        );

        if (!targets.isEmpty()) {
            LivingEntity newTarget = targets.get(random.nextInt(targets.size()));
            getBrain().setMemory(MemoryModuleType.ATTACK_TARGET, newTarget);
            System.out.println("新目标锁定: " + newTarget.getName()); // 调试输出
        }
    }
}

    private List<LivingEntity> findNewTarget() {
        AABB detectionArea = new AABB(
                getX() - DETECTION_RANGE, getY() - DETECTION_RANGE, getZ() - DETECTION_RANGE,
                getX() + DETECTION_RANGE, getY() + DETECTION_RANGE, getZ() + DETECTION_RANGE
        );

        List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, detectionArea,
                entity -> entity instanceof Enemy && !entity.isAlliedTo(this) && canAttack(entity));

        target = targets.isEmpty() ? null : targets.get(random.nextInt(targets.size()));
        return targets;
    }

    private void performAttacks() {
        if (target == null && getOwner() instanceof Mob owner) {
            target = owner.getTarget(); // 再次尝试获取主人目标
        }
        getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(target -> {
            if (attackCooldown <= 0 && distanceToSqr(target) <= ATTACK_RANGE * ATTACK_RANGE) {
                performRandomAttack();
                attackCooldown = ATTACK_COOLDOWN;
            }
        });
    }

    public Vec3 getSmoothedPosition(float partialTicks) {
        if (positionHistory[positionHistory.length - 1] == null) {
            return this.position();
        }

        Vec3 result = Vec3.ZERO;
        double totalWeight = 0;
        for (int i = 0; i < positionHistory.length; i++) {
            if (positionHistory[i] != null) {
                double weight = 1.0 / (i + 1);
                result = result.add(positionHistory[i].scale(weight));
                totalWeight += weight;
            }
        }
        return totalWeight > 0 ? result.scale(1.0 / totalWeight) : this.position();
    }


    private void performRandomAttack() {
        if (target == null) return;

        // 随机选择攻击方式
        switch (random.nextInt(3)) {
            case 0 -> performSlashAttack();
            case 1 -> performSpinAttack();
            case 2 -> performChargeAttack();
        }

        attackCooldown = ATTACK_COOLDOWN;
    }

    private void performSlashAttack() {
        doHurtTarget(target);
        level().broadcastEntityEvent(this, (byte) 4);
    }

    private void performSpinAttack() {
        level().getEntitiesOfClass(LivingEntity.class, getBoundingBox().inflate(2.0),
                        entity -> entity instanceof Enemy && !entity.isAlliedTo(this))
                .forEach(this::doHurtTarget);
        level().broadcastEntityEvent(this, (byte) 5);
    }

    private void performChargeAttack() {
        Vec3 toTarget = target.position().subtract(this.position());
        if (toTarget.length() > 0) {
            Vec3 direction = toTarget.normalize();
            // 飞行冲刺时略微抬高轨迹
            Vec3 chargeVec = direction.scale(CHARGE_SPEED).add(0, 0.3, 0);
            this.setDeltaMovement(chargeVec);
        }
        level().broadcastEntityEvent(this, (byte)6);
    }

    @Override
    public boolean isWithinMeleeAttackRange(LivingEntity target) {
        double reach = this.getBbWidth() * 2.0F + target.getBbWidth();
        return this.distanceToSqr(target) <= reach * reach;
    }

    @Override
    public void handleEntityEvent(byte id) {
        switch (id) {
            case 4 -> handleSlashAnimation();
            case 5 -> handleSpinAnimation();
            case 6 -> handleChargeAnimation();
            default -> super.handleEntityEvent(id);
        }
    }


    private void handleSlashAnimation() {
        // 挥砍动画效果
        if (level().isClientSide) {
            // 1. 粒子效果 - 弧形刀光
            for (int i = 0; i < 8; i++) {
                double angle = i * Math.PI / 4;
                Vec3 particlePos = position()
                        .add(0, 1.2, 0)
                        .add(new Vec3(Math.cos(angle) * 1.5, 0, Math.sin(angle) * 1.5));

                level().addParticle(ParticleTypes.SWEEP_ATTACK,
                        particlePos.x, particlePos.y, particlePos.z,
                        0, 0, 0);
            }

            // 2. 播放音效
            playSound(SoundEvents.PLAYER_ATTACK_SWEEP, 1.0F, 1.0F);

            // 3. 模型动画触发 (需要与渲染器配合)
            animationState = "slash";
            animationTimer = 10;
        }
    }

    private void handleSpinAnimation() {
        // 旋转动画效果
        if (level().isClientSide) {
            // 1. 圆形粒子效果
            for (int i = 0; i < 16; i++) {
                double angle = i * Math.PI * 2 / 16;
                Vec3 particlePos = position()
                        .add(0, 0.8, 0)
                        .add(new Vec3(Math.cos(angle) * 2.0, 0, Math.sin(angle) * 2.0));

                level().addParticle(ParticleTypes.CRIT,
                        particlePos.x, particlePos.y, particlePos.z,
                        Math.cos(angle) * 0.2, 0.1, Math.sin(angle) * 0.2);
            }

            // 2. 播放音效
            playSound(SoundEvents.PLAYER_ATTACK_KNOCKBACK, 0.8F, 1.2F);

            // 3. 模型动画触发
            animationState = "spin";
            animationTimer = 15;
        }
    }

    private void handleChargeAnimation() {
        // 冲刺动画效果
        if (level().isClientSide) {
            // 1. 冲刺轨迹粒子
            Vec3 motion = getDeltaMovement();
            for (int i = 0; i < 5; i++) {
                Vec3 particlePos = position()
                        .add(0, 0.5, 0)
                        .add(motion.scale(i * -0.2));

                level().addParticle(ParticleTypes.ELECTRIC_SPARK,
                        particlePos.x, particlePos.y, particlePos.z,
                        motion.x * 0.1, motion.y * 0.1, motion.z * 0.1);
            }

            // 2. 音效
            playSound(SoundEvents.TRIDENT_RIPTIDE_3, 0.7F, 1.5F);

            // 3. 模型动画触发
            animationState = "charge";
            animationTimer = 8;
        }
    }

    public String getAnimationState() {
        return animationTimer > 0 ? animationState : "idle";
    }

    public float getAnimationProgress(float partialTicks) {
        return (animationTimer - partialTicks) /
                (animationState.equals("spin") ? 15f : 10f);
    }

    private class PrismCopyOwnerTargetGoal extends TargetGoal {
        private final TargetingConditions copyConditions =
                TargetingConditions.forCombat()
                        .ignoreLineOfSight()
                        .ignoreInvisibilityTesting();

        public PrismCopyOwnerTargetGoal(Mob pMob, boolean pMustSee) {
            super(pMob, pMustSee);
        }

        @Override
        public boolean canUse() {
            if (getOwner() instanceof Mob owner) {
                LivingEntity ownerTarget = owner.getTarget();
                // 主人有目标 且 目标可攻击 且 不是棱镜自身
                return ownerTarget != null
                        && ownerTarget.isAlive()
                        && this.canAttack(ownerTarget, copyConditions)
                        && !ownerTarget.equals(TerraPrismEntity1.this);
            }
            return false;
        }

        @Override
        public void start() {
            if (getOwner() instanceof Mob owner) {
                setTarget(owner.getTarget()); // 强制同步目标
                System.out.println("同步主人目标: " + getTarget()); // 调试输出
            }
        }
    }
    public static class PrismChaseGoal extends Goal {
        private final TerraPrismEntity1 prism;
        private final float speedModifier;

        public PrismChaseGoal(TerraPrismEntity1 prism, float speed) {
            this.prism = prism;
            this.speedModifier = speed;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = prism.getTarget();
            return target != null &&
                    target.isAlive() &&
                    prism.distanceToSqr(target) > 2.0f; // 只在攻击范围外触发
        }

        @Override
        public void start() {
            prism.getNavigation().stop(); // 清除旧路径
            prism.setDeltaMovement(Vec3.ZERO); // 重置速度
        }

        @Override
        public void tick() {
            LivingEntity target = prism.getTarget();
            if (target == null) return;

            // 精确控制移动
            prism.getNavigation().moveTo(
                    target,
                    speedModifier * (prism.distanceTo(target) > 3.0 ? 1.2 : 0.8) // 动态速度
            );
        }
    }
    private class PrismChargeAttackGoal extends Goal {
        private final TerraPrismEntity1 prism;
        public PrismChargeAttackGoal(TerraPrismEntity1 prism) {
            this.prism = prism;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            LivingEntity target = prism.getTarget();
            return target != null
                    && target.isAlive()
                    && prism.distanceToSqr(target) <= 9.0 // 3格距离判定
                    && prism.hasLineOfSight(target); // 必须可见
        }

        @Override
        public void start() {
            LivingEntity target = getTarget();
            if (target != null) {
                Vec3 eyePos = target.getEyePosition();
                getMoveControl().setWantedPosition(eyePos.x, eyePos.y, eyePos.z, 1.0);
            }
            level().broadcastEntityEvent(prism, (byte) 4); // 触发冲锋动画
        }

        @Override
        public void tick() {
            LivingEntity target = prism.getTarget();
            if (target == null || !canUse()) return;

            // 持续靠近目标
            prism.getNavigation().moveTo(target, 1.2);

            // 接触判定
            if (prism.getBoundingBox().inflate(0.5).intersects(target.getBoundingBox())) {
                prism.doHurtTarget(target);
                prism.level().broadcastEntityEvent(prism, (byte)5); // 播放击打动画
            }
        }
        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }
    }
}

 */
