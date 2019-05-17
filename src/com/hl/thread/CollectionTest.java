package com.hl.thread;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Hanson on 2019/5/13 14:52
 */
public class CollectionTest {
    public static void main(String[] args) {
        int [] intArray = {1,2,3,4,5,1,2,3,1,2,1};
        List<int[]> ints = Arrays.asList(intArray);
        int frequency = Collections.frequency(ints, 1);
        System.out.println(frequency);
    }
}
