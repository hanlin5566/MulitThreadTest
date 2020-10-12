package com.hl.thread;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Hanson on 2019/5/14 15:21
 * 首先排序,从中间开始查找,如果逾期值小于当前值,则在左区间,end-1为当前值的下标,继续递归查找.否则就是为start+1,向右查找.
 */
public class BinarySearch {
    private Integer[] array;

    public BinarySearch(Integer[] array) {
        this.array = array;
    }

    /**
     * 递归实现二分查找
     * @param target
     * @return
     */
    public int searchRecursion(int target) {
        int count = 0;
        if (array != null) {
            return searchRecursion(target, 0, array.length - 1,count);
        }
        return -1;
    }

    /**
     *
     * @param target 目标值
     * @param start 起始下标
     * @param end 结束下标
     * @param count 记录查找总次数 ，正常应该不带此参数
     * @return
     */
    private int searchRecursion(int target, int start, int end,int count) {
        if (start > end) {
            return -1;
        }
        int mid = start + (end - start) / 2;
        if (array[mid] == target) {
            ++count;
            System.out.println("searchRecursion 查找了"+count);
            return mid;
        } else if (target < array[mid]) {
            return searchRecursion(target, start, mid - 1,count);
        } else {
            return searchRecursion(target, mid + 1, end,count);
        }
    }

    /**
     * 二分查找
     * @param target
     * @return
     */
    public int search(int target) {
        int count = 0;
        if (array == null) {
            return -1;
        }
        int start = 0;
        int end = array.length - 1;
        while (start <= end) {
            ++count;
            int mid = start + (end - start) / 2;
            if (array[mid] == target) {
                System.out.println("searchRecursion 查找了"+count);
                return mid;
            } else if (target < array[mid]) {//目标在左区间，所以从左区间（start---当前-1）开始查找。
                end = mid - 1;
            } else {
                start = mid + 1;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        int count = 20;
        for (int i = 0; i < count; i++) {
            list.add((int) (Math.random()*100));
        }
        Integer[] array = new Integer[list.size()];
        list.toArray(array);
        Arrays.sort(array);
        BinarySearch binarySearch = new BinarySearch(array);
        System.out.println(binarySearch.search(list.get(0)));
        System.out.println(binarySearch.searchRecursion(list.get(0)));
        System.out.println(binarySearch.search(list.get(list.size()-1)));
        System.out.println(binarySearch.searchRecursion(list.size()-1));
    }

}