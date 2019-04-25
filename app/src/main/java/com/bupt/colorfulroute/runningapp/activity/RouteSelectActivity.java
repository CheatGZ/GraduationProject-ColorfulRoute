package com.bupt.colorfulroute.runningapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.runnerlib.RouteGenerate;
import com.bupt.colorfulroute.runningapp.struct.RouteData;
import com.bupt.colorfulroute.runningapp.tools.MapScale;
import com.bupt.colorfulroute.runningapp.uicomponent.AlertMessage;
import com.bupt.colorfulroute.runningapp.uicomponent.RouteSchematicDiagramLayout;
import com.bupt.colorfulroute.runningapp.uiutils.StatusBarUtils;
import com.bupt.colorfulroute.util.BaseActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RouteSelectActivity extends BaseActivity {


    @BindView(R.id.start_navi_button)
    Button startNaviButton;
    @BindView(R.id.back_button)
    ImageView backButton;
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_button)
    ImageView rightButton;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;

    private RouteSelectActivity self = this;
    private Bundle savedInstanceState;

    private TextureMapView mainMapView;
    private MyLocationStyle myLocationStyle;
    private AMap aMap;

    private LinearLayout routeSchemaDiagramContainer;


    private ArrayList<RouteSchematicDiagramLayout> routeSchematicDiagramList = new ArrayList<>();
    private RouteSchematicDiagramLayout selectedDiagram = null;

    //data for search_view_bg route
    private int length;
    private double longitude;
    private double latitude;

    private List<LatLng> passPoints = null;

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.start_navi_button:
                    if (selectedDiagram == null) {
                        alert(new AlertMessage("错误!", "请先选择路线再点击跑步!"));
                    } else {
                        if (passPoints.size() < 2) {
                            alert(new AlertMessage("错误!", "路线规划失败!"));
                        }
                        Intent intent = new Intent();
                        intent.putExtra("route_data", new Gson().toJson(selectedDiagram.getRouteData()));
                        for (int j = 0; j < passPoints.size(); j++) {
                            intent.putExtra("point" + j, passPoints.get(j));
                        }
                        intent.setClass(self, RunningNaviActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
                case R.id.left_layout:
                    finish();
                    break;
                default:
                    for (RouteSchematicDiagramLayout diagram : routeSchematicDiagramList) {
                        if (v == diagram || v == diagram.getTextView()) {
                            for (RouteSchematicDiagramLayout d : routeSchematicDiagramList)
                                d.chooseView(false);
                            diagram.chooseView(true);
                            RouteData routeDataToDraw = diagram.getRouteData();
                            aMap.clear();
                            Log.d("cheatTest", "choosemap=" + diagram.getRouteData());
                            //start/stop marker
                            MarkerOptions markerOptions = new MarkerOptions();
                            markerOptions.position(routeDataToDraw.keyPoints.get(0));
                            markerOptions.draggable(false);
                            markerOptions.title("开始/终点");
                            aMap.addMarker(markerOptions);

                            PolylineOptions polylineOptions = new PolylineOptions();
                            polylineOptions.setPoints(routeDataToDraw.ployPoints);
                            passPoints = routeDataToDraw.keyPoints;
                            aMap.addPolyline(polylineOptions);
                            aMap.moveCamera(CameraUpdateFactory.changeLatLng(routeDataToDraw.centerPoint));
                            aMap.moveCamera(CameraUpdateFactory.zoomTo(MapScale.getScale((int) diagram.getRouteData().length)));
                            selectedDiagram = diagram;
                            break;
                        }
                    }
                    break;
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_select);
        StatusBarUtils.setStatusBarColor(this, Color.TRANSPARENT, false);
        ButterKnife.bind(this);
        this.savedInstanceState = savedInstanceState;
        //get data for search_view_bg route
        length = Integer.parseInt(getIntent().getStringExtra("length"));
        longitude = getIntent().getDoubleExtra("lng", 0);
        latitude = getIntent().getDoubleExtra("lat", 0);

        //init ui
        titleText.setText("规划路径");
        backButton.setBackgroundResource(R.mipmap.back);
        leftLayout.setOnClickListener(onClickListener);
        startNaviButton.setOnClickListener(onClickListener);
        mainMapView = findViewById(R.id.main_map);
        routeSchemaDiagramContainer = findViewById(R.id.route_schema_diagram_container);
        mainMapView.onCreate(savedInstanceState);

        //初始化定位蓝点
        if (aMap == null) {
            aMap = mainMapView.getMap();
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(18));
        aMap.getUiSettings().setAllGesturesEnabled(true);//不允许手势
        aMap.getUiSettings().setZoomControlsEnabled(false);//删除缩放按钮

        //规划路径
        new Thread(new Runnable() {
            @Override
            public void run() {
                searchRoute();
            }
        }).start();
    }

    public void addSchematicDiagram(RouteSchematicDiagramLayout routeSchematicDiagramLayout) {
        this.routeSchematicDiagramList.add(routeSchematicDiagramLayout);
        this.routeSchemaDiagramContainer.addView(routeSchematicDiagramLayout);
        routeSchematicDiagramLayout.setOnClickListener(onClickListener);
    }

    public Bundle getSavedInstanceState() {
        return savedInstanceState;
    }

    private void searchRoute() {
        SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int checkedRoute = sp.getInt("RouteKind", 0);
        RouteGenerate.generateFlowerRoute(checkedRoute, new LatLng(latitude, longitude),
                length,
                RouteGenerate.NORTH,
                self);
        RouteGenerate.generateFlowerRoute(checkedRoute,
                new LatLng(latitude, longitude),
                length,
                RouteGenerate.SOUTH,
                self
        );
        RouteGenerate.generateFlowerRoute(checkedRoute,
                new LatLng(latitude, longitude),
                length,
                RouteGenerate.WEST,
                self
        );
        RouteGenerate.generateFlowerRoute(checkedRoute,
                new LatLng(latitude, longitude),
                length,
                RouteGenerate.EAST,
                self
        );
    }
}
