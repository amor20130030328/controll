package com.amore.springboot.explore.utils;

import com.alibaba.fastjson.JSONArray;
import com.amore.springboot.explore.annotation.MethodCostTime;
import com.amore.springboot.explore.bean.Point;
import com.amore.springboot.explore.bean.WindowPointInfo;
import com.amore.springboot.explore.constant.CmdType;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.amore.springboot.explore.utils.FileUtils.writeStringToFile;
import static org.springframework.core.io.buffer.DataBufferUtils.readInputStream;


public class ADBUtils {

    private static final String ADB_DEVICE_IDS = "adb devices";
    private static final String CLOSE_APP = "adb -s {device_id} shell am force-stop {package}";

    private static final String CONTROLL_LIGHT = "adb -s {device_id} shell input keyevent {num}";
    //private static final String OPEN_APP = "adb -s {device_id} shell am start {package}/{activity}";
    private static final String OPEN_APP = "adb -s {device_id} shell monkey -p {package} -c android.intent.category.LAUNCHER 1";

    private static final String qtPath = "D:\\environment\\QtScrcpy-win-x64-v2.1.2\\QtScrcpy.exe";
    private static final String NginxPath = "D:\\environment\\nginx-1.16.1\\run.bat";

    private static final String AppSize ="adb -s {device_id} shell wm size";
    private static final String MoveApp ="adb -s {device_id}  shell input swipe {start_x} {start_y} {end_x} {end_y} 500";

    private static final String InstallApp="adb -s {device_id} install {apk}";
    private static final String Casting="scrcpy -s device_id --window-title device_id --no-audio --max-size 600 --window-width 400";
    private static final String ALWAYS_ON_LIGHT="adb -s {device_id} shell settings put system screen_off_timeout 2147483647";
    private static final String WIRELESS="adb -s {device_id} tcpip {port}";
    private static final String CONNECT="adb connect {ip}:{port}";
    private static final String window_close = "adb -s {device_id} shell dumpsys power | grep -E mWakefulness=Awake";
    private static final String TAKE_ON="adb -s {device_id} shell input keyevent KEYCODE_POWER";
    private static final String REDUCE_AUDIO="adb -s {device_id}  shell input keyevent KEYCODE_VOLUME_DOWN";
    private static final String LOCAL_AREA_IP ="adb -s {device_id}  shell ip addr show wlan0 | grep wlan0 | grep scope";
    private static final String TASK ="adb -s {device_id}  shell input keyevent KEYCODE_APP_SWITCH";
    private static final String WRITE ="adb -s {device_id}  shell echo '{data}'> /sdcard/text.json";
    private static final String READ ="adb -s {device_id}  shell cat /sdcard/text.json";
    private static Map<String, WindowPointInfo> map = new HashMap<>();

    private static ExecutorService appService = AppManager.getFixedThreadPool();

    private static final String GET_IP = "adb -s {device_id} shell ip addr show wlan0 | grep 192.168";
    private static final String GET_PHONE_NUM = "adb -s {device_id} shell ip addr show wlan0 | grep 192.168";

    static {
        String s = FileUtils.readDataFromResource("json/point.json");
        List<WindowPointInfo> list = JSONArray.parseArray(s, WindowPointInfo.class);
        list.forEach(item->{
            map.put(item.getDeviceId(), item);
        });


    }


    public static void write(String deviceId, String data) {
        String cmd = WRITE.replace("{device_id}", deviceId).replace("{data}", data);
        Processor.runSync(cmd);
    }

    //adb shell
    //    inet 192.168.137.58/24 brd 192.168.137.255 scope global wlan0
    public static List<String> getAllDeviceIds(){
        String res  = Processor.runSync(ADB_DEVICE_IDS);
        List<String> deviceIds = Arrays.stream(res.split("\n")).filter((item) -> {
            return item.split("\t").length == 2;
        }).filter(x->x.contains("device"))
                .map(x->x.split("\t")[0])
                .filter(d->d.contains("192.168") || d.length() == 16)
                .collect(Collectors.toList());
        return deviceIds;
    }

