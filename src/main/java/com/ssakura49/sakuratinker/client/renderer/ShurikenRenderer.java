package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.content.entity.ShurikenEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;

public class ShurikenRenderer extends EntityRenderer<ShurikenEntity> {
    private final ItemRenderer itemRenderer;

    public ShurikenRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(ShurikenEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.tickCount >= 2 || !(this.entityRenderDispatcher.distanceToSqr(entity) < 12.25F)) {
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            if (!entity.isInGround()) {
                poseStack.mulPose(Axis.ZP.rotationDegrees(-(entity.tickCount + partialTicks) * 30 % 360));
            } else {
                poseStack.mulPose(Axis.ZP.rotationDegrees(0));
            }
            poseStack.translate(-0.03125, -0.09375, 0);
            this.itemRenderer.renderStatic(
                    entity.getItem(),
                    ItemDisplayContext.GROUND,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    buffer,
                    entity.level(),
                    entity.getId());
            poseStack.popPose();
        }
    }

    @Override
    public ResourceLocation getTextureLocation(ShurikenEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
