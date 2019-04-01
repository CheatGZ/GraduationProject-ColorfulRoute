package com.bupt.colorfulroute.runningapp.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.uicomponent.AlertMessage;
import com.bupt.colorfulroute.runningapp.uiutils.StatusBarUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends AppCompatActivity {
    public int flag_net = 0;//标志网络状态，默认无网络
    Splash self = this;
    @BindView(R.id.splash)
    LinearLayout splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        ButterKnife.bind(this);
        StatusBarUtils.fullScreen(this);
        splash.setBackgroundResource(R.mipmap.splash);

        //判断是否已经登录，已登录直接跳到主界面
        final Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(2000);//使程序休眠2秒
                    SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    String account = sp.getString("account", "");
                    if (account == "") {
                        Intent intent = new Intent(self, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(self, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }

    @Override
    public void finish() {
        super.finish();
    }
}
