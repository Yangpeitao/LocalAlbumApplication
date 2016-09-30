package com.hddata.localalbumapplication.View;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hddata.localalbumapplication.Adapter.PhotoAdapter;
import com.hddata.localalbumapplication.Common.CommonFunction;
import com.hddata.localalbumapplication.R;
import com.personal.localalbum.Info.AlbumPhoto;
import com.personal.localalbum.Util.AlbumImageUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final static int SCAN = 1;
    public int selectCount = 0;
    private ImageView ivAll;
    private TextView tvSure;
    private TextView tvTitle;
    private RelativeLayout lyAll;
    private GridView gvPhoto;
    private PhotoAdapter adapter;
    private List<AlbumPhoto> photoList = new ArrayList<>();

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SCAN:
                    lyAll.setVisibility(View.VISIBLE);
                    updateList();
                    break;
            }
        }

    };

    public void preformAllButtom(boolean ret) {

        CommonFunction.setCheckboxIcon(ivAll, ret);
        if (selectCount > 0) {
            tvSure.setVisibility(View.VISIBLE);
        } else {
            tvSure.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initList();

        this.ivAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean ret = selectCount == photoList.size();
                selectCount = 0;
                Iterator<AlbumPhoto> iterator = photoList.iterator();
                while (iterator.hasNext()) {
                    AlbumPhoto photo = iterator.next();
                    if (ret) {
                        photo.setCheck(false);
                    } else {
                        photo.setCheck(true);
                        selectCount++;
                    }
                }
                updateList();
                preformAllButtom(!ret);
            }
        });
        this.tvSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(MainActivity.this, "您选择了 " + selectCount + " 张图像！", Toast.LENGTH_SHORT).show();

            }
        });
        this.gvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlbumPhoto photo = (AlbumPhoto) view.getTag();
                photo.changeCheck();
                if (photo.isCheck()) {
                    selectCount++;
                } else {
                    selectCount--;
                }
                updateList();
                preformAllButtom(selectCount == photoList.size());
            }
        });
    }

    private void initView() {
        this.tvSure = (TextView) findViewById(R.id.title_tv_right);
        this.tvTitle = (TextView) findViewById(R.id.title_tv_center);
        this.gvPhoto = (GridView) findViewById(R.id.p0802_gv_carrier);
        this.ivAll = (ImageView) findViewById(R.id.p0802_iv_all);
        this.lyAll = (RelativeLayout) findViewById(R.id.p0802_ly_all);
    }

    private void init() {
        lyAll.setVisibility(View.GONE);
        CommonFunction.setCheckboxIcon(ivAll, false);
        tvSure.setVisibility(View.INVISIBLE);
        tvSure.setText(R.string.bt_sure);
        tvTitle.setText(R.string.title_all_photo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initList() {

        new Thread(new Runnable() {

            @Override
            public void run() {

                photoList = AlbumImageUtil.initImage(MainActivity.this);
                //通知Handler扫描图片完成
                handler.sendEmptyMessage(SCAN);
            }
        }).start();
    }

    private void updateList() {
        adapter = new PhotoAdapter(photoList, MainActivity.this, true);
        gvPhoto.setAdapter(adapter);
    }
}
