package com.hl.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Create by hanlin on 2018年1月17日
<<<<<<< HEAD
 * 用于测试，类中的成员变量为静态时，导致的线程不安全问题。
=======
 * 测试 对象的静态私有变量一致性问题，静态变量会引发一致性问题，两个不同
>>>>>>> c42bf39baa6e566e6f2800a27cc30d58269a398a
 **/
public class StaticTest {
	public static void main(String[] args) throws InterruptedException {
		int count = 100;
		CountDownLatch latch = new CountDownLatch(count);
		for (int i = 1; i <count; i++){
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					Bean b = new Bean();
					b.name();
				}
			});
			t.setName("Thread"+i);
			t.start();
		}
		latch.await();
	}
}
