package com.hl.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hanson on 2019/6/27 17:55
 */
public class ListTest {
    public static void main(String[] args) {
        List<Integer> list1 = new ArrayList(Arrays.asList(new Integer[]{1, 2, 3}));
        List<Integer> list2 = new ArrayList(Arrays.asList(new Integer[]{2, 3, 4}));

        list1.retainAll(list2);

        out(list1);
        System.out.println("-------");
        out(list2);

        list2.removeAll(list1);
        System.out.println("");
        out(list1);
        System.out.println("-------");
        out(list2);

    }


    public static void out(List<Integer> list){
        list.forEach(integer -> System.out.println(integer));
    }
}
