package com.ssakura49.sakuratinker.mixin;

import com.ssakura49.sakuratinker.utils.DeprecatedMixin;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(Style.class)
@DeprecatedMixin
public class StyleMixin {
    @ModifyVariable(method = "applyFormat", at = @At("HEAD"), argsOnly = true)
    private ChatFormatting apply(ChatFormatting src) {
        if (src == null) src = ChatFormatting.WHITE;
        return src;
    }

}

