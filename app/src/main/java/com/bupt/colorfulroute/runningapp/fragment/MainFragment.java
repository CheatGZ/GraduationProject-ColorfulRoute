package com.bupt.colorfulroute.runningapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.activity.FreeRunActivity;
import com.bupt.colorfulroute.runningapp.activity.MainActivity;
import com.bupt.colorfulroute.runningapp.activity.RouteSelectActivity;
import com.bupt.colorfulroute.runningapp.entity.RouteInfo;
import com.bupt.colorfulroute.runningapp.uicomponent.DashBoardProgressView;
import com.bupt.colorfulroute.runningapp.uicomponent.ScrollTextView;
import com.bupt.colorfulroute.runningapp.uicomponent.scaleview.OnValueChangeListener;
import com.bupt.colorfulroute.runningapp.uicomponent.scaleview.VerticalScaleView;
import com.bupt.colorfulroute.util.CheckFormat;
import com.bupt.colorfulroute.util.MyMap;
import com.bupt.colorfulroute.util.OnMultiClickListener;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class MainFragment extends Fragment {
    protected static final float FLIP_DISTANCE = 50;
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
    @BindView(R.id.tip_right)
    TextView tipRight;
    Unbinder unbinder;
    @BindView(R.id.layout_title_bar)
    ConstraintLayout layoutTitleBar;
    View.OnLongClickListener onLongClickListener = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            switch (v.getId()) {
                case R.id.btn_run_start:
                    Vibrator vibrator = (Vibrator) getActivity().getSystemService(getActivity().VIBRATOR_SERVICE);
                    vibrator.vibrate(250);
                    Intent intent = new Intent(getActivity(), FreeRunActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        }
    };
    @BindView(R.id.layout_start)
    LinearLayout layoutStart;
    @BindView(R.id.route_kind_btn)
    TextView routeKindBtn;
    @BindView(R.id.my_location)
    LinearLayout myLocation;
    private TextView btnRunStart;
    private DashBoardProgressView wpbView;
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
    private GestureDetector mDetector;
    private int length = 3000;
    private double longitude;
    private double latitude;
    private MyMap mapView;
    private MyLocationStyle myLocationStyle;
    private AMap aMap;
    OnMultiClickListener onMultiClickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            btnRunStart.setEnabled(false);
            Intent intent = new Intent(getContext(), RouteSelectActivity.class);
            latitude = aMap.getMyLocation().getLatitude();
            longitude = aMap.getMyLocation().getLongitude();
            intent.putExtra("length", length + "");
            intent.putExtra("lat", latitude);
            intent.putExtra("lng", longitude);
            startActivity(intent);
        }
    };
    private int flag_map_show = 0;//默认显示地图
    private boolean flag_scale = false;//默认不显示刻度尺
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.change_map:
                    switch (flag_map_show) {
                        case 0:
                            mapView.setVisibility(View.GONE);
                            myLocation.setVisibility(View.GONE);
                            flag_map_show = 1;
                            break;
                        case 1:
                            mapView.setVisibility(View.VISIBLE);
                            myLocation.setVisibility(View.VISIBLE);
                            flag_map_show = 0;
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
                    layoutScaleView.setVisibility(View.GONE);
                    layoutScaleView.setAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
                    flag_scale = false;
                    SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    checkedRoute = sp.getInt("RouteKind", 0);
                    showPopWindow();
                    break;
                case R.id.scale_show_layout:
                    if (!flag_scale) {
                        layoutScaleView.setVisibility(View.VISIBLE);
                        layoutScaleView.setAnimation(AnimationUtils.makeInAnimation(getContext(), false));
                        flag_scale = true;
                    } else {
                        layoutScaleView.setVisibility(View.GONE);
                        layoutScaleView.setAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
                        flag_scale = false;
                    }
                    break;
                case R.id.my_location:
                    aMap.moveCamera(CameraUpdateFactory.changeLatLng(new LatLng(aMap.getMyLocation().getLatitude(), aMap.getMyLocation().getLongitude())));
                    break;
                default:
                    break;
            }
        }
    };
    private float zoom;

    public static MainFragment newInstance() {
        MainFragment mainFragment = new MainFragment();
        return mainFragment;
    }

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bmob.initialize(getContext(), "e834b45389cad785bed5c43e2942b606");
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        mapView = view.findViewById(R.id.main_map_view);
        wpbView = view.findViewById(R.id.wpb_progress_view);
        btnRunStart=view.findViewById(R.id.btn_run_start);
        SpannableString btn_text;
        btn_text = new SpannableString("规划跑 3.0 km");
        btn_text.setSpan(new AbsoluteSizeSpan(18, true), 4, 7, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        btnRunStart.setText(btn_text);
        mapView.onCreate(savedInstanceState);

        zoom = 19;
        initScrollTextView();
        initMap();
        initRouteKind();
        initScaleView();
        initDialView();
        initGestureDetector();

        //frgment滑动监听事件
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mDetector.onTouchEvent(event);
            }
        });

        btnRunStart.setOnClickListener(onMultiClickListener);
        btnRunStart.setOnLongClickListener(onLongClickListener);
        mapView.setOnClickListener(onClickListener);
        tipLeft.setOnClickListener(onClickListener);
        tipRight.setOnClickListener(onClickListener);
        changeMap.setOnClickListener(onClickListener);
        titleBar.setOnClickListener(onClickListener);
        scaleShowLayout.setOnClickListener(onClickListener);
        myLocation.setOnClickListener(onClickListener);

        return view;
    }

    private void initGestureDetector() {
        mDetector = new GestureDetector(getActivity(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                if (flag_scale == true) {
                    layoutScaleView.setVisibility(View.GONE);
                    layoutScaleView.setAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
                    flag_scale = false;
                }
                return true;
            }

            @Override
            public void onShowPress(MotionEvent e) {
            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //上滑
                if (e1.getY() - e2.getY() > FLIP_DISTANCE) {
                    wpbView.setVisibility(View.GONE);
                    return true;
                }
                //下滑
                if (e2.getY() - e1.getY() > FLIP_DISTANCE) {
                    wpbView.setVisibility(View.VISIBLE);
                    return true;
                }
                return false;
            }
        });
    }


    private void initDialView() {
        Long monthStart = CheckFormat.getTimeOfMonthStart();
        String start;
        BmobDate bmobCreatedAtDate = null;
        try {
            start = CheckFormat.longToString(monthStart, "yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date createdAtDate = sdf.parse(start);
            bmobCreatedAtDate = new BmobDate(createdAtDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String account = sp.getString("account", "");
        BmobQuery<RouteInfo> eq1 = new BmobQuery<>();
        eq1.addWhereEqualTo("account", account);
        BmobQuery<RouteInfo> eq2 = new BmobQuery<>();
        eq2.addWhereGreaterThanOrEqualTo("createdAt", bmobCreatedAtDate);
        List<BmobQuery<RouteInfo>> andQuerys = new ArrayList<>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);
        BmobQuery<RouteInfo> query = new BmobQuery<>();
        query.and(andQuerys);
        query.findObjects(new FindListener<RouteInfo>() {
            @Override
            public void done(List<RouteInfo> list, BmobException e) {
                if (e == null) {
                    double length = 0;
                    for (int i = 0; i < list.size(); i++) {
                        length = list.get(i).getLength() + length;
                    }
                    wpbView.refreshScore((int) length / 1000);
                } else {
                }
            }
        });
    }

    private void initScrollTextView() {
        List<String> scrollText = new ArrayList<>();
        scrollText.add("· 长按规划跑进入自由跑，自由跑不保存跑步数据!");
        scrollText.add("· 上滑隐藏月跑步计数!");
        scrollText.add("· 点击上方\"卉跑\"，选择跑步路径形状!");
        scrollText.add("· 点击下方蓝色地图，切换地图视图!");
        scrollText.add("· 点击下方红色直尺，选择跑步距离!");
        scrollText.add("· 点击下方黑色定位，进行定位!");
        scrollText.add("· 跑步记录右滑删除，删除后无法恢复!");
        scrollTextView.setList(scrollText);
        scrollTextView.startScroll();
    }

    private void initScaleView() {
        lengthScaleView.setRange(1, 9);//设置距离范围,两端值加起来为偶数
        lengthScaleView.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public void onValueChanged(float value) {
                SpannableString btn_text;
                btn_text = new SpannableString("规划跑 " + value + " km");
                btn_text.setSpan(new AbsoluteSizeSpan(18, true), 4, 4 + String.valueOf(value).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                btnRunStart.setText(btn_text);
                length = (int) value * 1000;
                if (value >= 7) {
                    zoom = 13;
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
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        aMap.moveCamera(CameraUpdateFactory.zoomTo(zoom));
        aMap.getUiSettings().setCompassEnabled(false);
        aMap.getUiSettings().setAllGesturesEnabled(true);//允许手势
        aMap.getUiSettings().setZoomControlsEnabled(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        btnRunStart.setEnabled(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
