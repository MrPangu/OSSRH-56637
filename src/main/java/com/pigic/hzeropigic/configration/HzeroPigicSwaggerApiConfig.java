package com.pigic.hzeropigic.configration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.service.Tag;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * description
 *
 * @author like.zhang@hand-china.com 2019/01/10 16:28
 */
@Configuration
public class HzeroPigicSwaggerApiConfig {

    /**
     * 外部接口
     */
    public static final String APFM_INTERFACE_MONITOR = "Apfm Interface Monitor";
    /**
     * 外部接口注册
     */
    public static final String APFM_INTERFACE_REGISTER = "Apfm Interface Register";
    @Autowired
    public HzeroPigicSwaggerApiConfig(Docket docket) {
        docket.tags(
                    new Tag(APFM_INTERFACE_MONITOR, "接口监控"),
                    new Tag(APFM_INTERFACE_REGISTER, "接口注册")
                    );

    }
}
