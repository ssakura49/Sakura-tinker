package com.ssakura49.sakuratinker.common.entity;

import com.mojang.datafixers.util.Pair;
import com.ssakura49.sakuratinker.common.entity.base.IYoyo;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import com.ssakura49.sakuratinker.network.PacketHandler;
import com.ssakura49.sakuratinker.network.s2c.CollectedDropsSync;
import com.ssakura49.sakuratinker.register.STEntities;
import com.ssakura49.sakuratinker.register.STItems;
import com.ssakura49.sakuratinker.utils.tinker.AttackUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.*;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.scores.Team;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.PlayMessages;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.*;
import java.util.function.DoubleSupplier;

import static java.lang.Math.*;

public class YoyoEntity extends Entity implements IEntityAdditionalSpawnData, ItemSupplier {

    public static final EntityDataAccessor<ItemStack> DATA_ITEM_STACK = SynchedEntityData.defineId(YoyoEntity.class,EntityDataSerializers.ITEM_STACK);
    public static final EntityDataAccessor<Byte> HAND = SynchedEntityData.defineId(YoyoEntity.class,EntityDataSerializers.BYTE);
    public static final EntityDataAccessor<Boolean> RETRACTING = SynchedEntityData.defineId(YoyoEntity.class,EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> MAX_TIME  = SynchedEntityData.defineId(YoyoEntity.class,EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> REMAINING_TIME  = SynchedEntityData.defineId(YoyoEntity.class,EntityDataSerializers.INT);
    public static final EntityDataAccessor<Float> WEIGHT = SynchedEntityData.defineId(YoyoEntity.class,EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> CURRENT_LENGTH = SynchedEntityData.defineId(YoyoEntity.class,EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Float> MAX_LENGTH = SynchedEntityData.defineId(YoyoEntity.class,EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> MAX_COLLECTED_DROPS = SynchedEntityData.defineId(YoyoEntity.class,EntityDataSerializers.INT);
    public static final float MAX_RETRACT_TIME = 40;
    private static final Logger log = LoggerFactory.getLogger(YoyoEntity.class);
    public static Map<UUID,YoyoEntity> CASTERS = new HashMap<>();


    protected List<ItemStack> collectedDrops = new ArrayList<>();
    protected int numCollectedDrops = 0;
    protected boolean needCollectedSync = false;

    private Player thrower;
    private boolean isThrowerInitialized = false;

    protected ItemStack yoyoStackLastTick = ItemStack.EMPTY;

    private IYoyo yoyo;
    private boolean isYoyoInitialized = false;

    protected int attackCool = 0;
    protected int attackInterval = 0;
    protected boolean shouldResetCool = false;

    protected boolean canCancelRetract = true;
    protected int retractionTimeout = 0;

    protected int lastSlot = -1;

    protected boolean shouldGetStats = true;

    protected boolean doesBlockInteraction = true;

    private ItemStack yoyoStack;
    private ToolStack toolStack;
    private StatsNBT stats;

    private final float baseDamage = 1.0f;
    private float bonusDamage = 0.0F;
    private boolean critical = false;

    private int hitCount = 0;

    // CONSTRUCTORS

    public YoyoEntity(EntityType<?> type, Level level) {
        super(type, level);
        noCulling = true;
        setNoGravity(true);
        this.yoyoStack = ItemStack.EMPTY;
        this.toolStack = null;
        this.stats = StatsNBT.EMPTY;
    }

    public YoyoEntity(Level level) {
        this(STEntities.YOYO.get(), level);
    }

    public YoyoEntity(Level level, Player player, InteractionHand hand) {
        this(STEntities.YOYO.get(), level,player,hand);
    }

    public YoyoEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        super(STEntities.YOYO.get(), level);
    }

    public void setTool(ItemStack tool) {
        if (!tool.isEmpty()) {
            this.yoyoStack = tool.copy();
            this.toolStack = ToolStack.from(tool);
            this.stats = this.toolStack.getStats();
        } else {
            this.yoyoStack = ItemStack.EMPTY;
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
        return this.stats.get(ToolStats.ATTACK_DAMAGE) + this.bonusDamage + getBaseDamage();
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

    public YoyoEntity(EntityType<?> type, Level level, Player player, InteractionHand hand) {
        this(type, level);
        setThrower(player);
        setHand(hand);
        CASTERS.put(player.getUUID(),this);
        Vec3 handPos = getPlayerHandPos(1f);
        setPos(handPos);
        if (!level.noCollision(this))
            setPos(player.getX(),player.getY() + getThrowerEyeHeight(), player.getZ());
    }

    //ENTITY METHODS

    @Override
    protected void defineSynchedData() {
//        this.entityData.define(YOYO_STACK, ItemStack.EMPTY);
        this.entityData.define(DATA_ITEM_STACK, ItemStack.EMPTY);
        this.entityData.define(HAND, (byte)InteractionHand.MAIN_HAND.ordinal());
        this.entityData.define(RETRACTING, false);
        this.entityData.define(MAX_TIME, -1);
        this.entityData.define(REMAINING_TIME, -1);
        this.entityData.define(WEIGHT, 1.0f);
        this.entityData.define(CURRENT_LENGTH, 1.0f);
        this.entityData.define(MAX_LENGTH, 1.0f);
        this.entityData.define(MAX_COLLECTED_DROPS, 0);
    }


    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        ListTag list = new ListTag();
        ItemStack $$1 = this.getItemRaw();
        if (!$$1.isEmpty()) {
            tag.put("Item", $$1.save(new CompoundTag()));
        }
        collectedDrops.forEach(it->{
            CompoundTag stackTag = new CompoundTag();
            ResourceLocation id = ForgeRegistries.ITEMS.getKey(it.getItem());
            stackTag.putString("id",id.toString());
            stackTag.putInt("count",it.getCount());
            if (it.hasTag()){
                stackTag.put("tag",it.getTag());
            }
            list.add(stackTag);
        });
        tag.put("collectedDrops",list);
        tag.put("Tool", yoyoStack.serializeNBT());
        tag.putFloat("BonusDamage", bonusDamage);
        tag.putBoolean("Critical", critical);
    }
    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        collectedDrops.clear();
        ItemStack yoyo = ItemStack.of(tag.getCompound("Item"));
        this.setItem(yoyo);
        ListTag list = tag.getList("collectedDrops", Tag.TAG_COMPOUND);
        for (int i = 0; i < list.size(); i++) {
            CompoundTag nbt = list.getCompound(i);
            nbt.putByte("Count", (byte) 1);
            ItemStack stack = ItemStack.of(nbt);
            stack.setCount(nbt.getByte("count"));
            collectedDrops.add(stack);
        }
        this.setTool(ItemStack.of(tag.getCompound("Tool")));
        this.bonusDamage = tag.getFloat("BonusDamage");
        this.critical = tag.getBoolean("Critical");
    }


    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItem(this.yoyoStack);
        buffer.writeFloat(this.bonusDamage);
        buffer.writeBoolean(this.critical);
        buffer.writeInt(thrower.getId());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf buffer) {
        Player player = (Player) level().getEntity(buffer.readInt());
        this.setTool(buffer.readItem());
        this.bonusDamage = buffer.readFloat();
        this.critical = buffer.readBoolean();
        thrower = player;
        if (player!=null) {
            CASTERS.put(player.getUUID(),this);
        }
    }


    /**
     * @return the amount of stack left uncollected
     */
    public ItemStack collectDrop(ItemStack stack) {
        if (!isCollecting()) return stack;

        int maxTake = getMaxCollectedDrops() - numCollectedDrops;

        ItemStack take = stack.split(maxTake);
        collectedDrops.add(take);
        needCollectedSync = true;
        numCollectedDrops += take.getCount();

        return stack;
    }

    public void collectDrop(ItemEntity drop) {
        if (drop == null) return;

        ItemStack stack = drop.getItem();
        int countBefore = stack.getCount();
        collectDrop(stack);

        if (countBefore == stack.getCount()) return;

        drop.setItem(stack);

        if (stack.isEmpty()) {
            drop.setNeverPickUp();
            drop.remove(RemovalReason.KILLED);
        }
        level().playSound(null, drop.getX(), drop.getY(), drop.getZ(), SoundEvents.ITEM_PICKUP, SoundSource.NEUTRAL, 0.2f, ((random.nextFloat() - random.nextFloat()) * 0.7f + 1.0f) * 2.0f);
    }
    public Vec3 getPlayerHandPos(Player thrower,Float partialTicks) {


        float yaw = thrower.getYRot();
        float pitch = thrower.getXRot();

        double posX = thrower.getX();
        double posY = thrower.getY();
        double posZ = thrower.getZ();

        if (partialTicks != 1f) {
            yaw = (float) interpolateValue(thrower.yRotO, yaw, partialTicks);
            pitch = (float) interpolateValue(thrower.xRotO, pitch, partialTicks);

            posX = interpolateValue(thrower.xo, posX, partialTicks);
            posY = interpolateValue(thrower.yo, posY, partialTicks);
            posZ = interpolateValue(thrower.zo, posZ, partialTicks);
        }

        double throwerLookOffsetX = cos(yaw * 0.01745329238474369f);
        double throwerLookOffsetZ = sin(yaw * 0.01745329238474369f);
        double throwerLookOffsetY = sin(pitch * 0.01745329238474369f);
        double throwerLookWidth = cos(pitch * 0.01745329238474369f);

        float side = thrower.getMainArm() == HumanoidArm.RIGHT == (getHand() == InteractionHand.MAIN_HAND) ? 1f : -1f;

        return new Vec3(posX - throwerLookOffsetX * side * 0.4 - throwerLookOffsetZ * 0.5 * throwerLookWidth, (posY + getThrowerEyeHeight()) - (throwerLookOffsetY * 0.5) - 0.25, posZ - throwerLookOffsetZ * side * 0.4 + throwerLookOffsetX * 0.5 * throwerLookWidth);
    }

    public Vec3 getPlayerHandPos(Float partialTicks) {
        if (!hasThrower()) return new Vec3(getX(), getY(), getZ());

        float yaw = thrower.getYRot();
        float pitch = thrower.getXRot();

        double posX = thrower.getX();
        double posY = thrower.getY();
        double posZ = thrower.getZ();

        if (partialTicks != 1f) {
            yaw = (float) interpolateValue(thrower.yRotO, yaw, partialTicks);
            pitch = (float) interpolateValue(thrower.xRotO, pitch, partialTicks);

            posX = interpolateValue(thrower.xo, posX, partialTicks);
            posY = interpolateValue(thrower.yo, posY, partialTicks);
            posZ = interpolateValue(thrower.zo, posZ, partialTicks);
        }

        double throwerLookOffsetX = cos(yaw * 0.01745329238474369f);
        double throwerLookOffsetZ = sin(yaw * 0.01745329238474369f);
        double throwerLookOffsetY = sin(pitch * 0.01745329238474369f);
        double throwerLookWidth = cos(pitch * 0.01745329238474369f);

        float side = thrower.getMainArm() == HumanoidArm.RIGHT == (getHand() == InteractionHand.MAIN_HAND) ? 1f : -1f;

        return new Vec3(posX - throwerLookOffsetX * side * 0.4 - throwerLookOffsetZ * 0.5 * throwerLookWidth, (posY + getThrowerEyeHeight()) - (throwerLookOffsetY * 0.5) - 0.25, posZ - throwerLookOffsetZ * side * 0.4 + throwerLookOffsetX * 0.5 * throwerLookWidth);
    }

    public double interpolateValue(double start, double end, double progress){
        return start + (end - start) * progress;
    }

    public void forceRetract(){
        setRetracting(true);
        canCancelRetract = false;
    }

    public void resetOrIncrementAttackCooldown(){
        if (shouldResetCool){
            attackCool = 0;
            shouldResetCool = false;
        } else {
            attackCool++;
        }
    }

    public int decrementRemainingTime() {
        return decrementRemainingTime(1); // Appel avec la valeur par défaut
    }

    public int decrementRemainingTime(int amount) {
        int out = getRemainingTime() - amount;
        setRemainingTime(out);
        return out;
    }

    public boolean canAttack(){
        return attackCool >= attackInterval;
    }

    public void resetAttackCooldown(){
        shouldResetCool = true;
    }

    @Override
    public void tick() {
        super.tick();
        this.xOld = this.getX();
        this.yOld = this.getY();
        this.zOld = this.getZ();

        if (hasThrower() /*&& thrower.isAlive()*/){
            // 添加死亡检查
            if (!thrower.isAlive()) {
                if (!level().isClientSide) {
                    remove(RemovalReason.KILLED);
                }
                return;
            }

            if (checkAndGetYoyo() == null) return;
            setYoyo(checkAndGetYoyo());

            if (getMaxTime() >= 0 && decrementRemainingTime() < 0) forceRetract();


            updateMotion();
            moveAndCollide();

            yoyo.onUpdate(getYoyoStack(),this);

            if (!level().isClientSide && doesBlockInteraction()) worldInteraction();

            if (isCollecting()) updateCapturedDrops();

            resetOrIncrementAttackCooldown();
        } else
        if (!level().isClientSide) remove(RemovalReason.UNLOADED_WITH_PLAYER);

    }

    public float getRotation(int age, float partialTicks){
        int maxTime = getMaxTime();
        float ageInTicks;

        if (maxTime < 0)
            ageInTicks = age + partialTicks;
        else
            ageInTicks = maxTime - getRemainingTime() + partialTicks;

        var multiplier = 35f;

        if (maxTime >= 0) multiplier *= ageInTicks / ((float)maxTime);

        return ageInTicks * multiplier;
    }

    protected void updateCapturedDrops(){

        if (!level().isClientSide && !collectedDrops.isEmpty() && needCollectedSync){
            Iterator<ItemStack> iterator = collectedDrops.iterator();

            Map<Item,ItemStack> existing = new HashMap<>();

            while (iterator.hasNext()) {
                ItemStack collectedDrop = iterator.next();

                if (!collectedDrop.isEmpty()) {
                    if (collectedDrop.hasTag()) continue ;
                    Item item = collectedDrop.getItem();

                    ItemStack master = existing.get(item);

                    if (master != null && collectedDrop.equals(master,false)) {
                        master.setCount(master.getCount() + collectedDrop.getCount());
                        iterator.remove();
                    } else {
                        existing.put(item,collectedDrop);
                    }
                }
            }

            if (!level().isClientSide) PacketHandler.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(()->level().getChunkAt(blockPosition())), new CollectedDropsSync(this));

            needCollectedSync = false;
        }
    }

    public void createItemDropOrCollect(ItemStack stack){
        ItemStack remaining = stack;
        if (isCollecting()){
            remaining = collectDrop(stack);

            if (remaining.isEmpty()) return;
        }
        ItemEntity item = new ItemEntity(level(),getX(),getY(),getZ(),remaining);
        item.setDefaultPickUpDelay();
        level().addFreshEntity(item);
    }

    protected void worldInteraction(){
        BlockPos pos = blockPosition();

        AABB entityBox = getBoundingBox().inflate(0.1);

        BlockPos.betweenClosedStream(pos.offset(-1,-1,-1),pos.offset(1,1,1))
                .map(p->new Pair<BlockPos, BlockState>(p.immutable(),level().getBlockState(p)))
                .filter(p->!p.getSecond().isAir())
                .filter(p->p.getSecond()
                        .getShape(level(),p.getFirst())
                        .toAabbs().stream().anyMatch(bb->bb.move(p.getFirst()).intersects(entityBox)))
                .forEach(p->yoyo.blockInteraction(getYoyoStack(),thrower,level(),p.getFirst(),p.getSecond(),p.getSecond().getBlock(),this));
    }

    protected void moveAndCollide(){
        AABB yoyoBox = getBoundingBox();
        AABB targetBox = yoyoBox.move(getDeltaMovement());
        if (noPhysics){
            Vec3 pos = targetBox.getCenter();
            setPos(pos);
            return;
        }

        AABB union = yoyoBox.minmax(targetBox);

        List<AABB> collisions = new ArrayList<>();

        for (VoxelShape voxelShape : level().getCollisions(null, union)) {
            collisions.addAll(voxelShape.toAabbs());
        }

        List<Entity> entities = level().getEntities(this,union);

        int steps = 50;

        for (int step = 1; step < steps; step++) {
            Vec3 motion = getDeltaMovement();
            double dx = motion.x / step;
            double dy = motion.y / step;
            double dz = motion.z / step;

            for (AABB collision : collisions) {
                dx = calculateOffset(collision,yoyoBox,dx,'x');
                dy = calculateOffset(collision,yoyoBox,dy,'y');
                dz = calculateOffset(collision,yoyoBox,dz,'z');
            }

            yoyoBox = yoyoBox.move(dx,dy,dz);

            for (AABB collision : collisions) {
                if (collision.intersects(yoyoBox)) {
                    dx = calculateOffset(collision, yoyoBox, dx, 'x');
                    dy = calculateOffset(collision, yoyoBox, dy, 'y');
                    dz = calculateOffset(collision, yoyoBox, dz, 'z');

                    yoyoBox = yoyoBox.move(-dx,-dy,-dz);

                }
            }

            if (!level().isClientSide){
                Iterator<Entity> iterator = entities.iterator();

                while (iterator.hasNext()){
                    Entity entity = iterator.next();

                    if (entity == thrower){
                        iterator.remove();
                        continue;
                    }

                    if (entity.getBoundingBox().intersects(yoyoBox)){
                        interactWithEntity(entity);
                        iterator.remove();
                    }
                }
            }

            //Vec3 pos = yoyoBox.getCenter();
            //setPos(pos.x,yoyoBox.minY,pos.z);
        }


    }

    protected void interactWithEntity(Entity entity) {
        if (!(entity instanceof LivingEntity target) || !canAttack()) return;

        Player thrower = getThrower();
        if (thrower == null) return;

        InteractionHand hand = getHand();
        ItemStack stack = getYoyoStack();
        if (toolStack != null && !toolStack.isBroken()) {
            DoubleSupplier cooldownFunction = () -> attackCool;
            AttackUtil.attackEntity(
                    toolStack,            // IToolStackView tool
                    thrower,              // LivingEntity attackerLiving
                    hand,                  // InteractionHand hand
                    target,                // Entity targetEntity
                    cooldownFunction,     // DoubleSupplier cooldownFunction
                    false,                 // boolean isExtraAttack
                    EquipmentSlot.MAINHAND,// EquipmentSlot sourceSlot
                    false,                 // boolean setDamage
                    getDamage(),       // float damageSet
                    false                  // boolean noToolDamage
            );
            resetAttackCooldown();
            hitCount++;
        }
    }
    public int getHitCount() {
        return hitCount;
    }


    protected void updateMotion(){
        Vec3 motion = getTarget().subtract(getX(),getY() + getDimensions(getPose()).height / 2, getZ()).scale(0.15 + 0.85 * pow(1.1,-((10-getWeight()) * (10-getWeight()))));

        if (isInWater()){
            motion = motion.scale(yoyo.getWaterMovementModifier(getYoyoStack()));
        }

        setDeltaMovement(motion);

        move(MoverType.SELF,getDeltaMovement());

    }

    protected Vec3 getTarget(){
        if (isRetracting()){
            Vec3 handPos = getPlayerHandPos(1f);
            double dX = getX() - handPos.x;
            double dY = getY() - handPos.y;
            double dZ = getZ() - handPos.z;

            if (dX * dX + dY * dY + dZ * dZ < 0.8 || retractionTimeout++ >= MAX_RETRACT_TIME ) remove(RemovalReason.KILLED);

            return handPos;
        } else {

            Vec3 eyePos = new Vec3(getThrower().getX(), getThrower().getY() + getThrowerEyeHeight(), getThrower().getZ());
            Vec3 look = getThrower().getViewVector(1f);

            double cordLength = getCurrentLength();

            Vec3 target = new Vec3(eyePos.x + look.x * cordLength,eyePos.y + look.y * cordLength, eyePos.z + look.z * cordLength);

            retractionTimeout = 0;
            HitResult result = getRaycast(eyePos,target);

            if (result != null) target = result.getLocation();

            return target;
        }
    }

    private HitResult getRaycast(Vec3 from, Vec3 to){
        double distance = from.distanceTo(to);
        HitResult result = level().clip(new ClipContext(from,to, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE,getThrower()));
        boolean flag = false;
        double d1 = distance;

        if (distance > 3.0) flag = true;

        d1 = result.getLocation().distanceTo(from);

        Vec3 vec3d1 = thrower.getViewVector(1f);
        Entity pointedEntity = null;
        Vec3 vec3d3 = null;
        AABB expanded = thrower.getBoundingBox().expandTowards(vec3d1.x * distance, vec3d1.y * distance, vec3d1.z * distance);

        List<Entity> listEntity = level().getEntities((Entity) null,expanded, e-> !(e instanceof Player) || !e.isSpectator() &&e.canBeCollidedWith());

        double d2 = d1;

        for (Entity entity : listEntity) {
            if (entity == this || entity == thrower) continue;
            AABB box = entity.getBoundingBox().inflate(entity.getPickRadius());
            Optional<Vec3> rayResult = box.clip(from, to);
            if (box.contains(from)){
                if (d2 >= 0.0) {
                    pointedEntity = entity;
                    vec3d3 = rayResult.orElse(from);
                    d2 = 0.0;
                }
            } else if (rayResult.isPresent()) {
                double d3 = from.distanceTo(rayResult.get());

                if (d3 < d2 || d2 == 0.0) {
                    if (entity.getRootVehicle() == thrower.getRootVehicle() && !thrower.canRiderInteract()) {
                        if (d2 == 0.0) {
                            pointedEntity = entity;
                            vec3d3 = rayResult.get();
                        }
                    } else {
                        pointedEntity = entity;
                        vec3d3 = rayResult.get();
                        d2 = d3;
                    }
                }
            }
        }

        if (vec3d3 != null) {
            if (flag) {
                pointedEntity = null;
                result = BlockHitResult.miss(vec3d3, Direction.UP, BlockPos.containing(vec3d3));
            }

            if (pointedEntity != null && result == null) {
                result = new EntityHitResult(pointedEntity, vec3d3);
            }
        }
        return result;
    }

    public static double calculateOffset(AABB one, AABB other, double offset, char axis) {
        switch (axis) {
            case 'x':
                if (other.maxY > one.minY && other.minY < one.maxY && other.maxZ > one.minZ && other.minZ < one.maxZ) {
                    if (offset > 0.0 && other.maxX <= one.minX) {
                        double d1 = one.minX - other.maxX;
                        if (d1 < offset) {
                            offset = d1;
                        }
                    } else if (offset < 0.0 && other.minX >= one.maxX) {
                        double d0 = one.maxX - other.minX;
                        if (d0 > offset) {
                            offset = d0;
                        }
                    }
                }
                break;

            case 'y':
                if (other.maxX > one.minX && other.minX < one.maxX && other.maxZ > one.minZ && other.minZ < one.maxZ) {
                    if (offset > 0.0 && other.maxY <= one.minY) {
                        double d1 = one.minY - other.maxY;
                        if (d1 < offset) {
                            offset = d1;
                        }
                    } else if (offset < 0.0 && other.minY >= one.maxY) {
                        double d0 = one.maxY - other.minY;
                        if (d0 > offset) {
                            offset = d0;
                        }
                    }
                }
                break;

            case 'z':
                if (other.maxX > one.minX && other.minX < one.maxX && other.maxY > one.minY && other.minY < one.maxY) {
                    if (offset > 0.0 && other.maxZ <= one.minZ) {
                        double d1 = one.minZ - other.maxZ;
                        if (d1 < offset) {
                            offset = d1;
                        }
                    } else if (offset < 0.0 && other.minZ >= one.maxZ) {
                        double d0 = one.maxZ - other.minZ;
                        if (d0 > offset) {
                            offset = d0;
                        }
                    }
                }
                break;

            default:
                throw new IllegalArgumentException("Invalid axis: " + axis);
        }

        return offset;
    }

    protected IYoyo checkAndGetYoyo(){
        InteractionHand hand = getHand();
        ItemStack stack = thrower.getItemInHand(hand);
        setYoyoStack(stack);
        setTool(stack);
        int currentSlot = hand == InteractionHand.MAIN_HAND ? getThrower().getInventory().selected : -2;

        ItemStack otherHand = getThrower().getItemInHand(hand == InteractionHand.MAIN_HAND ? InteractionHand.OFF_HAND : InteractionHand.MAIN_HAND);

        if (!CASTERS.containsKey(getThrower().getUUID()) || !(stack.getItem() instanceof IYoyo) || tickCount > 1 && (lastSlot != -1 && lastSlot != currentSlot || otherHand == yoyoStackLastTick)){
            remove(RemovalReason.KILLED);
            return null;
        }

        yoyoStackLastTick = stack;

        if (stack.getMaxDamage() < stack.getDamageValue() /*&& stack.getItem() != */){
            remove(RemovalReason.KILLED);
            return null;
        }

        if (!level().isClientSide && CASTERS.get(getThrower().getUUID()) != this){
            CASTERS.put(getThrower().getUUID(),this);
        }

        IYoyo yoyo = (IYoyo) stack.getItem();

        if (!level().isClientSide && shouldGetStats) {
            if (toolStack != null && !toolStack.isBroken()) {
                StatsNBT stats = toolStack.getStats();

                int maxTime = stats.getInt(STToolStats.TIME);
                setMaxTime(maxTime);
                setRemainingTime(maxTime);

                float reach = stats.get(STToolStats.LENGTH);
                float maxLength = 4.0f + reach;
                setMaxLength(maxLength);
                setCurrentLength(maxLength);

                int maxCollected = stats.getInt(STToolStats.MAX_COLLECTED);
                setMaxCollectedDrops(maxCollected);

                int attack_Interval = stats.getInt(STToolStats.ATTACK_INTERVAL);
                attackInterval = attack_Interval;

                float weight = stats.get(STToolStats.WEIGHT);
                setWeight(weight);


                setInteractsWithBlocks(yoyo.interactsWithBlocks(stack));
                shouldGetStats = false;
            } else {
                setMaxCollectedDrops(1);
                attackInterval = 20;
                setMaxTime(100);
                setRemainingTime(100);
                setCurrentLength(3);
                setMaxLength(3);
                setWeight(8);
                setInteractsWithBlocks(yoyo.interactsWithBlocks(stack));
                shouldGetStats = false;
            }
        }
        lastSlot = currentSlot;
        return yoyo;
    }

    @Override
    public void remove(@NotNull RemovalReason p_146834_) {
        super.remove(p_146834_);
        boolean hasThrower = hasThrower();
        if (hasThrower) CASTERS.remove(getThrower().getUUID());

        if (collectedDrops.isEmpty()) return;

        if (!level().isClientSide){
            // 只在玩家存活时尝试归还物品, && thrower.isAlive()
            if (hasThrower && thrower.isAlive()){
                Inventory inv = getThrower().getInventory();
                collectedDrops.stream()
                        .filter(it->!it.isEmpty())
                        .forEach(inv::placeItemBackInInventory);
            } else {
                collectedDrops.forEach(it->{
                    if (it != null && !it.isEmpty()){
                        while (it.getCount() > 0){
                            ItemStack stack = it.split(it.getMaxStackSize());
                            ItemEntity item = new ItemEntity(level(),getX(),getY() + getDimensions(Pose.STANDING).height,getZ(),stack);
                            item.setDefaultPickUpDelay();
                            item.setDeltaMovement(Vec3.ZERO);

                            level().addFreshEntity(item);
                        }
                    }
                });
            }
        }
        collectedDrops.clear();
    }


    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public @Nullable Team getTeam() {
        return hasThrower() ? getThrower().getTeam() : super.getTeam();
    }

    //GETTERS SETTERS AND HASVAR
    public List<ItemStack> getCollectedDrops() {
        return collectedDrops;
    }

    public boolean doesBlockInteraction(){
        return doesBlockInteraction;
    }

    public void setInteractsWithBlocks(boolean blockInteraction) {
        doesBlockInteraction = blockInteraction;
    }

    public boolean hasThrower() {
        return isThrowerInitialized;
    }

    protected void setThrower(Player thrower) {
        this.thrower = thrower;
        this.isThrowerInitialized = true;
    }

    public Player getThrower() {
        if (!isThrowerInitialized) {
            throw new IllegalStateException("Thrower is not initialized");
        }
        return thrower;
    }

    public boolean hasYoyo() {
        return isYoyoInitialized;
    }

    protected void setYoyo(IYoyo yoyo) {
        this.yoyo = yoyo;
        this.isYoyoInitialized = true;
    }

    public IYoyo getYoyo() {
        if (!isYoyoInitialized) {
            throw new IllegalStateException("Yoyo is not initialized");
        }
        return yoyo;
    }

    public ItemStack getYoyoStack() {
    return this.entityData.get(DATA_ITEM_STACK);
}

    public void setYoyoStack(ItemStack stack) {
        this.entityData.set(DATA_ITEM_STACK, stack);
    }
    // hand
    public InteractionHand getHand() {
        return InteractionHand.values()[this.entityData.get(HAND)];
    }

    public void setHand(InteractionHand hand) {
        this.entityData.set(HAND, (byte) hand.ordinal());
    }

    // isRetracting
    public boolean isRetracting() {
        return this.entityData.get(RETRACTING);
    }

    public void setRetracting(boolean retracting) {
        if (canCancelRetract || !isRetracting()) {
            this.entityData.set(RETRACTING, retracting);
        }
    }

    // maxTime
    public int getMaxTime() {
        return this.entityData.get(MAX_TIME);
    }

    public void setMaxTime(int duration) {
        this.entityData.set(MAX_TIME, duration);
    }

    // remainingTime
    public int getRemainingTime() {
        return this.entityData.get(REMAINING_TIME);
    }

    public void setRemainingTime(int duration) {
        this.entityData.set(REMAINING_TIME, duration);
    }

    // weight
    public float getWeight() {
        return this.entityData.get(WEIGHT);
    }

    public void setWeight(float weight) {
        this.entityData.set(WEIGHT, weight);
    }

    // currentLength
    public float getCurrentLength() {
        return this.entityData.get(CURRENT_LENGTH);
    }

    public void setCurrentLength(float length) {
        this.entityData.set(CURRENT_LENGTH, length);
    }

    // maxLength
    public float getMaxLength() {
        return this.entityData.get(MAX_LENGTH);
    }

    public void setMaxLength(float length) {
        this.entityData.set(MAX_LENGTH, length);
    }

    // maxCollectedDrops
    public int getMaxCollectedDrops() {
        return this.entityData.get(MAX_COLLECTED_DROPS);
    }

    public void setMaxCollectedDrops(int drops) {
        this.entityData.set(MAX_COLLECTED_DROPS, drops);
    }

    public boolean isCollecting(){
        return getMaxCollectedDrops() > 0;
    }

    public float getThrowerEyeHeight(){
        return thrower.getStandingEyeHeight(thrower.getPose(),thrower.getDimensions(thrower.getPose()));
    }

    public float getBaseDamage() {
        return baseDamage;
    }
}
