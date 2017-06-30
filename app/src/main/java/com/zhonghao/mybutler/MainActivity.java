package com.zhonghao.mybutler;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zhonghao.mybutler.fragment.ButlerFragment;
import com.zhonghao.mybutler.fragment.GirlFragment;
import com.zhonghao.mybutler.fragment.UserFragment;
import com.zhonghao.mybutler.fragment.WechatFragment;
import com.zhonghao.mybutler.ui.SettingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    //标题
    List<String> mTitle;
    //Fragment
    List<Fragment> mFragment;

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.fb_setting)
    FloatingActionButton mFbSetting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //去掉阴影
        getSupportActionBar().setElevation(0);

        initData();
        initView();
    }

    private void initView() {
        //viewPager初始化
        //viewPager预加载
        mViewPager.setOffscreenPageLimit(mFragment.size());
        mViewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragment.get(position);
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            //设置标题

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitle.get(position);
            }
        });
        //viewPager设置滑动监听
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    mFbSetting.setVisibility(View.GONE);
                } else {
                    mFbSetting.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayout.setupWithViewPager(mViewPager);

        mFbSetting.setOnClickListener(this);
        mFbSetting.setVisibility(View.VISIBLE);

    }

    private void initData() {
        mTitle = new ArrayList<>();
        mTitle.add(getString(R.string.text_wechat));
        mTitle.add(getString(R.string.text_butler_service));
        mTitle.add(getString(R.string.text_girl));
        mTitle.add(getString(R.string.text_user_info));

        mFragment = new ArrayList<>();
        mFragment.add(new WechatFragment());
        mFragment.add(new ButlerFragment());
        mFragment.add(new GirlFragment());
        mFragment.add(new UserFragment());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fb_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }
}
