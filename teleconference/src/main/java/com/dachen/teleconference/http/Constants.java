package com.dachen.teleconference.http;

/**
 * Created by TianWei on 2016/9/1.
 */
public class Constants {
    //http://192.168.3.7:8087/
    //http://192.168.3.62:8089/

    public static String IP = "xg.mediportal.com.cn";//默认康泽环境
    //    public static String IP = "http://192.168.3.7:8087/";

    public static String SERVER_ACCOUNT = "server_37";
    /**
     * apiUrl
     */
    public static String API_BASE_URL = "http://" + IP + ":8087/";


    /**
     * 改变Ip,内网外网切换
     */
    public static void changeIp(String ip) {
        IP = ip;

        if (IP.equals("192.168.3.7")) {
            API_BASE_URL = "http://" + IP + ":8087/";
            SERVER_ACCOUNT = "server_37";
        } else if (IP.equals("120.24.94.126")) {
            API_BASE_URL = "http://" + IP + ":8087/";
            SERVER_ACCOUNT = "server_126";
        } else if (IP.equals("192.168.3.11")) {
            API_BASE_URL = "http://" + IP + ":8087/";

        } else if (IP.equals("xg.mediportal.com.cn")) {

            API_BASE_URL = "http://" + IP + ":8087/";

        } else if (IP.equals("192.168.3.63")) {
            API_BASE_URL = "http://" + IP + ":8087/";

        } else if (IP.equals("4")) {
            API_BASE_URL = "http://" + IP + ":8087/";
        } else if (IP.equals("120.25.84.65")) {
            API_BASE_URL = "http://" + IP + ":8087/";

        }


    }


}
