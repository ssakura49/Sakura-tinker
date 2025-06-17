package com.ssakura49.sakuratinker.content.entity.terraprisma;

import com.ssakura49.sakuratinker.content.entity.base.FlyingPathfinderMob;
import com.ssakura49.sakuratinker.register.STEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Objects;
import java.util.UUID;

public class TerraPrismEntity extends FlyingPathfinderMob implements OwnableEntity {
    private LivingEntity owner;
    private UUID ownerUUID;
    private AttackMode currentAttack = AttackMode.IDLE;
    private int attackTick = 0;
    private LivingEntity target;
    private String animationState = "idle";
    private int animationTick = 0;
    private int preAttackDelay = 0;

    private float moveSpeed = 0.3f;
    private int attackCooldown = 20;
    private double targetRange = 12.0;

    //属性应用
    public void applyStats(PrismStats stats) {
        setMaxHealth(stats.maxHealth());
        setBaseSpeed(stats.movementSpeed());
        setFlyingSpeed(stats.flyingSpeed());
        setAttackDamage(stats.attackDamage());
        setFollowRange(stats.followRange());
        this.attackCooldown = stats.attackCooldown();
    }

    //属性设置
    public void setMoveSpeed(float moveSpeed) { this.moveSpeed = moveSpeed; }
    public float getMoveSpeed() { return this.moveSpeed; }

    public void setAttackCooldown(int attackCooldown) { this.attackCooldown = attackCooldown; }
    public int getAttackCooldown() { return attackCooldown; }

    public void setTargetRange(double range) { this.targetRange = range; }
    public double getTargetRange() { return targetRange; }


    public void setMaxHealth(float maxHealth) {
        AttributeInstance attr = this.getAttribute(Attributes.MAX_HEALTH);
        if (attr != null) attr.setBaseValue(maxHealth);
    }

