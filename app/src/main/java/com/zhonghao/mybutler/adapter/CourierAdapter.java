package com.zhonghao.mybutler.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhonghao.mybutler.R;
import com.zhonghao.mybutler.entity.CourierData;

import java.util.List;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.adapter
 * 类描述：
 * 创建人：小豪
 * 创建时间：2017/6/27 15:56
 */

public class CourierAdapter extends BaseAdapter {
    private Context mContext;
    private List<CourierData> mLists;
    private LayoutInflater mInflater;
    private CourierData data;


    public CourierAdapter(Context context, List<CourierData> lists) {
        mContext = context;
        mLists = lists;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mLists.size();
    }

    @Override
    public Object getItem(int position) {
        return mLists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh = null;
        //第一次加载
        if (convertView == null) {
            vh = new ViewHolder();
            convertView = mInflater.inflate(R.layout.layout_courier_item, null);
            vh.tv_remark = (TextView) convertView.findViewById(R.id.tv_remark);
            vh.tv_zone = (TextView) convertView.findViewById(R.id.tv_zone);
            vh.tv_datetime = (TextView) convertView.findViewById(R.id.tv_datetime);

            //设置缓存
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        //设置数据
        data = mLists.get(position);
        vh.tv_remark.setText(data.getRemark());
        vh.tv_zone.setText(data.getZone());
        vh.tv_datetime.setText(data.getDatetime());

        return convertView;
    }

    class ViewHolder {
        private TextView tv_remark;
        private TextView tv_zone;
        private TextView tv_datetime;
    }
}
