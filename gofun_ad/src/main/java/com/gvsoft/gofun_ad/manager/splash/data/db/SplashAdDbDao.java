package com.gvsoft.gofun_ad.manager.splash.data.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.util.AdLogUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SplashAdDbDao {
    private volatile static SplashAdDbDao instance;
    private final SplashAdDbHelper dbHelper;

    private SplashAdDbDao(Context context) {
        dbHelper = new SplashAdDbHelper(context);
    }


    public static SplashAdDbDao getInstance(Context context) {
        if (instance == null) {
            synchronized (SplashAdDbDao.class) {
                if (instance == null) {
                    instance = new SplashAdDbDao(context);
                }
            }
        }
        return instance;
    }

    /**
     * 插入多条数据
     *
     * @param data
     */
    public synchronized void insert(List<AdData> data) {
        if (data == null || data.size() == 0) return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for (AdData d : data) {
                ContentValues cv = new ContentValues();
                cv.put(SplashDBKey.AD_ID, d.getId());
                cv.put(SplashDBKey.ACTION_URL, d.getActionUrl());
                cv.put(SplashDBKey.VIEW_URL, d.getViewUrl());
                cv.put(SplashDBKey.START_TIME, d.getStartTime());
                cv.put(SplashDBKey.END_TIME, d.getEndTime());
                cv.put(SplashDBKey.PATH, d.getFile());
                cv.put(SplashDBKey.PLAY_TIME, d.getPlayTime());
                cv.put(SplashDBKey.FILE_FORMAT, d.getAdType());
                db.replace(SplashDBKey.DB_TABLE, null, cv);
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 插入单条数据
     *
     * @param d
     */
    public synchronized void insertOrUpdate(AdData d) {
        if (d == null) return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues cv = new ContentValues();
            cv.put(SplashDBKey.AD_ID, d.getId());
            cv.put(SplashDBKey.ACTION_URL, d.getActionUrl());
            cv.put(SplashDBKey.VIEW_URL, d.getViewUrl());
            cv.put(SplashDBKey.START_TIME, d.getStartTime());
            cv.put(SplashDBKey.END_TIME, d.getEndTime());
            cv.put(SplashDBKey.PATH, d.getFile());
            cv.put(SplashDBKey.FILE_FORMAT, d.getAdType());
            cv.put(SplashDBKey.PLAY_TIME, d.getPlayTime());
            db.replace(SplashDBKey.DB_TABLE, null, cv);
            db.setTransactionSuccessful();
            AdLogUtils.i("===入库成功==>" + d.getId() + "==url==>" + d.getViewUrl() + "==>" + Thread.currentThread().getName());
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * truncate
     * 清空数据库中数据
     */
    public void clear() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            db.delete(SplashDBKey.DB_TABLE, null, null);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 删除某一条
     *
     * @param d
     */
    public synchronized void delete(AdData d) {
        if (d == null) return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            String sql = "DELETE FROM " + SplashDBKey.DB_TABLE + " where " + SplashDBKey.AD_ID + " = " + d.getId();
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    /**
     * 删除某一条
     */
    public synchronized void deleteByUrl(String url) {
        if (TextUtils.isEmpty(url)) return;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            String sql = "DELETE FROM " + SplashDBKey.DB_TABLE + " where " + SplashDBKey.VIEW_URL + " = " + url;
            db.execSQL(sql);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public AdData queryByAdId(String id) {
        AdData data = null;
        List<AdData> list = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ")
                    .append(SplashDBKey.DB_TABLE)
                    .append(" WHERE ")
                    .append(SplashDBKey.AD_ID)
                    .append(" = ")
                    .append(id);
            Cursor cursor = db.rawQuery(sb.toString(), null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    list = new ArrayList<>();
                    while (!cursor.isAfterLast()) {
                        AdData d = getData(cursor);
                        if (d != null) {
                            list.add(d);
                        }
                        cursor.moveToNext();
                    }
                    if (list.size() > 0) {
                        data = list.get(0);
                    }
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();

        }
        return data;
    }

    /**
     * 查询全部
     *
     * @return
     */
    public List<AdData> queryByCondition() {
        List<AdData> list = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("SELECT * FROM ")
                    .append(SplashDBKey.DB_TABLE)
                    .append(" WHERE ")
                    .append(SplashDBKey.END_TIME)
                    .append(" > ")
                    .append(System.currentTimeMillis())
                    .append(" ORDER BY ")
                    .append(SplashDBKey.ID)
                    .append(" ")
                    .append("ASC");
            Cursor cursor = db.rawQuery(sb.toString(), null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    list = new ArrayList<>();
                    while (!cursor.isAfterLast()) {
                        AdData d = getData(cursor);
                        if (d != null) {
                            list.add(d);
                        }
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();

        }
        return list;
    }

    /**
     * 查询全部
     *
     * @return
     */
    public List<AdData> queryAll() {
        List<AdData> list = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            Cursor cursor = db.query(SplashDBKey.DB_TABLE, null
                    , null, null, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    list = new ArrayList<>();
                    while (!cursor.isAfterLast()) {
                        AdData d = getData(cursor);
                        if (d != null) {
                            list.add(d);
                        }
                        cursor.moveToNext();
                    }
                }
                cursor.close();
            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
            db.close();
        }
        return list;
    }

    private AdData getData(Cursor cursor) {
        if (cursor == null) return null;
        AdData data = new AdData();
        int columnIndex = cursor.getColumnIndex(SplashDBKey.AD_ID);
        data.setId(cursor.getString(columnIndex));
        data.setViewUrl(cursor.getString(cursor.getColumnIndex(SplashDBKey.VIEW_URL)));
        data.setFile(cursor.getString(cursor.getColumnIndex(SplashDBKey.PATH)));
        data.setEndTime(cursor.getLong(cursor.getColumnIndex(SplashDBKey.END_TIME)));
        data.setStartTime(cursor.getLong(cursor.getColumnIndex(SplashDBKey.START_TIME)));
        data.setActionUrl(cursor.getString(cursor.getColumnIndex(SplashDBKey.ACTION_URL)));
        data.setActionUrl(cursor.getString(cursor.getColumnIndex(SplashDBKey.ACTION_URL)));
        data.setPlayTime(cursor.getInt(cursor.getColumnIndex(SplashDBKey.PLAY_TIME)));
//        String SHOW_COUNT = "showCount";//使用次数  预留字段 暂时无用 int
//        String FILE_FORMAT = "fileFormat";//文件格式 0 图片 1.视频 2.json  int
//        String FORCE = "force";//是不是强制展示 (1,强制 0,不强制) 预留字段 暂时无用  int
//        String OTHER_ID = "otherId";//第三方标识  预留字段 暂时无用 String
//        String AD_FROM = "ad_from";//(广告来源 0:自己 1,第三方) int
//        String AD_CODE = "ad_code";//广告code String
//        String AD_NAME = "ad_name";//广告名称 String
//        String PERIOD_TYPE = "period_type";//投放时间段:0全天投放 1指定时段 int
//        String CITY_CODE = "city_code";//城市  //String
//        String SHOW_SORT = "show_sort";//展示顺序 int
        return data;
    }

    /**
     * 删除不存在的
     *
     * @param data
     */
    public synchronized void deleteNoExistAndInvalid(List<String> data) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        List<String> deleteData = new ArrayList<>();
        try {
            Cursor cursor = db.query(SplashDBKey.DB_TABLE, null
                    , null, null, null, null, null);
            if (cursor != null) {
                if (cursor.getCount() > 0) {
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        AdData d = getData(cursor);
                        if (d != null) {
                            if (!data.contains(d.getId()) || d.getEndTime() <= System.currentTimeMillis()) {
                                deleteData.add(d.getId());
                                deleteFile(d.getFile());
                            }
                        }
                        cursor.moveToNext();
                    }
                }
                cursor.close();
                if (deleteData.size() > 0) {
                    for (String d : deleteData) {
                        AdLogUtils.e("====deleteNoExist====>" + d);
                        String sql = "DELETE FROM " + SplashDBKey.DB_TABLE + " where " + SplashDBKey.AD_ID + " = " + d;
                        db.execSQL(sql);
                    }
                }
            }
            db.setTransactionSuccessful();

        } finally {
            db.endTransaction();
            db.close();
        }
    }

    private void deleteFile(String file) {
        if (TextUtils.isEmpty(file)) return;
        File deleteFile = new File(file);
        if (deleteFile.exists()) {
            deleteFile.delete();
            AdLogUtils.e("====deleteNoExist=deleteFile===>" + file);
        }
    }
}
