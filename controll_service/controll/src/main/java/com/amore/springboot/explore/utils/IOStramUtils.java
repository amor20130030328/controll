package com.amore.springboot.explore.utils;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
public class IOStramUtils {

    public static String getFileString(String path){
        String content = "";
        try (InputStreamReader isr = new InputStreamReader(new FileInputStream(path), "UTF-8");
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            content = sb.toString(); // 整个文件内容作为字符串
            System.out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


    public String getJarPath() {
        URL url = IOStramUtils.class.getProtectionDomain().getCodeSource().getLocation();
        String path = url.getPath();
        System.out.println(path);
        return null;
    }

    public static void main(String[] args) {
        IOStramUtils reader = new IOStramUtils();
        String jarPath = reader.getJarPath();
        System.out.println("Jar包路径：" + jarPath);
    }


}
