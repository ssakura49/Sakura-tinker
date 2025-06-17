package com.ssakura49.sakuratinker.client.event;

import com.mojang.blaze3d.systems.RenderSystem;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.content.tools.item.BattleFlagItem;
import com.ssakura49.sakuratinker.library.tinkering.tools.STToolStats;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class BattleFlagChargeOverlayEvent {
    private static final ResourceLocation[] CHARGE_ICONS = new ResourceLocation[] {
            new ResourceLocation(SakuraTinker.MODID, "textures/gui/battle_flag/charge_1.png"),
            new ResourceLocation(SakuraTinker.MODID, "textures/gui/battle_flag/charge_2.png"),
            new ResourceLocation(SakuraTinker.MODID, "textures/gui/battle_flag/charge_3.png"),
            new ResourceLocation(SakuraTinker.MODID, "textures/gui/battle_flag/charge_4.png"),
            new ResourceLocation(SakuraTinker.MODID, "textures/gui/battle_flag/charge_5.png")
    };

    private static final float[] ATTACK_COLOR = {1.0f, 0.2f, 0.1f}; // 红黑
    private static final float[] DEFENCE_COLOR = {0.3f, 0.6f, 1.0f}; // 蓝白

    public static void renderChargeIndicator(RenderGuiOverlayEvent.Pre event) {
        if (event.getOverlay() != VanillaGuiOverlay.CROSSHAIR.type()) return;
        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null || !player.isUsingItem()) return;
        ItemStack stack = player.getUseItem();
        if (!(stack.getItem() instanceof BattleFlagItem)) return;
        ToolStack tool = ToolStack.from(stack);
        if (tool.isBroken()) return;
        boolean isAttackMode = tool.getPersistentData().getInt(BattleFlagItem.MODE_KEY) == BattleFlagItem.MODE_ATTACK;
        int maxDuration = stack.getUseDuration();
        int usedTicks = maxDuration - player.getUseItemRemainingTicks();
        int chargeUnit = tool.getStats().getInt(STToolStats.CHARGING_TIME);
        int currentStage = Math.min(usedTicks / chargeUnit + 1, 5);
        renderColoredCrosshair(event, isAttackMode, currentStage);
        event.setCanceled(true);
    }

    private static void renderColoredCrosshair(RenderGuiOverlayEvent.Pre event, boolean isAttackMode, int stage) {
        if (stage < 1 || stage > 5) return;
        GuiGraphics graphics = event.getGuiGraphics();
        int centerX = event.getWindow().getGuiScaledWidth() / 2 - 8;
        int centerY = event.getWindow().getGuiScaledHeight() / 2 - 8;
        float[] color = isAttackMode ? ATTACK_COLOR : DEFENCE_COLOR;
        float pulseAlpha = 0.7f + 0.3f * (float)Math.sin(System.currentTimeMillis() % 1000 / 500f * Math.PI);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        graphics.setColor(color[0], color[1], color[2], pulseAlpha);
        graphics.blit(CHARGE_ICONS[stage - 1], centerX, centerY, 0, 0, 16, 16, 16, 16);
        RenderSystem.disableBlend();
    }
}
