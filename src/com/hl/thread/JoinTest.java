package com.hl.thread;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

/**
 * Create by hanlin on 2019年3月14日
 * join 放到线程池中会失效，线程放入到线程池后，线程的生命周期就交由线程池管理了，线程结束并不依赖于线程运行的结束，join就失效了。
 * 可以使用
 * 1.CountDownlatch（计数器）初始总数，线程执行完-1 CountDownlatch.countDown，等待全部执行结束时latch.await()，则继续执行之后的代码。
 * 2.Semaphore(信号量)，类似于许可（锁），可以使用acquire占用一个锁，和release释放一个锁。
 * 3.cyclicBarrier(复用栅栏-可以用于批处理操作)，类似于一个栅栏，当等待的线程达到设定的数量时，才放行，继续执行。可以通过第二个参数指定放行后执行的线程。
 **/
public class JoinTest {
	SimpleDateFormat sdf = new SimpleDateFormat("HH:MM:ss");
	
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		JoinTest test = new JoinTest();
//		test.join();
//		test.joinUnAvailable();
//		test.singleThreadPool();
//		test.semaphore();
//		test.countDownLatch();
//		test.cyclicBarrier();
		test.invokeAll();
	}
	
	public void join() throws InterruptedException {
		final Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("t1 start @"+sdf.format(new Date()));
					Thread.sleep(2000);
					System.out.println("t1 end @"+sdf.format(new Date()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("t2 start @"+sdf.format(new Date()));
					//t2等待t1执行结束，再继续执行
					t1.join();
					Thread.sleep(5000);
					System.out.println("t2 end @"+sdf.format(new Date()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		t1.start();
		t2.start();
		//主线程等待t2结束再执行
		t2.join();
		System.out.println("process end @"+sdf.format(new Date()));
	}
	/**
	 * 模拟放入线程池，线程间join失效的情况。
	 * @throws InterruptedException
	 * t2 start @16:03:24
	 * t1 start @16:03:24
	 * t1 end @16:03:26 //t1休眠2秒
	 * t2 end @16:03:29 //t2未等待t1结束,直接执行休眠5秒，此处join失效
	 * process end @16:03:20 //主线程等待latch为0
	 */
	public void joinUnAvailable() throws InterruptedException{
		ExecutorService exectors = Executors.newCachedThreadPool();
		final CountDownLatch latch = new CountDownLatch(2);
		final Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("t1 start @"+sdf.format(new Date()));
					Thread.sleep(2000);
					System.out.println("t1 end @"+sdf.format(new Date()));
					latch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("t2 start @"+sdf.format(new Date()));
					//t2等待t1执行结束，再继续执行
					t1.join();
					Thread.sleep(5000);
					System.out.println("t2 end @"+sdf.format(new Date()));
					latch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		exectors.submit(t1);
		exectors.submit(t2);
		latch.await();
		System.out.println("process end @"+sdf.format(new Date()));
		exectors.shutdown();
	}
	
	/**
	 * countDownLatch的使用方法
	 * CountDownlatch（计数器）初始总数，线程执行完-1 CountDownlatch.countDown，等待全部执行结束时latch.await()，则继续执行之后的代码。
	 */
	public void countDownLatch() throws InterruptedException{
		ExecutorService exectors = Executors.newCachedThreadPool();
		final CountDownLatch latch = new CountDownLatch(2);
		final Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("t1 start @"+sdf.format(new Date()));
					Thread.sleep(2000);
					System.out.println("t1 end @"+sdf.format(new Date()));
					latch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("t2 start @"+sdf.format(new Date()));
					//t2等待t1执行结束，再继续执行
					t1.join();
					Thread.sleep(5000);
					System.out.println("t2 end @"+sdf.format(new Date()));
					latch.countDown();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		exectors.submit(t1);
		exectors.submit(t2);
		latch.await();
		System.out.println("process end @"+sdf.format(new Date()));
		exectors.shutdown();
	}
	/**
	 * semaphore用法
	 * Semaphore(信号量)，类似于许可（锁），可以使用acquire占用一个锁，和release释放一个锁。
	 * 公平锁，可以通过构造用的fair指定是否通过等待时间排优先级
	 * fair true if this semaphore will guaranteefirst-in first-out granting of permits under contention,else false
	 * @throws InterruptedException
	 */
	public void semaphore() throws InterruptedException{
		ExecutorService exectors = Executors.newCachedThreadPool();
		final Semaphore semaphore = new Semaphore(1); 
		final Thread t1 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("t1 start @"+sdf.format(new Date()));
					semaphore.acquire();
					System.out.println("t1 get acquire@"+sdf.format(new Date()));
					Thread.sleep(2000);
					System.out.println("t1 end @"+sdf.format(new Date()));
					semaphore.release();
					//占用许可，其他线程需要等待。
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		Thread t2 = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					System.out.println("t2 start @"+sdf.format(new Date()));
					semaphore.acquire();
					System.out.println("t2 get acquire@"+sdf.format(new Date()));
					Thread.sleep(5000);
					semaphore.release();
					System.out.println("t2 end @"+sdf.format(new Date()));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		exectors.submit(t1);
		exectors.submit(t2);
		semaphore.acquire();
		System.out.println("main get acquire@"+sdf.format(new Date()));
		System.out.println("process end @"+sdf.format(new Date()));
		semaphore.release();
		exectors.shutdown();
	}
	
	/**
	 * cyclicBarrier用法
	 * cyclicBarrier(复用栅栏)，类似于一个栅栏，当等待的线程达到设定的数量时，才放行，继续执行。可以通过第二个参数指定放行后执行的线程。
	 * @throws InterruptedException
	 */
	public void cyclicBarrier() throws InterruptedException{
		ExecutorService exectors = Executors.newCachedThreadPool();
		final CyclicBarrier cyclicBarrier = new CyclicBarrier(5);
		int N = 10;
		final CountDownLatch latch = new CountDownLatch(N);
		for (int i = 0; i < N; i++) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						System.out.println(Thread.currentThread().getName()+" enter barrier @"+sdf.format(new Date()));
						Thread.sleep(1000);
						cyclicBarrier.await();
						System.out.println(Thread.currentThread().getName()+" out barrier@"+sdf.format(new Date()));
						latch.countDown();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}
				}
			},"sheep"+i);
			exectors.submit(t);
		}
		latch.await();
	}
	
	/**
	 * 内部是一个FIFO队列，保证线程顺序执行
	 */
	public void singleThreadPool() {
		final Thread t1 = new Thread(new Runnable() {
            public void run() {
                System.out.println(Thread.currentThread().getName() + " run 1");
            }
        }, "T1");
        final Thread t2 = new Thread(new Runnable() {
            public void run() {
                try {
                    t1.join();
                    System.out.println(Thread.currentThread().getName() + " run 2");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "T2");
        final Thread t3 = new Thread(new Runnable() {
            public void run() {
                try {
                    t2.join();
                    System.out.println(Thread.currentThread().getName() + " run 3");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "T3");
//        t1.start();
//        t2.start();
//        t3.start();
 
//        method 2 使用 单个任务的线程池来实现。保证线程的依次执行
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(t1);
        executor.submit(t2);
        executor.submit(t3);
        executor.shutdown();
	}
	
	public void invokeAll() throws InterruptedException, ExecutionException {
		ArrayList<Callable<String>> callers = new ArrayList<Callable<String>>();
		int N = 100;
		for (int i = 0; i < N; i++) {
			Callable<String> callable = new Callable<String>() {
				@Override
				public String call() throws Exception {
					System.out.println(Thread.currentThread().getName() + "start @"+sdf.format(new Date()));
					Thread.sleep((long) (Math.random()*1000));
					return Thread.currentThread().getName() + "end"+sdf.format(new Date());
				}
			};
			callers.add(callable);
		}
		ExecutorService exector = Executors.newCachedThreadPool();
		List<Future<String>> invokeAll = exector.invokeAll(callers);
		for (Future<String> future : invokeAll) {
			String ret = future.get();
			System.err.println(ret);
		}
		exector.shutdown();
	}
	/**
	 * Fork Join 實例
	 */
	public void forkJoin() {
		
	}
}