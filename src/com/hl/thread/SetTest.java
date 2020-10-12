package com.hl.thread;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hanson on 2019/6/5 11:13
 */
public class SetTest {
    public static void main(String[] args) {
        Student s = new Student("hanson1",1);
        Set<Student> set = new HashSet<Student>();
        set.add(s);
        s.setNo(2);
        set.add(s);
        System.out.println(set);
    }
}
class Student{
    private String name;
    private int no;

    public Student(String name, int no) {
        this.name = name;
        this.no = no;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    @Override
    public int hashCode() {
        return no;
    }
}
