package com.bupt.colorfulroute.runningapp.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.AMapNaviView;
import com.amap.api.navi.AMapNaviViewListener;
import com.amap.api.navi.AMapNaviViewOptions;
import com.amap.api.navi.INaviInfoCallback;
import com.amap.api.navi.enums.NaviType;
import com.amap.api.navi.model.AMapCalcRouteResult;
import com.amap.api.navi.model.AMapLaneInfo;
import com.amap.api.navi.model.AMapModelCross;
import com.amap.api.navi.model.AMapNaviCameraInfo;
import com.amap.api.navi.model.AMapNaviCross;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.AMapNaviRouteNotifyData;
import com.amap.api.navi.model.AMapNaviTrafficFacilityInfo;
import com.amap.api.navi.model.AMapServiceAreaInfo;
import com.amap.api.navi.model.AimLessModeCongestionInfo;
import com.amap.api.navi.model.AimLessModeStat;
import com.amap.api.navi.model.NaviInfo;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.OverviewButtonView;
import com.amap.api.navi.view.RouteOverLay;
import com.amap.api.navi.view.TrafficButtonView;
import com.autonavi.tbt.TrafficFacilityInfo;
import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.entity.Achievement;
import com.bupt.colorfulroute.runningapp.entity.RouteInfo;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.runnerlib.LatLngCalculate;
import com.bupt.colorfulroute.runningapp.struct.RouteData;
import com.bupt.colorfulroute.runningapp.struct.RunningData;
import com.bupt.colorfulroute.runningapp.tools.MapScale;
import com.bupt.colorfulroute.runningapp.uiutils.StatusBarUtils;
import com.bupt.colorfulroute.util.BaseActivity;
import com.bupt.colorfulroute.util.CheckFormat;
import com.bupt.colorfulroute.util.ToastUtil;
import com.bupt.colorfulroute.util.WeiboConstants;
import com.google.gson.Gson;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class RunningNaviActivity extends BaseActivity implements AMapNaviViewListener, AMapNaviListener, INaviInfoCallback, WbShareCallback {

    private static final double CLOSE_LIMINAL = 100.0;
    private final double NEIBOUR_DISTANCE = 0.0001;
    //用户定位
    public List<Double> latitudes;
    public List<Double> longitudes;
    public Double runnedLength = 0.0;
    public Location aMapLocation;
    public AMapLocationClient aMapLocationClient = null;
    public AMapLocationClientOption aMapLocationClientOption = null;
    public long interval = 500;//定位间隔
    public long currentTime;
    public boolean isRun = true;
    public RunningData runningData;
    public Long time = 0L;
    public List<Achievement> achievement;
    public Integer number = 0;
    public Double length = 0D;
    @BindView(R.id.chronometer)
    Chronometer chronometer;
    @BindView(R.id.navi_length_text)
    TextView naviLengthText;
    @BindView(R.id.navi_finish_button)
    Button naviFinishButton;
    @BindView(R.id.route_kind)
    TextView routeKind;
    @BindView(R.id.route_length)
    TextView routeLength;
    @BindView(R.id.myTrafficButtonView)
    TrafficButtonView myTrafficButtonView;
    @BindView(R.id.back_main_button)
    Button backMainButton;
    @BindView(R.id.myOverviewButtonView)
    OverviewButtonView myOverviewButtonView;
    @BindView(R.id.running_date)
    CardView runningDate;
    @BindView(R.id.run_time)
    TextView runTime;
    @BindView(R.id.run_length)
    TextView runLength;
    @BindView(R.id.runned_date)
    CardView runnedDate;
    BitmapDescriptor[] descriptors = {
            BitmapDescriptorFactory.fromResource(R.mipmap.blue_road)
    };
    @BindView(R.id.weibo_share)
    ImageView weiboShare;
    private List<RouteInfo> routeList = new ArrayList<>();
    private RunningNaviActivity self = this;
    private AMap aMap;
    private AMapNaviView aMapNaviView = null;
    private AMapNavi aMapNavi;
    private Bitmap transparentBitmap;
    private long startTime;
    private RouteData routeData;
    private ArrayList<LatLng> pathPoints = new ArrayList<>();
    private List<NaviLatLng> passPoints = new ArrayList<>();
    private int calculatedPathIndex = 0;
    private boolean initialized = false;
    private boolean navigationStarted = false;
    private boolean flag = false;//标识是否暂停,true暂停，false跑步


    //自定义控件
    private OverviewButtonView mOverviewButtonView;
    private TrafficButtonView mTrafficButtonView;
    private Vibrator vibrator;

    private NaviLatLng myLocation;
    private List<RouteOverLay> routeOverLays = new ArrayList<>();

    private Message msg = null;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    final String objectId = sp.getString("objectId", "null");
                    for (int i = 0; i < routeList.size(); i++) {
                        length += routeList.get(i).getLength();
                        time += routeList.get(i).getTime();
                    }
                    final UserInfo userInfo1 = new UserInfo();
                    userInfo1.setTotalLength(length);
                    userInfo1.setTotalTime(time);
                    userInfo1.setNumber(routeList.size());//跑步次数加1
                    userInfo1.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                BmobQuery<UserInfo> bmobQuery = new BmobQuery();
                                bmobQuery.getObject(objectId, new QueryListener<UserInfo>() {
                                    @Override
                                    public void done(UserInfo userInfo, BmobException e) {
                                        //成就更新判断
                                        if (userInfo.getNumber() >= 1 || userInfo.getAchievement().get(1).getTimeAchieved() == 0) {
                                            userInfo.getAchievement().get(1).setTimeAchieved(System.currentTimeMillis());
                                        }
                                        if (userInfo.getNumber() >= 10 || userInfo.getAchievement().get(2).getTimeAchieved() == 0) {
                                            userInfo.getAchievement().get(2).setTimeAchieved(System.currentTimeMillis());
                                        }
                                        if (runnedLength >= 1000 || userInfo.getAchievement().get(3).getTimeAchieved() == 0) {
                                            userInfo.getAchievement().get(3).setTimeAchieved(System.currentTimeMillis());
                                        }
                                        if (runnedLength >= 10000 || userInfo.getAchievement().get(4).getTimeAchieved() == 0) {
                                            userInfo.getAchievement().get(4).setTimeAchieved(System.currentTimeMillis());
                                        }
                                        if (length >= 100000 || userInfo.getAchievement().get(5).getTimeAchieved() == 0) {
                                            userInfo.getAchievement().get(5).setTimeAchieved(System.currentTimeMillis());
                                        }
                                        if (time >= 36000000 || userInfo.getAchievement().get(6).getTimeAchieved() == 0) {
                                            userInfo.getAchievement().get(6).setTimeAchieved(System.currentTimeMillis());
                                        }
                                        userInfo.update(objectId, new UpdateListener() {
                                            @Override
                                            public void done(BmobException e) {

                                            }
                                        });
                                    }
                                });

                            }
                        }
                    });
                    break;
                case 2:
                    naviLengthText.setText(runnedLength + " m");
                    break;
            }
        }
    };
    private View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.navi_finish_button:
                    isRun = false;
                    aMapNavi.stopNavi();
                    vibrator.vibrate(250);
                    runningDate.setVisibility(View.GONE);
                    runningDate.setAnimation(AnimationUtils.makeOutAnimation(getApplicationContext(), false));
                    naviFinishButton.setVisibility(View.GONE);
                    myTrafficButtonView.setVisibility(View.GONE);
                    myOverviewButtonView.setVisibility(View.GONE);
                    runnedDate.setVisibility(View.VISIBLE);
                    runnedDate.setAnimation(AnimationUtils.makeInAnimation(getApplicationContext(), true));
                    runLength.setText(runnedLength + " m");
                    backMainButton.setVisibility(View.VISIBLE);
                    runTime.setText(chronometer.getText());

                    //计算结果信息上传到数据库
                    runningData = new RunningData();
                    runningData.keyPoints = routeData.keyPoints;
                    runningData.ployPoints = pathPoints;
                    runningData.length = runnedLength;
                    runningData.startTime = startTime;
                    runningData.endTime = System.currentTimeMillis();
                    runningData.time = CheckFormat.getChronometerSeconds(chronometer) * 1000;
                    runningData.centerPoint = routeData.centerPoint;
                    weiboShare.setVisibility(View.VISIBLE);
                    UpdateRunData();
                    showRoute();
                    break;
            }
            return true;
        }
    };
    private WbShareHandler wbShareHandler;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.navi_finish_button:
                    if (!flag) {
                        chronometer.stop();
                        naviFinishButton.setText("继续(长按结束)");
                        naviFinishButton.setBackgroundResource(R.drawable.bg_divider500);
                        flag = true;
                    } else {
                        chronometer.setBase(CheckFormat.convertStrTimeToLong(chronometer.getText().toString()));
                        chronometer.start();
                        naviFinishButton.setText("暂停(长按结束)");
                        naviFinishButton.setBackgroundResource(R.drawable.selector_primary_btn);
                        flag = false;
                    }
                    break;
                case R.id.weibo_share:
                    aMap.getMapScreenShot(new AMap.OnMapScreenShotListener() {
                        @Override
                        public void onMapScreenShot(Bitmap bitmap) {
                            WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                            weiboMessage.imageObject = new ImageObject();
                            weiboMessage.imageObject.setImageObject(bitmap);
                            weiboMessage.textObject = new TextObject();
                            weiboMessage.textObject.text = "我刚刚在彩径完成了一次长为" + runningData.length + "米的跑步,快来看看吧！";
                            wbShareHandler.shareMessage(weiboMessage, false);
                        }

                        @Override
                        public void onMapScreenShot(Bitmap bitmap, int i) {
                            onMapScreenShot(bitmap);
                        }
                    });
                    weiboShare.setEnabled(false);
                    SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    final String objectId = sp.getString("objectId", "null");
                    BmobQuery<UserInfo> bmobQuery = new BmobQuery();
                    bmobQuery.getObject(objectId, new QueryListener<UserInfo>() {
                        @Override
                        public void done(UserInfo userInfo, BmobException e) {
                            if (e == null) {
                                if (userInfo.getAchievement().get(7).getTimeAchieved() == 0) {
                                    userInfo.getAchievement().get(7).setTimeAchieved(System.currentTimeMillis());
                                    userInfo.update(objectId, new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Log.d("Cheat achievement","achievement sucess!");
                                            }
                                        }
                                    });
                                }
                            }
                        }
                    });
                    break;
                case R.id.back_main_button:
                    finish();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_running_navi);
        StatusBarUtils.setStatusBarColor(this, Color.TRANSPARENT, false);
        ButterKnife.bind(this);
        vibrator = (Vibrator) this.getSystemService(this.VIBRATOR_SERVICE);

        //监听事件
        backMainButton.setOnClickListener(onClickListener);
        weiboShare.setOnClickListener(onClickListener);

        SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int checked1 = sp.getInt("RouteKind", 0);
        switch (checked1) {
            case 0:
                routeKind.setText("圆形");
                break;
            case 1:
                routeKind.setText("三叶花");
                break;
            case 2:
                routeKind.setText("四叶花");
                break;
            case 3:
                routeKind.setText("四叶花Ⅱ型");
                break;
            case 4:
                routeKind.setText("四叶花Ⅲ型");
                break;
            default:
                routeKind.setVisibility(View.GONE);
                routeLength.setVisibility(View.GONE);
                break;
        }


        //init weibo share
        WbSdk.install(self, new AuthInfo(self, WeiboConstants.APP_KEY, WeiboConstants.REDIRECT_URL, WeiboConstants.SCOPE));
        wbShareHandler = new WbShareHandler(this);
        wbShareHandler.registerApp();


        //绑定控件
        aMapNaviView = findViewById(R.id.navi_map);
        mOverviewButtonView = findViewById(R.id.myOverviewButtonView);
        mTrafficButtonView = findViewById(R.id.myTrafficButtonView);

        naviFinishButton.setOnClickListener(onClickListener);
        naviFinishButton.setOnLongClickListener(onLongClickListener);
        naviLengthText.setOnClickListener(onClickListener);


        /**
         获取路径关键点集
         */
        routeData = new Gson().fromJson(getIntent().getStringExtra("route_data"), RouteData.class);

        routeLength.setText(routeData.realLength + " m");
        /**
         获取路径绘制边集
         */
        for (int i = 0; i < routeData.keyPoints.size(); i++) {
//            LatLng latLng = intent.getParcelableExtra("point" + i);
//            if (latLng == null) {
//                break;
//            }
            LatLng latLng = routeData.keyPoints.get(i);
            pathPoints.add(latLng);
            NaviLatLng naviLatLng = latLngToNaviLatLng(latLng);
            passPoints.add(naviLatLng);
        }
        myLocation = passPoints.get(0);
        //size过小，规划失败
        if (passPoints.size() < 2 || !passPoints.get(0).equals(passPoints.get(passPoints.size() - 1))) {
            ToastUtil.show(this, getString(R.string.calc_path_fail));
            finish();
            return;
        }

