package com.gvsoft.gofun_ad.netwoker;

import android.text.TextUtils;

import androidx.collection.ArrayMap;

import com.alibaba.fastjson.JSON;
import com.gvsoft.gofun_ad.model.BaseModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class RequestUtils {

    // GET方法
    public static <T> void get(String url, ArrayMap<String, String> param, MyCallback<T> callback, Class<T> tClass) {
        // 拼接请求参数
        if (!param.isEmpty()) {
            StringBuffer buffer = new StringBuffer(url);
            buffer.append('?');
            for (Map.Entry<String, String> entry : param.entrySet()) {
                buffer.append(entry.getKey());
                buffer.append('=');
                buffer.append(entry.getValue());
                buffer.append('&');
            }
            buffer.deleteCharAt(buffer.length() - 1);
            url = buffer.toString();
        }
        Request.Builder builder = new Request.Builder().url(url);
        builder.method("GET", null);
        Request request = builder.build();
        Call call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.fail(-1, e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                pareData(response, callback, tClass);
            }
        });
    }

    public static <T> void get(String url, MyCallback<T> callback, Class<T> tClass) {
        get(url, new ArrayMap<String, String>(), callback, tClass);
    }

    public static final MediaType mediaType = MediaType.get("application/json; charset=utf-8");

    // POST 方法
    public static <T> void post(String url, ArrayMap<String, String> param, MyCallback<T> callback, Class<T> tClass) {
        JSONObject json = new JSONObject();
        try {

            if (!param.isEmpty()) {
                for (Map.Entry<String, String> entry : param.entrySet()) {

                    json.put(entry.getKey(), entry.getValue());

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(mediaType, json.toString());

        Request.Builder builder = new Request.Builder();
        Request request = builder.post(body)
                .url(url)
                .build();
        Call call = OkHttpUtils.getInstance().getOkHttpClient().newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (callback != null) {
                    callback.fail(-1, e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                pareData(response, callback, tClass);
            }
        });
    }

    private static <T> void pareData(Response response, MyCallback<T> callback, Class<T> tClass) {
        try {
            ResponseBody body = response.body();
            if (body != null) {
                String s = body.string();
                if (TextUtils.isEmpty(s)) {
                    if (callback != null) {
                        callback.fail(0, "无数据");
                    }
                    return;
                }
                BaseModel mode = JSON.parseObject(s, BaseModel.class);
                if (mode == null) {
                    if (callback != null) {
                        callback.fail(0, "无数据");
                    }
                    return;
                }
                Object modelData = mode.getModelData();
                if (modelData == null) {
                    if (callback != null) {
                        callback.fail(1, "未按格式返回");
                    }
                    return;
                }
                T data = JSON.parseObject(JSON.toJSONString(modelData), tClass);
                if (callback != null) {
                    callback.success(mode.getCode(), mode.getDesc(), data);
                }
            } else {
                if (callback != null) {
                    callback.fail(0, "无数据");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (callback != null) {
                callback.fail(2, "数据解析错误");
            }
        }
    }

    public static <T> void getAdData(MyCallback<T> callback, Class<T> tClass) {
        String url = "http://gateway20.51gofunev.com:8000/fsda/app/bootImage.json";
        ArrayMap<String, String> params = new ArrayMap<>();
        params.put("userPickCityCode", "010");
        params.put("cityCode", "010");
        post(url, params, callback, tClass);
    }


}
