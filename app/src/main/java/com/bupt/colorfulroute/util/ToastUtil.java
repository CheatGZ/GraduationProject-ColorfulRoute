package com.bupt.colorfulroute.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tanjie on 2018/4/9.
 */

public class ToastUtil {
    public static void show(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT);
    }
}
