package com.ssakura49.sakuratinker.network.handler;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.network.packet.PlayerLeftClickEmpty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE ;
    static int id = 0;

    public static void init() {
        INSTANCE = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(SakuraTinker.MODID, "sakura_msg")).networkProtocolVersion(() -> "1").clientAcceptedVersions(s -> true).serverAcceptedVersions(s -> true).simpleChannel();
        INSTANCE.messageBuilder(PlayerLeftClickEmpty.class,id++, NetworkDirection.PLAY_TO_SERVER).decoder(PlayerLeftClickEmpty::new).encoder(PlayerLeftClickEmpty::toByte).consumerMainThread(PlayerLeftClickEmpty::handle).add();
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
}
