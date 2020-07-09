package com.gvsoft.gofun_ad.tread.download;

import android.content.Context;
import android.text.TextUtils;

import com.gvsoft.gofun_ad.inter.Callback;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.util.AdLogUtils;
import com.gvsoft.gofun_ad.util.AdPathUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadRunnable implements Runnable {

    private String urlLocation;

    private Context context;

    private Callback callback;

    private AdData data;

    public DownloadRunnable(Context context, AdData data, Callback callback) {
        this.data = data;
        this.urlLocation = data != null ? data.getViewUrl() : "";
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void run() {
        InputStream in = null;
        FileOutputStream out = null;
        HttpURLConnection conn = null;
        try {
            if (data == null || TextUtils.isEmpty(urlLocation)) {
                return;
            }
            conn = getHttp();
            long fileLength = conn.getContentLength();
            File file = AdPathUtils.getFilePathAndCreate(context, urlLocation);
            out = new FileOutputStream(file);//为指定的文件路径创建文件输出流
            in = conn.getInputStream();
            byte[] buffer = new byte[1024 * 1024];
            int len = 0;
            long readLength = 0;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);//从buffer的第0位开始读取len长度的字节到输出流
                readLength += len;
                int curProgress = (int) (((float) readLength / fileLength) * 100);
                AdLogUtils.e("=======curProgress=======>"+curProgress);
                if (readLength >= fileLength) {
                    break;
                }
            }
            out.flush();
            data.setFile(file.getAbsolutePath());
            if (callback != null) {
                callback.onResponse(data, file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            /**
             * 处理下载过程中网络掉线的情况
             */
            File file = AdPathUtils.getFilePathAndCreate(context, urlLocation);
            if (file != null && file.exists()) {
                file.delete();
            }
            if (callback != null) {
                callback.onFailure(data);
            }
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public HttpURLConnection getHttp() throws IOException {
        URL url = null;
        if (urlLocation != null) {
            url = new URL(urlLocation);
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(30000);
        conn.setRequestMethod("GET");
        return conn;
    }

}
