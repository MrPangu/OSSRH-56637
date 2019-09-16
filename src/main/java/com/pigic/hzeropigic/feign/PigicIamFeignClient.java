package com.pigic.hzeropigic.feign;

import cn.pigicutils.core.lang.Dict;
import com.pigic.hzeropigic.api.vo.UserVO;
import com.pigic.hzeropigic.feign.fallback.PigicIamFeignClientFallbackFactory;
import feign.hystrix.FallbackFactory;
import io.swagger.annotations.ApiParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/*
 * iam的接口
 * @date: 2019/9/10
 * @author: 潘顾昌 <guchang.pan@hand-china.com>
 * @version: 0.0.1
 * @copyright Copyright (c) 2019, Hand
 */
@FeignClient(
        value = "hzero-iam",
        fallbackFactory = PigicIamFeignClientFallbackFactory.class
)
public interface PigicIamFeignClient {
    /**
     * 登录用户 - 查询自身基础信息
     * @return
     */
    @GetMapping("/hzero/v1/users/self")
    UserVO selectSelf();

    /**
     * 登录用户 - 个人中心查询详细信息
     * @return
     */
    @GetMapping("/hzero/v1/users/self/detail")
    UserVO selectSelfDetail();
}
