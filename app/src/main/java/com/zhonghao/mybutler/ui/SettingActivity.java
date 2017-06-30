package com.zhonghao.mybutler.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.xys.libzxing.zxing.activity.CaptureActivity;
import com.zhonghao.mybutler.R;
import com.zhonghao.mybutler.utils.L;
import com.zhonghao.mybutler.utils.SPUtils;
import com.zhonghao.mybutler.utils.StaticClass;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhonghao.mybutler.R.id.ll_qr_code;
import static com.zhonghao.mybutler.R.id.ll_update;
import static com.zhonghao.mybutler.R.id.tv_version;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.ui
 * 类描述：
 * 创建人：小豪
 * 创建时间：2017/6/22 16:56
 */

public class SettingActivity extends BaseActivity implements View.OnClickListener{
    @BindView(R.id.sw_speak)
    Switch mSwSpeak;
    @BindView(tv_version)
    TextView mTvVersion;
    @BindView(ll_update)
    LinearLayout mLlUpdate;
    @BindView(R.id.tv_scan_result)
    TextView mTvScanResult;
    @BindView(R.id.ll_scan)
    LinearLayout mLlScan;
    @BindView(ll_qr_code)
    LinearLayout mLlQrCode;
    @BindView(R.id.textView)
    TextView mTextView;
    @BindView(R.id.ll_my_location)
    LinearLayout mLlMyLocation;
    @BindView(R.id.ll_about)
    LinearLayout mLlAbout;

    private String url;
    private String versionName;
    private int versionCode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initListener();
        initView();
    }

    private void initView() {
        try {
            getVersionNameCode();
            mTvVersion.setText("检测版本: "+ versionName);
        } catch (PackageManager.NameNotFoundException e) {
            mTvVersion.setText("检测版本: ");
        }

    }

    private void initListener() {
        mSwSpeak.setOnClickListener(this);
        boolean isSpeak = SPUtils.getBoolean(this,"isSpeak",false);
        mSwSpeak.setChecked(isSpeak);

        mLlUpdate.setOnClickListener(this);

        mLlAbout.setOnClickListener(this);

        mLlMyLocation.setOnClickListener(this);

        mLlQrCode.setOnClickListener(this);

        mLlScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.sw_speak:
              //切换相反
              mSwSpeak.setSelected(!mSwSpeak.isSelected());
              //保存状态
              SPUtils.putBoolean(this,"isSpeak",mSwSpeak.isChecked());
              break;
          case R.id.ll_update:
              L.i("ll_update");
              /**
               * 步骤:
               * 1.请求服务器的配置文件，拿到code
               * 2.比较
               * 3.dialog提示
               * 4.跳转到更新界面，并且把url传递过去
               */
              RxVolley.get(StaticClass.CHECK_UPDATE_URL, new HttpCallback() {
                  @Override
                  public void onSuccess(String t) {
                      L.i("+++++++++"+t);
                      parsingJson(t);
                  }
              });
              break;
          case R.id.ll_scan:
              //打开扫描界面扫描条形码或二维码
              Intent openCameraIntent = new Intent(SettingActivity.this, CaptureActivity.class);
              startActivityForResult(openCameraIntent, 0);
              break;
          case R.id.ll_qr_code:
              startActivity(new Intent(this,QrCodeActivity.class));
              break;
          case R.id.ll_my_location:
              startActivity(new Intent(this,LocationActivity.class));
              break;
          case R.id.ll_about:
              startActivity(new Intent(this,AboutActivity.class));
              break;
      }

    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            int code = jsonObject.getInt("versionCode");
            url = jsonObject.getString("url");
            if (code>versionCode){
                showUpdateDialog(jsonObject.getString("content"));
            }else{
                Toast.makeText(this, "当前是最新版本", Toast.LENGTH_SHORT).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //弹出升级提示
    private void showUpdateDialog(String content) {
        new AlertDialog.Builder(this)
                .setTitle("有新版本啦！")
                .setMessage(content)
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(SettingActivity.this, UpdateActivity.class);
                        intent.putExtra("url",url);//这里传的是空的url，
                        startActivity(intent);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //我什么都不做，也会执行dismis方法
            }
        }).show();
    }


    //获取版本号/Code
    private void getVersionNameCode() throws PackageManager.NameNotFoundException {
        PackageManager pm = getPackageManager();
        PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
        versionName = info.versionName;
        versionCode = info.versionCode;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            mTvScanResult.setText(scanResult);
        }
    }
}