    public void setBaseSpeed(float speed) {
        AttributeInstance attr = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attr != null) attr.setBaseValue(speed);
    }

    public void setFlyingSpeed(float speed) {
        AttributeInstance attr = this.getAttribute(Attributes.FLYING_SPEED);
        if (attr != null) attr.setBaseValue(speed);
    }

    public void setAttackDamage(float damage) {
        AttributeInstance attr = this.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attr != null) attr.setBaseValue(damage);
    }

    public void setFollowRange(float range) {
        AttributeInstance attr = this.getAttribute(Attributes.FOLLOW_RANGE);
        if (attr != null) attr.setBaseValue(range);
    }

    public TerraPrismEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        super(STEntities.TERRA_PRISMA.get(),level);
    }

    public static AttributeSupplier.Builder createTerraPrismaAttributes() {
        return FlyingPathfinderMob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.FLYING_SPEED, 7.0)
                .add(Attributes.MOVEMENT_SPEED, 5.0)
                .add(Attributes.FOLLOW_RANGE, 16.0)
                .add(Attributes.ATTACK_DAMAGE, 4.0);
    }

    public enum AttackMode {
        IDLE, CHARGING, SLASHING, DASHING
    }

    public TerraPrismEntity(EntityType<? extends TerraPrismEntity> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }
    @Override
    public UUID getOwnerUUID() {
        return this.ownerUUID;
    }

    @Override
    public @NotNull Level level() {
        return super.level();
    }

    public LivingEntity getOwnerLiving() {
        return (LivingEntity) getOwner();
    }

    public void setOwner(LivingEntity owner) {
        this.ownerUUID = owner.getUUID();
    }
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        if (this.ownerUUID != null) {
            tag.putUUID("Owner", this.ownerUUID);
        }
        tag.putFloat("MoveSpeed", moveSpeed);
        tag.putInt("AttackCooldown", attackCooldown);
        tag.putDouble("TargetRange", targetRange);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.hasUUID("Owner")) {
            this.ownerUUID = tag.getUUID("Owner");
        }
        if (tag.contains("MoveSpeed")) moveSpeed = tag.getFloat("MoveSpeed");
        if (tag.contains("AttackCooldown")) attackCooldown = tag.getInt("AttackCooldown");
        if (tag.contains("TargetRange")) targetRange = tag.getDouble("TargetRange");
    }
    @Nullable
    public LivingEntity getOwner() {
        if (ownerUUID != null && level() instanceof ServerLevel serverLevel) {
            Entity e = serverLevel.getEntity(ownerUUID);
            return e instanceof LivingEntity living ? living : null;
        }
        return null;
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.ownerUUID = uuid;
    }
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new PrismFollowOwnerGoal(this));
        this.goalSelector.addGoal(2, new PrismAttackGoal(this));
        this.targetSelector.addGoal(1, new PrismOwnerTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(
                this,
                LivingEntity.class,
                10,
                true,
                false,
                living -> this.isValidTarget(living) &&
                        this.distanceToSqr(living) < this.getTargetRange() * this.getTargetRange()
        ));
    }

    private boolean isValidTarget(LivingEntity entity) {
        if (entity instanceof Player) return false;
        if (getOwner() != null && entity.isAlliedTo(getOwner())) return false;
        return entity instanceof Enemy;
    }

    public LivingEntity getOwnerEntity() {
        return owner;
    }
    public void setAnimationState(String state) {
        if (!this.animationState.equals(state)) {
            this.animationState = state;
            this.animationTick = 0;
        }
    }

    public String getAnimationState() {
        return animationState;
    }

    public float getAnimationProgress(float partialTicks) {
        return (animationTick + partialTicks) / 20f; // 一次动画20 tick
    }

    public Vec3 getSmoothedPosition(float partialTick) {
        return position().add(getDeltaMovement().scale(partialTick));
    }
    @Override
    protected @NotNull PathNavigation createNavigation(Level level) {
        return new FlyingPathNavigation(this, level);
    }
    @Override
    public void tick() {
        super.tick();
        this.setNoGravity(true);
        this.setOnGround(false);
        this.noPhysics = true;
        animationTick++;
        //每5tick更新目标
        if (!level().isClientSide && tickCount % 5 == 0) {
            if (getOwner() instanceof Player player) {
                LivingEntity ownerTarget = player.getLastHurtMob();
                if (ownerTarget != null && ownerTarget.isAlive() && !ownerTarget.equals(getTarget())) {
                    setTarget(ownerTarget);
                    preAttackDelay = 10; // 设置前摇延迟
                    currentAttack = AttackMode.IDLE;
                    attackTick = 0;
                    animationState = "prepare";
                }
            }
        }
        //有目标并处于准备攻击状态
        if (target != null && !target.isDeadOrDying()) {
            if (preAttackDelay > 0) {
                preAttackDelay--;
                lookAt(target, 360, 360);//面向目标
            } else if (currentAttack == AttackMode.IDLE) {
                //随机选择攻击类型
                currentAttack = level().random.nextBoolean() ? AttackMode.SLASHING : AttackMode.DASHING;
                attackTick = 0;
                animationState = currentAttack == AttackMode.SLASHING ? "slashing" : "dashing";
            }
        }

        //攻击行为
        if (currentAttack == AttackMode.SLASHING && target != null && !target.isDeadOrDying()) {
            attackTick++;
            double angle = attackTick * 0.3;
            double radiusX = 2.5, radiusY = 1.0;
            Vec3 center = owner.position().add(0, 1.2, 0);
            Vec3 slashPos = center.add(
                    Mth.cos((float) angle) * radiusX,
                    Mth.sin((float) angle * 2) * radiusY,
                    Mth.sin((float) angle) * radiusX
            );
            setPos(slashPos);
            if (attackTick == 10) {
                target.hurt(damageSources().mobAttack(this),
                        (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue());
            }
            if (attackTick > 20) {
                currentAttack = AttackMode.IDLE;
                animationState = "idle";
            }
        } else if (currentAttack == AttackMode.DASHING && target != null && !target.isDeadOrDying()) {
            attackTick++;
            Vec3 from = position();
            Vec3 to = target.getBoundingBox().getCenter();
            Vec3 dir = to.subtract(from).normalize().scale(0.6); // 控制冲刺速度
            setDeltaMovement(dir);
            move(MoverType.SELF, dir);
            if (attackTick == 5) {
                target.hurt(damageSources().mobAttack(this),
                        (float) getAttribute(Attributes.ATTACK_DAMAGE).getValue());
            }
            if (attackTick > 10) {
                setDeltaMovement(Vec3.ZERO);
                currentAttack = AttackMode.IDLE;
                animationState = "idle";
            }
        }
    }

    static class PrismFollowOwnerGoal extends Goal {
        private final TerraPrismEntity prism;
        private LivingEntity owner;
        public PrismFollowOwnerGoal(TerraPrismEntity prism) {
            this.prism = prism;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }
        @Override
        public boolean canUse() {
            LivingEntity living = this.prism.getOwner();
            //有攻击目标则不跟随
            if (prism.getTarget() != null && prism.getTarget().isAlive()) return false;
            if (living == null || !living.isAlive()) return false;
            this.owner = living;
            return true;
        }

        @Override
        public void tick() {
            if (owner == null) return;
            Vec3 ownerBack = owner.position().add(owner.getLookAngle().scale(-1.5)).add(0, 1.2, 0);
            Vec3 current = prism.position();
            Vec3 delta = ownerBack.subtract(current);
            double distance = delta.length();
            if (distance > 0.1) {
                Vec3 motion = delta.normalize().scale(Math.min(prism.getMoveSpeed(), distance * 0.25));
                prism.setDeltaMovement(motion);
            } else {
                prism.setDeltaMovement(Vec3.ZERO);
            }
            prism.getLookControl().setLookAt(owner);
        }
        @Override
        public boolean canContinueToUse() {
            return canUse();
        }
    }
    static class PrismAttackGoal extends Goal {
        private final TerraPrismEntity prism;
        private LivingEntity target;
        private int cooldown = 0;
        private boolean isSlashing;
        private int slashTick;

        public PrismAttackGoal(TerraPrismEntity prism) {
            this.prism = prism;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            LivingEntity t = prism.getTarget();
//            System.out.println("[canUse] 当前目标: " + t);
            if (t != null && t.isAlive()) {
                return true;
            }
            return false;
        }
        @Override
        public void start() {
//            System.out.println("[start]当前目标: " + prism.getTarget());
            super.start();
        }

        @Override
        public void tick() {
            LivingEntity target = prism.getTarget();
            if (cooldown > 0) {
                cooldown--;
                return;
            }
            if (target == null || !target.isAlive()) {
//                System.out.println("目标为空或已死亡，取消攻击");
                return;
            }

//            System.out.println("[tick]攻击行为tick,当前目标: " + prism.getTarget());

            Vec3 from = prism.position();
            Vec3 to = target.position().add(0, target.getBbHeight() / 2, 0);
            Vec3 returnPos = prism.getOwner() != null ?
                    prism.getOwner().position().add(prism.getOwner().getLookAngle().scale(-1.5)).add(0, 1.2, 0) :
                    from;

            //决定攻击方式
            if (prism.getRandom().nextBoolean()) {
                //冲刺攻击
                prism.setAnimationState("charge");
                Vec3 dir = to.subtract(from).normalize().scale(1.2);
                prism.setDeltaMovement(dir);
                //碰撞检测
                if (from.distanceTo(to) < 1.5) {
                    target.hurt(prism.damageSources().mobAttack(prism), 6.0f);
                    prism.setDeltaMovement(returnPos.subtract(prism.position()).normalize().scale(0.6));
                    cooldown = prism.getAttackCooldown() + prism.getRandom().nextInt(10);
                }
            } else {
                //椭圆挥砍轨迹
                if (!isSlashing) {
                    isSlashing = true;
                    slashTick = 0;
                    prism.setAnimationState("slash");
                }
                if (isSlashing && slashTick <= 20) {
                    ++slashTick;
                    double angle = Math.PI * 2 * (slashTick / 20.0);
                    double x = Math.cos(angle) * 2.0;
                    double y = Math.sin(angle) * 1.0;
                    Vec3 center = target.position().add(0, target.getBbHeight() / 2, 0);
                    Vec3 offset = new Vec3(x, y, 0);
                    Vec3 ellipsePos = center.add(prism.getLookAngle().cross(new Vec3(0, 1, 0)).normalize().scale(x)).add(0, y, 0);
                    Vec3 motion = ellipsePos.subtract(prism.position()).normalize().scale(0.6);
                    prism.setDeltaMovement(motion);
                    if (slashTick == 10 || slashTick == 20) {
                        if (prism.position().distanceTo(center) < 2.0) {
                            target.hurt(prism.damageSources().mobAttack(prism), 5.0f);
                        }
                    }
                }
                if (slashTick > 20) {
                    isSlashing = false;
                    cooldown = 30;
                    prism.setDeltaMovement(returnPos.subtract(prism.position()).normalize().scale(0.6));
                }
            }
            prism.getLookControl().setLookAt(target);
        }

        @Override
        public boolean canContinueToUse() {
            return target != null && target.isAlive();
        }
    }
}
