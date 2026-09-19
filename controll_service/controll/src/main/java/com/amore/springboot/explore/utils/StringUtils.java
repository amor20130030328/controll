package com.amore.springboot.explore.utils;

public class StringUtils {

    public static int parse2Int(String input){
        int i = Integer.parseInt(input);
        return i;
    }


    public static int mutil(int input,double data){
        double res =  input * data;
        int result = (int) res;
        return result;
    }
}
