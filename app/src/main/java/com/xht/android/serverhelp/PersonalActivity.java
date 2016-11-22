package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xht.android.serverhelp.model.UserInfo;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.IntentUtils;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016-10-13.
 *  安个人绩效
 */
public class PersonalActivity extends Activity implements View.OnClickListener {

    private ImageView mPersonImg;//头像
    private TextView mPersonName;//姓名
    private TextView mPersonBanZheng;//办证类型
    private TextView mPersonPhone;//电话号码
    private ImageView mPersonMa;//二维码
    private TextView mText;
    private TextView mMoney;//本月收入
    private TextView mTotalMoney;//累计总收入
    private TextView mServiceNum;//服务积分
    private TextView mNuclearNum;//企业核名
    private LinearLayout mTabLay01;
    private TextView mRegisterNum;//工商注册
    private LinearLayout mTabLay02;
    private TextView mStampNum;//雕刻印章
    private LinearLayout mTabLay03;
    private TextView mAccountNum;//银行开户
    private LinearLayout mTabLay04;
    private TextView mNationaltaxNum;//国税报道
    private LinearLayout mTabLay05;
    private TextView mPropertyTaxesNum;//地税报到
    private LinearLayout mTabLay06;

    private ProgressDialog mProgressDialog;
    private ChoosePicDialog mChoosePicDialog;
    private Uri mCurFromCamare;

    //访问网络拿到的数据
    private String mName;//姓名
    private String mStyle;//类型
    private String mPhone;//电话号码
    private String mTMoney;//总收入
    private String mYMoney;//月收入
    private String mJFNumber;//积分

    private String mQYHMNumber;//企业核名
    private String mGSZCNumber;//工商注册
    private String mDKYZNumber;//雕刻印章
    private String mYHKHNumber;//银行开户
    private String mGSBDNumber;//国税报道
    private String mDSBDNumber;//地税报道

private MainActivity mMainActivity;

    private int mUId;
    private static final String TAG = "PersonalActivity";
    private UserInfo instance;
    private long phoneNum;
    private String userName;
    private String mTempStrUR1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);



        Bundle bundle = getIntent().getBundleExtra("bundle");
        mUId = bundle.getInt("uid",-1);
        LogHelper.i(TAG,"-----PersonalActivity---"+mUId);

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

        //访问数据
        getPersonData(mUId);

    }

    @Override
    protected void onResume() {
        super.onResume();
        instance = mMainActivity.getInstance();
        phoneNum = instance.getPhoneNum();
        userName = instance.getUserName();
        mUId = instance.getUid();

        LogHelper.i(TAG,"-----PersonalActivity---"+mUId+ userName + phoneNum);

    }

    private void initialize() {

        mPersonImg = (ImageView) findViewById(R.id.mPersonImg);
        mPersonName = (TextView) findViewById(R.id.mPersonName);
        mPersonMa = (ImageView) findViewById(R.id.mPersonMa);
        mPersonBanZheng = (TextView) findViewById(R.id.mPersonBanZheng);
        mPersonPhone = (TextView) findViewById(R.id.mPersonPhone);
        mText = (TextView) findViewById(R.id.mText);
        mMoney = (TextView) findViewById(R.id.mMoney);
        mTotalMoney = (TextView) findViewById(R.id.mTotalMoney);
        mServiceNum = (TextView) findViewById(R.id.mServiceNum);
        mNuclearNum = (TextView) findViewById(R.id.mNuclearNum);
        mTabLay01 = (LinearLayout) findViewById(R.id.mTabLay01);
        mRegisterNum = (TextView) findViewById(R.id.mRegisterNum);
        mTabLay02 = (LinearLayout) findViewById(R.id.mTabLay02);
        mStampNum = (TextView) findViewById(R.id.mStampNum);
        mTabLay03 = (LinearLayout) findViewById(R.id.mTabLay03);
        mAccountNum = (TextView) findViewById(R.id.mAccountNum);
        mTabLay04 = (LinearLayout) findViewById(R.id.mTabLay04);
        mNationaltaxNum = (TextView) findViewById(R.id.mNationaltaxNum);
        mTabLay05 = (LinearLayout) findViewById(R.id.mTabLay05);
        mPropertyTaxesNum = (TextView) findViewById(R.id.mPropertyTaxesNum);
        mTabLay06 = (LinearLayout) findViewById(R.id.mTabLay06);

        mTabLay01.setOnClickListener(this);
        mTabLay02.setOnClickListener(this);
        mTabLay03.setOnClickListener(this);
        mTabLay04.setOnClickListener(this);
        mTabLay05.setOnClickListener(this);
        mTabLay06.setOnClickListener(this);

        mPersonImg.setOnClickListener(this);
    }


    //访问数据显示数量
    private void getPersonData(int uid) {

        VolleyHelpApi.getInstance().getPersonalData(uid,new APIListener() {

            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG, "----个人绩效所有信息--" + result.toString());
               //{"message":"数据加载成功","result":"success",
                // "entity":{"step3":2,"step2":2,"step1":2,"step6":2,"step5":3,"step4":2,"commission":1190,"currentMonthCommission":1190},"code":"1"}




                JSONObject JSONOB = ((JSONObject) result).optJSONObject("entity");
                 mTMoney = JSONOB.optString("commission");//总
                 mYMoney = JSONOB.optString("currentMonthCommission");//月
                mQYHMNumber = JSONOB.optString("step1");
                mGSZCNumber = JSONOB.optString("step2");
                mDKYZNumber = JSONOB.optString("step3");
                mYHKHNumber = JSONOB.optString("step4");
                mGSBDNumber = JSONOB.optString("step5");
                mDSBDNumber = JSONOB.optString("step6");

                LogHelper.i(TAG, "----个人绩效所有信息--" + JSONOB.toString());

                uDpateDataMethod();
            }
            @Override
            public void onError(Object e) {

                App.getInstance().showToast(e.toString());
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

    //更新数据
    private void uDpateDataMethod() {
        mPersonPhone.setText(phoneNum+"");
        mPersonName.setText(userName);
        mNuclearNum.setText(mQYHMNumber);
        mRegisterNum.setText(mGSZCNumber);
        mStampNum.setText(mDKYZNumber);
        mAccountNum.setText(mYHKHNumber);
        mNationaltaxNum.setText(mGSBDNumber);
        mPropertyTaxesNum.setText(mDSBDNumber);
        mMoney.setText(mYMoney);
        mTotalMoney.setText(mTMoney);
        mServiceNum.setText(0+"");
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){


            case R.id.mTabLay01://企业核名

                break;
            case R.id.mTabLay02://工商注册

                break;
            case R.id.mTabLay03://雕刻印章

                break;
            case R.id.mTabLay04://银行开户

                break;
            case R.id.mTabLay05://国税报到

                break;
            case R.id.mTabLay06://地税报到

                break;

        }


    }


}
