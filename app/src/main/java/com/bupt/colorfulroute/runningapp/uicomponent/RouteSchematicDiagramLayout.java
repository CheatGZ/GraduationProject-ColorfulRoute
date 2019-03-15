package com.bupt.colorfulroute.runningapp.uicomponent;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.struct.RouteData;


public class RouteSchematicDiagramLayout extends LinearLayout {
    private RouteSchematicDiagramLayout self = this;
    private TextureMapView textureMapView;
    private TextView textView;
    private RouteData routeData;

    public RouteSchematicDiagramLayout(Context context, Bundle savedInstanceState) {
        super(context);
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        inflater.inflate(R.layout.layout_route_schematic_diagram, this);
        textureMapView = findViewById(R.id.route_schematic_map);
        textView = findViewById(R.id.route_schematic_length_text_view);
        textureMapView.onCreate(savedInstanceState);
        this.setBackgroundColor(getResources().getColor(R.color.icons));
    }

    public TextureMapView getTextureMapView() {
        return textureMapView;
    }

    public TextView getTextView() {
        return textView;
    }

    public RouteData getRouteData() {
        return routeData;
    }

    public void setRouteData(RouteData routeData) {
        this.routeData = routeData;
    }

    public void chooseView(boolean choose) {
        if (choose) {
            this.setBackground(getResources().getDrawable(R.drawable.bg_primary_light));
        } else {
            this.setBackgroundColor(getResources().getColor(R.color.icons));
        }
    }

    @Override
    public void setOnClickListener(final OnClickListener l) {
        super.setOnClickListener(l);
        textureMapView.getMap().setOnMapClickListener(new AMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                self.callOnClick();
            }
        });
        textView.setOnClickListener(l);
    }
}
