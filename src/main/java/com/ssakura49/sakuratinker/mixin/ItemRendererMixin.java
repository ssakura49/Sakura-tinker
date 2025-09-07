package com.ssakura49.sakuratinker.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.client.baked.model.PerspectiveModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {

    @Unique
    ItemStack sakurashader$stack;
    @Unique
    VertexConsumer sakurashader$vertexConsumer;
    @Shadow
    @Final
    private Minecraft minecraft;

    @Inject(
            method = "render",
            at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V", ordinal = 0)
    )
    public void onRenderItem(ItemStack stack, ItemDisplayContext context, boolean leftHand, PoseStack mStack, MultiBufferSource buffers, int packedLight, int packedOverlay, BakedModel modelIn, CallbackInfo ci) {
        if (modelIn instanceof PerspectiveModel iItemRenderer) {
            mStack.pushPose();
            final PerspectiveModel renderer = (PerspectiveModel) iItemRenderer.applyTransform(context, mStack, leftHand);
            mStack.translate(-0.5D, -0.5D, -0.5D);
            renderer.renderItem(stack, context, mStack, buffers, packedLight, packedOverlay);
            mStack.popPose();
        }
    }

}