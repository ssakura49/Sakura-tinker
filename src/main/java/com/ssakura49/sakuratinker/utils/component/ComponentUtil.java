package com.ssakura49.sakuratinker.utils.component;

import com.ssakura49.sakuratinker.SakuraTinker;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.List;

public class ComponentUtil {
    public ComponentUtil(){}

    public static ResourceLocation getResource(String name) {
        return new ResourceLocation(SakuraTinker.MODID, name);
    }

    public static String makeTranslationKey(String base, @Nullable ResourceLocation name) {
        return net.minecraft.Util.makeDescriptionId(base, name);
    }

    public static Component makeTranslation(String base, @Nullable ResourceLocation name, Object... arguments) {
        return Component.translatable(makeTranslationKey(base, name), arguments);
    }

    public static String makeTranslationKey(String base, String name) {
        return ComponentUtil.makeTranslationKey(base, getResource(name));
    }

    public static MutableComponent makeTranslation(String base, String name) {
        return Component.translatable(makeTranslationKey(base, name));
    }

    public static MutableComponent makeTranslation(String base, String name, Object... arguments) {
        return Component.translatable(makeTranslationKey(base, name), arguments);
    }


    /**
     * 直接根据本地化键生成
     * @param list Component对应的list
     * @param translatableText 需要本地化键的文本
     * @param color 颜色,0xffffff这种或者直接十进制颜色
     */
    public static void colorfulTranslatable(List<Component> list, String translatableText, int color){
        list.add(Component.translatable(translatableText).withStyle(style -> style.withColor(color)));
    }
    /**
     * 直接根据文本和颜色生成
     * @param list Component对应的list
     * @param literal 文本
     * @param color 颜色,0xffffff这种或者直接十进制颜色
     */
    public static void colorfulLiteral(List<Component> list,String literal,int color){
        list.add(Component.literal(literal).withStyle(style -> style.withColor(color)));
    }
}
