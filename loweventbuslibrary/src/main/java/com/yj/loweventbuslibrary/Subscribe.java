package com.yj.loweventbuslibrary;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Subscribe {
    // 回调函数的线程模式
    ThreadMode threadMode() default  ThreadMode.MAIN;
}
