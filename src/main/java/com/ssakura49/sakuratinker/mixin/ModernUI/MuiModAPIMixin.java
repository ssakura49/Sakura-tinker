package com.ssakura49.sakuratinker.mixin.ModernUI;

import icyllis.modernui.mc.MuiModApi;
import net.minecraft.ChatFormatting;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Arrays;

@Mixin(value = MuiModApi.class, remap = false)
public class MuiModAPIMixin {
    @Shadow
    @Final
    @Mutable
    private static ChatFormatting[] FORMATTING_TABLE;

    @Inject(
            at = @At(
                    value = "FIELD",
                    shift = At.Shift.AFTER,
                    target = "Licyllis/modernui/mc/MuiModApi;FORMATTING_TABLE:[Lnet/minecraft/ChatFormatting;"
            ),
            method = "<clinit>"
    )
    private static void expandLength(CallbackInfo ci) {
        FORMATTING_TABLE = Arrays.copyOf(FORMATTING_TABLE, 65536);
    }

    @Inject(method = "getFormattingByCode", at = @At("HEAD"), cancellable = true)
    private static void patchFormattingCode(char code, CallbackInfoReturnable<ChatFormatting> cir) {
        if (code >= 128 && code < FORMATTING_TABLE.length) {
            ChatFormatting result = FORMATTING_TABLE[code];
            if (result != null) {
                cir.setReturnValue(result);
            }
        }
    }
}