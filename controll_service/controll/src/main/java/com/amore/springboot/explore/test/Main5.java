package com.amore.springboot.explore.test;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Main5 {

    private static class Horse {
        public static void race(Object name , int rate) {
            System.out.println("Horse name=" + name + " rate=" + rate);
        };
    }

    public static void main(String[] args) {
        MethodHandles.Lookup lookup = MethodHandles.lookup();
        MethodType methodType = MethodType.methodType(void.class, Object.class, int.class);
        MethodHandle handle = null;
        try {
            handle = lookup.findStatic(Horse.class, "race", methodType);
        } catch (NoSuchMethodException | IllegalAccessException ignore){

        }

        if (handle != null) {
            try {
                handle.asType(MethodType.methodType(void.class, String.class, int.class)).invokeExact("test1", 1);
            } catch (Throwable e) {
                System.out.println("test1 fail");
            }

            try {
                handle.bindTo("test2").invoke(2);
            } catch (Throwable e) {
                System.out.println("test2 fail");
            }
        }
    }
}
