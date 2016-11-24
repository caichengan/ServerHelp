package com.xht.android.serverhelp.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 *
 *@类名:IntentUtils
 *@时间:上午8:41:10
 * @author Administrator
 *
 *@描述:开启意图的几种不同方式
 */
public class IntentUtils {

	/**
	 * 直接进入一个界面
	 */
	public static void startActivityInfo(Activity context,Class<?>cls){
		Intent intent=new Intent(context,cls);
		context.startActivity(intent);
	}

	/**
	 * 携带数据进入一个界面
	 */
	public static void startActivityNumber(Activity context, Bundle bundle, Class<?>cls){
		Intent intent=new Intent(context,cls);
		intent.putExtra("bundle",bundle);
		context.startActivity(intent);
	}
	/**
	 * 携带数据进入一个界面
	 */
	public static void startActivityNumberForResult(Activity context, Bundle bundle, Class<?>cls){
		Intent intent=new Intent(context,cls);
		intent.putExtra("bundle",bundle);
		context.startActivityForResult(intent,20);
	}


	/**
	 * 返回数据
	 */
	public static void startActivityNumberForResult(int intData,Activity context, Class<?>cls){
		Intent intent=new Intent(context,cls);
		context.startActivityForResult(intent,intData);
	}

	/**
	 * 进入一个界面并结束自己
	 */
	public static void startActivityAndFinish(Activity context,Class<?>cls){
		Intent intent=new Intent(context,cls);
		context.startActivity(intent);
		context.finish();
	}
	/**
	 * 延迟进入进入一个界面
	 *
	 */
	public static void startActivityDelay(final Activity context,final Class<?>cls,final long time){

		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Intent intent=new Intent(context,cls);
				context.startActivity(intent);
			}
		}.start();
	}

	/**
	 * 延迟进入进入另一个界面并销毁这个界面
	 *
	 */
	public static void startActivityDelayAndFinish(final Activity context, final Bundle bundle, final Class<?>cls, final long time){

		new Thread(){
			@Override
			public void run() {
				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				Intent intent=new Intent(context,cls);
				intent.putExtra("bundle",bundle);
				context.startActivity(intent);
				context.finish();
			}
		}.start();
	}
}
