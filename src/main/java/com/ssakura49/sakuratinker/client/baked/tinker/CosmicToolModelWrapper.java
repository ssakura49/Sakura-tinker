package com.ssakura49.sakuratinker.client.baked.tinker;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.client.baked.model.PerspectiveModel;
import com.ssakura49.sakuratinker.client.baked.model.PerspectiveModelState;
import com.ssakura49.sakuratinker.client.baked.model.WrappedItemModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import slimeknights.tconstruct.library.tools.nbt.MaterialIdNBT;

import java.util.List;

public class CosmicToolModelWrapper extends WrappedItemModel implements PerspectiveModel {
    private final PerspectiveModelState modelState;

    public CosmicToolModelWrapper(BakedModel wrapped, PerspectiveModelState modelState) {
        super(wrapped);
        this.modelState = modelState;
    }

    @Override
    public @Nullable PerspectiveModelState getModelState() {
        return modelState;
    }

    @Override
    public void renderItem(ItemStack stack, ItemDisplayContext ctx, PoseStack poseStack, MultiBufferSource source, int light, int overlay) {
        if (isInfinityMaterial(stack)) {
            // ⚠️ 关键：必须 resolve 以生成带材质的模型
            BakedModel resolved = wrapped.getOverrides().resolve(wrapped, stack, Minecraft.getInstance().level, null, 0);

            if (resolved != null) {
                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                for (BakedModel pass : resolved.getRenderPasses(stack, true)) {
                    for (RenderType renderType : pass.getRenderTypes(stack, true)) {
                        VertexConsumer buffer = source.getBuffer(renderType);
                        itemRenderer.renderModelLists(pass, stack, light, overlay, poseStack, new MaskRedVertexConsumer(buffer));
                    }
                }
                return;
            }
        }

        // fallback：普通模型渲染
        if (wrapped instanceof PerspectiveModel perspectiveWrapped) {
            perspectiveWrapped.renderItem(stack, ctx, poseStack, source, light, overlay);
        }
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, @NotNull RandomSource rand) {
        return wrapped.getQuads(state, side, rand); // 不建议在这里写 mask 注入逻辑
    }

    private boolean isInfinityMaterial(ItemStack stack) {
        if (!stack.hasTag()) return false;
        return MaterialIdNBT.from(stack).getMaterials().stream()
                .anyMatch(id -> id.getId().getPath().equals("infinity"));
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return wrapped.getOverrides(); // ✅ 保留 ToolModel 的 override handler
    }
}
