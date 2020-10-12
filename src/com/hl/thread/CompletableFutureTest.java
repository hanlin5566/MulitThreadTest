package com.hl.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Hanson
 * @date 2020/9/24  11:50
 */
public class CompletableFutureTest {

    public static void main(String[] args) {
        CompletableFuture<Data> cf1 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(210);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Data().setAmount(300).setName("hanson1");
        });

        CompletableFuture<Data> cf2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Data().setAmount(400).setName("hanson2");
        });
        List<CompletableFuture<Data>> futures = new ArrayList<>();
        futures.add(cf1);
        futures.add(cf2);

        //cf1或cf2任意一个执行完毕则执行,且没有返回值
        CompletableFuture.anyOf(cf1, cf2).thenAccept((ret)->{
            System.out.println(((Data)ret).name);
        }).join();
        //cf1和cf2执行完毕,且返回Data的list,并循环打印.
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[futures.size()]))
                         .thenApply(v -> futures.stream().map(CompletableFuture::join).collect(Collectors.toList()))
                         .thenAccept(t -> t.stream().forEach(itm -> System.out.println(itm.getName()+":"+itm.getAmount()))).join();

    }

    public static class Data{
        public int amount;
        public String name;

        public int getAmount() {
            return amount;
        }

        public Data setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public String getName() {
            return name;
        }

        public Data setName(String name) {
            this.name = name;
            return this;
        }
    }
}
