package com.bupt.colorfulroute.runningapp.tools;

/**
 * Created by apple on 2018/4/13.
 */

public class LengthBalance {
    public static double lengthBalance(double distance){
        if (distance < 5000) {
            distance *= 0.6;
        } else if (distance < 7000) {
            distance *= 0.7;
        } else {
            distance *= 0.8;
        }
        return distance;
    }
}
