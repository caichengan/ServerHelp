package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xht.android.serverhelp.model.UserInfo;
import com.xht.android.serverhelp.util.LogHelper;

public class MainActivity extends Activity {


    private static final String TAG = "MainActivity";

    public static UserInfo mUserInfo = new UserInfo();
    public static UserInfo getInstance() {
        return mUserInfo;
    }

    @Override
    protected void onResume() {
        super.onResume();

        int uid = mUserInfo.getUid();
        long phoneNum = mUserInfo.getPhoneNum();
        LogHelper.i(TAG,"----"+uid+phoneNum);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(null);
        setContentView(R.layout.activity_main);
        ImageView mCustomView = new ImageView(this);
        mCustomView.setBackgroundResource(R.mipmap.logo);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ActionBar.LayoutParams lp = (ActionBar.LayoutParams) mCustomView.getLayoutParams();
        lp.gravity = lp.gravity & ~Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK | Gravity.CENTER_HORIZONTAL;
        aBar.setCustomView(mCustomView, lp);
        int change = ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);
    }

    public void switchToActivity(Class cls, Bundle bundle,int flag,
                                 boolean withTransition, boolean isExit) {
        try {
            Intent intent = new Intent(this, cls);
            if(bundle != null){
                intent.putExtras(bundle);
            }
            if(flag != 0){
                intent.setFlags(flag);
            }
            startActivity(intent);
            if(withTransition){
                if (!isExit) {
//					overridePendingTransition(R.anim.push_in_right, R.anim.push_out_right);
                }else {
//					overridePendingTransition(R.anim.back_in_left, R.anim.back_out_left);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
