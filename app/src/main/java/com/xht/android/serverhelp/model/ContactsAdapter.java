package com.xht.android.serverhelp.model;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.xht.android.serverhelp.R;

import java.util.List;

/**
 * Created by Administrator on 2016/11/24.
 */

public class ContactsAdapter extends BaseAdapter {

    private Context mContext;
    private List<ContactsBean> mListData;
    public ContactsAdapter(List<ContactsBean> mListData,Context mContext){
        this.mListData=mListData;
        this.mContext=mContext;
    }

    @Override
    public int getCount() {
        if (mListData.size()>0){
            return mListData.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.contacts_item,null);
            holder.tvComName= (TextView) convertView.findViewById(R.id.tvComName);


            convertView.setTag(holder);

        }else{
            holder= (ViewHolder) convertView.getTag();

        }
        if (mListData.size()==0){
            holder.tvComName.setText("暂无数据");
        }
        String companyName = mListData.get(position).getCompanyName();

        holder.tvComName.setText(companyName);


        return convertView;
    }
    class  ViewHolder{
        TextView tvComName;
    }
}
