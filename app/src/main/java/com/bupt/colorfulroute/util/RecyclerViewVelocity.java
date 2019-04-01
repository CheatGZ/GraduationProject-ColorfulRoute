package com.bupt.colorfulroute.util;

import android.support.v7.widget.RecyclerView;

import java.lang.reflect.Field;
import java.util.logging.FileHandler;

/**
 * @author CheatGZ
 * @date 2019/3/22.
 * description：
 */
public class RecyclerViewVelocity {
    public  static  void setMaxFlingVelocity(RecyclerView recyclerView,int velocity)
    {
        try {
            Field field=recyclerView.getClass().getDeclaredField("mMaxFlingVelocity");
            field.setAccessible(true);
            field.set(recyclerView,velocity);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
