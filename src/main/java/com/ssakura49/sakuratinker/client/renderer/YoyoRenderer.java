package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.common.entity.YoyoEntity;
import com.ssakura49.sakuratinker.common.entity.base.IYoyo;
import com.ssakura49.sakuratinker.common.entity.base.RenderOrientation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.Random;
import java.util.UUID;

public class YoyoRenderer  extends EntityRenderer<YoyoEntity> {

    private final ItemRenderer itemRenderer;
    private final Random random = new Random();

    public YoyoRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public boolean shouldRender(@NotNull YoyoEntity pLivingEntity, @NotNull Frustum pCamera, double pCamX, double pCamY, double pCamZ) {
        return true;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull YoyoEntity yoyo) {
        return InventoryMenu.BLOCK_ATLAS;
    }


    @Override
    public void render(YoyoEntity entity, float pEntityYaw, float partialTick, PoseStack stack, @NotNull MultiBufferSource bufferSource, int packedLight) {
        stack.pushPose();
        //stack.translate(entity.getX(),entity.getY() + entity.getBbHeight() / 2,entity.getZ());
        stack.scale(0.5f,0.5f,0.5f);
        Vec3 pointTo = entity.getPlayerHandPos(partialTick).subtract(entity.getX(), entity.getY(), entity.getZ()).normalize();

        stack.pushPose();

        if (entity.hasYoyo() && entity.getYoyo().getRenderOrientation(entity.getYoyoStack()) == RenderOrientation.Horizontal){
            stack.mulPose(Axis.XP.rotationDegrees(-90));
        } else {
            if (Minecraft.getInstance().cameraEntity != null) {
                stack.mulPose(Axis.YP.rotationDegrees(270 - Minecraft.getInstance().cameraEntity.getYRot()));
            }
        }

        stack.mulPose(Axis.ZP.rotation(((float) entity.getRemainingTime() / entity.getMaxTime())* 2 * 360));

//        itemRenderer.renderStatic(
//                entity.getYoyoStack(),
//                ItemDisplayContext.NONE,
//                packedLight,
//                0,
//                stack,
//                bufferSource,
//                Minecraft.getInstance().level,
//                packedLight);
        this.itemRenderer.renderStatic(
                entity.getItem(),
                ItemDisplayContext.GROUND,
                packedLight,
                OverlayTexture.NO_OVERLAY,
                stack,
                bufferSource,
                entity.level(),
                entity.getId());
        stack.popPose();
        if (entity.isCollecting() && !entity.getCollectedDrops().isEmpty()) {
            renderCollectedItems(entity, partialTick,stack,bufferSource,packedLight);
        }
        stack.popPose();
        renderChord(entity,partialTick,stack,bufferSource,packedLight);
        Minecraft.getInstance().getProfiler().pop();
    }




