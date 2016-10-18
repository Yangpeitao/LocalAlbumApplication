package com.personal.localalbum.Util;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.personal.localalbum.Info.AlbumPhoto;
import com.personal.localalbum.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户的接口类
 * Created by 杨培韬 on 2016/9/27.
 */
public class AlbumImageUtil {

    public static List<AlbumPhoto> initImage(Context context) {
        List<AlbumPhoto> list = new ArrayList<>();
        //获取大图的游标
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  // 大图URI
                new String[]{
                        MediaStore.Images.Media.DATA,
                        MediaStore.Images.Media.ORIENTATION
                },   // 字段
                MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                new String[]{
                        "image/jpeg", "image/png"
                },
                MediaStore.Images.Media.DATE_MODIFIED + " DESC"); //根据时间降序
        if (cursor.moveToFirst()) {
            do {
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));//大图路径
                File file = new File(path);
                //判断大图是否存在
                if (file.exists()) {
                    //获取目录名
                    String parentName = file.getParentFile().getName();

//                    if ((parentName.contains("Camera"))
//                            || (parentName.contains("camera"))) {

                        int degree = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                        if (degree != 0) {
                            degree = degree + 180;
                        }

                        AlbumPhoto photo = new AlbumPhoto(path, 360 - degree);

                        list.add(photo);
//                    }

                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public static void showImage(Context context, ImageView iv, AlbumPhoto photo) {
        iv.setImageResource(R.mipmap.image_default);
        ImageLoader.getInstance().loadImage(photo.getPath(), iv);
    }
}
