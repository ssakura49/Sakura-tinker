package com.ssakura49.sakuratinker.auto;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CustomRendererAttributes {
    String name();

    boolean canCommandUse() default true;

    int id() default -1;

    int var() default 0;
}
