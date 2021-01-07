package com.pigic.hzeropigic.configration;

/**
 * @author guchang.pan@hand-china.com
 *
 */

import com.pigic.hzeropigic.lock.DistributedLocker;
import com.pigic.hzeropigic.lock.impl.DistributedLockerImpl;
import com.pigic.hzeropigic.properties.RedissonProperties;
import com.pigic.hzeropigic.utils.CookieUtils;
import com.pigic.hzeropigic.utils.LockUtil;
import org.apache.commons.lang.StringUtils;
import org.hzero.lock.autoconfigurer.LockAutoConfiguration;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnSingleCandidate;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Configuration
@ConditionalOnClass(LockAutoConfiguration.class)
public class RedissonAutoConfiguration {

    /**
     * 装配locker类，并将实例注入到RedissLockUtil中
     * @return
     */
    @ConditionalOnClass(RedissonClient.class)
    @Bean
    DistributedLocker distributedLocker(@Qualifier("lockRedissonClient") @Autowired RedissonClient redissonClient) {
        DistributedLockerImpl locker = new DistributedLockerImpl();
        locker.setRedissonClient(redissonClient);
        LockUtil.setLocker(locker);
        return locker;
    }

}
