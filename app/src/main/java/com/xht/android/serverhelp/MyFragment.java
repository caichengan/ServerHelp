package com.xht.android.serverhelp;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xht.android.serverhelp.model.UserInfo;
import com.xht.android.serverhelp.provider.MyDatabaseManager;
import com.xht.android.serverhelp.util.AppInfoUtils;
import com.xht.android.serverhelp.util.AsyncImageLoader;
import com.xht.android.serverhelp.util.BitmapUtils;
import com.xht.android.serverhelp.util.IntentUtils;
import com.xht.android.serverhelp.util.LogHelper;

import static android.content.Context.MODE_APPEND;

public class MyFragment extends Fragment implements View.OnClickListener {
	private static final String TAG = "MyFragment";
	public static final String BRO_ACT_S = "com.xht.android.serverhelp.bro_act";
	public static final String BRO_ACT_S2 = "com.xht.android.serverhelp.bro_act_s2";

	public static final String PHONENUM_KEY = "phone_key";
	public static final String UID_KEY = "userId_key";
	public static final String UNAME_KEY = "userName_key";
	public static final String UNAME_URL= "userName_url";
	private MainActivity mActivity;
	private ImageView headimg;
	private TextView aName;
	private TextView aPhoneNum;
	private ImageView erweima;

	private SharedPreferences mSHaredPreference;
	private LinearLayout fragmmyll1;
	private TextView changemima;
	private TextView banben;
	private LinearLayout fragmmyll2;
	private UserInfo mUserInfo;

	private AsyncImageLoader imageLoader;

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			int uId = intent.getIntExtra(UID_KEY, 0);
			long phoneNum = intent.getLongExtra(PHONENUM_KEY, 0);
			String uName = intent.getStringExtra(UNAME_KEY);
			String contactUrl = intent.getStringExtra(UNAME_URL);
			LogHelper.i(TAG,"--------收到广播-");
			mUserInfo.setUid(uId);

			LogHelper.i(TAG, "-----uName=" + uId+"---phoneNum"+phoneNum+uName+"------"+contactUrl);
			mUserInfo.setPhoneNum(phoneNum);

			mUserInfo.setUserName(uName);
			mUserInfo.setmContactUrl(contactUrl);

