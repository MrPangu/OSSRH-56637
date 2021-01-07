package com.pigic.hzeropigic.infra.annotation;

import com.pigic.hzeropigic.configration.ConditionOnOSCondition;
import com.pigic.hzeropigic.infra.enums.OSSystem;
import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @author guchang.pan@hand-china.com
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional({ConditionOnOSCondition.class})
public @interface ConditionOnOS {
    OSSystem value() default OSSystem.WINDOWS;
}
