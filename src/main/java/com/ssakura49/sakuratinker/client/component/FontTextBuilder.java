package com.ssakura49.sakuratinker.client.component;

import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.FormattedCharSink;
import org.jetbrains.annotations.NotNull;

public class FontTextBuilder {
    public static String formattedCharSequenceToString(FormattedCharSequence text) {
        STFormattedCharSink myFormattedCharSink = new STFormattedCharSink();
        text.accept(myFormattedCharSink);
        return myFormattedCharSink.getText();
    }

    public static class STFormattedCharSink implements FormattedCharSink {
        private final StringBuilder text = new StringBuilder();

        @Override
        public boolean accept(int var1, @NotNull Style style, int var2) {
            text.append(Character.toChars(var2));
            return true;
        }

        public String getText() {
            return text.toString();
        }
    }
}
