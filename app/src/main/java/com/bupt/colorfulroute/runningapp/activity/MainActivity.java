package com.bupt.colorfulroute.runningapp.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.ViewGroup;

import com.bupt.colorfulroute.R;
import com.bupt.colorfulroute.runningapp.fragment.HistoryFragment;
import com.bupt.colorfulroute.runningapp.fragment.MainFragment;
import com.bupt.colorfulroute.runningapp.fragment.UserFragment;
import com.bupt.colorfulroute.runningapp.uicomponent.AlertMessage;
import com.bupt.colorfulroute.util.AaseActivity;
import com.bupt.colorfulroute.util.JudgeNetAndGPS;
import com.viewpagerindicator.TabPageIndicator;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

public class MainActivity extends AaseActivity {
    public String titles[] = new String[]{"跑步历史", "跑步", "个人中心"};
    public Fragment history = HistoryFragment.newInstance();
    public Fragment main = MainFragment.newInstance();
    public Fragment user = UserFragment.newInstance();
    @BindView(R.id.scrollView)
    ViewPager scrollView;
    @BindView(R.id.indicator)
    TabPageIndicator indicator;
    private TabPageIndicatorAdapter mAdpter;
    private int currentPage = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "e834b45389cad785bed5c43e2942b606");
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        judgeNetAndGPS();
    }

    private void judgeNetAndGPS() {
        if (!JudgeNetAndGPS.isNetworkAvailable(this)) {
            final AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setCancelable(false);
            builder.setTitle("无法连接到网络，部分功能无法使用，请检查手机设置后重试!")
                    .setPositiveButton("退出", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    }).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void changeFragment(int fragment) {
        indicator.setCurrentItem(fragment);
    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (currentPage == 0) {
//            changeFragment(1);
//            return true;
//        } else if (currentPage == 2) {
//            changeFragment(1);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public void onBackPressed() {
        if (currentPage == 0) {
            changeFragment(1);
        } else if (currentPage == 2) {
            changeFragment(1);
        }
        else {
            super.onBackPressed();
        }
    }

    private void initView() {
        history = HistoryFragment.newInstance();
        main = MainFragment.newInstance();
        user = UserFragment.newInstance();

        mAdpter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        //给ViewPager设置Adapter
        scrollView.setAdapter(mAdpter);
        //与ViewPager绑在一起（核心步骤）
        indicator.setViewPager(scrollView);
        indicator.setCurrentItem(1);
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    //adapter
    class TabPageIndicatorAdapter extends FragmentPagerAdapter {
        Bundle args = new Bundle();

        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    args.putString("arg", titles[position]);
                    history.setArguments(args);
                    return history;
                case 1:
                    args.putString("arg", titles[position]);
                    main.setArguments(args);
                    return main;
                case 2:
                    args.putString("arg", titles[position]);
                    user.setArguments(args);
                    return user;
            }
            return main;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position % titles.length];
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem(container, position, object);//不销毁fragment
        }
    }
}
