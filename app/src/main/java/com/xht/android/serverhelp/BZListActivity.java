package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.xht.android.serverhelp.model.BZItem;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 2016-10-13 办证中列表
 * @author czw
 */
public class BZListActivity extends Activity {
    private static final String TAG = "BZListActivity";

    private ProgressDialog mProgDoal;
    private ListView mListView;
    private List<BZItem> mBZItems = new ArrayList<>();
    private int mUid;
    private BanZAdapter bzAdapter;
    private String flowName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bz_list);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        mUid = bundle.getInt("uid",-1);
        LogHelper.i(TAG,"------uid-"+ mUid);

        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);


        mListView = (ListView) findViewById(R.id.listView);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(BZListActivity.this, RwxqActivity.class);
                int orderId = mBZItems.get(position).getOrdId();
                intent.putExtra("ordId", orderId);
                intent.putExtra("mUid",mUid);
                startActivity(intent);
            }
        });
      //  mBZItems.clear();
        fetchItemTask(mUid);


        final PullRefreshLayout layout = (PullRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        layout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        layout.setRefreshing(false);
                        //getTaskBarData();
                        // 刷新3秒完成
                          mBZItems.clear();
                        fetchItemTask(mUid);
                        App.getInstance().showToast("刷新完成");

                    }
                }, 3000);
            }
        });

        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bundle = getIntent().getBundleExtra("bundle");
        mUid = bundle.getInt("uid",-1);
        LogHelper.i(TAG,"------onResume--uid-"+ mUid);

        LogHelper.i(TAG,"------onResume--fetchItemTaskuid-"+ mUid);

    }

    /**
     * 加载列表数据
     * @param id 客服id
     */
    private void fetchItemTask(int id) {
        createProgressDialog("数据加载中...");
        VolleyHelpApi.getInstance().getBZItems(id, new APIListener() {
            @Override
            public void onResult(Object result) {
                //{"message":"系统错误","result":"error","entity":null,"code":"0"}
                String code = ((JSONObject) result).optString("code");
                if (code.equals("0")){
                    App.getInstance().showToast("办证中暂无数据");
                    dismissProgressDialog();
                    BZListActivity.this.finish();
                }else {
                    JSONArray jsonArray = ((JSONObject) result).optJSONArray("entity");
                    LogHelper.i(TAG, result.toString());
                    parseBZItems(jsonArray);
                    setupAdapter();
                    dismissProgressDialog();
                }
            }

            @Override
            public void onError(Object e) {
                JSONObject obj=(JSONObject)e;
                if (obj.optString("code").equals("0")){
                    App.getInstance().showToast("办证中暂无数据");

                    dismissProgressDialog();
                    BZListActivity.this.finish();

                }
                dismissProgressDialog();
                App.getInstance().showToast(e.toString());
            }
        });
    }

    private void parseBZItems(JSONArray result) {
        LogHelper.i(TAG,"------BZListActivity--"+result);
        BZItem bZItem;
        String name;
        JSONObject jO;
        int length = result.length();
        for (int i = 0; i < length; i++) {
            try {
                //[{"countyName":"石岐区","companyName":"有意义","robOrderTime":"1479284665457","contactName":"他天天","orderId":3,"flowId":1}]
                jO = result.getJSONObject(i);

                bZItem = new BZItem();
                bZItem.setTime(jO.optString("robOrderTime"));
                bZItem.setKehu(jO.getString("contactName"));
                bZItem.setComp(jO.getString("companyName"));
                bZItem.setCompArea(jO.getString("countyName"));
                bZItem.setOrdId(jO.getInt("orderId"));
                bZItem.setFlowId(jO.getString("flowId"));
                mBZItems.add(bZItem);
                //mBZItems.add(bZItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void setupAdapter() {
        bzAdapter = new BanZAdapter(mBZItems);
        if (mListView == null) {
            return;
        }
        if (mBZItems != null) {
            mListView.setAdapter(bzAdapter);
        } else {
            mListView.setAdapter(null);
        }
    }

    private class BanZAdapter extends ArrayAdapter<BZItem> {

        public BanZAdapter(List<BZItem> items) {
            super(BZListActivity.this, 0, items);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = BZListActivity.this.getLayoutInflater().inflate(R.layout.bzmx_item, parent, false);
                holder = new ViewHolder();
                holder.nameTV = (TextView) convertView.findViewById(R.id.nameTV);
                holder.timeTV = (TextView) convertView.findViewById(R.id.timeTV);
                holder.kehuTV = (TextView) convertView.findViewById(R.id.khTV);
                holder.compTV = (TextView) convertView.findViewById(R.id.compTV);
                holder.areaTV = (TextView) convertView.findViewById(R.id.quyuTV);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            BZItem item = getItem(position);

            String flowId = item.getFlowId();
            switch (flowId){
                case "1":
                    flowName = "企业核名";
                    break;
                case "2":
                    flowName = "工商注册";
                    break;
                case "3":
                    flowName = "雕刻印章";
                    break;
                case "4":
                    flowName = "银行开户";
                    break;
                case "5":
                    flowName = "国税报道";
                    break;
                case "6":
                    flowName = "地税报到";
                    break;

            }

            holder.nameTV.setText(flowName);
            String time = item.getTime();
            if (!time.equals("null")){
                long mTime=Long.valueOf(time);
                holder.timeTV.setText(getDateTime(mTime));
            }

            holder.kehuTV.setText(item.getKehu());
            holder.compTV.setText(item.getComp());
            holder.areaTV.setText(item.getCompArea());

            return convertView;
        }

    }

    private class ViewHolder {
        TextView nameTV;
        TextView timeTV;
        TextView kehuTV;
        TextView compTV;
        TextView areaTV;
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
    private void createProgressDialog(String title) {
        if (mProgDoal == null) {
            mProgDoal = new ProgressDialog(this);
        }
        mProgDoal.setTitle(title);
        mProgDoal.setIndeterminate(true);
        mProgDoal.setCancelable(false);
        mProgDoal.show();
    }

    /**
     * 隐藏mProgressDialog
     */
    private void dismissProgressDialog()
    {
        if(mProgDoal != null)
        {
            mProgDoal.dismiss();
            mProgDoal = null;
        }
    }
}
