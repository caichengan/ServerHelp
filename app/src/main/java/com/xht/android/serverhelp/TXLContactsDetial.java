package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xht.android.serverhelp.model.ContactsAdapter;
import com.xht.android.serverhelp.model.ContactsBean;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-11-1.
 */
public class TXLContactsDetial extends Activity implements View.OnClickListener {

    private String name;
    private String phoneNum;
    private static final String TAG = "TXLContactsDetial";
    private ImageView head;
    private TextView mCallName;
    private ImageView message;
    private TextView mCallPhone;
    private ImageView call;
    private RelativeLayout relayoutcall;
    private LinearLayout linCompanyName;
    private String mId;
    private TextView tvCompany;
    private ListView mListCompanyName;
    private List<ContactsBean> mDataList;
    private ContactsAdapter mConAdapter;
    private String style;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactsdetial);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        name = bundle.getString("mContactName");
        phoneNum = bundle.getString("mContactsPhone");//  bundle.putString("mId",mId);
        mId = bundle.getString("mId");
        style = bundle.getString("style");

        LogHelper.i(TAG,"----"+name+phoneNum+mId);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        initialize();

        initData();

        if (style.equals("1")) {
            getCompanyName(mId);
            mListCompanyName.setVisibility(View.VISIBLE);
        }else{
            mListCompanyName.setVisibility(View.GONE);
        }

    }

    private void getCompanyName(String mId) {

        VolleyHelpApi.getInstance().getCompanyName(mId, new APIListener() {
            @Override
            public void onResult(Object result) {

                //{"message":"公司加载成功","result":"success",
                // "entity":[{"userid":14,"companyName":"春天","companyId":60},{"userid":14,"companyName":"平湖","companyId":61}

                JSONObject object1= (JSONObject) result;
                String code = object1.optString("code");
                if (code.equals("0")){
                    return;
                }
                JSONArray jobArray=object1.optJSONArray("entity");
                LogHelper.i(TAG,"------gngsi---"+jobArray.toString());
                //{"message":"没有公司","result":"error","entity":null,"code":"0"}


                int length = jobArray.length();
                for (int i=0;i<length;i++){
                    ContactsBean item=new ContactsBean();
                    try {
                        JSONObject object= (JSONObject) jobArray.get(i);
                        String userid = object.getString("userid");
                        String companyName = object.getString("companyName");
                        String companyId = object.getString("companyId");

                        item.setUserId(userid);
                        item.setCompanyId(companyId);
                        item.setCompanyName(companyName);
                        LogHelper.i(TAG,"---------"+userid+companyId+companyName);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    mDataList.add(item);

                }

                udpateListView();
            }

            @Override
            public void onError(Object e) {

            }
        });

    }

    private void udpateListView() {

        mConAdapter = new ContactsAdapter(mDataList,this);

        mListCompanyName.setAdapter(mConAdapter);

    }

    private void initData() {
        mDataList = new ArrayList<ContactsBean>();
        if (name==null||name==""||name.equals("null")){
         mCallName.setText(phoneNum);
        }else {
            mCallName.setText(name);
        }
        mCallPhone.setText(phoneNum);
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

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.message:
                //发送信息
                sendMessage();
                break;
            case R.id.call:

            case R.id.relayout_call:
                //拨打电话
                callPhone();
                break;


        }



    }

    //拨打电话
    private void callPhone() {

        LogHelper.i(TAG,"----打电话");
        Intent intent = new Intent(Intent.ACTION_CALL);

        Uri data = Uri.parse("tel:" + phoneNum);

        intent.setData(data);

        startActivity(intent);
    }

    //发送消息
    private void sendMessage() {
        LogHelper.i(TAG,"----发消息");

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.putExtra("address", phoneNum);
            intent.putExtra("sms_body", "");
            intent.setType("vnd.android-dir/mms-sms");
            startActivity(intent);



    }

    private void initialize() {

        head = (ImageView) findViewById(R.id.head);
        mCallName = (TextView) findViewById(R.id.mCallName);//姓名
        message = (ImageView) findViewById(R.id.message);//发送消息
        mCallPhone = (TextView) findViewById(R.id.mCallPhone);//电话号码
        call = (ImageView) findViewById(R.id.call);//点击打电话
        relayoutcall = (RelativeLayout) findViewById(R.id.relayout_call);//点击打电话
        linCompanyName = (LinearLayout) findViewById(R.id.linCompanyName);//添加公司信息
        tvCompany = (TextView) findViewById(R.id.tvCompany);

        mListCompanyName = (ListView) findViewById(R.id.tvCompany1);

        relayoutcall.setOnClickListener(this);
        call.setOnClickListener(this);
        message.setOnClickListener(this);
    }



}
