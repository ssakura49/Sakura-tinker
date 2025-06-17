package com.ssakura49.sakuratinker.client.event;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.*;
import com.mojang.blaze3d.systems.RenderSystem;
import com.ssakura49.sakuratinker.register.STModifiers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableBowItem;
import slimeknights.tconstruct.library.tools.nbt.ToolStack;

public class BowDrawOverlayEvent {
    private static final ResourceLocation[] DRAW_ICONS = new ResourceLocation[] {
            new ResourceLocation("sakuratinker:textures/gui/bows/draw_bow_1.png"),
            new ResourceLocation("sakuratinker:textures/gui/bows/draw_bow_2.png"),
            new ResourceLocation("sakuratinker:textures/gui/bows/draw_bow_3.png"),
            new ResourceLocation("sakuratinker:textures/gui/bows/draw_bow_4.png"),
            new ResourceLocation("sakuratinker:textures/gui/bows/draw_bow_5.png")
    };

    public static void renderDraw(RenderGuiOverlayEvent.Pre event) {
        Minecraft mc = Minecraft.getInstance();

        if (event.getOverlay() == VanillaGuiOverlay.CROSSHAIR.type()) {
            Gui gui = mc.gui;
            if (gui instanceof ForgeGui && mc.gameMode != null && !mc.options.hideGui) {
                Entity view = mc.getCameraEntity();
                if (!(view instanceof Player player)) return;

                if (!player.isUsingItem()) return;

                ItemStack stack = player.getUseItem();
                if (!(stack.getItem() instanceof ModifiableBowItem)) return;

                ToolStack tool = ToolStack.from(stack);
                if (tool.isBroken()) return;

                float drawProgress = (stack.getUseDuration() - player.getUseItemRemainingTicks()) / 20.0f;
                float normalized = Math.min(drawProgress, 1.0f);
                int stage = Math.min((int) (normalized * 5.0f), 4);

                GuiGraphics graphics = event.getGuiGraphics();
                int width = event.getWindow().getGuiScaledWidth();
                int height = event.getWindow().getGuiScaledHeight();
                int x = width / 2 - 8;
                int y = height / 2 - 8;

                RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
                RenderSystem.defaultBlendFunc();
                RenderSystem.enableBlend();
                RenderSystem.setShaderTexture(0, DRAW_ICONS[stage]);

                graphics.blit(DRAW_ICONS[stage], x, y, 0, 0, 16, 16, 256, 256);

                RenderSystem.disableBlend();

                event.setCanceled(true);
            }
        }
    }

    private static float getPulseAlpha(int remainingTicks) {
        return 0.7f + 0.3f * (float)Math.sin(remainingTicks * 0.2f);
    }
}
