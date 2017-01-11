package com.hl.thread;

import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redis.clients.jedis.HostAndPort;

public class CallableAndFuture {
	public static void main(String[] args) {
		
		int threadCount = 5;
		int dataCount = 100000;
		HostAndPort host = new HostAndPort("192.168.9.192", 7000);
		ExecutorService threadPool = Executors.newFixedThreadPool(2);
		long s = System.currentTimeMillis();
		CompletionService<Map<String,String>> cs = new ExecutorCompletionService<Map<String,String>>(threadPool);
		for (int i = 0; i < threadCount; i++) {
			final int taskID = i+1;
			cs.submit(new RedisPutTest(dataCount, "putRedisTest"+taskID, host));
		}
		// 可能做一些事情
		
		for (int i = 0; i < 5; i++) {
			try {
				Map<String,String> map = cs.take().get();
				long time = Long.valueOf(map.get("time"));
				System.out.println(map.get("name")+" finished time elapsed:"+time);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		}
		long totalTime = System.currentTimeMillis() - s;
		System.out.println("all thread done,time-consuming:"+totalTime);
		threadPool.shutdown();
	}
}
