package com.bupt.colorfulroute.runningapp.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.uiutils.StatusBarUtils;
import com.bupt.colorfulroute.util.BaseActivity;
import com.bupt.colorfulroute.util.CheckFormat;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bupt.colorfulroute.runningapp.runnerlib.LatLngCalculate.getDistance;

/**
 * @author CheatGZ
 * @date 2019/3/3.
 * description：
 */
public class FreeRunActivity extends BaseActivity {
    //用户定位
    public List<Double> latitudes;
    public List<Double> longitudes;
    public Double runnedLength = 0.0;
    public AMapLocation aMapLocation;
    public AMapLocationClient aMapLocationClient = null;
    public AMapLocationClientOption aMapLocationClientOption = null;
    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.run_length_text)
    TextView runLengthText;
    @BindView(R.id.run_finish_button)
    Button runFinishButton;
    @BindView(R.id.run_time)
    TextView runTime;
    @BindView(R.id.run_length)
    TextView runLength;
    @BindView(R.id.back_main_button)
    Button backMainButton;
    @BindView(R.id.running_date)
    CardView runningDate;
    @BindView(R.id.runned_date)
    CardView runnedDate;
    long interval = 500;//定位间隔
    long currentTime;
    boolean isRun = true;
    @BindView(R.id.my_location)
    ImageView myLocation;
    private MapView mapView;
    private AMap aMap;
    private MyLocationStyle myLocationStyle;
    private Vibrator vibrator;
    private boolean flag = false;//标识是否暂停,true暂停，false跑步
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.run_finish_button:
                    if (!flag) {
                        chronometer.stop();
                        runFinishButton.setText("继续(长按结束)");
                        runFinishButton.setBackgroundResource(R.drawable.bg_divider500);
                        flag = true;
                    } else {
                        chronometer.setBase(CheckFormat.convertStrTimeToLong(chronometer.getText().toString()));
                        chronometer.start();
                        runFinishButton.setText("暂停(长按结束)");
                        runFinishButton.setBackgroundResource(R.drawable.selector_primary_btn);
                        flag = false;
                    }
                    break;
                case R.id.my_location:
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude())));
                    break;
                case R.id.back_main_button:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.run_finish_button:
                    isRun = false;
                    vibrator.vibrate(250);
                    runningDate.setVisibility(View.GONE);
                    runningDate.setAnimation(AnimationUtils.makeOutAnimation(getApplicationContext(), false));
                    runFinishButton.setVisibility(View.GONE);
                    runnedDate.setVisibility(View.VISIBLE);
                    runnedDate.setAnimation(AnimationUtils.makeInAnimation(getApplicationContext(), true));
                    runTime.setText(chronometer.getText());
                    runLength.setText(runLengthText.getText());
                    backMainButton.setVisibility(View.VISIBLE);
                    break;
            }
            return true;
        }
    };

    private Message msg = null;
    //线程使用的handler  创建一个线程来进行实时显示总公里数
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    runLengthText.setText(runnedLength + " m");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_run);
        StatusBarUtils.setStatusBarColor(this, Color.TRANSPARENT, false);
        ButterKnife.bind(this);
        mapView = findViewById(R.id.map_view);
        vibrator = (Vibrator) this.getSystemService(VIBRATOR_SERVICE);
        chronometer.start();
        initMap(savedInstanceState);
        initMyLocation();
        runFinishButton.setOnClickListener(onClickListener);
        runFinishButton.setOnLongClickListener(onLongClickListener);
        backMainButton.setOnClickListener(onClickListener);
        myLocation.setOnClickListener(onClickListener);

    }

    private void initMyLocation() {
        aMapLocationClient = new AMapLocationClient(this);
        aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
        if (null != aMapLocationClient) {
            aMapLocationClient.setLocationOption(aMapLocationClientOption);
            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
            aMapLocationClient.stopLocation();
            aMapLocationClient.startLocation();
        }
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClientOption.setInterval(500);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.setLocationListener(new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation mapLocation) {
                if (mapLocation != null) {
                    aMapLocation = mapLocation;
                }
            }
        });

        aMapLocationClient.startLocation();
        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();

        new Thread(new Runnable() {
            @Override
            public void run() {

                currentTime = System.currentTimeMillis();
                while (isRun) {
                    if (System.currentTimeMillis() - interval == currentTime) {
                        latitudes.add(aMapLocation.getLatitude());
                        longitudes.add(aMapLocation.getLongitude());
                        if (latitudes.size() >= 2) {
                            runnedLength = runnedLength + getDistance(latitudes.get(latitudes.size() - 2)
                                    , longitudes.get(longitudes.size() - 2)
                                    , latitudes.get(latitudes.size() - 1)
                                    , longitudes.get(longitudes.size() - 1));
                        }
                        msg = new Message();
                        msg.what = 1;
                        handler.sendMessage(msg);
                        currentTime = System.currentTimeMillis();
                    }
                }
            }
        }).start();


    }

    private void initMap(Bundle savedInstanceState) {
        mapView.onCreate(savedInstanceState);

        if (aMap == null) {
            aMap = mapView.getMap();
        }
        myLocationStyle = new MyLocationStyle();//（1秒1次定位）如果不设置myLocationType，默认执行此种模式。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//设置定位蓝点精度圆圈的填充颜色的方法。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(19));
        aMap.getUiSettings().setAllGesturesEnabled(true);//设置手势
        aMap.getUiSettings().setZoomControlsEnabled(false);//设置缩放
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapLocationClient.stopLocation();
        aMapLocationClient.onDestroy();
    }
}
