package com.bupt.colorfulroute.runningapp.runnerlib;

import android.graphics.Color;
import android.util.Log;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import com.bupt.colorfulroute.runningapp.activity.RouteSelectActivity;
import com.bupt.colorfulroute.runningapp.uicomponent.AlertMessage;
import com.bupt.colorfulroute.runningapp.struct.RouteData;
import com.bupt.colorfulroute.runningapp.tools.LengthBalance;
import com.bupt.colorfulroute.runningapp.uicomponent.RouteSchematicDiagramLayout;

import java.util.ArrayList;


public class RouteGenerate {
    public static final int NORTH = 0;
    public static final int NORTHEAST = 1;
    public static final int EAST = 2;
    public static final int SOUTHEAST = 3;
    public static final int SOUTH = 4;
    public static final int SOUTHWEST = 5;
    public static final int WEST = 6;
    public static final int NORTHWEST = 7;

    /**
     * 根据距离计算出路径圆上关键点的坐标集合
     *
     * @param startPoint 起始点
     * @param distance   距离
     * @param orient     方向
     * @return RouteData类型
     */
    private static RouteData getCircleKeyPoints(LatLng startPoint, double distance, int orient) {
        ArrayList<LatLng> points = new ArrayList<>();
        double ini_distance = distance;
        //平衡路线规划增加的路径长度
        if (distance < 5000) {
            distance *= 0.6;
        } else if (distance < 7000) {
            distance *= 0.7;
        } else {
            distance *= 0.8;
        }

        //n,w,s,e,正角方向
        LatLng n, s, w, e, nw, ne, sw, se, c;
        double tempLat, tempLng, tempDis;
        switch (orient) {
            case NORTH:
                s = new LatLng(startPoint.latitude, startPoint.longitude);
                tempLat = s.latitude;
                tempLng = s.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(s.latitude, s.longitude, tempLat, tempLng);
                    if (tempDis > distance / Math.PI)
                        break;
                    else
                        tempLat += 0.000001;
                }
                n = new LatLng(tempLat, tempLng);
                c = new LatLng((s.latitude + n.latitude) / 2, (s.longitude + n.longitude) / 2);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLng -= 0.000001;
                }
                w = new LatLng(tempLat, tempLng);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLng += 0.000001;
                }
                e = new LatLng(tempLat, tempLng);
                break;
            case SOUTH:
                n = new LatLng(startPoint.latitude, startPoint.longitude);
                tempLat = n.latitude;
                tempLng = n.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(n.latitude, n.longitude, tempLat, tempLng);
                    if (tempDis > distance / Math.PI)
                        break;
                    else
                        tempLat -= 0.000001;
                }
                s = new LatLng(tempLat, tempLng);
                c = new LatLng((s.latitude + n.latitude) / 2, (s.longitude + n.longitude) / 2);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLng -= 0.000001;
                }
                w = new LatLng(tempLat, tempLng);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLng += 0.000001;
                }
                e = new LatLng(tempLat, tempLng);
                break;
            case WEST:
                e = new LatLng(startPoint.latitude, startPoint.longitude);
                tempLat = e.latitude;
                tempLng = e.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(e.latitude, e.longitude, tempLat, tempLng);
                    if (tempDis > distance / Math.PI)
                        break;
                    else
                        tempLng -= 0.000001;
                }
                w = new LatLng(tempLat, tempLng);
                c = new LatLng((e.latitude + w.latitude) / 2, (e.longitude + w.longitude) / 2);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLat += 0.000001;
                }
                n = new LatLng(tempLat, tempLng);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLat -= 0.000001;
                }
                s = new LatLng(tempLat, tempLng);
                break;
            case EAST:
                w = new LatLng(startPoint.latitude, startPoint.longitude);
                tempLat = w.latitude;
                tempLng = w.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(w.latitude, w.longitude, tempLat, tempLng);
                    if (tempDis > distance / Math.PI)
                        break;
                    else
                        tempLng += 0.000001;
                }
                e = new LatLng(tempLat, tempLng);
                c = new LatLng((e.latitude + w.latitude) / 2, (e.longitude + w.longitude) / 2);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLat += 0.000001;
                }
                n = new LatLng(tempLat, tempLng);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLat -= 0.000001;
                }
                s = new LatLng(tempLat, tempLng);
                break;
            default:
                return null;
        }

        //nw,ne,sw,se,偏角方向
        tempLat = c.latitude;
        tempLng = c.longitude;
        while (true) {
            tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
            if (tempDis > distance / (Math.PI * 2))
                break;
            else {
                tempLat += 0.000001;
                tempLng -= 0.000001;
            }
        }
        nw = new LatLng(tempLat, tempLng);
        tempLat = c.latitude;
        tempLng = c.longitude;
        while (true) {
            tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
            if (tempDis > distance / (Math.PI * 2))
                break;
            else {
                tempLat += 0.000001;
                tempLng += 0.000001;
            }
        }
        ne = new LatLng(tempLat, tempLng);
        tempLat = c.latitude;
        tempLng = c.longitude;
        while (true) {
            tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
            if (tempDis > distance / (Math.PI * 2))
                break;
            else {
                tempLat -= 0.000001;
                tempLng -= 0.000001;
            }
        }
        sw = new LatLng(tempLat, tempLng);
        tempLat = c.latitude;
        tempLng = c.longitude;
        while (true) {
            tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
            if (tempDis > distance / (Math.PI * 2))
                break;
            else {
                tempLat -= 0.000001;
                tempLng += 0.000001;
            }
        }
        se = new LatLng(tempLat, tempLng);
        switch (orient) {
            case NORTH:
                points.add(s);
                points.add(sw);
                points.add(w);
                points.add(nw);
                points.add(n);
                points.add(ne);
                points.add(e);
                points.add(se);
                points.add(s);
                break;
            case SOUTH:
                points.add(n);
                points.add(ne);
                points.add(e);
                points.add(se);
                points.add(s);
                points.add(sw);
                points.add(w);
                points.add(nw);
                points.add(n);
                break;
            case WEST:
                points.add(e);
                points.add(se);
                points.add(s);
                points.add(sw);
                points.add(w);
                points.add(nw);
                points.add(n);
                points.add(ne);
                points.add(e);
                break;
            case EAST:
                points.add(w);
                points.add(nw);
                points.add(n);
                points.add(ne);
                points.add(e);
                points.add(se);
                points.add(s);
                points.add(sw);
                points.add(w);
                break;
            default:
                return null;
        }

        RouteData routeData = new RouteData();
        routeData.centerPoint = c;
        routeData.keyPoints = points;
        routeData.length = ini_distance;
        return routeData;
    }

    private static RouteData getCircleKeyPointsByLocation(LatLng startPoint, double distance) {
        ArrayList<LatLng> points = new ArrayList<LatLng>();
        double ini_distance = distance;
        distance = LengthBalance.lengthBalance(distance);
        RouteData routeData = new RouteData();
//        double new_r = distance / (Math.PI * 2 + 2);//有返回半径
        double new_r = distance / (Math.PI * 2);//无返回半径
        double tempLat, tempLng;
        double tempDistance;
        LatLng n, ne, e, se, s, sw, w, nw;
        routeData.centerPoint = startPoint;
        tempLat = startPoint.latitude;
        tempLng = startPoint.longitude;
        while (true) {
            tempDistance = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude, startPoint.longitude);
            if (tempDistance > new_r)
                break;
            else
                tempLat += 0.000001;
        }
        n = new LatLng(tempLat, tempLng);
        tempLat = startPoint.latitude;
        tempLng = startPoint.longitude;
        while (true) {
            tempDistance = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude, startPoint.longitude);
            if (tempDistance > new_r)
                break;
            else
                tempLat -= 0.000001;
        }
        s = new LatLng(tempLat, tempLng);
        tempLat = startPoint.latitude;
        tempLng = startPoint.longitude;
        while (true) {
            tempDistance = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude, startPoint.longitude);
            if (tempDistance > new_r)
                break;
            else
                tempLng += 0.000001;
        }
        e = new LatLng(tempLat, tempLng);
        tempLat = startPoint.latitude;
        tempLng = startPoint.longitude;
        while (true) {
            tempDistance = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude, startPoint.longitude);
            if (tempDistance > new_r)
                break;
            else
                tempLng -= 0.000001;
        }
        w = new LatLng(tempLat, tempLng);
        tempLat = startPoint.latitude;
        tempLng = startPoint.longitude;
        while (true) {
            tempDistance = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude, startPoint.longitude);
            if (tempDistance > new_r)
                break;
            else {
                tempLat += 0.000001;
                tempLng += 0.000001;
            }
        }
        ne = new LatLng(tempLat, tempLng);
        tempLat = startPoint.latitude;
        tempLng = startPoint.longitude;
        while (true) {
            tempDistance = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude, startPoint.longitude);
            if (tempDistance > new_r)
                break;
            else {
                tempLat += 0.000001;
                tempLng -= 0.000001;
            }
        }
        nw = new LatLng(tempLat, tempLng);
        tempLat = startPoint.latitude;
        tempLng = startPoint.longitude;
        while (true) {
            tempDistance = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude, startPoint.longitude);
            if (tempDistance > new_r)
                break;
            else {
                tempLat -= 0.000001;
                tempLng += 0.000001;
            }
        }
        se = new LatLng(tempLat, tempLng);
        tempLat = startPoint.latitude;
        tempLng = startPoint.longitude;
        while (true) {
            tempDistance = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude, startPoint.longitude);
            if (tempDistance > new_r)
                break;
            else {
                tempLat -= 0.000001;
                tempLng -= 0.000001;
            }
        }
        sw = new LatLng(tempLat, tempLng);
        points.add(startPoint);
        points.add(n);
        points.add(ne);
        points.add(e);
        points.add(se);
        points.add(s);
        points.add(sw);
        points.add(w);
        points.add(nw);
        points.add(n);
        points.add(startPoint);
        routeData.keyPoints = points;
        routeData.length = ini_distance;

        return routeData;
    }

    /**
     * 根据距离计算出路径圆上关键点的坐标集合
     *
     * @param startPoint 起始点
     * @param distance   距离
     * @param orient     方向
     * @return RouteData类型
     */
    private static RouteData getRectangleKeyPoints(LatLng startPoint, double distance, int orient) {
        ArrayList<LatLng> points = new ArrayList<>();
        double ini_distance = distance;
        //平衡路线规划增加的路径长度
        if (distance < 5000) {
            distance *= 0.6;
        } else if (distance < 7000) {
            distance *= 0.7;
        } else {
            distance *= 0.8;
        }

        //n,w,s,e,正角方向
        LatLng n, s, w, e, nw, ne, sw, se, c;
        double tempLat, tempLng, tempDis;
        switch (orient) {
            case NORTH:
                s = new LatLng(startPoint.latitude, startPoint.longitude);
                tempLat = s.latitude;
                tempLng = s.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(s.latitude, s.longitude, tempLat, tempLng);
                    if (tempDis > distance / Math.PI)
                        break;
                    else
                        tempLat += 0.000001;
                }
                n = new LatLng(tempLat, tempLng);
                c = new LatLng((s.latitude + n.latitude) / 2, (s.longitude + n.longitude) / 2);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLng -= 0.000001;
                }
                w = new LatLng(tempLat, tempLng);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLng += 0.000001;
                }
                e = new LatLng(tempLat, tempLng);
                break;
            case SOUTH:
                n = new LatLng(startPoint.latitude, startPoint.longitude);
                tempLat = n.latitude;
                tempLng = n.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(n.latitude, n.longitude, tempLat, tempLng);
                    if (tempDis > distance / Math.PI)
                        break;
                    else
                        tempLat -= 0.000001;
                }
                s = new LatLng(tempLat, tempLng);
                c = new LatLng((s.latitude + n.latitude) / 2, (s.longitude + n.longitude) / 2);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLng -= 0.000001;
                }
                w = new LatLng(tempLat, tempLng);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLng += 0.000001;
                }
                e = new LatLng(tempLat, tempLng);
                break;
            case WEST:
                e = new LatLng(startPoint.latitude, startPoint.longitude);
                tempLat = e.latitude;
                tempLng = e.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(e.latitude, e.longitude, tempLat, tempLng);
                    if (tempDis > distance / Math.PI)
                        break;
                    else
                        tempLng -= 0.000001;
                }
                w = new LatLng(tempLat, tempLng);
                c = new LatLng((e.latitude + w.latitude) / 2, (e.longitude + w.longitude) / 2);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLat += 0.000001;
                }
                n = new LatLng(tempLat, tempLng);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLat -= 0.000001;
                }
                s = new LatLng(tempLat, tempLng);
                break;
            case EAST:
                w = new LatLng(startPoint.latitude, startPoint.longitude);
                tempLat = w.latitude;
                tempLng = w.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(w.latitude, w.longitude, tempLat, tempLng);
                    if (tempDis > distance / Math.PI)
                        break;
                    else
                        tempLng += 0.000001;
                }
                e = new LatLng(tempLat, tempLng);
                c = new LatLng((e.latitude + w.latitude) / 2, (e.longitude + w.longitude) / 2);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLat += 0.000001;
                }
                n = new LatLng(tempLat, tempLng);
                tempLat = c.latitude;
                tempLng = c.longitude;
                while (true) {
                    tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
                    if (tempDis > distance / (Math.PI * 2))
                        break;
                    else
                        tempLat -= 0.000001;
                }
                s = new LatLng(tempLat, tempLng);
                break;
            default:
                return null;
        }

        //nw,ne,sw,se,偏角方向
        tempLat = c.latitude;
        tempLng = c.longitude;
        while (true) {
            tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
            if (tempDis > distance / (Math.PI * 2))
                break;
            else {
                tempLat += 0.000001;
                tempLng -= 0.000001;
            }
        }
        nw = new LatLng(tempLat, tempLng);
        tempLat = c.latitude;
        tempLng = c.longitude;
        while (true) {
            tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
            if (tempDis > distance / (Math.PI * 2))
                break;
            else {
                tempLat += 0.000001;
                tempLng += 0.000001;
            }
        }
        ne = new LatLng(tempLat, tempLng);
        tempLat = c.latitude;
        tempLng = c.longitude;
        while (true) {
            tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
            if (tempDis > distance / (Math.PI * 2))
                break;
            else {
                tempLat -= 0.000001;
                tempLng -= 0.000001;
            }
        }
        sw = new LatLng(tempLat, tempLng);
        tempLat = c.latitude;
        tempLng = c.longitude;
        while (true) {
            tempDis = LatLngCalculate.getDistance(c.latitude, c.longitude, tempLat, tempLng);
            if (tempDis > distance / (Math.PI * 2))
                break;
            else {
                tempLat -= 0.000001;
                tempLng += 0.000001;
            }
        }
        se = new LatLng(tempLat, tempLng);
        switch (orient) {
            case NORTH:
                points.add(s);
//                points.add(sw);
                points.add(w);
//                points.add(nw);
                points.add(n);
//                points.add(ne);
                points.add(e);
//                points.add(se);
                points.add(s);
//                points.add(sw);
                break;
            case SOUTH:
                points.add(n);
//                points.add(ne);
                points.add(e);
//                points.add(se);
                points.add(s);
//                points.add(sw);
                points.add(w);
//                points.add(nw);
                points.add(n);
//                points.add(ne);
                break;
            case WEST:
                points.add(e);
//                points.add(se);
                points.add(s);
//                points.add(sw);
                points.add(w);
//                points.add(nw);
                points.add(n);
//                points.add(ne);
                points.add(e);
//                points.add(se);
                break;
            case EAST:
                points.add(w);
//                points.add(nw);
                points.add(n);
//                points.add(ne);
                points.add(e);
//                points.add(se);
                points.add(s);
//                points.add(sw);
                points.add(w);
//                points.add(nw);
                break;
            default:
                return null;
        }

        RouteData routeData = new RouteData();
        routeData.centerPoint = c;
        routeData.keyPoints = points;
        routeData.length = ini_distance;
        return routeData;
    }


    //三方叶花形
    private static RouteData getFlowerPoints_1(LatLng startPoint, double distance, int orient) {
        ArrayList<LatLng> points = new ArrayList<>();//最终关键点集
        double corelength = distance / 4;//花蕊路径长度
        double petallength = (distance - corelength) / 3;//花瓣路径长度
        LatLng[] latLngs = new LatLng[3];//花瓣的起始位置
        RouteData routeData_core = null;
        RouteData[] routeDatas = new RouteData[3];//花瓣的关键点
        LatLng c = null;
        switch (orient) {
            case NORTH:
                routeData_core = getCircleKeyPoints(startPoint, corelength, orient);//花蕊关键点集
                c = routeData_core.centerPoint;
                //花瓣的起始位置
                latLngs[0] = routeData_core.keyPoints.get(0);
                latLngs[1] = routeData_core.keyPoints.get(3);
                latLngs[2] = routeData_core.keyPoints.get(5);
                routeDatas[0] = getRectangleKeyPoints(latLngs[0], petallength, SOUTH);
                routeDatas[1] = getRectangleKeyPoints(latLngs[1], petallength, WEST);
                routeDatas[2] = getRectangleKeyPoints(latLngs[2], petallength, EAST);
                //保存关键点
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[0].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(1));
                points.add(routeData_core.keyPoints.get(2));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[1].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(4));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[2].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(6));
                points.add(routeData_core.keyPoints.get(7));
                points.add(routeData_core.keyPoints.get(0));
                break;
            case SOUTH:
                routeData_core = getCircleKeyPoints(startPoint, corelength, orient);//花蕊关键点集
                c = routeData_core.centerPoint;
                //花瓣的起始位置
                latLngs[0] = routeData_core.keyPoints.get(0);
                latLngs[1] = routeData_core.keyPoints.get(3);
                latLngs[2] = routeData_core.keyPoints.get(5);
                routeDatas[0] = getRectangleKeyPoints(latLngs[0], petallength, NORTH);
                routeDatas[1] = getRectangleKeyPoints(latLngs[1], petallength, EAST);
                routeDatas[2] = getRectangleKeyPoints(latLngs[2], petallength, WEST);
                //保存关键点
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[0].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(1));
                points.add(routeData_core.keyPoints.get(2));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[1].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(4));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[2].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(6));
                points.add(routeData_core.keyPoints.get(7));
                points.add(routeData_core.keyPoints.get(0));
                break;
            case WEST:
                routeData_core = getCircleKeyPoints(startPoint, corelength, orient);//花蕊关键点集
                c = routeData_core.centerPoint;
                //花瓣的起始位置
                latLngs[0] = routeData_core.keyPoints.get(0);
                latLngs[1] = routeData_core.keyPoints.get(3);
                latLngs[2] = routeData_core.keyPoints.get(5);
                routeDatas[0] = getRectangleKeyPoints(latLngs[0], petallength, EAST);
                routeDatas[1] = getRectangleKeyPoints(latLngs[1], petallength, SOUTH);
                routeDatas[2] = getRectangleKeyPoints(latLngs[2], petallength, NORTH);
                //保存关键点
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[0].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(1));
                points.add(routeData_core.keyPoints.get(2));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[1].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(4));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[2].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(6));
                points.add(routeData_core.keyPoints.get(7));
                points.add(routeData_core.keyPoints.get(0));
                break;
            case EAST:
                routeData_core = getCircleKeyPoints(startPoint, corelength, orient);//花蕊关键点集
                c = routeData_core.centerPoint;
                //花瓣的起始位置
                latLngs[0] = routeData_core.keyPoints.get(0);
                latLngs[1] = routeData_core.keyPoints.get(3);
                latLngs[2] = routeData_core.keyPoints.get(5);
                routeDatas[0] = getRectangleKeyPoints(latLngs[0], petallength, WEST);
                routeDatas[1] = getRectangleKeyPoints(latLngs[1], petallength, NORTH);
                routeDatas[2] = getRectangleKeyPoints(latLngs[2], petallength, SOUTH);
                //保存关键点
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[0].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(1));
                points.add(routeData_core.keyPoints.get(2));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[1].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(4));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[2].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(6));
                points.add(routeData_core.keyPoints.get(7));
                points.add(routeData_core.keyPoints.get(0));
                break;
        }
        RouteData routeData_final = new RouteData();
        routeData_final.centerPoint = c;
        routeData_final.keyPoints = points;
        routeData_final.length = routeData_core.length
                + routeDatas[0].length
                + routeDatas[1].length
                + routeDatas[2].length;
        return routeData_final;
    }

    //双层四半圆叶花形
    private static RouteData getFlowerPoints_2(LatLng startPoint, double distance, int orient) {
        ArrayList<LatLng> points = new ArrayList<>();//最终关键点集
        double length_in = distance / 6;//内花瓣路径长度
        double length_out = distance - length_in;//外花瓣路径长度
        RouteData routeData_in = null, routeData_out = null;
        LatLng c = null;
        switch (orient) {
            case NORTH:
                routeData_out = getFlowerPoints_4(startPoint, length_out, orient);
                c = routeData_out.centerPoint;
                routeData_in = getCircleKeyPointsByLocation(c, length_in);
                for (int i = 0; i < routeData_out.keyPoints.size(); i++) {
                    points.add(routeData_out.keyPoints.get(i));
                }
                points.add(routeData_in.keyPoints.get(5));
                points.add(routeData_in.keyPoints.get(6));
                points.add(routeData_in.keyPoints.get(7));
                points.add(routeData_in.keyPoints.get(8));
                points.add(routeData_in.keyPoints.get(9));
                points.add(routeData_in.keyPoints.get(2));
                points.add(routeData_in.keyPoints.get(3));
                points.add(routeData_in.keyPoints.get(4));
                points.add(routeData_in.keyPoints.get(5));
                points.add(routeData_out.keyPoints.get(0));
                break;
            case SOUTH:
                routeData_out = getFlowerPoints_4(startPoint, length_out, orient);
                c = routeData_out.centerPoint;
                routeData_in = getCircleKeyPointsByLocation(c, length_in);
                for (int i = 0; i < routeData_out.keyPoints.size(); i++) {
                    points.add(routeData_out.keyPoints.get(i));
                }
                points.add(routeData_in.keyPoints.get(1));
                points.add(routeData_in.keyPoints.get(2));
                points.add(routeData_in.keyPoints.get(3));
                points.add(routeData_in.keyPoints.get(4));
                points.add(routeData_in.keyPoints.get(5));
                points.add(routeData_in.keyPoints.get(6));
                points.add(routeData_in.keyPoints.get(7));
                points.add(routeData_in.keyPoints.get(8));
                points.add(routeData_in.keyPoints.get(9));
                points.add(routeData_out.keyPoints.get(0));
                break;
            case WEST:
                routeData_out = getFlowerPoints_4(startPoint, length_out, orient);
                c = routeData_out.centerPoint;
                routeData_in = getCircleKeyPointsByLocation(c, length_in);
                for (int i = 0; i < routeData_out.keyPoints.size(); i++) {
                    points.add(routeData_out.keyPoints.get(i));
                }
                points.add(routeData_in.keyPoints.get(3));
                points.add(routeData_in.keyPoints.get(4));
                points.add(routeData_in.keyPoints.get(5));
                points.add(routeData_in.keyPoints.get(6));
                points.add(routeData_in.keyPoints.get(7));
                points.add(routeData_in.keyPoints.get(8));
                points.add(routeData_in.keyPoints.get(9));
                points.add(routeData_in.keyPoints.get(2));
                points.add(routeData_in.keyPoints.get(3));
                points.add(routeData_out.keyPoints.get(0));
                break;
            case EAST:
                routeData_out = getFlowerPoints_4(startPoint, length_out, orient);
                c = routeData_out.centerPoint;
                routeData_in = getCircleKeyPointsByLocation(c, length_in);
                for (int i = 0; i < routeData_out.keyPoints.size(); i++) {
                    points.add(routeData_out.keyPoints.get(i));
                }
                points.add(routeData_in.keyPoints.get(7));
                points.add(routeData_in.keyPoints.get(8));
                points.add(routeData_in.keyPoints.get(9));
                points.add(routeData_in.keyPoints.get(2));
                points.add(routeData_in.keyPoints.get(3));
                points.add(routeData_in.keyPoints.get(4));
                points.add(routeData_in.keyPoints.get(5));
                points.add(routeData_in.keyPoints.get(6));
                points.add(routeData_in.keyPoints.get(7));
                points.add(routeData_out.keyPoints.get(0));
                break;
        }
        RouteData routeData_final = new RouteData();
        routeData_final.centerPoint = c;
        routeData_final.keyPoints = points;
        routeData_final.length = routeData_in.length + routeData_out.length;
        return routeData_final;
    }

    //四方叶花形
    private static RouteData getFlowerPoints_3(LatLng startPoint, double distance, int orient) {
        ArrayList<LatLng> points = new ArrayList<>();//最终关键点集
        double corelength = distance / 3;//花蕊路径长度
        double petallength = (distance - corelength) / 4;//花瓣路径长度
        LatLng[] latLngs = new LatLng[4];//花瓣的起始位置
        RouteData routeData_core = null;
        RouteData[] routeDatas = new RouteData[4];//花瓣的关键点
        LatLng c = null;
        switch (orient) {
            case NORTH:
                routeData_core = getCircleKeyPoints(startPoint, corelength, orient);//花蕊关键点集
                c = routeData_core.centerPoint;
                //花瓣的起始位置
                latLngs[0] = routeData_core.keyPoints.get(1);
                latLngs[1] = routeData_core.keyPoints.get(3);
                latLngs[2] = routeData_core.keyPoints.get(5);
                latLngs[3] = routeData_core.keyPoints.get(7);
                routeDatas[0] = getRectangleKeyPoints(latLngs[0], petallength, WEST);
                routeDatas[1] = getRectangleKeyPoints(latLngs[1], petallength, WEST);
                routeDatas[2] = getRectangleKeyPoints(latLngs[2], petallength, EAST);
                routeDatas[3] = getRectangleKeyPoints(latLngs[3], petallength, EAST);
                //保存关键点
                points.add(routeData_core.keyPoints.get(0));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[0].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(2));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[1].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(4));
                for (int i = 0; i <= 4; i++)
                    points.add(routeData_core.keyPoints.get(6));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[3].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(0));
                break;
            case SOUTH:
                routeData_core = getCircleKeyPoints(startPoint, corelength, orient);//花蕊关键点集
                c = routeData_core.centerPoint;
                //花瓣的起始位置
                latLngs[0] = routeData_core.keyPoints.get(1);
                latLngs[1] = routeData_core.keyPoints.get(3);
                latLngs[2] = routeData_core.keyPoints.get(5);
                latLngs[3] = routeData_core.keyPoints.get(7);
                routeDatas[0] = getRectangleKeyPoints(latLngs[0], petallength, EAST);
                routeDatas[1] = getRectangleKeyPoints(latLngs[1], petallength, EAST);
                routeDatas[2] = getRectangleKeyPoints(latLngs[2], petallength, WEST);
                routeDatas[3] = getRectangleKeyPoints(latLngs[3], petallength, WEST);
                points.add(routeData_core.keyPoints.get(0));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[0].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(2));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[1].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(4));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[2].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(6));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[3].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(0));
                break;
            case WEST:
                routeData_core = getCircleKeyPoints(startPoint, corelength, orient);//花蕊关键点集
                c = routeData_core.centerPoint;
                //花瓣的起始位置
                latLngs[0] = routeData_core.keyPoints.get(1);
                latLngs[1] = routeData_core.keyPoints.get(3);
                latLngs[2] = routeData_core.keyPoints.get(5);
                latLngs[3] = routeData_core.keyPoints.get(7);
                routeDatas[0] = getRectangleKeyPoints(latLngs[0], petallength, SOUTH);
                routeDatas[1] = getRectangleKeyPoints(latLngs[1], petallength, SOUTH);
                routeDatas[2] = getRectangleKeyPoints(latLngs[2], petallength, NORTH);
                routeDatas[3] = getRectangleKeyPoints(latLngs[3], petallength, NORTH);
                points.add(routeData_core.keyPoints.get(0));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[0].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(2));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[1].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(4));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[2].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(6));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[3].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(0));
                break;
            case EAST:
                routeData_core = getCircleKeyPoints(startPoint, corelength, orient);//花蕊关键点集
                c = routeData_core.centerPoint;
                //花瓣的起始位置
                latLngs[0] = routeData_core.keyPoints.get(1);
                latLngs[1] = routeData_core.keyPoints.get(3);
                latLngs[2] = routeData_core.keyPoints.get(5);
                latLngs[3] = routeData_core.keyPoints.get(7);
                routeDatas[0] = getRectangleKeyPoints(latLngs[0], petallength, NORTH);
                routeDatas[1] = getRectangleKeyPoints(latLngs[1], petallength, NORTH);
                routeDatas[2] = getRectangleKeyPoints(latLngs[2], petallength, SOUTH);
                routeDatas[3] = getRectangleKeyPoints(latLngs[3], petallength, SOUTH);
                points.add(routeData_core.keyPoints.get(0));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[0].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(2));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[1].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(4));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[2].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(6));
                for (int i = 0; i <= 4; i++) {
                    points.add(routeDatas[3].keyPoints.get(i));
                }
                points.add(routeData_core.keyPoints.get(0));
                break;
        }
        RouteData routeData_final = new RouteData();
        routeData_final.centerPoint = c;
        routeData_final.keyPoints = points;
        routeData_final.length = routeData_core.length
                + routeDatas[0].length
                + routeDatas[1].length
                + routeDatas[2].length
                + routeDatas[3].length;
        return routeData_final;
    }

    //四半圆叶花形
    private static RouteData getFlowerPoints_4(LatLng startPoint, double distance, int orient) {
        ArrayList<LatLng> points = new ArrayList<>();//最终关键点集
        double length = distance / 4;//花瓣路径长度
        RouteData[] routeDatas = new RouteData[4];//花瓣的关键点
        LatLng c = null;
        switch (orient) {
            case NORTH:
                routeDatas[0] = getCircleKeyPoints(startPoint, length, orient);//起点花瓣
                c = routeDatas[0].keyPoints.get(4);
                routeDatas[1] = getCircleKeyPoints(c, length, EAST);
                routeDatas[2] = getCircleKeyPoints(c, length, NORTH);
                routeDatas[3] = getCircleKeyPoints(c, length, WEST);
                //花瓣1
                points.add(routeDatas[0].keyPoints.get(0));
                points.add(routeDatas[0].keyPoints.get(7));
                points.add(routeDatas[0].keyPoints.get(6));
                //花瓣2
                points.add(routeDatas[1].keyPoints.get(5));
                points.add(routeDatas[1].keyPoints.get(4));
                points.add(routeDatas[1].keyPoints.get(3));
                points.add(routeDatas[1].keyPoints.get(2));
                //花瓣3
                points.add(routeDatas[2].keyPoints.get(5));
                points.add(routeDatas[2].keyPoints.get(4));
                points.add(routeDatas[2].keyPoints.get(3));
                points.add(routeDatas[2].keyPoints.get(2));
                //花瓣4
                points.add(routeDatas[3].keyPoints.get(5));
                points.add(routeDatas[3].keyPoints.get(4));
                points.add(routeDatas[3].keyPoints.get(3));
                points.add(routeDatas[3].keyPoints.get(2));
                //花瓣1
                points.add(routeDatas[0].keyPoints.get(1));
                points.add(routeDatas[0].keyPoints.get(0));
                break;
            case SOUTH:
                routeDatas[0] = getCircleKeyPoints(startPoint, length, orient);//起点花瓣
                c = routeDatas[0].keyPoints.get(4);
                routeDatas[1] = getCircleKeyPoints(c, length, WEST);
                routeDatas[2] = getCircleKeyPoints(c, length, SOUTH);
                routeDatas[3] = getCircleKeyPoints(c, length, EAST);
                //花瓣1
                points.add(routeDatas[0].keyPoints.get(0));
                points.add(routeDatas[0].keyPoints.get(7));
                points.add(routeDatas[0].keyPoints.get(6));
                //花瓣2
//                points.add(routeDatas[1].keyPoints.get(2));
                points.add(routeDatas[1].keyPoints.get(5));
                points.add(routeDatas[1].keyPoints.get(4));
                points.add(routeDatas[1].keyPoints.get(3));
                points.add(routeDatas[1].keyPoints.get(2));
                //花瓣3
//                points.add(routeDatas[2].keyPoints.get(2));
                points.add(routeDatas[2].keyPoints.get(5));
                points.add(routeDatas[2].keyPoints.get(4));
                points.add(routeDatas[2].keyPoints.get(3));
                points.add(routeDatas[2].keyPoints.get(2));
                //花瓣4
//                points.add(routeDatas[3].keyPoints.get(2));
                points.add(routeDatas[3].keyPoints.get(5));
                points.add(routeDatas[3].keyPoints.get(4));
                points.add(routeDatas[3].keyPoints.get(3));
                points.add(routeDatas[3].keyPoints.get(2));
                //花瓣1
//                points.add(routeDatas[0].keyPoints.get(6));
                points.add(routeDatas[0].keyPoints.get(1));
                points.add(routeDatas[0].keyPoints.get(0));
                break;
            case WEST:
                routeDatas[0] = getCircleKeyPoints(startPoint, length, orient);//起点花瓣
                c = routeDatas[0].keyPoints.get(4);
                routeDatas[1] = getCircleKeyPoints(c, length, NORTH);
                routeDatas[2] = getCircleKeyPoints(c, length, WEST);
                routeDatas[3] = getCircleKeyPoints(c, length, SOUTH);
                //花瓣1
                points.add(routeDatas[0].keyPoints.get(0));
                points.add(routeDatas[0].keyPoints.get(7));
                points.add(routeDatas[0].keyPoints.get(6));
                //花瓣2
//                points.add(routeDatas[1].keyPoints.get(2));
                points.add(routeDatas[1].keyPoints.get(5));
                points.add(routeDatas[1].keyPoints.get(4));
                points.add(routeDatas[1].keyPoints.get(3));
                points.add(routeDatas[1].keyPoints.get(2));
                //花瓣3
//                points.add(routeDatas[2].keyPoints.get(2));
                points.add(routeDatas[2].keyPoints.get(5));
                points.add(routeDatas[2].keyPoints.get(4));
                points.add(routeDatas[2].keyPoints.get(3));
                points.add(routeDatas[2].keyPoints.get(2));
                //花瓣4
//                points.add(routeDatas[3].keyPoints.get(2));
                points.add(routeDatas[3].keyPoints.get(5));
                points.add(routeDatas[3].keyPoints.get(4));
                points.add(routeDatas[3].keyPoints.get(3));
                points.add(routeDatas[3].keyPoints.get(2));
                //花瓣1
//                points.add(routeDatas[0].keyPoints.get(6));
                points.add(routeDatas[0].keyPoints.get(1));
                points.add(routeDatas[0].keyPoints.get(0));
                break;
            case EAST:
                routeDatas[0] = getCircleKeyPoints(startPoint, length, orient);//起点花瓣
                c = routeDatas[0].keyPoints.get(4);
                routeDatas[1] = getCircleKeyPoints(c, length, SOUTH);
                routeDatas[2] = getCircleKeyPoints(c, length, EAST);
                routeDatas[3] = getCircleKeyPoints(c, length, NORTH);
                //花瓣1
                points.add(routeDatas[0].keyPoints.get(0));
                points.add(routeDatas[0].keyPoints.get(7));
                points.add(routeDatas[0].keyPoints.get(6));
                //花瓣2
//                points.add(routeDatas[1].keyPoints.get(2));
                points.add(routeDatas[1].keyPoints.get(5));
                points.add(routeDatas[1].keyPoints.get(4));
                points.add(routeDatas[1].keyPoints.get(3));
                points.add(routeDatas[1].keyPoints.get(2));
                //花瓣3
//                points.add(routeDatas[2].keyPoints.get(2));
                points.add(routeDatas[2].keyPoints.get(5));
                points.add(routeDatas[2].keyPoints.get(4));
                points.add(routeDatas[2].keyPoints.get(3));
                points.add(routeDatas[2].keyPoints.get(2));
                //花瓣4
//                points.add(routeDatas[3].keyPoints.get(2));
                points.add(routeDatas[3].keyPoints.get(5));
                points.add(routeDatas[3].keyPoints.get(4));
                points.add(routeDatas[3].keyPoints.get(3));
                points.add(routeDatas[3].keyPoints.get(2));
                //花瓣1
//                points.add(routeDatas[0].keyPoints.get(6));
                points.add(routeDatas[0].keyPoints.get(1));
                points.add(routeDatas[0].keyPoints.get(0));
                break;
        }
        RouteData routeData_final = new RouteData();
        routeData_final.centerPoint = c;
        routeData_final.keyPoints = points;
        routeData_final.length = routeDatas[0].length
                + routeDatas[1].length
                + routeDatas[2].length
                + routeDatas[3].length;
        return routeData_final;
    }

    //五弧叶花形 TODO

    /**
     * 以当前点为圆心，计算出周边圆的跑步路径
     *
     * @param startPoint
     * @param passingPoint
     * @param distance
     * @param orient
     * @return
     */
    private static RouteData getCircleKeyPointsWithPassingPoint(LatLng startPoint,
                                                                LatLng passingPoint,
                                                                double distance,
                                                                int orient) {
        ArrayList<LatLng> points = new ArrayList<>();
        double ini_distance = distance;
        //平衡路线规划增加的路径长度
        distance = LengthBalance.lengthBalance(distance);

        RouteData routeData = new RouteData();
        //n,w,s,e
        LatLng n, s, w, e, nw, ne, sw, se, c = null;
        double tempLat, tempLng, tempDis, ac, bc;
        double r = distance / (Math.PI * 2);
//        double seta, halfChord, chordDistance;
//        double k = 0.0;
//        double centerX1, centerX2, centerY1, centerY2;
        double comLat, comLng, latLength, lngLength;
        boolean horizontal = false;
        comLng = passingPoint.longitude;
        comLat = startPoint.latitude;
        lngLength = LatLngCalculate.getDistance(comLat, comLng, passingPoint.latitude,
                passingPoint.longitude);
        latLength = LatLngCalculate.getDistance(comLat, comLng, startPoint.latitude,
                startPoint.longitude);

        if (latLength > lngLength) {
            horizontal = true;
        }
        tempLat = startPoint.latitude;
        tempLng = startPoint.longitude;
        if (horizontal) {
            while (true) {
                ac = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude,
                        startPoint.longitude);
                bc = LatLngCalculate.getDistance(tempLat, tempLng, passingPoint.latitude,
                        passingPoint.longitude);
                if (Math.abs(ac - bc) <= 0) {
                    break;
                } else {
                    if (startPoint.longitude < passingPoint.longitude)
                        tempLng += 0.000001;
                    else
                        tempLng -= 0.000001;
                }

            }
            switch (orient) {
                case NORTH:
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude,
                                startPoint.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLat += 0.000001;
                    }
                    c = new LatLng(tempLat, tempLng);
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLat += 0.000001;
                    }
                    n = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLat -= 0.000001;
                    }
                    s = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLng += 0.000001;
                    }
                    e = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLng -= 0.000001;
                    }
                    w = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat += 0.000001;
                            tempLng += 0.000001;
                        }
                    }
                    ne = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat += 0.000001;
                            tempLng -= 0.000001;
                        }
                    }
                    nw = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat -= 0.000001;
                            tempLng += 0.000001;
                        }
                    }
                    se = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat -= 0.000001;
                            tempLng -= 0.000001;
                        }
                    }
                    sw = new LatLng(tempLat, tempLng);
                    if (startPoint.longitude < passingPoint.longitude) {
                        points.add(startPoint);
                        if (startPoint.longitude < sw.longitude)
                            points.add(sw);
                        points.add(s);
                        if (passingPoint.longitude > se.longitude)
                            points.add(se);
                        points.add(passingPoint);
                        if (passingPoint.longitude < se.longitude)
                            points.add(se);
                        points.add(se);
                        points.add(e);
                        points.add(ne);
                        points.add(n);
                        points.add(nw);
                        points.add(w);
                        if (startPoint.longitude > sw.longitude)
                            points.add(sw);
                        points.add(startPoint);
                    } else {
                        points.add(startPoint);
                        if (startPoint.longitude < se.longitude)
                            points.add(se);
                        points.add(e);
                        points.add(ne);
                        points.add(n);
                        points.add(nw);
                        points.add(w);
                        if (passingPoint.longitude > sw.longitude)
                            points.add(sw);
                        points.add(passingPoint);
                        if (passingPoint.longitude < sw.longitude)
                            points.add(sw);
                        points.add(s);
                        if (startPoint.longitude > se.longitude)
                            points.add(se);
                        points.add(startPoint);
                    }
                    routeData.centerPoint = c;
                    break;
                case SOUTH:
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude,
                                startPoint.longitude);
                        if (tempDis > distance / (Math.PI * 2))
                            break;
                        else
                            tempLat -= 0.000001;
                    }
                    c = new LatLng(tempLat, tempLng);
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLat += 0.000001;
                    }
                    n = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLat -= 0.000001;
                    }
                    s = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLng += 0.000001;
                    }
                    e = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLng -= 0.000001;
                    }
                    w = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat += 0.000001;
                            tempLng += 0.000001;
                        }
                    }
                    ne = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat += 0.000001;
                            tempLng -= 0.000001;
                        }
                    }
                    nw = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat -= 0.000001;
                            tempLng += 0.000001;
                        }
                    }
                    se = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat -= 0.000001;
                            tempLng -= 0.000001;
                        }
                    }
                    sw = new LatLng(tempLat, tempLng);
                    if (startPoint.longitude < passingPoint.longitude) {
                        points.add(startPoint);
                        if (startPoint.longitude < nw.longitude)
                            points.add(nw);
                        points.add(n);
                        if (passingPoint.longitude > ne.longitude)
                            points.add(ne);
                        points.add(passingPoint);
                        if (passingPoint.longitude < ne.longitude)
                            points.add(ne);
                        points.add(e);
                        points.add(se);
                        points.add(s);
                        points.add(sw);
                        points.add(w);
                        if (startPoint.longitude > nw.longitude)
                            points.add(nw);
                        points.add(startPoint);
                    } else {
                        points.add(startPoint);
                        if (startPoint.longitude < ne.longitude)
                            points.add(ne);
                        points.add(e);
                        points.add(se);
                        points.add(s);
                        points.add(sw);
                        points.add(w);
                        if (passingPoint.longitude > nw.longitude)
                            points.add(nw);
                        points.add(passingPoint);
                        if (passingPoint.longitude < nw.longitude)
                            points.add(nw);
                        points.add(n);
                        if (startPoint.longitude > ne.longitude)
                            points.add(ne);
                        points.add(startPoint);
                    }
                    routeData.centerPoint = c;
                    break;
                default:
                    return null;
            }
        } else {
            while (true) {
                ac = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude,
                        startPoint.longitude);
                bc = LatLngCalculate.getDistance(tempLat, tempLng, passingPoint.latitude,
                        passingPoint.longitude);
                if (Math.abs(ac - bc) <= 50) {
                    break;
                } else {
                    if (startPoint.latitude < passingPoint.latitude)
                        tempLat += 0.000001;
                    else
                        tempLat -= 0.000001;
                }
            }
            switch (orient) {
                case NORTH:
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude,
                                startPoint.longitude);
                        if (tempDis > distance / (Math.PI * 2))
                            break;
                        else
                            tempLng -= 0.000001;
                    }
                    c = new LatLng(tempLat, tempLng);
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLat += 0.000001;
                    }
                    n = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLat -= 0.000001;
                    }
                    s = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLng += 0.000001;
                    }
                    e = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLng -= 0.000001;
                    }
                    w = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat += 0.000001;
                            tempLng += 0.000001;
                        }
                    }
                    ne = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat += 0.000001;
                            tempLng -= 0.000001;
                        }
                    }
                    nw = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat -= 0.000001;
                            tempLng += 0.000001;
                        }
                    }
                    se = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat -= 0.000001;
                            tempLng -= 0.000001;
                        }
                    }
                    sw = new LatLng(tempLat, tempLng);
                    if (startPoint.latitude < passingPoint.latitude) {
                        points.add(startPoint);
                        if (startPoint.longitude < se.longitude)
                            points.add(se);
                        points.add(e);
                        if (passingPoint.longitude < ne.longitude)
                            points.add(ne);
                        points.add(passingPoint);
                        if (passingPoint.longitude > ne.longitude)
                            points.add(ne);
                        points.add(n);
                        points.add(nw);
                        points.add(w);
                        points.add(sw);
                        points.add(s);
                        if (startPoint.longitude > se.longitude)
                            points.add(se);
                        points.add(startPoint);
                    } else {
                        points.add(startPoint);
                        if (startPoint.longitude > ne.longitude)
                            points.add(ne);
                        points.add(n);
                        points.add(nw);
                        points.add(w);
                        points.add(sw);
                        points.add(s);
                        if (passingPoint.longitude > se.longitude)
                            points.add(se);
                        points.add(passingPoint);
                        if (passingPoint.longitude < se.longitude)
                            points.add(se);
                        points.add(e);
                        if (startPoint.longitude < ne.longitude)
                            points.add(ne);
                        points.add(startPoint);
                    }
                    routeData.centerPoint = c;
                    break;
                case SOUTH:
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, startPoint.latitude,
                                startPoint.longitude);
                        if (tempDis > distance / (Math.PI * 2))
                            break;
                        else
                            tempLng += 0.000001;
                    }
                    c = new LatLng(tempLat, tempLng);
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLat += 0.000001;
                    }
                    n = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLat -= 0.000001;
                    }
                    s = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLng += 0.000001;
                    }
                    e = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else
                            tempLng -= 0.000001;
                    }
                    w = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat += 0.000001;
                            tempLng += 0.000001;
                        }
                    }
                    ne = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat += 0.000001;
                            tempLng -= 0.000001;
                        }
                    }
                    nw = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat -= 0.000001;
                            tempLng += 0.000001;
                        }
                    }
                    se = new LatLng(tempLat, tempLng);
                    tempLat = c.latitude;
                    tempLng = c.longitude;
                    while (true) {
                        tempDis = LatLngCalculate.getDistance(tempLat, tempLng, c.latitude,
                                c.longitude);
                        if (tempDis > r)
                            break;
                        else {
                            tempLat -= 0.000001;
                            tempLng -= 0.000001;
                        }
                    }
                    sw = new LatLng(tempLat, tempLng);
                    if (startPoint.latitude > passingPoint.latitude) {
                        points.add(startPoint);
                        if (startPoint.longitude > nw.longitude)
                            points.add(nw);
                        points.add(w);
                        if (passingPoint.longitude > sw.longitude)
                            points.add(sw);
                        points.add(passingPoint);
                        if (passingPoint.longitude < sw.longitude)
                            points.add(sw);
                        points.add(s);
                        points.add(se);
                        points.add(e);
                        points.add(ne);
                        points.add(n);
                        if (startPoint.longitude < nw.longitude)
                            points.add(nw);
                        points.add(startPoint);
                    } else {
                        points.add(startPoint);
                        if (startPoint.longitude < sw.longitude)
                            points.add(sw);
                        points.add(s);
                        points.add(se);
                        points.add(e);
                        points.add(ne);
                        points.add(n);
                        if (passingPoint.longitude < nw.longitude)
                            points.add(nw);
                        points.add(passingPoint);
                        if (passingPoint.longitude > nw.longitude)
                            points.add(nw);
                        points.add(w);
                        if (startPoint.longitude > sw.longitude)
                            points.add(sw);
                        points.add(startPoint);
                    }
                    routeData.centerPoint = c;
                    break;
                default:
                    return null;
            }
        }
        routeData.keyPoints = points;
        routeData.length = ini_distance;
        return routeData;
    }

    /**
     * 花形路径规划方案
     *
     * @param startPoint 起点
     * @param distance   距离
     * @param orient     方向
     * @param activity   activity
     */
    public static void generateFlowerRoute(int kind,final LatLng startPoint, double distance, int orient,
                                           final RouteSelectActivity activity) {
//        final RouteData routeData = getFlowerPoints_3(startPoint, distance, orient);

        final RouteData routeData;
        switch (kind) {
            case 0:
                routeData = getCircleKeyPoints(startPoint, distance, orient);
                break;
            case 1:
                routeData = getFlowerPoints_1(startPoint, distance, orient);
                break;
            case 2:
                routeData = getFlowerPoints_2(startPoint, distance, orient);
                break;
            case 3:
                routeData = getFlowerPoints_3(startPoint, distance, orient);
                break;
            case 4:
                routeData = getFlowerPoints_4(startPoint, distance, orient);
                break;
            default:
                routeData = getCircleKeyPoints(startPoint, distance, orient);
                break;
        }

        //amap search_view_bg
        final RouteSearch routeSearch = new RouteSearch(activity);
        final ArrayList<RouteSearch.FromAndTo> fromAndToList = new ArrayList<>();
        ArrayList<RouteSearch.WalkRouteQuery> queryList = new ArrayList<>();

        for (int i = 0; i < routeData.keyPoints.size() - 1; i++) {
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                    new LatLonPoint(routeData.keyPoints.get(i).latitude, routeData.keyPoints.get(i).longitude),
                    new LatLonPoint(routeData.keyPoints.get(i + 1).latitude, routeData.keyPoints.get(i + 1).longitude)
            );
            fromAndToList.add(fromAndTo);
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(
                    new RouteSearch.FromAndTo(
                            new LatLonPoint(routeData.keyPoints.get(i).latitude, routeData.keyPoints.get(i).longitude),
                            new LatLonPoint(routeData.keyPoints.get(i + 1).latitude, routeData.keyPoints.get(i + 1).longitude)
                    )
            );
            queryList.add(query);
        }
        routeSearch.setRouteSearchListener(new RunnerRouteSearchListener(queryList.size()) {
            @Override
            public void successHandler(WalkRouteResult walkRouteResult) {
                int index;
                for (index = 0; index < fromAndToList.size(); index++) {
                    RouteSearch.FromAndTo fromAndTo1 = walkRouteResult.getWalkQuery().getFromAndTo();
                    RouteSearch.FromAndTo fromAndTo2 = fromAndToList.get(index);
                    if (fromAndTo1.getFrom().getLatitude() == fromAndTo2.getFrom().getLatitude()
                            && fromAndTo1.getFrom().getLongitude() == fromAndTo2.getFrom().getLongitude()) {
                        break;
                    }
                }
                resultList[index] = walkRouteResult;
                responseCounter++;
                if (responseCounter >= resultList.length) {
                    polyProcess();
                }
            }

            private void polyProcess() {
                //从所有结果中提取多边形顶点
                ArrayList<LatLng> polyResourcePoints = new ArrayList<>();
                for (WalkRouteResult result : this.resultList) {
                    if (result == null)
                        continue;
                    WalkPath path = result.getPaths().get(result.getPaths().size() - 1);
                    for (WalkStep step : path.getSteps()) {
                        for (LatLonPoint rawPoint : step.getPolyline()) {
                            polyResourcePoints.add(new LatLng(rawPoint.getLatitude(), rawPoint.getLongitude()));
                        }
                    }
                }
                //删除重复路径
                Boolean[] deleteFlag = new Boolean[polyResourcePoints.size()];
                for (int i = 0; i < deleteFlag.length; i++)
                    deleteFlag[i] = false;
                //路线剪枝
                ArrayList<LatLng> optimizedPoints = new ArrayList<>();
                for (int i = 0, j; i < polyResourcePoints.size() - 1; i++) {
                    for (j = i + 1; j < polyResourcePoints.size(); j++) {
                        if (LatLngCalculate.isSamePoint(polyResourcePoints.get(i), polyResourcePoints.get(j))
                                && (j - i) <= polyResourcePoints.size() / 3) {
                            //剪枝
                            for (int deleteIndex = i + 1; deleteIndex <= j; deleteIndex++) {
                                deleteFlag[deleteIndex] = true;
                            }
                        }
                    }
                }
                for (int i = 0; i < polyResourcePoints.size(); i++) {
                    if (!deleteFlag[i])
                        optimizedPoints.add(polyResourcePoints.get(i));
                }
                polyResourcePoints = optimizedPoints;
                if (!LatLngCalculate.isSamePoint(polyResourcePoints.get(0), polyResourcePoints.get(polyResourcePoints.size() - 1)))
                    polyResourcePoints.add(new LatLng(polyResourcePoints.get(0).latitude, polyResourcePoints.get(0).longitude));


                //add polyline to schematic diagram map view
                routeData.realLength = LatLngCalculate.getPathLength(polyResourcePoints);
                if (Math.abs(routeData.length - routeData.realLength) < 1500) {
                    routeData.ployPoints = polyResourcePoints;
                    RouteSchematicDiagramLayout diagram = new RouteSchematicDiagramLayout(activity, activity.getSavedInstanceState());
                    diagram.setRouteData(routeData);
                    AMap map = diagram.getTextureMapView().getMap();
                    diagram.getTextView().setText("" + routeData.realLength + "M");
                    diagram.getTextView().setTextColor(Color.RED);
                    diagram.getTextureMapView().onCreate(activity.getSavedInstanceState());
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.setPoints(polyResourcePoints);
                    polylineOptions.width(8);
                    polylineOptions.color(Color.RED);
                    polylineOptions.geodesic(true);
                    map.addPolyline(polylineOptions);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(startPoint);
                    markerOptions.draggable(false);
                    map.addMarker(markerOptions).setClickable(false);
                    map.moveCamera(CameraUpdateFactory.changeLatLng(routeData.centerPoint));
                    map.moveCamera(CameraUpdateFactory.zoomTo(13));
                    map.getUiSettings().setZoomControlsEnabled(false);
                    map.getUiSettings().setAllGesturesEnabled(false);
                    activity.addSchematicDiagram(diagram);
                }
            }

            @Override
            public void errorHandler() {
                activity.alert(new AlertMessage("路线规划出错,请重新选择长度！", ""));
                activity.finish();
            }
        });

        //asyn query
        for (RouteSearch.WalkRouteQuery query : queryList)
            routeSearch.calculateWalkRouteAsyn(query);
    }

    /**
     * 一般路径规划方案
     *
     * @param startPoint 起始点
     * @param distance   距离
     * @param orient     方向
     * @param activity   activity
     */
    public static void generateRoute(int kind, final LatLng startPoint, double distance, int orient,
                                     final RouteSelectActivity activity) {
//        final RouteData routeData = getCircleKeyPoints(startPoint, distance, orient);//圆形路径规划
        final RouteData routeData;
        switch (kind) {
            case 0:
                routeData = getCircleKeyPoints(startPoint, distance, orient);
                break;
            case 1:
                routeData = getFlowerPoints_1(startPoint, distance, orient);
                break;
            case 2:
                routeData = getFlowerPoints_2(startPoint, distance, orient);
                break;
            case 3:
                routeData = getFlowerPoints_3(startPoint, distance, orient);
                break;
            case 4:
                routeData = getFlowerPoints_4(startPoint, distance, orient);
                break;
            default:
                routeData = getCircleKeyPoints(startPoint, distance, orient);
                break;
        }


        //amap search_view_bg
        final RouteSearch routeSearch = new RouteSearch(activity);
        final ArrayList<RouteSearch.FromAndTo> fromAndToList = new ArrayList<>();
        ArrayList<RouteSearch.WalkRouteQuery> queryList = new ArrayList<>();

        for (int i = 0; i < routeData.keyPoints.size() - 1; i++) {
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                    new LatLonPoint(routeData.keyPoints.get(i).latitude, routeData.keyPoints.get(i).longitude),
                    new LatLonPoint(routeData.keyPoints.get(i + 1).latitude, routeData.keyPoints.get(i + 1).longitude)
            );
            fromAndToList.add(fromAndTo);
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(
                    new RouteSearch.FromAndTo(
                            new LatLonPoint(routeData.keyPoints.get(i).latitude, routeData.keyPoints.get(i).longitude),
                            new LatLonPoint(routeData.keyPoints.get(i + 1).latitude, routeData.keyPoints.get(i + 1).longitude)
                    )
            );
            queryList.add(query);
        }

        //query listener
        routeSearch.setRouteSearchListener(new RunnerRouteSearchListener(queryList.size()) {
            @Override
            public void successHandler(WalkRouteResult walkRouteResult) {
                int index;
                for (index = 0; index < fromAndToList.size(); index++) {
                    RouteSearch.FromAndTo fromAndTo1 = walkRouteResult.getWalkQuery().getFromAndTo();
                    RouteSearch.FromAndTo fromAndTo2 = fromAndToList.get(index);
                    if (fromAndTo1.getFrom().getLatitude() == fromAndTo2.getFrom().getLatitude()
                            && fromAndTo1.getFrom().getLongitude() == fromAndTo2.getFrom().getLongitude()) {
                        break;
                    }
                }
                resultList[index] = walkRouteResult;
                responseCounter++;
                if (responseCounter >= resultList.length) {
                    polyProcess();
                }
            }

            @Override
            public void errorHandler() {
                activity.alert(new AlertMessage("路线规划出错,请重新选择长度！", ""));
                activity.finish();
            }

            private void polyProcess() {
                //从所有结果中提取多边形顶点
                ArrayList<LatLng> polyResourcePoints = new ArrayList<>();
                for (WalkRouteResult result : this.resultList) {
                    if (result == null)
                        continue;
                    WalkPath path = result.getPaths().get(result.getPaths().size() - 1);
                    for (WalkStep step : path.getSteps()) {
                        for (LatLonPoint rawPoint : step.getPolyline()) {
                            polyResourcePoints.add(new LatLng(rawPoint.getLatitude(), rawPoint.getLongitude()));
                        }
                    }
                }
                //删除重复路径
                Boolean[] deleteFlag = new Boolean[polyResourcePoints.size()];
                for (int i = 0; i < deleteFlag.length; i++)
                    deleteFlag[i] = false;
                //路线剪枝
                ArrayList<LatLng> optimizedPoints = new ArrayList<>();
                for (int i = 0, j; i < polyResourcePoints.size() - 1; i++) {
                    for (j = i + 1; j < polyResourcePoints.size(); j++) {
                        if (LatLngCalculate.isSamePoint(polyResourcePoints.get(i), polyResourcePoints.get(j))
                                && (j - i) <= polyResourcePoints.size() / 3) {
                            //剪枝
                            for (int deleteIndex = i + 1; deleteIndex <= j; deleteIndex++) {
                                deleteFlag[deleteIndex] = true;
                            }
                        }
                    }
                }
                for (int i = 0; i < polyResourcePoints.size(); i++) {
                    if (!deleteFlag[i])
                        optimizedPoints.add(polyResourcePoints.get(i));
                }
                polyResourcePoints = optimizedPoints;
                if (!LatLngCalculate.isSamePoint(polyResourcePoints.get(0), polyResourcePoints.get(polyResourcePoints.size() - 1)))
                    polyResourcePoints.add(new LatLng(polyResourcePoints.get(0).latitude, polyResourcePoints.get(0).longitude));


                //add polyline to schematic diagram map view
                routeData.realLength = LatLngCalculate.getPathLength(polyResourcePoints);
                if (Math.abs(routeData.length - routeData.realLength) < 1500) {
                    routeData.ployPoints = polyResourcePoints;
                    RouteSchematicDiagramLayout diagram = new RouteSchematicDiagramLayout(activity, activity.getSavedInstanceState());
                    diagram.setRouteData(routeData);
                    AMap map = diagram.getTextureMapView().getMap();
                    diagram.getTextView().setText("" + routeData.realLength + "M");
                    diagram.getTextView().setTextColor(Color.RED);
                    diagram.getTextureMapView().onCreate(activity.getSavedInstanceState());
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.setPoints(polyResourcePoints);
                    polylineOptions.width(8);
                    polylineOptions.color(Color.RED);
                    polylineOptions.geodesic(true);
                    map.addPolyline(polylineOptions);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(startPoint);
                    markerOptions.draggable(false);
                    map.addMarker(markerOptions).setClickable(false);
                    map.moveCamera(CameraUpdateFactory.changeLatLng(routeData.centerPoint));
                    map.moveCamera(CameraUpdateFactory.zoomTo(13));
                    map.getUiSettings().setZoomControlsEnabled(false);
                    map.getUiSettings().setAllGesturesEnabled(false);
                    activity.addSchematicDiagram(diagram);
                }
            }
        });

        //asyn query
        for (RouteSearch.WalkRouteQuery query : queryList)
            routeSearch.calculateWalkRouteAsyn(query);

    }

    /**
     * 带有途径点的路径规划方案
     *
     * @param startPoint
     * @param passingPoint
     * @param distance
     * @param orient
     * @param activity
     */
    public static void generateRouteWithPassingPoint(final LatLng startPoint, final LatLng passingPoint,
                                                     double distance, int orient,
                                                     final RouteSelectActivity activity) {
        final RouteData routeData = getCircleKeyPointsWithPassingPoint(startPoint, passingPoint,
                distance, orient);
        final RouteSearch routeSearch = new RouteSearch(activity);
        final ArrayList<RouteSearch.FromAndTo> fromAndToList = new ArrayList<RouteSearch.FromAndTo>();
        ArrayList<RouteSearch.WalkRouteQuery> queryList = new ArrayList<RouteSearch.WalkRouteQuery>();

        for (int i = 0; i < routeData.keyPoints.size() - 1; i++) {
            RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
                    new LatLonPoint(routeData.keyPoints.get(i).latitude, routeData.keyPoints.get(i).longitude),
                    new LatLonPoint(routeData.keyPoints.get(i + 1).latitude, routeData.keyPoints.get(i + 1).longitude)
            );
            fromAndToList.add(fromAndTo);
            RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(
                    new RouteSearch.FromAndTo(
                            new LatLonPoint(routeData.keyPoints.get(i).latitude, routeData.keyPoints.get(i).longitude),
                            new LatLonPoint(routeData.keyPoints.get(i + 1).latitude, routeData.keyPoints.get(i + 1).longitude)
                    )
            );
            queryList.add(query);
        }

        //query listener
        routeSearch.setRouteSearchListener(new RunnerRouteSearchListener(queryList.size()) {
            @Override
            public void successHandler(WalkRouteResult walkRouteResult) {
                int index;
                for (index = 0; index < fromAndToList.size(); index++) {
                    RouteSearch.FromAndTo fromAndTo1 = walkRouteResult.getWalkQuery().getFromAndTo();
                    RouteSearch.FromAndTo fromAndTo2 = fromAndToList.get(index);
                    if (fromAndTo1.getFrom().getLatitude() == fromAndTo2.getFrom().getLatitude()
                            && fromAndTo1.getFrom().getLongitude() == fromAndTo2.getFrom().getLongitude()) {
                        break;
                    }
                }
                resultList[index] = walkRouteResult;
                responseCounter++;
                if (responseCounter >= resultList.length) {
                    polyProcess();
                }
            }

            @Override
            public void errorHandler() {
                activity.alert(new AlertMessage("路线规划出错", ""));
            }

            private void polyProcess() {
                //从所有结果中提取多边形顶点
                ArrayList<LatLng> polyResourcePoints = new ArrayList<LatLng>();
                for (WalkRouteResult result : this.resultList) {
                    if (result == null)
                        continue;
                    WalkPath path = result.getPaths().get(result.getPaths().size() - 1);
                    for (WalkStep step : path.getSteps()) {
                        for (LatLonPoint rawPoint : step.getPolyline()) {
                            polyResourcePoints.add(new LatLng(rawPoint.getLatitude(),
                                    rawPoint.getLongitude()));
                        }
                    }
                }
                //删除重复路径
                Boolean[] deleteFlag = new Boolean[polyResourcePoints.size()];
                for (int i = 0; i < deleteFlag.length; i++)
                    deleteFlag[i] = false;
                //路线剪枝
                ArrayList<LatLng> optimizedPoints = new ArrayList<LatLng>();
                for (int i = 0, j; i < polyResourcePoints.size() - 1; i++) {
                    for (j = i + 1; j < polyResourcePoints.size(); j++) {
                        if (LatLngCalculate.isSamePoint(polyResourcePoints.get(i),
                                polyResourcePoints.get(j))
                                && (j - i) <= polyResourcePoints.size() / 3) {
                            //剪枝
                            for (int deleteIndex = i + 1; deleteIndex <= j; deleteIndex++) {
                                deleteFlag[deleteIndex] = true;
                            }
                        }
                    }
                }
                for (int i = 0; i < polyResourcePoints.size(); i++) {
                    if (!deleteFlag[i])
                        optimizedPoints.add(polyResourcePoints.get(i));
                }
                polyResourcePoints = optimizedPoints;
                if (!LatLngCalculate.isSamePoint(polyResourcePoints.get(0),
                        polyResourcePoints.get(polyResourcePoints.size() - 1)))
                    polyResourcePoints.add(new LatLng(polyResourcePoints.get(0).latitude,
                            polyResourcePoints.get(0).longitude));


                //add polyline to schematic diagram map view
                routeData.realLength = LatLngCalculate.getPathLength(polyResourcePoints);
                if (Math.abs(routeData.length - routeData.realLength) < 1500) {
                    routeData.ployPoints = polyResourcePoints;
                    RouteSchematicDiagramLayout diagram = new RouteSchematicDiagramLayout(activity,
                            activity.getSavedInstanceState());
                    diagram.setRouteData(routeData);
                    AMap map = diagram.getTextureMapView().getMap();
                    diagram.getTextView().setText("" + routeData.realLength + "M");
                    diagram.getTextView().setTextColor(Color.RED);
                    diagram.getTextureMapView().onCreate(activity.getSavedInstanceState());
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.setPoints(polyResourcePoints);
                    polylineOptions.width(8);
                    polylineOptions.color(Color.RED);
                    polylineOptions.geodesic(true);
                    map.addPolyline(polylineOptions);
                    MarkerOptions markerOptions = new MarkerOptions();
                    markerOptions.position(startPoint);
                    markerOptions.draggable(false);
                    map.addMarker(markerOptions).setClickable(false);
                    MarkerOptions passPoint = new MarkerOptions();
                    passPoint.position(passingPoint);
                    passPoint.draggable(false);
                    map.addMarker(passPoint).setClickable(false);
                    map.moveCamera(CameraUpdateFactory.changeLatLng(routeData.centerPoint));
                    map.moveCamera(CameraUpdateFactory.zoomTo(13));
                    map.getUiSettings().setZoomControlsEnabled(false);
                    map.getUiSettings().setAllGesturesEnabled(false);
                    activity.addSchematicDiagram(diagram);
                }
            }
        });

        //asyn query
        for (RouteSearch.WalkRouteQuery query : queryList)
            routeSearch.calculateWalkRouteAsyn(query);

    }

}



