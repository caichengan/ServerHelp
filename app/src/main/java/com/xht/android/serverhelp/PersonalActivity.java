package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
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
import com.xht.android.serverhelp.util.BitmapUtils;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2016-10-13.
 *  安个人绩效
 */
public class PersonalActivity extends Activity implements View.OnClickListener {






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

    private String step1;
    private String step2;
    private String step3;
    private String step4;
    private String step5;
    private String step6;

    private String step7;
    private String step8;
    private String step9;
    private String step10;
    private String step11;
    private String step12;

    private String step13;
    private String step14;
    private String step15;
    private String step16;
    private String step17;
    private String step18;
    private String step19;
    private String step20;

    private MainActivity mMainActivity;

    private int mUId;
    private static final String TAG = "PersonalActivity";
    private UserInfo instance;
    private long phoneNum;
    private String userName;
    private String mTempStrUR1;
    private String persomnalImg;
    private SharedPreferences mSHaredPreference;




    private ImageView mPersonImg;//头像
    private TextView mPersonName;//姓名
    private TextView mPersonBanZheng;//办证类型
    private TextView mPersonPhone;//电话号码
    private ImageView mPersonMa;//二维码
    private TextView mText;
    private TextView mMoney;//本月收入
    private TextView mTotalMoney;//累计总收入
    private TextView mServiceNum;//服务积分

    private TextView mReferenceNum;//资料交接
    private LinearLayout mTabLay01;
    private TextView mNuansNum;//工商核名
    private LinearLayout mTabLay02;
    private TextView mRegisterNum;//注册文件
    private LinearLayout mTabLay03;
    private TextView mSculptureNum;//雕刻印章
    private LinearLayout mTabLay04;
    private TextView mChapterNum;//公安拿章
    private LinearLayout mTabLay05;
    private TextView mBankOpenNum;//银行开户  6
    private LinearLayout mTabLay06;
    private TextView mBankEntrustNum;//银行委托
    private LinearLayout mTabLay07;
    private TextView mNationalTaxNum;//国税核税
    private LinearLayout mTabLay08;
    private TextView mDigitalNum;//数字证书
    private LinearLayout mTabLay09;
    private TextView mGoldPlateNum;//金税盘 10
    private LinearLayout mTabLay10;
    private TextView mElectronicInvoiceNum;//电子发票
    private LinearLayout mTabLay11;
    private TextView mLocalTaxNum;//地税核税
    private LinearLayout mTabLay12;
    private TextView mRentNum;//租苈备案
    private LinearLayout mTabLay13;
    private TextView mAcountingNum;//移交会记
    private LinearLayout mTabLay14;
    private TextView mCustomerNum;//移交客户  15
    private LinearLayout mTabLay15;
    private TextView mCallbackNum;//办证回访
    private LinearLayout mTabLay16;
    private TextView mSendPeopleNum;//发送朋友圈
    private LinearLayout mTabLay17;
    private TextView mBackLecturingNum;//后台开讲
    private LinearLayout mTabLay18;

    private TextView mPublicPraiseNum;//口碑传播
    private LinearLayout mTabLay19;

    private LinearLayout mTabLay20;
    private LinearLayout mTabLay21;
    private int mCurrentStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        mUId = bundle.getInt("uid",-1);
        LogHelper.i(TAG,"-----PersonalActivity---"+mUId);

        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("绩效");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        mSHaredPreference = getSharedPreferences("tou",MODE_APPEND);

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
        persomnalImg = instance.getmContactUrl();

