package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xht.android.serverhelp.model.Constants;
import com.xht.android.serverhelp.model.UserInfo;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.BitmapHelper;
import com.xht.android.serverhelp.util.BitmapUtils;
import com.xht.android.serverhelp.util.LogHelper;
import com.xht.android.serverhelp.view.RoundImageView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2016/11/22.
 */
public class LoadPersonImageView extends Activity implements View.OnClickListener {

    private static final String TAG = "LoadPersonImageView";
    private ProgressDialog mProgressDialog;
    private ChoosePicDialog mChoosePicDialog;
    private Uri mCurFromCamare;

    private Button loadImg;
    private String mTempStrUR1;
    private String mTempStrT;
    private RoundImageView personImg;

    private UserInfo mUserInfo;
    private int uid;
    private String savePath="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_picture);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        mUserInfo=MainActivity.mUserInfo;
        uid = mUserInfo.getUid();
        LogHelper.i(TAG,"--------uid-"+uid);

        initialize();
        personImg.setOnClickListener(this);

        loadImg.setOnClickListener(this);

    }


    private void initialize() {

        personImg = (RoundImageView) findViewById(R.id.personImg);
        loadImg = (Button) findViewById(R.id.loadImg);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.loadImg:
                LogHelper.i(TAG, "-----mTempStrUR1-----" + mTempStrUR1);
                createProgressDialogTitle("正在上传");
                LogHelper.i(TAG, "-----mTempStrUR1-----" + mTempStrUR1);
                if (!TextUtils.isEmpty(mTempStrUR1)) {

                    new UploadPicTask().execute(mTempStrUR1);
                }

                break;
            case R.id.personImg:
                if (mChoosePicDialog == null) {
                    mChoosePicDialog = new ChoosePicDialog(LoadPersonImageView.this,this);
                }
                mChoosePicDialog.show();
                break;
            case R.id.goToAlbum://相册选择照片
                LogHelper.i(TAG, "------相册选择照片");

                dismissmChooseIconDialog();
                selectPicFromAlbum();
                break;
            case R.id.goToCamera://相机选择拍照
                LogHelper.i(TAG, "------相机选择拍照");
                dismissmChooseIconDialog();
                mCurFromCamare = Uri.parse("file://" + Constants.BZ_CAM_PATH + "/"
                        + "bzzbz_"
                        + "e"+uid +"_"+System.currentTimeMillis()+ ".jpg");
                getPhotoFromCamera(mCurFromCamare, 4);
                LogHelper.i(TAG,"-----"+mCurFromCamare.toString());

                break;
        }
    }

    private class UploadPicTask extends AsyncTask<String, Void, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            //   boolean temp = uploadPicFile(mTempStrURL);
            String url = params[0];
           // String ImageUrl = BitmapUtils.compressImage(url, url, 90);
            boolean temp = uploadPicFile(url);


            LogHelper.i(TAG, "------temp-------" + url);
            LogHelper.i(TAG, "------temp" + url);
            return temp;
        }

        @Override
        protected void onPostExecute(Boolean param) {
            LogHelper.i(TAG, "-----正在上传照片");
            LogHelper.i(TAG, "------temp" + param);
            if (param) {
                dismissProgressDialog();//取消
                showDialog("上传成功");

                LogHelper.i(TAG, "------上传成功");
            } else {
                dismissProgressDialog();//取消
                showDialog("上传失败");
                LogHelper.i(TAG, "------上传失败");
            }
        }
    }


    //上传照片至Server的方法picPath 图片真实路径
    private boolean uploadPicFile(String picPath) {

        String fName = picPath.trim();
        String newName = fName.substring(fName.lastIndexOf("/") + 1);
        LogHelper.i(TAG, "---" + newName);
        if (!newName.substring(0, 6).equals("bzzbz_")) {
            newName = mTempStrT;
        }
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        try {
            URL url = new URL(VolleyHelpApi.BZ_TOU_UPLOAD_Url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
          /* 允许Input、Output，不使用Cache */
            con.setReadTimeout(10 * 1000000);
            con.setConnectTimeout(10 * 1000000);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
          /* 设置传送的method=POST */
            con.setRequestMethod("POST");
          /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary=" + boundary);
          /* 设置DataOutputStream */
            DataOutputStream ds =
                    new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; " +
                    "name=\"hport\";filename=\"" +
                    newName + "\"" + end);
            ds.writeBytes("Content-Type: application/octet-stream; charset=utf-8" + end);
            ds.writeBytes(end);

          /* 取得文件的FileInputStream */

            FileInputStream fStream = new FileInputStream(picPath);

          /* 设置每次写入1024bytes */
            int bufferSize = 1024 * 1024;
            byte[] buffer = new byte[bufferSize];
            int length = -1;
          /* 从文件读取数据至缓冲区 */
            while ((length = fStream.read(buffer)) != -1) {
            /* 将资料写入DataOutputStream中 */
                LogHelper.i(TAG, length + "");
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
          /* close streams */
            fStream.close();
            ds.flush();
          /* 取得Response内容 */
            BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
            String line = null;
            StringBuilder strb = new StringBuilder();
            while((line = br.readLine()) != null){
                strb.append(line);
            }

//{"message":"头像上传成功","result":"success","entity":{"savePath":"http://www.xiaohoutai.com.cn:8888/XHT/empheadportraitController/downloadEmpheadportrait?fileName=1479891737903bzzbz_e4_1479891725889.jpg"},"code":"1"}
            JSONObject json=new JSONObject(strb.toString());
            String entity = json.optString("entity");
            LogHelper.i(TAG,"-------en--"+entity.toString());
            JSONObject jsonObject=new JSONObject(entity);
            savePath = jsonObject.optString("savePath");

            mUserInfo.setmContactUrl(savePath);

          /* 将Response显示于Dialog */
            //showDialog("上传成功"+b.toString().trim());
          /* 关闭DataOutputStream */

            ds.close();
            return true;
        } catch (Exception e) {
            LogHelper.e(TAG, e.toString());
            return false;
        }
    }

    /**
     * 调用相机拍照
     *
     * @param uri
     * @param requestCode
     */
    private void getPhotoFromCamera(Uri uri, int requestCode) {
        mkdirs(uri);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        LogHelper.i(TAG, "-------调用相机拍照-----------");
        startActivityForResult(intent, requestCode);//or TAKE_SMALL_PICTURE
    }

    /**
     * 创建存储照片的文件夹
     *
     * @param uri
     */
    private void mkdirs(Uri uri) {
        String path = uri.getPath();
        File file = new File(path.substring(0, path.lastIndexOf("/")));
        if (!file.exists()) {
            boolean success = file.mkdirs();
            LogHelper.i(TAG,"---创建存储照片的文件夹success = " + success);
        }

    }

    private void selectPicFromAlbum() {

        Intent intent = new Intent(Intent.ACTION_PICK, null);//从列表中选择某项并返回所有数据
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//得到系统所有的图片
                "image/*");//图片的类型,image/*为所有类型图片

        startActivityForResult(intent, 3);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            if (requestCode == 3) {//相册返回

                Bitmap thumbnail =null;
                Uri fullPhotoUri = data.getData();//content://media/external/images/media/3844

                //真实路径
                String realFilePath = BitmapUtils.getRealFilePath(this, fullPhotoUri);

                mCurFromCamare = Uri.parse("file://" + Constants.BZ_CAM_PATH + "/"
                        + "bzzbz_"
                        + "e"+uid +"_"+System.currentTimeMillis()+ ".jpg");


                String path = mCurFromCamare.getPath();
                LogHelper.i(TAG,"-------path---"+path);
                LogHelper.i(TAG,"-------realFilePath---"+realFilePath);
                //压缩新的路径
                String ImageUrl = BitmapUtils.compressImage(realFilePath, path,60);
                String size = BitmapUtils.getAutoFileOrFilesSize(ImageUrl);
                LogHelper.i(TAG,"-------size---"+size);
                LogHelper.i(TAG, "--------fullPhotoUri----" + fullPhotoUri.toString() + "------>>>>>" + realFilePath + "------>>>>>" + ImageUrl);

                thumbnail = BitmapHelper.getThumbnail(this, fullPhotoUri,
                        60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                personImg.setImageBitmap(thumbnail);
                mTempStrUR1 = ImageUrl;
            }
            if (requestCode == 4) {      //照相
                Bitmap thumbnail ;

                mTempStrUR1 = mCurFromCamare.getPath();
                String autoFileOrFilesSize = BitmapUtils.getAutoFileOrFilesSize(mTempStrUR1);
                LogHelper.i(TAG,"-----daxiao--"+autoFileOrFilesSize);//       ---2.38 M
                //压缩文件 KB
                String imageUrl = BitmapUtils.compressImage(mTempStrUR1, mTempStrUR1, 60);
                String FilesSize = BitmapUtils.getAutoFileOrFilesSize(imageUrl);
                LogHelper.i(TAG,"-----daxiao--"+FilesSize+"--");//

                LogHelper.i(TAG, "--------照相返回------" + mTempStrUR1 + "-----");//string   /storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f1_t1478759682811.jpg
                thumbnail = BitmapHelper.getThumbnail(this, mCurFromCamare,
                        60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                personImg.setImageBitmap(thumbnail);
                // mPersonImg.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                LogHelper.i(TAG, "--------照相--1-返回-----mTempStrUR1-" + mTempStrUR1 );
            }
        }
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
     * 创建mProgressDialog
     */
    private void createProgressDialogTitle(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
        }
        mProgressDialog.setTitle(title);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }
    /**
     * 隐藏选择图片对话框
     */
    private void dismissmChooseIconDialog() {
        if (mChoosePicDialog != null) {
            mChoosePicDialog.dismiss();
        }
    }

    /**
     *  显示Dialog的method
     *  */
    private void showDialog(String mess) {
        new AlertDialog.Builder(this).setTitle("Message").setMessage(mess)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent();
                        intent.putExtra("url",mTempStrUR1);
                        intent.putExtra("url1",savePath);
                        setResult(31,intent);
                        finish();
                    }
                }).show();
    }
    /**Error:Execution failed for task ':app:buildInfoDebugLoader'.
     > Exception while doing past iteration backup : Source D:\xht\ServerHelp\app\build\intermediates\builds\debug\33123457965597\classes.dex and destination
     D:\xht\ServerHelp\app\build\intermediates\builds\debug\33123457965597\classes.dex must be different
     * 隐藏mProgressDialog
     */
    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }




}
