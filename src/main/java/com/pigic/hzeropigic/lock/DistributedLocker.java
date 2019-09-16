package com.pigic.hzeropigic.lock;

/**
 * @Author: 潘顾昌
 * @Date: 2019/1/25 12:31
 */

import org.redisson.api.RFuture;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

public interface DistributedLocker {

    RLock lock(String lockKey);

    RLock lock(String lockKey, int timeout);

    RLock lock(String lockKey, TimeUnit unit, int timeout);

    boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    RFuture<Boolean> tryLockAsync(String lockKey, TimeUnit unit, int waitTime, int leaseTime, int threadId);

    void unlock(String lockKey);

    void unlock(RLock lock);

    RLock fairLock(String lockKey);

    RLock fairLock(String lockKey, int timeout);

    RLock fairLock(String lockKey, TimeUnit unit, int timeout);

    boolean tryFairLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime);

    RFuture<Boolean> tryFairLockAsync(String lockKey, TimeUnit unit, int waitTime, int leaseTime, int threadId);

    void unlockFair(String lockKey);

    void unlockFair(RLock lock);
}