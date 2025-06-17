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
import net.minecraft.world.entity.Entity;

public class TerraPrismaModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(SakuraTinker.MODID, "terra_prisma"), "main");
    private final ModelPart main;

    public TerraPrismaModel(ModelPart root) {
        this.main = root.getChild("main");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-3.5F, -37.0F, -0.5F, 7.0F, 28.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 8).addBox(-0.5F, -45.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 15).addBox(-1.5F, -44.0F, -0.5F, 3.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 0).addBox(-2.5F, -41.0F, -0.5F, 5.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 13).addBox(-2.5F, -9.0F, -0.5F, 5.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 5).addBox(-1.5F, -8.0F, -0.5F, 3.0F, 7.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 11).addBox(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 0).addBox(-2.5F, -5.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(28, 4).addBox(1.5F, -5.0F, -0.5F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 21).addBox(-4.5F, -13.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 25).addBox(3.5F, -13.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 19).addBox(-5.5F, -15.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 5).addBox(4.5F, -15.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(20, 19).addBox(-6.5F, -16.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 15).addBox(5.5F, -16.0F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(16, 25).addBox(-7.5F, -16.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                .texOffs(24, 26).addBox(6.5F, -16.0F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 24.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void setupAnim(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
