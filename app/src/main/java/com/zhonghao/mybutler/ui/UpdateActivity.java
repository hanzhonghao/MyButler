package com.zhonghao.mybutler.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.TextView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.client.ProgressListener;
import com.kymjs.rxvolley.http.VolleyError;
import com.kymjs.rxvolley.toolbox.FileUtils;
import com.zhonghao.mybutler.R;
import com.zhonghao.mybutler.utils.L;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.ui
 * 类描述：
 * 创建人：小豪
 * 创建时间：2017/6/29 11:22
 */

public class UpdateActivity extends BaseActivity {
    //正在下载
    public static final int HANDLER_LODING = 10001;
    //下载完成
    public static final int HANDLER_OK = 10002;
    //下载失败
    public static final int HANDLER_ON = 10003;

    private String url;
    private String path;

    @BindView(R.id.tv_size)
    TextView mTvSize;
    @BindView(R.id.number_progress_bar)
    NumberProgressBar mNumberProgressBar;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case HANDLER_LODING:
                    //实时更新进度
                    Bundle bundle = msg.getData();
                    long transferredBytes = bundle.getLong("transferredBytes");
                    long totalSize = bundle.getLong("totalSize");
                    mTvSize.setText(transferredBytes+"/"+totalSize);

                    //设置进度
                    mNumberProgressBar.setProgress((int)((float)transferredBytes/(float)totalSize)*100);
                    break;
                case HANDLER_OK:
                    mTvSize.setText("下载成功");
                    //启动这个应用安装
                    startInstallApk();
                    break;
                case HANDLER_ON:
                    mTvSize.setText("下载失败");
                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mNumberProgressBar.setMax(100);

        path = FileUtils.getSDCardPath() + "/" + System.currentTimeMillis() + ".apk";

        //下载
        url = getIntent().getStringExtra("url");
        L.i("****"+ url);
        if (!TextUtils.isEmpty(url)) {
            //下载
            RxVolley.download(path, url, new ProgressListener() {
                @Override
                public void onProgress(long transferredBytes, long totalSize) {
//                    L.i("transferredBytes:" + transferredBytes + "totalSize:" + totalSize);
                    Message msg = new Message();
                    msg.what = HANDLER_LODING;
                    Bundle bundler = new Bundle();
                    bundler.putLong("transferredBytes", transferredBytes);
                    bundler.putLong("totalSize", totalSize);
                    msg.setData(bundler);
                    handler.sendMessage(msg);
                }
            }, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    super.onSuccess(t);
                    L.i("下载成了");
                }

                @Override
                public void onFailure(VolleyError error) {
                    super.onFailure(error);
                    L.i("下载失败");
                }
            });
        }
    }

    private void startInstallApk() {
        Intent i = new Intent();
        i.setAction(Intent.ACTION_VIEW);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setDataAndType(Uri.fromFile(new File(path)),"application/vnd.android.package-archive");
        startActivity(i);
        finish();
    }

}
