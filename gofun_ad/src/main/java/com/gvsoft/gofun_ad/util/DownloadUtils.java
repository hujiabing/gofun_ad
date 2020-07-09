package com.gvsoft.gofun_ad.util;

import android.content.Context;
import android.text.TextUtils;

import com.gvsoft.gofun_ad.inter.Callback;
import com.gvsoft.gofun_ad.model.AdData;
import com.gvsoft.gofun_ad.tread.download.DownloadRunnable;
import com.gvsoft.gofun_ad.tread.ThreadPoolUtil;

import java.io.File;

public class DownloadUtils {


    public static void load(Context context, AdData data, Callback callback) {
        if (data == null || TextUtils.isEmpty(data.getViewUrl())) {
            callback.onFailure(data);
            return;
        }

        File file = AdPathUtils.getFilePathAndCreate(context, data.getViewUrl());
        if (file != null && file.exists() && file.length() > 0) {
            data.setFile(file.getAbsolutePath());
            ThreadPoolUtil.defaultInstance().execute(new Runnable() {
                @Override
                public void run() {
                    if (callback != null) {
                        callback.onResponse(data, file);
                    }
                }
            });
            return;
        }

        ThreadPoolUtil.defaultInstance().execute(new DownloadRunnable(context, data, callback));
    }
}
