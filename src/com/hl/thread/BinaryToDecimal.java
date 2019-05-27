package com.hl.thread;

/**
 * Created by Hanson on 2019/5/21 13:15
 * 2进制转十进制
 */
import java.util.Scanner;
public class BinaryToDecimal {
    public static void main(String args[]){
        Scanner input = new Scanner( System.in );
        System.out.print("Enter a binary number: ");
        String binaryString =input.nextLine();
        System.out.println("Output: "+Integer.parseInt(binaryString,2));
    }
}
