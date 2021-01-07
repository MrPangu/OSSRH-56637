package com.pigic.hzeropigic.configration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @author guchang.pan@hand-china.com
 *
 */
@Configuration
public class HttpAutoConfiguration {


    @Bean
    @ConditionalOnClass(ClientHttpRequestInterceptor.class)
    public ClientHttpRequestInterceptor clientHttpRequestInterceptor(){
        return  new RestTemplateIntercetor();
    }

    @Bean
    @ConditionalOnBean(ClientHttpRequestInterceptor.class)
    public RestTemplate restTemplate(ClientHttpRequestInterceptor clientHttpRequestInterceptor){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setInterceptors(Collections.singletonList(clientHttpRequestInterceptor));
        return restTemplate;
    }

}
