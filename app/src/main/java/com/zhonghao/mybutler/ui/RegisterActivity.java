package com.zhonghao.mybutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.zhonghao.mybutler.R;
import com.zhonghao.mybutler.entity.MyUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.zhonghao.mybutler.R.id.et_age;
import static com.zhonghao.mybutler.R.id.et_desc;
import static com.zhonghao.mybutler.R.id.et_email;
import static com.zhonghao.mybutler.R.id.et_pass;
import static com.zhonghao.mybutler.R.id.et_password;
import static com.zhonghao.mybutler.R.id.et_user;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.ui
 * 类描述：
 * 创建人：小豪
 * 创建时间：2017/6/23 15:46
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener{
    @BindView(et_user)
    EditText mEtUser;
    @BindView(et_age)
    EditText mEtAge;
    @BindView(et_desc)
    EditText mEtDesc;
    @BindView(R.id.rb_boy)
    RadioButton mRbBoy;
    @BindView(R.id.rb_girl)
    RadioButton mRbGirl;
    @BindView(R.id.radioGroup)
    RadioGroup mRadioGroup;
    @BindView(et_pass)
    EditText mEtPass;
    @BindView(et_password)
    EditText mEtPassword;
    @BindView(et_email)
    EditText mEtEmail;
    @BindView(R.id.btnRegistered)
    Button mBtnRegistered;
    private boolean isGender = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        initListener();

    }

    private void initListener() {
        mBtnRegistered.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegistered:
                //获取到输入框的值
                String name = mEtUser.getText().toString().trim();
                String age = mEtAge.getText().toString().trim();
                String desc = mEtDesc.getText().toString().trim();
                String pass = mEtPass.getText().toString().trim();
                String password = mEtPassword.getText().toString().trim();
                String email = mEtEmail.getText().toString().trim();

                //判断是否为空
                if (!TextUtils.isEmpty(name) & !TextUtils.isEmpty(age) &
                        !TextUtils.isEmpty(pass) &
                        !TextUtils.isEmpty(password) &
                        !TextUtils.isEmpty(email)) {

                    //判断两次输入的密码是否一致
                    if (pass.equals(password)) {

                        //先把性别判断一下
                        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(RadioGroup group, int checkedId) {
                                if (checkedId == R.id.rb_boy) {
                                    isGender = true;
                                } else if (checkedId == R.id.rb_girl) {
                                    isGender = false;
                                }
                            }
                        });

                        //判断简介是否为空
                        if (TextUtils.isEmpty(desc)) {
                            desc = getString(R.string.text_nothing);
                        }

                        //注册
                        MyUser user = new MyUser();
                        user.setUsername(name);
                        user.setPassword(password);
                        user.setEmail(email);
                        user.setAge(Integer.parseInt(age));
                        user.setSex(isGender);
                        user.setDesc(desc);

                        user.signUp(new SaveListener<MyUser>() {
                            @Override
                            public void done(MyUser myUser, BmobException e) {
                                if(e==null){
                                    Toast.makeText(RegisterActivity.this, R.string.text_registered_successful, Toast.LENGTH_SHORT).show();
                                    finish();
                                }else{
                                    Toast.makeText(RegisterActivity.this, getString(R.string.text_registered_failure) + e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        Toast.makeText(this, R.string.text_two_input_not_consistent, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, getString(R.string.text_tost_empty), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
