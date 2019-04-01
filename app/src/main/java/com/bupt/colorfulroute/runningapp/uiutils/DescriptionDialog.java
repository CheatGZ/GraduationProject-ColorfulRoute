package com.bupt.colorfulroute.runningapp.uiutils;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.bupt.colorfulroute.R;


public class DescriptionDialog extends Dialog {
    public EditText descriptionEditText;
    public LinearLayout descriptionDialogView;
    private Button btnSavePop;
    private Activity mContext;

    private View.OnClickListener mClickListener;

    public DescriptionDialog(Activity context) {
        super(context);
        this.mContext = context;

    }

    public DescriptionDialog(Activity context, View.OnClickListener clickListener) {
        super(context);
        this.mContext = context;
        this.mClickListener = clickListener;
    }

    public DescriptionDialog(Activity context, int theme, View.OnClickListener clickListener) {
        super(context, theme);
        this.mContext = context;
        this.mClickListener = clickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.dialog_description);
        descriptionEditText = findViewById(R.id.description_edit_text);
        btnSavePop = findViewById(R.id.btn_save_pop);
        /*
         * 获取弹出框的窗口对象及参数对象以修改对话框的布局设置, 可以直接调用getWindow(),表示获得这个Activity的Window
         * 对象,这样这可以以同样的方式改变这个Activity的属性.
         */
        Window dialogWindow = this.getWindow();

        WindowManager wm = mContext.getWindowManager();
        Display d = wm.getDefaultDisplay(); // 获取屏幕宽、高用
        WindowManager.LayoutParams p = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.5); // 高度设置为屏幕的0.5
        p.width = (int) (d.getWidth() * 0.8); // 宽度设置为屏幕的0.5
        dialogWindow.setAttributes(p);
        dialogWindow.setBackgroundDrawableResource(R.drawable.bg_primary_light);
        // 为按钮绑定点击事件监听器
        btnSavePop.setOnClickListener(mClickListener);

        this.setCancelable(true);
    }
}
