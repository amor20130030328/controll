package com.amore.springboot.explore.test;

import java.util.regex.Pattern;

public class Main2 {
    public static void main(String[] args) {

        String srcText = "123\\d";
        String rst1 = srcText.replaceAll("\\d", "456");
        String rst2 = srcText.replaceAll(Pattern.quote("\\d"), "456");
        String rst3 = srcText.replace("\\d", "456");
        System.out.println(rst1);
        System.out.println(rst2);
        System.out.println(rst3);


    }
}
