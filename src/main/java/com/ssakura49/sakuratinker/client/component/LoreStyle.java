package com.ssakura49.sakuratinker.client.component;


import com.ssakura49.sakuratinker.utils.java.ExeCallable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum LoreStyle {
    NONE(mutableComponent -> mutableComponent),
    ATTRIBUTE_PREFIX(mutableComponent -> Component.literal("- ").withStyle(ChatFormatting.DARK_PURPLE).append(mutableComponent));
    private final ExeCallable<MutableComponent> delegate;
    public ExeCallable<MutableComponent> getDelegate() {
        return this.delegate;
    }

    LoreStyle(ExeCallable<MutableComponent> delegate) {
        this.delegate = delegate;
    }
}
