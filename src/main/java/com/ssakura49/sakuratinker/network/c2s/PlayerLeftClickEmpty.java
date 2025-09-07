package com.ssakura49.sakuratinker.network.c2s;

import com.ssakura49.sakuratinker.library.hooks.click.LeftClickModifierHook;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PlayerLeftClickEmpty {
    public PlayerLeftClickEmpty(){}
    public PlayerLeftClickEmpty(FriendlyByteBuf buf){
    }

    public void toByte(FriendlyByteBuf buf){
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier){
        NetworkEvent.Context context =supplier.get();
        ServerPlayer serverPlayer = context.getSender();
        if (serverPlayer!=null) {
            ItemStack stack =serverPlayer.getItemInHand(serverPlayer.getUsedItemHand());
            EquipmentSlot slot = stack.getEquipmentSlot();
            context.enqueueWork(() -> {
                LeftClickModifierHook.handleLeftClick(stack,serverPlayer,slot);
            });
        }
        return true;
    }
}
