package com.hl.thread;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Hanson on 2019/5/17 11:17
 */
public class LinkedQueueTest {
    public static void main(String[] args) {
        LinkedBlockingQueue queue = new LinkedBlockingQueue();
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);

        while(true){
            System.out.println(queue.poll());
            if(queue.size() == 0){
                break;
            }

        }
    }
}
