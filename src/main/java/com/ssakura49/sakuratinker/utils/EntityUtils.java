package com.ssakura49.sakuratinker.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.List;

public class EntityUtils {
    public EntityUtils() {
    }

    public static List<LivingEntity> getMonsters(LivingEntity center, int range) {
        return center.level().getEntitiesOfClass(LivingEntity.class, center.getBoundingBox().inflate((double)range), (entity) -> entity instanceof Monster);
    }

    public static List<LivingEntity> getExceptForCenterMonsters(LivingEntity center, int range) {
        List<LivingEntity> entities = center.level().getEntitiesOfClass(LivingEntity.class, center.getBoundingBox().inflate((double)range), (entity) -> entity instanceof Monster);
        entities.remove(center);
        return entities;
    }

    public static void spawnThunder(BlockPos pos, Level level) {
        LightningBolt lightningbolt = (LightningBolt)EntityType.LIGHTNING_BOLT.create(level);
        if (lightningbolt != null) {
            lightningbolt.moveTo(Vec3.atBottomCenterOf(pos));
            level.addFreshEntity(lightningbolt);
        }

    }

    public static void spawnThunder(Entity target) {
        spawnThunder(target.getOnPos(), target.level());
    }

    public static boolean isEnderEntity(LivingEntity entity) {
        return entity instanceof EnderDragon || entity instanceof Endermite || entity instanceof Shulker || entity instanceof EnderMan;
    }

    public static boolean isInTheSun(LivingEntity entity) {
        return entity.level().canSeeSky(entity.getOnPos()) && entity.level().isDay();
    }

    public static DamageSource getMobOrPlayerSource(LivingEntity entity) {
        if (entity instanceof Player player) {
            return entity.damageSources().playerAttack(player);
        } else {
            return entity.damageSources().mobAttack(entity);
        }
    }

    public static void setFullAttackCooldown(Player player) {
        player.resetAttackStrengthTicker();
    }

    public static LocalPlayer getClientPlayer() {
        return Minecraft.getInstance().player;
    }

    public static boolean isEmptyInHand(LivingEntity entity) {
        return entity.getMainHandItem().isEmpty() && entity.getOffhandItem().isEmpty();
    }

    public static void modifyArrowSpeed(AbstractArrow arrow, double value) {
        arrow.setDeltaMovement(arrow.getDeltaMovement().scale(value));
    }

    public static boolean isCanDeath(ToolStack tool, LivingDamageEvent event) {
        return tool != null && !event.isCanceled();
    }

    public static boolean isFullChance(Player player) {
        return player.getAttackStrengthScale(0.5F) > 0.9F;
    }

    public static int getEffectLevel(LivingEntity entity, MobEffect effect) {
        MobEffectInstance instance = entity.getEffect(effect);
        return instance == null ? -1 : instance.getAmplifier();
    }

    public static float getMissHp(LivingEntity entity) {
        return entity.getMaxHealth() - entity.getHealth();
    }

    public static float getPerMissHp(LivingEntity entity) {
        return 1.0F - entity.getHealth() / entity.getMaxHealth();
    }

    public static void addEffect(LivingEntity entity, MobEffect effect, int time, int level) {
        entity.addEffect(new MobEffectInstance(effect, time, level));
    }

    public static void addEffect(LivingEntity entity, MobEffect effect, int time) {
        addEffect(entity, effect, time, 0);
    }

    public static int getHarmfulEffectCount(LivingEntity entity) {
        return (int)entity.getActiveEffects().stream().filter((e) -> e.getEffect().getCategory() == MobEffectCategory.HARMFUL).count();
    }

    public static int getBeneficialEffectCount(LivingEntity entity) {
        return (int)entity.getActiveEffects().stream().filter((e) -> e.getEffect().getCategory() == MobEffectCategory.BENEFICIAL).count();
    }

    public static boolean isJumping() {
        return Minecraft.getInstance().options.keyJump.isDown();
    }

    public static boolean isLookingBehindTarget(LivingEntity target, Vec3 attackerLocation) {
        if (attackerLocation != null) {
            Vec3 lookingVector = target.getViewVector(1.0F);
            Vec3 attackAngleVector = attackerLocation.subtract(target.position()).normalize();
            attackAngleVector = new Vec3(attackAngleVector.x, (double)0.0F, attackAngleVector.z);
            return attackAngleVector.dot(lookingVector) < (double)-0.5F;
        } else {
            return false;
        }
    }
}
