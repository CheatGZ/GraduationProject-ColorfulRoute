package com.bupt.colorfulroute.runningapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.activity.FreeRunActivity;
import com.bupt.colorfulroute.runningapp.activity.MainActivity;
import com.bupt.colorfulroute.runningapp.activity.RouteSelectActivity;
import com.bupt.colorfulroute.runningapp.uicomponent.ScrollTextView;
import com.bupt.colorfulroute.runningapp.uicomponent.scaleview.OnValueChangeListener;
import com.bupt.colorfulroute.runningapp.uicomponent.scaleview.VerticalScaleView;
import com.bupt.colorfulroute.util.OnMultiClickListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.Bmob;

public class MainFragment extends Fragment {


    View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.btn_run_start:
                    Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                    Intent intent = new Intent(getActivity(), FreeRunActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @BindView(R.id.change_map)
    LinearLayout changeMap;
    @BindView(R.id.scale_show_layout)
    LinearLayout scaleShowLayout;
    @BindView(R.id.layout_scale_view)
    RelativeLayout layoutScaleView;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.title_bar)
    LinearLayout titleBar;
    @BindView(R.id.scroll_text_view)
    ScrollTextView scrollTextView;
    @BindView(R.id.length_scale_view)
    VerticalScaleView lengthScaleView;
    @BindView(R.id.tip_left)
    TextView tipLeft;
    @BindView(R.id.btn_run_start)
    Button btnRunStart;
    @BindView(R.id.tip_right)
    TextView tipRight;
    Unbinder unbinder;
    @BindView(R.id.layout_title_bar)
    LinearLayout layoutTitleBar;
    private RadioButton[] rb = new RadioButton[5];
    private int checkedRoute;
    private PopupWindow popupWindow;
    View.OnClickListener routeKind = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sp1 = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            int checked1 = sp1.getInt("RouteKind", 0);
            SharedPreferences.Editor editor = sp1.edit();//获取编辑器
            switch (v.getId()) {
                case R.id.bt1:
                    rb[checked1].setChecked(false);
                    rb[0].setChecked(true);
                    editor.putInt("RouteKind", 0);
                    editor.apply();//提交修改
                    titleText.setText("圆形");
                    popupWindow.dismiss();
                    break;
                case R.id.bt2:
                    rb[checked1].setChecked(false);
                    rb[1].setChecked(true);
                    editor.putInt("RouteKind", 1);
                    editor.apply();//提交修改
                    titleText.setText("三叶花");
                    popupWindow.dismiss();
                    break;
                case R.id.bt3:
                    rb[checked1].setChecked(false);
                    rb[2].setChecked(true);
                    editor.putInt("RouteKind", 2);
                    editor.apply();//提交修改
                    titleText.setText("四叶花");
                    popupWindow.dismiss();
                    break;
                case R.id.bt4:
                    rb[checked1].setChecked(false);
                    rb[3].setChecked(true);
                    editor.putInt("RouteKind", 3);
                    editor.apply();//提交修改
                    titleText.setText("四叶花Ⅱ型");
                    popupWindow.dismiss();
                    break;
                case R.id.bt5:
                    rb[checked1].setChecked(false);
                    rb[4].setChecked(true);
                    editor.putInt("RouteKind", 4);
                    editor.apply();//提交修改
                    titleText.setText("四叶花Ⅲ型");
                    popupWindow.dismiss();
                    break;
                default:
                    break;
            }
        }
    };
    private int length = 3000;
    private double longitude;
    private double latitude;
    private TextureMapView mapView;
    private MyLocationStyle myLocationStyle;
    private AMap aMap;
    OnMultiClickListener onMultiClickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            Intent intent = new Intent();
            latitude = aMap.getMyLocation().getLatitude();
            longitude = aMap.getMyLocation().getLongitude();
            intent.putExtra("length", length + "");
            intent.putExtra("lat", latitude);
            intent.putExtra("lng", longitude);
            intent.setClass(getContext(), RouteSelectActivity.class);
            startActivity(intent);
        }
    };
    private int flag_map_show = 0;//默认显示普通地图
    private boolean flag_scale = false;//默认不显示刻度尺
    private float zoom;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.change_map:
                    switch (flag_map_show) {
                        case 0:
                            flag_map_show = 1;
                            initMap();
                            break;
                        case 1:
                            flag_map_show = 2;
                            initMap();
                            break;
                        case 2:
                            flag_map_show = 0;
                            initMap();
                            break;
                        default:
                            initMap();
                            aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                            break;
                    }
                    break;
                case R.id.tip_left:
                    ((MainActivity) getActivity()).changeFragment(0);
                    break;
                case R.id.tip_right:
                    ((MainActivity) getActivity()).changeFragment(2);
                    break;
                case R.id.title_bar:
                    SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    checkedRoute = sp.getInt("RouteKind", 0);
                    showPopWindow();
                    break;
                case R.id.scale_show_layout:
                    if (!flag_scale) {
                        layoutTitleBar.setVisibility(View.GONE);
                        layoutTitleBar.setAnimation(AnimationUtils.makeOutAnimation(getContext(), false));
                        layoutScaleView.setVisibility(View.VISIBLE);
                        layoutScaleView.setAnimation(AnimationUtils.makeInAnimation(getContext(), false));
                        flag_scale = true;
                    } else {
                        layoutTitleBar.setVisibility(View.VISIBLE);
                        layoutTitleBar.setAnimation(AnimationUtils.makeInAnimation(getContext(), true));
                        layoutScaleView.setVisibility(View.GONE);
                        layoutScaleView.setAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
                        flag_scale = false;
                    }
                    break;
                default:
                    break;
            }
        }
    };

    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bmob.initialize(getContext(), "e834b45389cad785bed5c43e2942b606");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        titleText.setText("跑  步");

        mapView = view.findViewById(R.id.main_map_view);
        btnRunStart.setText("规划跑 3 km");
        mapView.onCreate(savedInstanceState);
        zoom = 19;
        initScrollTextView();
        initMap();
        initRouteKind();
        initScaleView();

        tipLeft.setOnClickListener(onClickListener);
        tipRight.setOnClickListener(onClickListener);
        changeMap.setOnClickListener(onClickListener);
        titleBar.setOnClickListener(onClickListener);
        btnRunStart.setOnClickListener(onMultiClickListener);
        btnRunStart.setOnLongClickListener(onLongClickListener);
        scaleShowLayout.setOnClickListener(onClickListener);

        return view;
    }

    private void initScrollTextView() {
        List<String> scrollText = new ArrayList<>();
        scrollText.add("| 长按规划跑可进入自由跑！自由跑不会保存跑步数据！");
        scrollText.add("| 点击上方标题栏，可以选择跑步路径形状！");
        scrollText.add("| 点击下方蓝色按钮，可以切换地图视图!");
        scrollText.add("| 点击下方白色按钮，可以弹出距离尺，选择跑步距离！");
        scrollText.add("| 足迹界面的历史记录可以右滑删除，删除后无法恢复！");
        scrollTextView.setList(scrollText);
        scrollTextView.startScroll();
    }

    private void initScaleView() {
        lengthScaleView.setRange(1, 9);//设置距离范围,两端值加起来为偶数
        lengthScaleView.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public void onValueChanged(float value) {
                btnRunStart.setText("规划跑 " + value + " km");
                length = (int) value * 1000;
                if (value >= 7) {
                    zoom=13;
                } else {
                    zoom = 20 - value;
                }
//                initMap();
                aMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
            }
        });
    }

    private void initRouteKind() {
        SharedPreferences sp1 = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        int checked1 = sp1.getInt("RouteKind", 0);
        switch (checked1) {
            case 0:
                titleText.setText("圆形");
                break;
            case 1:
                titleText.setText("三叶花");
                break;
            case 2:
                titleText.setText("四叶花");
                break;
            case 3:
                titleText.setText("四叶花Ⅱ型");
                break;
            case 4:
                titleText.setText("四叶花Ⅲ型");
                break;
        }
    }

    private void showPopWindow() {
        View popView = getLayoutInflater().inflate(R.layout.route_kind_select, null);
        popupWindow = new PopupWindow(popView, ViewGroup.LayoutParams.MATCH_PARENT, 300, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        popupWindow.setBackgroundDrawable(getResources().getDrawable(R.color.primary_light, null));
        popupWindow.showAsDropDown(titleBar);
        View inView = popupWindow.getContentView();
        rb[0] = inView.findViewById(R.id.bt1);
        rb[1] = inView.findViewById(R.id.bt2);
        rb[2] = inView.findViewById(R.id.bt3);
        rb[3] = inView.findViewById(R.id.bt4);
        rb[4] = inView.findViewById(R.id.bt5);
        rb[checkedRoute].setChecked(true);
        rb[0].setOnClickListener(routeKind);
        rb[1].setOnClickListener(routeKind);
        rb[2].setOnClickListener(routeKind);
        rb[3].setOnClickListener(routeKind);
        rb[4].setOnClickListener(routeKind);
    }

    private void initMap() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.strokeColor(Color.argb(0, 0, 0, 0));
        myLocationStyle.radiusFillColor(Color.argb(0, 0, 0, 0));//设置定位蓝点精度圆圈的填充颜色的方法。
        myLocationStyle.interval(1000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        switch (flag_map_show) {
            case 0:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                break;
            case 1:
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);
                break;
            case 2:
                aMap.setMapType(AMap.MAP_TYPE_SATELLITE);
                break;
            default:
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
                break;
        }
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
        aMap.getUiSettings().setCompassEnabled(false);
        aMap.getUiSettings().setAllGesturesEnabled(false);//不允许手势
        aMap.getUiSettings().setZoomControlsEnabled(false);//不允许缩放
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
