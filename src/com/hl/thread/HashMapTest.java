package com.hl.thread;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Hanson on 2019/5/13 13:32
 * 自定义类型为key时，需要重写equals方法与hashcode方法，否则是按照内存地址比较，不同对象不认为是同一个值。
 * 底层：HashMap底层实现还是数组，只是数组的一个元素可能是一个单链表(哈希冲突时才是链表)。
 * 插入：put过程是先计算hash然后通过hash与table.length取摸计算index值，然后将key放到table[index]位置，当table[index]已存在其它元素时(哈希冲突)，会在table[index]位置形成一个链表，将新添加的元素放在table[index]，原来的元素通过Entry的next进行链接(新值链头，原值后移)--哈希冲突的解决方案；
 * 获取：先根据key的hash值得到这个元素在数组中的位置，然后通过key的equals()在链表中找到key对应的Entry节点；
 */
public class HashMapTest {
    public static void main(String[] args) {
        Map<Person,Person> map = new HashMap<>();
        Map<String,Person> map1 = new HashMap<>();
        for (int i = 0; i < 100; i++) {
            Person person = new Person("hanson",2101051);
            map.put(person,person);
            map1.put(person.name,person);
        }
        synchronized(map){

        }


        Hashtable table = new Hashtable();//->线程安全,通过get/put方法通过synchronized进行同步,锁粒度较粗.
//        table.put("",null);//异常不允许 空值空key
        HashMap map2 = new HashMap();//->线程不安全
        map.put(null,null);//允许空值空key
        ConcurrentHashMap cMap = new ConcurrentHashMap();//->线程安全
//        cMap.put("",null);//异常,不允许空值空key
    }


    protected static class Person extends  Object{
        public String name;
        public int idCard;

        public Person(String name, int idCard) {
            this.name = name;
            this.idCard = idCard;
        }

//        @Override
//        public int hashCode() {
//            return this.idCard >>> 16;
//        }
//
//        @Override
//        public boolean equals(Object obj) {
//            if(this == obj)
//                return true;
//            if(obj instanceof Person)
//                return this.idCard == ((Person) obj).idCard && this.name.equals(((Person) obj).name);
//            return false;
//        }
    }
}
