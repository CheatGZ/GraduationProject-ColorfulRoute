package com.bupt.colorfulroute.util;

import android.util.Log;

import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WbAuthListener;
import com.sina.weibo.sdk.auth.WbConnectErrorMessage;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class WbAuthUtils {

    private static final String TAG = "WbAuthUtils";

    private static final String WEIBO_USERINFO_URL = "https://api.weibo.com/2/users/show.json?access_token=";//微博个人信息链接

    /**
     * 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能
     */
    private static Oauth2AccessToken mAccessToken;

    /**
     * 开启微博授权登录
     */
    public static void startSinaWeiBo(SsoHandler ssoHandler) {

        //授权方式有三种，第一种对客户端授权 第二种对Web短授权，第三种结合前两中方式
        ssoHandler.authorize(new WbAuthListener() {
            @Override
            public void onSuccess(Oauth2AccessToken token) {

                Log.e(TAG, "onSuccess: " + token.getToken());
                Log.e(TAG, "onSuccess: " + token.getUid());

                mAccessToken = token;
                //Session是否过期
                if (mAccessToken.isSessionValid()) {
                    getSinaWeiBoInfo(token.getToken(), token.getUid());
                }
            }

            @Override
            public void cancel() {
                Log.e(TAG, "微博授权取消: ");
            }

            @Override
            public void onFailure(WbConnectErrorMessage wbConnectErrorMessage) {
                Log.e(TAG, "微博授权失败: ");
            }
        });
    }


    /**
     * 获取微博信息
     */
    private static void getSinaWeiBoInfo(String token, String uid) {

        String url = WEIBO_USERINFO_URL + token + "&uid=" + uid;

        Log.e(TAG, "请求token: " + token);
        Log.e(TAG, "请求openId: " + uid);

        Request request = new Request.Builder().url(url).build();
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                listener.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //成功回调
                String result = response.body().string();

                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String name = jsonObject.optString("screen_name");
                    String icon = jsonObject.optString("avatar_hd");

                    Log.e(TAG, "result: "+ result);
                    Log.e(TAG, "name: "+ name);
                    Log.e(TAG, "icon: "+ icon);

                    listener.onSuccess(result, icon, name);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private static onSuccessListener listener;

    public void setListener(onSuccessListener listener) {
        this.listener = listener;
    }

    public interface onSuccessListener {

        void onSuccess(String result, String icon, String name);//json数据、头像、名字

        void onFailure(String errorMessage);//错误信息
    }
}