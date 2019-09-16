package com.pigic.hzeropigic.utils;

/**
 * @Author: 潘顾昌
 * @Date: 2019/1/26 10:22
 */


import com.pigic.hzeropigic.lock.DistributedLocker;
import org.redisson.api.RFuture;
import org.redisson.api.RLock;

import java.util.concurrent.TimeUnit;

/**
 * redis分布式锁帮助类
 * @author 潘顾昌
 *
 */
public class LockUtil {
    private static DistributedLocker redissLock;

    public static void setLocker(DistributedLocker locker) {
        redissLock = locker;
    }

    /**
     * 加锁
     * @param lockKey
     * @return
     */
    public static RLock lock(String lockKey) {
        return redissLock.lock(lockKey);
    }

    /**
     * 释放锁
     * @param lockKey
     */
    public static void unlock(String lockKey) {
        redissLock.unlock(lockKey);
    }

    /**
     * 释放锁
     * @param lock
     */
    public static void unlock(RLock lock) {
        redissLock.unlock(lock);
    }

    /**
     * 拿不到锁持续等待，拿到锁后，不手动关闭锁，锁timeout秒后自动关闭
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public static RLock lock(String lockKey, int timeout) {
        return redissLock.lock(lockKey, timeout);
    }

    /**
     * 带超时的锁
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     */
    public static RLock lock(String lockKey, TimeUnit unit ,int timeout) {
        return redissLock.lock(lockKey, unit, timeout);
    }

    /**
     * 尝试获取锁,waitTime获取不到则不再继续等待，主动放弃，拿到锁后持续leaseTime时间自动解锁
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        return redissLock.tryLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
    }

    /**
     * 尝试获取锁
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        return redissLock.tryLock(lockKey, unit, waitTime, leaseTime);
    }

    /**
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @param threadId 线程Id
     * @return
     */
    public static RFuture<Boolean> tryLockAsync(String lockKey, TimeUnit unit, int waitTime, int leaseTime, int threadId) {
        RFuture<Boolean> rFuture = redissLock.tryLockAsync(lockKey, unit, waitTime, leaseTime, threadId);
        return rFuture;
    }

//公平锁

    /**
     * 公平锁
     * 加锁
     * @param lockKey
     * @return
     */
    public static RLock fairLock(String lockKey) {
        return redissLock.fairLock(lockKey);
    }

    /**
     * 公平锁
     * 释放锁
     * @param lockKey
     */
    public static void unfairLock(String lockKey) {
        redissLock.unlockFair(lockKey);
    }

    /**
     * 公平锁
     * 释放锁
     * @param lock
     */
    public static void unfairLock(RLock lock) {
        redissLock.unlockFair(lock);
    }

    /**
     * 公平锁
     * 带超时的锁
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public static RLock fairLock(String lockKey, int timeout) {
        return redissLock.fairLock(lockKey, timeout);
    }

    /**
     * 公平锁
     * 带超时的锁
     * @param lockKey
     * @param unit 时间单位
     * @param timeout 超时时间
     */
    public static RLock fairLock(String lockKey, TimeUnit unit ,int timeout) {
        return redissLock.fairLock(lockKey, unit, timeout);
    }

    /**
     * 公平锁
     * 尝试获取锁
     * @param lockKey
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryFairLock(String lockKey, int waitTime, int leaseTime) {
        return redissLock.tryFairLock(lockKey, TimeUnit.SECONDS, waitTime, leaseTime);
    }

    /**
     * 公平锁
     * 尝试获取锁
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public static boolean tryFairLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        return redissLock.tryFairLock(lockKey, unit, waitTime, leaseTime);
    }

    /**
     * 公平锁
     * @param lockKey
     * @param unit 时间单位
     * @param waitTime 最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @param threadId 线程Id
     * @return
     */
    public static RFuture<Boolean> tryFairLockAsync(String lockKey, TimeUnit unit, int waitTime, int leaseTime, int threadId) {
        RFuture<Boolean> rFuture = redissLock.tryFairLockAsync(lockKey, unit, waitTime, leaseTime, threadId);
        return rFuture;
    }



}
