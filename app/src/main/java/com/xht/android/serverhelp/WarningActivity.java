package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.xht.android.serverhelp.model.WaringItemBean;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Administrator on 2016-10-17.
 */
public class WarningActivity extends Activity{

    private ListView mListWaring;
    private int mUId;

    private WarningAdapter adapter;
   private ArrayList<WaringItemBean> mListItem;
    private static final String TAG = "WarningActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waring_activity);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        mUId = bundle.getInt("uid",-1);
        LogHelper.i(TAG,"------WarningActivity-"+mUId);

        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        mListWaring = (ListView) findViewById(R.id.list_view_waring);

        /**
         * 下拉刷新
         */
        final PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // 刷新3秒完成
                        layout.setRefreshing(false);
                        mListItem.clear();
                        getWarningData();
                    }
                }, 3000);
            }
        });

        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);

        mListItem=new ArrayList<>();

        //请求数据
        getWarningData();


    }
    //访问数据
    private void getWarningData() {
        VolleyHelpApi.getInstance().getWarningData(mUId,new APIListener() {

            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG, "----所有信息--" + result.toString());
//{"message":"预警详情加载失败","result":"error","entity":null,"code":"0"}

               //[{"countyName":"石岐区","contactName":"叼","companyName":"北京","fdatediff":0,"flowName":"企业核名","robOrderTime":"1477301891000","orderId":21},
                // {"countyName":"石岐区","contactName":"叼","companyName":"急急急","fdatediff":0,"flowName":"企业核名","robOrderTime":"1477301798000","orderId":19},
                // {"countyName":"石岐区","contactName":"近距离","companyName":"看看","fdatediff":0,"flowName":"企业核名","robOrderTime":"1477301774000","orderId":18}]
                JSONArray JsonAy = null;
                String code = ((JSONObject) result).optString("code");
                if (code.equals("0")){
                    App.getInstance().showToast("暂无预警数据");
                    finish();
                }else {
                    try {
                        JsonAy = ((JSONObject) result).getJSONArray("entity");

                        int JsonArryLenth = JsonAy.length();
                        for (int i = 0; i < JsonArryLenth; i++) {
                            WaringItemBean item = new WaringItemBean();
                            JSONObject JsonItem = (JSONObject) JsonAy.get(i);

                            String countyName = JsonItem.getString("countyName");//地区
                            String flowName = JsonItem.getString("flowName");//起始步骤
                            String companyName = JsonItem.getString("companyName");//公司名
                            String contactName = JsonItem.getString("contactName");//客户
                            String robOrderTime = JsonItem.optString("robOrderTime");//时间
                            int fdatediff = JsonItem.getInt("fdatediff");//天数

                            //   int orderId = JsonItem.getInt("orderId");

                            item.setComName(companyName);
                            item.setmAddress(countyName);
                            item.setmDaty(fdatediff);
                            item.setmName(contactName);
                            if (TextUtils.isEmpty(robOrderTime)) {
                                long t = Long.parseLong(robOrderTime);
                                item.setmTime(getDateTime(t));
                            }
                            item.setTaskName(flowName);

                            LogHelper.i(TAG, "---所有信息--" + countyName + flowName + companyName + contactName + robOrderTime + fdatediff);
                            mListItem.add(item);

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    LogHelper.i(TAG, "---所有信息--" + JsonAy.toString());

                    adapter = new WarningAdapter(mListItem);
                    mListWaring.setAdapter(adapter);
                }
            }

            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
            }
        });

    }

    /**
     * 转换时间
     * @param time
     * @return
     */
    public String getDateTime(long time){
    Date date = new Date(time);
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    return sdf.format(date);
}


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    //listview的适配器
    class  WarningAdapter extends ArrayAdapter<WaringItemBean>{


        public WarningAdapter(ArrayList<WaringItemBean> item) {
            super(WarningActivity.this, 0, item);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView==null){
                holder=new ViewHolder();
                convertView=View.inflate(WarningActivity.this,R.layout.warning_activity,null);

                holder.mTextTask = (TextView)convertView. findViewById(R.id.warning_task);
                holder.mTextTime = (TextView) convertView.findViewById(R.id.warning_time);
                holder.mTextName = (TextView)convertView. findViewById(R.id.warning_mName);
                holder.mTextCompany = (TextView) convertView.findViewById(R.id.warning_mCompany);
                holder.mTextDay = (TextView)convertView. findViewById(R.id.warning_mDay);
                holder.mTextAddress = (TextView) convertView.findViewById(R.id.warning_mAddress);

                convertView.setTag(holder);
            }else{
                holder= (ViewHolder) convertView.getTag();
            }
            WaringItemBean itemBean = mListItem.get(position);

            holder.mTextTask.setText(itemBean.getTaskName());
            holder.mTextTime.setText(itemBean.getmTime());
            holder.mTextName.setText(itemBean.getmName());
            holder.mTextCompany.setText(itemBean.getComName());


            int day = itemBean.getmDaty();
            if (day<2){
                holder.mTextDay.setBackgroundColor(0Xff99cc00);
            }
            if (day>5&&day<9){
                holder.mTextDay.setBackgroundColor(0Xffff8800);
            }
            if (day>9){
                holder.mTextDay.setBackgroundColor(0Xffff4444);
            }

            holder.mTextDay.setText(day+"");
            holder.mTextAddress.setText(itemBean.getmAddress());

            return convertView;
        }
    }
    static class ViewHolder{
        TextView mTextTask;
        TextView mTextTime;
        TextView mTextName;
        TextView mTextCompany;
        TextView mTextDay;
        TextView mTextAddress;

    }
}
