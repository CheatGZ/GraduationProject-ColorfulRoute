package com.bupt.colorfulroute.runningapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.activity.LoginActivity;
import com.bupt.colorfulroute.runningapp.activity.MainActivity;
import com.bupt.colorfulroute.runningapp.adapter.AchievementAdapter;
import com.bupt.colorfulroute.runningapp.entity.Achievement;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.uiutils.AchievementDialog;
import com.bupt.colorfulroute.runningapp.uiutils.DescriptionDialog;
import com.bupt.colorfulroute.runningapp.uiutils.LogoutDialog;
import com.bupt.colorfulroute.runningapp.uiutils.ScreenUtils;
import com.bupt.colorfulroute.runningapp.uiutils.ShadowDrawable;
import com.bupt.colorfulroute.runningapp.uiutils.StatusBarUtils;
import com.bupt.colorfulroute.runningapp.updateInfoActivity.UpdateInfoActivity;
import com.bupt.colorfulroute.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;

public class UserFragment extends Fragment {

    private static final int GET_PIC_FROM_PHOTOS = 1;
    private static final int CROP_PHOTO = 2;
    @BindView(R.id.back_button)
    ImageView backButton;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_button)
    ImageView rightButton;
    @BindView(R.id.description_edit)
    ImageView descriptionEdit;
    @BindView(R.id.description_layout)
    LinearLayout descriptionLayout;
    @BindView(R.id.info_text)
    LinearLayout infoText;
    @BindView(R.id.person_info_rel)
    RelativeLayout personInfoRel;
    @BindView(R.id.layout_info)
    LinearLayout layoutInfo;
    @BindView(R.id.layout_achievement)
    LinearLayout layoutAchievement;

    SimpleDraweeView avatarView;
    TextView descriptionText;
    TextView userNameText;
    TextView achievementShow;

    @BindView(R.id.image_achievement_show)
    ImageView imageAchievementShow;
    @BindView(R.id.achievement_rv)
    RecyclerView achievementRv;
    SharedPreferences sp;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.title_bar)
    LinearLayout titleBar;
    @BindView(R.id.view_one)
    View viewOne;
    Unbinder unbinder;


    private Bitmap avatar;
    private String name;
    private String icon;//微博头像地址
    private String description;//简介
    private String achievement;
    private DescriptionDialog descriptionDialog;
    private LogoutDialog logoutDialog;
    private AchievementDialog achievementDialog;
    private boolean flag_achievement = false;//判断成就信息是显示true/隐藏false
    private Message msg = null;


    //线程使用的handler  创建一个线程来进行实时显示
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (name.equals(null)) {
                        userNameText.setText("未设置");
                    } else {
                        userNameText.setText(name);
                    }
                    if (avatarView.equals(null)) {
                        Uri uri = Uri.parse("res://com.bupt.colorfulroute/" + getActivity().getResources().getResourceName(R.mipmap.no_icon));
                        avatarView.setImageURI(uri);
                    } else {
                        avatarView.setImageURI(icon);
                    }
                    descriptionText.setText("简介:" + description);
                    break;
            }
        }
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            String objectId = sp.getString("objectId", "");
            UserInfo userInfo = new UserInfo();
            switch (v.getId()) {
                case R.id.layout_info:
                    startActivity(new Intent(getContext(), UpdateInfoActivity.class));
                    break;
                case R.id.avatar_view:
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, GET_PIC_FROM_PHOTOS);
                    break;
                case R.id.right_button:
                    showLogoutDialog();
                    break;
                case R.id.description_layout:
                    showEditDialog();
                    break;
