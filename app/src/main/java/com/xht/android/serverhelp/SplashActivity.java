package com.xht.android.serverhelp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.xht.android.serverhelp.model.UserInfo;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.provider.MyDatabaseManager;
import com.xht.android.serverhelp.util.AppInfoUtils;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONObject;

import java.io.File;

/**
 * Created by Administrator on 2016-9-29.
 */
public class SplashActivity extends Activity {
    private static final String TAG = "SplashActivity";


    public static UserInfo mUserInfo = new UserInfo();
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    private File file;

    public static UserInfo getInstance() {
        return mUserInfo;
    }

    private Handler handler=new Handler(){
        @Override
        public String getMessageName(Message message) {
            return super.getMessageName(message);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spash_activity);
        ImageView spashImg = (ImageView) findViewById(R.id.spash_img);
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.lin);
        //开启动画
        //设置进入动画透明状
        AlphaAnimation aa = new AlphaAnimation(0.2f, 1.0f);
        aa.setDuration(2000);
        /**
         * 开启动画
         */
        linearLayout.startAnimation(aa);
        aa.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }
            @Override
            public void onAnimationEnd(Animation animation) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkVersion();
                    }
                },2000);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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

    private void checkVersion() {
        LogHelper.i(TAG, "--------版本更新的所有信息--" );
        VolleyHelpApi.getInstance().getCheckVersion(new APIListener() {

            @Override
            public void onResult(Object result) {

                //{"message":"没有最新版本","result":"error","entity":null,"code":"0"}

                String code = ((JSONObject) result).optString("code");
                if (code.equals("0")){
                    LogHelper.i(TAG,"-------无最新数据---");
                    return;
                }
                JSONObject mJsonVersion = ((JSONObject)result).optJSONObject("entity");
                LogHelper.i(TAG, "--------版本更新的所有信息--------" );
                LogHelper.i(TAG, "---版本更新的所有信息--" + result.toString());

                //"entity":{"downloadurl":"http:\/\/www.xiaohoutai.com.cn:8888\/XHT\/business\/apkcustomerserviceController\/downloadApk?fileName=1480303305698app-release.apk",
                // "version":"10"}
                //获取当前版本号
                String appInfoName = AppInfoUtils.getAppInfoName(SplashActivity.this);
                int appInfoNumber = AppInfoUtils.getAppInfoNumber(SplashActivity.this);
                LogHelper.i(TAG, "---有新版本，下载更新" + appInfoName + "-" + appInfoNumber);


                String versionNum = mJsonVersion.optString("version");
                //服务器中的版本号
                Double versionNew = Double.parseDouble(versionNum);
                String downloadurl = mJsonVersion.optString("downloadurl");
                LogHelper.i(TAG, "-----前versionNum：=" + versionNew + "---" + downloadurl);
                LogHelper.i(TAG, "-----appInfoNumber：=" + appInfoNumber + "---" );
                if (versionNew > appInfoNumber) {
                    versionNew = versionNew / 10.0;
                    LogHelper.i(TAG, "-------后--versionNum：=" + versionNew + "---" + downloadurl);
                    LogHelper.i(TAG, "---有新版本，下载更新");
                    showDialogUpdate(versionNew + "", "新的版本，修复文章bug", downloadurl);
                } else {

                    if (isUserLogin()) {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        LogHelper.i(TAG, "------------------");
                    } else {
                        // startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        LogHelper.i(TAG, "---------11111---------");
                    }

                    finish();
                }
            }

            @Override
            public void onError(Object e) {

                if (isUserLogin()) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    LogHelper.i(TAG, "------------------");
                } else {
                    startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                    LogHelper.i(TAG, "---------11111---------");
                }
                finish();
            }
        });
    }

    boolean isUserLogin() {
        if (mUserInfo.getUid() == 0) {
            Cursor cursor = this.getContentResolver().query(MyDatabaseManager.MyDbColumns.CONTENT_URI, null, null, null, null);
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
        LogHelper.i(TAG, "----------mUserInfo.getName() == " + mUserInfo.getUserName() + "----------mUserInfo.getUid() == " + mUserInfo.getUid() + "mUserInfo.getPhoneNum() == " + mUserInfo.getPhoneNum());
        //aPhoneNum.setText("" + mUserInfo.getPhoneNum());
        return true;

    }

    //弹出更新对话框showDialogUpdate("发现新版本:,请确认更新下载安装","更新日志：")
    private void showDialogUpdate(String versionName, String desc, final String downLoadUrl) {
        //弹出一个更新对话框
        final AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
        builder.setTitle("发现新版本:" + versionName + ",请确认下载升级");
        builder.setMessage("更新日志：" + desc);
        builder.setPositiveButton("立刻升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                App.getInstance().showToast("请稍等片刻...");
                //开源项目断点下载xUtils
                HttpUtils http = new HttpUtils();
                file = new File(Environment.getExternalStorageDirectory(), "xiaokufu.apk");

                http.download(downLoadUrl, file.getAbsolutePath(), true, new RequestCallBack<File>() {
                    //下载失败
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {

                    }

                    //下载成功
                    @Override
                    public void onSuccess(ResponseInfo arg0) {

                        //下载成功，替换安装
                        App.getInstance().showToast("下载成功");
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                        SplashActivity.this.startActivity(intent);


                    }
                });
                dialog.dismiss();
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();

            }
        });
        builder.setNegativeButton("下次再说", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                finish();
                dialog.dismiss();
            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    /**
     *  显示Dialog的method
     *  */
    private void showDialog(String mess) {
        new AlertDialog.Builder(this).setTitle("Message").setMessage(mess)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addCategory("android.intent.category.DEFAULT");
                        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");

                        startActivity(intent);
                    }
                }).show();
    }

    private void uninstall() {
        Uri uri = Uri.parse("package:com.xht.android.serverhelp");//获取删除包名的URI
        Intent intent = new Intent(Intent.ACTION_DELETE, uri);
        startActivity(intent);



    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Splash Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
