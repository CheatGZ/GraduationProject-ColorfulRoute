package com.bupt.colorfulroute.runningapp.runnerlib;

import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;


abstract public class RunnerRouteSearchListener implements RouteSearch.OnRouteSearchListener {
    protected WalkRouteResult[] resultList;
    protected int responseCounter;

    public RunnerRouteSearchListener(int subRouteNum) {
        resultList = new WalkRouteResult[subRouteNum];
        responseCounter = 0;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
        //ignore
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        //ignore
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
        if (walkRouteResult == null) {
            errorHandler();
        } else {
            successHandler(walkRouteResult);
        }
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
        //ignore
    }

    abstract public void successHandler(WalkRouteResult walkRouteResult);

    abstract public void errorHandler();
}