    public static void CloseApp(String deviceId,String packageName){
        String cmd = CLOSE_APP.replace("{device_id}", deviceId)
                .replace("{package}", packageName);
        System.out.println(cmd);
        Processor.runSync(cmd);
    }

    public static void OpenApp(String deviceId,String packageName,String activity){
        String cmd = OPEN_APP.replace("{device_id}", deviceId)
                .replace("{package}", packageName)
                        .replace("{activity}",activity);
        Processor.runSync(cmd);
    }



    public static void startQt(){
        String run = Processor.runSync("tasklist ");
        if(!run.contains("QtScrcpy.exe")){
            Processor.run(qtPath);
        }else{
            System.out.println("qtscrpy already start...");
        }

    }

    public static void startNginx(){
        String run = Processor.runSync("tasklist ");
        if(!run.contains("nginx.exe")){
            Processor.run(NginxPath);
            System.out.println("nginx starting...");
        }else{
            System.out.println("nginx already start...");
        }

    }

    public static void move(String deviceId){
        String stdout_str = Processor.runSync(AppSize.replace("{device_id}",deviceId));
        String x_size = stdout_str.split(":")[1].split("x")[0].trim();
        String y_size = stdout_str.split(":")[1].split("x")[1].trim();

        double start_x = StringUtils.parse2Int(x_size) * 0.85;
        double start_y = StringUtils.parse2Int(y_size) * 0.85;
        double end_x = StringUtils.parse2Int(x_size) * 0.85;
        double end_y = StringUtils.parse2Int(y_size) * 0.4;

        String cmd = MoveApp.replace("{device_id}",deviceId).replace("{start_x}", start_x+"")
                .replace("{start_y}", start_y+"")
                .replace("{end_x}", end_x+"")
                .replace("{end_y}", end_y+"");
        System.out.println(cmd);
        Processor.runSync(cmd);

    }



    private static Point getWindowSize(String deviceId) {
        String stdout_str = Processor.runSync(AppSize.replace("{device_id}",deviceId));
        stdout_str = stdout_str.replace("Physical size: ", "")
                .replace("Override size: ", "");
        if (stdout_str.contains("\n")) {
            String s = stdout_str.split("\n")[0];
            String x = s.split("x")[0].trim();
            String y = s.split("x")[1].trim();
            return new Point(Integer.parseInt(x),Integer.parseInt(y));
        }
        return null;
    }


    private static String readData(String deviceId) {
        String stdout_str = Processor.runSync(READ.replace("{device_id}",deviceId));

        return stdout_str;
    }


    @MethodCostTime
    public static void move(List<String> deviceIds,boolean up){
        deviceIds.forEach(deviceId->{
            appService.submit(()->{
                double largeStartPrecision = 0.85;  //0.7;
                double largeEndPrecision = 0.4;     //0.55;
                splitScreenMove(deviceId, up, largeStartPrecision, largeEndPrecision);
            });
        });
    }

    public static void splitScreenMove(String deviceId,boolean up,
                                       double largeStartPrecision, double largeEndPrecision ){

            Point point = getWindowSize(deviceId);
            int endX =  StringUtils.mutil(point.getX(),0.5) ;
            int startX = StringUtils.mutil(point.getX(), 0.5);
            int endY = up ? StringUtils.mutil(point.getY(),largeEndPrecision) : StringUtils.mutil(point.getY(),largeStartPrecision);
            int startY = up ? StringUtils.mutil(point.getY(),largeStartPrecision) : StringUtils.mutil(point.getY(),largeEndPrecision);

            String cmd = MoveApp.replace("{device_id}",deviceId)
                    .replace("{start_x}", startX+"")
                    .replace("{start_y}", startY+"")
                    .replace("{end_x}", endX+"")
                    .replace("{end_y}", endY+"");
            Processor.runSync(cmd);

    }

