package com.ssakura49.sakuratinker.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.VertexFormatElement;
import com.ssakura49.sakuratinker.render.shader.core.CustomVertexElements;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(VertexFormatElement.Usage.class)
public class VertexFormatElementUsageMixin {
    @Shadow(remap = false)
    @Final
    @Mutable
    @SuppressWarnings("target")
    private static VertexFormatElement.Usage[] $VALUES;

    VertexFormatElementUsageMixin(String id, int ordinal, String p_166975_, VertexFormatElement.Usage.SetupState setupState, VertexFormatElement.Usage.ClearState clearState) {
        throw new AssertionError("NONE");
    }

    @Inject(
            at = {@At(
                    value = "FIELD",
                    shift = At.Shift.AFTER,
                    target = "Lcom/mojang/blaze3d/vertex/VertexFormatElement$Usage;$VALUES:[Lcom/mojang/blaze3d/vertex/VertexFormatElement$Usage;"
            )},
            method = {"<clinit>"}
    )
    private static void addUsage(CallbackInfo ci) {

        int ordinal = $VALUES.length + 1;
        //添加三个CF
        $VALUES = Arrays.copyOf($VALUES, ordinal);
        CustomVertexElements.TINY_MATRIX = (VertexFormatElement.Usage) (Object) (new VertexFormatElementUsageMixin("TINY_MATRIX", ordinal - 1,
                "TinyMatrix",
                (p_167043_, p_167044_, p_167045_, p_167046_, p_167047_, p_167048_) -> {
                    GlStateManager._enableVertexAttribArray(p_167048_);
                    GlStateManager._vertexAttribPointer(p_167048_, p_167043_, p_167044_, false, p_167045_, p_167046_);
                }, (p_167040_, p_167041_) -> GlStateManager._disableVertexAttribArray(p_167041_)));
        CustomVertexElements.ELEMENT_TINY_MAT  = new VertexFormatElement(0, VertexFormatElement.Type.FLOAT, CustomVertexElements.TINY_MATRIX, 16);
        $VALUES[ordinal - 1] = CustomVertexElements.TINY_MATRIX;
    }
}