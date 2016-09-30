package com.personal.localalbum.Util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.personal.localalbum.Info.AlbumPhoto;
import com.personal.localalbum.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by linjizong on 15/6/11.
 */
public class AlbumImageUtil {
    //大图遍历字段
    private static final String[] STORE_IMAGES = {
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATA,
            MediaStore.Images.Media.ORIENTATION
    };
    //小图遍历字段
    private static final String[] THUMBNAIL_STORE_IMAGE = {
            MediaStore.Images.Thumbnails._ID,
            MediaStore.Images.Thumbnails.DATA
    };

    public static List<AlbumPhoto> initImage(Context context) {
        List<AlbumPhoto> paths = new ArrayList<>();
        //获取大图的游标
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  // 大图URI
                STORE_IMAGES,   // 字段
                null,         // No where clause
                null,         // No where clause
                MediaStore.Images.Media.DATE_TAKEN + " DESC"); //根据时间升序
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);//大图ID
                String path = cursor.getString(1);//大图路径
                File file = new File(path);
                //判断大图是否存在
                if (file.exists()) {
                    //获取目录名
                    String parentName = file.getParentFile().getName();

                    if ((parentName.contains("Camera"))
                            || (parentName.contains("camera"))) {
                        //小图URI
                        String thumbUri = getThumbnail(context, id);
                        //获取大图URI
                        String uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().
                                appendPath(Integer.toString(id)).build().toString();
                        if (isEmpty(uri))
                            continue;
                        if (isEmpty(thumbUri))
                            thumbUri = uri;

                        int degree = cursor.getInt(2);
                        if (degree != 0) {
                            degree = degree + 180;
                        }

                        AlbumPhoto photo = new AlbumPhoto(uri, thumbUri, path, 360 - degree);

                        paths.add(photo);
                    }

                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return paths;
    }

    public static void showImage(Context context, ImageView iv, AlbumPhoto photo) {
        SimpleImageLoadingListener loadingListener = new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, final Bitmap bm) {
                if (TextUtils.isEmpty(imageUri)) {
                    return;
                }
                //由于很多图片是白色背景，在此处加一个#eeeeee的滤镜，防止checkbox看不清
                try {
                    ((ImageView) view).getDrawable().setColorFilter(Color.argb(0xff, 0xee, 0xee, 0xee), PorterDuff.Mode.MULTIPLY);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        initImageLoader(context);

        ImageLoader.getInstance().displayImage(photo.getThumbnailUri(), new ImageViewAware(iv),
                getOptions(), loadingListener, null, photo.getOrientation());
    }

    private static String getThumbnail(Context context, int id) {
        //获取大图的缩略图
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                THUMBNAIL_STORE_IMAGE,
                MediaStore.Images.Thumbnails.IMAGE_ID + " = ?", new String[]{
                        id + ""}, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int thumId = cursor.getInt(0);
            String uri = MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI.buildUpon().
                    appendPath(Integer.toString(thumId)).build().toString();
            cursor.close();
            return uri;
        }
        cursor.close();
        return null;
    }

    private static boolean isEmpty(String input) {
        if (input == null || "".equals(input))
            return true;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            if (c != ' ' && c != '\t' && c != '\r' && c != '\n') {
                return false;
            }
        }
        return true;
    }

    private static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY);
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 4);
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //修改连接超时时间5秒，下载超时时间5秒
        config.imageDownloader(new BaseImageDownloader(context, 5 * 1000, 5 * 1000));
        //		config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    private static DisplayImageOptions getOptions() {
        return new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageForEmptyUri(R.mipmap.image_default)
                .showImageOnFail(R.mipmap.image_default)
                .showImageOnLoading(R.mipmap.image_default)
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .setImageSize(new ImageSize(((AlbumContext) context.getApplicationContext()).getQuarterWidth(), 0))
                .displayer(new SimpleBitmapDisplayer()).build();
    }

}
