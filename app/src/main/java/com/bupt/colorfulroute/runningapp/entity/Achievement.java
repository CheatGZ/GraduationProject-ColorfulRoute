package com.bupt.colorfulroute.runningapp.entity;

public class Achievement {
    private Integer titleId;//称号ID
    private String title;
    private Long timeAchieved;//获取时间
    private Integer icon;//图片
    private Integer unIcon;//未获得图片
    private String condition;//获取条件
    private String description;//描述

    public Achievement() {
    }

    public Integer getUnIcon() {
        return unIcon;
    }

    public void setUnIcon(Integer unIcon) {
        this.unIcon = unIcon;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }

    public Long getTimeAchieved() {
        return timeAchieved;
    }

    public void setTimeAchieved(Long timeAchieved) {
        this.timeAchieved = timeAchieved;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
