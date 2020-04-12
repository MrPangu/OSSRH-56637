package cn.pigicutils.core.thread.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * 无锁实现
 * 
 * @author guchang.pan@hand-china.com
 *
 */
public class NoLock implements Lock{

	@Override
	public void lock() {
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
	}

	@Override
	public boolean tryLock() {
		return true;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return true;
	}

	@Override
	public void unlock() {
	}

	@Override
	public Condition newCondition() {
		return null;
	}

}
