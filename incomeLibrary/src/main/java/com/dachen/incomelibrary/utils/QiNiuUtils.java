package com.dachen.incomelibrary.utils;

/**
 * Created by Mcp on 2016/1/8.
 */
public class QiNiuUtils {

    private static String DOMAIN=".dev.file.dachentech.com.cn";

    public static final String DOMAIN_3_7=".dev.file.dachentech.com.cn";
    public static final String DOMAIN_ALI_YUN=".test.file.dachentech.com.cn";

//    public static final String DOMAIN_3_7="message.dev.file.dachentech.com.cn";
//    public static final String DOMAIN_ALI_YUN="message.test.file.dachentech.com.cn";

    public static final String BUCKET_MSG="message";
    public static final String BUCKET_PATIENT="patient";
    public static final String BUCKET_DOCTOR="doctor";
    public static final String BUCKET_TEL_RECORD="telrecord";
    public static final String BUCKET_AVATAR="avatar";

    public static final String SURFIX_SMALL="_small";

    public static String getFileUrl(String bucket,String key){
        return  "http://"+bucket+DOMAIN+"/"+key;
    }
//    public static String getFileUrl(String bucket,String key){
//        return  "http://"+DOMAIN+"/"+key;
//    }
    public static void changeEnv(String target){
        DOMAIN=target;
    }
}
