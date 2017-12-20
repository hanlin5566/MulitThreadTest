package com.hl.thread.sync;
/**
 * Create by hanlin on 2017年12月18日
 **/
public class DataModel {
	public String name;
	public String key;
	public int value;
	
	public DataModel(String name, String key, int value) {
		super();
		this.name = name;
		this.key = key;
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	@Override
	public String toString() {
		return String.format("{n:%s,k:%s,v:%s}",this.name,this.key,this.value);
	}
}
