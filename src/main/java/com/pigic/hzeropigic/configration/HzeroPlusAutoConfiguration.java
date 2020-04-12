package com.pigic.hzeropigic.configration;

import io.choerodon.resource.annoation.EnableChoerodonResourceServer;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/6/29 12:08
 */
@ComponentScan(value = {"com.pigic.hzeropigic"})
@EnableFeignClients({"com.pigic.hzeropigic"})
public class HzeroPlusAutoConfiguration {
}
