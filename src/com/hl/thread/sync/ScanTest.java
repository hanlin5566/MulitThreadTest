package com.hl.thread.sync;

import java.util.Scanner;

/**
 * Create by hanlin on 2017年12月19日
 **/
public class ScanTest {
	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		String name = s.nextLine();
		int ival = s.nextInt();
		System.out.println(ival + "," + name);
	}
}
