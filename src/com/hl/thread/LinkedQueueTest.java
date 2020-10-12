package com.hl.thread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Hanson on 2019/5/17 11:17
 */
public class LinkedQueueTest {
    public static void main(String[] args) {
        LinkedBlockingQueue queue = new LinkedBlockingQueue();
        ArrayList<Object> arrayList = new ArrayList<>();
        HashMap map = new HashMap();
        HashSet hashSet = new HashSet();
        queue.offer(1);
        queue.offer(2);
        queue.offer(3);

        while(true){
            System.out.println(queue.poll());
            if(queue.isEmpty()){
                break;
            }

        }
    }
}
