package com.xht.android.serverhelp;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.xht.android.serverhelp.model.UserInfo;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.provider.MyDatabaseManager;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginFragment extends Fragment {
	
	private Button mLoginButton;
	TextView mButtonReg, mWangJi;
	private LoginActivity mActivity;
	private EditText mEditText1, mEditText2;
	private ProgressDialog mProgressDialog;
	private UserInfo mUseInfo;
	private String password;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (LoginActivity) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity.setTitle(R.string.login_changhu);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		mWangJi = (TextView) view.findViewById(R.id.fotgetMMBt);//忘记密码
		/*mWangJi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				LogHelper.i("111", "aaa");
//				Intent intent = new Intent(getActivity(), ForgetActivity.class);
//				startActivity(intent);
				ForgetFragment fragment = new ForgetFragment();
				FragmentManager fm = getFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				
				ft.add(R.id.login_container, fragment, "ForgetFragment");
				ft.addToBackStack(null);
				ft.hide(LoginFragment.this);
				ft.commit();
			}
		});*/
		mButtonReg = (TextView) view.findViewById(R.id.zhuceBt);//注册
		/*mButtonReg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mActivity.switchToActivity(RegisterActivity.class, null, 0, false, false);
				mActivity.finish();
			}
		});*/
		mLoginButton = (Button) view.findViewById(R.id.loginBt);
		mLoginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				postLogin();
			}
		});
		mEditText1 = (EditText) view.findViewById(R.id.phoneEdit);
		mEditText2 = (EditText) view.findViewById(R.id.mimaEdit);
		return view;		
	}

	private void postLogin() {
		final String pNum = mEditText1.getText().toString();
		if (TextUtils.isEmpty(pNum)) {
			App.getInstance().showToast(getResources().getString(R.string.tip_input_pnum));
			return;
		}
		password = mEditText2.getText().toString();
		if (TextUtils.isEmpty(password)) {
			App.getInstance().showToast(getResources().getString(R.string.tip_input_password));
			return;
		}
		createProgressDialogTitle(getResources().getString(R.string.zhengzai_login));

		JSONObject object=new JSONObject();
		try {
			object.put("telephone",pNum);
			object.put("password", password);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		VolleyHelpApi.getInstance().postLogin( object, new APIListener() {
			@Override
			public void onResult(Object result) {
				LogHelper.i("登录成功", "-------2016---"+result.toString());
				dismissProgressDialog();

				JSONObject json= (JSONObject) result;
				try {//{"employeeId":4,
					// "empheadportrait":"http:\/\/www.xiaohoutai.com.cn:8888\/XHT\/empheadportraitController\/downloadEmpheadportrait?fileName=1479872942008bzzbz_e4_1479872416924.jpg",
					// "employeeName":"蔡成安"}
					int employeeId = json.getInt("employeeId");
					String employeeName = json.getString("employeeName");
					String empheadportrait = json.getString("empheadportrait");
					long ppomeNum=Long.valueOf(pNum);
					ContentValues cv = new ContentValues();
					cv.put(MyDatabaseManager.MyDbColumns.UID, employeeId);
					cv.put(MyDatabaseManager.MyDbColumns.PHONE, pNum);
					cv.put(MyDatabaseManager.MyDbColumns.NAME, employeeName);
					cv.put(MyDatabaseManager.MyDbColumns.URL, empheadportrait);
					mActivity.getContentResolver().insert(MyDatabaseManager.MyDbColumns.CONTENT_URI, cv);
					LogHelper.i("------LoginFragment", "phoneNum" +pNum+employeeId+employeeName+empheadportrait);

					Intent intent = new Intent(MyFragment.BRO_ACT_S);
					intent.putExtra(MyFragment.UID_KEY, employeeId);
					intent.putExtra(MyFragment.PHONENUM_KEY, ppomeNum);
					intent.putExtra(MyFragment.UNAME_KEY, employeeName);
					intent.putExtra(MyFragment.UNAME_URL, empheadportrait);

					mActivity.sendBroadcast(intent);
					startActivity(new Intent(getActivity(), MainActivity.class));
					mActivity.finish();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			@Override
			public void onError(Object e) {
				dismissProgressDialog();
				App.getInstance().showToast(e.toString());
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
			mProgressDialog = new ProgressDialog(getActivity());
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
