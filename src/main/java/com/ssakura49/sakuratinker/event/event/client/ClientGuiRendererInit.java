package com.ssakura49.sakuratinker.event.event.client;

import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.event.BattleFlagChargeOverlayEvent;
import com.ssakura49.sakuratinker.client.event.BowDrawOverlayEvent;
import com.ssakura49.sakuratinker.utils.SafeClassUtil;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SakuraTinker.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientGuiRendererInit {
    public ClientGuiRendererInit(){}

    @SubscribeEvent
    public static void onRenderGUI(RenderGuiOverlayEvent.Pre event) {
        if (!SafeClassUtil.TinkersCalibrationLoaded) {
            BowDrawOverlayEvent.renderDraw(event);
        }
        BattleFlagChargeOverlayEvent.renderChargeIndicator(event);
    }

    public static void init() {
    }
}
