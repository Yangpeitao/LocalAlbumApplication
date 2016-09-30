package com.hddata.localalbumapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.hddata.localalbumapplication.Common.CommonFunction;
import com.hddata.localalbumapplication.R;
import com.personal.localalbum.Info.AlbumPhoto;
import com.personal.localalbum.Util.AlbumImageUtil;

import java.util.List;

/**
 * Created by ustc on 2016/9/30.
 */

public class PhotoAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private List<AlbumPhoto> list;
    private boolean needCheckbox;

    public PhotoAdapter(List<AlbumPhoto> list, Context context, boolean needCheckbox) {
        this.list = list;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.needCheckbox = needCheckbox;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ItemView view = new ItemView();
        AlbumPhoto photo = list.get(position);
        convertView = this.inflater.inflate(R.layout.item_p0802, null);

        view.ivPicture = (ImageView) convertView.findViewById(R.id.item_p0802_iv_picture);
        view.ivCheckbox = (ImageView) convertView.findViewById(R.id.item_p0802_iv_checkout);

        AlbumImageUtil.showImage(context, view.ivPicture, photo);

        if (needCheckbox) {
            CommonFunction.setCheckboxIcon(view.ivCheckbox, photo.isCheck());
        } else {
            view.ivCheckbox.setVisibility(View.GONE);
        }

        convertView.setTag(photo);
        return convertView;
    }

    private class ItemView {
        private ImageView ivPicture;
        private ImageView ivCheckbox;
    }


}
