package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.xht.android.serverhelp.model.PersonBzzxq;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.BitmapUtils;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 办证中任务详情
 * @author czw 2016-10-13
 */
public class RwxqActivity extends Activity {
    private static final String TAG = "RwxqActivity";
    private Fragment mFragment1, mFragment2, mFragment3;
    private RadioButton mRBtn1, mRBtn2, mRBtn3;
    private RadioGroup rg;
    public int mOrderId;
    private ProgressDialog mProgDoal;
    private HorizontalScrollView mHorizontalSV;
    LinearLayout mLLayout;
    private ArrayList<PersonBzzxq> mPBArrayList;
    private int mUid;
    private String employeeName;
    private TextView textName;

    private SharedPreferences mSHaredPreference;
    private ImageView textImg;
    private String companyName;

    @Override
    protected void onResume() {
        super.onResume();

        mUid=MainActivity.mUserInfo.getUid();
        mOrderId = getIntent().getIntExtra("ordId", 0);
        LogHelper.i(TAG,"-----mUid----------"+mUid+"----mOrderId"+mOrderId);
        new JinDuFragment().newInstance("",null);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderId = getIntent().getIntExtra("ordId", 0);
        mUid = getIntent().getIntExtra("mUid", 0);
        companyName = getIntent().getStringExtra("mCompanyName");
        LogHelper.i(TAG,"-----mUid"+mUid+"----mOrderId"+mOrderId);

        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText(companyName);
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);



        setContentView(R.layout.activity_rwxq);
        rg = (RadioGroup) findViewById(R.id.switch_tabs);
        mRBtn1 = (RadioButton) findViewById(R.id.tab1);
        mRBtn2 = (RadioButton) findViewById(R.id.tab2);
        mRBtn3 = (RadioButton) findViewById(R.id.tab3);
        mLLayout = (LinearLayout) findViewById(R.id.containLLayout);
        mHorizontalSV = (HorizontalScrollView) findViewById(R.id.chengyuan);
        //-----------------------------测试-----------------------------------------------
        View view = this.getLayoutInflater().inflate(R.layout.item_bz_chengyuan, null);
        textName = (TextView) view.findViewById(R.id.chengyuanName);
        textImg = (ImageView) view.findViewById(R.id.touxiang);
        mLLayout.addView(view);
        //-----------------------------测试end-----------------------------------------------
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mFragment1 = fm.findFragmentByTag("f1");
        if (mFragment1 == null) {
            mFragment1 = JinDuFragment.newInstance("", null);
            //mFragment1 = ProgressFragment.newInstance("", null);
        }

        mSHaredPreference = getSharedPreferences("tou",MODE_APPEND);

        String url = mSHaredPreference.getString("url", "null");
        LogHelper.i(TAG,"------"+url);
        if (!url.equals("null")){
            LogHelper.i(TAG,"----11--"+url);
            Bitmap smallBitmap = BitmapUtils.getSmallBitmap(url);
            textImg.setImageBitmap(smallBitmap);
            // BitmapUtils.loadImgageUrl(url,mPersonImg);
        }

        ft.add(R.id.fragment_contain, mFragment1, "f1");
        mFragment2 = fm.findFragmentByTag("f2");
        if (mFragment2 == null) {
            mFragment2 = GenJinFragment.newInstance("", null);

        }
        ft.add(R.id.fragment_contain, mFragment2, "f2");
        mFragment3 = fm.findFragmentByTag("f3");
        if (mFragment3 == null) {
            mFragment3 = DetailFragment.newInstance("", null);
        }
        ft.add(R.id.fragment_contain, mFragment3, "f3");
        ft.commit();




        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
              @Override
              public void onCheckedChanged(RadioGroup group, int checkedId) {
                  updateFragmentVisibility();
                  switch (checkedId) {
                      case R.id.tab1:
                          break;
                      case R.id.tab2:
                          break;
                      case R.id.tab3:
                          break;

                      default:
                          break;
                  }
              }
        });

        updateFragmentVisibility();
        loadDataPs();
    }

    // Update fragment visibility based on current check box state.
    void updateFragmentVisibility() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (mRBtn1.isChecked()) {
            ft.show(mFragment1);
        } else {
            ft.hide(mFragment1);
        }
        if (mRBtn2.isChecked()) {
            ft.show(mFragment2);
        } else {
            ft.hide(mFragment2);
        }
        if (mRBtn3.isChecked()) {
            ft.show(mFragment3);
        } else {
            ft.hide(mFragment3);
        }
        ft.commit();
    }
    /**
     * 办证中获取成员数据
     */
    private void loadDataPs() {
        VolleyHelpApi.getInstance().getDataBZChengYuan(mUid, new APIListener() {
            @Override
            public void onResult(Object result) {

                JSONObject jsonObj = ((JSONObject) result).optJSONObject("entity");
                //"entity":{"employeeName":"蔡成安"},"code":"1"}
                employeeName = jsonObj.optString("employeeName");
                LogHelper.i(TAG,"------result----"+result);
                textName.setText(employeeName);
            }
            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {
                Fragment fragment = getFragmentManager().findFragmentByTag("f1");
                if (fragment != null && fragment.isVisible()) {
                    fragment.onActivityResult(requestCode, resultCode, intent);
                }
            }
        }
    }
    public void createProgressDialog(String title) {
        if (mProgDoal == null) {
            mProgDoal = new ProgressDialog(this);
        }
        mProgDoal.setTitle(title);
        mProgDoal.setIndeterminate(true);
        mProgDoal.setCancelable(false);
        mProgDoal.show();
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

    /**
     * 隐藏mProgressDialog
     */
    public void dismissProgressDialog()
    {
        if(mProgDoal != null)
        {
            mProgDoal.dismiss();
            mProgDoal = null;
        }
    }

    private class ViewHolder {
        TextView nameTV;
        NetworkImageView networkImageView;
    }

}
