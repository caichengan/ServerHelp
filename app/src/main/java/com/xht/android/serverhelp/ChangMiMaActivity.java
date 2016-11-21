package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xht.android.serverhelp.model.UserInfo;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ChangMiMaActivity extends Activity {

    private EditText editOldMiMa;
    private EditText editNewMiMa;
    private Button btnSure;
    private MainActivity mMainactivity;
    private UserInfo userInfo;
    private String userName;

    private ProgressDialog mProgressDialog;
    private int uid;
    private long phoneNum;
    private static final String TAG = "ChangMiMaActivity";
    private String oldMiMa;
    private String newMiMa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_changmima);

        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        userInfo = mMainactivity.getInstance();
        phoneNum = userInfo.getPhoneNum();
        uid = userInfo.getUid();
        userName = userInfo.getUserName();

        LogHelper.i(TAG,"-----22-"+uid+userName+phoneNum);

        initialize();
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

        editOldMiMa = (EditText) findViewById(R.id.editOldMiMa);
        editNewMiMa = (EditText) findViewById(R.id.editNewMiMa);
        btnSure = (Button) findViewById(R.id.btnSure);



        btnSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //提交数据修改密码

                oldMiMa = editOldMiMa.getText().toString();
                newMiMa = editNewMiMa.getText().toString();

                LogHelper.i(TAG,"-----22-"+oldMiMa+"---"+newMiMa);
                postDataChang();
            }
        });
    }

    private void postDataChang() {

        if (oldMiMa.equals("")&&oldMiMa==null){
            App.getInstance().showToast("请先填写原密码");
            return;
        }
        if (newMiMa.equals("")&&newMiMa==null){
            App.getInstance().showToast("请填写新密码");
            return;
        }
        JSONObject jsonObj=new JSONObject();
        try {//eid、tel、npw、opw
            jsonObj.put("eid",uid);
            jsonObj.put("opw",oldMiMa);
            jsonObj.put("tel",phoneNum);
            jsonObj.put("npw",newMiMa);

            LogHelper.i(TAG,"------"+jsonObj.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        createProgressDialogTitle("正在提交数据修改密码");
        VolleyHelpApi.getInstance().getDataChangMiMa(jsonObj, new APIListener() {
            @Override
            public void onResult(Object result) {
               JSONObject obj= (JSONObject) result;

                String code = obj.optString("code");
                String message = obj.optString("message");
                dismissProgressDialog();
                if (code.equals("1")){
                    App.getInstance().showToast(message);

                    ChangMiMaActivity.this.finish();
                    return;
                }else{
                    App.getInstance().showToast(message);
                    return;

                }
                //成功返回：{ message: "密码修改成功", result: "success", entity: null, code: "1" }
               // 失败返回：{ message: "新旧密码不能一致", result: "error", entity: null, code: "0" }

            }

            @Override
            public void onError(Object e) {

                dismissProgressDialog();
            }
        });


    }

    /**
     * 创建mProgressDialog
     */
    private void createProgressDialogTitle(String title)
    {
        if(mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(ChangMiMaActivity.this);
        }
        mProgressDialog.setTitle(title);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    /**
     * 隐藏mProgressDialog
     */
    private void dismissProgressDialog()
    {
        if(mProgressDialog != null)
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}
