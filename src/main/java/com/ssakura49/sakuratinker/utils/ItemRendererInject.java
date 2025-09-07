package com.ssakura49.sakuratinker.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexMultiConsumer;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.render.RendererUtils;
import com.ssakura49.sakuratinker.render.shader.CullWrappedRenderLayer;
import com.ssakura49.sakuratinker.render.shader.CustomCosmic;
import com.ssakura49.sakuratinker.render.shader.GlowRenderLayer;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import com.ssakura49.sakuratinker.utils.time.TimeContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.util.List;

public class ItemRendererInject {
    public static ItemRendererInject INSTANCE = new ItemRendererInject(Type.NULL);

    private Type type;

    ItemRendererInject(Type type) {
        this.type = type;
    }

//    public static boolean hasCustomRenderer(ItemStack stack) {
//        boolean flag = false;
//        if (stack.getItem() instanceof Scroll && ISpellContainer.isSpellContainer(stack)) {
//            AbstractSpell spell = ISpellContainer.get(stack).getSpellAtIndex(0).getSpell();
//            flag = (spell != null && spell.getDefaultConfig() != null && spell.getDefaultConfig().schoolResource != null && spell.getDefaultSTConfig().schoolResource == TargetRegister.FANTASY_RESOURCE);
//            INSTANCE.setType(Type.FANTASY_SCROLL);
//        } else if (stack.is(AutoRegisterManager.ITEM_ARH().get(WitherFactorItem.class))) {
//            flag = true;
//            INSTANCE.setType(Type.WITHER_FACTOR);
//        } else if (stack.is(AutoRegisterManager.ITEM_ARH().get(FantasyFactorItem.class))) {
//            flag = true;
//            INSTANCE.setType(Type.FANTASY_FACTOR);
//        }
//        return flag;
//
//    }

    public static void renderBall(PoseStack stack) {
        stack.pushPose();
        stack.scale(0.5f, 0.5f, 0.5f);
        RendererUtils.renderSphere(stack, Minecraft.getInstance().renderBuffers.bufferSource(), 1, 30, 0xF000F0, 1, 1, 1, 1, new GlowRenderLayer(new CullWrappedRenderLayer(CustomCosmic.customEndPortal(RendererUtils.cosmic)), null, 1, false), false);
        stack.popPose();

    }

