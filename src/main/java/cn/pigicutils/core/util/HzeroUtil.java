package cn.pigicutils.core.util;

import cn.pigicutils.core.bean.Token;
import cn.pigicutils.core.exceptions.UtilException;
import cn.pigicutils.core.lang.Dict;
import com.pigic.hzeropigic.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * Hzero常用工具类
 *
 * @author pgc
 * @date 2020/5/7 13:40
 */
public class HzeroUtil {

    /**
     * Oauth微服务名称
     */
    public static final String GATEWAY_SERVICE = "HZERO-GATEWAY";

    /**
     * 常用默认密码模式获取Token
     * 只针对包含
     * 协议： http
     * 用户名：admin
     * 密码：Admin@123
     * client_id: client
     * client_secret: secret
     *
     * @return
     */
    public static Token genDefaultPasswordToken() {
        RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);
        ResponseEntity<String> exchange = restTemplate.exchange(
                StrUtil.concat(true, "http://", GATEWAY_SERVICE, "/oauth/oauth/token?grant_type=password&client_id=client&client_secret=secret&username=admin&password=Nx0em52TvzkAJ7jv4sFhEuqp0Uc6AKnfjJDAeYxJgr3HxETGHziodz8UyiivgIJx/eGK3tnyi/mEeT11afEhqg=="),
                HttpMethod.POST,
                null,
                String.class
        );
        String body = exchange.getBody();
        Dict dict = Dict.parseJsonObject(body);
        if (ObjectUtil.isNotEmpty(dict) && dict.getStr("access_token") != null) {
            Token token = dict.toBeanWithCamelCase(Token.class);
            return token;
        }
        throw new UtilException("获取Token失败,请联系管理员!");
    }

    /**
     * 常用默认客户端模式获取Token
     * 只针对包含
     * 协议： http
     * 用户名：admin
     * 密码：Admin@123
     * client_id: client
     * client_secret: secret
     *
     * @return
     */
    public static Token genDefaultClientToken() {
        RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);
        ResponseEntity<String> exchange = restTemplate.exchange(
                StrUtil.concat(true, "http://", GATEWAY_SERVICE, "/oauth/oauth/token?grant_type=client_credentials&client_id=client&client_secret=secret"),
                HttpMethod.POST,
                null,
                String.class
        );
        String body = exchange.getBody();
        Dict dict = Dict.parseJsonObject(body);
        if (ObjectUtil.isNotEmpty(dict) && dict.getStr("access_token") != null) {
            Token token = dict.toBeanWithCamelCase(Token.class);
            return token;
        }
        throw new UtilException("获取Token失败,请联系管理员!");
    }

    /**
     * 按照自定义需求获取密码模式Token
     *
     * @param url          请求协议(http://172.28.8.102:8080/oauth/oauth/token)
     * @param username     用户名
     * @param password     密码(RSA加密后)
     * @param clientId     客户端id
     * @param clientSecret 客户端secret
     * @return
     */
    public static Token genPasswordToken(String url, String username, String password, String clientId, String clientSecret) {
        RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);
        ResponseEntity<String> exchange = restTemplate.exchange(
                StrUtil.concat(true, url, "?grant_type=password&client_id=", clientId, "&client_secret=", clientSecret, "&username=", username, "&password=", password),
                HttpMethod.POST,
                null,
                String.class
        );
        String body = exchange.getBody();
        Dict dict = Dict.parseJsonObject(body);
        if (ObjectUtil.isNotEmpty(dict) && dict.getStr("access_token") != null) {
            Token token = dict.toBeanWithCamelCase(Token.class);
            return token;
        }
        throw new UtilException("获取Token失败,请联系管理员!");
    }

    /**
     * 按照自定义需求获取客户端模式Token
     *
     * @param url          请求协议(http://172.28.8.102:8080/oauth/oauth/token)
     * @param clientId     客户端id
     * @param clientSecret 客户端secret
     * @return
     */
    public static Token genClientToken(String url, String clientId, String clientSecret) {
        RestTemplate restTemplate = SpringUtils.getBean(RestTemplate.class);
        ResponseEntity<String> exchange = restTemplate.exchange(
                StrUtil.concat(true, url, "?grant_type=client_credentials&client_id=", clientId, "&client_secret=", clientSecret),
                HttpMethod.POST,
                null,
                String.class
        );
        String body = exchange.getBody();
        Dict dict = Dict.parseJsonObject(body);
        if (ObjectUtil.isNotEmpty(dict) && dict.getStr("access_token") != null) {
            Token token = dict.toBeanWithCamelCase(Token.class);
            return token;
        }
        throw new UtilException("获取Token失败,请联系管理员!");
    }


}
