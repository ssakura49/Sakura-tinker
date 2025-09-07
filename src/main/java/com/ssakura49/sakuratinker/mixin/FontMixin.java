package com.ssakura49.sakuratinker.mixin;


import com.ssakura49.sakuratinker.client.component.MinecraftFont;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import org.joml.Matrix4f;
import java.util.concurrent.atomic.AtomicReference;

@Mixin(value = Font.class, priority = 1001)
public abstract class FontMixin {

    @Shadow
    public abstract void drawInBatch8xOutline(FormattedCharSequence p_168646_, float p_168647_, float p_168648_, int p_168649_, int p_168650_, Matrix4f p_254170_, MultiBufferSource p_168652_, int p_168653_);

    @Inject(
            at = {@At("TAIL")},
            method = {"drawInBatch(Lnet/minecraft/util/FormattedCharSequence;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)I"},
            cancellable = true)
    private void drawOutLine(FormattedCharSequence pText, float pX, float pY, int pColor, boolean pDropShadow, Matrix4f pMatrix, MultiBufferSource pBuffer, Font.DisplayMode pDisplayMode, int pBackgroundColor, int pPackedLightCoords, CallbackInfoReturnable<Integer> cir) {
        if (sakuratinker$getFormattingName(pText).startsWith("st")) {
            cir.setReturnValue(MinecraftFont.INSTANCE.drawInBatch(pText, pX, pY, pColor, pDropShadow, pMatrix, pBuffer, pDisplayMode, pBackgroundColor, pPackedLightCoords));
        }

    }

    @Unique
    private static String sakuratinker$getFormattingName(FormattedCharSequence fcs) {
        AtomicReference<String> str = new AtomicReference<>("");
        fcs.accept((index, style, codePoint) -> {
            Optional<TextColor> optional = Optional.ofNullable(style.getColor());
            if (optional.isPresent()) {
                TextColor color1 = optional.get();

                str.set(color1.serialize());
            }
            return true;
        });
        return str.get();
    }
}
