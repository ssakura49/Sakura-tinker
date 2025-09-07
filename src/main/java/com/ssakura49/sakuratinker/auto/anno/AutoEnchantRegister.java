package com.ssakura49.sakuratinker.auto.anno;

import com.ssakura49.sakuratinker.SakuraTinker;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface AutoEnchantRegister {
    String value();
    /**
     * @return 前置模组ID
     */
    String dependsOn() default SakuraTinker.MODID;
}
