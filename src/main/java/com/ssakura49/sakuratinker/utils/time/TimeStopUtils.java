package com.ssakura49.sakuratinker.utils.time;

import com.ssakura49.sakuratinker.data.level.TimeStopSavedData;
import com.ssakura49.sakuratinker.network.PacketHandler;
import com.ssakura49.sakuratinker.network.s2c.timestop.TSDimensionSynchedPacket;
import com.ssakura49.sakuratinker.network.s2c.timestop.TimeStopSkillPacket;
import com.ssakura49.sakuratinker.register.STSounds;
import com.ssakura49.sakuratinker.utils.data.ClientLevelExpandedContext;
import com.ssakura49.sakuratinker.utils.intfce.LevelEC;
import com.ssakura49.sakuratinker.utils.intfce.ServerEC;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

/**
 * 节省性能，在客户端判断{@link TimeStopUtils#andSameDimension(Level)}请直接调用预备好的boolean字段{@link com.ssakura49.sakuratinker.render.RendererUtils#isTimeStop_andSameDimension}
 */
public class TimeStopUtils {
    public static volatile boolean isTimeStop;
    public static boolean andSameDimension(Level level) {
        if (level == null) return false;
        if (level.isClientSide) {
            return ((ClientLevelExpandedContext) ((LevelEC) level).st$levelECData()).isCurrentTS();
        } else return ((ServerEC) ((ServerLevel) level).getServer() ).st$serverECData().timeStopDimensions.contains(level.dimension());
    }
    public static boolean andSameDimension(ResourceKey<Level> level, MinecraftServer server) {
        if (level == null) return false;
        return ((ServerEC) server).st$serverECData().timeStopDimensions.contains(level);
    }
    public static boolean canMove(Entity entity) {
        if (entity instanceof Player player) {
            if (player.isCreative() || player.isSpectator())
                return true;
            else return TimeStopEntityData.getTimeStopCount(player) > 0;
        } else if (entity instanceof LivingEntity living)
            return TimeStopEntityData.getTimeStopCount(living) > 0;
        return false;
    }
    public static synchronized void use(boolean z, LivingEntity source) {
        use(z, source, true);
    }
    /**
     * @param z 是否时停
     * @param source 实体
     * @param force 为true时无条件设置当前实体剩余时停时间0
     */
    public static synchronized void use(boolean z, LivingEntity source, boolean force) {
        use(z, source, force, 180);
    }
    /**
     * @param z 是否时停
     * @param source 实体
     * @param time 设置时停的时候同时设置剩余时间
     * @param force 为true时无条件设置当前实体剩余时停时间0
     */
    public static synchronized void use(boolean z, LivingEntity source, boolean force, int time) {
        if (source.level().isClientSide) throw new RuntimeException(("time stop should be called on server side."));
        if (!source.level().isClientSide) {
            boolean lastState = isTimeStop;

            if (z == false) {
                for (LivingEntity living : source.level.getEntitiesOfClass(LivingEntity.class, new AABB(new BlockPos(0,0,0)).inflate(30000000))) {
                    if (TimeStopEntityData.getTimeStopCount(living) > 0 && living.isAlive()) {
                        if (force)
                            TimeStopEntityData.setTimeStopCount(source, 0);
                        return;
                    }
                }
            }
            isTimeStop = z;
            if (!isTimeStop) {
                TimeStopSavedData.readOrCreate(((ServerLevel) source.level).server).removeTsDimension(source.level.dimension());
            }
            PacketHandler.sendToAll(new TimeStopSkillPacket(isTimeStop, source.getUUID()));
            if (isTimeStop)
                PacketHandler.sendToAll(new TSDimensionSynchedPacket(ResourceLocation.parse(""), source.level.dimension().location()));
            else PacketHandler.sendToAll(new TSDimensionSynchedPacket(source.level.dimension().location(), ResourceLocation.parse("")));
            if (z) {
                TimeStopSavedData.readOrCreate(((ServerLevel) source.level).server).addTsDimension(source.level.dimension());
                TimeStopEntityData.setTimeStopCount(source, TimeStopEntityData.getTimeStopCount(source) <= 1 ? time : TimeStopEntityData.getTimeStopCount(source));
            } else {
                if (force)
                    TimeStopEntityData.setTimeStopCount(source, 0);
            }

            if (!lastState && isTimeStop) {
                source.playSound(STSounds.TIME_STOP.get());
            }
        }

    }
}
