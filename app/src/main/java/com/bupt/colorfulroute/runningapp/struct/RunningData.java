package com.bupt.colorfulroute.runningapp.struct;

import com.amap.api.maps.model.LatLng;

import java.io.Serializable;
import java.util.ArrayList;

public class RunningData implements Serializable {
    public ArrayList<LatLng> keyPoints;
    public ArrayList<LatLng> ployPoints;
    public LatLng centerPoint;
    public double length;
    public long startTime;
    public long endTime;
    public long time;
    public long calorie;
}
