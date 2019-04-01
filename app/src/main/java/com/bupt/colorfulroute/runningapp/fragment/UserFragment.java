package com.bupt.colorfulroute.runningapp.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.activity.AchievementActivity;
import com.bupt.colorfulroute.runningapp.activity.LoginActivity;
import com.bupt.colorfulroute.runningapp.activity.MainActivity;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.uiutils.DescriptionDialog;
import com.bupt.colorfulroute.runningapp.uiutils.LogoutDialog;
import com.bupt.colorfulroute.runningapp.updateInfoActivity.UpdateInfoActivity;
import com.bupt.colorfulroute.util.AppVersion;
import com.bupt.colorfulroute.util.ShowKeyBoard;
import com.bupt.colorfulroute.util.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;

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
    @BindView(R.id.image_achievement_show)
    ImageView imageAchievementShow;
    @BindView(R.id.title_bar)
    LinearLayout titleBar;
    @BindView(R.id.view_one)
    View viewOne;
    Unbinder unbinder;
    SharedPreferences sp;
    SimpleDraweeView avatarView;
    TextView descriptionText;
    TextView userNameText;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;
    @BindView(R.id.version_code)
    TextView versionCode;
    @BindView(R.id.version_name)
    TextView versionName;


    private Bitmap avatar;
    private String name;
    private String icon;//微博头像地址
    private String description;//简介
    private DescriptionDialog descriptionDialog;
    private LogoutDialog logoutDialog;
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
                case R.id.avatar_view:
                    Intent intent = new Intent(Intent.ACTION_PICK, null);
                    intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                    startActivityForResult(intent, GET_PIC_FROM_PHOTOS);
                    break;
                case R.id.layout_info:
                    startActivity(new Intent(getContext(), UpdateInfoActivity.class));
                    break;
                case R.id.layout_achievement:
                    Intent intent1 = new Intent(getActivity(), AchievementActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.description_edit:
                    showEditDialog();
                    break;
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
                case R.id.right_layout:
                    showLogoutDialog();
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
                default:
                    break;
            }
        }
    };

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
        unbinder = ButterKnife.bind(this, view);

        titleText.setText("个人中心");
        rightButton.setBackgroundResource(R.mipmap.log_out);
        versionCode.setText("Version "+AppVersion.packageName(getActivity()));
        versionName.setText("卉跑 RunRoute");

        avatarView.setClickable(false);
//        avatarView.setOnClickListener(onClickListener);
        descriptionEdit.setOnClickListener(onClickListener);
        layoutInfo.setOnClickListener(onClickListener);
        rightLayout.setOnClickListener(onClickListener);
        layoutAchievement.setOnClickListener(onClickListener);
        initUserinfo();
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
                    msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    public void showLogoutDialog() {
        logoutDialog = new LogoutDialog(getActivity(), onClickListener);
        logoutDialog.getWindow().setDimAmount(0.4f);
        logoutDialog.show();
    }

    public void showEditDialog() {
        descriptionDialog = new DescriptionDialog(getActivity(), onClickListener);
        descriptionDialog.show();
        descriptionDialog.descriptionEditText.setText(descriptionText.getText().toString().substring(3));
        descriptionDialog.descriptionEditText.setSelection(descriptionText.getText().toString().substring(3).length());
        ShowKeyBoard.delayShowSoftKeyBoard(descriptionDialog.descriptionEditText);
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