    public static void mutilmove(List<String> deviceIds,boolean up){

        move(deviceIds,up);

        deviceIds.forEach(deviceId->{
            Point point = getWindowSize(deviceId);

            int start_x = 0;
            int start_y = 0;
            int end_x = 0;
            int end_y = 0;
            if(up) {
                start_x = StringUtils.mutil(point.getX(),  0.5) ;
                start_y = StringUtils.mutil(point.getY(),0.6) ;
                end_x =  StringUtils.mutil(point.getX(),0.5) ;
                end_y =  StringUtils.mutil(point.getY(),0.25) ;
            }else {
                end_x =  StringUtils.mutil(point.getX(),0.5) ;
                end_y =  StringUtils.mutil(point.getY(),0.25) ;
                start_x = StringUtils.mutil(point.getX(),0.5) ;
                start_y =  StringUtils.mutil(point.getY(),0.6);
            }
            String cmd = MoveApp.replace("{device_id}",deviceId).replace("{start_x}", start_x+"")
                    .replace("{start_y}", start_y+"")
                    .replace("{end_x}", end_x+"")
                    .replace("{end_y}", end_y+"");
            System.out.println(cmd);
            Processor.run(cmd);
            long timestamp = System.currentTimeMillis();
            cmd = "adb -s" + deviceId + " shell screencap -p /sdcard/screenshot_" + timestamp + ".png";
            System.out.println(cmd);
            //Processor.run(cmd);
        });
    }

    public static void installApp(String deviceId){
        String path = "./apk";

        List<String> list = FileUtils.listFiles(path);
        list.forEach(fileName->{
            String cmd = InstallApp.replace("{device_id}", deviceId)
                    .replace("{apk}", path + "//" + fileName);
            System.out.println(cmd);
            Processor.runSync(cmd);
        });

    }

