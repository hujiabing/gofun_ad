package com.gvsoft.gofun_ad.model;

public class AdData {

    /**
     * showLogoTab : 1
     * viewUrl : http://images.51gofunev.com/gofunapp/images/20200526/IXgZHhmJMR.jpg
     * actionUrl : gofun://deposits
     * showAdTab : 0
     * playTime : 5
     * startTime : 1590808065
     * id : 1469
     * endTime : 1595473665
     * advertisingSwitch : 1
     * isShow : 1
     */

    private int showLogoTab;
    private String viewUrl;
    private String actionUrl;
    private int showAdTab;
    private int playTime;
    private long startTime;
    private String id;
    private long endTime;
    private String advertisingSwitch;
    private int isShow;
    private int adType;//0 图片 1 视频 2 视频 lottie
    private String file;

    public int getShowLogoTab() {
        return showLogoTab;
    }

    public void setShowLogoTab(int showLogoTab) {
        this.showLogoTab = showLogoTab;
    }

    public String getViewUrl() {
        return viewUrl;
    }

    public void setViewUrl(String viewUrl) {
        this.viewUrl = viewUrl;
    }

    public String getActionUrl() {
        return actionUrl;
    }

    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }

    public int getShowAdTab() {
        return showAdTab;
    }

    public void setShowAdTab(int showAdTab) {
        this.showAdTab = showAdTab;
    }

    public int getPlayTime() {
        return playTime;
    }

    public void setPlayTime(int playTime) {
        this.playTime = playTime;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getAdvertisingSwitch() {
        return advertisingSwitch;
    }

    public void setAdvertisingSwitch(String advertisingSwitch) {
        this.advertisingSwitch = advertisingSwitch;
    }

    public int getIsShow() {
        return isShow;
    }

    public void setIsShow(int isShow) {
        this.isShow = isShow;
    }

    public int getAdType() {
        return adType;
    }

    public void setAdType(int adType) {
        this.adType = adType;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return "AdData{" +
                "showLogoTab=" + showLogoTab +
                ", viewUrl='" + viewUrl + '\'' +
                ", actionUrl='" + actionUrl + '\'' +
                ", showAdTab=" + showAdTab +
                ", playTime=" + playTime +
                ", startTime=" + startTime +
                ", id='" + id + '\'' +
                ", endTime=" + endTime +
                ", advertisingSwitch='" + advertisingSwitch + '\'' +
                ", isShow=" + isShow +
                ", adType=" + adType +
                ", file='" + file + '\'' +
                '}';
    }
}
