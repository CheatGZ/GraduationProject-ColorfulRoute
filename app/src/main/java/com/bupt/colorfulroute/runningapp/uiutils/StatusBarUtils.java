package com.bupt.colorfulroute.runningapp.uiutils;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.Window;

/**
 * 设置状态栏颜色；用于实现沉浸式状态栏
 */
public class StatusBarUtils {
    public static void setStatusBarColor(Activity activity, int colorId, boolean dark) {
        if (Build.VERSION.SDK_INT >= 22) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            int option;
            if (dark) {
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            }
            decorView.setSystemUiVisibility(option);
            window.setStatusBarColor(colorId);
            //透明导航栏
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    public static void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT >= 22) {
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(option);
        }
    }

}