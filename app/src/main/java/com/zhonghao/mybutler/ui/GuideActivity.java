package com.zhonghao.mybutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhonghao.mybutler.R;
import com.zhonghao.mybutler.utils.L;
import com.zhonghao.mybutler.utils.UtilTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.zhonghao.mybutler.R.id.iv_back;
import static com.zhonghao.mybutler.R.id.point1;
import static com.zhonghao.mybutler.R.id.point2;
import static com.zhonghao.mybutler.R.id.point3;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.ui
 * 类描述：引导页
 * 创建人：小豪
 * 创建时间：2017/6/22 19:45
 */

public class GuideActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(point1)
    ImageView mPoint1;
    @BindView(point2)
    ImageView mPoint2;
    @BindView(point3)
    ImageView mPoint3;
    @BindView(iv_back)
    ImageView mIvBack;

    //容器
    private List<View> mList = new ArrayList<>();
    private View view1, view2, view3;

    private TextView tv_pager_1, tv_pager_2, tv_pager_3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mIvBack.setOnClickListener(this);

        view1 = View.inflate(this, R.layout.pager_item_one, null);
        view2 = View.inflate(this, R.layout.pager_item_two, null);
        view3 = View.inflate(this, R.layout.pager_item_three, null);
        view3.findViewById(R.id.btn_start).setOnClickListener(this);

        //设置默认图片
        setPointImg(true, false, false);

        tv_pager_1 = (TextView) view1.findViewById(R.id.tv_pager_1);
        tv_pager_2 = (TextView) view2.findViewById(R.id.tv_pager_2);
        tv_pager_3 = (TextView) view3.findViewById(R.id.tv_pager_3);

        UtilTools.setFont(this,tv_pager_1);
        UtilTools.setFont(this,tv_pager_2);
        UtilTools.setFont(this,tv_pager_3);

        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        //设置适配器
        mViewPager.setAdapter(new GuideAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                L.i("position: "+ position);
                switch (position){
                    case 0:
                        setPointImg(true,false,false);
                        mIvBack.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        setPointImg(false,true,false);
                        mIvBack.setVisibility(View.VISIBLE);
                        break;
                    case 2:
                        setPointImg(false,false,true);
                        mIvBack.setVisibility(View.GONE);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class GuideAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view ==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager)container).addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView(mList.get(position));
        }
    }

    //设置小圆点的选中效果
    private void setPointImg(boolean isCheck1, boolean isCheck2, boolean isCheck3) {
        if (isCheck1) {
            mPoint1.setBackgroundResource(R.drawable.point_on);
        } else {
            mPoint1.setBackgroundResource(R.drawable.point_off);
        }

        if (isCheck2) {
            mPoint2.setBackgroundResource(R.drawable.point_on);
        } else {
            mPoint2.setBackgroundResource(R.drawable.point_off);
        }

        if (isCheck3) {
            mPoint3.setBackgroundResource(R.drawable.point_on);
        } else {
            mPoint3.setBackgroundResource(R.drawable.point_off);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
            case R.id.iv_back:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
