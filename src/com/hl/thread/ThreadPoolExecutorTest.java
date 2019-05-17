package com.hl.thread;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Hanson on 2019/5/14 18:23
 * 参数
 * corePoolSize	核心线程池大小 平时保留的线程数
 * maximumPoolSize	最大线程池大小 当workQueue都放不下时，启动新线程，最大线程数
 * keepAliveTime	线程池中超过corePoolSize数目的空闲线程最大存活时间；可以allowCoreThreadTimeOut(true)使得核心线程有效时间
 * TimeUnit	keepAliveTime时间单位
 * workQueue：阻塞队列，存放来不及执行的线程
 *      * ArrayBlockingQueue：构造函数一定要传大小
 *      * LinkedBlockingQueue：构造函数不传大小会默认为（Integer.MAX_VALUE ），当大量请求任务时，如果任务提交速度持续大余任务处理速度，会造成队列大量阻塞，容易造成 内存耗尽。
 *          newFixedThreadPool使用此队列，并且core和max是一样大小，适用场景：可用于Web服务瞬时削峰，但需注意长时间持续高峰情况造成的队列阻塞。
 *      * SynchronousQueue：这个队列类似于一个接力棒，入队出队必须同时传递，因为CachedThreadPool线程创建无限制，不会有队列等待，所以使用SynchronousQueue；
 *          CachedThreadPool 使用此队列,核心线程数=0，max=最大，超时时间=60s，使用场景：快速处理大量耗时较短的任务，如Netty的NIO接受请求时，可使用
 *      * PriorityBlockingQueue : 优先队列
 * threadFactory	新建线程工厂
 * RejectedExecutionHandler	当提交任务数超过maxmumPoolSize+workQueue之和时，任务会交给RejectedExecutionHandler来处理
 *    * AbortPolicy（默认）：直接抛弃
 *    * CallerRunsPolicy：用调用者的线程执行任务
 *    * DiscardOldestPolicy：抛弃队列中最久的任务
 *    * DiscardPolicy：抛弃当前任务
 *
 * 测试线程池的运行规则
 * 如果工作线程(workCount) < 核心线程(corePoolSize)，则创建新线程执行
 * 否则查看队列是否已满，未满放入队列，如果已满，并且超出MAX则执行reject策略，未超出则创建线程执行，当线程存活时间>设定时间则销毁线程，core线程不被销毁。
 */
public class ThreadPoolExecutorTest {
    private static AtomicInteger count = new AtomicInteger();
    private static volatile int finishState = 0;

    public static void main(String[] args) throws Exception{
        BlockingQueue workQueue = new LinkedBlockingQueue(5);
//        Executors.newFixedThreadPool(5);
//        Executors.newCachedThreadPool();
        MineThreadFactory mineThreadFactory = new MineThreadFactory(count);
        MineRejectedHandler rejectedHandler = new MineRejectedHandler();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2,5,5, TimeUnit.SECONDS,workQueue,mineThreadFactory,rejectedHandler);
        ThreadPoolExecutorTest lock = new ThreadPoolExecutorTest();

        ExecutorCompletionService<String> executorCompletionService = new ExecutorCompletionService(threadPoolExecutor);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 50; i++) {
                    String name = "name_" + i;
                    TestCallable testCallable = new TestCallable(name);
                    try {
                        executorCompletionService.submit(testCallable);
                        synchronized (lock) {
                            System.out.print("+++添加任务 name: " + name);
                            System.out.print(" ActiveCount: " + threadPoolExecutor.getActiveCount());
                            System.out.print(" poolSize: " + threadPoolExecutor.getPoolSize());
                            System.out.print(" queueSize: " + threadPoolExecutor.getQueue().size());
                            System.out.println(" taskCount: " + threadPoolExecutor.getTaskCount());
                        }
                    } catch (RejectedExecutionException e) {
                        synchronized (this) {
                            System.out.println("拒绝：" + name);
                        }
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                finishState = 1;
            }
        };

        Thread addThread = new Thread(runnable);
        addThread.start();

        //System.out.println(" taskCount: " + threadPoolExecutor.getTaskCount());

        //添加的任务有被抛弃的。taskCount不一定等于添加的任务。
        int completeCount = 0;
        while (!(completeCount == threadPoolExecutor.getTaskCount() && finishState == 1)) {
            Future<String> take = executorCompletionService.take();
            String taskName = take.get();
            synchronized (lock) {
                System.out.print("---完成任务 name: " + taskName);
                System.out.print(" ActiveCount: " + threadPoolExecutor.getActiveCount());
                System.out.print(" poolSize: " + threadPoolExecutor.getPoolSize());
                System.out.print(" queueSize: " + threadPoolExecutor.getQueue().size());
                System.out.print(" taskCount: " + threadPoolExecutor.getTaskCount());
                System.out.println(" finishTask：" + (++completeCount));

            }
        }

        addThread.join();


        while (threadPoolExecutor.getPoolSize() > 0) {
            Thread.sleep(5000);
            synchronized (lock) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                System.out.print(simpleDateFormat.format(new Date()));
                //System.out.print("name: " + taskName);
                System.out.print(" ActiveCount: " + threadPoolExecutor.getActiveCount());
                System.out.print(" poolSize: " + threadPoolExecutor.getPoolSize());
                System.out.print(" queueSize: " + threadPoolExecutor.getQueue().size());
                System.out.println(" taskCount: " + threadPoolExecutor.getTaskCount());
            }
        }

        // Tell threads to finish off.
        threadPoolExecutor.shutdown();
        // Wait for everything to finish.
        while (!threadPoolExecutor.awaitTermination(10, TimeUnit.SECONDS)) {
            System.out.println("complete");
        }

    }

    static class TestCallable implements Callable<String>{


        private String name;

        public TestCallable(String name) {
            this.name = name;
        }

        @Override
        public String call() throws Exception {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return this.name;
        }
    }

    static class MineThreadFactory implements ThreadFactory{
        AtomicInteger count;
        public MineThreadFactory(AtomicInteger count) {
            this.count = count;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            int andIncrement = count.getAndIncrement();
            t.setName("T"+andIncrement);
            return t;
        }
    }

    static class MineRejectedHandler implements RejectedExecutionHandler{
        public MineRejectedHandler() {
        }

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {

            System.out.println("拒绝：" + new Thread(r).getName());
            throw new RejectedExecutionException();
        }
    }
}
