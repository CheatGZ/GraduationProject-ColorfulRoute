package com.bupt.colorfulroute.runningapp.updateInfoActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.uicomponent.AlertMessage;
import com.bupt.colorfulroute.util.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdateGender extends BaseActivity {
    UpdateGender self = this;
    @BindView(R.id.back_button)
    ImageView backButton;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_button)
    ImageView rightButton;
    @BindView(R.id.none)
    RadioButton none;
    @BindView(R.id.male)
    RadioButton male;
    @BindView(R.id.female)
    RadioButton female;
    @BindView(R.id.other)
    RadioButton other;
    @BindView(R.id.gender_select_rg)
    RadioGroup genderSelectRg;
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;

    private UserInfo userInfo = new UserInfo();
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_layout:
                    finish();
                    break;
                case R.id.right_layout:
                    SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    String objectId = sp.getString("objectId", "");

                    if (!none.isChecked() && !male.isChecked() && !female.isChecked() && !other.isChecked()) {
                        alert(new AlertMessage("提交失败！", "请选择有效项！"));
                    } else {
                        userInfo.update(objectId, new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    finish();
                                } else {
                                    Toast.makeText(self, "提交失败，请重试！", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    };

    View.OnClickListener gender = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.none:
                    userInfo.setGender("n");
                    break;
                case R.id.male:
                    userInfo.setGender("m");
                    break;
                case R.id.female:
                    userInfo.setGender("f");
                    break;
                case R.id.other:
                    userInfo.setGender("o");
                    break;
                default:
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bmob.initialize(self, "e834b45389cad785bed5c43e2942b606");
        setContentView(R.layout.activity_update_gender);
        ButterKnife.bind(this);

        backButton.setBackgroundResource(R.mipmap.back);
        titleText.setText("性  别");
        rightButton.setImageResource(R.mipmap.confirm);
        leftLayout.setOnClickListener(onClickListener);
        rightLayout.setOnClickListener(onClickListener);

        none.setOnClickListener(gender);
        male.setOnClickListener(gender);
        female.setOnClickListener(gender);
        other.setOnClickListener(gender);
    }
}
