package com.dachen.teleconference.utils;


import com.dachen.common.utils.QiNiuUtils;

/**
 * 服务器地址说明：
 * <p/>
 * 在浏览器中输入：http://192.168.3.7:8091/config 即可查看相关地址信息
 */
public class Constants {
    /**
     * 配置： 阿里云：120.24.94.126
     * <p/>
     * 内网：192.168.3.7
     */
    public static String IP = "xg.mediportal.com.cn";//默认康泽环境

    /**
     * apiUrl
     */
    public static String API_BASE_URL = "http://" + IP + ":80/health/";

    /**
     * web页面
     */
    public static String API_BASE_WEB_URL = "http://" + IP + "/health/web/";


    /**
     * 发起电话会议
     */
    public static String GET_BANK_CARDS = API_BASE_URL + "pack/bank/getBankCards";


    /**
     * 改变Ip,内网外网切换
     */
    public static void changeIp(String ip) {
        IP = ip;

        if (IP.equals("192.168.3.7") || IP.equals("120.24.94.126") || IP.equals("192.168.3.11")) {
            if (IP.equals("192.168.3.7")) {
                QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_3_7);
            } else if (IP.equals("120.24.94.126")) {
                QiNiuUtils.changeEnv(QiNiuUtils.DOMAIN_ALI_YUN);
            }
            /**
             * apiUrl
             */
            API_BASE_URL = "http://" + IP + ":80/health/";
            API_BASE_WEB_URL = "http://" + IP + "/health/web/";

        } else if (IP.equals("xg.mediportal.com.cn")) {

            /**
             * apiUrl
             */
            API_BASE_URL = "http://" + IP + ":80/health/";
            API_BASE_WEB_URL = "http://" + IP + "/health/web/";

        } else if (IP.equals("192.168.3.63")) {
            /**
             * apiUrl
             */
            API_BASE_URL = "http://" + IP + ":80/health/";
            API_BASE_WEB_URL = "http://" + IP + "/health/web/";

        } else if (IP.equals("4")) {

            /**
             * apiUrl
             */
            API_BASE_URL = "http://" + IP + ":80/health/";
            API_BASE_WEB_URL = "http://" + IP + "/health/web/";

        } else if (IP.equals("120.25.84.65")) {
            API_BASE_URL = "http://" + IP + ":80/health/";
            API_BASE_WEB_URL = "http://" + IP + "/health/web/";
        }


    }

}
