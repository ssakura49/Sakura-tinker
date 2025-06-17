package com.ssakura49.sakuratinker.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin({ItemRenderer.class})
public interface ItemRendererAccessor {
    @Invoker
    void callRenderQuadList(PoseStack var1, VertexConsumer var2, List<BakedQuad> var3, ItemStack var4, int var5, int var6);
}
