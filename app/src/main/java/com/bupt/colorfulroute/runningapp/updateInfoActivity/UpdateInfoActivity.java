package com.bupt.colorfulroute.runningapp.updateInfoActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.util.BaseActivity;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.uiutils.StatusBarUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UpdateInfoActivity extends BaseActivity {
    UpdateInfoActivity self = this;

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.left_layout:
                    finish();
                    break;
                case R.id.name_layout:
                    intent = new Intent(self, UpdateName.class);
                    startActivity(intent);
                    break;
                case R.id.gender_layout:
                    intent = new Intent(self, UpdateGender.class);
                    startActivity(intent);
                    break;
                case R.id.age_layout:
                    intent = new Intent(self, UpdateAge.class);
                    startActivity(intent);
                    break;
                case R.id.height_layout:
                    intent = new Intent(self, UpdateHeight.class);
                    startActivity(intent);
                    break;
                case R.id.weight_layout:
                    intent = new Intent(self, UpdateWeight.class);
                    startActivity(intent);
                    break;
                case R.id.email_layout:
                    intent = new Intent(self, UpdateEmail.class);
                    startActivity(intent);
                    break;
                case R.id.phone_layout:
                    intent = new Intent(self, UpdatePhone.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    };
    @BindView(R.id.back_button)
    ImageView backButton;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_button)
    ImageView rightButton;
    @BindView(R.id.name_text)
    TextView nameText;
    @BindView(R.id.name_layout)
    LinearLayout nameLayout;
    @BindView(R.id.gender_text)
    TextView genderText;
    @BindView(R.id.gender_layout)
    LinearLayout genderLayout;
    @BindView(R.id.age_text)
    TextView ageText;
    @BindView(R.id.age_layout)
    LinearLayout ageLayout;
    @BindView(R.id.height_text)
    TextView heightText;
    @BindView(R.id.height_layout)
    LinearLayout heightLayout;
    @BindView(R.id.weight_text)
    TextView weightText;
    @BindView(R.id.weight_layout)
    LinearLayout weightLayout;
    @BindView(R.id.email_text)
    TextView emailText;
    @BindView(R.id.email_layout)
    LinearLayout emailLayout;
    @BindView(R.id.phone_text)
    TextView phoneText;
    @BindView(R.id.phone_layout)
    LinearLayout phoneLayout;
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;

    private Message msg = null;
    private UserInfo userInfo = null;
    //线程使用的handler  创建一个线程来进行实时显示总公里数
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @SuppressLint("SetTextI18n")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if(!nameText.equals(null)){
                        nameText.setText(userInfo.getName());
                    }
                    if (userInfo.getGender().equals("f")) {
                        genderText.setText("女");
                    } else if (userInfo.getGender().equals("m")) {
                        genderText.setText("男");
                    } else if (userInfo.getGender().equals("o")) {
                        genderText.setText("其他");
                    } else if (userInfo.getGender().equals("n")) {
                        genderText.setText("保密");
                    }
                    if (userInfo.getAge() != 0) {
                        ageText.setText(userInfo.getAge() + "");
                    }
                    if (userInfo.getHeight() != 0) {
                        heightText.setText(userInfo.getHeight() + "");
                    }
                    if (userInfo.getWeight() != 0) {
                        weightText.setText(userInfo.getWeight() + "");
                    }
                    if (userInfo.getEmail().length() > 0) {
                        emailText.setText(userInfo.getEmail());
                    }
                    if (userInfo.getPhone().length() > 0) {
                        phoneText.setText(userInfo.getPhone());
                    }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(self, "e834b45389cad785bed5c43e2942b606");
        setContentView(R.layout.activity_update_info);
        ButterKnife.bind(this);

        backButton.setBackgroundResource(R.mipmap.back);
        titleText.setText("个人信息");
        //加载数据
        initUserInfo();

        //设置监听事件
        leftLayout.setOnClickListener(onClickListener);
        nameLayout.setOnClickListener(onClickListener);
        genderLayout.setOnClickListener(onClickListener);
        ageLayout.setOnClickListener(onClickListener);
        heightLayout.setOnClickListener(onClickListener);
        weightLayout.setOnClickListener(onClickListener);
        emailLayout.setOnClickListener(onClickListener);
        phoneLayout.setOnClickListener(onClickListener);
    }

    private void initUserInfo() {
        SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String account = sp.getString("account", "");
        BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
        bmobQuery.addWhereEqualTo("account", account);
        bmobQuery.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if (e == null) {
                    userInfo = list.get(0);
                    msg = new Message();
                    msg.what = 1;
                    handler.sendMessage(msg);
                } else {
//                    Toast.makeText(getContext(), "网络似乎出了点问题，无法加载数据！",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        initUserInfo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initUserInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initUserInfo();
    }
}
