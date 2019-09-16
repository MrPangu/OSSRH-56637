package com.pigic.hzeropigic.configration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @Author: 潘顾昌
 * @Date: 2019/1/6 21:18
 */
@Configuration
@PropertySource(value = "classpath:config/pigic.properties")
public class BaseConfigration {
}
