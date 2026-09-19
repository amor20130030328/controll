package com.amore.springboot.explore.test;

import java.util.Arrays;

public class Main {

    static String[] split(String s) {
        return s.split("$");
    }

    public static void main(String[] args) {
        String[] r = split("a$b$c");
        System.out.println(Arrays.toString(r));


    }
}
