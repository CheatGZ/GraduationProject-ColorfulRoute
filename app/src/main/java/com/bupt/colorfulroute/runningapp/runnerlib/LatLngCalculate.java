package com.bupt.colorfulroute.runningapp.runnerlib;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Created by yisic on 2017/7/9.
 */

public class LatLngCalculate {
    private static final double EARTH_RADIUS = 6378137;
    private static final double RAD = Math.PI / 180.0;

    public static double getDistance(double latitude1,
                                     double longitude1,
                                     double latitude2,
                                     double longitude2) {
        double radLat1 = latitude1 * RAD;
        double radLat2 = latitude2 * RAD;
        double a = radLat1 - radLat2;
        double b = (longitude1 - longitude2) * RAD;
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * EARTH_RADIUS;
        s = Math.round(s * 10000) / 10000;
        return s;
    }

    public static long getPathLength(ArrayList<LatLng> points) {
        long length = 0;
        for (int i = 0; i < points.size() - 1; i++) {
            length += getDistance(points.get(i).latitude,
                    points.get(i).longitude,
                    points.get(i + 1).latitude,
                    points.get(i + 1).longitude);
        }
        length += getDistance(points.get(points.size() - 1).latitude,
                points.get(points.size() - 1).longitude,
                points.get(0).latitude,
                points.get(0).longitude);
        return length;
    }

    public static boolean isSamePoint(LatLng point1, LatLng point2) {
        if (point1.latitude == point2.latitude
                && point1.longitude == point2.longitude)
            return true;
//        double cri = 0.00001;
//        if ((!(point1.latitude - point2.latitude > cri
//                && point1.latitude - point2.latitude < (-1) * cri))
//                && (!(point1.longitude - point2.longitude > cri
//                || point1.longitude - point2.longitude < (-1) * cri)))
//            return true;
        return false;
    }
}
