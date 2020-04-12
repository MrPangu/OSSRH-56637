package com.pigic.hzeropigic.configration;

import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponseInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

/**
 * @author guchang.pan@hand-china.com
 * @Date: 2019/6/29 13:58
 */
@Configuration
public class HttpAutoConfiguration {

    @Bean
    @ConditionalOnClass(HttpRequestInterceptor.class)
    public HttpRequestInterceptor HttpClientRequestInterceptor(){
        return new HttpClientRequestInterceptor();
    }

    @Bean
    @ConditionalOnClass(HttpResponseInterceptor.class)
    public HttpResponseInterceptor HttpClientResponseInterceptor(){
        return new HttpClientResponseInterceptor();
    }

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
