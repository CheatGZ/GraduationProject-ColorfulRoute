package com.bupt.colorfulroute.util;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.uicomponent.AlertMessage;
import com.bupt.colorfulroute.runningapp.uiutils.SlideBackLayout;
import com.bupt.colorfulroute.runningapp.uiutils.StatusBarUtils;

import cn.bmob.v3.Bmob;


abstract public class AaseActivity extends AppCompatActivity {
    private BaseApplication application;
    private AaseActivity oContext;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //bmob后端云
        Bmob.initialize(this, "e834b45389cad785bed5c43e2942b606");
        //状态栏颜色
        StatusBarUtils.setStatusBarColor(this, Color.TRANSPARENT, false);

        //activity右侧滑动滑出动画
        overridePendingTransition(R.anim.slide_in, R.anim.no_slide);

        //添加activity，最终统一管理
        if (application == null) {
            // 得到Application对象
            application = (BaseApplication) getApplication();
        }
        oContext = this;// 把当前的上下文对象赋值给BaseActivity
        addActivity();// 调用添加方法
    }

    @Override
    public void finish() {
        super.finish();

    }

    // 添加Activity方法
    public void addActivity() {
        application.addActivity_(oContext);// 调用myApplication的添加Activity方法
    }

    //销毁当个Activity方法
    public void removeActivity() {
        application.removeActivity_(oContext);// 调用myApplication的销毁单个Activity方法
    }

    //销毁所有Activity方法
    public void removeALLActivity() {
        application.removeALLActivity_();// 调用myApplication的销毁所有Activity方法
    }

    /* 把Toast定义成一个方法  可以重复使用，使用时只需要传入需要提示的内容即可*/
    public void showToast(String text) {
        Toast.makeText(oContext, text, Toast.LENGTH_SHORT).show();
    }

    public void alert(AlertMessage alertMessage) {
        new android.app.AlertDialog.Builder(this).setTitle(alertMessage.getTitle())
                .setMessage(alertMessage.getDetail())
                .show();
    }

}
