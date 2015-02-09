package com.alex.lazysdk.ioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by alex on 15-2-6.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface BaseIocView {

    public int id();

    public String click() default "";

    public String longClick() default "";

    public String itemClick() default "";

    public String itemLongClick() default "";

    public BaseIocSelect select() default @BaseIocSelect(seleted = "");
}
