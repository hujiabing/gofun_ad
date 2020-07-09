package com.gvsoft.gofun_ad.model;

public class BaseModel<T> {

    private int code;
    private String desc;
    private T modelData;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public T getModelData() {
        return modelData;
    }

    public void setModelData(T modelData) {
        this.modelData = modelData;
    }

}
