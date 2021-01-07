package com.pigic.hzeropigic.configration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author guchang.pan@hand-china.com
 *
 */
@Configuration
@PropertySource(value = "classpath:config/pigic.properties")
public class BaseConfigration {
}
