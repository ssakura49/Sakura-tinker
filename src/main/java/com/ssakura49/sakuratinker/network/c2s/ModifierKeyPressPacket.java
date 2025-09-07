package com.ssakura49.sakuratinker.network.c2s;

import com.ssakura49.sakuratinker.library.hooks.click.KeyPressModifierHook;
import com.ssakura49.sakuratinker.utils.tinker.ToolUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

import java.util.function.Supplier;

public class ModifierKeyPressPacket {
    private final String keyId;

    public ModifierKeyPressPacket(String keyId) {
        this.keyId = keyId;
    }

    public ModifierKeyPressPacket(FriendlyByteBuf buf) {
        this.keyId = buf.readUtf();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeUtf(this.keyId);
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null) {
                for (InteractionHand hand : InteractionHand.values()) {
                    ItemStack stack = player.getItemInHand(hand);
                    processItemStack(stack, player);
                }

                for (ItemStack armor : player.getArmorSlots()) {
                    processItemStack(armor, player);
                }

                for (ItemStack curio : ToolUtil.Curios.getStacks(player)){
                    processItemStack(curio,player);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    private void processItemStack(ItemStack stack, Player player) {
        if (ToolUtil.checkTool(stack)) {
            ToolStack tool = ToolStack.from(stack);
            for (ModifierEntry entry : tool.getModifierList()) {
                if (entry.getModifier() instanceof KeyPressModifierHook hook) {
                    hook.onKeyPress(tool, entry, player, this.keyId);
                }
            }
        }
    }
}
