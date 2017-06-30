package com.zhonghao.mybutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.zhonghao.mybutler.R;
import com.zhonghao.mybutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.ui
 * 类描述：
 * 创建人：小豪
 * 创建时间：2017/6/29 16:49
 */

public class AboutActivity extends BaseActivity {
    @BindView(R.id.profile_image)
    CircleImageView mProfileImage;
    @BindView(R.id.mListView)
    ListView mMListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        //去除阴影
        getSupportActionBar().setElevation(0);

        initData();
    }

    private void initData() {
        mList.add("应用名："+ " 我的管家");
        mList.add("版本号："+ UtilTools.getVersion(this));
        mList.add("作者个人博客主页：http://blog.csdn.net/fighting_boss");
        mList.add("作者个人Github：https://github.com/hanzhonghao");
        mAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,mList);
        //设置适配器
        mMListView.setAdapter(mAdapter);
    }
}
