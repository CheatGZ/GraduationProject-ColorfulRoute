package com.bupt.colorfulroute.runningapp.uiutils;

import android.app.Activity;
import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.entity.Achievement;
import com.bupt.colorfulroute.util.CheckFormat;
import com.facebook.drawee.view.SimpleDraweeView;


public class AchievementDialog extends Dialog {

    public TextView achTitle;
    private Activity mContext;
    private SimpleDraweeView achImage;
    private TextView achDescription;
    private TextView achCondition;
    private TextView achDate;
    private Button achShow;
    private Achievement mAchievement;//传进来的achievement
    private int mPosition;//第mPosition个成就

    private View.OnClickListener mClickListener;

    public AchievementDialog(Activity context) {
        super(context);
        this.mContext = context;

    }

    public AchievementDialog(Activity context, View.OnClickListener clickListener, Achievement achievement, int position) {
        super(context);
        this.mContext = context;
        this.mClickListener = clickListener;
        this.mAchievement = achievement;
        this.mPosition = position;
    }

    public AchievementDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.mContext = context;
        this.mClickListener = clickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_achievement);
        achImage = findViewById(R.id.image_achievement_show);
        achTitle = findViewById(R.id.text_achievement_title);
        achDescription = findViewById(R.id.text_achievement_description);
        achCondition = findViewById(R.id.text_achievement_condition);
        achDate = findViewById(R.id.text_achievement_date);
        achShow = findViewById(R.id.btn_achievement_show);
        /*
         * 获取弹出框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager wm = mContext.getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
//        p.height = (int) (d.getHeight() * 0.7); // 高度设置为屏幕的n倍
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕n倍
        dialogWindow.setAttributes(p);
        dialogWindow.setBackgroundDrawableResource(R.drawable.bg_white);

        // 为按钮绑定点击事件监听器
        achShow.setOnClickListener(mClickListener);

        initDialog();//为dialog赋值

        this.setCancelable(true);
    }

    private void initDialog() {
        Uri uri;
        int[] image = new int[]{R.mipmap.cat
                , R.mipmap.cattle
                , R.mipmap.fish
                , R.mipmap.frog
                , R.mipmap.monkey
                , R.mipmap.panda
                , R.mipmap.rabbit
                , R.mipmap.sloth};
        if (mAchievement.getTimeAchieved() != 0) {
            uri = Uri.parse("res://com.bupt.colorfulroute/" + image[mPosition]);
            achImage.setImageURI(uri);
            achTitle.setText(mAchievement.getTitle());
            achDescription.setText(Html.fromHtml(mAchievement.getDescription()));
            achCondition.setText(mAchievement.getCondition());
            achDate.setText(CheckFormat.dateFormat2(mAchievement.getTimeAchieved()).substring(0, 10));
        } else {
            uri = Uri.parse("res://com.bupt.colorfulroute/" + R.mipmap.no_achieve);
            achImage.setImageURI(uri);
            achCondition.setText(mAchievement.getCondition());
            achShow.setVisibility(View.GONE);
        }
    }
}
