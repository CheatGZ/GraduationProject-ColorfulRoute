package com.bupt.colorfulroute.runningapp.tools;

/**
 * Created by apple on 2018/3/13.
 */

public class MapScale {
    public static int getScale(int length){
        if(length == 0){
            return 19;
        }else if(length < 1000){
            return 17;
        }else if(length < 2000){
            return 16;
        }else if(length < 5000){
            return 15;
        }else if(length < 10000){
            return 14;
        }else{
            return 13;
        }
    }
}
