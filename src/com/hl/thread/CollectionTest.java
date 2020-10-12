package com.hl.thread;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hanson on 2019/5/13 14:52
 * 可以统计出某个对象在collection中出现的次数
 */
public class CollectionTest {
    public static void main(String[] args) {
        Integer [] intArray = {1,2,3,4,5,1,2,3,1,2,1};
        List<Integer> ints = Arrays.asList(intArray);
        int frequency = Collections.frequency(ints, 4);
        System.out.println(frequency);

        Arrays.sort(intArray);
        for (Integer i :intArray) {
            System.out.print(i);
        }
    }
}
