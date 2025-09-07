package com.ssakura49.sakuratinker.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssakura49.sakuratinker.event.custom.STRenderTooltipEvent;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = GuiGraphics.class, priority = 1001)
public abstract class GuiGraphicsMixin {

    @Shadow
    public PoseStack pose;

    @Shadow
    public MultiBufferSource.BufferSource bufferSource;

    @Shadow(remap = false)
    public ItemStack tooltipStack;

    @Shadow
    public abstract int guiWidth();

    @Shadow
    public abstract int guiHeight();

    @Shadow
    public abstract PoseStack pose();

    @Shadow
    @Deprecated
    public abstract void flushIfUnmanaged();


    @Inject(method = "renderTooltipInternal", at = @At(value = "INVOKE", target = "Lnet/minecraftforge/client/ForgeHooksClient;onRenderTooltipPre(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/client/gui/GuiGraphics;IIIILjava/util/List;Lnet/minecraft/client/gui/Font;Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipPositioner;)Lnet/minecraftforge/client/event/RenderTooltipEvent$Pre;", remap = false), cancellable = true)
    private void renderTooltipInternal(Font font, List<ClientTooltipComponent> pComponents, int pMouseX, int pMouseY, ClientTooltipPositioner pTooltipPositioner, CallbackInfo ci) {
        STRenderTooltipEvent.PrePre preEvent = new STRenderTooltipEvent.PrePre(this.tooltipStack, (GuiGraphics) (Object) this, pMouseX, pMouseY, guiWidth(), guiHeight(), ForgeHooksClient.getTooltipFont(tooltipStack, font), pComponents, pTooltipPositioner, this.pose());
        MinecraftForge.EVENT_BUS.post(preEvent);
        if (preEvent.isCanceled()) ci.cancel();
    }

    @Inject(method = "renderTooltipInternal", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V", shift = At.Shift.AFTER))
    private void renderTooltipInternal_post(Font font, List<ClientTooltipComponent> pComponents, int pMouseX, int pMouseY, ClientTooltipPositioner pTooltipPositioner, CallbackInfo ci) {
        STRenderTooltipEvent.Post event = new STRenderTooltipEvent.Post(this.tooltipStack, (GuiGraphics) (Object) this, pMouseX, pMouseY, font, pComponents, pTooltipPositioner);
        MinecraftForge.EVENT_BUS.post(event);
    }
}