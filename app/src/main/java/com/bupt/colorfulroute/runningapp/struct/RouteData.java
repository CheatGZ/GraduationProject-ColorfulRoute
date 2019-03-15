package com.bupt.colorfulroute.runningapp.struct;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class RouteData implements Serializable {
    //方向关键点集
    public ArrayList<LatLng> keyPoints = new ArrayList<LatLng>();
    //绘制路径规划方案预览时的路径
    public ArrayList<LatLng> ployPoints = new ArrayList<LatLng>();
    //圆心坐标
    public LatLng centerPoint;
    //跑步距离
    public double length;
    //实际规划距离
    public double realLength;
}
