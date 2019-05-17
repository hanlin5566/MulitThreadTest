package com.hl.thread;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Hanson on 2019/5/15 13:45
 * 对象都在哪里分配内存
 * 方法体中的引用变量和基本类型的变量都在栈上，其他都在堆上。
 * 字符串运行时常量池：1.7方法区，1.8堆
 * 成员变量堆上
 * 静态变量或者常量方法区
 * 局部变量虚拟机栈的局部变量表
 */
public class AllotTest {
    volatile boolean isFirst = true;
    public static void main(String[] args){
        AllotTest allotTest = new AllotTest();
        AllotTest.B b = allotTest.new B();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("t1 start");
                for (;;){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("t1 b.i="+b.i);
                    System.out.println("t1 b.c.j="+b.c.j);
                    System.out.println("t1 b.c="+b.c);
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("t2 start");
                for (;;){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    b.i = 3;
                    b.c.j = 4;
                    if(allotTest.isFirst){
                        System.out.println("t2 before b.i="+b.i);
                        System.out.println("t2 before new b.c.j="+b.c.j);
                        System.out.println("t2 before new b.c.="+b.c);
                        b.c = allotTest.new C();
                        b.c.j = 5;
                        allotTest.isFirst = false;
                    }
                    System.out.println("t2 b.i="+b.i);
                    System.out.println("t2 b.c.j="+b.c.j);
                    System.out.println("t2 b.c="+b.c);
                }
            }
        });
        //t1设置高优先级
        t1.setPriority(10);
        t2.setPriority(1);
        t1.start();
        t2.start();


    }
    class B{
        int i;
        C c;
        B(){
            i = 1;
            c = new C();
        }
        public void setI(int i) {
            this.i = i;
        }
    }

    class C{
        int j;
        C(){
            j=2;
        }
    }
}
