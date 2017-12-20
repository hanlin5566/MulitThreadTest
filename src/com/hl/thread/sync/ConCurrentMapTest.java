package com.hl.thread.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Create by hanlin on 2017年12月18日
 * [0] 指令
 *  init
 *   empty
 *  add
 *   [1] index
 *   [2] prefix
 *  refresh
 *   [1] index
 *   [2] prefix
 *  read
 *   [1]prefix
 *  write
 *   [1] index
 *   [2] prefix
 **/

public class ConCurrentMapTest {
	enum Flag{
		INIT,ADD,REFRESH,READ,WRITE,EXIT
	}
	public static final int sleepTime = 5000;
	public static final int readThreadCount = 2;
	public static final int writeThreadCount = 1;
	public static final int dataLength = 3;
	
	public static void main(String[] args) {
		
		String info = "* [0] instructions\r\n*  init\r\n*   empty\r\n*  add\r\n*   [1] index\r\n*   [2] prefix\r\n* refresh\r\n*   [1] index\r\n*  [2] prefix\r\n*  read\r\n*   [1]prefix\r\n*  write\r\n*   [1] index\r\n*   [2] prefix\r\n";
		System.out.println(info);
		Scanner s = new Scanner(System.in);
		Flag flag;
		ConCurrentMapTest main = new ConCurrentMapTest();
		while(s.hasNext()){
			String line = s.nextLine();
			String [] param = new String[3];
			if(line.indexOf("-")>-1){
				param = line.split("-");
			}else{
				param[0] = line;
			}
			System.out.println("rec "+param[0]+" operation..");
			switch (param[0]) {
			case "init":
				flag = Flag.INIT;
				main.init();
				break;
			case "add":
				flag = Flag.ADD;
				main.add(Integer.parseInt(param[1]), param[2]);
				break;
			case "refresh":
				flag = Flag.REFRESH;
				main.refresh(Integer.parseInt(param[1]), param[2]);
				break;
			case "read":
				flag = Flag.INIT;
				main.readThread(param[1]);
				break;
			case "write":
				flag = Flag.INIT;
				main.writeThread(Integer.parseInt(param[1]),param[2]);
				break;
			case "exit":
				flag = Flag.INIT;
				s.close();
				break;
			default:
				System.out.println("unknow instructions");
				break;
			}
			System.out.println("input instructions");
		}
	}
	
	

	Map<String, Map<String, DataModel>> data = new ConcurrentHashMap<String, Map<String, DataModel>>();
	
	
	private void sleep(){
		try {
			Random random = new Random();
			int i = random.nextInt(sleepTime);
			Thread.sleep(Long.parseLong(i+""));
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// 前缀和
	private void putData(int sIndex, String prefix) {
		Map<String, DataModel> map = new HashMap<String, DataModel>();
		if(data.containsKey(prefix)){
			map = data.get(prefix);
		}
		for (int i = sIndex; i < dataLength+sIndex; i++) {
			DataModel dataModel = new DataModel(prefix + "_" + i, prefix + "_" + i, i);
			sleep();
			map.put("" + i, dataModel);
		}
		data.put(prefix, map);
	}

	private void read(String prefix) {
		Map<String, DataModel> map = data.get(prefix);
		for (String key : map.keySet()) {
			DataModel dataModel = map.get(key);
			String threadName = Thread.currentThread().getName();
			System.out.print("thread "+threadName+" read:"+dataModel.toString());
			System.out.println();
		}
	}
	
	private void readAll() {
		for (String prefix : data.keySet()) {
			Map<String, DataModel> map = data.get(prefix);
			for (String key : map.keySet()) {
				DataModel dataModel = map.get(key);
				String threadName = Thread.currentThread().getName();
				System.err.println("thread "+threadName+" read:"+dataModel.toString());
			}
		}
	}

	public void init() {
		this.putData(1, "init");
	}

	public void add(int index,String prefix) {
		this.putData(index, prefix);
	}

	public void refresh(int index, String prefix) {
		Map<String, Map<String, DataModel>> freshData = new ConcurrentHashMap<String, Map<String, DataModel>>();
		Map<String, DataModel> map = new HashMap<String, DataModel>();
		for (int i = index; i < dataLength; i++) {
			DataModel dataModel = new DataModel(prefix + "_" + i, prefix + "_" + i, i);
			sleep();
			map.put("" + i, dataModel);
		}
		freshData.put(prefix, map);
		data = freshData;
	}

	public void readThread(final String prefix) {
		for (int i = 0; i < readThreadCount; i++) {
			Thread read = new Thread(new Runnable() {
				public void run() {
					if("all".equals(prefix)){
						while(true){
							readAll();
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}else{
						read(prefix);
					}
				}
			});
			read.setName("Reader_"+prefix + i);
			read.start();
		}
	}

	public void writeThread(final int index,final String prefix) {
		for (int i = 0; i < writeThreadCount; i++) {
			Thread write = new Thread(new Runnable() {
				public void run() {
					refresh(index, prefix);
				}
			});
			write.setName("Writer" + i);
			write.start();
		}
	}
}
