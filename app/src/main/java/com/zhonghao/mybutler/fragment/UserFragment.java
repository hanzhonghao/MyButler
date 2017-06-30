package com.zhonghao.mybutler.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zhonghao.mybutler.R;
import com.zhonghao.mybutler.entity.MyUser;
import com.zhonghao.mybutler.ui.CourierActivity;
import com.zhonghao.mybutler.ui.LoginActivity;
import com.zhonghao.mybutler.utils.L;
import com.zhonghao.mybutler.utils.UtilTools;
import com.zhonghao.mybutler.view.MyDialog;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.zhonghao.mybutler.R.id.et_age;
import static com.zhonghao.mybutler.R.id.et_desc;
import static com.zhonghao.mybutler.R.id.et_sex;
import static com.zhonghao.mybutler.R.id.et_username;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.fragment
 * 类描述：
 * 创建人：小豪
 * 创建时间：2017/6/22 16:39
 */

public class UserFragment extends Fragment implements View.OnClickListener {
    private static final String PHOTO_IMAGE_FILE_NAME = "fileImg.jpg";
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int IMAGE_REQUEST_CODE = 101;
    public static final int RESULT_REQUEST_CODE = 102;

    private File tempFile = null;
    CircleImageView mProfileName;
    @BindView(R.id.edit_user)
    TextView mEditUser;
    @BindView(et_username)
    EditText mEtUsername;
    @BindView(et_sex)
    EditText mEtSex;
    @BindView(et_age)
    EditText mEtAge;
    @BindView(et_desc)
    EditText mEtDesc;
    @BindView(R.id.btn_update_ok)
    Button mBtnUpdateOk;
    @BindView(R.id.tv_courier)
    TextView mTvCourier;
    @BindView(R.id.tv_phone)
    TextView mTvPhone;
    @BindView(R.id.btn_exit_user)
    Button mBtnExitUser;
    Unbinder unbinder;

    //提示框
    private MyDialog dialog;

    private Button btn_camera;
    private Button btn_picture;
    private Button btn_cancel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragemnt_user, null);
        unbinder = ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    private void initView(View view) {


        mProfileName = (CircleImageView) view.findViewById(R.id.profile_name);
        mBtnExitUser.setOnClickListener(this);
        mBtnUpdateOk.setOnClickListener(this);
        mTvCourier.setOnClickListener(this);
        mTvPhone.setOnClickListener(this);

        mEditUser.setOnClickListener(this);

        mProfileName.setOnClickListener(this);
        UtilTools.getImageFromShare(getActivity(), mProfileName);

        //默认是不可点击的/不可输入
        setEnabled(false);

        //设置具体的值
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        mEtUsername.setText(userInfo.getUsername());
        mEtAge.setText(userInfo.getAge() + "");
        mEtSex.setText(userInfo.isSex() ? getString(R.string.text_boy) : getString(R.string.text_girl_f));
        mEtDesc.setText(userInfo.getDesc());

        //初始化dialog
        dialog = new MyDialog(getActivity(), 0, 0, R.layout.dialog_photo, R.style.pop_anim_style
                , Gravity.BOTTOM, 0);
        //提示框外点击无效
        dialog.setCancelable(false);
        btn_camera = (Button) dialog.findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_picture = (Button) dialog.findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(this);
        btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_exit_user:
                //清除缓存用的对象
                MyUser.logOut();
                //现在currentUser是null
                BmobUser currentUser = MyUser.getCurrentUser();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
                break;
            case R.id.edit_user:
                setEnabled(true);
                mBtnUpdateOk.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_update_ok:
                //1.拿到输入框的值
                String username = mEtUsername.getText().toString();
                String sex = mEtSex.getText().toString();
                String age = mEtAge.getText().toString();
                String desc = mEtDesc.getText().toString();
                //2.判断是否为空
                if (!TextUtils.isEmpty(username) & !TextUtils.isEmpty(age) & !TextUtils.isEmpty(sex)) {
                    //3.更新属性
                    MyUser user = new MyUser();
                    user.setUsername(username);
                    user.setAge(Integer.parseInt(age));
                    //性别
                    if (sex.equals(getString(R.string.text_boy))) {
                        user.setSex(true);
                    } else {
                        user.setSex(false);
                    }
                    //简介
                    if (!TextUtils.isEmpty(desc)) {
                        user.setDesc(desc);
                    } else {
                        user.setDesc(getString(R.string.text_nothing));
                    }
                    BmobUser bmobUser = BmobUser.getCurrentUser();
                    user.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                //修改成功
                                setEnabled(false);
                                mBtnUpdateOk.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), R.string.text_editor_success, Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getActivity(), R.string.text_editor_failure, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), getString(R.string.text_tost_empty), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.profile_name:
                dialog.show();
                break;
            case R.id.btn_cancel:
                dialog.dismiss();
                break;
            case R.id.btn_camera:
                toCamera();
                break;
            case R.id.btn_picture:
                toPicture();
                break;
            case R.id.tv_courier:
                startActivity(new Intent(getActivity(),CourierActivity.class));
                break;
            case R.id.tv_phone:
                break;
        }
    }


    //跳转相机
    private void toCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断内存卡是否可用,可用的话就进行存储
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
                PHOTO_IMAGE_FILE_NAME)));
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
        dialog.dismiss();
    }

    //跳转相册
    private void toPicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_REQUEST_CODE);
        dialog.dismiss();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                //相册数据
                case IMAGE_REQUEST_CODE:
                    startPhotoZoom(data.getData());//裁剪图片的方法
                    break;
                //相机数据
                case CAMERA_REQUEST_CODE:
                    tempFile = new File(Environment.getExternalStorageDirectory(), PHOTO_IMAGE_FILE_NAME);
                    startPhotoZoom(Uri.fromFile(tempFile));
                case RESULT_REQUEST_CODE:
                    //有可能点击舍弃按钮
                    if (data != null) {
                        //拿到图片设置
                        setImageToView(data);
                        //既然已经设置了图片，我们原先的就应该删除
                        if (tempFile != null) {
                            tempFile.delete();
                        }
                    }
                    break;
            }
        }
    }


    //裁剪
    private void startPhotoZoom(Uri uri) {
        if (uri == null) {
            L.e("uri == null");
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        //设置裁剪
        intent.putExtra("crop", "true");
        //裁剪宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        //裁剪图片的质量
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 320);
        //发送数据
        intent.putExtra("return-data", true);
        startActivityForResult(intent, RESULT_REQUEST_CODE);

    }

    private void setImageToView(Intent data) {
        Bundle bundle = data.getExtras();
        if (bundle != null) {
            Bitmap bitmap = bundle.getParcelable("data");
            mProfileName.setImageBitmap(bitmap);
        }
    }


    //控制焦点
    private void setEnabled(boolean is) {
        mEtUsername.setEnabled(is);
        mEtSex.setEnabled(is);
        mEtAge.setEnabled(is);
        mEtDesc.setEnabled(is);
    }

    //通过sharePreference保存用户头像
    @Override
    public void onDestroy() {
        super.onDestroy();
        //保存
        UtilTools.putImageToShare(getActivity(),mProfileName);
    }
}
