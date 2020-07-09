package com.gvsoft.gofun_ad.manager;

import android.content.Context;
import android.text.TextUtils;

import com.gvsoft.gofun_ad.inter.Callback;
import com.gvsoft.gofun_ad.inter.OnDataReadyCallback;
import com.gvsoft.gofun_ad.manager.carousel.OnDataCallback;
import com.gvsoft.gofun_ad.manager.splash.data.db.SplashAdDbDao;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.tread.ThreadPoolUtil;
import com.gvsoft.gofun_ad.util.AdLogUtils;
import com.gvsoft.gofun_ad.util.AdPathUtils;
import com.gvsoft.gofun_ad.util.DownloadUtils;
import com.gvsoft.gofun_ad.util.MainThreadUtils;
import com.gvsoft.gofun_ad.util.NetWorkUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class AdEngine {

    private Context context;
    private List<AdData> localList;

    public AdEngine(Context context) {
        this.context = context;
        ThreadPoolUtil.defaultInstance().getExecutor();//初始化单例
    }

    private List<AdData> data = new ArrayList<>();

    public AdEngine setData(List<AdData> data) {
        if (data == null) return this;
        if (this.data != null) {
            this.data.clear();
            this.data.addAll(data);
        }
        return this;
    }


    public void getNewData() {
        if (!NetWorkUtil.isWifi(context)) {//未开启wifi不更新全部数据
            return;
        }
        //获取本地缓存
        localList = SplashAdDbDao.getInstance(context).queryAll();
        //todo request
        MainThreadUtils.delayedRunOnMainThread(new Runnable() {
            @Override
            public void run() {
                AdLogUtils.i("===getNewData===success===>");
                String s0 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1253337603,395256110&fm=26&gp=0.jpg";
                String s1 = "http://pic1.win4000.com/pic/e/88/1a3166c6ef_250_300.jpg";
                String s2 = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1276311334,1321953888&fm=26&gp=0.jpg";
                String s3 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3621776991,4158598969&fm=26&gp=0.jpg";
                String s4 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1337035159,297537113&fm=26&gp=0.jpg";
                String s5 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1553210546,907004571&fm=11&gp=0.jpg";
                String s6 = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2237446662,1891834123&fm=26&gp=0.jpg";
                String video = "http://video.pp.cn/fs08/2017/01/16/3/200_528893ee2d1573573679809fb7a75b70.mp4";
                String json = "http://material.shouqiev.com/ca243b2857f672054fb1b22123c84ef7.json";
                List<String> strings = new ArrayList<>();
                strings.add(s0);
                strings.add(s1);
                strings.add(s2);
                strings.add(s3);
                strings.add(s4);
                strings.add(s5);
                strings.add(s6);
                strings.add(video);
                strings.add(json);

                List<AdData> list = new ArrayList<>();
                for (int i = 0; i < strings.size(); i++) {
                    AdData e = new AdData();
                    e.setId(i + "");
                    e.setViewUrl(strings.get(i));
                    e.setPlayTime(15);
                    if (i == 2 || i == 3) {
                        e.setEndTime(1593747308000L);
                    } else {
                        e.setEndTime(1625801708000L);
                    }
                    list.add(e);
                }

                checkData(list);
            }
        }, 2000);
    }

    public void getSingleData(OnDataReadyCallback dataReadyCallback) {
        //todo 模拟请求  请求一条数据
        MainThreadUtils.delayedRunOnMainThread(new Runnable() {
            @Override
            public void run() {
                String id = "7";
                String s6 = "http://video.pp.cn/fs08/2017/01/16/3/200_528893ee2d1573573679809fb7a75b70.mp4";
                AdData d = new AdData();
                d.setId(id);
                d.setEndTime(1625801708000L);
                d.setViewUrl(s6);
                d.setPlayTime(18);
                if (d==null|| TextUtils.isEmpty(d.getViewUrl())){
                    if (dataReadyCallback != null) {
                        dataReadyCallback.skip();
                    }
                    getNewData();
                    return;
                }
                AdData adData = SplashAdDbDao.getInstance(context).queryByAdId(d.getId());
                if (adData != null && !TextUtils.isEmpty(adData.getFile())) {
                    adData.setShowLogoTab(1);
                    adData.setShowAdTab(1);
                    adData.setActionUrl("https://www.baidu.com");
                    if (AdPathUtils.isFileExists(adData.getFile())) {//有缓存 显示并加载新数据
                        if (dataReadyCallback != null) {
                            dataReadyCallback.onDataReady(adData);
                        }
                        //去更新全部缓存
                        getNewData();
                    } else {
                        SplashAdDbDao.getInstance(context).delete(adData);
                        //直接跳过去首页
                        if (dataReadyCallback != null) {
                            dataReadyCallback.skip();
                        }
                        //去缓存单条数据
                        cacheSingleData(d, true);
                    }
                } else {//无db
                    boolean exists = AdPathUtils.isFileExists(context, d.getViewUrl());
                    if (exists){
                        File file = AdPathUtils.getFilePathAndCreate(context, d.getViewUrl());
                        if (file!=null){
                            d.setFile(file.getAbsolutePath());
                            SplashAdDbDao.getInstance(context).insertOrUpdate(d);
                            if (dataReadyCallback != null) {
                                dataReadyCallback.onDataReady(adData);
                            }
                            //去更新全部缓存
                            getNewData();
                            return;
                        }
                    }
                    //直接跳过去首页
                    if (dataReadyCallback != null) {
                        dataReadyCallback.skip();
                    }
                    //去缓存单条数据
                    cacheSingleData(d, true);
                }
            }
        }, 3000);
    }

    private void checkData(List<AdData> list) {
        List<String> current = new ArrayList<>();
        for (AdData adData : list) {
            current.add(adData.getId());
            checkItem(adData);
        }
        //删除不存在的
        SplashAdDbDao.getInstance(context).deleteNoExistAndInvalid(current);
    }

    private synchronized void checkItem(AdData adData) {
        if (adData == null) return;
        if (!TextUtils.isEmpty(adData.getViewUrl())) {
            boolean exist = AdPathUtils.isFileExists(context, adData.getViewUrl());
            if (!exist) {
                cacheSingleData(adData, false);
            }
//            else {
//                AdData d = SplashAdDbDao.getInstance(context).queryByAdId(adData.getId());
//                if (d == null || TextUtils.isEmpty(d.getFile())) {
//                    AdPathUtils.deleteFile(context, adData.getViewUrl());
//                    cacheSingleData(adData, false);//清除重新下载
//                }
//            }
        }
    }

    private void cacheSingleData(AdData adData, boolean needUpdate) {
        DownloadUtils.load(context, adData, new Callback() {
            @Override
            public void onFailure(AdData data) {

            }

            @Override
            public void onResponse(AdData data, File file) {
                insertDB(data);
                if (needUpdate) {
                    getNewData();
                }
            }
        });
    }

    private void insertDB(AdData data) {
        if (data != null) {
            SplashAdDbDao.getInstance(context).insertOrUpdate(data);
        }
    }

    public void getViewPagerData(OnDataCallback callback) {
        //todo 模拟请求  请求一条数据
        MainThreadUtils.delayedRunOnMainThread(new Runnable() {
            @Override
            public void run() {
                String s0 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1253337603,395256110&fm=26&gp=0.jpg";
                String s1 = "http://pic1.win4000.com/pic/e/88/1a3166c6ef_250_300.jpg";
                String s2 = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1276311334,1321953888&fm=26&gp=0.jpg";
                String s3 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3621776991,4158598969&fm=26&gp=0.jpg";
                String s4 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1337035159,297537113&fm=26&gp=0.jpg";
                String s5 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1553210546,907004571&fm=11&gp=0.jpg";
                String s6 = "https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2237446662,1891834123&fm=26&gp=0.jpg";
                List<String> strings = new ArrayList<>();
                strings.add(s0);
                strings.add(s1);
                strings.add(s2);
                strings.add(s3);
                strings.add(s4);
                strings.add(s5);
                strings.add(s6);

                List<AdData> list = new ArrayList<>();
                for (int i = 0; i < strings.size(); i++) {
                    AdData e = new AdData();
                    e.setId(i + "");
                    e.setViewUrl(strings.get(i));
                    e.setActionUrl(strings.get(i));
                    e.setPlayTime(15);
                    list.add(e);
                }
                if (callback != null) {
                    callback.onDataReady(list);
                }
            }
        }, 300);
    }

    public void getBannerData(OnDataCallback onDataCallback) {
        //todo 模拟请求  请求一条数据
        MainThreadUtils.delayedRunOnMainThread(new Runnable() {
            @Override
            public void run() {
                String s0 = "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=1253337603,395256110&fm=26&gp=0.jpg";
                String s2 = "http://video.pp.cn/fs08/2017/01/16/3/200_528893ee2d1573573679809fb7a75b70.mp4";
                String s3 = "http://material.shouqiev.com/ca243b2857f672054fb1b22123c84ef7.json";
//                String s2 = "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1276311334,1321953888&fm=26&gp=0.jpg";
//                String s3 = "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=3621776991,4158598969&fm=26&gp=0.jpg";
                List<String> strings = new ArrayList<>();
                strings.add(s0);
                strings.add(s2);
                strings.add(s3);

                List<AdData> list = new ArrayList<>();
                for (int i = 0; i < strings.size(); i++) {
                    AdData e = new AdData();
                    e.setId(i + "");
                    e.setViewUrl(strings.get(i));
                    e.setActionUrl(strings.get(i));
                    e.setPlayTime(15);
                    list.add(e);
                }
                if (onDataCallback != null) {
                    onDataCallback.onDataReady(list);
                }
            }
        }, 300);
    }
}
