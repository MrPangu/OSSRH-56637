package com.pigic.hzeropigic.configration;

import io.choerodon.mybatis.pagehelper.domain.PageRequest;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.core.ResolvableType;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author guchang.pan@hand-china.com
 *
 */
@Component
public class CustomKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object o, Method method, Object... objects) {
        ResolvableType resolvableType1 = ResolvableType.forClass(o.getClass());
        Class<?> resolve2 = resolvableType1.getSuperType().getGeneric(0).resolve();
        StringBuilder stringBuilder=new StringBuilder();
        stringBuilder.append(resolve2.getName());
        stringBuilder.append(":"+method.getName());
        for (Object obj:objects) {
            if (obj instanceof PageRequest){
                PageRequest pageRequest = (PageRequest) obj;
                stringBuilder.append(":"+pageRequest.getPage());
                stringBuilder.append(":"+pageRequest.getSize());
                stringBuilder.append(":"+pageRequest.getSort().toString());
            }else{
                stringBuilder.append(":"+obj);
            }
        }
        String rediskey = stringBuilder.toString();
        return rediskey;
    }
}
