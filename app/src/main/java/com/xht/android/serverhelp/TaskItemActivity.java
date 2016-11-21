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
import android.widget.Button;
import android.widget.TextView;

import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016-10-13.
 */
public class TaskItemActivity extends Activity{

    private TextView mTaskName;
    private TextView mName;
    private TextView mComName;
    private TextView mPhone;
    private TextView mTaskAddress;
    private TextView mProcedure;
    private TextView mTaskTime;
    private TextView mAwardMoney;
    private TextView mPunishment;
    private Button mRefuse;
    private Button mAccept;
    private int mUid; //用户id

    private static final String TAG = "TaskItemActivity";
    private String comName;
    private String addressName;
    private String flowName;
    private String contactName;
    private int orderId;
    private int position;
    private String phone;

    private ProgressDialog mProgDoal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskitem);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        mUid = bundle.getInt("uid",-1);
        LogHelper.i(TAG,"-----TaskItemActivity"+mUid);

        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        //初始化控件
        initialize();

        comName = bundle.getString("comName");
        addressName = bundle.getString("addressName");
        flowName = bundle.getString("styleName");
        contactName = bundle.getString("contactName");
        phone = bundle.getString("phone");
        orderId = bundle.getInt("orderId");
        position = bundle.getInt("position");

        LogHelper.i(TAG,"----TaskItem--muid"+mUid+ comName + addressName + flowName + contactName +"---"+ orderId+phone);

        updateView();

    }

    //更新显示数据
    private void updateView() {
        mTaskName.setText("公司注册");
        mComName.setText(comName);
        mName.setText(contactName);
        mPhone.setText(phone);
        mTaskAddress.setText(addressName);
        mProcedure.setText(flowName);
        mTaskTime.setText("10天");

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

    private void initialize() {

        mTaskName = (TextView) findViewById(R.id.mTaskName);
        mName = (TextView) findViewById(R.id.mName);
        mComName = (TextView) findViewById(R.id.mComName);
        mPhone = (TextView) findViewById(R.id.mPhone);
        mTaskAddress = (TextView) findViewById(R.id.mTaskAddress);
        mProcedure = (TextView) findViewById(R.id.mProcedure);
        mTaskTime = (TextView) findViewById(R.id.mTaskTime);
       // mAwardMoney = (TextView) findViewById(R.id.mAwardMoney);
        mPunishment = (TextView) findViewById(R.id.mPunishment);
        mRefuse = (Button) findViewById(R.id.mRefuse);
        mAccept = (Button) findViewById(R.id.mAccept);

        //拒绝任务
        mRefuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();


            }
        });

        //接受任务
        mAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                postTaskData(mUid);


            }
        });
    }


    //接受任务，提交服务器
    private void postTaskData(int uid) {
        JSONObject obj = new JSONObject();

        try {
            obj.put("userid",uid);//下单用户Id
            obj.put("orderid",orderId);//订单Id

            //obj.put("startTime",System.currentTimeMillis());//时间
            //obj.put("flowName",flowName);//当前步骤

            LogHelper.i(TAG,"-----用户id，订单id"+uid+orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogHelper.i(TAG, "----接受任务提交的所有信息" + obj.toString());
        VolleyHelpApi.getInstance().postTaskAccept(uid, obj, new APIListener() {
            @Override
            public void onResult(Object result) {
                App.getInstance().showToast("成功接受任务");

                Intent intent=getIntent();
                intent.putExtra("position",position);
                setResult(20,intent);
                TaskItemActivity.this.finish();
            }
            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
            }
        });
        dismissProgressDialog();
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

}
