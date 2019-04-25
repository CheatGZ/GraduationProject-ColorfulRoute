package com.bupt.colorfulroute.runningapp.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.entity.RouteInfo;
import com.bupt.colorfulroute.util.BaseActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

public class HistoryDetailActivity extends BaseActivity {
    HistoryDetailActivity self = this;
    @BindView(R.id.back_button)
    ImageView backButton;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_button)
    ImageView rightButton;
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;
    @BindView(R.id.length_text)
    TextView lengthText;
    @BindView(R.id.time_text)
    TextView timeText;


    private List<LatLng> ployPoints;
    private String objectId;
    private RouteInfo mRouteInfo = null;

    private MapView mapView;
    private AMap map;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_layout:
                    finish();
                    break;
                case R.id.right_layout:
                    map.moveCamera(CameraUpdateFactory.changeLatLng(ployPoints.get(0)));
                    map.moveCamera(CameraUpdateFactory.zoomTo(15));
                    break;
                default:
                    break;
            }
        }
    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(self, "e834b45389cad785bed5c43e2942b606");
        setContentView(R.layout.activity_history_detail);
        ButterKnife.bind(this);

        //初始化地图
        mapView = findViewById(R.id.detail_map);
        mapView.onCreate(savedInstanceState);
        map = mapView.getMap();

        backButton.setBackgroundResource(R.mipmap.back);
        rightButton.setBackgroundResource(R.mipmap.my_location_white);
        leftLayout.setOnClickListener(onClickListener);
        rightLayout.setOnClickListener(onClickListener);

        //获取数据
        initMap();
    }


    private void initMap() {
        this.objectId = getIntent().getStringExtra("objectId");
        BmobQuery<RouteInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("objectId", objectId);
        bmobQuery.getObject(objectId, new QueryListener<RouteInfo>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void done(RouteInfo routeInfo, BmobException e) {
                if (e == null) {
                    mRouteInfo = routeInfo;
                    ployPoints = routeInfo.getKeyPoints();

                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.setPoints(ployPoints);
                    map.addPolyline(polylineOptions);
                    MarkerOptions startMarker = new MarkerOptions().position(ployPoints.get(0));
                    MarkerOptions endMarker = new MarkerOptions().position(ployPoints.get(ployPoints.size() - 1));
                    map.addMarker(startMarker);
                    map.addMarker(endMarker);
                    map.moveCamera(CameraUpdateFactory.changeLatLng(ployPoints.get(0)));
                    map.moveCamera(CameraUpdateFactory.zoomTo(15));
                    map.getUiSettings().setAllGesturesEnabled(true);
                    map.getUiSettings().setZoomControlsEnabled(false);

                    titleText.setText(mRouteInfo.getStartTime());
                    lengthText.setText(mRouteInfo.getLength() / 1000 + " km");
                    timeText.setText(timeFormat(+mRouteInfo.getTime()));
                }
            }
        });
    }

    private String timeFormat(long mills) {
        int h, m, s;
        h = (int) (mills / 1000 / 3600 % 24);
        m = (int) (mills / 1000 / 60 % 60);
        s = (int) (mills / 1000 % 60);
        return "" + h + "时" + m + "分" + s + "秒";
    }
}
