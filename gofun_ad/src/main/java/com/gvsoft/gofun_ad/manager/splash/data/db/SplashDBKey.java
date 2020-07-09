package com.gvsoft.gofun_ad.manager.splash.data.db;

public interface SplashDBKey {
    String DB_TABLE = "splash_list";
    String ID = "_id";//子增长id
    String AD_ID = "ad_id";//广告id String
    String VIEW_URL = "viewUrl";//url路径 String
    String PATH = "path";//本地缓存路径 String
    String SHOW_COUNT = "showCount";//使用次数  预留字段 暂时无用 int
    String END_TIME = "endTime";//有效期 long
    String START_TIME = "startTime";//开始时间  预留字段 暂时无用 long
    String FILE_FORMAT = "fileFormat";//文件格式 0 图片 1.视频 2.json  int
    String FORCE = "force";//是不是强制展示 (1,强制 0,不强制) 预留字段 暂时无用  int
    String ACTION_URL = "actionUrl";//跳转链接  String
    String OTHER_ID = "otherId";//第三方标识  预留字段 暂时无用 String
    String AD_FROM = "ad_from";//(广告来源 0:自己 1,第三方) int
    String AD_CODE = "ad_code";//广告code String
    String AD_NAME = "ad_name";//广告名称 String
    String PERIOD_TYPE = "period_type";//投放时间段:0全天投放 1指定时段 int
    String CITY_CODE = "city_code";//城市  //String
    String SHOW_SORT = "show_sort";//展示顺序 int
    String PLAY_TIME = "play_time";//展示时间


    String[] ALL_DATA = {ID,AD_ID, VIEW_URL, PATH, SHOW_COUNT, END_TIME, START_TIME, FILE_FORMAT, FORCE
            , ACTION_URL, OTHER_ID, AD_FROM, AD_CODE, AD_NAME, PERIOD_TYPE, SHOW_SORT,PLAY_TIME};
}
