package com.hddata.localalbumapplication.Common;

import android.widget.ImageView;

import com.hddata.localalbumapplication.R;

/**
 * Created by ustc on 2016/9/30.
 */

public class CommonFunction {

    public static void setCheckboxIcon(ImageView iv, boolean ret) {
        if (ret){
            iv.setImageResource(R.mipmap.icon_checkbox_on);
        }else {
            iv.setImageResource(R.mipmap.icon_checkbox_off);
        }
    }

}
