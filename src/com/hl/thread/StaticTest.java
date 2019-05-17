package com.hl.thread;
/**
 * Create by hanlin on 2018年1月17日
 * 用于测试，类中的成员变量为静态时，导致的线程不安全问题。
 **/
public class StaticTest {
	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i <100; i++){
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
		
		Thread.sleep(10000);
	}
}