    public void renderChord(YoyoEntity entity, float partialTicks,PoseStack stack,MultiBufferSource source,int packedLight) {

        UUID uuid = YoyoEntity.CASTERS.keySet().stream().filter(k->YoyoEntity.CASTERS.get(k).equals(entity)).findFirst().orElse(null);
        if (uuid == null) return;
        Player player = null;
        if (Minecraft.getInstance().level != null) {
            player = Minecraft.getInstance().level.getPlayerByUUID(uuid);
        }
        if (player != null) {
            boolean isMainHand = player.getMainHandItem().equals(entity.getYoyoStack(), false);
            boolean isOffHand = player.getOffhandItem().equals(entity.getYoyoStack(), false);
            stack.pushPose();
            int armDirection = player.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
            if (isOffHand) {
                armDirection = -armDirection;
            }
            float attackAnimation = player.getAttackAnim(partialTicks);
            float interpolatedAttackSwing = Mth.sin(Mth.sqrt(attackAnimation ) * (float)Math.PI);
            float bodyYawRadians = Mth.lerp(partialTicks, player.yBodyRotO, player.yBodyRot) * ((float)Math.PI / 180F);
            double bodyYawSin = Mth.sin(bodyYawRadians );
            double bodyYawCos = Mth.cos(bodyYawRadians );
            double offsetX = (double)armDirection * 0.35D;
            double hookX,hookY,hookZ;
            float crouchOffset;
            if (this.entityRenderDispatcher.options.getCameraType().isFirstPerson() && player == Minecraft.getInstance().player) {
                double fovFactor = 960.0D / (double)this.entityRenderDispatcher.options.fov().get().intValue();
                Vec3 cameraOffset = this.entityRenderDispatcher.camera.getNearPlane().getPointOnPlane((float)armDirection * 0.525F, -0.1F);
                cameraOffset = cameraOffset.scale(fovFactor);
                cameraOffset = cameraOffset.yRot(interpolatedAttackSwing  * 0.5F);
                cameraOffset = cameraOffset.xRot(-interpolatedAttackSwing  * 0.7F);
                hookX = Mth.lerp(partialTicks, player.xo, player.getX()) + cameraOffset.x;
                hookY = Mth.lerp(partialTicks, player.yo, player.getY()) + cameraOffset.y;
                hookZ = Mth.lerp(partialTicks, player.zo, player.getZ()) + cameraOffset.z;
                crouchOffset = player.getEyeHeight();
            } else {
                hookX = Mth.lerp(partialTicks, player.xo, player.getX()) - bodyYawCos * offsetX - bodyYawSin  * 0.8D;
                hookY = player.yo + (double)player.getEyeHeight() + (player.getY() - player.yo) * (double)partialTicks - 0.45D;
                hookZ = Mth.lerp(partialTicks, player.zo, player.getZ()) - bodyYawSin  * offsetX + bodyYawCos * 0.8D;
                crouchOffset = player.isCrouching() ? -0.1875F : 0.0F;
            }

            double entityX = Mth.lerp(partialTicks, entity.xo, entity.getX());
            double entityY = Mth.lerp(partialTicks, entity.yo, entity.getY()) + 0.25D;
            double entityZ = Mth.lerp(partialTicks, entity.zo, entity.getZ());
            float deltaX = (float)(hookX - entityX);
            float deltaY = (float)(hookY - entityY) + crouchOffset;
            float deltaZ = (float)(hookZ - entityZ);
            VertexConsumer lineBuffer = source.getBuffer(RenderType.lineStrip());
            PoseStack.Pose poseStack = stack.last();
            int segments = 24;

            int color = 0xDDDDDD;
            if (entity.getYoyoStack().getItem() instanceof IYoyo yoyo) {
                color = yoyo.getCordColor(entity.getYoyoStack(), entity.tickCount + partialTicks);
            }



            for(int segment = 0; segment <= segments; ++segment) {
                stringVertex(deltaX, deltaY, deltaZ, lineBuffer, poseStack, fraction(segment, 24), fraction(segment + 1, 24),segment,color);
            }

            stack.popPose();
        }

    }

    private static float fraction(int p_114691_, int p_114692_) {
        return (float)p_114691_ / (float)p_114692_;
    }

    private static void stringVertex(
            float deltaX, float deltaY, float deltaZ,
            VertexConsumer vertexBuffer, PoseStack.Pose pose,
            float currentSegmentFraction, float nextSegmentFraction, int currentSegment, int color) {

        float currentX = deltaX * currentSegmentFraction;
        float currentY = deltaY * (currentSegmentFraction * currentSegmentFraction + currentSegmentFraction) * 0.5F + 0.25F;
        float currentZ = deltaZ * currentSegmentFraction;

        float nextX = deltaX * nextSegmentFraction - currentX;
        float nextY = deltaY * (nextSegmentFraction * nextSegmentFraction + nextSegmentFraction) * 0.5F + 0.25F - currentY;
        float nextZ = deltaZ * nextSegmentFraction - currentZ;

        float segmentLength = Mth.sqrt(nextX * nextX + nextY * nextY + nextZ * nextZ);
        nextX /= segmentLength;
        nextY /= segmentLength;
        nextZ /= segmentLength;

        float stringR = (color >> 16& 255) / 255f;
        float stringG = (color >> 8 & 255) / 255f;
        float stringB = (color & 255) / 255f;

        float r = stringR;
        float g = stringG;
        float b = stringB;

        if (currentSegment % 2 == 0) {
            r *= 0.7f;
            g *= 0.7f;
            b *= 0.7f;
        }

        vertexBuffer.vertex(pose.pose(), currentX, currentY, currentZ)
                .color(r, g, b,1)
                .normal(pose.normal(), nextX, nextY, nextZ)
                .endVertex();
    }


