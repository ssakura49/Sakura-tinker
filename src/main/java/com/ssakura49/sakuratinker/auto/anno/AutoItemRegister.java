package com.ssakura49.sakuratinker.auto.anno;

import com.ssakura49.sakuratinker.SakuraTinker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AutoItemRegister {
    String value();

    /**
     * @return 是否添加到创造物品栏
     */
    boolean addIntoTabs() default true;

    /**
     * @return 前置模组ID
     */
    String dependsOn() default SakuraTinker.MODID;

    /**
     * @return 创造物品栏 名
     */
    String tabName() default "st_items";

    /**
     * @return 为方块物品
     */
    boolean isBlockItem() default false;

    /**
     * @return 优先级(越小越先被加入到tab)
     */
    int priority() default 0;
}
