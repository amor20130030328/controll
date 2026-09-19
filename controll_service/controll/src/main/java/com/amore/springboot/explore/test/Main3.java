package com.amore.springboot.explore.test;

public class Main3 {


    private static void invoke(Integer value) {
        System.out.println("invoke first");
    }

    private static void invoke(int value, Object ... args) {
        System.out.println("invoke second");
    }

    private static void invoke(Integer value, Object ... args) {
        System.out.println("invoke third");
    }

    public static void main(String[] args) {
        invoke(1);
    }
}
