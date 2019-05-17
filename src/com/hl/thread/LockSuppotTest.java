package com.hl.thread;

import java.util.concurrent.locks.LockSupport;

/**
 * Created by Hanson on 2019/5/15 16:46
 * 用于测试LockSuppot的同步
 * LockSupport同步线程和wait/notify不一样，LockSupport并不需要获取对象的监视器，而是给线程一个 “许可”(permit)。而permit只能是0个或者1个。unpark会给线程一个permit，而且最多是1； 而park会消耗一个permit并返回，如果线程没有permit则会阻塞。
 *
 * park unpark与wait nofity区别
 * •	Wait nofity基于synchorized实现，需要获取monitor
 * •	unpark可以先于park调用。也就是我们在使用park和unpark的时候可以不用担心park的时序问题造成死锁。相比之下，wait/notify存在时序问题，wait必须在notify调用之前调用，否则虽然另一个线程 调用了notify，但是由于在wait之前调用了，wait感知不到，就造成wait永远在阻塞。
 */
public class LockSuppotTest {
    public static void main(String[] args) {
        Thread boyThread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("boy: 我要吃鸡");
                LockSupport.park();
                System.out.println("boy: park1");
                LockSupport.park(); // 第二次会阻塞住，因为只有一个permit
                System.out.println("boy: park2");
                System.out.println("boy: 开始吃鸡了");
            }
        });

        Thread girlThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("girl: 允许");
                LockSupport.unpark(boyThread); // unpark两次，但是permit不会叠加
                LockSupport.unpark(boyThread);
            }
        });

        boyThread.start();
        girlThread.start();
    }
}
