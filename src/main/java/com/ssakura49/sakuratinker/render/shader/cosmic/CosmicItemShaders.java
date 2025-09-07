package com.ssakura49.sakuratinker.render.shader.cosmic;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.ssakura49.sakuratinker.STConfig;
import com.ssakura49.sakuratinker.SakuraTinker;
import com.ssakura49.sakuratinker.event.event.client.ClientModHandler;
import com.ssakura49.sakuratinker.render.shader.STRenderType;
import com.ssakura49.sakuratinker.render.shader.core.CosmicShaderInstance;
import com.ssakura49.sakuratinker.render.shader.core.STUniform;
import net.minecraft.Util;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Vector2d;

import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CosmicItemShaders {
    public static final float[] COSMIC_UVS = new float[40];
    public static CosmicShaderInstance cosmicShader;
    public static STUniform cosmicTime;
    public static STUniform cosmicYaw;
    public static STUniform cosmicPitch;
    public static STUniform cosmicExternalScale;
    public static STUniform cosmicOpacity;
    public static STUniform cosmicUVs;
    public static STUniform cosmicUVs1;
    public static STUniform cosmicUVs2;
    public static STUniform cosmicUVs3;
    public static STUniform cosmicUVs4;
    public static STUniform cosmicUVs5;
    public static STUniform cosmicUVs6;
    public static STUniform cosmicUVs7;
    public static STUniform cosmicUVs8;
    public static STUniform cosmicUVs9;
    public static RenderType COSMIC_RENDER_TYPE = create();

    public CosmicItemShaders() {
    }

    public static void onRegisterShaders(RegisterShadersEvent event) {
        event.registerShader(CosmicShaderInstance.create(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "cosmic"), DefaultVertexFormat.BLOCK), (e) -> {
            cosmicShader = (CosmicShaderInstance) e;
            cosmicTime = Objects.requireNonNull(cosmicShader.getUniform("time"));
            cosmicYaw = Objects.requireNonNull(cosmicShader.getUniform("yaw"));
            cosmicPitch = Objects.requireNonNull(cosmicShader.getUniform("pitch"));
            cosmicExternalScale = Objects.requireNonNull(cosmicShader.getUniform("externalScale"));
            cosmicOpacity = Objects.requireNonNull(cosmicShader.getUniform("opacity"));
            cosmicUVs = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs"));
            cosmicUVs1 = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs1"));
            cosmicUVs2 = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs2"));
            cosmicUVs3 = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs3"));
            cosmicUVs4 = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs4"));
            cosmicUVs5 = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs5"));
            cosmicUVs6 = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs6"));
            cosmicUVs7 = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs7"));
            cosmicUVs8 = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs8"));
            cosmicUVs9 = Objects.requireNonNull(cosmicShader.getUniform("cosmicuvs9"));
        });


    }

    public static RenderType create() {
        return RenderType.create("sakuratinker:cosmic",
                STRenderType.ANOTHER_BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false,
                RenderType.CompositeState.builder()
                        .setShaderState(STRenderType.COSMIC_STATE)
                        .setDepthTestState(RenderStateShard.EQUAL_DEPTH_TEST)
                        .setLightmapState(RenderStateShard.LIGHTMAP)
                        .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                        .setTextureState(RenderStateShard.BLOCK_SHEET_MIPPED)
                        .createCompositeState(true));
    }

    public static void updateShaderData(ItemDisplayContext context) {
        Minecraft mc = Minecraft.getInstance();
        float yaw = 0.0F;
        float pitch = 0.0F;
        float scale = 0.5F * (float) STConfig.Client.cosmic_scale;
        if (!ClientModHandler.inventoryRender && context != ItemDisplayContext.GUI) {
            if (mc.player != null) {
                Camera camera = mc.gameRenderer.getMainCamera();
                float yd = (float) camera.getPosition().y;
                float xzd = (float) new Vector2d(camera.getPosition().x, camera.getPosition().z).distance(0, 0);
                if (context == ItemDisplayContext.FIRST_PERSON_RIGHT_HAND || context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND)
                    xzd = -xzd;
                pitch = -((float) ((double) (mc.player.getXRot() * 2.0F) * Math.PI / 360.0) - yd / 16F);
                yaw = -(float) ((double) (mc.player.getYRot() * 2.0F) * Math.PI / 360.0) - xzd / 16F;
            }
        } else {
            scale = 25.0F * (float) STConfig.Client.cosmic_scale;
        }
        CosmicItemShaders.cosmicTime.set(Util.getMillis() / (100F / (float) STConfig.Client.cosmic_speed_multiplier));
        CosmicItemShaders.cosmicYaw.set(yaw);
        CosmicItemShaders.cosmicPitch.set(pitch);
        CosmicItemShaders.cosmicExternalScale.set(scale);
        CosmicItemShaders.cosmicOpacity.set(1.0F);
        CosmicItemShaders.cosmicShader.setCosmicIcon();
    }
}
