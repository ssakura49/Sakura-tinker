package com.ssakura49.sakuratinker.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class FoxCurioModel<T extends LivingEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(SakuraTinker.MODID, "fox_curio"), "main");
    public final ModelPart Head;
    public final ModelPart Body;
    public final ModelPart Ear;
    public final ModelPart Left_ear;
    public final ModelPart Right_ear;
    public final ModelPart MTail;
    public final ModelPart Tail;
    public final ModelPart Tail2;
    public final ModelPart Tail3;
    public final ModelPart Tail4;
    public final ModelPart Tail5;
    public final ModelPart Tail6;
    public final ModelPart Tail7;
    public boolean crouching;
    public float swimAmount;
    public boolean young;

    public FoxCurioModel(ModelPart root) {
        this.Head = root.getChild("Head");
        this.Body = root.getChild("Body");

        this.Ear = this.Head.getChild("Ear");
        this.Left_ear = this.Ear.getChild("Left_ear");
        this.Right_ear = this.Ear.getChild("Right_ear");

        this.MTail = this.Body.getChild("MTail");
        this.Tail = this.MTail.getChild("Tail");
        this.Tail2 = this.Tail.getChild("Tail2");
        this.Tail3 = this.Tail2.getChild("Tail3");
        this.Tail4 = this.Tail3.getChild("Tail4");
        this.Tail5 = this.Tail4.getChild("Tail5");
        this.Tail6 = this.Tail5.getChild("Tail6");
        this.Tail7 = this.Tail6.getChild("Tail7");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition Head = partdefinition.addOrReplaceChild("Head", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        PartDefinition Body = partdefinition.addOrReplaceChild("Body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition Ear = Head.addOrReplaceChild("Ear", CubeListBuilder.create(), PartPose.offset(-1.7118F, -4.4054F, 1.6817F));

        PartDefinition Left_ear = Ear.addOrReplaceChild("Left_ear", CubeListBuilder.create(), PartPose.offsetAndRotation(6.1475F, -4.3604F, -1.6273F, 3.0665F, -0.0844F, 2.0173F));

        PartDefinition Left_ear_r1 = Left_ear.addOrReplaceChild("Left_ear_r1", CubeListBuilder.create().texOffs(18, 35).addBox(-1.0F, -1.5F, -0.5F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0503F, -0.2643F, 0.2672F, 0.1028F, -0.159F, 0.9414F));

        PartDefinition Left_ear_r2 = Left_ear.addOrReplaceChild("Left_ear_r2", CubeListBuilder.create().texOffs(24, 7).addBox(-0.75F, -0.5F, -0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4884F, -0.0254F, 0.5799F, -0.0801F, 0.033F, 0.8176F));

        PartDefinition Left_ear_r3 = Left_ear.addOrReplaceChild("Left_ear_r3", CubeListBuilder.create().texOffs(19, 5).addBox(-0.5F, 1.6F, -0.25F, 1.0F, -2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1824F, 0.1655F, 0.4146F, 0.1743F, -0.0091F, -1.9119F));

        PartDefinition Left_ear_r4 = Left_ear.addOrReplaceChild("Left_ear_r4", CubeListBuilder.create().texOffs(35, 22).addBox(-0.7437F, -0.75F, -0.875F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4112F, -0.9418F, 0.0124F, 2.2544F, 1.507F, 2.0075F));

        PartDefinition Left_ear_r5 = Left_ear.addOrReplaceChild("Left_ear_r5", CubeListBuilder.create().texOffs(29, 36).addBox(-0.6437F, -0.75F, -1.275F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4929F, -0.6355F, 0.0724F, 1.208F, 1.4998F, 0.6106F));

        PartDefinition Left_ear_r6 = Left_ear.addOrReplaceChild("Left_ear_r6", CubeListBuilder.create().texOffs(30, 6).addBox(-0.6187F, -0.75F, -1.525F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2767F, 0.6142F, -0.1184F, -1.7697F, 1.3552F, -1.4775F));

        PartDefinition Left_ear_r7 = Left_ear.addOrReplaceChild("Left_ear_r7", CubeListBuilder.create().texOffs(32, 31).addBox(-0.826F, -0.6646F, -2.0502F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1027F, -0.5913F, 0.265F, 0.2903F, 1.3176F, -0.0978F));

        PartDefinition Left_ear_r8 = Left_ear.addOrReplaceChild("Left_ear_r8", CubeListBuilder.create().texOffs(30, 11).addBox(-0.6465F, -1.1746F, -1.5639F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0398F, 0.1689F, -0.4522F, -2.5101F, 1.3463F, -2.7694F));

        PartDefinition Left_ear_r9 = Left_ear.addOrReplaceChild("Left_ear_r9", CubeListBuilder.create().texOffs(0, 39).addBox(-0.7022F, -0.7448F, -0.6392F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0398F, 0.1689F, -0.4522F, -2.9102F, 1.2565F, -3.0085F));

        PartDefinition Left_ear_r10 = Left_ear.addOrReplaceChild("Left_ear_r10", CubeListBuilder.create().texOffs(10, 35).addBox(-0.7813F, -0.5438F, -1.3687F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2828F, 0.2F, -0.1098F, -1.5132F, 1.2805F, -1.3236F));

        PartDefinition Left_ear_r11 = Left_ear.addOrReplaceChild("Left_ear_r11", CubeListBuilder.create().texOffs(37, 10).addBox(-0.7267F, -0.5304F, -0.51F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0398F, 0.1689F, -0.4522F, 2.9276F, 1.2881F, -3.0611F));

        PartDefinition Right_ear = Ear.addOrReplaceChild("Right_ear", CubeListBuilder.create(), PartPose.offsetAndRotation(-2.8136F, -4.4051F, -1.7825F, 3.0665F, 0.0844F, -2.0173F));

        PartDefinition Right_ear_r1 = Right_ear.addOrReplaceChild("Right_ear_r1", CubeListBuilder.create().texOffs(24, 35).addBox(-0.7999F, -1.57F, -0.9081F, 2.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2828F, 0.055F, 0.3305F, 0.1028F, 0.159F, -0.9414F));

        PartDefinition Right_ear_r2 = Right_ear.addOrReplaceChild("Right_ear_r2", CubeListBuilder.create().texOffs(24, 6).addBox(-0.75F, -0.5F, -0.25F, 1.0F, 1.0F, 0.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4884F, -0.0253F, 0.58F, -0.0801F, -0.033F, -0.8176F));

        PartDefinition Right_ear_r3 = Right_ear.addOrReplaceChild("Right_ear_r3", CubeListBuilder.create().texOffs(22, 14).addBox(-1.0F, 1.6F, -0.25F, 1.0F, -2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.1824F, 0.1655F, 0.4146F, 0.1743F, 0.0091F, 1.9119F));

        PartDefinition Right_ear_r4 = Right_ear.addOrReplaceChild("Right_ear_r4", CubeListBuilder.create().texOffs(-1, 35).addBox(-0.7438F, -0.75F, -0.875F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.4112F, -0.9418F, 0.0124F, 2.2544F, -1.507F, -2.0075F));

        PartDefinition Right_ear_r5 = Right_ear.addOrReplaceChild("Right_ear_r5", CubeListBuilder.create().texOffs(5, 38).addBox(-0.8437F, -0.75F, -1.275F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.4929F, -0.6355F, 0.0724F, 1.208F, -1.4998F, -0.6106F));

        PartDefinition Right_ear_r6 = Right_ear.addOrReplaceChild("Right_ear_r6", CubeListBuilder.create().texOffs(10, 30).addBox(-0.7687F, -0.75F, -1.525F, 1.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.2767F, 0.6142F, -0.1184F, -1.7697F, -1.3552F, 1.4775F));

        PartDefinition Right_ear_r7 = Right_ear.addOrReplaceChild("Right_ear_r7", CubeListBuilder.create().texOffs(30, 17).addBox(-0.5615F, -0.6646F, -2.0502F, 1.0F, 1.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.1027F, -0.5913F, 0.265F, 0.2903F, -1.3176F, 0.0978F));

        PartDefinition Right_ear_r8 = Right_ear.addOrReplaceChild("Right_ear_r8", CubeListBuilder.create().texOffs(32, 0).addBox(-0.716F, -1.1746F, -1.564F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0398F, 0.1689F, -0.4522F, -2.5101F, -1.3463F, 2.7694F));

        PartDefinition Right_ear_r9 = Right_ear.addOrReplaceChild("Right_ear_r9", CubeListBuilder.create().texOffs(36, 26).addBox(-0.7603F, -0.7448F, -0.6392F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0398F, 0.1689F, -0.4522F, -2.9102F, -1.2565F, 3.0085F));

        PartDefinition Right_ear_r10 = Right_ear.addOrReplaceChild("Right_ear_r10", CubeListBuilder.create().texOffs(32, 15).addBox(-0.6688F, -0.5437F, -1.3688F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.2828F, 0.2F, -0.1098F, -1.5132F, -1.2805F, 1.3236F));

        PartDefinition Right_ear_r11 = Right_ear.addOrReplaceChild("Right_ear_r11", CubeListBuilder.create().texOffs(35, 36).addBox(-0.6358F, -0.5304F, -0.51F, 1.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-0.0398F, 0.1689F, -0.4522F, 2.9276F, -1.2881F, 3.0611F));

        PartDefinition MTail = Body.addOrReplaceChild("MTail", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition Tail = MTail.addOrReplaceChild("Tail", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -13.4591F, 2.4878F, -0.5672F, 0.0F, 0.0F));

        PartDefinition tail_main_r1 = Tail.addOrReplaceChild("tail_main_r1", CubeListBuilder.create().texOffs(18, 8).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2182F, 0.0F, 0.0F));

        PartDefinition Tail2 = Tail.addOrReplaceChild("Tail2", CubeListBuilder.create(), PartPose.offset(0.0F, 0.3955F, 2.8848F));

        PartDefinition tail_main_r2 = Tail2.addOrReplaceChild("tail_main_r2", CubeListBuilder.create().texOffs(16, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.1745F, 0.0F, 0.0F));

        PartDefinition Tail3 = Tail2.addOrReplaceChild("Tail3", CubeListBuilder.create(), PartPose.offset(0.0F, -1.217F, 2.9492F));

        PartDefinition tail5_r1 = Tail3.addOrReplaceChild("tail5_r1", CubeListBuilder.create().texOffs(0, 24).addBox(-1.6F, -1.5F, -1.2656F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.2429F, -0.3005F, 0.1983F, 0.6149F, 0.1071F, 0.0754F));

        PartDefinition tail5_r2 = Tail3.addOrReplaceChild("tail5_r2", CubeListBuilder.create().texOffs(0, 24).addBox(-1.4F, -1.5F, -1.2656F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.2429F, -0.3005F, 0.1983F, 0.6149F, -0.1071F, -0.0754F));

        PartDefinition tail4_r1 = Tail3.addOrReplaceChild("tail4_r1", CubeListBuilder.create().texOffs(0, 30).addBox(0.3F, -2.3789F, -1.8F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-3.0F, 1.6714F, -1.2052F, 0.6305F, -0.27F, -0.1446F));

        PartDefinition tail4_r2 = Tail3.addOrReplaceChild("tail4_r2", CubeListBuilder.create().texOffs(24, 29).addBox(-2.3F, -2.3789F, -1.8F, 2.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(3.0F, 1.6714F, -1.2052F, 0.6305F, 0.27F, 0.1446F));

        PartDefinition tail3_r1 = Tail3.addOrReplaceChild("tail3_r1", CubeListBuilder.create().texOffs(24, 24).addBox(-2.0F, -1.5F, -1.0F, 4.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.5363F, 2.4918F, -0.0873F, 0.0F, 0.0F));

        PartDefinition tail2_r1 = Tail3.addOrReplaceChild("tail2_r1", CubeListBuilder.create().texOffs(0, 8).addBox(-2.5F, -2.0F, -2.0F, 5.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5083F, 0.4248F, 0.6109F, 0.0F, 0.0F));

        PartDefinition tail1_r1 = Tail3.addOrReplaceChild("tail1_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-2.5F, -1.5F, -2.5F, 5.0F, 3.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.9578F, -0.5323F, 0.8727F, 0.0F, 0.0F));

        PartDefinition Tail4 = Tail3.addOrReplaceChild("Tail4", CubeListBuilder.create(), PartPose.offset(0.0F, -3.5401F, 1.5519F));

        PartDefinition tail1_r2 = Tail4.addOrReplaceChild("tail1_r2", CubeListBuilder.create().texOffs(0, 16).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.1309F, 0.0F, 0.0F));

        PartDefinition Tail5 = Tail4.addOrReplaceChild("Tail5", CubeListBuilder.create().texOffs(20, 0).addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -2.5996F, 0.1807F));

        PartDefinition Tail6 = Tail5.addOrReplaceChild("Tail6", CubeListBuilder.create(), PartPose.offset(0.0F, -1.8326F, -0.5468F));

        PartDefinition tail1_r3 = Tail6.addOrReplaceChild("tail1_r3", CubeListBuilder.create().texOffs(34, 29).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.5236F, 0.0F, 0.0F));

        PartDefinition Tail7 = Tail6.addOrReplaceChild("Tail7", CubeListBuilder.create(), PartPose.offset(0.0F, -0.9335F, -0.6039F));

        PartDefinition cube_r1 = Tail7.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(18, 14).addBox(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.6109F, 0.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }
    @Override
    public void prepareMobModel(@NotNull T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
        this.crouching = entity.isCrouching();
        this.swimAmount = entity.getSwimAmount(partialTicks);
        this.young = entity.isBaby();
        super.prepareMobModel(entity, limbSwing, limbSwingAmount, partialTicks);
    }
    @Override
    public void setupAnim(@NotNull T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
//        applyTailRotation(this.Tail,  ageInTicks, 30, 10, 1.0F);
//        applyTailRotation(this.Tail2, ageInTicks, 90, 10, 1.0F);
//        applyTailRotation(this.Tail3, ageInTicks, 120, 10, 0.9F);
//        applyTailRotation(this.Tail4, ageInTicks, 160, 10, 0.8F);
//        applyTailRotation(this.Tail5, ageInTicks, 180, 10, 0.7F);
//        applyTailRotation(this.Tail6, ageInTicks, 180, 10, 0.6F);
//        applyTailRotation(this.Tail7, ageInTicks, 180, 10, 0.5F);
//        this.Tail3.zRot = -Mth.sin(ageInTicks * 0.1F - Mth.DEG_TO_RAD * 120) * Mth.DEG_TO_RAD * 10F;
//
//        this.Left_ear.yRot = netHeadYaw * Mth.DEG_TO_RAD;
//        this.Right_ear.yRot = netHeadYaw * Mth.DEG_TO_RAD;
    }
//    private void applyTailRotation(ModelPart part, float ticks, float phaseDeg, float amplitudeDeg, float decayFactor) {
//        float base = Mth.sin(ticks * 0.1F - Mth.DEG_TO_RAD * phaseDeg) * Mth.DEG_TO_RAD * amplitudeDeg;
//        part.yRot = base * decayFactor;
//    }

    @Override
    public void renderToBuffer(@NotNull PoseStack poseStack, @NotNull VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        Ear.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
        MTail.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
