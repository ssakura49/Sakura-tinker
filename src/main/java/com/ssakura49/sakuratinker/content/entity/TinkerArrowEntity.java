package com.ssakura49.sakuratinker.content.entity;

import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileCritical;
import com.ssakura49.sakuratinker.library.interfaces.projectile.IProjectileDamage;
import com.ssakura49.sakuratinker.register.STEntities;
import com.ssakura49.sakuratinker.utils.ProjectileUtils;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import slimeknights.tconstruct.library.tools.nbt.StatsNBT;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;
import slimeknights.tconstruct.library.tools.stat.ToolStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class TinkerArrowEntity extends AbstractArrow implements IEntityAdditionalSpawnData, IProjectileCritical, IProjectileDamage {
    private ItemStack arrowStack;
    private ToolStack toolStack;
    private StatsNBT stats;
    private float bonusDamage = 0.0F;
    private boolean critical = false;
    private final Set<Integer> piercedEntityIds = new IntOpenHashSet();
    private final List<Entity> killedEntities = new ArrayList<>();

    public TinkerArrowEntity(EntityType<? extends TinkerArrowEntity> type, Level level) {
        super(type, level);
        this.arrowStack = ItemStack.EMPTY;
        this.toolStack = null;
        this.stats = StatsNBT.EMPTY;
    }
    public TinkerArrowEntity(Level level, double x, double y, double z) {
        super(STEntities.TINKER_ARROW_ENTITY.get(), level);
    }
    public TinkerArrowEntity(Level level, LivingEntity entity) {
        super(STEntities.TINKER_ARROW_ENTITY.get(), entity, level);
        this.setOwner(entity);
        this.arrowStack = ItemStack.EMPTY;
        this.toolStack = null;
        this.stats = StatsNBT.EMPTY;
    }

    public TinkerArrowEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        super(STEntities.TINKER_ARROW_ENTITY.get(),  level);
    }

    public void setTool(ItemStack tool) {
        if (!tool.isEmpty()) {
            this.arrowStack = tool.copy();
            this.toolStack = ToolStack.from(tool);
            this.stats = this.toolStack.getStats();
        } else {
            this.arrowStack = ItemStack.EMPTY;
            this.toolStack = null;
            this.stats = StatsNBT.EMPTY;
        }
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

        return this.calculateModifiedDamage();
    }
    private float calculateModifiedDamage() {
        float damage = this.stats.get(ToolStats.ATTACK_DAMAGE) + this.bonusDamage + 0.5F;

        if (this.getOwner() instanceof LivingEntity owner) {
            MobEffectInstance strength = owner.getEffect(MobEffects.DAMAGE_BOOST);
            if (strength != null) {
                int amplifier = strength.getAmplifier();
                damage += 3.0F * (amplifier + 1);
            }
            MobEffectInstance weakness = owner.getEffect(MobEffects.WEAKNESS);
            if (weakness != null) {
                int amplifier = weakness.getAmplifier();
                damage -= 4.0F * (amplifier + 1);
            }

        }

        return damage;
    }
    public ToolStack getTool() {
        return this.toolStack;
    }

    private boolean shouldIgnoreEntity(Entity entity) {
        return piercedEntityIds.contains(entity.getId());
    }

    private void markEntityPierced(Entity entity) {
        piercedEntityIds.add(entity.getId());
    }

    private void recordKilledEntity(Entity entity) {
        if (!entity.isAlive()) {
            killedEntities.add(entity);
        }
    }
    private void clearPiercingData() {
        piercedEntityIds.clear();
        killedEntities.clear();
    }
    private boolean isPiercingLimitReached() {
        return getPierceLevel() > 0 && piercedEntityIds.size() >= getPierceLevel() + 1;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Tool", arrowStack.serializeNBT());
        tag.putFloat("BonusDamage", bonusDamage);
        tag.putBoolean("Critical", critical);
    }
    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setTool(ItemStack.of(tag.getCompound("Tool")));
        this.bonusDamage = tag.getFloat("BonusDamage");
        this.critical = tag.getBoolean("Critical");
    }

    @Override
    public boolean tryPickup(@NotNull Player player) {
        return false;
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
    protected void onHitEntity(EntityHitResult result) {
        Entity target = result.getEntity();
        if (isPiercingLimitReached()) {
            discard();
            return;
        }

        markEntityPierced(target);
        if (target instanceof LivingEntity livingTarget) {
            handleLivingTargetHit(livingTarget, result);
        } else {
            handleNonLivingTargetHit();
        }
    }

    private void handleLivingTargetHit(LivingEntity target, EntityHitResult result) {
        float prevHealth = target.getHealth();
        boolean attackSuccess = attemptAttack(target);

        if (attackSuccess) {
            recordKilledEntity(target);
            triggerAchievementsIfNeeded();
            playHitSound();
        } else {
            revertTargetHealth(target, prevHealth);
            bounceArrow();
        }

        tryDiscardIfNoPierce();
    }

    private boolean attemptAttack(LivingEntity target) {
        return ProjectileUtils.arrowAttackEntity(
                arrowStack.getItem(),
                this,
                toolStack,
                (LivingEntity) getOwner(),
                target,
                (float) getDeltaMovement().length(),
                getDamage(),
                false
        );
    }

    private void triggerAchievementsIfNeeded() {
        if (level().isClientSide || !isCritArrow()) return;

        Entity shooter = getOwner();
        if (shooter instanceof ServerPlayer serverPlayer) {
            if (!killedEntities.isEmpty()) {
                CriteriaTriggers.KILLED_BY_CROSSBOW.trigger(serverPlayer, killedEntities);
            }
        }
    }

    private void revertTargetHealth(LivingEntity target, float prevHealth) {
        target.setHealth(prevHealth);
    }

    private void bounceArrow() {
        setDeltaMovement(getDeltaMovement().scale(-0.1));
        setYRot(getYRot() + 180.0F);
        yRotO += 180.0F;
    }

    private void tryDiscardIfNoPierce() {
        if (getPierceLevel() <= 0) {
            discard();
        }
    }

    private void handleNonLivingTargetHit() {
        playHitSound();
        discard();
    }

    private void playHitSound() {
        if (!this.isSilent()) {
            this.level().playSound(
                    (Player) this.getOwner(),
                    this.getX(), this.getY(), this.getZ(),
                    SoundEvents.ARROW_HIT,
                    this.getSoundSource(),
                    1.0F,// 音量
                    1.2F / (this.random.nextFloat() * 0.2F + 0.8F)
            );
        }
    }

    @Override
    protected boolean canHitEntity(Entity entity) {
        return super.canHitEntity(entity) && !shouldIgnoreEntity(entity);
    }

    @Override
    public @NotNull ItemStack getPickupItem() {
        return this.arrowStack;
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf buffer) {
        buffer.writeItem(this.arrowStack);
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
}
