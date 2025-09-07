package com.ssakura49.sakuratinker.mixin;

import com.ssakura49.sakuratinker.library.interfaces.textcolor.TextColorInterface;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TextColor.class)
public class TextColorMixin implements TextColorInterface {
    @Unique
    private char st$chatCode = ' ';
    @Override
    public void st$setCode(char code) {
        this.st$chatCode = code;
    }

    @Override
    public char st$getCode() {
        return st$chatCode;
    }
    @Inject(method = "<init>(ILjava/lang/String;)V", at = @At("RETURN"))
    private void init(int pValue, String pName, CallbackInfo ci) {
        ChatFormatting chatFormatting;
        if ((chatFormatting = ChatFormatting.getByName(pName)) != null)
            this.st$setCode(chatFormatting.getChar());
    }
}
