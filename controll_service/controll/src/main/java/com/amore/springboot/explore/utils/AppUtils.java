package com.amore.springboot.explore.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
public class AppUtils {

    private static String shellPath = "D:\\code\\programer\\java\\new_coder\\new_coder\\springboot_explore\\springboot_explore\\src\\main\\resources";

    public static void startApp(){
        try {
            // 例如，运行一个简单的DOS命令，比如dir
            String command = shellPath + "\\" + "start_app.bat";
            Process process = Runtime.getRuntime().exec(command);

            // 读取命令的输出
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            Thread.sleep(10000);
            // 等待命令执行完成
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
