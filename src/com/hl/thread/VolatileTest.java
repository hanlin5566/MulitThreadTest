package com.hl.thread;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Create by hanlin on 2019年1月25日
 * volatitle 只保证可见性，直接写入主存，不写入高速缓存，所以所有线程保证对其修改的可见。
 **/
public class VolatileTest implements Runnable{
		private int i = 0;//加 volatile 后程序无影响
		public int getValue(){
			return i;
		}
		private synchronized void evenIncrement(){
			i++;
			i = 0;
			i++;
		}
		@Override
		public void run() {
			while(true){
				evenIncrement();
			}
		}
		public static void main(String[] args) {
//			ExecutorService exec = Executors.newCachedThreadPool();
//			VolatileTest vt = new VolatileTest();
//			exec.execute(vt);
//			while(true){
//				int val = vt.getValue();
//				System.out.println(val);
//				if(val == 1){
//					System.out.print(val);
//					System.exit(0);
//				}
//			}
			VolatileTest volatileTest = new VolatileTest();
			int threadCount = 5;
			CountDownLatch countDownLatch = new CountDownLatch(2*threadCount);
			Reordering reordering = volatileTest. new Reordering();
			for (int i = 0; i < threadCount; i++) {

				Thread writerThread = new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println(Thread.currentThread().getName()+"writer run");
						reordering.writer();
						countDownLatch.countDown();
						System.out.println(Thread.currentThread().getName()+"writer end");
					}
				});
				writerThread.setName("I'm "+i+" writer");
				Thread readerThread = new Thread(new Runnable() {
					@Override
					public void run() {
						System.out.println(Thread.currentThread().getName()+"reader end");
						reordering.reader();
						countDownLatch.countDown();
						System.out.println(Thread.currentThread().getName()+"reader end");
					}
				});
				readerThread.setName("I'm "+i+" reader");

				writerThread.start();
				readerThread.start();
			}

			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	class Reordering {
		int x = 0, y = 0;
		public void writer() {
			x = 1;
			y = 2;
		}

		public void reader() {
			int r1 = y;
			int r2 = x;
			System.err.println(Thread.currentThread().getName()+" r1 --> y "+ r1);
			System.err.println(Thread.currentThread().getName()+" r2 --> x "+ r2);
		}
	}
}
