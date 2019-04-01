package com.bupt.colorfulroute.util;

import android.content.Context;
import android.support.v7.widget.LinearSmoothScroller;

/**
 * @author CheatGZ
 * @date 2019/3/24.
 * description：recyclerview平滑移动到某个位置
 */
public class TopSmoothScroller extends LinearSmoothScroller {
    public TopSmoothScroller(Context context) {
        super(context);
    }

    @Override
    public int getHorizontalSnapPreference() {
        return SNAP_TO_START;//具体见源码注释
    }

    @Override
    public int getVerticalSnapPreference() {
        return SNAP_TO_START;//具体见源码注释
    }
}

