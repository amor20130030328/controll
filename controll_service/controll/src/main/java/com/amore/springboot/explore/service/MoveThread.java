package com.amore.springboot.explore.service;

import com.amore.springboot.explore.utils.Processor;

public class MoveThread implements  Runnable{

    private String cmd;
    public MoveThread(String cmd){
        this.cmd = cmd;
    }
    @Override
    public void run() {
        Processor.run(cmd);
    }

    public static void main(String[] args) {
        new Thread(new MoveThread("")).start();
    }
}
