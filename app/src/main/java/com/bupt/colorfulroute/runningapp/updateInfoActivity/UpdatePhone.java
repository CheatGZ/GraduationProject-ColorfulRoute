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
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.uicomponent.AlertMessage;
import com.bupt.colorfulroute.runningapp.uiutils.SlideBackLayout;
import com.bupt.colorfulroute.util.BaseActivity;
import com.bupt.colorfulroute.util.CheckFormat;
import com.bupt.colorfulroute.util.ShowKeyBoard;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePhone extends BaseActivity {
    UpdatePhone self = this;
    @BindView(R.id.back_button)
    ImageView backButton;
    @BindView(R.id.title_text)
    TextView titleText;
    @BindView(R.id.right_button)
    ImageView rightButton;
    @BindView(R.id.input_text)
    EditText inputText;
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.left_layout:
                    finish();
                    break;
                case R.id.right_layout:
                    if (inputText.getText().toString().equals("") || !CheckFormat.isPhone(inputText.getText().toString())) {
                        alert(new AlertMessage("提交失败！", "请输入有效值！"));
                    } else {
                        SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                        String objectId = sp.getString("objectId", "");
                        UserInfo userInfo = new UserInfo();
                        userInfo.setPhone(inputText.getText().toString());
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
    @BindView(R.id.left_layout)
    LinearLayout leftLayout;
    @BindView(R.id.right_layout)
    LinearLayout rightLayout;

    private SlideBackLayout mSlideBackLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Bmob.initialize(self, "e834b45389cad785bed5c43e2942b606");
        setContentView(R.layout.activity_update_phone);
        ButterKnife.bind(this);
        mSlideBackLayout = new SlideBackLayout(this);
        mSlideBackLayout.bind();

        backButton.setBackgroundResource(R.mipmap.back);
        titleText.setText("手机号");
        rightButton.setImageResource(R.mipmap.confirm);
        ShowKeyBoard.delayShowSoftKeyBoard(inputText);
        leftLayout.setOnClickListener(onClickListener);
        rightLayout.setOnClickListener(onClickListener);
    }
}
