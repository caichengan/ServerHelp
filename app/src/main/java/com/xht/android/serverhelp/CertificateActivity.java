package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.baoyz.widget.PullRefreshLayout;
import com.xht.android.serverhelp.model.TaskDescBean;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.IntentUtils;
import com.xht.android.serverhelp.util.LogHelper;
import com.xht.android.serverhelp.view.RefreshableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-10-12.
 *
 *   an 任务池
 */
public class CertificateActivity extends Activity implements AdapterView.OnItemClickListener {


    private int mUid; //用户id

    private String companyname; //公司名称
    private String operationname; //业务类型
    private String addressname; // 地区

    boolean addFlag=false;

    private ArrayList<TaskDescBean> mTaskBar=new ArrayList();
    private ArrayList<TaskDescBean> mTaskAdd=new ArrayList();

    private static final String TAG = "CertificateActivity";

    private EditText autoText;
    private Spinner spinsel;
    private Button refreshbut;
    private ListView rwlistview;
    private TaskBarAdapter mTaskBarAdapter;
    private TaskAddAdapter mTaskAddAdapter;
    private int mAreaSelect;
    private int mStyleSelect;
    private String address;
    private String autoString;
    private RefreshableView refreshableview;
    private ProgressDialog mProgDoal;
    private Spinner mSpinner;
    private String style;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_certificate);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        mUid = bundle.getInt("uid",-1);
        LogHelper.i(TAG,"---"+mUid);

        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("任务池");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        initialize();//初始化控件

        initData(); //初始化数据
       getTaskBarData();//TODO 访问网络数据

    }

    //初始化数据
    private void initData() {

        final ArrayAdapter<CharSequence> mAdapter = ArrayAdapter.createFromResource(this, R.array.style, android.R.layout.simple_spinner_item);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinner.setAdapter(mAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                style = mSpinner.getSelectedItem().toString();
                LogHelper.i("-------style", style);
                mStyleSelect = position + 1;
                LogHelper.i(TAG,"----选中-"+mStyleSelect);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        final ArrayAdapter<CharSequence> arrayAdapter = ArrayAdapter.createFromResource(this, R.array.areas, android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinsel.setAdapter(arrayAdapter);
        spinsel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                address = spinsel.getSelectedItem().toString();
                LogHelper.i("-------spinner1", address);
                mAreaSelect = position + 1;
                LogHelper.i(TAG,"----选中-"+mAreaSelect);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        //按钮刷新
        refreshbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RefreshMethod2();
                //RefreshMethod();
            }
        });

        /**
         *   刷新列表访问数据
         *   TODO  数据增加了一倍
         */
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
                        RefreshMethod2();
                        //RefreshMethod();
                        App.getInstance().showToast("刷新完成");
                    }
                }, 3000);
            }
        });

        layout.setRefreshStyle(PullRefreshLayout.STYLE_MATERIAL);


    }

    public void RefreshMethod2() {
        createProgressDialog("正在搜索数据...");
        mTaskAdd.clear();
        autoString = autoText.getText().toString();

        LogHelper.i(TAG,"----输入的公司名"+autoString,style);
        LogHelper.i(TAG,"----输入的公司名"+autoString,address);

        JSONObject jsonObject=new JSONObject();
        try {
            jsonObject.put("value",autoString);
            jsonObject.put("style",mStyleSelect);
            jsonObject.put("address",address);


            LogHelper.i(TAG,"-------style-"+jsonObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        VolleyHelpApi.getInstance().getTaskAddData(jsonObject,new APIListener() {
            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG, "---------getTaskAddData----所有信息--" + result.toString());

                JSONArray JsonAy = null;

                String code = ((JSONObject) result).optString("code");
                if (code.equals("0")){
                    App.getInstance().showToast("任务池中无数据符合要求");
                    mTaskAddAdapter.notifyDataSetChanged();
                    dismissProgressDialog();
                    return;

                }else {
                    try {
                        JsonAy = ((JSONObject) result).getJSONArray("entity");
                        int JsonArryLenth = JsonAy.length();

                        for (int i = 0; i < JsonArryLenth; i++) {
                            TaskDescBean item = new TaskDescBean();
                            JSONObject JsonItem = (JSONObject) JsonAy.get(i);
                            String countyName = JsonItem.getString("countyName");
                            String flowName = JsonItem.getString("flowName");//起始步骤
                            String companyName = JsonItem.getString("companyName");
                            String contactName = JsonItem.getString("contactName");
                            String telephone = JsonItem.getString("telephone");
                            int orderId = JsonItem.getInt("orderId");

                            item.setmComName(companyName);
                            item.setmAddressName(countyName);
                            item.setmStyleName(flowName);//TODO flowName==styleName

                            //    item.setmBusinessStyle(mBusinessStyle);//mBusinessStyle  是业务类型：公司注册、社保、发票、注册资金等
                            item.setmContactName(contactName);
                            item.setmPhone(telephone);
                            item.setmOrderId(orderId);

                            mTaskAdd.add(item);

                            LogHelper.i(TAG, "-----任务池--" + i + "--" + countyName + companyName + contactName + orderId + telephone + flowName);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dismissProgressDialog();
                    rwlistview.setAdapter(mTaskAddAdapter);
                    mTaskAddAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
                dismissProgressDialog();
            }
        });

    }


    public void RefreshMethod() {
       createProgressDialog("正在刷新数据...");

        mTaskAdd.clear();
        autoString = autoText.getText().toString();
        //刷新
        LogHelper.i(TAG,"----输入的公司名"+autoString);
        for (TaskDescBean bean:mTaskBar) {
            String addName = bean.getmAddressName();
            String comName = bean.getmComName();
            String styleName = bean.getmStyleName();

            if (address.equals("全部")){
                if (comName.equals(autoString) ) {
                    mTaskAdd.add(bean);
                    LogHelper.i(TAG, "----公司输入1111" + comName);
                }
                if (styleName.equals(autoString)) {
                    mTaskAdd.add(bean);
                    LogHelper.i(TAG, "----业务输入222" + styleName);
                }
            }
            if (TextUtils.isEmpty(autoString)&&address.equals("全部")){
                mTaskAdd.add(bean);
            }
            if (TextUtils.isEmpty(autoString)) {
                if (addName.equals(address)) {
                    mTaskAdd.add(bean);
                    LogHelper.i(TAG, "----flag" + addName);
                }
            } else {
                if (comName.equals(autoString) && addName.equals(address)) {
                    mTaskAdd.add(bean);
                    LogHelper.i(TAG, "----公司名输入和地区" + comName + addName);
                }
               if (styleName.equals(autoString) && addName.equals(address)) {
                    mTaskAdd.add(bean);
                    LogHelper.i(TAG, "----业务输入和地区" + styleName + addName);
                }
            }
        }
        dismissProgressDialog();
        rwlistview.setAdapter(mTaskAddAdapter);
        mTaskAddAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    //访问数据
    private void getTaskBarData() {

        mTaskBar.clear();
      createProgressDialog("正在加载数据...");
            VolleyHelpApi.getInstance().getTaskBarData(new APIListener() {
                @Override
                public void onResult(Object result) {
                    LogHelper.i(TAG, "-----所有信息--" + result.toString());
                    //{"message":"没有任务池信息123","result":"error","entity":null,"code":"0"}
                 //[{"countyName":"石岐区","startStepName":"企业核名","contactName":"山头","companyName":"中山第一办证有限公司","flowName":"企业核名","telephone":"13392924040","orderId":31},
                    // {"countyName":"石岐区","startStepName":"企业核名","contactName":"近距离","companyName":"天魔斩","flowName":"企业核名","telephone":"15886688888899","orderId":36},
                    // {"countyName":"石岐区","startStepName":"企业核名","contactName":"墨鱼想","companyName":"注册公司测试1","flowName":"企业核名","telephone":"18675009880","orderId":37}]
                    JSONArray JsonAy = null;

                    String code = ((JSONObject) result).optString("code");
                    if (code.equals("0")){
                        App.getInstance().showToast("任务池中暂无数据");
                        dismissProgressDialog();
                        CertificateActivity.this.finish();
                    }else {
                        try {
                            JsonAy = ((JSONObject) result).getJSONArray("entity");
                            int JsonArryLenth = JsonAy.length();

                            for (int i = 0; i < JsonArryLenth; i++) {
                                TaskDescBean item = new TaskDescBean();
                                JSONObject JsonItem = (JSONObject) JsonAy.get(i);
                                String countyName = JsonItem.getString("countyName");
                                String flowName = JsonItem.getString("flowName");//起始步骤
                                String companyName = JsonItem.getString("companyName");
                                String contactName = JsonItem.getString("contactName");
                                String telephone = JsonItem.getString("telephone");
                                int orderId = JsonItem.getInt("orderId");

                                item.setmComName(companyName);
                                item.setmAddressName(countyName);
                                item.setmStyleName(flowName);//TODO flowName==styleName

                                //    item.setmBusinessStyle(mBusinessStyle);//mBusinessStyle  是业务类型：公司注册、社保、发票、注册资金等
                                item.setmContactName(contactName);
                                item.setmPhone(telephone);
                                item.setmOrderId(orderId);

                                mTaskBar.add(item);

                                LogHelper.i(TAG, "-----任务池--" + i + "--" + countyName + companyName + contactName + orderId + telephone + flowName);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        rwlistview.setAdapter(mTaskBarAdapter);
                        mTaskBarAdapter.notifyDataSetChanged();
                        dismissProgressDialog();
                    }
                }
                @Override
                public void onError(Object e) {
                    App.getInstance().showToast(e.toString());
                    dismissProgressDialog();
                }
            });
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
    private void initialize() {
        autoText = (EditText) findViewById(R.id.autoText);
        spinsel = (Spinner) findViewById(R.id.spin_sel);
        refreshbut = (Button) findViewById(R.id.refresh_but);
        rwlistview = (ListView) findViewById(R.id.rw_listview);
        mSpinner = (Spinner) findViewById(R.id.spinner_style);
        rwlistview.setOnItemClickListener(this);
        mTaskBarAdapter=new TaskBarAdapter();
        mTaskAddAdapter=new TaskAddAdapter();
        rwlistview.setAdapter(mTaskBarAdapter);

    }

    class TaskBarAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            if (mTaskBar.size() > 0) {
                    return mTaskBar.size();
            }
            return 0;
        }
        @Override
        public Object getItem(int position) {
                return mTaskBar.get(position);
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
                convertView=View.inflate(CertificateActivity.this,R.layout.item_list_task,null);

                holder.mCompanyName = (TextView) convertView.findViewById(R.id.company_name);
                holder.mOperateName = (TextView) convertView.findViewById(R.id.operation_name);
                holder.mAddressName = (TextView)convertView.findViewById(R.id.address_name);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
                TaskDescBean item = mTaskBar.get(position);
                holder.mCompanyName.setText(item.getmComName());
                holder.mOperateName.setText(item.getmStyleName());
                holder.mAddressName.setText(item.getmAddressName());
            return convertView;
        }
    }
    class TaskAddAdapter extends BaseAdapter{
        @Override
        public int getCount() {
                if (mTaskAdd.size() > 0) {
                    return mTaskAdd.size();
                }
            return 0;
        }
        @Override
        public Object getItem(int position) {
                return mTaskAdd.get(position);
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
                convertView=View.inflate(CertificateActivity.this,R.layout.item_list_task,null);
                holder.mCompanyName = (TextView) convertView.findViewById(R.id.company_name);
                holder.mOperateName = (TextView) convertView.findViewById(R.id.operation_name);
                holder.mAddressName = (TextView)convertView.findViewById(R.id.address_name);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }
                TaskDescBean item=mTaskAdd.get(position);
                holder.mCompanyName.setText(item.getmComName());
                holder.mOperateName.setText(item.getmStyleName());
                holder.mAddressName.setText(item.getmAddressName());
            return convertView;
        }
    }
    static class ViewHolder{
        TextView mCompanyName;
        TextView mOperateName;
        TextView mAddressName;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TaskDescBean bean;
        if (mTaskAdd.size()>0){
             bean=mTaskAdd.get(position);
            String comName = bean.getmComName();
            String addressName = bean.getmAddressName();
            String styleName = bean.getmStyleName();
            String contactName = bean.getmContactName();
            String phone = bean.getmPhone();
            int orderId = bean.getmOrderId();
            LogHelper.i(TAG,"-----addTask---"+position);

            Bundle bundle=new Bundle();
            bundle.putString("comName",comName);
            bundle.putString("addressName",addressName);
            bundle.putString("styleName",styleName);
            bundle.putString("contactName",contactName);
            bundle.putString("phone",phone);
            bundle.putInt("orderId",orderId);
            bundle.putInt("position", position);

            bundle.putInt("uid",mUid);
            //进入任务池具体Item
            IntentUtils.startActivityNumberForResult(this, bundle, TaskItemActivity.class);
        }else {

            bean = mTaskBar.get(position);
            String comName = bean.getmComName();
            String addressName = bean.getmAddressName();
            String styleName = bean.getmStyleName();
            String contactName = bean.getmContactName();
            String phone = bean.getmPhone();
            int orderId = bean.getmOrderId();

            LogHelper.i(TAG, "-----mTaskBar---" +position);
            Bundle bundle = new Bundle();
            bundle.putString("comName", comName);
            bundle.putString("addressName", addressName);
            bundle.putString("styleName", styleName);
            bundle.putString("contactName", contactName);
            bundle.putString("phone", phone);
            bundle.putInt("position", position);

            bundle.putInt("orderId", orderId);
            bundle.putInt("uid", mUid);
            //进入任务池具体Item
            IntentUtils.startActivityNumberForResult(this, bundle, TaskItemActivity.class);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode==20){
            int position = data.getIntExtra("position", -1);
            if (position>-1){
                if (mTaskAdd.size()>0) {
                    mTaskAdd.remove(position);
                    mTaskBar.remove(position);
                    mTaskAddAdapter.notifyDataSetChanged();
                }else {
                    mTaskBar.remove(position);
                    mTaskBarAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 隐藏对话框
     */
    private void dismissProgressDialog() {
        if (mProgDoal != null) {
            mProgDoal.dismiss();
            mProgDoal = null;
        }
    }

    /**
     * 创建对话框
     *
     * @param title
     */
    private void createProgressDialog(String title) {
        if (mProgDoal == null) {
            mProgDoal = new ProgressDialog(this);
        }
        mProgDoal.setTitle(title);
        mProgDoal.setIndeterminate(true);
        mProgDoal.setCancelable(false);
        mProgDoal.show();
    }
}
