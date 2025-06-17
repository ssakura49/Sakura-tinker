package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.ssakura49.sakuratinker.content.entity.ManaRayEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class ManaRayRenderer extends EntityRenderer<ManaRayEntity> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("sakuratinker", "textures/entity/celestial_blade.png");
    public ManaRayRenderer(EntityRendererProvider.Context context) {
        super(context);
    }
    @Override
    public void render(ManaRayEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
    @Override
    public ResourceLocation getTextureLocation(ManaRayEntity entity) {
        return TEXTURE; // 不需要纹理
    }
}
