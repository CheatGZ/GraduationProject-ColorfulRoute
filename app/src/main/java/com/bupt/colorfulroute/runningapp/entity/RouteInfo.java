package com.bupt.colorfulroute.runningapp.entity;

import com.amap.api.maps.model.LatLng;
import java.util.ArrayList;
import cn.bmob.v3.BmobObject;

public class RouteInfo extends BmobObject {
    private String account;
    private String startTime;
    private String endTime;
    private Double length;
    private Integer calorie;
    private Long time;
    private ArrayList<LatLng> keyPoints;
    private LatLng centerPoints;
    private String remark;
    private Boolean isShow;

    public RouteInfo() {
    }

    public Boolean getShow() {
        return isShow;
    }

    public void setShow(Boolean show) {
        isShow = show;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }

    public Integer getCalorie() {
        return calorie;
    }

    public void setCalorie(Integer calorie) {
        this.calorie = calorie;
    }

    public ArrayList<LatLng> getKeyPoints() {
        return keyPoints;
    }

    public void setKeyPoints(ArrayList<LatLng> keyPoints) {
        this.keyPoints = keyPoints;
    }

    public LatLng getCenterPoints() {
        return centerPoints;
    }

    public void setCenterPoints(LatLng centerPoints) {
        this.centerPoints = centerPoints;
    }
}

