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
public class RWTest {
	
	public static final int sleepTime = 50;
	public static final int readCount = 3;
	public static final int writeCount = 2;
	
	private static ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);
    public static ReentrantReadWriteLock.ReadLock readLock = reentrantReadWriteLock.readLock();
    public static ReentrantReadWriteLock.WriteLock writeLock = reentrantReadWriteLock.writeLock();

	public static void main(String[] args) {
		final ReadSafeUnLock readSafe = new ReadSafeUnLock();
		final ThreadSafeWriteLock RWSafe = new ThreadSafeWriteLock();
		for (int i = 0; i < writeCount; i++) {
			Thread write = new Thread(new Runnable() {
				public void run() {
					String name = Thread.currentThread().getName();
					while (true) {
						RWTest.readLock.lock();
						int readSafeVar = readSafe.getVar();
						int RWSafeVar2 = RWSafe.getVar();
						RWTest.readLock.unlock();
						RWTest.writeLock.lock();
						readSafe.increment();
						RWSafe.increment();
						RWTest.writeLock.unlock();
//					System.err.println(String.format("Thread Name【%s】,r: 【%s】,rw: 【%s】 ,increment--r: 【%s】,rw: 【%s】", 
//							name,readSafeVar,RWSafeVar2,readSafe.getVar(),RWSafe.getVar()));
						RWTest.readLock.lock();
						int newReadSafeVar = readSafe.getVar();
						int newRWSafeVar2 = RWSafe.getVar();
						RWTest.readLock.unlock();;
						System.err.println(readSafeVar+"|"+RWSafeVar2+"|"+newReadSafeVar+"|"+newRWSafeVar2);
					}
				}
			});
			write.setName("threadWrite");
			write.start();
		}
		for (int i = 0; i < readCount; i++) {
			Thread read = new Thread(new Runnable() {
				public void run() {
					String name = Thread.currentThread().getName();
					while (true) {
						int readSafeVar = readSafe.getVar();
						int RWSafeVar2 = RWSafe.getVar();
//					System.out.println(String.format("Thread Name【%s】,r: 【%s】,rw: 【%s】", name,readSafeVar,RWSafeVar2));
						System.out.println(readSafeVar+"|"+RWSafeVar2);
					}
				}
			});
			read.setName("threadWrite");
			read.start();
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



class ReadSafeUnLock {
	private int var = 1;

	public int getVar() {
		RWTest.sleep();
		return var;
	}

	public void increment() {
		RWTest.sleep();
		var++;
	}
}

class ThreadSafeWriteLock {
	private int var = 1;

	public int getVar() {
		RWTest.sleep();
		return var;
	}

	public void increment() {
		RWTest.sleep();
		var++;
	}
}