			refleshUI();
		}
	};

	private BroadcastReceiver mClearUserReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			mUserInfo.setUid(0);
			mUserInfo.setPhoneNum(0);
			mUserInfo.setUserName(null);

			refleshUI2();
		}
	};
	private String url;

	private void refleshUI() {
		aPhoneNum.setText("" + mUserInfo.getPhoneNum());
		/*//设置用户id为标签
		addTag(mUserInfo.getUid());// 添加用户标签*/
	}

	/**
	 * 清空用户后
	 */
	private void refleshUI2() {
		aPhoneNum.setText("");
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (MainActivity) activity;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		IntentFilter intentFilter = new IntentFilter(BRO_ACT_S);
		getActivity().registerReceiver(mReceiver, intentFilter);

		IntentFilter intentFilter2 = new IntentFilter(BRO_ACT_S2);
		getActivity().registerReceiver(mClearUserReceiver, intentFilter2);

		mUserInfo = ((MainActivity) mActivity).mUserInfo;
		mSHaredPreference=getActivity().getSharedPreferences("tou",MODE_APPEND);




		imageLoader = new AsyncImageLoader(getActivity());

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_my, container, false);

		headimg = (ImageView)view.findViewById(R.id.head_img);
		aName = (TextView) view.findViewById(R.id.aName);
		aPhoneNum = (TextView)view.findViewById(R.id.aPhoneNum);
		erweima = (ImageView) view.findViewById(R.id.erweima);
		fragmmyll1 = (LinearLayout)view.findViewById(R.id.fragm_my_ll1);
		changemima = (TextView) view.findViewById(R.id.change_mima);
		banben = (TextView) view.findViewById(R.id.banben);
		fragmmyll2 = (LinearLayout) view.findViewById(R.id.fragm_my_ll2);

		fragmmyll1.setOnClickListener(this);//登录
		fragmmyll2.setOnClickListener(this);//修改密码
		headimg.setOnClickListener(this);//头像上传

		banben.setText("版本："+AppInfoUtils.getAppInfoName(getActivity()));

		url = mSHaredPreference.getString("url", "null");

		LogHelper.i(TAG,"------"+ url);
		if (!url.equals("null")){
			LogHelper.i(TAG,"----11--"+ url);
			Bitmap smallBitmap = BitmapUtils.getSmallBitmap(url);
			headimg.setImageBitmap(smallBitmap);
			// BitmapUtils.loadImgageUrl(url,mPersonImg);
		}


		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
		isUserLogin();

		 url = mSHaredPreference.getString("url", "null");

		LogHelper.i(TAG,"------"+url);
		if (!url.equals("null")){
			LogHelper.i(TAG,"----11--"+url);
			Bitmap smallBitmap = BitmapUtils.getSmallBitmap(url);
			headimg.setImageBitmap(smallBitmap);
			// BitmapUtils.loadImgageUrl(url,mPersonImg);
		}else{
			if (mUserInfo.getmContactUrl()!=null||mUserInfo.getmContactUrl()!=""||mUserInfo.getmContactUrl().equals("null")) {
				Bitmap smallBitmap = BitmapUtils.getSmallBitmap(mUserInfo.getmContactUrl());
				headimg.setImageBitmap(smallBitmap);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();

		mActivity.unregisterReceiver(mReceiver);
		mActivity.unregisterReceiver(mClearUserReceiver);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.fragm_my_ll1:
				if (isUserLogin()) {//已经登录，可进行其他操作

					FragmentTransaction ft = getFragmentManager().beginTransaction();
					DialogFragment newFragment = SwitchUserDialogFragment.newInstance(mUserInfo.getUid(), 0);
					newFragment.show(ft, "sw_u_dialog");

				} else {
					IntentUtils.startActivityInfo(mActivity,LoginActivity.class);
					return;
				}
				break;
			case R.id.fragm_my_ll2:
				if (isUserLogin()) {
					//修改密码
					Bundle bundle=new Bundle();
					IntentUtils.startActivityNumber(mActivity,bundle,ChangMiMaActivity.class);
				} else {
					//先登录再修改密码
					App.getInstance().showToast("先登录再修改密码");
					IntentUtils.startActivityInfo(mActivity,LoginActivity.class);
				}
				break;
			case R.id.head_img://头像

				IntentUtils.startActivityNumberForResult(30,getActivity(),LoadPersonImageView.class);

				break;
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == -1) {
			if (requestCode == 30) {//相册返回
				String url1 = data.getStringExtra("url1");

				LogHelper.i(TAG, "------url1" + url1);
				if (TextUtils.isEmpty(url1)) {
					Bitmap bitmap = imageLoader.loadImage(headimg, url1);
					if (bitmap != null) {
						headimg.setImageBitmap(bitmap);

					}
				}
			}

		}
	}

	boolean isUserLogin() {
		if (mUserInfo.getUid() == 0) {
			Cursor cursor = mActivity.getContentResolver().query(MyDatabaseManager.MyDbColumns.CONTENT_URI, null, null, null, null);
			if (cursor == null || cursor.getCount() == 0) {
				return false;
			}
			cursor.moveToFirst();
			int uidIndex = cursor.getColumnIndex(MyDatabaseManager.MyDbColumns.UID);
			int userNameIndex = cursor.getColumnIndex(MyDatabaseManager.MyDbColumns.NAME);
			int phoneIndex = cursor.getColumnIndex(MyDatabaseManager.MyDbColumns.PHONE);
			int urlIndex = cursor.getColumnIndex(MyDatabaseManager.MyDbColumns.URL);
			mUserInfo.setUid(cursor.getInt(uidIndex));
			mUserInfo.setUserName(cursor.getString(userNameIndex));
			mUserInfo.setPhoneNum(cursor.getLong(phoneIndex));
			mUserInfo.setmContactUrl(cursor.getString(urlIndex));

		}
		LogHelper.i(TAG,  "----------mUserInfo.getName() == " + mUserInfo.getUserName()+"----------mUserInfo.getUid() == " + mUserInfo.getUid() + "mUserInfo.getPhoneNum() == " + mUserInfo.getPhoneNum());
		LogHelper.i(TAG,  "----------mUserInfo.getmContactUrl() == " + mUserInfo.getmContactUrl());
		aPhoneNum.setText("" + mUserInfo.getPhoneNum());
		aName.setText(mUserInfo.getUserName());

		return  true;

	}
}