//        transparentBitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.transparent);
        //获取AMapNavi实例
        aMapNavi = AMapNavi.getInstance(getApplicationContext());
        //添加监听回调，用于处理算路成功
        aMapNavi.addAMapNaviListener(this);
        aMapNavi.setUseInnerVoice(true);
        aMapNaviView.onCreate(savedInstanceState);
        aMapNaviView.setAMapNaviViewListener(this);


        AMapNaviViewOptions options = aMapNaviView.getViewOptions();
        options.setLayoutVisible(false);//设置是否使用原控件
        options.setAutoDrawRoute(false);//设置自动绘制路线
        aMapNaviView.setViewOptions(options);
        //自定义控件
        aMapNaviView.setLazyOverviewButtonView(mOverviewButtonView);//自定义全览按钮
        aMapNaviView.setLazyTrafficButtonView(mTrafficButtonView);//路况


        transparentBitmap = BitmapFactory.decodeResource(this.getResources(), R.mipmap.transparent);

        initMyLocation();

    }

    private void initMyLocation() {
//        aMapLocationClient = new AMapLocationClient(this);
//        aMapLocationClientOption = new AMapLocationClientOption();
//        aMapLocationClientOption.setLocationPurpose(AMapLocationClientOption.AMapLocationPurpose.Sport);
//        if (null != aMapLocationClient) {
//            aMapLocationClient.setLocationOption(aMapLocationClientOption);
//            //设置场景模式后最好调用一次stop，再调用start以保证场景模式生效
//            aMapLocationClient.stopLocation();
//            aMapLocationClient.startLocation();
//        }
//        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        aMapLocationClientOption.setInterval(500);
//        aMapLocationClient.setLocationOption(aMapLocationClientOption);
//        aMapLocationClient.setLocationListener(new AMapLocationListener() {
//            @Override
//            public void onLocationChanged(AMapLocation mapLocation) {
//                if (mapLocation != null) {
//                    aMapLocation = mapLocation;
//                }
//            }
//        });
        aMap = aMapNaviView.getMap();
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();
        myLocationStyle.interval(500);
        myLocationStyle.showMyLocation(false);
        aMap.setMyLocationStyle(myLocationStyle);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
        aMap.setMyLocationEnabled(true);
        aMap.setOnMyLocationChangeListener(new AMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                if (location != null) {
                    aMapLocation = location;
//                    Message message = new Message();
//                    message.what = 3;
//                    handler.sendMessage(message);
                }
            }
        });
