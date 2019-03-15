package com.bupt.colorfulroute.runningapp.entity;

/**
 * ClassName:PositionEntity <br/>
 * Function: 封装的关于位置的实体 <br/>
 * @author yiyi.qi * @version * @since JDK 1.6
 * @see */
public class PositionEntity {
    @Override
    public String toString() {
        return "PositionEntity{" +
                "latitue=" + latitue +
                ", longitude=" + longitude +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
    public double latitue;
    public double longitude;
    public String address;
    public String city;
    public PositionEntity() {}
    public PositionEntity(double latitude, double longtitude, String address, String city) {
        this.latitue = latitude;
        this.longitude = longtitude;
        this.address = address;
        this.city = city;
    }

}