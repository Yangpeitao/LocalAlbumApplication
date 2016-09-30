package com.personal.localalbum.Info;

/**
 * Created by ustc on 2016/9/27.
 */

public class AlbumPhoto {
    /**
     * 原图URI
     */
    private String originalUri;
    /**
     * 原图的存储路径
     */
    private String path;
    /**
     * 缩略图URI
     */
    private String thumbnailUri;
    /**
     * 图片旋转角度
     */
    private int orientation;

    private boolean check;

    public AlbumPhoto(String originalUri, String thumbnailUri, String path, int orientation) {
        this.originalUri = originalUri;
        this.thumbnailUri = thumbnailUri;
        this.path = path;
        this.orientation = orientation;
        this.check = false;
    }

    public String getPath() {
        return path;
    }

    public String getThumbnailUri() {
        return thumbnailUri;
    }

    public String getOriginalUri() {
        return originalUri;
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
