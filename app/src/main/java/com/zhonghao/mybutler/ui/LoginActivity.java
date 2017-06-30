package com.zhonghao.mybutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghao.mybutler.MainActivity;
import com.zhonghao.mybutler.R;
import com.zhonghao.mybutler.entity.MyUser;
import com.zhonghao.mybutler.utils.SPUtils;
import com.zhonghao.mybutler.view.MyDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.ui
 * 类描述：
 * 创建人：小豪
 * 创建时间：2017/6/23 15:28
 */

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.profile_image)
    CircleImageView mProfileImage;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.keep_password)
    CheckBox mKeepPassword;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;
    @BindView(R.id.btn_registered)
    Button mBtnRegistered;
    @BindView(R.id.tv_forget)
    TextView mTvForget;
    private MyDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        initListener();
        initView();
    }

    private void initView() {
        boolean isKeep = SPUtils.getBoolean(this, "keepPass", false);
        mKeepPassword.setChecked(isKeep);

        if (isKeep) {
            String name = SPUtils.getString(this, "name", "");
            String password = SPUtils.getString(this, "password", "");
            mEtName.setText(name);
            mEtPassword.setText(password);
        }

        dialog = new MyDialog(this, 200, 100, R.layout.dialog_loading, R.style.Theme_dialog, Gravity.CENTER, R.style.pop_anim_style);
        dialog.setCancelable(false);//设置屏幕外点击无效
    }

    private void initListener() {
        mBtnRegistered.setOnClickListener(this);
        mBtnLogin.setOnClickListener(this);
        mTvForget.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_forget:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.btn_registered:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.btnLogin:
                //1.获取输入框的值
                String name = mEtName.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                //2.判断是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(password)) {
                    dialog.show();
                    //登录
                    MyUser myUser = new MyUser();
                    myUser.setUsername(name);
                    myUser.setPassword(password);
                    myUser.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {
                            if (e == null) {
                                dialog.dismiss();
                                //判断邮箱是否验证
                                if (myUser.getEmailVerified()) {
                                    //挑转
                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(LoginActivity.this, "请前往邮箱验证", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, "登录失败：" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    //假设用户输入用户名和密码，但是不点击登录，而是直接退出

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //保存状态
        SPUtils.putBoolean(this, "keepPass", mKeepPassword.isChecked());

        //是否记住密码
        if (mKeepPassword.isChecked()) {
            //记住用户名和密码
            SPUtils.putString(this, "name", mEtName.getText().toString().trim());
            SPUtils.putString(this, "password", mEtPassword.getText().toString().trim());
        } else {
            SPUtils.deleShare(this, "name");
            SPUtils.deleShare(this, "password");
        }
    }
}
