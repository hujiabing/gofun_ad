package com.gvsoft.gofun_ad.netwoker;

import com.gvsoft.gofun_ad.BuildConfig;

import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;


public class OkHttpUtils {

    private static final int DEFAULT_TIMEOUT = 30;
    private static final int DEFAULT_READ_TIMEOUT = 300;
    private static final int DEFAULT_WRITE_TIMEOUT = 300;
    private final static String HTTPS_TLS = "TLS";
    private static OkHttpUtils instance;
    private volatile OkHttpClient mOkHttpClient;
    private List<Interceptor> interceptors = new ArrayList<>();

    private OkHttpUtils() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (interceptors != null) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        try {
            X509TrustManager trustManager = new UnSafeTrustManager();
            SSLContext sslContext = SSLContext.getInstance(HTTPS_TLS);
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory, trustManager);
            builder.hostnameVerifier(new UnSafeHostnameVerifier());
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(DEFAULT_READ_TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(DEFAULT_WRITE_TIMEOUT, TimeUnit.SECONDS);
//        builder.dispatcher(new Dispatcher(ThreadPoolUtil.defaultInstance().getExecutor()));
        if (BuildConfig.GOFUN_DEBUG) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            //设置 Debug Log 模式
            builder.addInterceptor(loggingInterceptor);
        }

        this.mOkHttpClient = builder.build();
    }

    public static OkHttpUtils getInstance() {
        if (instance == null) {
            synchronized (OkHttpUtils.class) {
                if (instance == null) {
                    instance = new OkHttpUtils();
                }
            }
        }
        return instance;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }
}
