package com.hl.thread.sync;

import java.util.Random;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * volatile修饰符，保证线程间的可见性，但不保证原子性（写操作跳过CPU缓存，直接写入内存，线程的操作立刻可见。）,类似于读安全，多个写会出问题。
 * synchronized修饰符，任意时刻只有一个线程能够访问此代码块，配合wait，notify进行唤醒与阻塞操作。
 * Lock 手动控制锁，lock、unlock
 * Atomic 原子变量，保证操作都为原子操作，
 * 模拟读写锁请求
 * @author huhl
 *
 */
public class LockTest {
	
	public static final int sleepTime = 1;
	public static final int readCount = 20;
	public static final int writeCount = 30;
	
	private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);
    public static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    public static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

	public static void main(String[] args) {
		final ThreadSafe ts = new ThreadSafe();
		
		for (int i = 0; i < readCount; i++) {
			Thread read = new Thread(new Runnable() {
				public void run() {
					String name = Thread.currentThread().getName();
					int readSafeVar = ts.getVar();
					System.out.println("Thread:"+name+"v:"+readSafeVar);
				}
			});
			read.setName("threadWrite"+i);
			read.start();
		}
		
		for (int i = 0; i < writeCount; i++) {
			Thread write = new Thread(new Runnable() {
				public void run() {
					String name = Thread.currentThread().getName();
					ts.increment();
					System.err.println("Thread:"+name+"increment");
				}
			});
			write.setName("threadWrite"+i);
			write.start();
		}
	}
	
	public static void sleep(){
		try {
			Random random = new Random();
			int i = random.nextInt(sleepTime);
			Thread.sleep(Long.parseLong(i+""));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}



class ThreadSafe {
	private int var = 1;

	public int getVar() {
		LockTest.readLock.lock();
		LockTest.sleep();
		System.out.println("read....");
		LockTest.readLock.unlock();
		return var;
	}

	public void increment() {
		LockTest.writeLock.lock();
		LockTest.sleep();
		var++;
		System.err.println("write....");
		LockTest.writeLock.unlock();
	}
}
