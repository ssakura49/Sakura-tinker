package com.ssakura49.sakuratinker.client.component;

import com.ssakura49.sakuratinker.coremod.SakuraTinkerCore;
import com.ssakura49.sakuratinker.utils.java.RuntimeEnumExtender;
import net.minecraft.ChatFormatting;
import net.minecraft.util.StringRepresentable;

import java.util.Arrays;
import java.util.stream.Collectors;

public class ChatFormattingContext {
    public static final char ST_ORIGIN_CODE = '@';
    public static final char ST_DP_CODE = '#';
    public static final char ST_COSMIC_CODE = '*';
    private static volatile ChatFormatting SAKURA_ORIGIN;
    private static volatile ChatFormatting ST_DP;
    private static volatile ChatFormatting ST_COSMIC;

    private static final int darkPurpleColor = adjustColor(0x82b2ff);

    synchronized static void init() {
        try {

            RuntimeEnumExtender<ChatFormatting> util = new RuntimeEnumExtender<>(ChatFormatting.class, null);
            Class<?>[] argTypes = new Class[]{String.class, char.class, int.class, Integer.class};
            SAKURA_ORIGIN = util.createEnum("ST_ORIGIN", argTypes, (invoker, id, ord) -> invoker.invoke(id, ord, "ST_ORIGIN", ST_ORIGIN_CODE, -1, (Integer)0));
            ST_DP = util.createEnum("ST_DP", argTypes, (invoker, id, ord) -> invoker.invoke(id, ord, "ST_DP", ST_DP_CODE, 1, (Integer)darkPurpleColor));
            ST_COSMIC = util.createEnum("ST_COSMIC", argTypes, (invoker, id, ord) -> invoker.invoke(id, ord,  "ST_COSMIC", ST_COSMIC_CODE, 1, (Integer)0));
            util.dump();
            if (util.countToAdd > 0) {
                ChatFormatting.CODEC = StringRepresentable.fromEnum(ChatFormatting::values);
                ChatFormatting.FORMATTING_BY_NAME = Arrays.stream(ChatFormatting.values()).collect(Collectors.toMap((p_126660_) -> ChatFormatting.cleanName(p_126660_.name), (p_126652_) -> p_126652_));
            }
        } catch (Throwable e) {
            e.printStackTrace();
            e.printStackTrace(SakuraTinkerCore.stream);
            System.exit(-1);
        }
    }
    private static int adjustColor(int color) {
        return (color & -67108864) == 0 ? color | -16777216 : color;
    }
    static {
        init();
    }
    public static ChatFormatting SAKURA_ORIGIN() {
        return SAKURA_ORIGIN;
    }
    public static ChatFormatting ST_DP() {
        return ST_DP;
    }
    public static ChatFormatting ST_COSMIC() {
        return ST_COSMIC;
    }

}
