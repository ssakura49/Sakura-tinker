package com.ssakura49.sakuratinker.client.component;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;

import java.util.List;

public class LoreHelper {
    public static void applyShiftShowArmorEffect(List<Component> list) {
        list.add(Component.translatable("screen.key.shift_key_down.armor"));
    }

    public static void applyShiftShowItemEffect(List<Component> list) {
        list.add(Component.translatable("screen.key.shift_key_down"));
    }

    public static void applyShiftShowMaterialsInfo(List<Component> list) {
        list.add(Component.translatable("tooltip.sakuratinker.hold_shift").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
    }

    public static Object codeMode(ChatFormatting formatting) {
        return ChatFormatting.PREFIX_CODE + String.valueOf(formatting.getChar());
    }

    public static Component getContributorsInfoComponent() {
        return Component.translatable("tooltip.sakuratinker.contributors").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);
    }
}
