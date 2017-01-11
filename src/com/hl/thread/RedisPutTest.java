package com.hl.thread;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class RedisPutTest implements Callable<Map<String,String>>{
	private int count = 1000;
	private String name = "default";
	private HostAndPort host;
	
	public RedisPutTest(int count, String name, HostAndPort host) {
		super();
		this.count = count;
		this.name = name;
		this.host = host;
	}

	@Override
	public Map<String,String> call() throws Exception {
		Map<String,String> ret = new HashMap<String,String>();
		long s = System.currentTimeMillis();
		try {
			Set<HostAndPort> hosts = new HashSet<HostAndPort>();
			hosts.add(host);
			JedisCluster jc = new JedisCluster(hosts);
			
			int last = Integer.valueOf(jc.get(name+"_lat"));
			count+=last;
			for (int i = last; i < count; i++) {
				jc.set(name+i, i+"");
				jc.set(name+"_lat",i+"");
			}
			System.out.println(name+" added "+count+" data done");
			jc.close();
			ret.put("time", String.valueOf(System.currentTimeMillis() - s));
			ret.put("name", name);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}
}