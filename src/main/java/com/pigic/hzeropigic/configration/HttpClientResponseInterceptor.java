package com.pigic.hzeropigic.configration;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.pigic.hzeropigic.domain.entity.InterfaceMonitor;
import com.pigic.hzeropigic.domain.entity.InterfaceRegister;
import com.pigic.hzeropigic.feign.HalmPigicAmdmFeginClient;
import com.pigic.hzeropigic.utils.*;
import io.choerodon.core.oauth.CustomUserDetails;
import io.choerodon.core.oauth.DetailsHelper;
import jodd.util.ArraysUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Map;

/**
 * @Author: 潘顾昌
 * @Date: 2019/7/1 20:34
 */
@Slf4j
public class HttpClientResponseInterceptor implements HttpResponseInterceptor {
    @SneakyThrows
    @Override
    public void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        try {
            CustomUserDetails userDetails = DetailsHelper.getUserDetails();
            Long tenantId=0L;
            if (userDetails !=null){
                if (userDetails.getTenantId()!=null){
                    tenantId = userDetails.getTenantId();
                }else if (userDetails.getOrganizationId()!=null){
                    tenantId =userDetails.getOrganizationId();
                }else{
                    tenantId=0L;
                }
            }
            HttpCoreContext adapt = HttpCoreContext.adapt(httpContext);
            HttpRequest httpRequest = adapt.getRequest();
            String uri = httpRequest.getRequestLine().getUri();
            String method = httpRequest.getRequestLine().getMethod();
            Header[] allHeaders = httpRequest.getAllHeaders();
            HttpHost targetHost = adapt.getTargetHost();
            String toURI = targetHost.toURI();
            InterfaceRegister register = new InterfaceRegister();
            String fullURL = toURI + uri;
            String registerUrl = fullURL.substring(0, fullURL.indexOf("?")==-1?fullURL.length():fullURL.indexOf("?"));
            register.setRequestUrl(registerUrl);
            HalmPigicAmdmFeginClient halmPigicAmdmFeginClient = SpringUtils.getBean(HalmPigicAmdmFeginClient.class);
//            InterfaceRegisterService registerService = SpringUtils.getBean(InterfaceRegisterService.class);
//            InterfaceMonitorService interfaceMonitorService = SpringUtils.getBean(InterfaceMonitorService.class);

            Map map = halmPigicAmdmFeginClient.interfaceRegistersselectOne(tenantId, ObjectUtils.objectNotNullFieldToMap(register));
            InterfaceRegister interfaceRegister= (InterfaceRegister) MapToObjectConvert.mapToObject(map, InterfaceRegister.class);
//            InterfaceRegister interfaceRegister = registerService.selectOne(register);
            InterfaceMonitor interfaceMonitor = new InterfaceMonitor();
            HttpEntity entity = httpResponse.getEntity();
            String string = EntityUtils.toString(entity);
            EntityUtils.updateEntity(httpResponse, new StringEntity(string,"utf-8"));
            String code="200";
            try {
                JSONObject jsonObject = JSON.parseObject(string);
                code = jsonObject.getString("code");
            } catch (Exception e) {
                try {
                    JSONArray jsonArray = JSON.parseArray(string);
                    code="200";
                } catch (Exception e1) {
                    code="500";
                }
            }

            if (interfaceRegister!=null &&interfaceRegister.getEnabledFlag()==1){
                if (interfaceRegister.getShowHeader()==1){
                    interfaceMonitor.setRequestHeader(ArraysUtil.toString(allHeaders));
                }
                if (interfaceRegister.getShowParam()==1){
                    uri= URLDecoder.decode(uri, "UTF-8");
                    interfaceMonitor.setRequestParam(ParseUrlUtil.parser(uri).toString());
                }
                if ("post".equalsIgnoreCase(method)){
                    Object postBody = TempUtils.getContainer().get("postBody");
                    if (postBody!=null){
                        if (postBody instanceof Map) {
                            if (interfaceRegister.getShowBody() == 1) {
                                interfaceMonitor.setRequestBody(postBody.toString());
                            }
                        }else if (postBody instanceof String){
                            if (interfaceRegister.getShowBody() == 1) {
                                interfaceMonitor.setRequestBody((String) postBody);
                            }
                        }
                    }else{
                        log.warn("请使用HTTPUtils访问!!!不支持的Http访问方式");
                    }
                }
                if (interfaceRegister.getShowResponseCode()==1){
                    if (code!=null) {
                        interfaceMonitor.setResponseCode(Long.valueOf(code));
                    }else{
                        interfaceMonitor.setResponseCode(200L);
                    }
                }
                if (interfaceRegister.getShowResponse()==1){
                    interfaceMonitor.setResponseBody(string);
                }
                int status=1;
                if (code != null){
                    if (code.equals("400")||code.equals("500")||code.equals("404")||code.equals("401")||code.equals("403")){
                        status=0;
                    }
                }
                interfaceMonitor.setResponseStatus(status);
                interfaceMonitor.setRequestUrl(registerUrl);
                interfaceMonitor.setInterfaceName(interfaceRegister.getInterfaceName());
                interfaceMonitor.setRequestMethod(method);
                interfaceMonitor.setRegisterId(interfaceRegister.getInterfaceId());
                interfaceMonitor.setTenantId(interfaceRegister.getTenantId());
                Map<String, Object> map1 = ObjectUtils.objectNotNullFieldToMap(interfaceMonitor);
                halmPigicAmdmFeginClient.interfaceMonitorscreate(tenantId, map1);
//                interfaceMonitorService.insertSelective(interfaceMonitor);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (UnsupportedCharsetException e) {
            e.printStackTrace();
        } finally {
            TempUtils.getContainer().remove("postBody");
        }
    }
}
