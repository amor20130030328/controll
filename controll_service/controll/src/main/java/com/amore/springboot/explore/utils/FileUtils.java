package com.amore.springboot.explore.utils;

import com.alibaba.fastjson.JSONArray;
import com.amore.springboot.explore.bean.App;

import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public static List<String> listFiles(String path) {
        List<String> list = new ArrayList<>();
        File directory = new File(path);
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                String fileName = file.getName();
                list.add(fileName);
            }
        }
        return list;
    }


    public static String readDataFromResource(String path) {

        try (InputStream input = FileUtils.class.getClassLoader().getResourceAsStream(path)) {
            if (input == null) {
                System.out.println("Sorry, unable to find example.properties");
                return "";
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(input, Charset.forName("utf-8")));
            StringBuffer sb = new StringBuffer();
            String data = "";
            while ((data = reader.readLine()) != null){
                sb.append(data);
            }
            return sb.toString();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 将字符串写入文件
     * @param content 要写入的字符串内容
     * @param mode 写入模式："w" 覆盖写入，"a" 追加写入
     * @throws IOException 当文件操作失败时抛出异常
     */
    public static void writeStringToFile(String content, String mode) {
        try {
            String filePath = "D:\\code\\controll_service\\controll\\src\\main\\resources\\data\\data.json";
            // 校验模式参数合法性
            if (!"w".equals(mode) && !"a".equals(mode)) {
                throw new IllegalArgumentException("模式只能是 'w'（覆盖）或 'a'（追加）");
            }

            // FileWriter 的第二个参数为 true 时是追加模式，false 时是覆盖模式
            boolean appendMode = "a".equals(mode);

            // 使用 try-with-resources 自动关闭流（Java 7+ 特性），避免资源泄漏
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, appendMode))) {
                // 写入内容
                writer.write(content+"\n");
                // 可选：写入换行符（如果需要每次写入都换行）
                // writer.newLine();
                // 强制刷新缓冲区（确保内容立即写入文件）
                writer.flush();
            }
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        // 测试内容
        String testContent1 = "Hello, Java File Write!";
        String testContent2 = "This is append content!";
        writeStringToFile(testContent1, "w");
        System.out.println("覆盖写入成功！");
        // 2. 测试追加写入（a模式）
        writeStringToFile(testContent2, "a");
        System.out.println("追加写入成功！");
    }
}
