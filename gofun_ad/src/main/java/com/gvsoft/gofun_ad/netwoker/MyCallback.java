package com.gvsoft.gofun_ad.netwoker;

public interface MyCallback<T> {

    void success(int code, String message, T data);

    void fail(int code, String message);
}
