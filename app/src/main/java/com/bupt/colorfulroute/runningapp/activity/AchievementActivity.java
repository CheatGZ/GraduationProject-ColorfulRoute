package com.bupt.colorfulroute.runningapp.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.adapter.AchievementAdapter;
import com.bupt.colorfulroute.runningapp.entity.Achievement;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.uiutils.AchievementDialog;
import com.bupt.colorfulroute.util.BaseActivity;
import com.bupt.colorfulroute.util.RecyclerViewVelocity;
import com.bupt.colorfulroute.util.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * @author CheatGZ
 * @date 2019/3/23.
 * description：
 */
public class AchievementActivity extends BaseActivity {
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
    @BindView(R.id.achievement_rv)
    RecyclerView achievementRv;
    private TextView achievementShow;


    private String achievement;
    private AchievementDialog achievementDialog;
    private List<Achievement> list = new ArrayList<>();
    private int mPosition;//记录recycleerview点击的position
    private SharedPreferences sp;


    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String objectId = sp.getString("objectId", "");
            UserInfo userInfo = new UserInfo();
            switch (v.getId()) {
                case R.id.left_layout:
                    finish();
                    break;
                case R.id.btn_achievement_show:
                    userInfo.setTitle(achievementDialog.achTitle.getText().toString());
                    achievementShow.setText("当前徽章:  "+achievementDialog.achTitle.getText().toString());
                    userInfo.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                            } else {
                                ToastUtil.show(getApplicationContext(), "提交失败，请重试！");
                            }
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "e834b45389cad785bed5c43e2942b606");
        setContentView(R.layout.activity_achievement);
        ButterKnife.bind(this);
        achievementShow=findViewById(R.id.achievement_show);

        titleText.setText("运动徽章");
        backButton.setBackgroundResource(R.mipmap.back);
        leftLayout.setOnClickListener(onClickListener);

        initAchievement();
    }

    private void initAchievement() {
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String objectId = sp.getString("objectId", "");
        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<UserInfo>() {
            @Override
            public void done(UserInfo userInfo, BmobException e) {
                if (e == null) {
                    list.clear();
                    list.addAll(userInfo.getAchievement());
                    achievement = userInfo.getTitle();
                    achievementShow.setText("当前徽章:  "+achievement);

                    //适配器
                    AchievementAdapter achievementAdapter = new AchievementAdapter<Achievement>(list) {
                        @Override
                        public int getLayoutId(int viewType) {
                            return R.layout.layout_achievement_item;
                        }

                        @Override
                        public void convert(VH holder, Achievement data, int position) {
                            holder.setText(R.id.achievement_title, list.get(position).getTitle());
                            holder.setText(R.id.condition_text, list.get(position).getCondition());
                            holder.setTime(R.id.time_achieved, list.get(position).getTimeAchieved());
                            if (list.get(position).getTimeAchieved() == 0) {
                                holder.setUnShow(R.id.achievement_image,
                                        R.id.achievement_title,
                                        R.id.achievement_show_layout,
                                        false);
                            } else {
                                holder.setImage(R.id.achievement_image, position);
                            }
                        }

                    };
                    achievementAdapter.setOnItemClickListener(new AchievementAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            mPosition = position;
                            showAchievementDialog();
                        }
                    });
                    RecyclerViewVelocity.setMaxFlingVelocity(achievementRv,3000);
                    achievementRv.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
                    achievementRv.setAdapter(achievementAdapter);
                } else {
                    System.out.println("Bmob error" + e);
                }
            }
        });
    }

    public void showAchievementDialog() {
        achievementDialog = new AchievementDialog(this, onClickListener, list.get(mPosition), mPosition);
        achievementDialog.show();
    }
}
