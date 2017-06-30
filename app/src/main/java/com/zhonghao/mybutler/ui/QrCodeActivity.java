package com.zhonghao.mybutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.xys.libzxing.zxing.encoding.EncodingUtils;
import com.zhonghao.mybutler.R;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.ui
 * 类描述：
 * 创建人：小豪
 * 创建时间：2017/6/29 16:45
 */

public class QrCodeActivity extends BaseActivity{

    //我的二维码
    private ImageView iv_qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        initView();
    }

    private void initView() {

        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
        //屏幕的宽
        int width = getResources().getDisplayMetrics().widthPixels;

        Bitmap qrCodeBitmap = EncodingUtils.createQRCode("我的智能管家", width / 2, width / 2,
                BitmapFactory.decodeResource(getResources(), R.drawable.tubiao));
        iv_qr_code.setImageBitmap(qrCodeBitmap);
    }
}
