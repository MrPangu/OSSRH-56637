package com.pigic.hzeropigic.infra.annotation;

import com.pigic.hzeropigic.configration.HzeroPlusAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/6/29 12:07
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(HzeroPlusAutoConfiguration.class)
public @interface EnableHzeroPlus {
}