        LogHelper.i(TAG,"-----PersonalActivity---"+mUId+ userName + phoneNum);



    }



    //访问数据显示数量
    private void getPersonData(int uid) {

        VolleyHelpApi.getInstance().getPersonalData(uid,new APIListener() {

            @Override
            public void onResult(Object result) {
                LogHelper.i(TAG, "----个人绩效所有信息--" + result.toString());
             //{"step19":1,"step16":1,"step15":1,"step18":1,"step17":1,"step11":1,"step12":1,"step13":1,"step14":1,"step3":11,"step2":11,"step1":13,"step10":1,
                // "step7":1,"step6":12,"step5":12,"step4":12,"commission":7440,"step9":1,"currentMonthCommission":2010,"step8":1}

                JSONObject JSONOB = ((JSONObject) result).optJSONObject("entity");
                mTMoney = JSONOB.optString("commission");//总
                mYMoney = JSONOB.optString("currentMonthCommission");//月
                step1 = JSONOB.optString("step1");
                step2 = JSONOB.optString("step2");
                step3 = JSONOB.optString("step3");
                step4 = JSONOB.optString("step4");
                step5 = JSONOB.optString("step5");
                step6 = JSONOB.optString("step6");

                step7 = JSONOB.optString("step7");
                step8 = JSONOB.optString("step8");
                step9 = JSONOB.optString("step9");
                step10 = JSONOB.optString("step10");
                step11= JSONOB.optString("step11");
                step12= JSONOB.optString("step12");

                step13= JSONOB.optString("step13");
                step14= JSONOB.optString("step14");
                step15= JSONOB.optString("step15");
                step16= JSONOB.optString("step16");
                step17= JSONOB.optString("step17");
                step18= JSONOB.optString("step18");
                step19= JSONOB.optString("step19");

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
        mReferenceNum.setText(step1);
        mNuansNum.setText(step2);
        mRegisterNum.setText(step3);
        mSculptureNum.setText(step4);
        mChapterNum.setText(step5);
        mBankOpenNum.setText(step6);

        mBankEntrustNum.setText(step7);
        mNationalTaxNum.setText(step8);
        mDigitalNum.setText(step9);
        mGoldPlateNum.setText(step10);
        mElectronicInvoiceNum.setText(step11);
        mLocalTaxNum.setText(step12);

        mRentNum.setText(step13);
        mAcountingNum.setText(step14);
        mCustomerNum.setText(step15);
        mCallbackNum.setText(step16);
        mSendPeopleNum.setText(step17);
        mBackLecturingNum.setText(step18);
        mPublicPraiseNum.setText(step19);


        mMoney.setText(mYMoney+"元");
        mTotalMoney.setText(mTMoney+"元");
        mServiceNum.setText(0+"");
    }


    @Override
    public void onClick(View v) {

        switch(v.getId()){

            case R.id.mTabLay01://资料交接
                mCurrentStep = 1;

                break;
            case R.id.mTabLay02://工商核名
                mCurrentStep = 2;
                break;
            case R.id.mTabLay03://注册文件

                mCurrentStep = 3;
                break;
            case R.id.mTabLay04://雕刻印章

                mCurrentStep = 4;
                break;
            case R.id.mTabLay05://公安拿章

                mCurrentStep = 5;

                break;
            case R.id.mTabLay06://银行开户
                mCurrentStep = 6;
                break;
            case R.id.mTabLay07://银行委托

                mCurrentStep = 7;
                break;
            case R.id.mTabLay08://国税核税

                mCurrentStep = 8;
                break;
            case R.id.mTabLay09://数字证书

                mCurrentStep = 9;
                break;
            case R.id.mTabLay10://金税盘

                mCurrentStep = 10;
                break;
            case R.id.mTabLay11://电子发票

                mCurrentStep = 11;
                break;
            case R.id.mTabLay12://地税核税

                mCurrentStep = 12;
                break;

            case R.id.mTabLay13://租苈备案

                mCurrentStep = 13;
                break;
            case R.id.mTabLay14://移交会记

                mCurrentStep = 14;
                break;
            case R.id.mTabLay15://移交客户

                mCurrentStep = 15;
                break;
            case R.id.mTabLay16://办证回访

                mCurrentStep = 16;
                break;
            case R.id.mTabLay17://发朋友圈

                mCurrentStep = 17;
                break;
            case R.id.mTabLay18://后台开讲

                mCurrentStep = 18;
                break;
            case R.id.mTabLay19://口碑传播

                mCurrentStep = 19;
                break;

        }

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        DialogFragment newFragment = CompNDialogFragment.newInstance(mCurrentStep);
        newFragment.show(ft, "cn_dialog");




    }





    private void showDialogForStep(List<String> mStepList) {



    }


    private void initialize() {

        mPersonImg = (ImageView) findViewById(R.id.mPersonImg);
        mPersonName = (TextView) findViewById(R.id.mPersonName);
        mPersonBanZheng = (TextView) findViewById(R.id.mPersonBanZheng);
        mPersonPhone = (TextView) findViewById(R.id.mPersonPhone);
        mPersonMa = (ImageView) findViewById(R.id.mPersonMa);
        mText = (TextView) findViewById(R.id.mText);
        mMoney = (TextView) findViewById(R.id.mMoney);
        mTotalMoney = (TextView) findViewById(R.id.mTotalMoney);
        mServiceNum = (TextView) findViewById(R.id.mServiceNum);
        mReferenceNum = (TextView) findViewById(R.id.mReferenceNum);
        mTabLay01 = (LinearLayout) findViewById(R.id.mTabLay01);
        mNuansNum = (TextView) findViewById(R.id.mNuansNum);
        mTabLay02 = (LinearLayout) findViewById(R.id.mTabLay02);
        mRegisterNum = (TextView) findViewById(R.id.mRegisterNum);
        mTabLay03 = (LinearLayout) findViewById(R.id.mTabLay03);
        mSculptureNum = (TextView) findViewById(R.id.mSculptureNum);
        mTabLay04 = (LinearLayout) findViewById(R.id.mTabLay04);
        mChapterNum = (TextView) findViewById(R.id.mChapterNum);
        mTabLay05 = (LinearLayout) findViewById(R.id.mTabLay05);
        mBankOpenNum = (TextView) findViewById(R.id.mBankOpenNum);
        mTabLay06 = (LinearLayout) findViewById(R.id.mTabLay06);
        mBankEntrustNum = (TextView) findViewById(R.id.mBankEntrustNum);
        mTabLay07 = (LinearLayout) findViewById(R.id.mTabLay07);
        mNationalTaxNum = (TextView) findViewById(R.id.mNationalTaxNum);
        mTabLay08 = (LinearLayout) findViewById(R.id.mTabLay08);
        mDigitalNum = (TextView) findViewById(R.id.mDigitalNum);
        mTabLay09 = (LinearLayout) findViewById(R.id.mTabLay09);
        mGoldPlateNum = (TextView) findViewById(R.id.mGoldPlateNum);
        mTabLay10 = (LinearLayout) findViewById(R.id.mTabLay10);
        mElectronicInvoiceNum = (TextView) findViewById(R.id.mElectronicInvoiceNum);
        mTabLay11 = (LinearLayout) findViewById(R.id.mTabLay11);
        mLocalTaxNum = (TextView) findViewById(R.id.mLocalTaxNum);
        mTabLay12 = (LinearLayout) findViewById(R.id.mTabLay12);
        mRentNum = (TextView) findViewById(R.id.mRentNum);
        mTabLay13 = (LinearLayout) findViewById(R.id.mTabLay13);
        mAcountingNum = (TextView) findViewById(R.id.mAcountingNum);
        mTabLay14 = (LinearLayout) findViewById(R.id.mTabLay14);
        mCustomerNum = (TextView) findViewById(R.id.mCustomerNum);
        mTabLay15 = (LinearLayout) findViewById(R.id.mTabLay15);
        mCallbackNum = (TextView) findViewById(R.id.mCallbackNum);
        mTabLay16 = (LinearLayout) findViewById(R.id.mTabLay16);
        mSendPeopleNum = (TextView) findViewById(R.id.mSendPeopleNum);
        mTabLay17 = (LinearLayout) findViewById(R.id.mTabLay17);
        mBackLecturingNum = (TextView) findViewById(R.id.mBackLecturingNum);
        mTabLay18 = (LinearLayout) findViewById(R.id.mTabLay18);
        mPublicPraiseNum = (TextView) findViewById(R.id.mPublicPraiseNum);
        mTabLay19 = (LinearLayout) findViewById(R.id.mTabLay19);
        mTabLay20 = (LinearLayout) findViewById(R.id.mTabLay20);
        mTabLay21 = (LinearLayout) findViewById(R.id.mTabLay21);


        mTabLay01.setOnClickListener(this);
        mTabLay02.setOnClickListener(this);
        mTabLay03.setOnClickListener(this);
        mTabLay04.setOnClickListener(this);
        mTabLay05.setOnClickListener(this);
        mTabLay06.setOnClickListener(this);

        mTabLay07.setOnClickListener(this);
        mTabLay08.setOnClickListener(this);
        mTabLay09.setOnClickListener(this);
        mTabLay10.setOnClickListener(this);
        mTabLay11.setOnClickListener(this);
        mTabLay12.setOnClickListener(this);
        mTabLay13.setOnClickListener(this);
        mTabLay14.setOnClickListener(this);
        mTabLay15.setOnClickListener(this);
        mTabLay16.setOnClickListener(this);
        mTabLay17.setOnClickListener(this);
        mTabLay18.setOnClickListener(this);
        mTabLay19.setOnClickListener(this);
        mTabLay20.setOnClickListener(this);


        mPersonImg.setOnClickListener(this);

        String url = mSHaredPreference.getString("url", "null");

        LogHelper.i(TAG,"------"+url);
        if (!url.equals("null")){
            LogHelper.i(TAG,"----11--"+url);
            Bitmap smallBitmap = BitmapUtils.getSmallBitmap(url);
            mPersonImg.setImageBitmap(smallBitmap);
            // BitmapUtils.loadImgageUrl(url,mPersonImg);
        }

    }
}
