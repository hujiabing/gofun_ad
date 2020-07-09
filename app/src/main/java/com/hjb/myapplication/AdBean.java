package com.hjb.myapplication;

public class AdBean  {
    private String url;

    private int type;


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public AdBean(String url, int type) {
        this.url = url;
        this.type = type;
    }

    public AdBean() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
