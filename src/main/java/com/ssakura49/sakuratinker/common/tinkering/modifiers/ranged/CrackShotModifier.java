package com.ssakura49.sakuratinker.common.tinkering.modifiers.ranged;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.generic.BaseModifier;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ModDataNBT;
import slimeknights.tconstruct.library.tools.nbt.ModifierNBT;

import java.util.List;
import java.util.UUID;

public class CrackShotModifier extends BaseModifier {
    private static final ResourceLocation NBT_TARGET_ID = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID,"crack_shot_target");
    private void setTarget(ModDataNBT data, @Nullable Entity entity) {
        if (entity != null) {
            data.putInt(NBT_TARGET_ID, entity.getId());
        } else {
            data.remove(NBT_TARGET_ID);
        }
    }
    @Nullable
    private Entity getTarget(ModDataNBT data, Level level) {
        if (data.contains(NBT_TARGET_ID)) {
            int id = data.getInt(NBT_TARGET_ID);
            return level.getEntity(id);
        }
        return null;
    }
    @Nullable
    private LivingEntity findNewTarget(Level level, AbstractArrow arrow, @Nullable UUID ownerUUID) {
        Vec3 pos = arrow.position();
        Vec3 motion = arrow.getDeltaMovement().normalize();

        AABB searchBox = new AABB(pos, pos).inflate(5.0, 2.0, 5.0);
        List<LivingEntity> candidates = level.getEntitiesOfClass(LivingEntity.class, searchBox,
                e -> e.isAlive() && e != arrow.getOwner() && !(e instanceof TamableAnimal t && t.getOwnerUUID() == ownerUUID));

        double bestDot = -1.0;
        LivingEntity bestTarget = null;

        for (LivingEntity entity : candidates) {
            Vec3 toTarget = entity.position().subtract(pos).normalize();
            double dot = motion.dot(toTarget);

            if (dot > bestDot && dot > 0.5 && hasLineOfSight(arrow,entity)) {
                bestDot = dot;
                bestTarget = entity;
            }
        }

        return bestTarget;
    }
    public static boolean hasLineOfSight(Entity from, Entity to) {
        Level level = from.level();
        Vec3 start = from.getEyePosition();
        Vec3 end = to.position().add(0, to.getBbHeight() * 0.5, 0); // 中心点 or 眼睛
        HitResult result = level.clip(new ClipContext(
                start, end,
                ClipContext.Block.COLLIDER,
                ClipContext.Fluid.NONE,
                from));

        return result.getType() == HitResult.Type.MISS;
    }
    @Override
    public void modifierArrowTick(ModifierNBT modifiers, ModifierEntry entry, Level level, AbstractArrow arrow,
                                  ModDataNBT persistentData, boolean hasBeenShot, boolean leftOwner, boolean inGround,
                                  @Nullable IntOpenHashSet piercingIgnoreEntityIds) {
        if (!leftOwner || inGround || level.isClientSide()) return;
        LivingEntity owner = (LivingEntity) arrow.getOwner();
        UUID uuid = owner == null ? null : owner.getUUID();
        Entity target = getTarget(persistentData, level);
        if (target == null || !target.isAlive() || arrow.distanceToSqr(target) > 100) {
            target = findNewTarget(level, arrow, uuid);
            if (target != null) setTarget(persistentData, target);
        }
        if (target != null) {
            Vec3 motion = arrow.getDeltaMovement();
            Vec3 toTarget = new Vec3(target.getX() - arrow.getX(),
                    target.getY(0.5) - arrow.getY(),
                    target.getZ() - arrow.getZ());
            double dot = motion.normalize().dot(toTarget.normalize());
            if (dot > 0.5) {
                Vec3 newMotion = motion.add(toTarget.normalize().scale(0.3)).normalize().scale(motion.length());
                arrow.setDeltaMovement(newMotion.add(0, 0.045, 0));
                arrow.hurtMarked = true;
            } else {
                setTarget(persistentData, null); // 放弃目标
            }
        }
    }

}
