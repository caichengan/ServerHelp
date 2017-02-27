package com.xht.android.serverhelp;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.xht.android.serverhelp.model.Constants;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.AsyncImageLoader;
import com.xht.android.serverhelp.util.BitmapHelper;
import com.xht.android.serverhelp.util.BitmapUtils;
import com.xht.android.serverhelp.util.IntentUtils;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2016/11/25.
 *
 * an
 */

public class PicLoadActivity extends Activity implements View.OnClickListener {
    private ImageView mImgFile11;
    private ImageView mImgFile12;
    private ImageView mImgFile13;
    private ImageView mImgFile14;
    private ImageView mImgFile15;
    private ImageView mImgFile16;
    private ImageView mImgFile17;
    private ImageView mImgFile18;
    private Button btnLoadPic;


    private Uri mCurFromCamare;
    private Uri mCurFromCamare1;
    private Uri mCurFromCamare2;
    private Uri mCurFromCamare3;
    private Uri mCurFromCamare4;
    private Uri mCurFromCamare5;
    private Uri mCurFromCamare6;
    private Uri mCurFromCamare7;
    private Uri mCurFromCamare8;
    private AsyncImageLoader imageLoader;

    private String mTempStrT;
    private String mTempStrURL; //将要上传的图片的路径

    private ProgressDialog mProgressDialog;
    private ChoosePicDialog mChoosePicDialog;
    private static final String TAG = "PicLoadActivity";
    private String flowId;
    private String orderId;
    private String serviceId;

    private String imgFile1;
    private String imgFile2;
    private String imgFile3;
    private String imgFile4;
    private String imgFile5;
    private String imgFile6;
    private String imgFile7;
    private String imgFile8;

    private int curIVSelPic;
    private int uid;
    private String style;
    private String mTempStrUR1;
    private String mTempStrUR2;
    private String mTempStrUR3;
    private String mTempStrUR4;
    private String mTempStrUR5;
    private String mTempStrUR6;
    private String mTempStrUR7;
    private String mTempStrUR8;
    private String status;
    private List<String> resultPaths;
    private String[] pathString;
    private boolean flag3;

    private int loadNum=1;

