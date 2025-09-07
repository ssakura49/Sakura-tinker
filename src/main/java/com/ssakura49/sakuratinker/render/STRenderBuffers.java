package com.ssakura49.sakuratinker.render;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.ssakura49.sakuratinker.render.shader.cosmic.CosmicItemShaders;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.SortedMap;

public class STRenderBuffers {
    public static SortedMap<RenderType, BufferBuilder> buffers = Util.make(new Object2ObjectLinkedOpenHashMap<>(), (map) -> {
        map.put(CosmicItemShaders.COSMIC_RENDER_TYPE, new BufferBuilder(2097152));
        map.put(CosmicItemShaders.COSMIC_RENDER_TYPE, new BufferBuilder(2097152));
        put(map, Sheets.shieldSheet());
        put(map, Sheets.bedSheet());
        put(map, Sheets.shulkerBoxSheet());
        put(map, Sheets.signSheet());
        put(map, Sheets.hangingSignSheet());
        put(map, Sheets.chestSheet());
        put(map, RenderType.translucentNoCrumbling());
        put(map, RenderType.armorGlint());
        put(map, RenderType.armorEntityGlint());
        put(map, RenderType.glint());
        put(map, RenderType.glintDirect());
        put(map, RenderType.glintTranslucent());
        put(map, RenderType.entityGlint());
        put(map, RenderType.entityGlintDirect());
        put(map, RenderType.waterMask());
        ModelBakery.DESTROY_TYPES.forEach((p_173062_) -> {
            put(map, p_173062_);
        });
    });
    private static final PublicBufferSource bufferSource = new PublicBufferSource(new BufferBuilder(ModList.get().isLoaded("embeddium") || ModList.get().isLoaded("rubidium") ? 2097152 : 256), buffers);

    public static PublicBufferSource getBufferSource() {
        return bufferSource;
    }

    private static void put(Object2ObjectLinkedOpenHashMap<RenderType, BufferBuilder> map, RenderType renderType) {
        map.put(renderType, new BufferBuilder(renderType.bufferSize()));
    }

    public static class PublicBufferSource extends MultiBufferSource.BufferSource {

        public PublicBufferSource(BufferBuilder builder, Map<RenderType, BufferBuilder> map) {
            super(builder, map);
        }

        @Override
        public @NotNull VertexConsumer getBuffer(@NotNull RenderType renderType) {
            return super.getBuffer(renderType);
        }

        @Override
        public void endBatch(@NotNull RenderType renderType) {
            super.endBatch(renderType);
        }
    }
}
