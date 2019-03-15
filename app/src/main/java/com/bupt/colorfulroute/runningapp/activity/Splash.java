package com.bupt.colorfulroute.runningapp.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.uiutils.StatusBarUtils;
import com.bupt.colorfulroute.util.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends BaseActivity {
    Splash self = this;
    @BindView(R.id.splash)
    ImageView splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        StatusBarUtils.fullScreen(this);
        ButterKnife.bind(this);
        splash.setImageResource(R.mipmap.splash);

        //        //判断是否已经登录，已登录直接跳到主界面


        Thread myThread = new Thread() {//创建子线程
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
}