    private Handler handler=new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            LogHelper.i(TAG, "------handleMessage--" + loadNum);
            LogHelper.i(TAG, "------msg.what--" + msg.what);
            switch (msg.what) {
                case 8:
                    LogHelper.i(TAG, "------handleMessage--" + loadNum);


                    if (loadNum == 8) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 7) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 6) {
                        DissAndShowDialog();
                    }

                    if (loadNum == 5) {
                        DissAndShowDialog();
                    }

                    if (loadNum == 4) {
                        DissAndShowDialog();
                    }

                    if (loadNum == 3) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 2) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 1) {
                        DissAndShowDialog();
                    }
                    break;
                case 7:
                    LogHelper.i(TAG, "------handleMessage--" + loadNum);
                    if (loadNum == 7) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 6) {
                        DissAndShowDialog();
                    }

                    if (loadNum == 5) {
                        DissAndShowDialog();
                    }

                    if (loadNum == 4) {
                        DissAndShowDialog();
                    }

                    if (loadNum == 3) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 2) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 1) {
                        DissAndShowDialog();
                    }
                    break;
                case 6:
                    LogHelper.i(TAG, "------handleMessage--" + loadNum);
                    if (loadNum == 6) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 5) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 4) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 3) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 2) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 1) {
                        DissAndShowDialog();
                    }
                    break;
                case 5:
                    LogHelper.i(TAG, "------handleMessage--" + loadNum);
                    if (loadNum == 5) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 4) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 3) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 2) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 1) {
                        DissAndShowDialog();
                    }
                    break;
                case 4:
                    LogHelper.i(TAG, "------handleMessage--" + loadNum);
                    if (loadNum == 4) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 3) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 2) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 1) {
                        DissAndShowDialog();
                    }
                    break;
                case 3:
                    LogHelper.i(TAG, "------handleMessage--" + loadNum);
                    if (loadNum == 3) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 2) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 1) {
                        DissAndShowDialog();
                    }
                    break;
                case 2:
                    LogHelper.i(TAG, "------handleMessage--" + loadNum);
                    if (loadNum == 2) {
                        DissAndShowDialog();
                    }
                    if (loadNum == 1) {
                        DissAndShowDialog();
                    }
                    break;
                case 1:
                    LogHelper.i(TAG, "------handleMessage--" + loadNum);
                    if (loadNum == 1) {
                        DissAndShowDialog();
                    }

                    break;
            }
            return true;

        }
    });

    private void DissAndShowDialog() {
        dismissProgressDialog();
        showDialog("上传成功");
        App.getInstance().showToast("上传成功");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picload);
        TextView mCustomView = new TextView(this);
        mCustomView.setGravity(Gravity.CENTER);
        mCustomView.setText("返回");
        mCustomView.setTextSize(18);
        final ActionBar aBar = getActionBar();
        aBar.setCustomView(mCustomView,
                new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        int change = ActionBar.DISPLAY_HOME_AS_UP | ActionBar.DISPLAY_SHOW_CUSTOM;
        aBar.setDisplayOptions(change);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        flowId = bundle.getString("flowId");
        orderId = bundle.getString("orderId");
        serviceId = bundle.getString("serviceId");
        status = bundle.getString("status");
        //过程与结果的图片

        uid = bundle.getInt("uid", -1);
        style = bundle.getString("style");

        imgFile1 = bundle.getString("imgFile1");
        imgFile2 = bundle.getString("imgFile2");
        imgFile3 = bundle.getString("imgFile3");
        imgFile4 = bundle.getString("imgFile4");
        imgFile5= bundle.getString("imgFile5");
        imgFile6 = bundle.getString("imgFile6");
        imgFile7 = bundle.getString("imgFile7");
        imgFile8 = bundle.getString("imgFile8");


        LogHelper.i(TAG,"-----flowId-orderId-serviceId"+flowId+"--"+orderId+"--"+serviceId);
        LogHelper.i(TAG,"-----imgFile1-imgFile2-imgFile3"+imgFile1+"--"+imgFile2+"--"+imgFile3);
        LogHelper.i(TAG,"-----imgFile4-imgFile5-imgFile6"+imgFile4+"--"+imgFile5+"--"+imgFile6);
        LogHelper.i(TAG,"-----imgFile7-imgFile8-"+imgFile7+"--"+imgFile8+"--");

        imageLoader=new AsyncImageLoader(this);
        initialize();

        initImageView();

    }

    private void initImageView() {

        createProgressDialogTitle("正在加载数据");
       /* Bitmap bitmap = imageLoader.loadImage(mImgFile11, imgFile1);
        Bitmap bitmap1 = imageLoader.loadImage(mImgFile12, imgFile2);
        Bitmap bitmap2 = imageLoader.loadImage(mImgFile13, imgFile3);
        mImgFile11.setImageBitmap(bitmap);
        mImgFile12.setImageBitmap(bitmap1);
        mImgFile13.setImageBitmap(bitmap2);*/

        if (!imgFile1.equals("null")) {
            LogHelper.i(TAG,"----------1");
            BitmapUtils.loadImgageUrl(imgFile1, mImgFile11);

            if (!imgFile2.equals("null")) {
                LogHelper.i(TAG,"----------2");
                mImgFile12.setVisibility(View.VISIBLE);
                BitmapUtils.loadImgageUrl(imgFile2,mImgFile12);

                if (!imgFile3.equals("null")) {
                    LogHelper.i(TAG,"----------3");
                    mImgFile13.setVisibility(View.VISIBLE);
                    BitmapUtils.loadImgageUrl(imgFile3,mImgFile13);


                    if (!imgFile4.equals("null")) {
                        LogHelper.i(TAG,"----------4");
                        mImgFile14.setVisibility(View.VISIBLE);
                        BitmapUtils.loadImgageUrl(imgFile4,mImgFile14);
                        if (!imgFile5.equals("null")) {
                            LogHelper.i(TAG,"----------5");
                            mImgFile15.setVisibility(View.VISIBLE);
                            BitmapUtils.loadImgageUrl(imgFile5,mImgFile15);
                            if (!imgFile6.equals("null")) {
                                LogHelper.i(TAG,"----------6");
                                mImgFile16.setVisibility(View.VISIBLE);
                                BitmapUtils.loadImgageUrl(imgFile6,mImgFile16);
                                if (!imgFile7.equals("null")) {
                                    LogHelper.i(TAG,"----------7");
                                    mImgFile17.setVisibility(View.VISIBLE);
                                    BitmapUtils.loadImgageUrl(imgFile7,mImgFile17);
                                    if (!imgFile8.equals("null")) {
                                        LogHelper.i(TAG,"----------8");
                                        mImgFile18.setVisibility(View.VISIBLE);
                                        BitmapUtils.loadImgageUrl(imgFile8,mImgFile18);
                                    }else{
                                        if (status.equals("4")||status.equals("5")){
                                            mImgFile18.setVisibility(View.INVISIBLE);
                                        }else {
                                            mImgFile18.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }else{
                                    if (status.equals("4")||status.equals("5")){
                                        mImgFile17.setVisibility(View.INVISIBLE);
                                    }else {
                                        mImgFile17.setVisibility(View.VISIBLE);
                                    }
                                }
                            }else{
                                if (status.equals("4")||status.equals("5")){
                                    mImgFile16.setVisibility(View.INVISIBLE);
                                }else {
                                    mImgFile16.setVisibility(View.VISIBLE);
                                }
                            }
                        }else{
                            if (status.equals("4")||status.equals("5")){
                                mImgFile15.setVisibility(View.INVISIBLE);
                            }else {
                                mImgFile15.setVisibility(View.VISIBLE);
                            }
                        }
                    }else{
                        if (status.equals("4")||status.equals("5")){
                            mImgFile14.setVisibility(View.INVISIBLE);
                        }else {
                            mImgFile14.setVisibility(View.VISIBLE);
                        }
                    }
                }else{
                    if (status.equals("4")||status.equals("5")){
                        mImgFile13.setVisibility(View.INVISIBLE);
                    }else {
                        mImgFile13.setVisibility(View.VISIBLE);
                    }
                }
            }else{
                if (status.equals("4")||status.equals("5")){
                    mImgFile12.setVisibility(View.INVISIBLE);
                }else {
                    mImgFile12.setVisibility(View.VISIBLE);
                }
                mImgFile13.setVisibility(View.INVISIBLE);
            }

        }else{
            mImgFile11.setImageDrawable(null);
        }
        dismissProgressDialog();
    }
    private void initialize() {
        mImgFile11 = (ImageView) findViewById(R.id.mImgFile_11);
        mImgFile12 = (ImageView) findViewById(R.id.mImgFile_12);
        mImgFile13 = (ImageView) findViewById(R.id.mImgFile_13);
        mImgFile14 = (ImageView) findViewById(R.id.mImgFile_14);
        mImgFile15 = (ImageView) findViewById(R.id.mImgFile_21);
        mImgFile16 = (ImageView) findViewById(R.id.mImgFile_22);
        mImgFile17 = (ImageView) findViewById(R.id.mImgFile_23);
        mImgFile18 = (ImageView) findViewById(R.id.mImgFile_24);


        btnLoadPic = (Button) findViewById(R.id.btnLoadPic);
        initData();
    }

    private void initData() {
        mImgFile11.setOnClickListener(this);
        mImgFile12.setOnClickListener(this);
        mImgFile13.setOnClickListener(this);
        mImgFile14.setOnClickListener(this);
        mImgFile15.setOnClickListener(this);
        mImgFile16.setOnClickListener(this);
        mImgFile17.setOnClickListener(this);
        mImgFile18.setOnClickListener(this);

        btnLoadPic.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mImgFile_11:
                if (!imgFile1.equals("null")){
                      LogHelper.i(TAG, "-------onClick---imgFile1-----" + imgFile1);
                      mImgFile11.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Bundle bundle=new Bundle();
                                bundle.putString("imgUrl",imgFile1);
                                IntentUtils.startActivityNumber(PicLoadActivity.this,bundle,SpaceImageDetailActivity.class);
                            }
                        });
                    }else {

                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(PicLoadActivity.this, this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 11;
                }
                break;
            case R.id.mImgFile_12:

                if (!imgFile2.equals("null")){
                    LogHelper.i(TAG, "-------onClick---imgFile2-----" + imgFile2);
                    mImgFile12.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle=new Bundle();
                            bundle.putString("imgUrl",imgFile2);
                            IntentUtils.startActivityNumber(PicLoadActivity.this,bundle,SpaceImageDetailActivity.class);
                        }
                    });
                }else {

                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(PicLoadActivity.this, this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 12;
                }
                break;
            case R.id.mImgFile_13:
                LogHelper.i(TAG, "-------onClick---imgFile3-----" + imgFile3);
                if (!imgFile3.equals("null")){
                    LogHelper.i(TAG, "-------onClick---imgFile3-----" + imgFile3);
                    mImgFile13.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle=new Bundle();
                            bundle.putString("imgUrl",imgFile3);
                            IntentUtils.startActivityNumber(PicLoadActivity.this,bundle,SpaceImageDetailActivity.class);
                        }
                    });
                }else {

                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(PicLoadActivity.this, this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 13;
                }
                break;
            case R.id.mImgFile_14:
                LogHelper.i(TAG, "-------onClick---imgFile4-----" + imgFile4);
                if (!imgFile4.equals("null")){
                    LogHelper.i(TAG, "-------onClick---imgFile4-----" + imgFile4);
                    mImgFile14.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle=new Bundle();
                            bundle.putString("imgUrl",imgFile4);
                            IntentUtils.startActivityNumber(PicLoadActivity.this,bundle,SpaceImageDetailActivity.class);
                        }
                    });
                }else {

                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(PicLoadActivity.this, this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 14;
                }
                break;
            case R.id.mImgFile_21:
                LogHelper.i(TAG, "-------onClick---imgFile4-----" + imgFile5);
                if (!imgFile5.equals("null")){
                    LogHelper.i(TAG, "-------onClick---imgFile4-----" + imgFile5);
                    mImgFile15.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle=new Bundle();
                            bundle.putString("imgUrl",imgFile5);
                            IntentUtils.startActivityNumber(PicLoadActivity.this,bundle,SpaceImageDetailActivity.class);
                        }
                    });
                }else {

                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(PicLoadActivity.this, this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 15;
                }
                break;
            case R.id.mImgFile_22:
                LogHelper.i(TAG, "-------onClick---imgFile6-----" + imgFile6);
                if (!imgFile6.equals("null")){
                    LogHelper.i(TAG, "-------onClick---imgFile6-----" + imgFile6);
                    mImgFile16.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle=new Bundle();
                            bundle.putString("imgUrl",imgFile6);
                            IntentUtils.startActivityNumber(PicLoadActivity.this,bundle,SpaceImageDetailActivity.class);
                        }
                    });
                }else {

                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(PicLoadActivity.this, this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 16;
                }
                break;
            case R.id.mImgFile_23:
                LogHelper.i(TAG, "-------onClick---imgFile7-----" + imgFile7);
                if (!imgFile7.equals("null")){
                    LogHelper.i(TAG, "-------onClick---imgFile7----" + imgFile7);
                    mImgFile17.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle=new Bundle();
                            bundle.putString("imgUrl",imgFile7);
                            IntentUtils.startActivityNumber(PicLoadActivity.this,bundle,SpaceImageDetailActivity.class);
                        }
                    });
                }else {

                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(PicLoadActivity.this, this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 17;
                }
                break;
            case R.id.mImgFile_24:
                LogHelper.i(TAG, "-------onClick---imgFile8-----" + imgFile8);
                if (!imgFile8.equals("null")){
                    LogHelper.i(TAG, "-------onClick---imgFile8----" + imgFile8);
                    mImgFile18.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Bundle bundle=new Bundle();
                            bundle.putString("imgUrl",imgFile8);
                            IntentUtils.startActivityNumber(PicLoadActivity.this,bundle,SpaceImageDetailActivity.class);
                        }
                    });
                }else {

                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(PicLoadActivity.this, this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 18;
                }
                break;
            case R.id.btnLoadPic:

                MethodUdLoad();

                break;

            case R.id.goToAlbum://相册选择照片
                LogHelper.i(TAG, "------相册选择照片");

                dismissmChooseIconDialog();
                selectPicFromAlbum();
                break;
            case R.id.goToCamera://相机选择拍照
                LogHelper.i(TAG, "------相机选择拍照");
                MethodCamera();

                break;
        }


    }

    /**
     * 上传图片到服务器
     */
    private void MethodUdLoad() {

        LogHelper.i(TAG,"--------status---"+status);

        if (status.equals("4") || status.equals("5")) {
            App.getInstance().showToast("已经上传过照片");
            return;
        }
        resultPaths = new ArrayList<>();

        LogHelper.i(TAG, "-------" + mTempStrUR1);//null
        LogHelper.i(TAG, "-------" + mTempStrUR2);
        LogHelper.i(TAG, "-------" + imgFile1);


        if (!imgFile1.equals("null") && !imgFile2.equals("null") && !imgFile3.equals("null")&&!imgFile4.equals("null") && !imgFile5.equals("null") && !imgFile6.equals("null") && !imgFile7.equals("null") && !imgFile8.equals("null")) {
            LogHelper.i(TAG, "-----11--");
            App.getInstance().showToast("已经上传完照片");
            return;
        }

        if (TextUtils.isEmpty(mTempStrUR1) && TextUtils.isEmpty(mTempStrUR2) && TextUtils.isEmpty(mTempStrUR3)&&TextUtils.isEmpty(mTempStrUR4) && TextUtils.isEmpty(mTempStrUR5) && TextUtils.isEmpty(mTempStrUR6)&& TextUtils.isEmpty(mTempStrUR7) && TextUtils.isEmpty(mTempStrUR8)) {
            App.getInstance().showToast("先选择照片");
            LogHelper.i(TAG, "----22---");
            return;
        }


        createProgressDialogTitle("正在上传");

        if (uid == 0) {
            App.getInstance().showToast("服务器繁忙，请稍后再试....");
            LogHelper.i(TAG, "------" + uid);

            dismissProgressDialog();
            return;
        }

        //LogHelper.i(TAG, "-----mTempStrUR1-----" + mTempStrUR1+mTempStrUR2+mTempStrUR3);
        //  new UploadPicTask().execute(mTempStrUR1,mTempStrUR2,mTempStrUR3);

        LogHelper.i(TAG, "-----mTempStrUR1-----" + mTempStrUR1);
        if (!TextUtils.isEmpty(mTempStrUR1)) {
            resultPaths.add(mTempStrUR1);
            new UploadPicTask().execute(mTempStrUR1);

        }
        LogHelper.i(TAG, "-----mTempStrUR2-----" + mTempStrUR2);
        if (!TextUtils.isEmpty(mTempStrUR2)) {
            resultPaths.add(mTempStrUR2);
            new UploadPicTask().execute(mTempStrUR2);
        }
        LogHelper.i(TAG, "-----mTempStrUR3-----" + mTempStrUR3);

        if (!TextUtils.isEmpty(mTempStrUR3)) {
            resultPaths.add(mTempStrUR3);
            new UploadPicTask().execute(mTempStrUR3);
        }
        LogHelper.i(TAG, "-----mTempStrUR4-----" + mTempStrUR4);
        if (!TextUtils.isEmpty(mTempStrUR4)) {
            resultPaths.add(mTempStrUR4);
            new UploadPicTask().execute(mTempStrUR4);
        }
        LogHelper.i(TAG, "-----mTempStrUR5----" + mTempStrUR5);
        if (!TextUtils.isEmpty(mTempStrUR5)) {
            resultPaths.add(mTempStrUR5);
            new UploadPicTask().execute(mTempStrUR5);
        }
        LogHelper.i(TAG, "-----mTempStrUR6-----" + mTempStrUR6);
        if (!TextUtils.isEmpty(mTempStrUR6)) {
            resultPaths.add(mTempStrUR6);
            new UploadPicTask().execute(mTempStrUR6);
        }
        LogHelper.i(TAG, "-----mTempStrUR7-----" + mTempStrUR7);
        if (!TextUtils.isEmpty(mTempStrUR7)) {
            resultPaths.add(mTempStrUR7);
            new UploadPicTask().execute(mTempStrUR7);
        }
        LogHelper.i(TAG, "-----mTempStrUR8-----" + mTempStrUR8);
        if (!TextUtils.isEmpty(mTempStrUR8)) {
            resultPaths.add(mTempStrUR8);
            new UploadPicTask().execute(mTempStrUR8);
        }

        LogHelper.i(TAG, "------loadNum" + loadNum);
        LogHelper.i(TAG, "------loadNum" + loadNum);

        final Message message=new Message();

        if (curIVSelPic == 18) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogHelper.i(TAG, "------run--" + loadNum);
                    LogHelper.i(TAG, "------run--" + curIVSelPic);
                    message.what=loadNum;
                    handler.sendMessage(message);
                }
            },(loadNum*1000+1000));
            return;
        }
        if (curIVSelPic == 17) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogHelper.i(TAG, "------run--" + loadNum);
                    LogHelper.i(TAG, "------run--" + curIVSelPic);
                    message.what=loadNum;
                    handler.sendMessage(message);
                }
            },(loadNum*1000+1000));
            return;
        }
        if (curIVSelPic == 16) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogHelper.i(TAG, "------run--" + loadNum);
                    LogHelper.i(TAG, "------run--" + curIVSelPic);
                    message.what=loadNum;
                    handler.sendMessage(message);
                }
            },(loadNum*1000+1000));
            return;
        }

        if (curIVSelPic == 15) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogHelper.i(TAG, "------run--" + loadNum);
                    LogHelper.i(TAG, "------run--" + curIVSelPic);
                    message.what=loadNum;
                    handler.sendMessage(message);
                }
            },(loadNum*1000+1000));
            return;
        }
        if (curIVSelPic == 14) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogHelper.i(TAG, "------run--" + loadNum);
                    LogHelper.i(TAG, "------run--" + curIVSelPic);
                    message.what=loadNum;
                    handler.sendMessage(message);
                }
            },(loadNum*1000+1000));
            return;
        }
        if (curIVSelPic == 13) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        LogHelper.i(TAG, "------run--" + loadNum);
                        LogHelper.i(TAG, "------run--" + curIVSelPic);
                        message.what=loadNum;
                        handler.sendMessage(message);
                    }
                },(loadNum*1000+1000));
                return;
            }
        if (curIVSelPic == 12) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogHelper.i(TAG, "------run--" + loadNum);
                    LogHelper.i(TAG, "------run--" + curIVSelPic);

                    message.what=loadNum;
                    handler.sendMessage(message);
                }
            },4000);
            return;
        }
        if (curIVSelPic == 11) {

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    LogHelper.i(TAG, "------run--" + loadNum);
                    LogHelper.i(TAG, "------run--" + curIVSelPic);


                    message.what=loadNum;
                    handler.sendMessage(message);
                    //handler.sendEmptyMessage(1);
                }
            },3000);

            return;
        }

      /*  if (flag3) {
            App.getInstance().showToast("已经上传完毕");
            return;
        }*/
    }

    private void MethodCamera() {
    /*
    图片名称格式（filename的值）：（如o100_s120_e10_f11.jpg，o100：表示订单编号为100，s120：表示服务编号为120，
    e10：表示员工编号为10，f11：表示流程编号为11。中间的下划线（"_"）不能少。顺序必须是：订单编号、服务编号、员工编号、流程编号）
    图片的表单名（即Content-Disposition: form-data; name=\"img\"中的name值）：必须是“img”
    请求方式：POST
    返回结果：成功返回｛"code":"1","result":"success","message":"文件上传成功","entity":{"fileId","文件编号"}｝
    失败返回｛"code":"0","result":"error","message":"系统错误","entity":null｝
     */
        dismissmChooseIconDialog();
        LogHelper.i(TAG, "-----ProsItem-相机拍照--" + orderId + "--" + serviceId);
        if (style.equals("result")){
            mCurFromCamare = Uri.parse("file://" + Constants.BZ_CAM_PATH + "/"
                    + "bzzbz_" + "o" + orderId + "_s" +serviceId//+ item.getOrderId()
                    + "_e" + uid + "_f" + flowId + "_t" + System.currentTimeMillis() + "_j" + ".jpg");
        }else{//过程
            mCurFromCamare = Uri.parse("file://" + Constants.BZ_CAM_PATH + "/"
                    + "bzzbz_" + "o" + orderId + "_s" +serviceId
                    + "_e" + uid + "_f" + flowId + "_t" + System.currentTimeMillis() + "_g" + ".jpg");
        }
        switch (curIVSelPic) {
            case 11:
                mCurFromCamare1 = mCurFromCamare;
                LogHelper.i(TAG, "------相机拍照--1--" + mCurFromCamare1);
                //file:///storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f11478239384309.jpg
                break;
            case 12:
                mCurFromCamare2 = mCurFromCamare;
                LogHelper.i(TAG, "------相机拍照--2--" + mCurFromCamare2);//TODO mCurFromCamare2 File
                //file:///storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f11478239414869.jpg
                break;
            case 13:
                mCurFromCamare3 = mCurFromCamare;
                LogHelper.i(TAG, "------相机拍照--3--" + mCurFromCamare3);
                // file:///storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f11478239438180.jpg
                break;
            case 14:
                mCurFromCamare4 = mCurFromCamare;
                LogHelper.i(TAG, "------相机拍照--4--" + mCurFromCamare4);
                // file:///storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f11478239438180.jpg
                break;
            case 15:
                mCurFromCamare5 = mCurFromCamare;
                LogHelper.i(TAG, "------相机拍照--5--" + mCurFromCamare5);
                // file:///storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f11478239438180.jpg
                break;
            case 16:
                mCurFromCamare6 = mCurFromCamare;
                LogHelper.i(TAG, "------相机拍照--6--" + mCurFromCamare6);
                // file:///storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f11478239438180.jpg
                break;
            case 17:
                mCurFromCamare7 = mCurFromCamare;
                LogHelper.i(TAG, "------相机拍照--7--" + mCurFromCamare7);
                // file:///storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f11478239438180.jpg
                break;
            case 18:
                mCurFromCamare8 = mCurFromCamare;
                LogHelper.i(TAG, "------相机拍照--8--" + mCurFromCamare8);
                // file:///storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f11478239438180.jpg
                break;

        }
        getPhotoFromCamera(mCurFromCamare, 41);
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
        new AlertDialog.Builder(this).setTitle("Message").setMessage(mess).setCancelable(false)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                        Intent intent = new Intent(JinDuFragment.BRO_ACT);
                        intent.putExtra("mUid",uid);
                        intent.putExtra("ordId",orderId);
                        sendBroadcast(intent);

                        finish();
                        //startActivity(new Intent(PicLoadActivity.this, RwxqActivity.class));



                    }
                }).show();
    }
    /**
     * 隐藏mProgressDialog
     */
    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }


    private class UploadPicTask extends AsyncTask<String, Void, Boolean> {
        private boolean temp;
        @Override
        protected Boolean doInBackground(String... params) {
            //   boolean temp = uploadPicFile(mTempStrURL);
            String url = params[0];
            temp = uploadPicFile(url);
            LogHelper.i(TAG, "------temp" + temp);
            if (temp) {
                loadNum ++;
            }
            LogHelper.i(TAG, "------loadNum" + loadNum);
            return temp;
        }
        @Override
        protected void onPostExecute(Boolean param) {
            LogHelper.i(TAG, "-----正在上传照片");
            LogHelper.i(TAG, "------temp" + param);
            /*if (param) {
                dismissProgressDialog();//取消
                showDialog("上传成功");
                App.getInstance().showToast("上传成功");
                LogHelper.i(TAG, "------上传成功");
            } else {
                dismissProgressDialog();//取消
                showDialog("上传失败");
                LogHelper.i(TAG, "------上传失败");
            }*/
        }
    }


    private class UploadPic extends AsyncTask<String[], Void, Boolean> {
        private boolean temp;


        @Override
        protected Boolean doInBackground(String[]... params) {

            int length = params.length;

            //temp = uploadPicFile(params);
            LogHelper.i(TAG, "------temp" + temp);
            LogHelper.i(TAG, "------length" + length);

            return temp;
        }

        @Override
        protected void onPostExecute(Boolean param) {
            LogHelper.i(TAG, "-----正在上传照片");
            LogHelper.i(TAG, "------temp" + param);
            if (param) {
                DissAndShowDialog();
                LogHelper.i(TAG, "------上传成功");
            } else {
                dismissProgressDialog();//取消
                showDialog("上传失败");
                LogHelper.i(TAG, "------上传失败");
            }
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



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            if (requestCode == 31) {//相册返回

                Bitmap thumbnail;
                Uri fullPhotoUri = data.getData();//content://media/external/images/media/3844

                //真实路径
                String realFilePath = BitmapUtils.getRealFilePath(this, fullPhotoUri);


                if (style.equals("result")) {

                    mCurFromCamare = Uri.parse("file://" + Constants.BZ_PIC_PATH + "/"
                            + "bzzbz_" + "o" + orderId + "_s" + serviceId
                            + "_e" + uid + "_f" + flowId + "_t" + System.currentTimeMillis() + "_j" + ".jpg");
                } else {
                    //("/sdcard/image.jpg")
                    mCurFromCamare = Uri.parse("file://" + Constants.BZ_PIC_PATH + "/"
                            + "bzzbz_" + "o" + orderId + "_s" + serviceId
                            + "_e" + uid + "_f" + flowId + "_t" + System.currentTimeMillis() + "_g" + ".jpg");
                }


                String path = mCurFromCamare.getPath();


                //压缩新的路径
                String ImageUrl = BitmapUtils.compressImage(realFilePath, path, 50);

                LogHelper.i(TAG, "-------fullPhotoUri---" + fullPhotoUri.toString());
                LogHelper.i(TAG, "-------realFilePath---" + realFilePath);
                String size = BitmapUtils.getAutoFileOrFilesSize(ImageUrl);
                LogHelper.i(TAG, "---------大小---" + size);

                LogHelper.i(TAG, "-------ImageUrl---" + ImageUrl);
                LogHelper.i(TAG, "--------fullPhotoUri----" + fullPhotoUri.toString() + "------>>>>>" + realFilePath + "------>>>>>" + ImageUrl);
                //-fullPhotoUri----content://media/external/images/media/3844------>>>>>/storage/emulated/0/Pictures/Screenshots/S61101-153152.jpg
                // ------>>>>>/storage/emulated/0/ServerHelp/bzbz/camera/bzzbz_o48_s139_e4_f1_t1479002629947_g.jpg
                switch (curIVSelPic) {
                    case 11:
                        //mTempBitmap = BitmapFactory.decodeFile(mTempStrURL);


                        thumbnail = BitmapHelper.getThumbnail(this, fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);

                        //holder.mImgFile11.setImageBitmap(thumbnail);
                        mImgFile11.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile12.setVisibility(View.VISIBLE);
                        mTempStrUR1 = ImageUrl;


                        break;
                    case 12:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        //holder.mImgFile12.setImageBitmap(thumbnail);
                        mImgFile12.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile13.setVisibility(View.VISIBLE);
                        mTempStrUR2 = ImageUrl;

                        break;
                    case 13:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        // holder.mImgFile13.setImageBitmap(thumbnail);
                        mImgFile13.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile14.setVisibility(View.VISIBLE);
                        mTempStrUR3 = ImageUrl;

                        break;
                    case 14:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        // holder.mImgFile13.setImageBitmap(thumbnail);
                        mImgFile14.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile15.setVisibility(View.VISIBLE);
                        mTempStrUR4 = ImageUrl;

                        break;
                    case 15:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        // holder.mImgFile13.setImageBitmap(thumbnail);
                        mImgFile15.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile16.setVisibility(View.VISIBLE);
                        mTempStrUR5 = ImageUrl;

                        break;
                    case 16:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        // holder.mImgFile13.setImageBitmap(thumbnail);
                        mImgFile16.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile17.setVisibility(View.VISIBLE);
                        mTempStrUR6 = ImageUrl;

                        break;
                    case 17:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        // holder.mImgFile13.setImageBitmap(thumbnail);
                        mImgFile17.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile18.setVisibility(View.VISIBLE);
                        mTempStrUR7 = ImageUrl;

                        break;
                    case 18:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        // holder.mImgFile13.setImageBitmap(thumbnail);
                        mImgFile18.setBackgroundDrawable(new BitmapDrawable(thumbnail));

                        mTempStrUR8= ImageUrl;

                        break;

                }

            }
            if (requestCode == 41) {      //照相
                Bitmap thumbnail;
                mTempStrURL = mCurFromCamare.getPath();

                LogHelper.i(TAG, "-----照相-----------mTempStrURL-" + mTempStrURL + "--");//
                String autoFileOrFilesSize = BitmapUtils.getAutoFileOrFilesSize(mTempStrURL);

                LogHelper.i(TAG, "-----daxiao--" + autoFileOrFilesSize);//       ---2.38 M

                //压缩文件 KB
                String imageUrl = BitmapUtils.compressImage(mTempStrURL, mTempStrURL,50);

                String FilesSize = BitmapUtils.getAutoFileOrFilesSize(imageUrl);
                String FilesSize1 = BitmapUtils.getAutoFileOrFilesSize(mTempStrURL);
                LogHelper.i(TAG, "-----daxiao--" + FilesSize + "--" + FilesSize1);//
                LogHelper.i(TAG, "-----照相--mTempStrURL" + "--" + mTempStrURL);//

                LogHelper.i(TAG, "--------照相返回------" + mTempStrURL + "-----");//string   /storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f1_t1478759682811.jpg
                switch (curIVSelPic) {
                    case 11:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);

                        thumbnail = BitmapHelper.getThumbnail(this, mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);


                        long bitmapsize = BitmapHelper.getBitmapsize(thumbnail);
                        int byteCount = thumbnail.getByteCount();
                        LogHelper.i(TAG, "-----大小-thumbnail-" + byteCount + "----" + bitmapsize);

                        mTempStrUR1 = mTempStrURL;
                        mImgFile11.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        LogHelper.i(TAG, "--------照相--1-返回-----mTempStrUR1-" + mTempStrUR1 + "-----" + mCurFromCamare.toString());
                        mImgFile12.setVisibility(View.VISIBLE);

                        break;
                    case 12:
                        LogHelper.i(TAG, "-------------" + curIVSelPic + "-----" + mTempStrURL);
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        mTempStrUR2 = mTempStrURL;

                        LogHelper.i(TAG, "--------照相--2--返回-----mTempStrUR2-" + mTempStrUR2 + "-----" + mCurFromCamare.toString());
                        mImgFile12.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile13.setVisibility(View.VISIBLE);

                        break;
                    case 13:
                        LogHelper.i(TAG, "-------------" + curIVSelPic + "-----");
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        mTempStrUR3 = mTempStrURL;
                        LogHelper.i(TAG, "--------照相--3--返回-----mTempStrUR3-" + mTempStrUR3 + "-----" + mCurFromCamare.toString());
                        mImgFile13.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile14.setVisibility(View.VISIBLE);

                        break;
                    case 14:
                        LogHelper.i(TAG, "-------------" + curIVSelPic + "-----");
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        mTempStrUR4 = mTempStrURL;
                        LogHelper.i(TAG, "--------照相--4--返回-----mTempStrUR3-" + mTempStrUR4 + "-----" + mCurFromCamare.toString());
                        mImgFile14.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile15.setVisibility(View.VISIBLE);

                        break;
                    case 15:
                        LogHelper.i(TAG, "-------------" + curIVSelPic + "-----");
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        mTempStrUR5 = mTempStrURL;
                        LogHelper.i(TAG, "--------照相--5--返回-----mTempStrUR5-" + mTempStrUR5 + "-----" + mCurFromCamare.toString());
                        mImgFile15.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile16.setVisibility(View.VISIBLE);

                        break;
                    case 16:
                        LogHelper.i(TAG, "-------------" + curIVSelPic + "-----");
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        mTempStrUR6 = mTempStrURL;
                        LogHelper.i(TAG, "--------照相--6--返回-----mTempStrUR6-" + mTempStrUR6 + "-----" + mCurFromCamare.toString());
                        mImgFile16.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile17.setVisibility(View.VISIBLE);

                        break;
                    case 17:
                        LogHelper.i(TAG, "-------------" + curIVSelPic + "-----");
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        mTempStrUR7 = mTempStrURL;
                        LogHelper.i(TAG, "--------照相-7--返回-----mTempStrUR7-" + mTempStrUR7 + "-----" + mCurFromCamare.toString());
                        mImgFile17.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mImgFile18.setVisibility(View.VISIBLE);

                        break;
                    case 18:
                        LogHelper.i(TAG, "-------------" + curIVSelPic + "-----");
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(this, mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        mTempStrUR8 = mTempStrURL;
                        LogHelper.i(TAG, "--------照相-8--返回-----mTempStrUR8-" + mTempStrUR8 + "-----" + mCurFromCamare.toString());
                        mImgFile18.setBackgroundDrawable(new BitmapDrawable(thumbnail));

                        break;


                }
            }
        }
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
       /* boolean isKitKatO = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        Intent getAlbum;
        if (isKitKatO) {
            getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
        }
        getAlbum.setType("image*//*");
        startActivityForResult(getAlbum, 0);*/

        Intent intent = new Intent(Intent.ACTION_PICK, null);//从列表中选择某项并返回所有数据
        intent.setDataAndType(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//得到系统所有的图片
                "image/*");//图片的类型,image/*为所有类型图片

        startActivityForResult(intent, 31);
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
            URL url = new URL(VolleyHelpApi.BZ_PIC_UPLOAD_Url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
          /* 允许Input、Output，不使用Cache */
          //  con.setReadTimeout(20*1000);
            con.setConnectTimeout( 3*1000);
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
                    "name=\"img\";filename=\"" +
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
            InputStream is = con.getInputStream();


            int ch;
            StringBuffer b = new StringBuffer();
            while ((ch = is.read()) != -1) {
                b.append((char) ch);
            }
            ds.close();
            //{"message":"æä»¶ä¸ä¼ æå","result":"success","entity":{"fileId":"43"},"code":"1"}
            LogHelper.i(TAG,"----b--"+b.toString());
            JSONObject object=new JSONObject(b.toString());
            String code = object.optString("code");

            if (code.equals("1")){
                return true;
            }else{
                return false;
            }

          /* 将Response显示于Dialog */
            //showDialog("上传成功"+b.toString().trim());
          /* 关闭DataOutputStream */

        } catch (Exception e) {
            LogHelper.e(TAG, e.toString());
            return false;
        }
    }

    /**
     * 转换时间
     *
     * @param time
     * @return
     */
    public String getDateTime(long time) {
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date);
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

}
