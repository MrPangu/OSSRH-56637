package com.pigic.hzeropigic.lock.impl;

import com.pigic.hzeropigic.lock.DistributedLocker;
import org.redisson.api.RFuture;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.TimeUnit;

/**
 * @Author: 潘顾昌
 * @Date: 2019/1/25 12:45
 */

public class DistributedLockerImpl implements DistributedLocker {

    private RedissonClient redissonClient;

    @Override
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    @Override
    public RLock lock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    @Override
    public RLock lock(String lockKey, TimeUnit unit ,int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    @Override
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public RFuture<Boolean> tryLockAsync(String lockKey, TimeUnit unit, int waitTime, int leaseTime,int threadId) {
        RLock lock = redissonClient.getLock(lockKey);
        RFuture<Boolean> rFuture = lock.tryLockAsync(waitTime, leaseTime, TimeUnit.SECONDS, threadId);
        return rFuture;
    }

    @Override
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    @Override
    public void unlock(RLock lock) {
        lock.unlock();
    }

    @Override
    public RLock fairLock(String lockKey) {
        RLock fairLock = redissonClient.getFairLock(lockKey);
        fairLock.lock();
        return fairLock;
    }

    @Override
    public RLock fairLock(String lockKey, int timeout) {
        RLock lock = redissonClient.getFairLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    @Override
    public RLock fairLock(String lockKey, TimeUnit unit, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    @Override
    public boolean tryFairLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getFairLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public RFuture<Boolean> tryFairLockAsync(String lockKey, TimeUnit unit, int waitTime, int leaseTime,int threadId) {
        RLock fairLock = redissonClient.getFairLock(lockKey);
        RFuture<Boolean> rFuture = fairLock.tryLockAsync(waitTime, leaseTime, TimeUnit.SECONDS, threadId);
        return rFuture;
    }

    @Override
    public void unlockFair(String lockKey) {
        RLock fairLock = redissonClient.getFairLock(lockKey);
        fairLock.unlock();
    }

    @Override
    public void unlockFair(RLock lock) {
        lock.unlock();
    }

    public void setRedissonClient(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }
}
