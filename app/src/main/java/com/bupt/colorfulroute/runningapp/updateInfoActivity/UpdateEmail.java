package com.bupt.colorfulroute.runningapp.updateInfoActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.util.BaseActivity;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.uicomponent.AlertMessage;
import com.bupt.colorfulroute.runningapp.uiutils.StatusBarUtils;
import com.bupt.colorfulroute.util.CheckFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdateEmail extends BaseActivity {
    UpdateEmail self = this;


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_layout:
                    finish();
                    break;
                case R.id.right_layout:
                    if (inputText.getText().toString().equals("") || !CheckFormat.isEmail(inputText.getText().toString())) {
                        alert(new AlertMessage("请输入有效值！", ""));
                    } else {
                        SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        String objectId = sp.getString("objectId", "");
                        UserInfo userInfo = new UserInfo();
                        userInfo.setEmail(inputText.getText().toString());
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
    @BindView(R.id.back_button)
    ImageView backButton;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_button)
    ImageView rightButton;
    @BindView(R.id.input_text)
    EditText inputText;
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bmob.initialize(self, "e834b45389cad785bed5c43e2942b606");
        setContentView(R.layout.activity_update_email);
        ButterKnife.bind(this);

        backButton.setBackgroundResource(R.mipmap.back);

        titleText.setText("邮  箱");
        rightButton.setImageResource(R.mipmap.confirm);

        leftLayout.setOnClickListener(onClickListener);
        rightLayout.setOnClickListener(onClickListener);
    }
}
