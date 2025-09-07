package com.ssakura49.sakuratinker.render.shader.core;

import com.mojang.blaze3d.Blaze3D;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.RegisterShadersEvent;

import java.util.Objects;

public class ModShaders {
    static Minecraft mc = Minecraft.getInstance();
    private static STShaderInstance ENTITY_HASH;
    private static STShaderInstance LIGHT_BEACON_BEAM;
    private static STShaderInstance CIL_SHADER;
    private static STUniform time;
    private static STUniform ScreenSize;

    public static void onRegisterShaders(RegisterShadersEvent event) {
        event.registerShader(STShaderInstance.create(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "hash"), DefaultVertexFormat.NEW_ENTITY), (e) -> {
            ENTITY_HASH = (STShaderInstance) e;
            time = Objects.requireNonNull(ENTITY_HASH.getUniform("Time"));
            ScreenSize = Objects.requireNonNull(ENTITY_HASH.getUniform("ScreenSize"));
            ENTITY_HASH.onApply(() -> {
                ModShaders.time.set((float) Blaze3D.getTime()*100F);
                ModShaders.ScreenSize.set((float) mc.mainRenderTarget.width, (float) mc.mainRenderTarget.height);
            });
        });
        event.registerShader(STShaderInstance.create(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "rendertype_light_beacon_beam"), DefaultVertexFormat.BLOCK), (e) -> {
            LIGHT_BEACON_BEAM = (STShaderInstance) e;
        });
        CustomVertexElements.CIL();
        event.registerShader(STShaderInstance.create(event.getResourceProvider(), ResourceLocation.fromNamespaceAndPath(SakuraTinker.MODID, "rendertype_cil_particle"), CustomVertexElements.CIL()), (e) -> {
            CIL_SHADER = (STShaderInstance) e;
        });
    }

    public static STShaderInstance getEntityHashShader() {
        return ENTITY_HASH;
    }

    public static STShaderInstance getLightBeaconBeam() {
        return LIGHT_BEACON_BEAM;
    }

    public static STShaderInstance getCilShader() {
        return CIL_SHADER;
    }
}
