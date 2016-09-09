package com.dachen.community.model;

/**
 * Created by Administrator on 2016/9/9.
 */
public class PictureModel {
    private String localImg;
    private String netImg;
    private int width;
    private int height;
    private boolean showDel;

    public String getLocalImg() {
        return localImg;
    }

    public void setLocalImg(String localImg) {
        this.localImg = localImg;
    }

    public String getNetImg() {
        return netImg;
    }

    public void setNetImg(String netImg) {
        this.netImg = netImg;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isShowDel() {
        return showDel;
    }

    public void setShowDel(boolean showDel) {
        this.showDel = showDel;
    }
}
