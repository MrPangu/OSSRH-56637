package com.pigic.hzeropigic.configration;

import io.choerodon.resource.annoation.EnableChoerodonResourceServer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author guchang.pan@hand-china.com
 *
 */
@ComponentScan(value = {"com.pigic.hzeropigic"})
@EnableFeignClients({"com.pigic.hzeropigic"})
public class HzeroPlusAutoConfiguration {
}
