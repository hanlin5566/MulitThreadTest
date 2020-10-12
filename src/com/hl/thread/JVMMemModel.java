package com.hl.thread;

/**
 * Created by Hanson on 2019/5/31 17:24
 * jvm内存模型,对象都存在哪测试.
 */
public class JVMMemModel {
    public static void main(String[] args) {
        A a = new A(3);
        for (int i = 0; i < 5; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println(a.i++);//堆中,线程共享.
                    a.b.incer();//因为成员变量在堆中,所以线程不安全.
                }
            });
            t.setName("thread"+i);
            t.start();
        }
    }
}
class A{
    int i;//成员变量在堆中,赋予默认值
    B b;//成员变量的实例在堆中

    public A(int i) {
        i = 1;
        int j = 2;//基础类型在栈中,构造完成就被释放了.
        b = new B();
        /**
         * 可以使用jhat看到 classB有俩二引用,其中e0被A引用,f0没有引用
         * References to this object:
         * [Ljava.lang.Object;@0xd7258940 (176 bytes) : Element 12 of [Ljava.lang.Object;@0xd7258940
         * com.hl.thread.B@0xd72605e0 (20 bytes) : ?? -->>>>References to this object: com.hl.thread.A@0xd725ea88 (28 bytes) : field b
         *
         *
         * com.hl.thread.B@0xd72605f0 (20 bytes) : ?? -->>>> References to this object:
         *
         * 从实例数也可以看到
         * Class	              Instance Count	Total Size
         * class com.hl.thread.B	2	                8
         */
        B b1 = new B();//实例在堆中,引用在栈,也就是说A构造完成后会有两个B对象,并且b1对象并没有引用.
    }
}
class B{
    private int i;//成员变量在堆中

    public void incer(){
        i++;
        System.out.println(Thread.currentThread().getName()+"     "+i);
    }
}
