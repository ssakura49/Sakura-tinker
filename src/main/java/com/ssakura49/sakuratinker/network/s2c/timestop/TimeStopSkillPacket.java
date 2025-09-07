package com.ssakura49.sakuratinker.network.s2c.timestop;

import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.render.custom.normal.ShockWaveSkillRenderer;
import com.ssakura49.sakuratinker.utils.time.TimeStopUtilsWrapped;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkEvent;

import java.util.UUID;
import java.util.function.Supplier;

public class TimeStopSkillPacket {
    private final boolean isTimeStop;
    private final UUID user;
    private final boolean onlyRemoveEntity;
    private final boolean safelyCanCancel;
    public TimeStopSkillPacket(boolean isTimeStop, UUID user, boolean onlyRemoveEntity, boolean safelyCanCancel) {
        this.isTimeStop = isTimeStop;
        this.user = user;
        this.onlyRemoveEntity = onlyRemoveEntity;
        this.safelyCanCancel = safelyCanCancel;
        synchronized (SakuraTinker.class) {
            /*
            ModSource.areaOut(this.getClass().getName(), () -> {
                final Map<String, String> m = new HashMap<>();
                final List<String> methods = Arrays.stream(new Exception().getStackTrace()).map(StackTraceElement::getMethodName).toList();
                MUtils.safelyForEach(Arrays.stream(new Exception().getStackTrace()).map(StackTraceElement::getClassName).toList(), (s, ind) -> {
                    s = s.split("\\.")[s.split("\\.").length - 1];
                    m.put(s, methods.get(ind));
                });
                ModSource.out(m.entrySet());
            }, () -> {
                for (Field field : this.getClass().getDeclaredFields()) {
                    field.setAccessible(true);
                    try {
                        ModSource.out("%s:%s", field.getName(), field.get(this));
                    } catch (IllegalAccessException e) {
                        FantasyEndingCore.catchException(e);
                    }
                }
            });
             */
        }
    }
    public TimeStopSkillPacket(boolean isTimeStop, UUID user) {
        this(isTimeStop, user, false, true);
    }
    public static TimeStopSkillPacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new TimeStopSkillPacket(friendlyByteBuf.readBoolean(), friendlyByteBuf.readUUID(), friendlyByteBuf.readBoolean(), friendlyByteBuf.readBoolean());
    }

    public static void encode(TimeStopSkillPacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeBoolean(packet.isTimeStop);
        friendlyByteBuf.writeUUID(packet.user);
        friendlyByteBuf.writeBoolean(packet.onlyRemoveEntity);
        friendlyByteBuf.writeBoolean(packet.safelyCanCancel);
    }

    public static void handle(TimeStopSkillPacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(TimeStopSkillPacket packet, Supplier<NetworkEvent.Context> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null) System.exit(-1);
        if (mc.level.entityStorage.getEntityGetter().get(packet.user) instanceof LivingEntity living) {
            if (packet.isTimeStop) {
                TimeStopUtilsWrapped.enable();
                if (living instanceof Player) {
                    if (STConfig.Client.enable_time_stop_ball_renderer && mc.level != null) {
                        for (int i = 0; i < 3; i++) {
                            ShockWaveSkillRenderer renderer = new ShockWaveSkillRenderer(living, true);
                            renderer.scale = 1.0F - i * 0.05F;
                            renderer.enable();
                        }
                    }
                }
            } else {
                if (!packet.onlyRemoveEntity || packet.safelyCanCancel) {
                    TimeStopUtilsWrapped.disable();
                }
            }

        }
    }
}