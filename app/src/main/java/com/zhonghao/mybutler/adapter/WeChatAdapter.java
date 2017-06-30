package com.zhonghao.mybutler.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhonghao.mybutler.R;
import com.zhonghao.mybutler.entity.WeChatData;
import com.zhonghao.mybutler.ui.WebViewActivity;
import com.zhonghao.mybutler.utils.L;
import com.zhonghao.mybutler.utils.PicassoUtils;

import java.util.List;

/**
 * 项目名称：MyButler
 * 包名：com.zhonghao.mybutler.adapter
 * 类描述：
 * 创建人：小豪
 * 创建时间：2017/6/27 16:59
 */

public class WeChatAdapter extends BaseAdapter{
    private Context mContext;
    private LayoutInflater inflater;
    private List<WeChatData> mList;
    private WeChatData data;
    private int width,height;
    private WindowManager wm;
    private List<String> mTitles;
    private List<String> mUrls;

    public WeChatAdapter(Context mContext, List<WeChatData> mList,List<String> mTitles,List<String> mUrls) {
        this.mContext = mContext;
        this.mList = mList;
        this.mTitles = mTitles;
        this.mUrls = mUrls;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();
        height = wm.getDefaultDisplay().getHeight();
        L.i("Width:" + width + "Height:" + height);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.wechat_item, null);
            viewHolder.iv_img = (ImageView) convertView.findViewById(R.id.iv_img);
            viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tv_source = (TextView) convertView.findViewById(R.id.tv_source);
            viewHolder.cd = (CardView) convertView.findViewById(R.id.card_stories);

            convertView.setTag(viewHolder);

            viewHolder.cd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra("title", mTitles.get(position));
                    intent.putExtra("url", mUrls.get(position));
                    mContext.startActivity(intent);
                }
            });
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        data = mList.get(position);
        viewHolder.tv_title.setText(data.getTitle());
        viewHolder.tv_source.setText(data.getSource());
        if(!TextUtils.isEmpty(data.getImgUrl())){
            //加载图片
            PicassoUtils.loadImageViewSize(mContext, data.getImgUrl(), width/3, 150, viewHolder.iv_img);
        }

        return convertView;
    }

    class ViewHolder {
        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_source;
        private CardView cd;
    }
}