//                case R.id.achievement_item:
//                    showAchievementDialog();
//                    break;
                case R.id.btn_save_pop:
                    userInfo.setDescription(descriptionDialog.descriptionEditText.getText().toString());
                    userInfo.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                descriptionDialog.dismiss();
                                initUserinfo();
                            } else {
                                ToastUtil.show(getActivity(), "提交失败，请重试！");
                            }
                        }
                    });
                    break;
                case R.id.btn_app_out:
                    MainActivity mainActivity = new MainActivity();
                    mainActivity.removeActivity();
                    break;
                case R.id.btn_logout:
                    //将当前sharedPreference中的账号信息清除并返回登录界面
                    SharedPreferences.Editor editor = sp.edit();//获取编辑器
                    editor.clear();
                    editor.apply();//提交修改
                    startActivity(new Intent(getContext(), LoginActivity.class));
                    getActivity().finish();
                    break;
                case R.id.layout_achievement:
                    if (!flag_achievement) {
                        imageAchievementShow.setBackgroundResource(R.mipmap.achievement_show);
                        personInfoRel.setVisibility(View.GONE);
                        layoutInfo.setVisibility(View.GONE);
                        divider.setVisibility(View.GONE);
                        viewOne.setVisibility(View.GONE);
                        achievementRv.setVisibility(View.VISIBLE);
                        titleBar.setClickable(true);
                        achievementShow.setText("佩戴徽章: " + achievement);
                        flag_achievement = true;
                    } else {
                        imageAchievementShow.setBackgroundResource(R.mipmap.go);
                        personInfoRel.setVisibility(View.VISIBLE);
                        layoutInfo.setVisibility(View.VISIBLE);
                        divider.setVisibility(View.VISIBLE);
                        viewOne.setVisibility(View.VISIBLE);
                        achievementRv.setVisibility(View.INVISIBLE);
                        achievementShow.setText("运动徽章");
                        titleBar.setClickable(false);
                        flag_achievement = false;
                    }
                    break;
                case R.id.btn_achievement_show:
                    userInfo.setTitle(achievementDialog.achTitle.getText().toString());
                    userInfo.update(objectId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                descriptionDialog.dismiss();
                                initUserinfo();
                            } else {
                                ToastUtil.show(getActivity(), "提交失败，请重试！");
                            }
                        }
                    });
                    break;
                case R.id.title_bar:
                    imageAchievementShow.setBackgroundResource(R.mipmap.go);
                    personInfoRel.setVisibility(View.VISIBLE);
                    layoutInfo.setVisibility(View.VISIBLE);
                    divider.setVisibility(View.VISIBLE);
                    achievementRv.setVisibility(View.INVISIBLE);
                    achievementShow.setText("成就");
                    titleBar.setClickable(false);
                    layoutAchievement.setBackgroundColor(getActivity().getResources().getColor(R.color.icons));
                    flag_achievement = false;
                    break;
                default:
                    break;
            }
        }
    };
    private List<Achievement> list = new ArrayList<>();
    private int mPosition;//记录recycleerview点击的position

    public static UserFragment newInstance() {
        UserFragment fragment = new UserFragment();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bmob.initialize(getContext(), "e834b45389cad785bed5c43e2942b606");
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        avatarView = view.findViewById(R.id.avatar_view);
        descriptionText = view.findViewById(R.id.description_text);
        userNameText = view.findViewById(R.id.user_name_text);
        achievementShow = view.findViewById(R.id.achievement_show);
        unbinder = ButterKnife.bind(this, view);

//        StatusBarUtils.setStatusBarColor(getActivity(), Color.TRANSPARENT,false);
        titleText.setText("个人中心");
        rightButton.setBackgroundResource(R.mipmap.log_out);

//        ShadowDrawable.setShadowDrawable(personInfoRel, ShadowDrawable.SHAPE_ROUND, 0xffffff, ScreenUtils.dp2px(getContext(), 6)
//                , 0x66305CDD, ScreenUtils.dp2px(getContext(), 8), ScreenUtils.dp2px(getContext(), 0), ScreenUtils.dp2px(getContext(), 3));

        //适配器
        AchievementAdapter achievementAdapter = new AchievementAdapter<Achievement>(list) {
            @Override
            public int getLayoutId(int viewType) {
                return R.layout.layout_achievement_item;
            }

            @Override
            public void convert(VH holder, Achievement data, int position) {
                holder.setImage(R.id.achievement_image, list.get(position).getIcon());
                holder.setText(R.id.achievement_title, list.get(position).getTitle());
                holder.setText(R.id.condition_text, list.get(position).getCondition());
                holder.setTime(R.id.time_achieved, list.get(position).getTimeAchieved());
                if (list.get(position).getTimeAchieved() == 0)
                    holder.setUnShow(R.id.achievement_image,
                            R.id.achievement_title,
                            R.id.achievement_show_layout,
                            R.id.achievement_item,
                            list.get(position).getUnIcon(),
                            false);
            }

        };
        achievementAdapter.setOnItemClickListener(new AchievementAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mPosition = position;
                showAchievementDialog();
            }
        });
        achievementRv.setLayoutManager(new LinearLayoutManager(getContext()));
        achievementRv.setAdapter(achievementAdapter);


        titleBar.setClickable(false);
        avatarView.setClickable(false);
        titleBar.setOnClickListener(onClickListener);
//        avatarView.setOnClickListener(onClickListener);
        descriptionLayout.setOnClickListener(onClickListener);
        layoutInfo.setOnClickListener(onClickListener);
        rightButton.setOnClickListener(onClickListener);
        layoutAchievement.setOnClickListener(onClickListener);
        initUserinfo();

        initAchievement();

        return view;
    }

    private void initUserinfo() {
        sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String objectId = sp.getString("objectId", "");
        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<UserInfo>() {
            @Override
            public void done(UserInfo userInfo, BmobException e) {
                if (e == null) {
                    name = userInfo.getName();
                    icon = userInfo.getIcon();
                    description = userInfo.getDescription();
                    achievement = userInfo.getTitle();
                    msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    private void initAchievement() {
        sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String objectId = sp.getString("objectId", "");
        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(objectId, new QueryListener<UserInfo>() {
            @Override
            public void done(UserInfo userInfo, BmobException e) {
                if (e == null) {
                    list.clear();
                    list.addAll(userInfo.getAchievement());
                } else {
                    System.out.println("Bmob error" + e);
                }
            }
        });
    }

    public void showLogoutDialog() {
        logoutDialog = new LogoutDialog(getActivity(), onClickListener);
        logoutDialog.getWindow().setDimAmount(0.4f);
        logoutDialog.show();
    }

    public void showAchievementDialog() {

        //TODO 实时监测后台成就信息，并返回前端
        achievementDialog = new AchievementDialog(getActivity(), onClickListener, list.get(mPosition), mPosition);
        achievementDialog.show();
    }

    public void showEditDialog() {
        descriptionDialog = new DescriptionDialog(getActivity(), onClickListener);
        descriptionDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onResume() {
        super.onResume();
        initUserinfo();
    }
}
