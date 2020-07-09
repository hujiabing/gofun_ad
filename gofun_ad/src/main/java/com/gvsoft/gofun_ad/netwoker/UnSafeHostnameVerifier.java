package com.gvsoft.gofun_ad.netwoker;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class UnSafeHostnameVerifier implements HostnameVerifier {
    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
}
