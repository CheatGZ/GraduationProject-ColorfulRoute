package com.bupt.colorfulroute.runningapp.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.entity.Achievement;
import com.bupt.colorfulroute.runningapp.entity.UserInfo;
import com.bupt.colorfulroute.runningapp.uiutils.StatusBarUtils;
import com.bupt.colorfulroute.util.ImageToUri;
import com.bupt.colorfulroute.util.WbAuthUtils;
import com.bupt.colorfulroute.util.WeiboConstants;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {
    private final int mRequestCode = 100;//权限请求码
    LoginActivity self = this;
    SsoHandler ssoHandler;
    WbAuthUtils utils = new WbAuthUtils();
    @BindView(R.id.activity_login_weibo_login_button)
    Button loginButton;
    @BindView(R.id.login_bg_layout)
    LinearLayout loginBgLayout;
    UserInfo userInfo1 = new UserInfo();
    Achievement[] achievement = new Achievement[8];
    String[] permissions = new String[]{Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION};
    List<String> mPermissionList = new ArrayList<>();


    //请求权限
    /**
     * 不再提示权限时的展示对话框
     */
    AlertDialog mPermissionDialog;
    String mPackName = "com.huawei.liwenzhi.weixinasr";
    private boolean isSignedUp = false;//判断是否已经注册
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.activity_login_weibo_login_button:
                    weiboLogin();
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
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //activity右侧滑动滑出动画
        overridePendingTransition(R.anim.slide_in, R.anim.no_slide);

        StatusBarUtils.setStatusBarColor(this, Color.TRANSPARENT, false);
        if (Build.VERSION.SDK_INT >= 23) {//6.0才用动态权限
            initPermission();
        }

        //weibo sdk init
        WbSdk.install(self, new AuthInfo(self, WeiboConstants.APP_KEY, WeiboConstants.REDIRECT_URL, WeiboConstants.SCOPE));
        ssoHandler = new SsoHandler(self);

        //login activity init
        loginButton.setOnClickListener(onClickListener);

        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.mipmap.splash);
        if (drawable != null) {
            drawable.setColorFilter(Color.GRAY, PorterDuff.Mode.MULTIPLY);
        }
        loginBgLayout.setBackground(drawable);
    }

    private void initPermission() {
        mPermissionList.clear();//清空没有通过的权限

        //逐个判断你要的权限是否已经通过
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }

        //申请权限
        if (mPermissionList.size() > 0) {//有权限没有通过，需要申请
            ActivityCompat.requestPermissions(this, permissions, mRequestCode);
        } else {
            //说明权限都已经通过，可以做你想做的事情去
        }

    }

    //请求权限后回调的方法
    //参数： requestCode  是我们自己定义的权限请求码
    //参数： permissions  是我们请求的权限名称数组
    //参数： grantResults 是我们在弹出页面后是否允许权限的标识数组，数组的长度对应的是权限名称数组的长度，数组的数据0表示允许权限，-1表示我们点击了禁止权限
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean hasPermissionDismiss = false;//有权限没有通过
        if (mRequestCode == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            //如果有权限没有被允许
            if (hasPermissionDismiss) {
                showPermissionDialog();//跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
            } else {
                //全部权限通过，可以进行下一步操作。。。

            }
        }

    }

    private void showPermissionDialog() {
        if (mPermissionDialog == null) {
            mPermissionDialog = new AlertDialog.Builder(this)
                    .setMessage("已禁用权限，请手动授予")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            cancelPermissionDialog();

                            Uri packageURI = Uri.parse("package:" + mPackName);
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //关闭页面或者做其他操作
                            cancelPermissionDialog();
                        }
                    })
                    .create();
        }
        mPermissionDialog.show();
    }

    //关闭对话框
    private void cancelPermissionDialog() {
        mPermissionDialog.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            default:
                if (ssoHandler != null) {
                    ssoHandler.authorizeCallBack(requestCode, resultCode, data);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initAchievement(UserInfo userInfo) {
        List<Achievement> list = new ArrayList<>();
        //初始化成就信息
        String[] titles = new String[]{"橘胖", "夏洛莱牛", "咸鱼君", "黑斑侧褶蛙", "金丝猴", "熊猫滚滚", "灰尾兔", "三趾树懒"};
        String[] conditions = new String[]{"第一次进入彩径", "完成一次跑步", "完成10次跑步", "完成一次1km的跑步", "完成一次10km的跑步", "总跑步距离100km", "总跑步时间10小时", "分享一次跑步截图"};
        String[] descriptions = new String[]{"橘猫是家猫常见的一种毛色，也叫橘子猫，桔猫，普遍存在于混种猫和不具独特规定毛色的注册纯种猫中，与品种无关，与被毛基因有关。"
                , "夏洛莱牛原产于法国中西部到东南部的夏洛莱省和涅夫勒地区，是举世闻名的大型肉牛品种，自育成以来就以其生长快、肉量多、体型大、耐粗放而受到国际市场的广泛欢迎，早已输往世界许多国家。"
                , "咸鱼，是粤语中的一种俗称。电影《少林足球》中的台词：做人如果没有梦想，和咸鱼有什么区别呢？"
                , "黑斑侧褶蛙，也叫黑斑蛙，属无尾目，蛙科，侧褶蛙属。分布于除新疆、西藏、云南、台湾、海南省外，广泛分布于各省。"
                , "金丝猴，毛质柔软，鼻子上翘，有缅甸金丝猴、怒江金丝猴、川金丝猴、滇金丝猴、黔金丝猴、越南金丝猴6种，其中除缅甸金丝猴和越南金丝猴外，均为中国特有的珍贵动物。"
                , "大熊猫是属于食肉目、熊科、大熊猫亚科和大熊猫属唯一的哺乳动物，体色为黑白两色，它有着圆圆的脸颊，大大的黑眼圈，胖嘟嘟的身体，标志性的内八字的行走方式，也有解剖刀般锋利的爪子。是世界上最可爱的动物之一。"
                , "灰尾兔也叫高原兔，是青藏高原的特有种，体毛浅灰棕，殿部青灰，尾毛浅白，尾基有灰斑。仅分布于阿尔金山和昆仑山海拔3000米以上的高寒草原、灌丛中，穴居，以棘豆、苔草等高山植物为食。"
                , "三趾树懒，是树懒科、树懒属的哺乳动物。三趾树懒头小而圆，体长50-60厘米，身上针毛长而粗糙。身上被毛原是灰棕色，后显绿色。"};

        int[] icons = new int[]{R.mipmap.cat_thumbnail
                , R.mipmap.cattle_thumbnail
                , R.mipmap.fish_thumbnail
                , R.mipmap.frog_thumbnail
                , R.mipmap.monkey_thumbnail
                , R.mipmap.panda_thumbnail
                , R.mipmap.rabbit_thumbnail
                , R.mipmap.sloth_thumbnail};
        for (int i = 0; i < 8; i++) {
            achievement[i] = new Achievement();
            list.add(achievement[i]);
        }
        userInfo.setAchievement(list);
        //设置成就信息(获得和未获得)
        String unIconUri = ImageToUri.imageTranslateUri(this, R.mipmap.no_achieve);

        for (int i = 0; i < 8; i++) {
            userInfo.getAchievement().get(i).setTitleId(i);//成就id
            userInfo.getAchievement().get(i).setUnIcon(R.mipmap.no_achieve);//未获得成就icon
            userInfo.getAchievement().get(i).setIcon(icons[i]);
            userInfo.getAchievement().get(i).setTitle(titles[i]);//成就名
            userInfo.getAchievement().get(i).setCondition(conditions[i]);
            userInfo.getAchievement().get(i).setDescription(descriptions[i]);
        }
        //设置获得时间
        userInfo.getAchievement().get(0).setTimeAchieved(System.currentTimeMillis());
        for (int i = 1; i < 8; i++) {
            userInfo.getAchievement().get(i).setTimeAchieved(0L);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.no_slide, R.anim.slide_exit);
    }

    private void weiboLogin() {
        WbAuthUtils.startSinaWeiBo(ssoHandler);
        SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();//获取编辑器
        editor.clear();
        editor.apply();
        utils.setListener(new WbAuthUtils.onSuccessListener() {
            @Override
            public void onSuccess(final String result, final String icon, final String name) {

                try {
                    final JSONObject userInfo = new JSONObject(result);
                    final String account = userInfo.optString("idstr");
                    final String screen_name = userInfo.optString("screen_name");
                    final String gender = userInfo.optString("gender");
                    final String description = userInfo.optString("description");
                    //判断数据库是否已经存在登录账号
                    BmobQuery<UserInfo> bmobQuery = new BmobQuery<>();
                    bmobQuery.addWhereEqualTo("account", account);
                    bmobQuery.count(UserInfo.class, new CountListener() {
                        @Override
                        public void done(Integer integer, BmobException e) {
                            if (e == null) {
                                if (integer == 1) {
                                    isSignedUp = true;
                                } else {
                                    isSignedUp = false;
                                }
                                if (isSignedUp) {
                                    //已注册
                                    BmobQuery<UserInfo> bmobQuery2 = new BmobQuery<>();
                                    bmobQuery2.addWhereEqualTo("account", account);
                                    bmobQuery2.findObjects(new FindListener<UserInfo>() {
                                        @Override
                                        public void done(List<UserInfo> list, BmobException e) {
                                            if (e == null) {
                                                String objectId = list.get(0).getObjectId();
                                                //将当前账号写入sharedpreference
                                                SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sp.edit();//获取编辑器
                                                editor.putString("account", account);
                                                editor.putString("name", screen_name);
                                                editor.putString("objectId", objectId);
                                                editor.putInt("RouteKind", 0);
                                                editor.putBoolean("mapShow", true);
                                                editor.apply();//提交修改

                                                //登录成功，跳转界面
                                                Toast.makeText(self, "登录成功", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(self, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                Toast.makeText(self, "彩径尽头发生未知错误，请重新登录！", Toast.LENGTH_SHORT).show();
                                                System.out.println("Bmob login error" + e);
                                            }
                                        }
                                    });
                                } else {
                                    //未注册,初始化数据
                                    userInfo1.setAccount(account);
                                    userInfo1.setName(screen_name);
                                    userInfo1.setGender(gender);
                                    userInfo1.setIcon(icon);
                                    userInfo1.setDescription(description);
                                    userInfo1.setNumber(0);
                                    userInfo1.setTotalCalorie(0);
                                    userInfo1.setTotalTime(0L);
                                    userInfo1.setTotalLength(0D);
                                    userInfo1.setWeight(0);
                                    userInfo1.setHeight(0);
                                    userInfo1.setAge(0);
                                    //初始化成就信息
                                    initAchievement(userInfo1);
                                    //初始化显示成就
                                    userInfo1.setTitle("无");
                                    userInfo1.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if (e == null) {
                                                //将当前账号写入sharedpreference
                                                SharedPreferences sp = self.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sp.edit();//获取编辑器
                                                editor.putString("account", account);
                                                editor.putString("name", screen_name);
                                                editor.putString("objectId", s);
                                                editor.putInt("RouteKind", 0);
                                                editor.apply();//提交修改

                                                //登录成功，跳转界面
                                                Toast.makeText(self, "登录成功", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(self, MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                            } else {
                                                System.out.println("注册失败：" + e.getMessage());
                                                Toast.makeText(self, "注册失败，请重新登录！", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            } else {
                                Toast.makeText(self, "卉跑服务器失去联系，请重新登录！", Toast.LENGTH_SHORT).show();
                                System.out.println("Bmob login error" + e);
                            }
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                //登录失败
                Toast.makeText(self, "微博服务器失去联系，请重新登录！", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
