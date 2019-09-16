package com.pigic.hzeropigic.feign.fallback;

import cn.pigicutils.core.lang.Dict;
import com.pigic.hzeropigic.api.vo.UserVO;
import com.pigic.hzeropigic.feign.PigicIamFeignClient;
import feign.hystrix.FallbackFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author: 潘顾昌
 * @Date: 2019/9/10 10:39
 */
@Component
public class PigicIamFeignClientFallbackFactory implements FallbackFactory<PigicIamFeignClient> {
    private Logger logger = LoggerFactory.getLogger(PigicIamFeignClientFallbackFactory.class);
    @Override
    public PigicIamFeignClient create(Throwable cause) {
        return new PigicIamFeignClient() {
            @Override
            public UserVO selectSelf() {
                this.logError();
                return new UserVO();
            }

            @Override
            public UserVO selectSelfDetail() {
                this.logError();
                return new UserVO();
            }

            private void logError() {
                PigicIamFeignClientFallbackFactory.this.logger.error("can not get response from hzero-iam, cause by");
                PigicIamFeignClientFallbackFactory.this.logger.error(cause.getMessage(), cause);
            }
        };
    }
}
