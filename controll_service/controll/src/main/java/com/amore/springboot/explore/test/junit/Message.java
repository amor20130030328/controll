package com.amore.springboot.explore.test.junit;

public class Message {
    public static void check(String message) {
        if (message == null) {
            throw new RuntimeException("Error: Message");
        }
    }
}
