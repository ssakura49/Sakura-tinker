package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.client.model.FoxCurioModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.client.ICurioRenderer;

public class FoxCurioModelRenderer implements ICurioRenderer.ModelRender<FoxCurioModel<LivingEntity>> {
    public static final FoxCurioModelRenderer INSTANCE = new FoxCurioModelRenderer();
    public static final FoxCurioModel<LivingEntity> MODEL =
            new FoxCurioModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(FoxCurioModel.LAYER_LOCATION));
    public static final ResourceLocation TEXTURE =
            new ResourceLocation(SakuraTinker.MODID, "textures/model/fox_curio.png");

    @Override
    public FoxCurioModel<LivingEntity> getModel(ItemStack stack, SlotContext slotContext) {
        return MODEL;
    }

    @Override
    public ResourceLocation getModelTexture(ItemStack stack, SlotContext slotContext) {
        return TEXTURE;
    }

    @Override
    public void prepareModel(ItemStack stack, SlotContext slotContext, PoseStack poseStack,
                             RenderLayerParent<LivingEntity, EntityModel<LivingEntity>> renderLayerParent,
                             float limbSwing, float limbSwingAmount, float partialTicks,
                             float ageInTicks, float netHeadYaw, float headPitch) {

        LivingEntity entity = slotContext.entity();
        FoxCurioModel<LivingEntity> model = getModel(stack, slotContext);

        //同步实体状态
        model.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);

        //同步头部旋转
        model.Head.yRot = netHeadYaw * ((float)Math.PI / 180F);
        model.Head.xRot = headPitch * ((float)Math.PI / 180F);

        //同步身体姿势
        if (renderLayerParent.getModel() instanceof HumanoidModel<?> humanoidModel) {
            model.Body.xRot = humanoidModel.body.xRot;
            model.Body.yRot = humanoidModel.body.yRot;
            model.Body.zRot = humanoidModel.body.zRot;
        }

        //耳朵角度同步
        syncEarRotations(model, entity, netHeadYaw, headPitch,partialTicks);

        //尾巴动画
        animateTail(model, entity, limbSwingAmount, partialTicks);
    }

    private void syncEarRotations(FoxCurioModel<LivingEntity> model, LivingEntity entity, float netHeadYaw, float headPitch, float partialTicks) {
        float radians = Mth.DEG_TO_RAD * 68;
        model.Left_ear.setPos(4.1F, -8.5F, -1.0F);
        model.Right_ear.setPos(-4.1F, -8.5F, -1.0F);
        model.Right_ear.zRot = radians;
        model.Right_ear.yRot = (float) Math.PI;
        model.Left_ear.zRot = -radians;
        model.Left_ear.yRot = (float) Math.PI;
//        model.Ear.yRot = (float) Math.PI;
//        model.Left_ear.yRot = (float) Math.PI; // yaw（左右转向）
//        model.Left_ear.zRot = (float) Math.PI; // roll（侧向倾斜）
        ICurioRenderer.followHeadRotations(entity, model.Ear);

        // 添加自然摆动
        float time = entity.tickCount + partialTicks;
        float wiggle = Mth.sin(time * 0.2f) * 0.05f;

        model.Left_ear.xRot = wiggle;
        model.Right_ear.xRot = wiggle;
    }


    private void animateTail(FoxCurioModel<LivingEntity> model, LivingEntity entity, float limbSwingAmount, float partialTicks) {
        float time = entity.tickCount + partialTicks;
        float baseSpeed = 0.15f;
        float swingStrength = 0.2f + limbSwingAmount * 0.5f;

        ModelPart[] tails = {
                model.Tail, model.Tail2, model.Tail3, model.Tail4,
                model.Tail5, model.Tail6, model.Tail7
        };

        for (int i = 0; i < tails.length; i++) {
            float delay = i * 0.4f;
            float segmentFactor = (1.0f - i * 0.12f);

            float yaw = Mth.sin(time * baseSpeed - delay) * swingStrength * segmentFactor;
            float pitch = -Mth.cos(time * baseSpeed * 0.7f - delay) * swingStrength * 0.35f * segmentFactor;
            float tremor = Mth.sin(time * 0.3f + i) * 0.02f;

            tails[i].yRot = yaw + tremor;
            tails[i].xRot = pitch + tremor * 0.5f;
        }
    }
}