    private void renderCollectedItems(YoyoEntity entity, float pt,PoseStack poseStack,MultiBufferSource source,int light) {
        var boundTexture = false;


        for (int i = 0; i < entity.getCollectedDrops().size(); i++) {
            ItemStack stack = entity.getCollectedDrops().get(i);
            int count = stack.getCount();
            int maxCount = stack.getMaxStackSize();
            while (count > 0){
                renderItem(i,entity,stack,pt,poseStack,source, light);
                count -= maxCount;
            }
        }
    }

    private void renderItem(int i, YoyoEntity yoyo, ItemStack itemStack, float partialTicks, PoseStack poseStack, MultiBufferSource source, int light) {
        poseStack.pushPose();

        long seed = ((Item.getId(itemStack.getItem()) * 31L + i) * 31 + itemStack.getCount());

        this.random.setSeed(seed);

        BakedModel bakedModel = this.itemRenderer.getItemModelShaper().getItemModel(itemStack);
        int modelCount = this.transformModelCount(yoyo, itemStack, partialTicks, bakedModel,poseStack);
        boolean gui3d = bakedModel.isGui3d();

        if (!gui3d) {
            float f3 = -0.0f * (modelCount - 1) * 0.5f;
            float f4 = -0.0f * (modelCount - 1) * 0.5f;
            float f5 = -0.09375f * (modelCount - 1) * 0.5f;
            poseStack.translate((double) f3, (double) f4, (double) f5);
        }

        for (int k = 0; k < modelCount; k++) {
            poseStack.pushPose();
            if (gui3d) {

                if (k > 0) {
                    float f7 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    float f9 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    float f6 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f;
                    poseStack.translate((double) f7, (double) f9, (double) f6);
                }

                this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND,light, OverlayTexture.NO_OVERLAY,poseStack,source,Minecraft.getInstance().level, 0);
                poseStack.popPose();
            } else {

                if (k > 0) {
                    float f8 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    float f10 = (this.random.nextFloat() * 2.0f - 1.0f) * 0.15f * 0.5f;
                    poseStack.translate((double) f8, (double) f10, 0.0);
                }

                this.itemRenderer.renderStatic(itemStack, ItemDisplayContext.GROUND,light,OverlayTexture.NO_OVERLAY,poseStack,source,Minecraft.getInstance().level, 0);
                poseStack.popPose();
                poseStack.translate(0.0, 0.0, 0.09375);
            }
        }

        poseStack.popPose();
    }
    private double interpolateValue(double start, double end, double pct) {
        return start + (end - start) * pct;
    }

    private float interpolateValue(float start, float end, float pct) {
        return start + (end - start) * pct;
    }



    private int transformModelCount(YoyoEntity yoyo, ItemStack itemStack, float partialTicks, BakedModel model,PoseStack stack) {
        boolean gui3d = model.isGui3d();
        int count = this.getModelCount(itemStack);

        double bob = Math.sin((random.nextDouble() + yoyo.tickCount + partialTicks) / 10.0
                + random.nextDouble() * Math.PI * 2.0) * 0.1 + 0.1;

        double scale = model.getTransforms()
                .getTransform(ItemDisplayContext.GROUND)
                .scale.y;
        stack.translate(0.0, bob + 0.25 * scale, 0.0);

        if (gui3d ) {
            double angle = ((random.nextDouble() + yoyo.tickCount + partialTicks) / 20.0
                    + random.nextDouble() * Math.PI * 2.0) * (180.0 / Math.PI);
            stack.mulPose(Axis.YP.rotationDegrees((float) angle));
        }


        return count;
    }

    private int getModelCount(ItemStack stack) {
        if (stack.getCount() > 48) {
            return 5;
        } else if (stack.getCount() > 32) {
            return 4;
        } else if (stack.getCount() > 16) {
            return 3;
        } else if (stack.getCount() > 1) {
            return 2;
        } else {
            return 1;
        }
    }

}