//        aMapLocationClient.startLocation();
        latitudes = new ArrayList<>();
        longitudes = new ArrayList<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentTime = System.currentTimeMillis();
                while (isRun) {
                    if (System.currentTimeMillis() - interval == currentTime) {
                        latitudes.add(aMapLocation.getLatitude());
                        longitudes.add(aMapLocation.getLongitude());
                        if (latitudes.size() >= 2) {
                            runnedLength = runnedLength + LatLngCalculate.getDistance(latitudes.get(latitudes.size() - 2)
                                    , longitudes.get(longitudes.size() - 2)
                                    , latitudes.get(latitudes.size() - 1)
                                    , longitudes.get(longitudes.size() - 1));
                        }
                        Message msg = new Message();
                        msg.what = 2;
                        handler.sendMessage(msg);
                        currentTime = System.currentTimeMillis();
                    }
                }
            }
        }).start();


    }

    private void showRoute() {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.setPoints(runningData.ployPoints);
        aMap.addPolyline(polylineOptions);
        MarkerOptions startMarker = new MarkerOptions().position(runningData.ployPoints.get(0));
        MarkerOptions endMarker = new MarkerOptions().position(runningData.ployPoints.get(runningData.ployPoints.size() - 1));
        aMap.setMyLocationEnabled(false);
        aMap.addMarker(startMarker);
        aMap.addMarker(endMarker);
        aMap.moveCamera(CameraUpdateFactory.changeLatLng(runningData.centerPoint));
        aMap.moveCamera(CameraUpdateFactory.zoomTo(MapScale.getScale((int) routeData.length)));
        aMap.getUiSettings().setAllGesturesEnabled(true);
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }

    //更新服务端数据
    private void UpdateRunData() {
        SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String account = sp.getString("account", "null");
        final String objectId = sp.getString("objectId", "null");

        RouteInfo routeInfo = new RouteInfo();
        routeInfo.setAccount(account);
        routeInfo.setStartTime(CheckFormat.dateFormat(runningData.startTime));
        routeInfo.setEndTime(CheckFormat.dateFormat(runningData.endTime));
        routeInfo.setLength(runningData.length);
        routeInfo.setTime(runningData.time);
        routeInfo.setKeyPoints(runningData.keyPoints);
        routeInfo.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    System.out.println("添加数据成功，返回objectId为：" + s);
                    BmobQuery<RouteInfo> bmobQuery = new BmobQuery<>();
                    bmobQuery.addWhereEqualTo("account", account);
                    bmobQuery.order("-startTime");
                    bmobQuery.findObjects(new FindListener<RouteInfo>() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void done(List<RouteInfo> list, BmobException e) {
                            if (e == null) {
                                routeList = new ArrayList<>();
                                routeList.addAll(list);
                                msg = new Message();
                                msg.what = 1;
                                handler.sendMessage(msg);
                            } else {
                                System.out.println("analysis bmob fail!" + e.getMessage());
                            }
                        }
                    });
                } else {
                    System.out.println("创建数据失败：" + e.getMessage());
                }
            }
        });
    }

    public NaviLatLng latLngToNaviLatLng(LatLng point) {
        return new NaviLatLng(point.latitude, point.longitude);
    }

    @Override
    protected void onResume() {
        super.onResume();
        aMapNaviView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        aMapNaviView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapNaviView.onDestroy();
        aMapNavi.destroy();
//        aMapLocationClient.stopLocation();
//        aMapLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
    }

    public void onBackPressed() {
    }

    @Override
    public void onInitNaviFailure() {

    }

    @Override
    public void onInitNaviSuccess() {
        aMapNavi.calculateWalkRoute(passPoints.get(calculatedPathIndex), passPoints.get(calculatedPathIndex + 1));
    }

    @Override
    public void onStartNavi(int i) {
        this.startTime = System.currentTimeMillis();
        naviFinishButton.setBackgroundResource(R.drawable.selector_primary_btn);
        chronometer.setFormat("%s");
        chronometer.start();
        naviFinishButton.setText("暂停(长按结束)");
        naviFinishButton.setEnabled(true);

    }

    @Override
    public void onTrafficStatusUpdate() {

    }

    @Override
    public void onLocationChange(AMapNaviLocation aMapNaviLocation) {

    }

    @Override
    public void onArriveDestination(boolean b) {

    }

    @Override
    public void onGetNavigationText(int i, String s) {

    }

    @Override
    public void onGetNavigationText(String s) {
        aMapNavi.startGPS();
    }

    @Override
    public void onEndEmulatorNavi() {

    }

    @Override
    public void onArriveDestination() {

    }

    @Override
    public void onCalculateRouteFailure(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(int[] ints) {
        if (calculatedPathIndex != passPoints.size() - 1 && !initialized) {
            RouteOverLay routeOverLay = drawRoute(aMapNavi.getNaviPath(), 40, 0, passPoints.size() - 2);
            calculatedPathIndex++;
            if (calculatedPathIndex == pathPoints.size() - 1) {
                initialized = true;
                aMapNavi.startNavi(NaviType.GPS);
                return;
            }
            routeOverLays.add(routeOverLay);
            aMapNavi.calculateWalkRoute(passPoints.get(calculatedPathIndex), passPoints.get(calculatedPathIndex + 1));
            return;
        }
    }

    private RouteOverLay drawRoute(AMapNaviPath path, int width, int startLogo, int endLogo) {
        RouteOverLay routeOverLay = new RouteOverLay(aMapNaviView.getMap(), path, this);
        if (calculatedPathIndex != startLogo) {
            routeOverLay.setStartPointBitmap(transparentBitmap);
            Log.i("tanjie", calculatedPathIndex + "无起点");
        }
        if (calculatedPathIndex != endLogo) {
            routeOverLay.setEndPointBitmap(transparentBitmap);
            Log.i("tanjie", calculatedPathIndex + "无终点");
        }

        routeOverLay.setWidth(width);
        routeOverLay.addToMap(descriptors, path.getWayPointIndex());
        return routeOverLay;
    }


    @Override
    public void onStopSpeaking() {
    }

    @Override
    public void onReCalculateRoute(int i) {

    }

    @Override
    public void onExitPage(int i) {

    }

    @Override
    public void onStrategyChanged(int i) {

    }

    @Override
    public View getCustomNaviBottomView() {
        return null;
    }

    @Override
    public View getCustomNaviView() {
        return null;
    }

    @Override
    public void onReCalculateRouteForYaw() {

    }

    @Override
    public void onReCalculateRouteForTrafficJam() {

    }

    @Override
    public void onArrivedWayPoint(int i) {

    }

    @Override
    public void onGpsOpenStatus(boolean b) {

    }

    @Override
    public void onNaviInfoUpdate(NaviInfo naviInfo) {

    }

    @Override
    public void onNaviInfoUpdated(AMapNaviInfo aMapNaviInfo) {

    }

    @Override
    public void updateCameraInfo(AMapNaviCameraInfo[] aMapNaviCameraInfos) {

    }

    @Override
    public void updateIntervalCameraInfo(AMapNaviCameraInfo aMapNaviCameraInfo, AMapNaviCameraInfo aMapNaviCameraInfo1, int i) {

    }

    @Override
    public void onServiceAreaUpdate(AMapServiceAreaInfo[] aMapServiceAreaInfos) {

    }

    @Override
    public void showCross(AMapNaviCross aMapNaviCross) {

    }

    @Override
    public void hideCross() {

    }

    @Override
    public void showModeCross(AMapModelCross aMapModelCross) {

    }

    @Override
    public void hideModeCross() {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo[] aMapLaneInfos, byte[] bytes, byte[] bytes1) {

    }

    @Override
    public void showLaneInfo(AMapLaneInfo aMapLaneInfo) {

    }

    @Override
    public void hideLaneInfo() {

    }


    @Override
    public void notifyParallelRoad(int i) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo aMapNaviTrafficFacilityInfo) {

    }

    @Override
    public void OnUpdateTrafficFacility(AMapNaviTrafficFacilityInfo[] aMapNaviTrafficFacilityInfos) {

    }

    @Override
    public void OnUpdateTrafficFacility(TrafficFacilityInfo trafficFacilityInfo) {

    }

    @Override
    public void updateAimlessModeStatistics(AimLessModeStat aimLessModeStat) {

    }

    @Override
    public void updateAimlessModeCongestionInfo(AimLessModeCongestionInfo aimLessModeCongestionInfo) {

    }

    @Override
    public void onPlayRing(int i) {

    }

    @Override
    public void onCalculateRouteSuccess(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onCalculateRouteFailure(AMapCalcRouteResult aMapCalcRouteResult) {

    }

    @Override
    public void onNaviRouteNotify(AMapNaviRouteNotifyData aMapNaviRouteNotifyData) {

    }

    @Override
    public void onNaviSetting() {

    }

    @Override
    public void onNaviCancel() {

    }

    @Override
    public boolean onNaviBackClick() {
        return true;
    }

    @Override
    public void onNaviMapMode(int i) {

    }

    @Override
    public void onNaviTurnClick() {

    }

    @Override
    public void onNextRoadClick() {

    }

    @Override
    public void onScanViewButtonClick() {

    }

    @Override
    public void onLockMap(boolean b) {

    }

    @Override
    public void onNaviViewLoaded() {

    }

    @Override
    public void onMapTypeChanged(int i) {

    }

    @Override
    public void onNaviViewShowMode(int i) {

    }

    private double getDistance(double point1x, double point1y, double point2x, double point2y) {
        return Math.sqrt(Math.pow(point1x - point2x, 2) + Math.pow(point1y - point2y, 2));
    }

    private double getDistance(NaviLatLng point1, NaviLatLng point2) {
        return getDistance(point1.getLongitude(), point1.getLatitude(), point2.getLongitude(), point2.getLatitude());
    }

    @Override
    public void onWbShareSuccess() {
        weiboShare.setImageResource(R.mipmap.share_complete);
        weiboShare.setEnabled(false);
    }

    @Override
    public void onWbShareCancel() {
        //ignore
    }

    @Override
    public void onWbShareFail() {
        //ignore
    }
}
