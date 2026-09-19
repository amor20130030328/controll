package com.amore.springboot.explore.test.junit;

public class Robot {

    public String ask(String message) {
        Message.check(message);
        if("".equals(message)) {
            return "repeat again";
        }
        return "Hi " + message;
    }
}
