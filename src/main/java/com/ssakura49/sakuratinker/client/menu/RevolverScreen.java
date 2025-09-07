package com.ssakura49.sakuratinker.client.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class RevolverScreen extends AbstractContainerScreen<RevolverMenu> {
    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath("sakuratinker","textures/gui/revolver_gui.png");

    public RevolverScreen(RevolverMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 132;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        //RenderSystem.setShaderTexture(0, GUI_TEXTURE);
        guiGraphics.blit(GUI_TEXTURE, leftPos, topPos,
                0, 0,
                imageWidth, imageHeight,
                176, 132);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // 移动物品栏名字位置
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 8, 40, 4210752, false);
        guiGraphics.drawString(this.font, this.title, 8, 6, 4210752, false);
    }
}
