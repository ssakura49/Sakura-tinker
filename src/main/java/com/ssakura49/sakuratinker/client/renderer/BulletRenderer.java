package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssakura49.sakuratinker.common.entity.BulletEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class BulletRenderer extends EntityRenderer<BulletEntity> {
    public BulletRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(BulletEntity entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // 什么都不渲染
    }

    @Override
    public ResourceLocation getTextureLocation(BulletEntity entity) {
        return ResourceLocation.withDefaultNamespace("textures/item/iron_ingot.png");
    }
}
