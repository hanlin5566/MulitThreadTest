package com.hl.thread;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Hanson on 2019/5/17 18:18
 * 线程A请求AB资源,线程B请求BA资源.模拟死锁问题.
 *
 * 解决死锁,让线程A与线程B请求资源为相同顺序,ABAB或者BABA,如果ABBA或者BAAB都会出现死锁.
 */
public class DeadLockedThread {

    public static void main(String[] args) {
        Object work = new Object();
        Object experience = new Object();
        AtomicBoolean hasWork = new AtomicBoolean(false);

        Thread jobhunter = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    synchronized (work){
                        System.out.println("我得到了工作,准备获得经验.");
                        synchronized (experience){
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            System.out.println("我正在工作,涨经验");
                        }
                    }
                }
            }
        });

        Thread manager = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    synchronized (experience){
                        System.err.println("我需要经验才能给你工作.");
                        synchronized (work){
                            System.err.println("你有经验了,我给你工作.");
                        }
                    }
                }
            }
        });

        jobhunter.start();
        manager.start();
    }
}
