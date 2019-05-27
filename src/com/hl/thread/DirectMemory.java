package com.hl.thread;

import com.sun.management.OperatingSystemMXBean;

import java.lang.management.ManagementFactory;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by Hanson on 2019/5/14 13:12
 */
public class DirectMemory {
    public static void main(String[] args) throws Exception {
        ArrayList<ByteBuffer> list = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while(true){
                        if(list.size()>0){
                            list.remove(list.size()-1);
                            System.err.println("-----移除最后一个，释放引用");
                        }
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        for (int i = 0; i < 500; i++) {
            Thread.sleep(1000);
            System.err.println("++++++每秒添加一个");
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(10*1024*1024);//每次分配10M
            list.add(byteBuffer);

            Thread.yield();
            // 虚拟机级内存情况查询
            System.out.println("======================================");
            long vmFree = 0;
            long vmUse = 0;
            long vmTotal = 0;
            long vmMax = 0;
            int byteToMb = 1024 * 1024;
            Runtime rt = Runtime.getRuntime();
            vmTotal = rt.totalMemory() / byteToMb;
            vmFree = rt.freeMemory() / byteToMb;
            vmMax = rt.maxMemory() / byteToMb;
            vmUse = vmTotal - vmFree;
            System.out.println("JVM内存已用的空间为：" + vmUse + " MB");
            System.out.println("JVM内存的空闲空间为：" + vmFree + " MB");
            System.out.println("JVM总内存空间为：" + vmTotal + " MB");
            System.out.println("JVM总内存空间为：" + vmMax + " MB");


            System.out.println("**************************************");
            // 操作系统级内存情况查询
            OperatingSystemMXBean osmxb = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            String os = System.getProperty("os.name");
            long physicalFree = osmxb.getFreePhysicalMemorySize() / byteToMb;
            long physicalTotal = osmxb.getTotalPhysicalMemorySize() / byteToMb;
            long physicalUse = physicalTotal - physicalFree;
            System.out.println("操作系统的版本：" + os);
            System.out.println("操作系统物理内存已用的空间为：" + physicalFree + " MB");
            System.out.println("操作系统物理内存的空闲空间为：" + physicalUse + " MB");
            System.out.println("操作系统总物理内存：" + physicalTotal + " MB");
            System.out.println("======================================");
        }
    }
}
