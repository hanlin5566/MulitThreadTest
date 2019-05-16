package com.hl.thread;

/**
 * @Auther: Hanson
 * @Date: 2019/5/16 22:57
 * @Description:
 */
public class SleepAndWaitTest {
    public static void main(String [] args){
        SleepAndWaitTest lock = new SleepAndWaitTest();
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    long s = System.currentTimeMillis();
                    System.out.println("方法1执行"+s);
                    try {
//                        Thread.sleep(2000);//不释放锁
//                        lock.wait();//释放锁,可以让线程t2继续执行
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.out.println("方法1执行结束"+(System.currentTimeMillis()-s));
                }
            }
        });

        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (lock){
                    long s = System.currentTimeMillis();
                    System.out.println("方法2执行"+s);
                    System.out.println("方法2执行结束"+(System.currentTimeMillis()-s));
//                    lock.notify();获得锁,执行结束唤醒线程t1
                }
            }
        });

        t1.setPriority(10);
        t2.setPriority(1);
        t1.start();
        t2.start();
    }
}
