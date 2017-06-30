package com.zhonghao.mybutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhonghao.mybutler.R;
import com.zhonghao.mybutler.utils.SPUtils;
import com.zhonghao.mybutler.utils.StaticClass;
import com.zhonghao.mybutler.utils.UtilTools;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.ui
 * 类描述：
 * 创建人：小豪
 * 创建时间：2017/6/22 19:23
 */

/**
 * 1.延时2秒
 * 2.判断程序是否第一次运行
 * 3.自定义字体
 * 4.Activity全屏主题
 */

public class SplashActivity extends AppCompatActivity {
    @BindView(R.id.tv_splash)
    TextView mTvSplash;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case StaticClass.HANDLER_SPLASH:
                    //判断程序是否第一次运行
                    if (isFirst()) {
                        startActivity(new Intent(SplashActivity.this, GuideActivity.class));
                    } else {
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    }
                    finish();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        initLogic();//延时发消息的逻辑

    }

    private void initLogic() {
        //延迟2000ms
        handler.sendEmptyMessageDelayed(StaticClass.HANDLER_SPLASH, 2000);
        //设置字体
        UtilTools.setFont(this, mTvSplash);
    }


    //判断程序是否是第一次运行
    private boolean isFirst() {
        boolean isFirst = SPUtils.getBoolean(this, StaticClass.SHARE_IS_FIRST, true);
        if (isFirst) {
            //标记我们是否启动过App
            SPUtils.putBoolean(this, StaticClass.SHARE_IS_FIRST, false);
            return true;
        } else {
            return false;
        }
    }

    //禁止返回键

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
