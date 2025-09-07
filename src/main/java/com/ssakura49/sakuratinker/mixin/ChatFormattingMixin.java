package com.ssakura49.sakuratinker.mixin;

import net.minecraft.ChatFormatting;
import net.minecraft.util.FastColor;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = ChatFormatting.class, priority = 1001)
public class ChatFormattingMixin {
    @SuppressWarnings("target")
    @Shadow(remap = false)
    @Mutable
    public static ChatFormatting[] $VALUES;

    ChatFormattingMixin(String id, int ordinal, String name, char code, int colorIndex, @Nullable Integer colorValue) {
        throw new AssertionError("NONE");
    }

    @Inject(
            at = {@At(
                    value = "FIELD",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/ChatFormatting;$VALUES:[Lnet/minecraft/ChatFormatting;"
            )},
            method = {"<clinit>"}
    )
    @SuppressWarnings("InstantiationOfUtilityClass")
    private static void addFormatting(CallbackInfo ci) {
        int ordinal = $VALUES.length + 3;
        //添加3个ChatFormatting
        $VALUES = (ChatFormatting[]) Arrays.copyOf($VALUES, ordinal);

        $VALUES[ordinal-3] = (ChatFormatting)(Object)(new ChatFormattingMixin("ST_ORIGIN", ordinal-3, "ST_ORIGIN", '@', 0, st$adjustColor(st$getDarkColor(0x567283))));
        $VALUES[ordinal-2] = (ChatFormatting)(Object)(new ChatFormattingMixin("ST_COSMIC", ordinal-2, "ST_COSMIC", '*', 0, 0));
        $VALUES[ordinal-1] = (ChatFormatting)(Object)(new ChatFormattingMixin("ST_DP", ordinal-1, "ST_DP", '#', 0, st$adjustColor(0x82b2ff)));
    }
    @Unique
    private static int st$adjustColor(int color) {
        return (color & -67108864) == 0 ? color | -16777216 : color;
    }
    @Unique
    private static int st$getDarkColor(int i) {
        double d0 = 0.4D;
        int j = (int) ((double) FastColor.ARGB32.red(i) * d0);
        int k = (int) ((double) FastColor.ARGB32.green(i) * d0);
        int l = (int) ((double) FastColor.ARGB32.blue(i) * d0);
        return FastColor.ARGB32.color(0, j, k, l);
    }
}
