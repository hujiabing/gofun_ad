package com.gvsoft.gofun_ad.manager.splash.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 开屏页数据库
 */
public class SplashAdDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "ad_data.db";

    private final static int DB_VERSION = 1;

    public SplashAdDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //创建数据库表
        createDbTable(db, SplashDBKey.DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 创建数据库表
     *
     * @param db        数据实例
     * @param tableName 表名
     */
    private void createDbTable(SQLiteDatabase db, String tableName) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE if not exists ")
                .append(tableName)
                .append("(").append(SplashDBKey.ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT")//广告id
                .append(",").append(SplashDBKey.AD_ID).append(" TEXT").append(" UNIQUE")//url路径
                .append(",").append(SplashDBKey.VIEW_URL).append(" TEXT")//url路径
                .append(",").append(SplashDBKey.PATH).append(" TEXT")//本地缓存路径
                .append(",").append(SplashDBKey.SHOW_COUNT).append(" INTEGER")//使用次数  预留字段 暂时无用
                .append(",").append(SplashDBKey.END_TIME).append(" BIGINT")//有效期
                .append(",").append(SplashDBKey.START_TIME).append(" BIGINT")//开始时间  预留字段 暂时无用
                .append(",").append(SplashDBKey.FILE_FORMAT).append(" INTEGER")//文件格式 0 图片 1.视频 2.json
                .append(",").append(SplashDBKey.FORCE).append(" INTEGER")//是不是强制展示 (1,强制 0,不强制) 预留字段 暂时无用
                .append(",").append(SplashDBKey.ACTION_URL).append(" TEXT")//跳转链接
                .append(",").append(SplashDBKey.OTHER_ID).append(" TEXT")//第三方标识  预留字段 暂时无用
                .append(",").append(SplashDBKey.AD_FROM).append(" INTEGER")//(广告来源 0:自己 1,第三方)
                .append(",").append(SplashDBKey.AD_CODE).append(" VARCHA")//广告code
                .append(",").append(SplashDBKey.AD_NAME).append(" VARCHA")//广告名称
                .append(",").append(SplashDBKey.PERIOD_TYPE).append(" INTEGER")//投放时间段:0全天投放 1指定时段
                .append(",").append(SplashDBKey.CITY_CODE).append(" VARCHA")//城市
                .append(",").append(SplashDBKey.SHOW_SORT).append(" INTEGER")//展示顺序
                .append(",").append(SplashDBKey.PLAY_TIME).append(" INTEGER")//展示时间
                .append(")");
        db.execSQL(sb.toString());
    }
}
