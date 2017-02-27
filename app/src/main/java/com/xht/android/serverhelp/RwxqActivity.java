package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * 办证中任务详情
 * @author czw 2016-10-13
 */
public class RwxqActivity extends Activity {
    private static final String TAG = "RwxqActivity";
    private Fragment mFragment1, mFragment2, mFragment3,mFragment4;
    private RadioButton mRBtn1, mRBtn2, mRBtn3,mRBtn4;
    private RadioGroup rg;
    public int mOrderId;
    private ProgressDialog mProgDoal;
    private HorizontalScrollView mHorizontalSV;
    LinearLayout mLLayout;
    private ArrayList<PersonBzzxq> mPBArrayList;
    private int mUid;
    public String contactName;
    private TextView textName;

    private SharedPreferences mSHaredPreference;
    private ImageView textImg;
    private String companyName;
    private String companyId;
    public String phone;

    @Override
    protected void onResume() {
        super.onResume();

        mUid=MainActivity.mUserInfo.getUid();
        mOrderId = getIntent().getIntExtra("ordId", 0);
        companyId = getIntent().getStringExtra("companyId");
        phone = getIntent().getStringExtra("phone");
        contactName = getIntent().getStringExtra("contactName");
        LogHelper.i(TAG,"-----mUid----------"+mUid+"----mOrderId"+phone+"---"+phone);
        new JinDuFragment().newInstance(companyId,null);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mOrderId = getIntent().getIntExtra("ordId", 0);
        mUid = getIntent().getIntExtra("mUid", 0);
        companyId = getIntent().getStringExtra("companyId");
        companyName = getIntent().getStringExtra("mCompanyName");
        phone = getIntent().getStringExtra("phone");
        contactName = getIntent().getStringExtra("contactName");
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
        mRBtn4 = (RadioButton) findViewById(R.id.tab4);
        mLLayout = (LinearLayout) findViewById(R.id.containLLayout);
        mHorizontalSV = (HorizontalScrollView) findViewById(R.id.chengyuan);
        //-----------------------------测试-----------------------------------------------
      /*  View view = this.getLayoutInflater().inflate(R.layout.item_bz_chengyuan, null);//TODO 添加头像
        textName = (TextView) view.findViewById(R.id.chengyuanName);
        textImg = (ImageView) view.findViewById(R.id.touxiang);

        //TODO 加到滑动标签 中去
        mLLayout.addView(view);*/
        //-----------------------------测试end-----------------------------------------------
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mFragment1 = fm.findFragmentByTag("f1");
        if (mFragment1 == null) {
            mFragment1 = JinDuFragment.newInstance(companyId, null);
            //mFragment1 = ProgressFragment.newInstance("", null);
        }

        mSHaredPreference = getSharedPreferences("tou",MODE_APPEND);

        String url = mSHaredPreference.getString("url", "null");
        LogHelper.i(TAG,"------"+url);
      /*  if (!url.equals("null")){
            LogHelper.i(TAG,"----11--"+url);
            Bitmap smallBitmap = BitmapUtils.getSmallBitmap(url);
            textImg.setImageBitmap(smallBitmap);
            // BitmapUtils.loadImgageUrl(url,mPersonImg);
        }*/

        ft.add(R.id.fragment_contain, mFragment1, "f1");
        mFragment2 = fm.findFragmentByTag("f2");
        if (mFragment2 == null) {
            mFragment2 = GenJinFragment.newInstance("", null);

        }
        ft.add(R.id.fragment_contain, mFragment2, "f2");
        mFragment3 = fm.findFragmentByTag("f3");
        if (mFragment3 == null) {
            mFragment3 = DetailFragment.newInstance(companyId, mOrderId+"");
        }
        ft.add(R.id.fragment_contain, mFragment3, "f3");

        mFragment4 = fm.findFragmentByTag("f4");
        if (mFragment4 == null) {
            mFragment4 = OrderFragment.newInstance(mOrderId+"", null);
        }
        ft.add(R.id.fragment_contain, mFragment4, "f4");

        ft.commit();

        mRBtn2.setTextColor(Color.GRAY);


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
            mRBtn1.setTextColor(Color.RED);
        } else {
            ft.hide(mFragment1);
            mRBtn1.setTextColor(Color.GRAY);
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
        if (mRBtn4.isChecked()) {
            ft.show(mFragment4);
        } else {
            ft.hide(mFragment4);
        }
        ft.commit();
    }
    /**
     * 办证中获取成员数据
     */
    private void loadDataPs() {
        VolleyHelpApi.getInstance().getDataBZChengYuan(mOrderId, new APIListener() {
            @Override
            public void onResult(Object result) {

                JSONArray jsonObj = ((JSONObject) result).optJSONArray("entity");
               //[{"employeeId":2,"headportrait":null,"employeeName":"安仔"}]
                LogHelper.i(TAG,"------result----"+result);
                for (int i=0;i<jsonObj.length();i++) {
                    try {
                       JSONObject obj= (JSONObject) jsonObj.get(i);
                        String headportrait = obj.optString("headportrait");
                        String employeeName = obj.optString("employeeName");
                        LogHelper.i(TAG,"-----headportrait-"+headportrait);

                        View view = View.inflate(RwxqActivity.this,R.layout.item_bz_chengyuan,null);//TODO 添加头像
                        textName = (TextView) view.findViewById(R.id.chengyuanName);
                        textImg = (ImageView) view.findViewById(R.id.touxiang);



                        textName.setText(employeeName);
                        BitmapUtils.loadImgageUrl(headportrait,textImg);

                        //TODO 加到滑动标签 中去
                        mLLayout.addView(view);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

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
