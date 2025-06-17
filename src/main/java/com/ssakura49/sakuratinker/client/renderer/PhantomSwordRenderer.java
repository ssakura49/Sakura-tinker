package com.ssakura49.sakuratinker.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.ssakura49.sakuratinker.client.model.BakedModels;
import com.ssakura49.sakuratinker.compat.ExtraBotany.ExtraBotanyCompat;
import com.ssakura49.sakuratinker.content.entity.PhantomSwordEntity;
import com.ssakura49.sakuratinker.register.STItems;
import com.ssakura49.sakuratinker.utils.RenderUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import slimeknights.tconstruct.library.tools.item.ModifiableItem;
import vazkii.botania.client.core.helper.RenderHelper;

public class PhantomSwordRenderer extends EntityRenderer<PhantomSwordEntity> {

    private final ItemRenderer itemRenderer;

    public PhantomSwordRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(PhantomSwordEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        Minecraft mc = Minecraft.getInstance();
        if(entity.getDelay() > 0)
            return;

        poseStack.pushPose();

        poseStack.pushPose();
        float s = 1.5F;
        poseStack.scale(s, s, s);
        poseStack.mulPose(Axis.YP.rotationDegrees(entity.getRotation()+90F));
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getPitch()));
        poseStack.mulPose(Axis.ZP.rotationDegrees(-45));

        float alpha = entity.getFake() ? Math.max(0F, 0.75F - entity.tickCount * (0.75F / PhantomSwordEntity.LIVE_TICKS) * 1.5F) : 1F;
        BakedModel model = BakedModels.firstFractalWeaponModels[entity.getVariety()];
        int color = 0xFFFFFF | ((int) (alpha * 255F)) << 24;
        RenderUtil.renderItemCustomColor(mc.player, new ItemStack(ExtraBotanyCompat.first_fractal.get().asItem()), color, poseStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, model);

        poseStack.scale(1 / s, 1 / s, 1 / s);
        poseStack.popPose();

        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(PhantomSwordEntity entity) {
        return InventoryMenu.BLOCK_ATLAS;
    }
}
