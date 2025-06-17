package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.content.entity.MiniGrapplingHookEntity;
import com.ssakura49.sakuratinker.register.STItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class MiniGrapplingHookRenderer extends EntityRenderer<MiniGrapplingHookEntity> {
    private final ItemRenderer itemRenderer;

    public MiniGrapplingHookRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(MiniGrapplingHookEntity entity, float yaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int light) {
        poseStack.pushPose();

        float lerpYRot = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        float lerpXRot = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());

        poseStack.mulPose(Axis.YP.rotationDegrees(lerpYRot));

        poseStack.mulPose(Axis.XP.rotationDegrees(-lerpXRot));

        poseStack.mulPose(Axis.YP.rotationDegrees(-90F));

        poseStack.mulPose(Axis.ZP.rotationDegrees(-45F));

        poseStack.mulPose(Axis.YP.rotationDegrees(180F));

        ItemStack stack = new ItemStack(STItems.grappling_blade.get());
        BakedModel model = itemRenderer.getModel(stack, entity.level(), null, entity.getId());
        itemRenderer.render(
                stack,
                ItemDisplayContext.GROUND,
                false,
                poseStack,
                buffer,
                light,
                OverlayTexture.NO_OVERLAY,
                model
        );

        poseStack.popPose();
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull MiniGrapplingHookEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
