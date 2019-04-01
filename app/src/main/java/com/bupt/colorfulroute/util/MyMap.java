package com.bupt.colorfulroute.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.TextureMapView;
import com.autonavi.amap.mapcore.interfaces.IMapFragmentDelegate;

/**
 * @author CheatGZ
 * @date 2019/3/24.
 * description：自定义map，防止viewpager中父控件拦截map的触摸动作
 */
public class MyMap extends TextureMapView {
    public MyMap(Context context) {
        super(context);
    }

    public MyMap(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    protected IMapFragmentDelegate getMapFragmentDelegate() {
        return super.getMapFragmentDelegate();
    }

    public MyMap(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
