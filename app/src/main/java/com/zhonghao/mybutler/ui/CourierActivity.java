package com.zhonghao.mybutler.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.zhonghao.mybutler.R;
import com.zhonghao.mybutler.adapter.CourierAdapter;
import com.zhonghao.mybutler.entity.CourierData;
import com.zhonghao.mybutler.utils.L;
import com.zhonghao.mybutler.utils.StaticClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.ui
 * 类描述：
 * 创建人：小豪
 * 创建时间：2017/6/27 15:17
 */

public class CourierActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_number)
    EditText mEtNumber;
    @BindView(R.id.btn_get_courier)
    Button mBtnGetCourier;
    @BindView(R.id.listView)
    ListView mListView;

    private List<CourierData> mList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_courier);
        ButterKnife.bind(this);
        initListener();
    }

    private void initListener() {
        mBtnGetCourier.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_get_courier:
                /**
                 * 1.获取输入框的内容
                 * 2.判断是否为空
                 * 3.拿到数据去请求数据（Json）
                 * 4.解析Json
                 * 5.listview适配器
                 * 6.实体类（item）
                 * 7.设置数据/显示效果
                 */
                //拼接Url
                String name = mEtName.getText().toString().trim();
                String number = mEtNumber.getText().toString().trim();

                String url= "http://v.juhe.cn/exp/index?key="+ StaticClass.COURIER_KEY
                        +"&com="+ name +"&no="+ number;

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(number)){
                    RxVolley.get(url, new HttpCallback() {
                        @Override
                        public void onSuccess(String t) {
                            //Toast.makeText(CourierActivity.this, t, Toast.LENGTH_SHORT).show();
                            L.i("Courier:" + t);
                            //4.解析Json
                            parsingJson(t);
                        }
                    });
                }else{
                    Toast.makeText(this, "输入框不能为空", Toast.LENGTH_SHORT).show();
                }
        }
    }

    //解析数据
    private void parsingJson(String t){
        try {
            JSONObject jsonObject = new JSONObject(t);
            JSONObject jsonResult = jsonObject.getJSONObject("result");
            JSONArray jsonArray = jsonResult.getJSONArray("list");
            for (int i = 0;i<jsonArray.length();i++){
                JSONObject json = (JSONObject) jsonArray.get(i);
                CourierData data = new CourierData();
                data.setRemark(json.getString("remark"));
                data.setZone(json.getString("zone"));
                data.setDatetime(json.getString("datetime"));
                mList.add(data);
            }
            //倒序
            Collections.reverse(mList);
            CourierAdapter courierAdapter = new CourierAdapter(this, mList);
            mListView.setAdapter(courierAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
