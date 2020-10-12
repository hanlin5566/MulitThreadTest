package com.hl.thread;

import java.util.concurrent.SynchronousQueue;

/**
 * Created by Hanson on 2019/6/3 18:13
 * SynchronousQueue是一个没有缓存的阻塞队列,put一个就需要等待take,反之亦然,否则会一直阻塞下去.
 * 可以理解为两个线程牵手.实现了线程间的一对一通信
 */
public class SynchronousQueueTest {
    public static void main(String[] args) {
        final SynchronousQueue<Integer> queue = new SynchronousQueue<Integer>();

        Thread putThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("put thread1 start");
                try {
                    queue.put(1);
                } catch (InterruptedException e) {
                }
                System.out.println("put thread1 end");
            }
        });

        Thread putThread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("put thread2 start");
                try {
                    queue.put(2);
                } catch (InterruptedException e) {
                }
                System.out.println("put thread2 end");
            }
        });

        Thread takeThread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("take thread1 start");
                try {
                    System.out.println("take from putThread1: " + queue.take());
                } catch (InterruptedException e) {
                }
                System.out.println("take thread1 end");
            }
        });

        putThread2.start();
        putThread1.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        takeThread1.start();
    }
}
