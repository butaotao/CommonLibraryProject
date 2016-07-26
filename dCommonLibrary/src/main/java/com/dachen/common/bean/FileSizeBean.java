package com.dachen.common.bean;

import java.text.DecimalFormat;

/**
 * Created by Mcp on 2016/1/15.
 */
public class FileSizeBean {
    private static long ONE_BYTES = 1;
    private static long ONE_KB = ONE_BYTES * 1024;
    private static long ONE_MB = ONE_KB * 1024;
    private static long ONE_GB = ONE_MB * 1024;
    private static long ONE_TB = ONE_GB * 1024;
    private static long ONE_PB = ONE_TB * 1024;
    public String unitStr;
    public long unitByteLength = 1;
    public double sizeOfUnit;
    public int size;

    public FileSizeBean(int size) {
        this.size = size;
        init();
    }

//    private void init() {
//        if (size > 1024 * 1024 * 1024) {
//            unitByteLength = 1024 * 1024 * 1024;
//            unitStr = "GB";
//            sizeOfUnit = size / (1024 * 1024 * 1024);
//        } else if (size > 1024 * 1024) {
//            unitByteLength = 1024 * 1024;
//            unitStr = "MB";
//            sizeOfUnit = size / (1024 * 1024);
//        } else if (size > 1024) {
//            unitByteLength = 1024;
//            unitStr = "KB";
//            sizeOfUnit = size / (1024);
//        } else {
//            unitStr = "B";
//            sizeOfUnit = size;
//        }
//    }

    public String getSizeStr() {
        // 格式化小数
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(sizeOfUnit) + unitStr;
    }

    private void init() {
        if (this.size < ONE_BYTES) {
            unitByteLength = ONE_BYTES;
            sizeOfUnit = 0;
            unitStr = "bytes";
        } else if (ONE_BYTES <= this.size && this.size < ONE_KB) {
            unitByteLength = ONE_BYTES;
            sizeOfUnit = this.size / (double) ONE_BYTES;
            unitStr = "bytes";
        } else if (ONE_KB <= this.size && this.size < ONE_MB) {
            unitByteLength = ONE_KB;
            sizeOfUnit = this.size / (double) ONE_KB;
            unitStr = "KB";
        } else if (ONE_MB <= this.size && this.size < ONE_GB) {
            unitByteLength = ONE_MB;
            sizeOfUnit = this.size / (double) ONE_MB;
            unitStr = "MB";
        } else if (ONE_GB <= this.size && this.size < ONE_TB) {
            unitByteLength = ONE_GB;
            sizeOfUnit = this.size / (double) ONE_GB;
            unitStr = "GB";
        } else if (ONE_TB <= this.size && this.size < ONE_PB) {
            unitByteLength = ONE_TB;
            sizeOfUnit = this.size / (double) ONE_TB;
            unitStr = "TB";
        } else {
            unitByteLength = ONE_PB;
            sizeOfUnit = this.size / (double) ONE_PB;
            unitStr = "PB";
        }

    }
}
