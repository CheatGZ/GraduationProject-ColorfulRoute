package com.bupt.colorfulroute.runningapp.tools;

import com.amap.api.maps.model.LatLng;
import com.bupt.colorfulroute.runningapp.runnerlib.LatLngCalculate;
import com.bupt.colorfulroute.runningapp.struct.RouteData;

import java.util.ArrayList;

public class ReplanningRoute {
    public static RouteData getRoute(LatLng locat, RouteData destinationData, double alreadyRunLength) {
        RouteData routeData = new RouteData();
        routeData.centerPoint = destinationData.centerPoint;
        routeData.length = destinationData.length;
//        routeData.passingPoint = destinationData.passingPoint;
//        routeData.isMerchant = destinationData.isMerchant;
        //routeData.merchantInfo = destinationData.merchantInfo;
        routeData.ployPoints = destinationData.ployPoints;
        ArrayList<LatLng> keyPoints = destinationData.keyPoints;
        ArrayList<LatLng> points = new ArrayList<LatLng>();
        double restLength = destinationData.length - alreadyRunLength;
        LatLng lastPoint = keyPoints.get(keyPoints.size() - 1);
//        if (routeData.passingPoint == null) {
            int ansIndex = 0;
            double distance = LatLngCalculate.getDistance(locat.latitude, locat.longitude,
                    lastPoint.latitude, lastPoint.longitude);
            if (restLength <= distance) {
                points.add(lastPoint);
            } else {
                for (int i = keyPoints.size() - 1; i >= 0; ) {
                    if (Math.abs(restLength - distance) > 200) {
                        i--;
                        lastPoint = keyPoints.get(i);
                        distance = LatLngCalculate.getDistance(locat.latitude, locat.longitude,
                                lastPoint.latitude, lastPoint.longitude);
                    } else {
                        ansIndex = i;
                        break;
                    }
                }
                for (int i = ansIndex; i < keyPoints.size(); i++) {
                    points.add(keyPoints.get(i));
                }
            }
//        } else {
//            int ansIndex = 0;
//            double distance = LatLngCalculate.getDistance(locat.latitude, locat.longitude,
//                    routeData.passingPoint.latitude, routeData.passingPoint.longitude);
//            if (restLength <= distance) {
//                points.add(routeData.passingPoint);
//                points.add(lastPoint);
//            } else {
//                int index = keyPoints.indexOf(routeData.passingPoint);
//                for (int i = index; i >= 0; ) {
//                    if (Math.abs(restLength - distance) > 200) {
//                        i--;
//                        lastPoint = keyPoints.get(i);
//                        distance = LatLngCalculate.getDistance(locat.latitude, locat.longitude,
//                                lastPoint.latitude, lastPoint.longitude);
//                    } else {
//                        ansIndex = i;
//                        break;
//                    }
//                }
//                for (int i = ansIndex; i < keyPoints.size(); i++) {
//                    points.add(keyPoints.get(i));
//                }
//            }
//        }

        routeData.keyPoints = points;
        return routeData;
    }

}
