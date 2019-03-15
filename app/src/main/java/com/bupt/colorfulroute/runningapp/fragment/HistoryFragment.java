package com.bupt.colorfulroute.runningapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.activity.HistoryDetailActivity;
import com.bupt.colorfulroute.runningapp.adapter.HistoryAdapter;
import com.bupt.colorfulroute.runningapp.adapter.RecycleItemTouchHelper;
import com.bupt.colorfulroute.runningapp.entity.RouteInfo;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.uiutils.ScreenUtils;
import com.bupt.colorfulroute.runningapp.uiutils.ShadowDrawable;
import com.bupt.colorfulroute.runningapp.uiutils.StatusBarUtils;
import com.bupt.colorfulroute.util.CheckFormat;

import org.jetbrains.annotations.NotNull;

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
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    private TextView length;
    private TextView time;
    private RecyclerView itemView;
    private UserInfo userInfo1 = null;
    private List<RouteInfo> routeList = new ArrayList<>();
    private Message msg = null;
    //线程使用的handler  创建一个线程来进行实时显示总公里数
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    length.setText((userInfo1.getTotalLength() / 1000) + " km");
                    time.setText(CheckFormat.timeFormat(userInfo1.getTotalTime()) + "");
                    if (routeList.size() > 0) {
                        itemView.setBackgroundResource(R.color.icons);
                    }
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

//        StatusBarUtils.setStatusBarColor(getActivity(), Color.TRANSPARENT,false);
        length = view.findViewById(R.id.length_all);
        time = view.findViewById(R.id.time_all);
        itemView = view.findViewById(R.id.history_list_view);
        titleText.setText("足  迹");

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        updateHistoryData();
                        refreshLayout.setRefreshing(false);
                    }
                }, 1500);
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
            }

            @Override
            public void onItemDelete(final int positon) {

                RouteInfo routeInfo = new RouteInfo();
                routeInfo.setObjectId(routeList.get(positon).getObjectId());
                routeInfo.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.d("Cheat delete", "删除成功" + routeList.get(positon).getObjectId());
                        }
                    }
                });
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
        itemView.setLayoutManager(new LinearLayoutManager(getContext()));
        itemView.setAdapter(mAdapter);
        //实现滑动删除
        ItemTouchHelper.Callback callback = new RecycleItemTouchHelper(getContext(), mAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(itemView);
        //TODO 云端数据修改

        updateStatisticData();
        updateHistoryData();

//        ShadowDrawable.setShadowDrawable(historyAll, ShadowDrawable.SHAPE_ROUND, 0xffffff, ScreenUtils.dp2px(getContext(), 6)
//                , 0x66305CDD, ScreenUtils.dp2px(getContext(), 8), ScreenUtils.dp2px(getContext(), 0), ScreenUtils.dp2px(getContext(), 3));

        return view;
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
            @Override
            public void done(List<RouteInfo> list, BmobException e) {
                if (e == null) {
                    routeList.clear();
                    routeList.addAll(list);
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
