package com.bupt.colorfulroute.runningapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.activity.AnalysisActivity;
import com.bupt.colorfulroute.runningapp.activity.HistoryDetailActivity;
import com.bupt.colorfulroute.runningapp.activity.MainActivity;
import com.bupt.colorfulroute.runningapp.adapter.HistoryAdapter;
import com.bupt.colorfulroute.runningapp.adapter.RecycleItemTouchHelper;
import com.bupt.colorfulroute.runningapp.entity.RouteInfo;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.uicomponent.overFlyingView.OverFlyingLayoutManager;
import com.bupt.colorfulroute.util.OnMultiClickListener;
import com.bupt.colorfulroute.util.RecyclerViewVelocity;
import com.bupt.colorfulroute.util.TopSmoothScroller;

import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class
HistoryFragment extends Fragment {

    public SwipeRefreshLayout refreshLayout;
    @BindView(R.id.back_button)
    ImageView backButton;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_button)
    ImageView rightButton;
    @BindView(R.id.history_all)
    LinearLayout historyAll;
    Unbinder unbinder;
    HistoryAdapter mAdapter;
    @BindView(R.id.layout_back_to_top)
    LinearLayout layoutBackToTop;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;
    private TextView length;
    private TextView time;
    private TextView number;
    private RecyclerView itemView;
    private UserInfo userInfo1 = null;
    private List<RouteInfo> routeList = new ArrayList<>();
    private Message msg = null;
    private OverFlyingLayoutManager layoutManager;
    private Boolean flag = false;//标识顶部按钮是否可见，初始不可见
    //线程使用的handler  创建一个线程来进行实时显示总公里数
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    //规范输出形式，包括小数点，以及单位字体的大小
                    SpannableString km, num, stime;
                    double h, m;
                    DecimalFormat df = new DecimalFormat("#.##");
                    h = (double) (userInfo1.getTotalTime() / 1000 / 3600 % 24);
                    m = (double) (userInfo1.getTotalTime() / 1000 / 60 % 60);
                    km = new SpannableString(df.format(userInfo1.getTotalLength() / 1000) + "公里");
                    num = new SpannableString(userInfo1.getNumber() + "次");
                    stime = new SpannableString(df.format(h + m / 60) + "小时");
                    km.setSpan(new AbsoluteSizeSpan(11, true), df.format(userInfo1.getTotalLength() / 1000).length(), df.format(userInfo1.getTotalLength() / 1000).length() + 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    num.setSpan(new AbsoluteSizeSpan(11, true), userInfo1.getNumber().toString().length(), userInfo1.getNumber().toString().length() + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    stime.setSpan(new AbsoluteSizeSpan(11, true), df.format(h + m / 60).length(), df.format(h + m / 60).length() + 2, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    length.setText(km);
                    number.setText(num);
                    time.setText(stime);
                    break;
            }
        }
    };

    private OnMultiClickListener onMultiClickListener = new OnMultiClickListener() {
        @Override
        public void onMultiClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.right_layout:
                    ((MainActivity) getActivity()).changeFragment(1);
                    break;
                case R.id.layout_back_to_top:
                    if (flag) {
                        LinearSmoothScroller smoothScroller = new TopSmoothScroller(getActivity());
                        smoothScroller.setTargetPosition(0);
                        layoutManager.startSmoothScroll(smoothScroller);
                        layoutBackToTop.setVisibility(View.GONE);
                        layoutBackToTop.setAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
                        flag = false;
                    }
                    break;
                case R.id.history_all:
                    intent = new Intent(getActivity(), AnalysisActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };

    public static HistoryFragment newInstance() {
        HistoryFragment fragment = new HistoryFragment();
        return fragment;

    }

    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bmob.initialize(getActivity(), "e834b45389cad785bed5c43e2942b606");
        View view = inflater.inflate(R.layout.fragment_history, container, false);
        unbinder = ButterKnife.bind(this, view);

        refreshLayout = view.findViewById(R.id.refresh_layout);
        length = view.findViewById(R.id.length_all);
        time = view.findViewById(R.id.time_all);
        number = view.findViewById(R.id.number_all);
        itemView = view.findViewById(R.id.history_list_view);
        titleText.setText("足  迹");
        rightButton.setBackgroundResource(R.mipmap.back_right);

        layoutBackToTop.setOnClickListener(onMultiClickListener);
        historyAll.setOnClickListener(onMultiClickListener);
        rightLayout.setOnClickListener(onMultiClickListener);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        updateHistoryData();
                        updateStatisticData();
                        refreshLayout.setRefreshing(false);
                    }
                }, 700);
            }
        });

        mAdapter = new HistoryAdapter<RouteInfo>(routeList) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.layout_history_item;
            }

            @Override
            public void convert(VH holder, RouteInfo data, int position) {
                holder.setText(R.id.StartDateText, routeList.get(position).getStartTime().substring(0, 10));
                holder.setText(R.id.StartTimeText, routeList.get(position).getStartTime().substring(11, 19));
                holder.setText(R.id.RouteLengthText, (routeList.get(position).getLength() / 1000) + " km");
                holder.setText(R.id.flag_click, position + 1 + "");
                holder.setImage(R.id.flag_click, position);
            }

            @Override
            public void onItemDelete(final int positon) {
                RouteInfo routeInfo = new RouteInfo();
                routeInfo.setObjectId(routeList.get(positon).getObjectId());
                routeInfo.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                    }
                });

                SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                String objectId = sp.getString("objectId", "");
                UserInfo userInfo = new UserInfo();
                userInfo.setNumber(userInfo1.getNumber() - 1);
                userInfo.setTotalLength(userInfo1.getTotalLength() - routeList.get(positon).getLength());
                userInfo.setTotalTime(userInfo1.getTotalTime() - routeList.get(positon).getTime());
                userInfo.update(objectId, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                    }
                });
                //删除后更新UI和数据
                updateHistoryData();
                updateStatisticData();
                super.onItemDelete(positon);
            }
        };
        mAdapter.setOnItemClickListener(new HistoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), HistoryDetailActivity.class);
                intent.putExtra("objectId", routeList.get(position).getObjectId());
                startActivity(intent);
            }
        });
        layoutManager = new OverFlyingLayoutManager(OrientationHelper.VERTICAL, false);
        itemView.setLayoutManager(layoutManager);
        itemView.setAdapter(mAdapter);
        RecyclerViewVelocity.setMaxFlingVelocity(itemView, 2000);
        //实现滑动删除
        ItemTouchHelper.Callback callback = new RecycleItemTouchHelper(getContext(), mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(itemView);


        updateStatisticData();
        updateHistoryData();
        showBtnBackToTop();
        return view;
    }

    private void showBtnBackToTop() {
        itemView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //根据下移的距离(offset)判断是否先睡回到顶部按钮
                if (layoutManager.index > 200) {
                    if (!flag) {
                        layoutBackToTop.setVisibility(View.VISIBLE);
                        layoutBackToTop.setAnimation(AnimationUtils.makeInAnimation(getContext(), false));
                        flag = true;
                    }
                } else {
                    if (flag) {
                        layoutBackToTop.setVisibility(View.GONE);
                        layoutBackToTop.setAnimation(AnimationUtils.makeOutAnimation(getContext(), true));
                        flag = false;
                    }
                }
            }
        });
    }

    private void updateHistoryData() {
        //将当前账号写入sharedpreference
        SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String account = sp.getString("account", "");
        BmobQuery<RouteInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account", account);
        bmobQuery.setLimit(20);
        bmobQuery.order("-startTime");
        bmobQuery.findObjects(new FindListener<RouteInfo>() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void done(List<RouteInfo> list, BmobException e) {
                if (e == null) {
                    routeList.clear();
                    routeList.addAll(list);
                    if (routeList.size() > 0) {
                        if (Build.VERSION.SDK_INT >= 23) {
                            itemView.setForeground(getActivity().getResources().getDrawable(R.drawable.grdient_primary_light_bottom, null));
                        } else {
                            itemView.setBackgroundResource(R.drawable.grdient_primary_light_bottom);
                        }
                    } else {
                        if (Build.VERSION.SDK_INT >= 23) {
                            itemView.setForeground(getActivity().getResources().getDrawable(R.mipmap.history_placeholder, null));
                        } else {
                            itemView.setBackgroundResource(R.mipmap.history_placeholder);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                } else {
                    System.out.println("bmob fail!");
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }

    @Override
    public void onResume() {
        super.onResume();
        updateStatisticData();
    }

    private void updateStatisticData() {
        SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String objectId = sp.getString("objectId", "");
        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<UserInfo>() {
            @Override
            public void done(UserInfo userInfo, BmobException e) {
                if (e == null) {
                    userInfo1 = userInfo;
                    msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
