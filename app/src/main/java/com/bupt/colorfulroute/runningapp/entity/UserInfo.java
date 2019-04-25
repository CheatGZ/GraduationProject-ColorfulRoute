package com.bupt.colorfulroute.runningapp.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class UserInfo extends BmobObject {
    private String account;//微博账号
    private String name;//昵称
    private String email;//邮箱
    private String phone;//手机号
    private String password;//密码
    private String gender;//性别
    private Integer age;//年龄
    private Integer height;//身高
    private Integer weight;//体重
    private Double totalLength;//总长度
    private Long totalTime;//总时间
    private Integer totalCalorie;//总卡路里
    private String icon;//微博头像
    private String description;//签名
    private Integer number;//跑步次数
    private String title;//称号
    private List<Achievement> achievement;//成就信息
    private List<Double> location;//定位信息
    private Long logInTime;//最新登录时间

    public UserInfo() {
    }

    public Long getLogInTime() {
        return logInTime;
    }

    public void setLogInTime(Long logInTime) {
        this.logInTime = logInTime;
    }

    public List<Double> getLocation() {
        return location;
    }

    public void setLocation(List<Double> location) {
        this.location = location;
    }

    public List<Achievement> getAchievement() {
        return achievement;
    }

    public void setAchievement(List<Achievement> achievement) {
        this.achievement = achievement;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public Double getTotalLength() {
        return totalLength;
    }

    public void setTotalLength(Double totalLength) {
        this.totalLength = totalLength;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Long totalTime) {
        this.totalTime = totalTime;
    }

    public Integer getTotalCalorie() {
        return totalCalorie;
    }

    public void setTotalCalorie(Integer totalCalorie) {
        this.totalCalorie = totalCalorie;
    }
}
