package com.pigic.hzeropigic.configration;

import com.pigic.hzeropigic.infra.annotation.ConditionOnOS;
import com.pigic.hzeropigic.infra.enums.OSSystem;
import jodd.util.SystemUtil;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.util.Map;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/8/12 9:19
 */
public class ConditionOnOSCondition implements Condition {
    @Override
    public boolean matches(ConditionContext conditionContext, AnnotatedTypeMetadata annotatedTypeMetadata) {
        Map<String, Object> annotationAttributes = annotatedTypeMetadata.getAnnotationAttributes(ConditionOnOS.class.getName());
        OSSystem osSystemVal = (OSSystem) annotationAttributes.get("value");
        String osName = OSSystem.getOSName(osSystemVal);
        boolean b = System.getProperty("os.name").toUpperCase().startsWith(osName);
        return b;
    }
}
