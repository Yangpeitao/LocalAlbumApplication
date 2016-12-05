package com.personal.localalbum.Info;

/**
 * 本地相册的操作类
 * Created by 杨培韬 on 2016/9/27.
 */

public class AlbumPhoto {
    /**
     * 原图的存储路径
     */
    private String path;
    /**
     * 图片旋转角度
     */
    private int orientation;

    private boolean check;

    public AlbumPhoto(String path, int orientation) {
        this.path = path;
        this.orientation = orientation;
        this.check = false;
    }

    public String getPath() {
        return path;
    }

    public int getOrientation() {
        return orientation;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public void changeCheck() {
        this.check = !check;
    }
}
