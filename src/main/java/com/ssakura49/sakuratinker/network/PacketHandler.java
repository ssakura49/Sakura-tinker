package com.ssakura49.sakuratinker.network;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.network.c2s.ModifierKeyPressPacket;
import com.ssakura49.sakuratinker.network.c2s.PlayerLeftClickEmpty;
import com.ssakura49.sakuratinker.network.s2c.CollectedDropsSync;
import com.ssakura49.sakuratinker.network.s2c.SLightningPacket;
import com.ssakura49.sakuratinker.network.s2c.SyncManaPacket;
import com.ssakura49.sakuratinker.network.s2c.timestop.TSDimensionSynchedPacket;
import com.ssakura49.sakuratinker.network.s2c.timestop.TimeStopSkillPacket;
import com.ssakura49.sakuratinker.utils.SafeClassUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder
            .named(ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "main"))
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(PROTOCOL_VERSION::equals)
            .serverAcceptedVersions(PROTOCOL_VERSION::equals)
            .simpleChannel();
    private static int id = 0;

    public static void init() {
        INSTANCE.messageBuilder(PlayerLeftClickEmpty.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(PlayerLeftClickEmpty::new).encoder(PlayerLeftClickEmpty::toByte).consumerMainThread(PlayerLeftClickEmpty::handle).add();
        if (SafeClassUtil.ISSLoaded) {
            INSTANCE.messageBuilder(SyncManaPacket.class, id++, NetworkDirection.PLAY_TO_CLIENT).decoder(SyncManaPacket::new).encoder(SyncManaPacket::toBytes).consumerMainThread(SyncManaPacket::handle).add();
        }
        INSTANCE.messageBuilder(ModifierKeyPressPacket.class, id++, NetworkDirection.PLAY_TO_SERVER)
                .decoder(ModifierKeyPressPacket::new)
                .encoder(ModifierKeyPressPacket::toBytes)
                .consumerMainThread(ModifierKeyPressPacket::handle)
                .add();
        INSTANCE.messageBuilder(CollectedDropsSync.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .decoder(CollectedDropsSync::new)
                .encoder(CollectedDropsSync::encode)
                .consumerNetworkThread(CollectedDropsSync::handle)
                .add();
        INSTANCE.registerMessage(id++, TimeStopSkillPacket.class, TimeStopSkillPacket::encode, TimeStopSkillPacket::decode, TimeStopSkillPacket::handle);
        INSTANCE.registerMessage(id++, TSDimensionSynchedPacket.class, TSDimensionSynchedPacket::encode, TSDimensionSynchedPacket::decode, TSDimensionSynchedPacket::handle);
        INSTANCE.registerMessage(id++, SLightningPacket.class, SLightningPacket::encode, SLightningPacket::decode, SLightningPacket::consume);
    }

    public static <MSG> void sendToServer(MSG msg){
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(MSG msg, ServerPlayer player){
        INSTANCE.send(PacketDistributor.PLAYER.with(()->player),msg);
    }

    public static <MSG> void sendToClient(MSG msg){
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

    public static <MSG> void sendToAll(MSG msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

    public static <MSG> void sentToTrackingChunk(LevelChunk chunk, MSG msg) {
        INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), msg);
    }

    public static <MSG> void sentToTrackingEntity(Entity entity, MSG msg) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), msg);
    }

    public static <MSG> void sentToTrackingEntityAndPlayer(Entity entity, MSG msg) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), msg);
    }

    public static <MSG> void sendToClient(ServerPlayer player, MSG msg) {
        INSTANCE.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
    }
}
