package com.hl.thread;
/**
 * Create by hanlin on 2018年1月17日
 **/
public class Bean {
	private volatile static Name n;
	
	public Bean() {
		super();
		n = new Name();
		n.setS(Thread.currentThread().getName());
	}

	public void name() {
		if(Thread.currentThread().getName().equals(n.getS())){
			System.out.println(String.format("ThreadName【%s】 name【%S】 n【%s】", Thread.currentThread().getName(),n.getS(),n));
		}else{
			System.err.println(String.format("ThreadName【%s】 name【%S】  n【%s】", Thread.currentThread().getName(),n.getS(),n));
		}
	}
}

class Name {
	private String s;

	public String getS() {
		return s;
	}

	public void setS(String s) {
		this.s = s;
	}
}

