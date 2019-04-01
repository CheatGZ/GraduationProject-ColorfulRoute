package com.bupt.colorfulroute.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * @author CheatGZ
 * @date 2019/3/23.
 * description：
 */
public class ShowKeyBoard {
    //延迟显示软键盘
    public static void delayShowSoftKeyBoard(final EditText editText) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(editText, 0);
                           }
                       },
                250);
    }

    //显示软键盘,跳入新界面弹出软键盘请用延迟显示键盘
    public static void showSoftKeyBoard(final EditText editText) {
        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(editText, 0);

    }
}
