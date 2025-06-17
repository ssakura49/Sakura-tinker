package com.ssakura49.sakuratinker.network.packet;

import com.ssakura49.sakuratinker.utils.ModListUtil;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SyncManaPacket {
    private final int mana;

    public SyncManaPacket(FriendlyByteBuf buf) {
        this.mana = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(mana);
    }

    public void handle(Supplier<NetworkEvent.Context> contextSupplier) {
        if (ModListUtil.ISSLoaded) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {ClientMagicData.setMana(mana);});
            context.setPacketHandled(true);
        }
    }

    public SyncManaPacket(int mana) {
        this.mana = mana;
    }
}
