package com.pigic.hzeropigic.configration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

/**
 * @Author: 潘顾昌
 * @Date: 2019/6/29 14:34
 */
public class RestTemplateIntercetor implements ClientHttpRequestInterceptor {
    private static Logger logger = LoggerFactory.getLogger(RestTemplateIntercetor.class);
    private static String NOTICE_MESSAGE_HOST = "www.zjzr.com.cn";
    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {

        String authority = httpRequest.getURI().getAuthority();
        if(NOTICE_MESSAGE_HOST.equals(authority)){
            HttpHeaders headers = httpRequest.getHeaders();
            boolean contains = headers.containsKey("token");
            if(!contains){
                logger.warn("您对www.zjzr.com.cn的访问没有认证标识token,有可能被网关拦截无权访问,请注意");
            }
        }
        //继续执行请求
        return clientHttpRequestExecution.execute(httpRequest,bytes);
    }
}