    public static void casting(List<String> deviceIds){
        int screenWidth = 2500;
        int deviceCount = deviceIds.size();
        int windowWidth = screenWidth / deviceCount;

        for(int i = 0 ; i < deviceCount; i++){
            String deviceId = deviceIds.get(i);
            int windowX = i * windowWidth;

            int screenHeight = 1500;
            int windowHeight = screenHeight * 2 / 3;
            String cmd = "scrcpy -s " + deviceId +
                        " --window-title " + deviceId +
                        " --no-audio --max-size 500" +
                        " --window-width " + windowWidth +
                        " --window-height " + windowHeight +
                        " --window-x " + windowX +
                        " --window-y 100";

            System.out.println(cmd);
            Processor.run(cmd);

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }


    public static void alwaysOnLight(List<String> deviceIds){
        deviceIds.forEach(deviceId->{
            String cmd = ALWAYS_ON_LIGHT.replace("{device_id}", deviceId);
            System.out.println("always on light :" + cmd);
            Processor.runSync(cmd);
        });

    }

    public static void casting(String deviceId, String title){
        //scrcpy -s device_id --window-title device_id --no-audio --turn-screen-off --window-size 220x700
        String cmd = Casting.replace("{device_id}", deviceId);
        Processor.runSync(cmd);
    }

    public static void casting(String deviceId){
        String cmd = "scrcpy -s " + deviceId + "  --max-size 500 --window-width 300 --window-title " + deviceId ;
        Processor.runSync(cmd);
    }

    public static void controllLight(String deviceId, boolean isUp){
        if(deviceId != null && deviceId != ""){
            String cmd = CONTROLL_LIGHT.replace("{device_id}", deviceId);
            String num = isUp ? "221":"220";
            cmd = cmd.replace("{num}", num);
            for(int i = 0 ; i < 10 ;i++){
                Processor.runSync(cmd);
            }

        }else{
            ADBUtils.getAllDeviceIds().forEach(Id->{
                String cmd = CONTROLL_LIGHT.replace("{device_id}", Id);
                String num = isUp ? "221":"220";
                cmd = cmd.replace("{num}", num);
                for(int i = 0 ; i < 10 ;i++){
                    Processor.runSync(cmd);
                }
            });
        }

    }


    public static  int basePort = 5555;


    public static void wireless(String deviceId, String privateIp) {
        try {
            System.out.println("无线 ===========>");
            int port = basePort ++;
            String cmd = WIRELESS.replace("{device_id}", deviceId).replace("{port}", port + "");
            Processor.runSync(cmd);
            writeStringToFile(cmd, "a");
            cmd = CONNECT.replace("{device_id}", deviceId).replace("{ip}", privateIp).replace("{port}", port + "");
            Processor.runSync(cmd);
            writeStringToFile(cmd, "a");
            basePort++;
            System.out.println("无线结束 =========>");
        } catch (Exception e) {
            System.out.println("wireless fail deviceId = " + deviceId);
        }

    }

    public static void wireless(String privateIp) {
        try {
            String cmd = CONNECT.replace("{ip}:{port}", privateIp);
            Processor.runSync(cmd);
        } catch (Exception e) {
            System.out.println("wireless fail deviceId = " + privateIp);
        }

    }



    public static void runCmd(String deviceId, String cmdType) {

        String cmd = "";
        switch (cmdType) {
            case CmdType.REDUC_AUDIO:
                cmd = REDUCE_AUDIO.replace("{device_id}", deviceId);
                Processor.runSync(cmd);
                break;
            case CmdType.TAKE_ON:
                cmd = TAKE_ON.replace("{device_id}", deviceId);
                Processor.runSync(cmd);
                move(deviceId);
                break;
        }


    }


    /**
     * 检查设备屏幕是否亮屏
     * @return true=亮屏，false=息屏
     * @throws IOException 执行ADB命令异常
     */


    public static void takeOn() {

        List<String> deviceIds = getAllDeviceIds();
        for (String deviceId : deviceIds) {

            String cmd = window_close.replace("{device_id}", deviceId);
            String[] s1 = window_close.replace("\"","").split(" ");
            String label = s1[s1.length-1];

            String res  = Processor.runSync(cmd);
            if (!res.contains(label)) {
                cmd = TAKE_ON.replace("{device_id}", deviceId);
                Processor.runSync(cmd);
            }
        }
        sleep(1);
    }




    public static String getLocalAreaIp(String deviceId) {

        try {
            String cmd = LOCAL_AREA_IP.replace("{device_id}", deviceId);
            String res  = Processor.runSync(cmd);
            String[] s = res.split("/")[0].split(" ");
            String ip = s[s.length - 1];
            return ip;
        } catch (Exception e) {
            System.out.println("getLocalAreaIp fail deviceId = " + deviceId);
        }


        return "";
    }
    
    public static void restart() {
        String cmd = "adb kill-server";
        Processor.runSync(cmd);
        cmd = "adb start-server";
        Processor.runSync(cmd);
    }

    public static void enterTask(String deviceId) {
        String cmd = TASK.replace("{device_id}", deviceId);
        Processor.runSync(cmd);

        Point point = getWindowSize(deviceId);

        String s = readData(deviceId);
        WindowPointInfo windowPointInfo = new Gson().fromJson(s, WindowPointInfo.class);

        double x = point.getX() * windowPointInfo.getX();
        double y = point.getY() * windowPointInfo.getY();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {

            e.printStackTrace();
        }
        cmd = "adb -s " + deviceId + " shell input tap " + x +" " + y;
        Processor.runSync(cmd);

    }

    public static String getIp(String deviceId) {
        String cmd = GET_IP.replace("{device_id}", deviceId);
        String s = Processor.runSync(cmd);
        System.out.println(s);
        String [] ips = s.split("/")[0].split(" ");
        String ip = ips[ips.length - 1];
        return ip;
    }


    public static void sleep(int second){

        try {
            Thread.sleep( second * 1000L);
        } catch (InterruptedException e) {
            System.out.println("睡眠2秒异常");
        }
    }

    public static void main(String[] args) {
        List<String> deviceIds = getAllDeviceIds();
        String cmd = "adb -s {device_id} shell 'content query --uri content://telephony/siminfo | grep -o \"number=[^,]*\" | cut -d= -f2'";
        for(String deviceId : deviceIds) {
            String c = cmd.replace("{device_id}", deviceId);
            String res = Processor.runSync(c);
            System.out.println(res);
        }

    }
    
}