    public static void renderBox(PoseStack stack, ItemDisplayContext context, boolean p_115146_, BakedModel p_115151_) {
        Color color = TimeContext.Both.rainbow(1400.0F, 0.15F, 1.0F);
        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers.bufferSource();
        stack.pushPose();
        if (context == ItemDisplayContext.FIXED) {
            stack.translate(-.5f / 16f, -.5f / 16f, 0);
            net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(stack, p_115151_, context, p_115146_);
        } else if (context == ItemDisplayContext.GUI) {
            stack.translate(1.5 / 16f, -1.5 / 16f, 0);
            net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(stack, p_115151_, context, p_115146_);
        } else if (context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND || context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND) {
            stack.translate(0, 10 / 3F / 16f, (7 / 3F) / 16F);
            stack.scale(.5f, .5f, .5f);
        } else if (context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND || context == ItemDisplayContext.THIRD_PERSON_RIGHT_HAND) {
            stack.translate(0, 8 / 3F / 16f, 1 / 16F);
            stack.scale(.5f, .5f, .5f);
        } else if (context == ItemDisplayContext.GROUND) {
            stack.translate(.5f / 16f, -.5f / 16f, 0);
            net.minecraftforge.client.ForgeHooksClient.handleCameraTransforms(stack, p_115151_, context, p_115146_);
        }
        float degrees = TimeContext.Client.getCommonDegrees();
        float degrees1 = degrees / 1.3F;
        float degrees2 = degrees1 / 2.0F;
        VertexConsumer consumer = source.getBuffer(RenderType.lines());
        stack.scale(0.1f, 0.1f, 0.1f);
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(degrees));
        stack.mulPose(Axis.YP.rotationDegrees(degrees));
        stack.mulPose(Axis.ZP.rotationDegrees(degrees));
        RendererUtils.renderLineBox(stack, consumer, new AABB(-0.6F, -0.6F, -0.6F, 0.6F, 0.6F, 0.6F), 0.4f, 0.4f, 0.4f, .6f);
        stack.popPose();
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(degrees1));
        stack.mulPose(Axis.YP.rotationDegrees(degrees1));
        stack.mulPose(Axis.ZP.rotationDegrees(degrees1));
        RendererUtils.renderLineBox(stack, consumer, new AABB(-1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F), 0.4f, 0.4f, 0.4f, .7f);
        stack.popPose();
        stack.pushPose();
        stack.mulPose(Axis.XP.rotationDegrees(degrees2));
        stack.mulPose(Axis.YP.rotationDegrees(degrees2));
        stack.mulPose(Axis.ZP.rotationDegrees(degrees2));
        RendererUtils.renderLineBox(stack, consumer, new AABB(-1.24F, -1.24F, -1.24F, 1.24F, 1.24F, 1.24F), color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, .81f);
        stack.popPose();
        stack.popPose();
        source.endBatch(RenderType.lines());
    }


    public static void renderLight(PoseStack stack) {
        float time = RendererUtils.hTime();
        float lighting = 1f;

        stack.pushPose();
        stack.translate(0, 0.325, 0);
        stack.scale(.1f, .001f, .1f);
        Vector4f color = TimeContext.Both.rainbowV4(3999F, .5f, 1.0F);
        Vector4f colorBase = TimeContext.Both.rainbowV4(4936, .25f, .8F);
        RendererUtils.dragonDeathLight2(time, stack, new Vector3f(colorBase.x * lighting, colorBase.y * lighting, colorBase.z * lighting), new Vector4f(color.x * lighting, color.y * lighting, color.z * lighting, color.w));
        stack.popPose();
    }

    public static void renderBigBox(PoseStack poseStack) {
        float degrees = TimeContext.Client.getCommonDegrees();
        Color color = TimeContext.Both.rainbow(1400.0F, 0.15F, 1.0F);
        MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers.bufferSource();
        poseStack.pushPose();
        poseStack.translate(1 / 16f, -.5 / 16f, -2.828);
        poseStack.scale(.325F, .325F, .325F);
        poseStack.pushPose();
        poseStack.mulPose(Axis.XP.rotationDegrees(degrees));
        poseStack.mulPose(Axis.YP.rotationDegrees(degrees));
        poseStack.mulPose(Axis.ZP.rotationDegrees(degrees));
        RendererUtils.renderLineBox(poseStack, source.getBuffer(RenderType.lines()), new AABB(-1.24F, -1.24F, -1.24F, 1.24F, 1.24F, 1.24F), color.getRed() / 255F, color.getGreen() / 255F, color.getBlue() / 255F, .4F);
        poseStack.popPose();
        poseStack.popPose();
    }

    public static void renderGuiOrFixed(ItemStack stack, PoseStack poseStack, ItemDisplayContext context, boolean p_115146_, BakedModel p_115151_, CallbackInfo callbackInfo) {
        if (INSTANCE.getType() == Type.WITHER_FACTOR)
            renderBall(poseStack);
        else if (INSTANCE.getType() == Type.FANTASY_FACTOR) {
            renderBox(poseStack, context, p_115146_, p_115151_);
        } else if (INSTANCE.getType() == Type.FANTASY_SCROLL && context == ItemDisplayContext.GUI) {
            renderBigBox(poseStack);
        }
    }

    public static void renderOther(ItemStack stack, PoseStack poseStack, ItemDisplayContext context, boolean p_115146_, BakedModel p_115151_, CallbackInfo callbackInfo) {
        if (INSTANCE.getType() == Type.WITHER_FACTOR) {
            poseStack.popPose();
            callbackInfo.cancel();
        }
        if (INSTANCE.getType() == Type.FANTASY_FACTOR) {
            renderBox(poseStack, context, p_115146_, p_115151_);
        }
    }

    public static void renderModelList(ItemRenderer instance, BakedModel p_115190_, ItemStack stack, int p_115191_, int p_115192_, PoseStack p_115194_, VertexConsumer p_115195_) {
        if (STConfig.Client.enable_cosmic_renderer) {
            RandomSource randomsource = RandomSource.create();
            MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers.bufferSource();
            VertexConsumer vertexConsumer1 = VertexMultiConsumer.create(source.getBuffer(STRenderType.END_COSMIC_ITEM(ResourceLocation.parse(STConfig.Client.wrapped_texture_location))));
            randomsource.setSeed(42L);
            for (Direction direction : Direction.values()) {
                renderQuadList(p_115194_, vertexConsumer1, p_115190_.getQuads(null, direction, randomsource), stack, p_115191_, p_115192_);

            }
            renderQuadList(p_115194_, vertexConsumer1, p_115190_.getQuads(null, null, randomsource), stack, p_115191_, p_115192_);

        }
    }

    public static void renderQuadList(PoseStack p_115163_, VertexConsumer p_115164_, List<BakedQuad> p_115165_, ItemStack p_115166_, int p_115167_, int p_115168_) {

        PoseStack.Pose posestack$pose = p_115163_.last();

        for (BakedQuad bakedquad : p_115165_) {

            float f = 1F;
            float f1 = 1F;
            float f2 = 1F;
            p_115164_.putBulkData(posestack$pose, bakedquad, f, f1, f2, .1F, p_115167_, p_115168_, true);
        }

    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        FANTASY_SCROLL,
        WITHER_FACTOR,
        FANTASY_FACTOR,
        NULL
    }
}
