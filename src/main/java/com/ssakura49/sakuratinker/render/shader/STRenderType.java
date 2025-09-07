package com.ssakura49.sakuratinker.render.shader;

import com.google.common.collect.ImmutableMap;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.render.RendererUtils;
import com.ssakura49.sakuratinker.render.custom.normal.AwayParticleRenderer;
import com.ssakura49.sakuratinker.render.custom.normal.BeamBottomRenderer;
import com.ssakura49.sakuratinker.render.custom.normal.RGBStrikeRenderer;
import com.ssakura49.sakuratinker.render.shader.core.CustomVertexElements;
import com.ssakura49.sakuratinker.render.shader.core.ModShaders;
import com.ssakura49.sakuratinker.render.shader.cosmic.CosmicItemShaders;
import io.github.lounode.eventwrapper.forge.event.converter.level.LevelEventConverter;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

import java.util.function.Function;

public class STRenderType extends RenderType {
    /**
     * 自定义透明渲染状态 - 宇宙效果专用
     * 使用特殊的混合函数实现发光效果:
     * - 颜色混合: SRC_COLOR + DEST_COLOR
     * - Alpha混合: SRC_ALPHA + (1-SRC_ALPHA)
     */
    public static final RenderStateShard.TransparencyStateShard COSMIC_TRANSPARENCY =
            new RenderStateShard.TransparencyStateShard("cosmic_transparency", () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(
                        GlStateManager.SourceFactor.SRC_COLOR,  // 颜色源因子
                        GlStateManager.DestFactor.ONE,          // 颜色目标因子
                        GlStateManager.SourceFactor.SRC_ALPHA,  // Alpha源因子
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA // Alpha目标因子
                );
            }, () -> {
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            });
    public static final VertexFormat ANOTHER_BLOCK = new VertexFormat(ImmutableMap.<String, VertexFormatElement>builder()
            .put("Position", DefaultVertexFormat.ELEMENT_POSITION)
            .put("Color", DefaultVertexFormat.ELEMENT_COLOR)
            .put("UV0", DefaultVertexFormat.ELEMENT_UV0)
            .put("UV2", DefaultVertexFormat.ELEMENT_UV2)
            .put("Normal", DefaultVertexFormat.ELEMENT_NORMAL)
            .put("Padding", DefaultVertexFormat.ELEMENT_PADDING)
            .build());

    public static final RenderStateShard.TexturingStateShard GLINT_TEXTURING = new RenderStateShard.TexturingStateShard("glint_texturing", () -> setupGlintTexturing(8.0F), RenderSystem::resetTextureMatrix);

    protected static final RenderStateShard.ShaderStateShard RENDERTYPE_LIGHT_BEACON_BEAM_SHADER = new RenderStateShard.ShaderStateShard(ModShaders::getLightBeaconBeam);
    protected static final RenderStateShard.ShaderStateShard ENTITY_HASH = new RenderStateShard.ShaderStateShard(ModShaders::getEntityHashShader);

    private static final Function<ResourceLocation, RenderType> ENTITY_HASH_CUTOUT_NO_CULL_ALPHA = Util.memoize((p_286149_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(ENTITY_HASH)
                .setTextureState(new RenderStateShard.TextureStateShard(p_286149_, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL).setLightmapState(LIGHTMAP)
                .setOverlayState(OVERLAY).setLayeringState(VIEW_OFFSET_Z_LAYERING)
                .createCompositeState(true);
        return create("entity_hash_cutout_no_cull_alpha", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    });
    private static final Function<ResourceLocation, RenderType> ENTITY_CUTOUT_NO_CULL_ALPHA = Util.memoize((p_286149_) -> {
        RenderType.CompositeState rendertype$compositestate = RenderType.CompositeState.builder()
                .setShaderState(RENDERTYPE_ENTITY_CUTOUT_NO_CULL_SHADER)
                .setTextureState(new RenderStateShard.TextureStateShard(p_286149_, false, false))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY).setCullState(NO_CULL)
                .setLightmapState(LIGHTMAP).setOverlayState(OVERLAY).setLayeringState(VIEW_OFFSET_Z_LAYERING)
                .createCompositeState(true);
        return create("entity_hash_cutout_no_cull_alpha", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, rendertype$compositestate);
    });

    public static ShaderStateShard COSMIC_STATE = new ShaderStateShard(() -> CosmicItemShaders.cosmicShader);
    public static int id = 1;
    public static RenderType PRT_STAR;
    public static RenderType PRT_LIGHT_STAR;
    public static ResourceLocation[] CIL_RUNE_TEX = new ResourceLocation[RendererUtils.MAX_RUNE_COUNT + 1];
    public static ResourceLocation[] CIL_RUNE_LAYER_TEX = new ResourceLocation[RendererUtils.MAX_RUNE_COUNT + 1];
    public static RenderType[] PRT_RUNE = new RenderType[RendererUtils.MAX_RUNE_COUNT + 1];
    public static RenderType[] PRT_RUNE_LAYER = new RenderType[RendererUtils.MAX_RUNE_COUNT + 1];
    public static RenderType PRT_WAVE;
    public static RenderType[] PRT_AWAY_PARTICLE = new RenderType[2];
    public static RenderType PRT_BEAM_BOTTOM;
    public static RenderType PRT_WHITE;
    public static RenderType TINY_PRT_WHITE;
    public static RenderType PRT_LIGHT_WHITE;
    public static RenderType[] PRT_RGB_STRIKE = new RenderType[4];


    public STRenderType(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    private static final RenderType GUI_OVERLAY_NO_CULL = RenderType.create("gui_overlay_no_cull", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, false, CompositeState.builder().setShaderState(RENDERTYPE_GUI_OVERLAY_SHADER).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setDepthTestState(NO_DEPTH_TEST).setCullState(NO_CULL).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
    public static RenderType guiOverlayNoCull() {
        return GUI_OVERLAY_NO_CULL;
    }
    /**
     * 动态光效纹理状态
     * 基于游戏时间计算纹理偏移，实现流动光效
     * @param scale 纹理缩放系数
     */
    private static void setupGlintTexturing(float scale) {
        long i = (long) ((double) Util.getMillis() * Minecraft.getInstance().options.glintSpeed().get() * 8.0D);
        float f = (float) (i % 110000L) / 110000.0F;
        float f1 = (float) (i % 30000L) / 30000.0F;
        Matrix4f matrix4f = (new Matrix4f()).translation(-f, f1, 0.0F);
        matrix4f.rotateZ(0.17453292F).scale(scale);
        RenderSystem.setTextureMatrix(matrix4f);
    }

    public static RenderType createSphereRenderType2(ResourceLocation r) {
        id++;
        return create("magic_sphere2_" + id, DefaultVertexFormat.POSITION_COLOR_TEX, VertexFormat.Mode.TRIANGLE_STRIP, 256, false, false,
                CompositeState.builder()
                        .setLayeringState(RenderStateShard.POLYGON_OFFSET_LAYERING)
                        .setShaderState(RenderStateShard.RENDERTYPE_BEACON_BEAM_SHADER)
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setCullState(NO_CULL)
                        .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                        .setOutputState(RenderStateShard.PARTICLES_TARGET)
                        .setTextureState(new TextureStateShard(r, false, false))
                        .createCompositeState(true));
    }
    /**
     * 创建宇宙风格球体渲染类型
     * @param blur 是否启用纹理模糊
     * @return 配置好的RenderType实例
     */
    public static RenderType createCosmicSphereRenderType(boolean blur) {
        id++;
        return create("cosmic_sphere" + id, DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.TRIANGLE_STRIP, 256, false, false,
                CompositeState.builder()
                        .setLayeringState(RenderStateShard.POLYGON_OFFSET_LAYERING)
                        .setShaderState(new ShaderStateShard(GameRenderer::getRendertypeEndPortalShader))
                        .setTransparencyState(COSMIC_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setCullState(NO_CULL)
                        .setLightmapState(RenderStateShard.NO_LIGHTMAP)
                        .setTextureState(MultiTextureStateShard.builder()
                                .add(RendererUtils.cosmic, blur, false)
                                .add(RendererUtils.cosmic, blur, false)
                                .build())
                        .createCompositeState(true));
    }

    public static RenderType createSphereRenderType(ResourceLocation r) {
        id++;
        return create("magic_sphere_" + id, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false,
                CompositeState.builder()
                        .setLayeringState(RenderStateShard.POLYGON_OFFSET_LAYERING)
                        .setShaderState(new ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setCullState(NO_CULL)
                        .setLightmapState(NO_LIGHTMAP)
                        .setTextureState(new TextureStateShard(r, false, false))
                        .createCompositeState(true));
    }

    public static RenderType createSphereRenderType_withLines(ResourceLocation r) {
        id++;
        return create("magic_sphere_" + id, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.DEBUG_LINE_STRIP, 256, false, false,
                CompositeState.builder()
                        .setLayeringState(RenderStateShard.POLYGON_OFFSET_LAYERING)
                        .setShaderState(new ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setCullState(NO_CULL)
                        .setLightmapState(NO_LIGHTMAP)
                        .setTextureState(new TextureStateShard(r, false, false))
                        .createCompositeState(true));
    }

    public static RenderType createRingRenderType(ResourceLocation r) {
        id++;
        return create("magic_ring" + id, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false,
                CompositeState.builder()
                        .setLayeringState(RenderStateShard.POLYGON_OFFSET_LAYERING)
                        .setShaderState(new ShaderStateShard(GameRenderer::getPositionColorTexLightmapShader))
                        .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setCullState(NO_CULL)
                        .setLightmapState(NO_LIGHTMAP)
                        .setTextureState(new TextureStateShard(r, false, false))
                        .createCompositeState(true));
    }

    public static RenderType END_PORTAL() {
        id++;
        return create("endportal_render_cosmic" + id, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false,
                CompositeState.builder()
                        .setLayeringState(RenderStateShard.POLYGON_OFFSET_LAYERING)
                        .setShaderState(new ShaderStateShard(GameRenderer::getRendertypeEndPortalShader))
                        .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setCullState(NO_CULL)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setTextureState(MultiTextureStateShard.builder()
                                .add(TheEndPortalRenderer.END_SKY_LOCATION, false, false)
                                .add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false)
                                .build())
                        .createCompositeState(true));
    }

    public static RenderType createLightingEndPortalSphere() {
        id++;
        return create("endportal_lighting_sphere_render" + id, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.TRIANGLE_STRIP, 256, false, false,
                CompositeState.builder()
                        .setLayeringState(RenderStateShard.POLYGON_OFFSET_LAYERING)
                        .setShaderState(new ShaderStateShard(GameRenderer::getRendertypeEndPortalShader))
                        .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setCullState(NO_CULL)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setTextureState(MultiTextureStateShard.builder()
                                .add(TheEndPortalRenderer.END_SKY_LOCATION, false, false)
                                .add(TheEndPortalRenderer.END_PORTAL_LOCATION, false, false)
                                .build())
                        .createCompositeState(true));
    }

    public static RenderType END_PORTAL(ResourceLocation resourceLocation) {
        id++;
        return create("font_endportal_render_cosmic" + id, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false,
                CompositeState.builder()
                        .setLayeringState(RenderStateShard.POLYGON_OFFSET_LAYERING)
                        .setShaderState(new ShaderStateShard(GameRenderer::getRendertypeEndPortalShader))
                        .setTransparencyState(RenderStateShard.LIGHTNING_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setCullState(NO_CULL)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setTextureState(MultiTextureStateShard.builder()
                                .add(resourceLocation, false, false)
                                .add(resourceLocation, false, false)
                                .build())
                        .createCompositeState(true));
    }

    public static RenderType END_COSMIC_ITEM(ResourceLocation resourceLocation) {
        id++;
        return create("font_endportal_render_cosmic" + id, DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, CompositeState.builder().setShaderState(new RenderStateShard.ShaderStateShard(GameRenderer::getRendertypeEndPortalShader)).setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST).setLightmapState(RenderStateShard.LIGHTMAP).setTransparencyState(COSMIC_TRANSPARENCY)
                .setTextureState(MultiTextureStateShard.builder()
                        .add(resourceLocation, false, false)
                        .add(resourceLocation, false, false)
                        .build())
                .createCompositeState(true));

    }

    public static RenderType END_PORTAL_TRANSLUCENT(ResourceLocation resourceLocation) {
        id++;
        return create("font_endportal_render_cosmic" + id, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false,
                CompositeState.builder()
                        .setLayeringState(RenderStateShard.POLYGON_OFFSET_LAYERING)
                        .setShaderState(new ShaderStateShard(GameRenderer::getRendertypeEndPortalShader))
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setDepthTestState(RenderStateShard.LEQUAL_DEPTH_TEST)
                        .setCullState(NO_CULL)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setTextureState(MultiTextureStateShard.builder()
                                .add(resourceLocation, false, false)
                                .add(resourceLocation, false, false)
                                .build())
                        .createCompositeState(true));
    }
    /**
     * 标准粒子渲染类型
     * @param location 粒子纹理路径
     * @return 配置好的粒子RenderType
     */
    public static RenderType particle(ResourceLocation location) {
        id++;
        RenderStateShard.TextureStateShard textureStateShard = new RenderStateShard.TextureStateShard(location, false, false);
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setTextureState(textureStateShard)
                .setShaderState((RENDERTYPE_BEACON_BEAM_SHADER))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setOverlayState(OVERLAY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return create("sakuratinker" + ":particle" + id, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 256, true, true, compositeState);
    }
    public static RenderType particleFast(ResourceLocation location) {
        id++;
        RenderStateShard.TextureStateShard textureStateShard = new RenderStateShard.TextureStateShard(location, false, false);
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setTextureState(textureStateShard)
                .setShaderState(new ShaderStateShard(ModShaders::getCilShader))
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setOverlayState(OVERLAY)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return create("sakuratinker:particle", CustomVertexElements.CIL(), VertexFormat.Mode.QUADS, 2097152, true, true, compositeState);
    }
    public static RenderType lightParticle(ResourceLocation location) {
        id++;
        RenderStateShard.TextureStateShard textureStateShard = new RenderStateShard.TextureStateShard(location, false, false);
        RenderType.CompositeState compositeState = RenderType.CompositeState.builder()
                .setTextureState(textureStateShard)
                .setShaderState(RENDERTYPE_LIGHT_BEACON_BEAM_SHADER)
                .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                .setCullState(NO_CULL)
                .setOverlayState(OVERLAY)
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .setWriteMaskState(COLOR_WRITE)
                .createCompositeState(false);
        return create("sakuratinker" + ":light_particle" + id, DefaultVertexFormat.POSITION_COLOR_TEX_LIGHTMAP, VertexFormat.Mode.QUADS, 2097152, true, true, compositeState);
    }

    public static RenderType glow(ResourceLocation location) {
        return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 0, CompositeState.builder()
                .setShaderState(RenderType.POSITION_COLOR_TEX_LIGHTMAP_SHADER)
                .setTextureState(new TextureStateShard(location, false, false))
                .setTransparencyState(RenderType.LIGHTNING_TRANSPARENCY)
                .setCullState(RenderType.NO_CULL)
                .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
                .setOutputState(RenderStateShard.PARTICLES_TARGET)
                .createCompositeState(true));
    }

    public static RenderType mask(ResourceLocation location) {
        return RenderType.create("", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, true, false, CompositeState.builder()
                .setShaderState(new ShaderStateShard(() -> CosmicItemShaders.cosmicShader))
                .setTextureState(new TextureStateShard(location, false, false))
                .setTransparencyState(RenderType.TRANSLUCENT_TRANSPARENCY)
                .setLightmapState(RenderType.LIGHTMAP)
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setCullState(RenderType.NO_CULL)
                .setWriteMaskState(RenderStateShard.COLOR_WRITE)
                .setLayeringState(RenderType.VIEW_OFFSET_Z_LAYERING)
                .createCompositeState(true));
    }

    public static RenderType LIGHTING_ITEM_ENTITY() {
        return RenderType.create("lighting_item_entity", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.QUADS, 256, false, true, CompositeState.builder()
                .setShaderState(RenderType.RENDERTYPE_LIGHTNING_SHADER)
                .setWriteMaskState(RenderType.COLOR_WRITE)
                .setTransparencyState(RenderType.LIGHTNING_TRANSPARENCY)
                .setOutputState(RenderType.PARTICLES_TARGET)
                .createCompositeState(false));
    }

    public static RenderType entityHashCutoutNoCullAlpha(ResourceLocation location) {
        return ENTITY_HASH_CUTOUT_NO_CULL_ALPHA.apply(location);
    }

    public static RenderType entityCutoutNoCullAlpha(ResourceLocation location) {
        return ENTITY_CUTOUT_NO_CULL_ALPHA.apply(location);
    }

    protected static final RenderType.CompositeState LIGHTNING_STATE = RenderType.CompositeState.builder()
            .setShaderState(POSITION_COLOR_SHADER)
            .setTransparencyState(LIGHTNING_TRANSPARENCY)
            .createCompositeState(false);
    public static final RenderType LIGHTNING = RenderType.create(
            source("lightning"),
            DefaultVertexFormat.POSITION_COLOR,
            VertexFormat.Mode.QUADS,
            256, false, true, LIGHTNING_STATE);

    public static void reloadParticleRenderTypes() {
        PRT_STAR = STRenderType.particle(ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/particle/star.png"));
        PRT_LIGHT_STAR = STRenderType.lightParticle(ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/particle/star.png"));
        for (int i = 0; i <= RendererUtils.MAX_RUNE_COUNT; i++) {
            CIL_RUNE_TEX[i] = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/particle/rune" + i + ".png");
            CIL_RUNE_LAYER_TEX[i] = ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "textures/particle/rune" + i + "_layer.png");
            PRT_RUNE[i] = STRenderType.particle(CIL_RUNE_TEX[i]);
            PRT_RUNE_LAYER[i] = STRenderType.particle(CIL_RUNE_LAYER_TEX[i]);
        }
        PRT_WAVE = STRenderType.particle(RendererUtils.wave);
        PRT_AWAY_PARTICLE[0] = STRenderType.particle(AwayParticleRenderer.TEXTURES[0]);
        PRT_AWAY_PARTICLE[1] = STRenderType.particle(AwayParticleRenderer.TEXTURES[1]);
        PRT_BEAM_BOTTOM = STRenderType.particle(BeamBottomRenderer.TEXTURE);
        TINY_PRT_WHITE = STRenderType.particleFast(RendererUtils.beam);
        PRT_WHITE = STRenderType.particle(RendererUtils.beam);
        PRT_LIGHT_WHITE = STRenderType.lightParticle(RendererUtils.beam);
        PRT_RGB_STRIKE[0] = RenderType.entityCutoutNoCull(RGBStrikeRenderer.TEXTURES[0]);
        PRT_RGB_STRIKE[1] = RenderType.entityCutoutNoCull(RGBStrikeRenderer.TEXTURES[1]);
        PRT_RGB_STRIKE[2] = RenderType.entityCutoutNoCull(RGBStrikeRenderer.TEXTURES[2]);
        PRT_RGB_STRIKE[3] = RenderType.entityCutoutNoCull(RGBStrikeRenderer.TEXTURES[3]);
    }

    private static String source(String name) {
        return SakuraTinker.MODID + ":" + name;
    }
}
