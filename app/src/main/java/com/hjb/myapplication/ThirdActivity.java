package com.hjb.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.gvsoft.gofun_ad.manager.splash.data.db.SplashAdDbDao;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.util.AdLogUtils;
import com.gvsoft.gofun_ad.util.AdPathUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThirdActivity extends AppCompatActivity {

    @BindView(R.id.tv_add)
    TextView mTvAdd;
    @BindView(R.id.tv_delete)
    TextView mTvDelete;
    @BindView(R.id.tv_que)
    TextView mTvQue;
    @BindView(R.id.tv_add_one)
    TextView mTvAddOne;
    private List<AdData> data;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        ButterKnife.bind(this);
        data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            AdData d = new AdData();
            d.setViewUrl("http:" + i);
            d.setId("" + i);
            d.setActionUrl("ActionUrl" + i);
            d.setAdType(0);
            d.setFile("file" + i);
            d.setStartTime(1593158375000L);
            if (i % 2 == 0) {
                d.setEndTime(1593244775000L);
            } else {
                d.setEndTime(1596009575000L);
            }
            data.add(d);
        }
    }

    @OnClick({R.id.tv_add, R.id.tv_delete, R.id.tv_que, R.id.tv_add_one, R.id.tv_que_all, R.id.tv_clear})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_add:
                SplashAdDbDao.getInstance(this).insert(data);
                break;
            case R.id.tv_delete:
                AdData d1 = new AdData();
                d1.setId("12121212");
                SplashAdDbDao.getInstance(this).delete(d1);
                break;
            case R.id.tv_que:
                AdData adData = SplashAdDbDao.getInstance(this).queryByAdId("1");
                break;
            case R.id.tv_que_all:
                List<AdData> list1 = SplashAdDbDao.getInstance(this).queryAll();
                if (list1!=null&&list1.size()>0){
                    for (AdData data : list1) {
                        AdLogUtils.e("^^^^^^^^^^^^", data.getId() + "%%" + data.getViewUrl());
                    }
                }else {
                    AdLogUtils.e("^^^^^^无数据^^^^^^");
                }

                break;

            case R.id.tv_add_one:
                AdData d = new AdData();
                d.setViewUrl("http:wqqwqw");
                d.setId("12121212");
                d.setActionUrl("ActionUrl");
                d.setAdType(0);
                d.setFile("file");
                d.setStartTime(System.currentTimeMillis());
                if (isadd) {
                    d.setEndTime(System.currentTimeMillis() + 10000);
                } else {
                    d.setEndTime(System.currentTimeMillis() - 10000);
                }
                SplashAdDbDao.getInstance(this).insertOrUpdate(d);
                isadd = !isadd;
                break;
            case R.id.tv_clear:
                SplashAdDbDao.getInstance(this).clear();
                AdPathUtils.clear(this);
                break;

        }
    }

    boolean isadd = false;
}
