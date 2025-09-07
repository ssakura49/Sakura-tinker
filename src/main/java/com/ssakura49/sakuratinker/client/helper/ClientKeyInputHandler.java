package com.ssakura49.sakuratinker.client.helper;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.network.PacketHandler;
import com.ssakura49.sakuratinker.network.c2s.ModifierKeyPressPacket;
import com.ssakura49.sakuratinker.register.STKeys;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, value = Dist.CLIENT)
public class ClientKeyInputHandler {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null || mc.screen != null) return;
        if (STKeys.getSummonKey().consumeClick()) {
            PacketHandler.sendToServer(new ModifierKeyPressPacket(STKeys.SUMMON_KEY_ID));
        }
        if (STKeys.getTreasureToggleLootKey().consumeClick()) {
            PacketHandler.sendToServer(new ModifierKeyPressPacket(STKeys.TREASURE_TOGGLE_LOOT_KEY_ID));
        }
        if (STKeys.getBloodBurnKey().consumeClick()) {
            PacketHandler.sendToServer(new ModifierKeyPressPacket(STKeys.BLOOD_BURN_KEY_ID));
        }
        if (STKeys.getSelectedBullet().consumeClick()) {
            PacketHandler.sendToServer(new ModifierKeyPressPacket(STKeys.SELECTED_BULLET_KEY_ID));
        }
    }
}
