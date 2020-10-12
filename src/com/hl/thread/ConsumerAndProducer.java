package com.hl.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Hanson on 2019/6/3 16:08
 * 生产者 消费者
 * 一定要是lock住对象的 wait与notify,否则会报IllegalMonitorStateException异常.
 */
public class ConsumerAndProducer {

    List<Integer> products = new ArrayList<Integer>();
    AtomicInteger integer = new AtomicInteger();

    public static void main(String[] args){
        ConsumerAndProducer lock = new ConsumerAndProducer();

        Thread consumer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (lock.products){
                        System.out.println("消费者获得锁,尝试拿商品.");
                        if(lock.products.size() > 0){
                            Integer product = lock.products.remove(0);//拿出来一个
                            System.out.println("------------------------消费者获得"+product+"商品.");
                            //消费一个商品,唤醒其他线程.
                            lock.products.notify();
                        }else{
                            //等待生产,释放锁.
                            try {
                                System.err.println("没拿到商品,等待生产.");
                                lock.products.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        consumer.setName("consumer");
        consumer.start();

        Thread producer = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    synchronized (lock.products){
                        System.out.println("生产者获得锁,尝试生产.");
                        if(lock.products.size() > 10){
                            //满了,等待消费
                            //等待消费,释放锁.
                            try {
                                System.err.println("生产者获得锁,满了,等待消费.");
                                lock.products.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }else{
                            int product = lock.integer.getAndIncrement();
                            lock.products.add(product);//生产一个
                            System.out.println("+++++++++++++++++++++++++生产者生产"+product+"商品.");
                            lock.products.notify();
                        }

                    }
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        producer.setName("producer");
        producer.start();
    }
}


