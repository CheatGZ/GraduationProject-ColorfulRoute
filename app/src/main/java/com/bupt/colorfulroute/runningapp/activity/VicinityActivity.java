package com.bupt.colorfulroute.runningapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.adapter.VicinityAdapter;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.runnerlib.LatLngCalculate;
import com.bupt.colorfulroute.util.BaseActivity;
import com.bupt.colorfulroute.util.OnMultiClickListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * @author CheatGZ
 * @date 2019/4/12.
 * description：
 */
public class VicinityActivity extends BaseActivity {
    public DecimalFormat df = new DecimalFormat("#.##");
    public AMapLocation mapLocation = null;
    public AMapLocationClient aMapLocationClient;
    public AMapLocationListener aMapLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            mapLocation = aMapLocation;
        }
    };
    public AMapLocationClientOption aMapLocationClientOption = null;
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
    @BindView(R.id.title_bar)
    LinearLayout titleBar;
    @BindView(R.id.btn_vicinity)
    RadioButton btnVicinity;
    @BindView(R.id.btn_online)
    RadioButton btnOnline;
    private RecyclerView rvVicinity;
    private VicinityAdapter vicinityAdapter;
    private List<UserInfo> userList = new ArrayList<>();
    private OnMultiClickListener onMultiClickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            switch (v.getId()) {
                case R.id.left_layout:
                    finish();
                    break;
            }
        }
    };
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_vicinity:
                    updateVicinitybyLength();
                    break;
                case R.id.btn_online:
                    updateVicinitybyTime();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vicinity);
        ButterKnife.bind(this);
        rvVicinity = findViewById(R.id.rv_vicinity);
        backButton.setBackgroundResource(R.mipmap.back);
        titleText.setText("附近的人");
        leftLayout.setOnClickListener(onMultiClickListener);

        vicinityAdapter = new VicinityAdapter<UserInfo>(userList) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.layout_vicinity_item;
            }

            @Override
            public void convert(VH holder, UserInfo data, int position) {
                holder.setIcon(R.id.user_icon, userList.get(position).getIcon());
                holder.setText(R.id.user_name, userList.get(position).getName());
                if (userList.get(position).getDescription().length() != 0) {
                    holder.setText(R.id.user_title, "徽章：" + userList.get(position).getTitle());
                }
                if (userList.get(position).getDescription().length() != 0) {
                    holder.setText(R.id.user_description, userList.get(position).getDescription());
                }
                double d, h, m;
                Long logInTime;
                logInTime = System.currentTimeMillis() - userList.get(position).getLogInTime();
                d = (double) (logInTime / 1000 / 3600 / 24);
                h = (double) (logInTime / 1000 / 3600 % 24);
                m = (double) (logInTime / 1000 / 60 % 60);
                if (d == 0) {
                    if (h == 0) {
                        holder.setText(R.id.user_time, m + "分前");
                    } else {
                        holder.setText(R.id.user_time, h + "小时前");
                    }
                } else {
                    holder.setText(R.id.user_time, d + "天前");
                }
                double length;
                length = LatLngCalculate.getDistance(aMapLocationClient.getLastKnownLocation().getLatitude()
                        , userList.get(position).getLocation().get(0)
                        , aMapLocationClient.getLastKnownLocation().getLongitude()
                        , userList.get(position).getLocation().get(1));
                holder.setText(R.id.user_location, df.format(length / 100000000) + "km");

            }
        };
        rvVicinity.setLayoutManager(new LinearLayoutManager(this));
        rvVicinity.setAdapter(vicinityAdapter);


        //开启定位
        aMapLocationClient = new AMapLocationClient(this);
        aMapLocationClient.setLocationListener(aMapLocationListener);
        aMapLocationClientOption = new AMapLocationClientOption();
        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClient.setLocationOption(aMapLocationClientOption);
        aMapLocationClient.startLocation();

        updateVicinitybyLength();

        btnVicinity.setOnClickListener(onClickListener);
        btnOnline.setOnClickListener(onClickListener);
    }

    private void updateVicinitybyLength() {
        btnVicinity.setChecked(true);
        btnOnline.setChecked(false);

        SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String objectId = sp.getString("objectId", "");
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.addWhereNotEqualTo("objectId", objectId);

        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    userList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getLocation().get(0) - aMapLocationClient.getLastKnownLocation().getLatitude() < 0.1 && list.get(i).getLocation().get(1) - aMapLocationClient.getLastKnownLocation().getLongitude() < 0.1) {
                            userList.add(list.get(i));
                        }
                    }
                    vicinityAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void updateVicinitybyTime() {
        btnVicinity.setChecked(false);
        btnOnline.setChecked(true);
        SharedPreferences sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String objectId = sp.getString("objectId", "");
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.addWhereNotEqualTo("objectId", objectId);

        query.order("-logInTime");
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    userList.clear();
                    for (int i = 0; i < list.size(); i++) {
                        if (System.currentTimeMillis() - list.get(i).getLogInTime() <= 3600000)
                            userList.add(list.get(i));
                    }
                    vicinityAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        aMapLocationClient.stopLocation();
        aMapLocationClient.onDestroy();
    }
}
