package com.pigic.hzeropigic;

import java.lang.annotation.*;

/**
 * @Author: 潘顾昌
 * @Date: 2019/6/11 23:51
 */
@Target({ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Pigict {
    int value();
    String name() default "";
}
