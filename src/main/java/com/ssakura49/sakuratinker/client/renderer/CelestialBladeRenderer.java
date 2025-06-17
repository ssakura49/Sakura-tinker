package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.content.entity.ShurikenEntity;
import com.ssakura49.sakuratinker.register.STItems;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import com.ssakura49.sakuratinker.content.entity.CelestialBladeProjectile;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import slimeknights.tconstruct.library.tools.item.ranged.ModifiableLauncherItem;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class CelestialBladeRenderer extends EntityRenderer<CelestialBladeProjectile> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("sakuratinker", "textures/entity/celestial_blade.png");
    private final ItemRenderer itemRenderer;
//    private final Random random = new Random();
//
//    private static final Item[] WEAPON_MODELS = {
//            Items.GOLDEN_SWORD,
//            Items.DIAMOND_SWORD,
//            Items.NETHERITE_SWORD,
//            Items.TRIDENT
//    };
    public CelestialBladeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
//        this.shadowRadius = 0.15F;
//        this.shadowStrength = 0.5F;
    }

    @Override
    public void render(CelestialBladeProjectile entity, float entityYaw, float partialTicks,
                       PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        VertexConsumer trailConsumer = buffer.getBuffer(RenderType.entityCutout(TEXTURE));
        renderTrail(entity, poseStack, trailConsumer, packedLight);

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot() - 45.0F));

        float progress = (entity.getTickCount() + partialTicks) / entity.getLife();
        float tiltAngle = calculateTiltAngle(progress);
        if (progress < 0.5f) {
            poseStack.mulPose(Axis.ZP.rotationDegrees(tiltAngle));
        } else  {
            poseStack.mulPose(Axis.XP.rotationDegrees(tiltAngle));
        }

        poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
        poseStack.scale(2.0F, 2.0F, 2.0F);
        Entity ownerEntity = entity.getOwner();
        if (ownerEntity instanceof LivingEntity owner) {
            this.itemRenderer.renderStatic(
                    this.getRenderItem(owner),
                    ItemDisplayContext.GROUND,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    buffer,
                    entity.level(),
                    entity.getId()
            );
        } else {
            this.itemRenderer.renderStatic(
                    ((ModifiableItem)STItems.blade_convergence.get()).getRenderTool(),
                    ItemDisplayContext.GROUND,
                    packedLight,
                    OverlayTexture.NO_OVERLAY,
                    poseStack,
                    buffer,
                    entity.level(),
                    entity.getId()
            );
        }

        poseStack.popPose();
    }

    private float calculateTiltAngle(float progress) {
        float angle;
        if (progress < 0.5f) {
            angle = 10.0f * (1 - progress * 2);
        } else {
            angle = -10.0f * ((progress - 0.5f) * 2);
        }
        return angle;
    }

    private void renderTrail(CelestialBladeProjectile entity, PoseStack poseStack, VertexConsumer consumer, int packedLight) {
        float progress = (float)entity.getTickCount() / entity.getLife();
        if (progress > 1.0F) progress = 1.0F;

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(-entity.getYRot() - 90.0F));

        PoseStack.Pose pose = poseStack.last();
        Matrix4f matrix4f = pose.pose();
        Matrix3f matrix3f = pose.normal();

        float width = 0.4F * (1.0F - progress);
        float length = 1.5F * progress;
        float alpha = 0.7F * (1.0F - progress * 0.5F);

        int color = 0xFF9900FF;

        vertex(matrix4f, matrix3f, consumer, -width, 0, 0, 0, 1, color, alpha, packedLight);
        vertex(matrix4f, matrix3f, consumer, width, 0, 0, 1, 1, color, alpha, packedLight);
        vertex(matrix4f, matrix3f, consumer, width, 0, length, 1, 0, color, alpha, packedLight);
        vertex(matrix4f, matrix3f, consumer, -width, 0, length, 0, 0, color, alpha, packedLight);

        poseStack.popPose();
    }

    private void vertex(Matrix4f matrix, Matrix3f normal, VertexConsumer consumer,
                        float x, float y, float z, float u, float v, int color, float alpha, int light) {
        consumer.vertex(matrix, x, y, z)
                .color(
                        (color >> 16) & 0xFF,
                        (color >> 8) & 0xFF,
                        color & 0xFF,
                        (int)(alpha * 255))
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(light)
                .normal(normal, 0, 1, 0)
                .endVertex();
    }

    private ItemStack getRenderItem(LivingEntity owner) {
        if (owner.getMainHandItem().is(STItems.blade_convergence.get())) {
            return owner.getMainHandItem();
        } else if (owner.getOffhandItem().is(STItems.blade_convergence.get())) {
            return owner.getOffhandItem();
        } else {
            return ((ModifiableItem)STItems.blade_convergence.get()).getRenderTool();
        }
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CelestialBladeProjectile entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
