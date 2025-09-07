package com.ssakura49.sakuratinker.render.shader;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Function;

@OnlyIn(Dist.CLIENT)
public class CustomCosmic extends RenderStateShard {

    private static final Function<ResourceLocation, RenderType> END_PORTAL;
    private static final Function<ResourceLocation, RenderType> COSMIC;

    static {
        END_PORTAL = Util.memoize(texture -> {
            RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_END_PORTAL_SHADER)
                    .setTextureState(MultiTextureStateShard.builder()
                            .add(texture, false, false)
                            .add(texture, false, false).build())
                    .createCompositeState(false);
            return RenderType.create("custom_end_portal", DefaultVertexFormat.POSITION, VertexFormat.Mode.QUADS, 256, false, false, rendertype$compositestate);
        });
        COSMIC = Util.memoize(texture -> {
            RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                    .setShaderState(RenderStateShard.RENDERTYPE_END_PORTAL_SHADER)
                    .setTextureState(MultiTextureStateShard.builder()
                            .add(texture, false, false)
                            .add(texture, false, false).build())
                    .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                    .setLightmapState(RenderStateShard.LIGHTMAP)
                    .setCullState(RenderStateShard.CULL)
                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                    .createCompositeState(true);
            return RenderType.create("custom_cosmic", DefaultVertexFormat.POSITION, VertexFormat.Mode.TRIANGLE_STRIP, 256, false, false, rendertype$compositestate);
        });
    }

    public CustomCosmic(String a1, Runnable a2, Runnable a3) {
        super(a1, a2, a3);
    }

    public static RenderType customEndPortal(ResourceLocation texture) {
        return END_PORTAL.apply(texture);
    }

    public static RenderType cosmic(ResourceLocation texture) {
        return COSMIC.apply(texture);
    }
}
