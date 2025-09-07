package com.ssakura49.sakuratinker.utils.component;

public class RomanNumberalUtil {
    private static final int[] VALUES = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
    private static final String[] SYMBOLS = {
            "M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"
    };

    /**
     * 将整数转换为罗马数字（支持 1 ~ 3999）
     */
    public static String toRoman(int number) {
        if (number <= 0 || number > 3999) {
            throw new IllegalArgumentException("罗马数字只支持 1-3999: " + number);
        }
        StringBuilder sb = new StringBuilder();
        int remain = number;
        for (int i = 0; i < VALUES.length; i++) {
            while (remain >= VALUES[i]) {
                remain -= VALUES[i];
                sb.append(SYMBOLS[i]);
            }
        }
        return sb.toString();
    }

    /**
     * 将罗马数字转为整数
     */
    public static int fromRoman(String roman) {
        int i = 0, result = 0;
        for (int j = 0; j < VALUES.length; j++) {
            String symbol = SYMBOLS[j];
            while (roman.startsWith(symbol, i)) {
                result += VALUES[j];
                i += symbol.length();
            }
        }
        return result;
    }
}
