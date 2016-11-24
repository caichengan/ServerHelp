package com.xht.android.serverhelp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xht.android.serverhelp.model.Constants;
import com.xht.android.serverhelp.model.ProsItem;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.AsyncImageLoader;
import com.xht.android.serverhelp.util.BitmapHelper;
import com.xht.android.serverhelp.util.BitmapUtils;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
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
 * UJinDuFragment
 * <p>
 * 办证中列表项的详细
 */
public class JinDuFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private static final String TAG = "JinDuFragment";
    private SharedPreferences sharedPreferences;
    ProgressAdapter.ViewHolder holder;
    private ProgressDialog mProgressDialog;

    //提交更新进度后，出现的对任务进行的三个操作的Layout
    private LinearLayout mRwZzSel1, mRwZzSel2;
    private ChoosePicDialog mChoosePicDialog;
    private int curIVSelPic;    //为哪一个IV选择照片
    private Bitmap mTempBitmap; //将要上传的图片
    private String mTempStrURL; //将要上传的图片的路径
    private String mTempStrUR1 = null; //将要上传的图片的路径
    private String mTempStrUR2 = null; //将要上传的图片的路径
    private String mTempStrUR3 = null; //将要上传的图片的路径
    private String mTempStrUR4 = null; //将要上传的图片的路径
    private String mTempStrUR5 = null; //将要上传的图片的路径
    private String mTempStrUR6 = null; //将要上传的图片的路径

    //由于相册返回的照片路径不是想要的路径，所以临时又声明一个路径
    private String mTempStrT;
    private boolean mShiFouJiangSCFlag; //是否必须要上传先
    private boolean mNotSelPic;  //不能选择图片吗？
    private int mWjNum11;   //文件照已经上传了多少张
    private int mJgNum11;   //结果照已经上传了多少张
    private Uri mCurFromCamare;
    private Uri mCurFromCamare1;
    private Uri mCurFromCamare2;
    private Uri mCurFromCamare3;
    private Uri mCurFromCamare4;
    private Uri mCurFromCamare5;
    private Uri mCurFromCamare6;
    private ArrayList<ProsItem> mProsItems = new ArrayList<>();

    boolean flag1 = false;//标记第一步完成
    boolean flag11 = false;//标记第一步完成
    private MainActivity mMainActivity;
    RwxqActivity mRwxqActivity;

    private ListView mProgressListView;

    private ProgressAdapter progressAdapter;
    private List<String> filePaths;
    private List<String> resultPaths;
    private SharedPreferences.Editor edit;
    private MainActivity mMainActivity1;
    private int uid;
    private List<TaskPeople> mListTask;


    public JinDuFragment() {
        // Required empty public constructor
    }

    /**
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment JinDuFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static JinDuFragment newInstance(String param1, String param2) {
        JinDuFragment fragment = new JinDuFragment();
        Bundle args = new Bundle();

        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    // Caused by: java.lang.OutOfMemoryError: Failed to allocate a 63701004 byte allocation with 4194208 free bytes and 25MB until OOM

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        /*sharedPreferences = getActivity().getSharedPreferences("progress", Context.MODE_APPEND);
        edit = sharedPreferences.edit();*/

      /*  mMainActivity1 = (MainActivity) getActivity();
        UserInfo mUserInfo = mMainActivity1.mUserInfo;
        uid = mUserInfo.getUid();*/

        uid = MainActivity.getInstance().getUid();
        mRwxqActivity = (RwxqActivity) getActivity();
        LogHelper.i(TAG, "---------MainActivity---uid------" + uid);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);

        mProgressListView = (ListView) view.findViewById(R.id.mProgressListView);
        getDataInit();
        return view;
    }

    private void getDataInit() {
        createProgressDialogTitle("正在加载数据...");
        VolleyHelpApi.getInstance().getBZProcs(((RwxqActivity) getActivity()).mOrderId, new APIListener() {
            @Override
            public void onResult(Object result) {

                LogHelper.i(TAG, "-----ProgressFragment" + result.toString());


                // TODO
                // [{"employeeId":4,"file2":null,"file3":null,"progressStatus":4,"file0":null,"file1":null,"file6":null,"file7":null,
                // "file4":null,"file5":null,"endTime":null,
                // "employeeName":"蔡成安","startTime":"1478231803612","serviceId":106,"flowName":"企业核名","orderId":37,"flowId":1}]

                JSONObject tempJO;
                JSONArray tempJA = (JSONArray) result;
                ProsItem tempPI;
                int lenghtJA = tempJA.length();
                for (int i = 0; i < lenghtJA; i++) {
                    try {
                        tempJO = tempJA.getJSONObject(i);
                        tempPI = new ProsItem();
                        tempPI.setFlowName(tempJO.optString("flowName"));//类型
                        tempPI.setStartTime(tempJO.optString("startTime"));
                        tempPI.setEndTime(tempJO.optString("endTime"));
                        tempPI.setGengjinP(tempJO.optString("employeeName"));
                        tempPI.setProgressStatus(tempJO.optString("progressStatus"));
                        tempPI.setServiceId(tempJO.optString("serviceId"));
                        tempPI.setOrderId(tempJO.optString("orderId"));
                        tempPI.setFlowId(tempJO.optString("flowId"));
                        String employeeId = tempJO.optString("employeeId");
                        //tempPI.setEmployeeId(tempJO.optString("employeeId"));

                        LogHelper.i(TAG, "-----------" + employeeId);
                        LogHelper.i(TAG, "----" + tempJO.optString("employeeId"));
                        tempPI.setImgFile1(tempJO.optString("file0"));
                        tempPI.setImgFile2(tempJO.optString("file1"));
                        tempPI.setImgFile3(tempJO.optString("file2"));
                        tempPI.setImgFile4(tempJO.optString("file3"));
                        tempPI.setImgResFile1(tempJO.optString("file4"));
                        tempPI.setImgResFile2(tempJO.optString("file5"));
                        tempPI.setImgResFile3(tempJO.optString("file6"));
                        tempPI.setImgResFile4(tempJO.optString("file7"));
                        mProsItems.add(tempPI);

                        LogHelper.i(TAG, "------0" + tempPI.getFlowName());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                progressAdapter = new ProgressAdapter(mRwxqActivity, mProsItems);
                mProgressListView.setAdapter(progressAdapter);
                dismissProgressDialog();
            }

            @Override
            public void onError(Object e) {
                App.getInstance().showToast(e.toString());
                dismissProgressDialog();
                //Activity com.xht.android.serverhelp.RwxqActivity has leaked window com.android.internal.policy.impl.PhoneWindow$DecorView{36ebcc7d V.E..... R......D 0,0-1026,486} that was originally added here
                getActivity().finish();
            }
        });
    }


    public class ProgressAdapter extends BaseAdapter implements View.OnClickListener {

        private List<ProsItem> mProsItem = new ArrayList<>();
        public RwxqActivity mContext;
        private ProgressDialog mProgressDialog;

        private AsyncImageLoader imageLoader;


        public ProgressAdapter(RwxqActivity mContext, List<ProsItem> Item) {
            this.mProsItem = Item;
            this.mContext = mContext;
            imageLoader = new AsyncImageLoader(mContext);
        }

        @Override
        public int getCount() {
            if (mProsItem.size() > 0) {
                return mProsItem.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            return mProsItem.get(position);
        }
        @Override
        public long getItemId(int position) {
            return mProsItem.size();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_progress, null);

                holder.mHeMing = (TextView) convertView.findViewById(R.id.mHeMing);
                holder.kssj1 = (TextView) convertView.findViewById(R.id.kssj1);
                holder.mStartTime = (TextView) convertView.findViewById(R.id.mStartTime);
                holder.jssj1 = (TextView) convertView.findViewById(R.id.jssj1);
                holder.mEndTime = (TextView) convertView.findViewById(R.id.mEndTime);
                holder.mUploadfile = (Button) convertView.findViewById(R.id.mUploadfile);
                holder.mResultLoad = (Button) convertView.findViewById(R.id.mResultLoad);
                holder.mOfficeStyle = (TextView) convertView.findViewById(R.id.mOfficeStyle);
                holder.mImgFile11 = (ImageView) convertView.findViewById(R.id.mImgFile_11);
                holder.mImgFile12 = (ImageView) convertView.findViewById(R.id.mImgFile_12);
                holder.mImgFile13 = (ImageView) convertView.findViewById(R.id.mImgFile_13);
                holder.mImgResult11 = (ImageView) convertView.findViewById(R.id.mImgResult11);
                holder.mImgResult12 = (ImageView) convertView.findViewById(R.id.mImgResult12);
                holder.mImgResult13 = (ImageView) convertView.findViewById(R.id.mImgResult13);
                holder.mFollowPeople = (TextView) convertView.findViewById(R.id.mFollowPeople);
                holder.mFollowName = (TextView) convertView.findViewById(R.id.mFollowName);
                holder.mSubmitgx1 = (Button) convertView.findViewById(R.id.mSubmit_gx1);
                holder.mCancelrw1 = (TextView) convertView.findViewById(R.id.mCancel_rw1);
                holder.mContinuerw1 = (TextView) convertView.findViewById(R.id.mContinue_rw1);
                holder.mConverrw1 = (TextView) convertView.findViewById(R.id.mConver_rw1);
                holder.aftersub1 = (LinearLayout) convertView.findViewById(R.id.after_sub1);
                holder.shbz1 = (RelativeLayout) convertView.findViewById(R.id.shbz1);

                convertView.setTag(holder);
                // {"startTime":"1478063503084","comContactName":null,"serviceId":95,"ORDERID":20,"progressStatus":1,
                // "companyName":"地皇","endTime":null,"flowName":"企业核名","flowId":1}]
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String flowId = mProsItem.get(mProsItem.size() - 1).getFlowId();
            LogHelper.i(TAG, "------------------" + flowId + "-==========");

            holder.mImgFile11.setOnClickListener(this);
            holder.mImgFile12.setOnClickListener(this);
            holder.mImgFile13.setOnClickListener(this);

            holder.mImgResult11.setOnClickListener(this);
            holder.mImgResult12.setOnClickListener(this);
            holder.mImgResult13.setOnClickListener(this);

            holder.mSubmitgx1.setOnClickListener(this);
            holder.mResultLoad.setOnClickListener(this);
            holder.mUploadfile.setOnClickListener(this);

            holder.mCancelrw1.setOnClickListener(this);
            holder.mContinuerw1.setOnClickListener(this);
            holder.mConverrw1.setOnClickListener(this);

            ProsItem item = mProsItem.get(position);
            LogHelper.i(TAG, "-------position-----" + position);

   /*  case "1"://审核中
        case "2":// 驳回
        case "3"://终止z  //TODO
        case "4"://审核中
        case "5"://审核通过*/

            String serviceId = item.getServiceId();//服务id
            String name = item.getFlowName();//步骤流程名称 如企业核名
            String startTime = item.getStartTime();//当前步骤开始时间
            LogHelper.i(TAG, "------" + startTime);
            String endTime = item.getEndTime();//当前步骤结束的时间



            if (!startTime.equals("null")) {
                long mStartTime = Long.valueOf(startTime);
                holder.mStartTime.setText(getDateTime(mStartTime));
            }
            LogHelper.i(TAG, "---" + endTime);

            if (!endTime.equals("null")) {
                long mEndTime = Long.parseLong(endTime);
                holder.mEndTime.setText(getDateTime(mEndTime));
            }
            String gengjinP = item.getGengjinP();//跟进任务的人名
            String orderId = item.getOrderId();//订单id

            /**
             * 加载网络图片 TODO
             */

            final String imgFile1 = item.getImgFile1();
            // item.setmImgTag1(imgFile1);
            holder.mImgFile11.setTag(imgFile1);

            final String imgFile2 = item.getImgFile2();
            // item.setmImgTag2(imgFile2);
            holder.mImgFile12.setTag(imgFile2);

            final String imgFile3 = item.getImgFile3();
            //item.setmImgTag3(imgFile3);
            holder.mImgFile13.setTag(imgFile3);

            final String imgResFile1 = item.getImgResFile1();
            // item.setmImgTag4(imgResFile1);
            holder.mImgResult11.setTag(imgResFile1);

            final String imgResFile2 = item.getImgResFile2();
            //item.setmImgTag5(imgResFile2);
            holder.mImgResult12.setTag(imgResFile2);

            final String imgResFile3 = item.getImgResFile3();
            //item.setmImgTag6(imgResFile3);
            holder.mImgResult13.setTag(imgResFile3);

            LogHelper.i(TAG, "-------imgFile1-----" + imgFile1);

            //final String imgUrl = list[position];
            // 给 ImageView 设置一个 tag
            // holder.mImgFile11.setTag(imgFile1);
            // 预设一个图片

            if (!imgFile1.equals("null")){
                Bitmap bitmap = imageLoader.loadImage(holder.mImgFile11, imgFile1);
                holder.mImgFile12.setVisibility(View.VISIBLE);
                if (bitmap != null) {

                    holder.mImgFile11.setImageBitmap(bitmap);
                    holder.mImgFile12.setVisibility(View.VISIBLE);
                    if (!imgFile2.equals("null")){
                        Bitmap bitmap1 = imageLoader.loadImage(holder.mImgFile12, imgFile2);

                        if (bitmap1 != null) {
                            holder.mImgFile12.setImageBitmap(bitmap1);
                            holder.mImgFile13.setVisibility(View.VISIBLE);
                            if (!imgFile3.equals("null")){
                                Bitmap bitmap2 = imageLoader.loadImage(holder.mImgFile13, imgFile3);

                                if (bitmap2 != null) {
                                    holder.mImgFile13.setImageBitmap(bitmap2);
                                }else{
                                    holder.mImgFile13.setImageDrawable(null);
                                }
                            }else{
                                holder.mImgFile13.setImageDrawable(null);
                                //holder.mImgFile13.setImageResource(R.mipmap.ic_action_add);
                            }
                        }else{
                            holder.mImgFile12.setImageDrawable(null);
                            //holder.mImgFile12.setImageResource(R.mipmap.ic_action_add);
                        }
                    }else{
                        holder.mImgFile12.setImageDrawable(null);
                        holder.mImgFile13.setImageDrawable(null);
                        //holder.mImgFile12.setImageResource(R.mipmap.ic_action_add);
                        //holder.mImgFile13.setImageResource(R.mipmap.ic_action_add);
                        // holder.mImgFile13.setVisibility(View.GONE);
                    }
                }else{
                    holder.mImgFile11.setImageDrawable(null);
                    //holder.mImgFile11.setImageResource(R.mipmap.ic_action_add);
                }
            }else{
                holder.mImgFile11.setImageDrawable(null);
                holder.mImgFile12.setImageDrawable(null);
                holder.mImgFile13.setImageDrawable(null);
               // holder.mImgFile11.setImageResource(R.mipmap.ic_action_add);
               // holder.mImgFile12.setImageResource(R.mipmap.ic_action_add);
                //holder.mImgFile13.setImageResource(R.mipmap.ic_action_add);
            }
            //结果
            if (!imgResFile1.equals("null")){
                Bitmap bitmap = imageLoader.loadImage(holder.mImgResult11, imgResFile1);
                if (bitmap != null) {
                    holder.mImgResult11.setImageBitmap(bitmap);
                    holder.mImgResult12.setVisibility(View.VISIBLE);
                    if (!imgResFile2.equals("null")){
                        Bitmap bitmap1 = imageLoader.loadImage(holder.mImgResult12, imgResFile2);
                        if (bitmap1 != null) {
                            holder.mImgResult12.setImageBitmap(bitmap1);
                            holder.mImgResult13.setVisibility(View.VISIBLE);


                            if (!imgResFile3.equals("null")){
                                Bitmap bitmap2 = imageLoader.loadImage(holder.mImgResult13, imgResFile3);
                                if (bitmap2 != null) {
                                    holder.mImgResult13.setImageBitmap(bitmap2);

                                }else{
                                    holder.mImgResult13.setImageDrawable(null);
                                }
                            }else{
                                holder.mImgResult13.setImageDrawable(null);
                                //holder.mImgResult13.setImageResource(R.mipmap.ic_action_add);
                            }

                        }else{
                            holder.mImgResult12.setImageDrawable(null);
                            //holder.mImgResult12.setImageResource(R.mipmap.ic_action_add);

                        }
                    }else{
                        holder.mImgResult12.setImageDrawable(null);
                        holder.mImgResult13.setImageDrawable(null);
                        //holder.mImgResult12.setImageResource(R.mipmap.ic_action_add);
                      //  holder.mImgResult13.setImageResource(R.mipmap.ic_action_add);
                        // holder.mImgResult13.setVisibility(View.GONE);
                    }
                }else{
                    holder.mImgResult11.setImageDrawable(null);
                    //holder.mImgResult11.setImageResource(R.mipmap.ic_action_add);
                }
            }else{
                holder.mImgResult11.setImageDrawable(null);
                holder.mImgResult12.setImageDrawable(null);
                holder.mImgResult13.setImageDrawable(null);
                //holder.mImgResult11.setImageResource(R.mipmap.ic_action_add);

               // holder.mImgResult12.setImageResource(R.mipmap.ic_action_add);
                //holder.mImgResult13.setImageResource(R.mipmap.ic_action_add);
            }


            final String prestatus = mProsItems.get(position).getProgressStatus();

            setTrue();
            switch (prestatus) {
                case "1":

                    holder.mUploadfile.setText("文件上传");
                    holder.mResultLoad.setText("文件上传");

                    holder.mSubmitgx1.setVisibility(View.VISIBLE);
                    holder.aftersub1.setVisibility(View.GONE);
                    holder.mSubmitgx1.setText("审核提交");

                    holder.mUploadfile.setTextColor(Color.BLACK);
                    holder.mResultLoad.setTextColor(Color.BLACK);
                    holder.mSubmitgx1.setTextColor(Color.BLACK);
                    holder.mImgFile11.setClickable(true);
                    holder.mImgFile12.setClickable(true);
                    holder.mImgFile13.setClickable(true);
                    holder.mImgResult11.setClickable(true);
                    holder.mImgResult12.setClickable(true);
                    holder.mImgResult13.setClickable(true);
                    holder.mUploadfile.setClickable(true);
                    holder.mResultLoad.setClickable(true);
                    break;
                case "4":
                    holder.mUploadfile.setText("已上传");
                    holder.mUploadfile.setTextColor(Color.GRAY);
                    holder.mResultLoad.setTextColor(Color.GRAY);
                    holder.mResultLoad.setText("已上传");
                    holder.mSubmitgx1.setVisibility(View.VISIBLE);
                    holder.aftersub1.setVisibility(View.GONE);
                    holder.mSubmitgx1.setText("审核中");

                    holder.mUploadfile.setClickable(false);
                    holder.mResultLoad.setClickable(false);
                    holder.mImgFile11.setClickable(false);
                    holder.mImgFile12.setClickable(false);
                    holder.mImgFile13.setClickable(false);
                    holder.mImgResult11.setClickable(false);
                    holder.mImgResult12.setClickable(false);
                    holder.mImgResult13.setClickable(false);

                    break;
                case "5":

                    holder.mUploadfile.setText("已上传");
                    holder.mResultLoad.setText("已上传");
                    holder.mUploadfile.setTextColor(Color.GRAY);
                    holder.mResultLoad.setTextColor(Color.GRAY);
                    holder.mImgFile11.setClickable(false);
                    holder.mImgFile12.setClickable(false);
                    holder.mImgFile13.setClickable(false);
                    holder.mImgResult11.setClickable(false);
                    holder.mImgResult12.setClickable(false);
                    holder.mImgResult13.setClickable(false);
                    holder.mUploadfile.setClickable(false);
                    holder.mResultLoad.setClickable(false);

                    if (position == mProsItems.size() - 1) {
                       /* holder.mUploadfile.setVisibility(View.VISIBLE);
                        holder.mResultLoad.setVisibility(View.VISIBLE);*/
                        String flowId1 = mProsItems.get(position).getFlowId();
                        if (flowId1.equals("6")) {
                            holder.mSubmitgx1.setVisibility(View.VISIBLE);
                            holder.aftersub1.setVisibility(View.GONE);
                            holder.mSubmitgx1.setText("已完成");
                            holder.mSubmitgx1.setTextColor(Color.GRAY);
                            holder.mSubmitgx1.setClickable(false);
                            break;
                        }
                        holder.mSubmitgx1.setVisibility(View.GONE);
                        holder.aftersub1.setVisibility(View.VISIBLE);
                    } else {
                        holder.mSubmitgx1.setVisibility(View.VISIBLE);
                        holder.aftersub1.setVisibility(View.GONE);
                     /*   holder.mUploadfile.setVisibility(View.INVISIBLE);
                        holder.mResultLoad.setVisibility(View.INVISIBLE);*/
                        holder.mSubmitgx1.setText("已完成");
                        holder.mSubmitgx1.setTextColor(Color.GRAY);
                        holder.mSubmitgx1.setClickable(false);
                    }
                    break;
            }
            holder.mHeMing.setText(name);
            holder.mFollowName.setText(gengjinP);


            return convertView;
        }
        @Override
        public void onClick(View v) {
            String status = mProsItems.get(mProsItems.size() - 1).getProgressStatus();
            switch (v.getId()) {
                case R.id.mUploadfile://文件图片上传
                    MethodLoadFile(status);
                    break;
                case R.id.mResultLoad://结果上传
                    //  flag11 = sharedPreferences.getBoolean("flag11", false);
                    MethodLoadResult(status);
                    break;
                case R.id.mImgFile_11://文件图片
                    if (status.equals("4")||status.equals("5")){
                        App.getInstance().showToast("已经上传过照片，不可再上传");
                        return;
                    }
                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(getActivity(), this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 11;
                    break;
                case R.id.mImgFile_12:
                    if (status.equals("4")||status.equals("5")){
                        App.getInstance().showToast("已经上传过照片，不可再上传");
                        return;
                    }

                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(getActivity(), this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 12;
                    break;
                case R.id.mImgFile_13:
                    if (status.equals("4")||status.equals("5")){
                        App.getInstance().showToast("已经上传过照片，不可再上传");
                        return;
                    }
                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(getActivity(), this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 13;
                    break;
                case R.id.mImgResult11://结果照片
                    if (status.equals("4")||status.equals("5")){
                        App.getInstance().showToast("已经上传过照片，不可再上传");
                        return;
                    }
                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(getActivity(), this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 14;
                    break;
                case R.id.mImgResult12:
                    if (status.equals("4")||status.equals("5")){
                        App.getInstance().showToast("已经上传过照片，不可再上传");
                        return;
                    }
                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(getActivity(), this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 15;
                    break;
                case R.id.mImgResult13:
                    if (status.equals("4")||status.equals("5")){
                        App.getInstance().showToast("已经上传过照片，不可再上传");
                        return;
                    }
                    if (mChoosePicDialog == null) {
                        mChoosePicDialog = new ChoosePicDialog(getActivity(), this);
                    }
                    mChoosePicDialog.show();
                    curIVSelPic = 16;
                    break;
                case R.id.mSubmit_gx1://提交审核
                    MethodLoadSubmit(status);


                    break;
                case R.id.mContinue_rw1://继续任务
                    LogHelper.i(TAG, "------继续任务");

                    continueTaskMethod();
                    break;
                case R.id.mCancel_rw1://取消任务
                    LogHelper.i(TAG, "------取消任务");

                    cancelTaskMethod();

                    break;
                case R.id.mConver_rw1://转交任务
                    LogHelper.i(TAG, "------转交任务");

                    forwardTaskMethod();
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

        private void MethodLoadSubmit(String status) {
            //String status = mProsItems.get(mProsItems.size() - 1).getProgressStatus();
            if (status.equals("4")) {
                App.getInstance().showToast("请先等审核通过再继续下一步");
                return;
            }
            if (status.equals("5")) {
                App.getInstance().showToast("已经完成整个流程");
                return;
            }
            LogHelper.i(TAG, "------提交审核");
            createProgressDialogTitle("进度更新");
            JSONObject obj = new JSONObject();
            try {
                obj.put("employeeId", uid + "");
                obj.put("orderId", mRwxqActivity.mOrderId);
                LogHelper.i(TAG, "------提交审核-----" + uid + "---" + mProsItems.get(mProsItems.size() - 1).getOrderId());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (uid==0){
                App.getInstance().showToast("服务器繁忙,请稍后再试...");
                dismissProgressDialog();

            }else {
                VolleyHelpApi.getInstance().postRefleshPro(obj
                        , new APIListener() {//"employeeId":4,"serviceId":95
                            @Override
                            public void onResult(Object result) {
                                LogHelper.i(TAG, "------提交审核------" + result.toString());
                                holder.mSubmitgx1.setVisibility(View.VISIBLE);
                                holder.mSubmitgx1.setText("审核中");
                                dismissProgressDialog();
                            }

                            @Override
                            public void onError(Object e) {
                                dismissProgressDialog();
                                App.getInstance().showToast(e.toString());
                                holder.mSubmitgx1.setText("提交审核");
                            }
                        });
                progressAdapter.notifyDataSetChanged();
            }
        }

        class ViewHolder {
            TextView mHeMing;//核名
            TextView kssj1;//
            TextView mStartTime;//开始时间
            TextView jssj1;
            TextView mEndTime;//结束时间
            Button mUploadfile;//上传图片文件
            Button mResultLoad;//上传图片结果
            TextView mOfficeStyle;//类似于工商局的业务类型
            ImageView mImgFile11;//文件图片一
            ImageView mImgFile12;//文件图片二
            ImageView mImgFile13;//文件图片三
            ImageView mImgResult11;//结果图片一
            ImageView mImgResult12;//结果图片二
            ImageView mImgResult13;//结果图片三
            TextView mFollowPeople;//跟进人
            TextView mFollowName;//跟进人姓名
            Button mSubmitgx1;//提交审核
            TextView mCancelrw1;//取消任务
            TextView mContinuerw1;//继续任务
            TextView mConverrw1;//转交任务
            LinearLayout aftersub1;//提交审核之后显示LinearLayout
            RelativeLayout shbz1;
        }

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

        ProsItem item = mProsItems.get(mProsItems.size() - 1);//TODO
        LogHelper.i(TAG, "-----ProsItem-相机拍照--" + item.getOrderId() + "--" + item.getServiceId());
        switch (curIVSelPic) {
            case 11:
            case 12:
            case 13:
                mCurFromCamare = Uri.parse("file://" + Constants.BZ_PIC_PATH + "/"
                        + "bzzbz_" + "o" + ((RwxqActivity) getActivity()).mOrderId + "_s" + item.getServiceId()//+ item.getOrderId()
                        + "_e" + uid + "_f" + item.getFlowId() + "_t" + System.currentTimeMillis() + "_g" + ".jpg");
                break;
            case 14:
            case 15:
            case 16:
                mCurFromCamare = Uri.parse("file://" + Constants.BZ_PIC_PATH + "/"
                        + "bzzbz_" + "o" + ((RwxqActivity) getActivity()).mOrderId + "_s" + item.getServiceId()//+ item.getOrderId()
                        + "_e" + uid + "_f" + item.getFlowId() + "_t" + System.currentTimeMillis() + "_j" + ".jpg");
                break;
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
                LogHelper.i(TAG, "------相机拍照--1--" + mCurFromCamare1);
                //file:///storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f11478239384309.jpg
                break;
            case 15:
                mCurFromCamare5 = mCurFromCamare;
                LogHelper.i(TAG, "------相机拍照--2--" + mCurFromCamare2);//TODO mCurFromCamare2 File
                //file:///storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f11478239414869.jpg
                break;
            case 16:
                mCurFromCamare6 = mCurFromCamare;
                LogHelper.i(TAG, "------相机拍照--3--" + mCurFromCamare3);
                // file:///storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f11478239438180.jpg
                break;
        }
        getPhotoFromCamera(mCurFromCamare, 2);
    }

    private void MethodLoadResult(String status) {
        if (status.equals("4")||status.equals("5")){
            App.getInstance().showToast("已经上传过照片");
            return;
        }
        resultPaths = new ArrayList<>();

        if (TextUtils.isEmpty(mTempStrUR4) && TextUtils.isEmpty(mTempStrUR5) && TextUtils.isEmpty(mTempStrUR6)) {
            App.getInstance().showToast("先选择照片");
            return;
        }
        createProgressDialogTitle("正在上传");
        LogHelper.i(TAG, "-----mTempStrUR1-----" + mTempStrUR4);
        if (!TextUtils.isEmpty(mTempStrUR4)) {
            resultPaths.add(mTempStrUR4);
            new UploadPicTask().execute(mTempStrUR4);
        }
        LogHelper.i(TAG, "-----mTempStrUR1-----" + mTempStrUR5);
        if (!TextUtils.isEmpty(mTempStrUR5)) {
            resultPaths.add(mTempStrUR5);
            new UploadPicTask().execute(mTempStrUR5);
        }
        LogHelper.i(TAG, "-----mTempStrUR1-----" + mTempStrUR6);
        if (!TextUtils.isEmpty(mTempStrUR6)) {
            resultPaths.add(mTempStrUR6);
            new UploadPicTask().execute(mTempStrUR6);
        }

    }

    private void MethodLoadFile(String status) {
        if (status.equals("4")||status.equals("5")){
            App.getInstance().showToast("已经上传过照片");
            return;
        }
        // flag1 = sharedPreferences.getBoolean("flag1", false);
        if (flag1) {
            App.getInstance().showToast("已经上传过照片");
            return;
        }
        LogHelper.i(TAG, "------文件图片上传");
        LogHelper.i(TAG, "------要上传照片");
        filePaths = new ArrayList<>();
        if (TextUtils.isEmpty(mTempStrUR1) && TextUtils.isEmpty(mTempStrUR2) && TextUtils.isEmpty(mTempStrUR3)) {
            App.getInstance().showToast("先选择照片");
            return;
        }
        createProgressDialogTitle("正在上传");
        LogHelper.i(TAG, "-----mTempStrUR1-----" + mTempStrUR1);
        if (!TextUtils.isEmpty(mTempStrUR1)) {
            filePaths.add(mTempStrUR1);
            new UploadPicTask().execute(mTempStrUR1);
        }
        LogHelper.i(TAG, "-----mTempStrUR1-----" + mTempStrUR2);
        if (!TextUtils.isEmpty(mTempStrUR2)) {
            filePaths.add(mTempStrUR2);
            new UploadPicTask().execute(mTempStrUR2);
        }
        LogHelper.i(TAG, "-----mTempStrUR1-----" + mTempStrUR3);
        if (!TextUtils.isEmpty(mTempStrUR3)) {
            filePaths.add(mTempStrUR3);
            new UploadPicTask().execute(mTempStrUR3);
        }

    }

    private void setTrue() {

        holder.mImgFile11.setClickable(true);
        holder.mImgFile12.setClickable(true);
        holder.mImgFile13.setClickable(true);
        holder.mImgResult11.setClickable(true);
        holder.mImgResult12.setClickable(true);
        holder.mImgResult13.setClickable(true);

        holder.mSubmitgx1.setClickable(true);
        holder.mUploadfile.setClickable(true);
        holder.mResultLoad.setClickable(true);
    }


    // TODO 取消任务
    private void cancelTaskMethod() {
        ProsItem prosItem = mProsItems.get(mProsItems.size() - 1);
        String employeeId = prosItem.getEmployeeId();
        String orderId = prosItem.getOrderId();

        //{"employeeId":4,"orderId":95}

        LogHelper.i(TAG, "-------------------------");
        JSONObject object = new JSONObject();
        try {
            object.put("employeeId", uid + "");
            object.put("orderId", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogHelper.i(TAG, "-------------------------" + object.toString());
        if (uid==0){
            App.getInstance().showToast("服务器繁忙，请稍后再试....");

        }else {
            VolleyHelpApi.getInstance().postCancelTask(object, new APIListener() {
                @Override
                public void onResult(Object result) {//{"message":"系统错误","result":"error","entity":null,"code":"0"}

                    JSONObject jsonObj = (JSONObject) result;
                    String code = jsonObj.optString("code");
                    if (code.equals("1")) {
                        App.getInstance().showToast("取消任务成功");
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        dismissProgressDialog();
                    }
                    getActivity().finish();
                }

                @Override
                public void onError(Object e) {
                    App.getInstance().showToast(e.toString());
                }
            });
        }

    }

    // TODO 转交任务
    private void forwardTaskMethod() {
        ProsItem prosItem = mProsItems.get(mProsItems.size() - 1);

        LogHelper.i(TAG, "-------------------------");
        createProgressDialogTitle("正在转交...");
        VolleyHelpApi.getInstance().getForwardTask(new APIListener() {
            @Override
            public void onResult(Object result) {
            /*  Bundle bundle=new Bundle();

              bundle.putInt("uid",uid);
              bundle.putInt("orderId",mRwxqActivity.mOrderId);
              bundle.putString("result",result.toString());

              IntentUtils.startActivityNumber(getActivity(),bundle,ForwardTask.class);
*/
                mListTask = new ArrayList<TaskPeople>();
                JSONArray obj = null;
                try {
                    obj = new JSONArray(result.toString());

                    int len = obj.length();
                    LogHelper.i(TAG, "-----len--" + len);
                    for (int i = 0; i < len; i++) {
                        TaskPeople ItemList = new TaskPeople();
                        JSONObject item = (JSONObject) obj.get(i);
                        LogHelper.i(TAG, "-----obj--i--" + obj.get(i));
                        String telPhone = item.optString("eTel");
                        String telName = item.optString("eName");
                        String telId = item.optString("eId");

                        LogHelper.i(TAG, "-------i--" + telPhone + "--" + telName + "--" + telId);
                        ItemList.seteTel(telPhone);
                        ItemList.seteName(telName);
                        ItemList.seteId(telId);

                        mListTask.add(ItemList);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                DialogDanyi(mListTask);
                dismissProgressDialog();
            }

            @Override
            public void onError(Object e) {

            }
        });

    }

    //2、点击按钮弹出单选对话框

    private void DialogDanyi(final List<TaskPeople> mTask) {

        //通过builder构造器gouzao
        final JSONObject json = new JSONObject();

        AlertDialog.Builder builder = new AlertDialog.Builder(mRwxqActivity);

        builder.setTitle("选择你要转交任务的人员");
        int size = mTask.size();
        final String items[] = new String[size];
        for (int i = 0; i < size; i++) {
            String name = mTask.get(i).geteName();
            items[i] = name;
        }

        // final String items[]={"香蕉","西瓜","黄瓜","哈密瓜","苹果"};

        //-1代表默认没有条目被选中

        builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which) {

                //把选择到的条目取出来

                String item = items[which];
                TaskPeople taskPeople = mTask.get(which);
                LogHelper.i(TAG, "----" + taskPeople.geteId() + taskPeople.geteName());
                try {
                    json.put("fromEId", uid + "");
                    json.put("orderId", mRwxqActivity.mOrderId);
                    json.put("toEId", taskPeople.geteId());


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LogHelper.i(TAG, "------json---" + json.toString());
                App.getInstance().showToast(taskPeople.geteId() + taskPeople.geteName());

                //关闭对话框
                //dialog.dismiss();

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                App.getInstance().showToast("item" + "which" + which);
                LogHelper.i(TAG, "------确定--json---" + json.toString());
                ForwardTaskPeople(json);//{"fromEId":4,"orderId":95,"toEId":95}
                dismissProgressDialog();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        //显示对话框
        builder.show();
    }

    //转交任务保存成功
    private void ForwardTaskPeople(JSONObject json) {
        createProgressDialogTitle("正在转交任务");
        if (uid==0){
            App.getInstance().showToast("服务器繁忙，请稍后再试....");
            dismissProgressDialog();
        }else {


            VolleyHelpApi.getInstance().postSaveForward(json, new APIListener() {
                @Override
                public void onResult(Object result) {

                    JSONObject jsonObj = (JSONObject) result;
                    String code = jsonObj.optString("code");
                    if (code.equals("1")) {
                        App.getInstance().showToast("任务转交成功");
                        startActivity(new Intent(getActivity(), MainActivity.class));
                        dismissProgressDialog();
                    }
                    getActivity().finish();
                }

                @Override
                public void onError(Object e) {
                    App.getInstance().showToast(e.toString());
                    getActivity().finish();
                }
            });
        }

    }


    // TODO 继续任务
    private void continueTaskMethod() {

        ProsItem prosItem = mProsItems.get(mProsItems.size() - 1);
        String employeeId = prosItem.getEmployeeId();
        String orderId = prosItem.getOrderId();
        LogHelper.i(TAG, "--------uid" + uid + "--" + employeeId + "--" + orderId);
        JSONObject jsonObject = new JSONObject();
        //"employeeId":4,"orderId":95
        try {

            jsonObject.put("employeeId", uid);

            if (!TextUtils.isEmpty(orderId)) {
                jsonObject.put("orderId", orderId);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogHelper.i(TAG, "------" + jsonObject.toString());

        createProgressDialogTitle("正在请求继续任务。。。");
        if (uid==0){
            App.getInstance().showToast("服务器繁忙，请稍后再试....");
            dismissProgressDialog();
        }else {
            VolleyHelpApi.getInstance().postContinusTask(jsonObject, new APIListener() {
                @Override
                public void onResult(Object result) {
                    /**
                     * [{"employeeId":4,
                     * "progressStatus":1,
                     * "endTime":null,"employeeName":"蔡成安","startTime":"1478860056038","serviceId":139,"flowName":"企业核名","orderId":48,"flowId":1}]
                     */
                    LogHelper.i(TAG, "------result----" + result.toString());

                    JSONObject jsonObject = ((JSONObject) result).optJSONObject("entity");
                    String success = ((JSONObject) result).optString("result");
                    if (success.equals("success")) {
                        App.getInstance().showToast("继续成功，请进入完成任务");
                        dismissProgressDialog();
                        mRwxqActivity.finish();
                    }


               /* ProsItem item=new ProsItem();
                String employeeId = jsonObject.optString("employeeId");

                String endTime = jsonObject.optString("endTime");
                String employeeName = jsonObject.optString("employeeName");
                LogHelper.i(TAG,"-----"+employeeName);
                String progressStatus = jsonObject.optString("progressStatus");
                String startTime = jsonObject.optString("startTime");
                String serviceId = jsonObject.optString("serviceId");
                String flowName = jsonObject.optString("flowName");
                String orderId = jsonObject.optString("orderId");
                String flowId = jsonObject.optString("flowId");
                LogHelper.i(TAG,"-----"+employeeName+"---"+endTime+"---"+employeeId+"---"+progressStatus+"---"+startTime+"---"+flowName+"---"+orderId);
                item.setEmployeeId(employeeId);
                item.setGengjinP(employeeName);
                item.setFlowName(flowName);
                item.setFlowId(flowId);
                item.setProgressStatus(progressStatus);
                item.setServiceId(serviceId);
                item.setOrderId(orderId);



                item.setStartTime(startTime);
                item.setEndTime(endTime);

                mProsItems.add(item);

                //继续任务更新
                continusAdd();*/


                }

                @Override
                public void onError(Object e) {
                    App.getInstance().showToast(e.toString());
                }
            });
        }

    }

    private String replaceUrl(String fileUrl) {
        return fileUrl.replace("\\/", "/");

    }//http://baidu.com/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            if (requestCode == 0) {//相册返回

                Bitmap thumbnail ;
                Uri fullPhotoUri = data.getData();//content://media/external/images/media/3844

                //真实路径
                String realFilePath = BitmapUtils.getRealFilePath(getActivity(), fullPhotoUri);
                ProsItem item = mProsItems.get(mProsItems.size() - 1);

                switch (curIVSelPic) {
                    case 11:
                    case 12:
                    case 13:
                        //("/sdcard/image.jpg")
                        mCurFromCamare = Uri.parse("file://" + Constants.BZ_CAM_PATH + "/"
                                + "bzzbz_" + "o" + ((RwxqActivity) getActivity()).mOrderId + "_s" + item.getServiceId()//+ item.getOrderId()
                                + "_e" + uid + "_f" + item.getFlowId() + "_t" + System.currentTimeMillis() + "_g" + ".jpg");
                        break;
                    case 14:
                    case 15:
                    case 16:
                        mCurFromCamare = Uri.parse("file://" + Constants.BZ_PIC_PATH + "/"
                                + "bzzbz_" + "o" + ((RwxqActivity) getActivity()).mOrderId + "_s" + item.getServiceId()//+ item.getOrderId()
                                + "_e" + uid + "_f" + item.getFlowId() + "_t" + System.currentTimeMillis() + "_j" + ".jpg");
                        break;
                }
                String path = mCurFromCamare.getPath();


                //压缩新的路径
                String ImageUrl = BitmapUtils.compressImage(realFilePath, path, 90);

                LogHelper.i(TAG,"-------fullPhotoUri---"+fullPhotoUri.toString());
                LogHelper.i(TAG,"-------realFilePath---"+realFilePath);
                String size = BitmapUtils.getAutoFileOrFilesSize(ImageUrl);
                LogHelper.i(TAG,"---------大小---"+size);

                LogHelper.i(TAG,"-------ImageUrl---"+ImageUrl);
                LogHelper.i(TAG, "--------fullPhotoUri----" + fullPhotoUri.toString() + "------>>>>>" + realFilePath + "------>>>>>" + ImageUrl);
                //-fullPhotoUri----content://media/external/images/media/3844------>>>>>/storage/emulated/0/Pictures/Screenshots/S61101-153152.jpg
                // ------>>>>>/storage/emulated/0/ServerHelp/bzbz/camera/bzzbz_o48_s139_e4_f1_t1479002629947_g.jpg
                switch (curIVSelPic) {
                    case 11:
                        //mTempBitmap = BitmapFactory.decodeFile(mTempStrURL);


                        thumbnail = BitmapHelper.getThumbnail(getActivity(), fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);

                        //holder.mImgFile11.setImageBitmap(thumbnail);
                        holder.mImgFile11.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        holder.mImgFile12.setVisibility(View.VISIBLE);
                        mTempStrUR1 = ImageUrl;

                        progressAdapter.notifyDataSetChanged();

                        break;
                    case 12:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(getActivity(), fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        //holder.mImgFile12.setImageBitmap(thumbnail);
                       holder.mImgFile12.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        holder.mImgFile13.setVisibility(View.VISIBLE);
                        mTempStrUR2 = ImageUrl;

                        progressAdapter.notifyDataSetChanged();
                        break;
                    case 13:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(getActivity(), fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                       // holder.mImgFile13.setImageBitmap(thumbnail);
                        holder.mImgFile13.setBackgroundDrawable(new BitmapDrawable(thumbnail));

                        mTempStrUR3 = ImageUrl;

                        progressAdapter.notifyDataSetChanged();
                        break;
                    case 14:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(getActivity(), fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);

                        //holder.mImgResult11.setImageBitmap(thumbnail);
                        holder.mImgResult11.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        holder.mImgResult12.setVisibility(View.VISIBLE);
                        mTempStrUR4 = ImageUrl;
                        progressAdapter.notifyDataSetChanged();
                        break;
                    case 15:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(getActivity(), fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);

                        //holder.mImgResult12.setImageBitmap(thumbnail);
                        holder.mImgResult12.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        holder.mImgResult13.setVisibility(View.VISIBLE);
                        mTempStrUR5 = ImageUrl;
                        progressAdapter.notifyDataSetChanged();
                        break;
                    case 16:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(getActivity(), fullPhotoUri,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);

                        //holder.mImgResult13.setImageBitmap(thumbnail);
                        holder.mImgResult13.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mTempStrUR6 = ImageUrl;
                        progressAdapter.notifyDataSetChanged();
                        break;
                }
                mShiFouJiangSCFlag = true;
            }
            if (requestCode == 2) {      //照相
                Bitmap thumbnail ;
                mTempStrURL = mCurFromCamare.getPath();

                LogHelper.i(TAG,"-----照相-----------mTempStrURL-"+mTempStrURL+"--");//
                String autoFileOrFilesSize = BitmapUtils.getAutoFileOrFilesSize(mTempStrURL);

                LogHelper.i(TAG,"-----daxiao--"+autoFileOrFilesSize);//       ---2.38 M

                //压缩文件 KB
                String imageUrl = BitmapUtils.compressImage(mTempStrURL, mTempStrURL, 90);

                String FilesSize = BitmapUtils.getAutoFileOrFilesSize(imageUrl);
                String FilesSize1 = BitmapUtils.getAutoFileOrFilesSize(mTempStrURL);
                LogHelper.i(TAG,"-----daxiao--"+FilesSize+"--"+FilesSize1);//
                LogHelper.i(TAG,"-----照相--mTempStrURL"+"--"+mTempStrURL);//

                LogHelper.i(TAG, "--------照相返回------" + mTempStrURL + "-----");//string   /storage/emulated/0/ServerHelp/bzbz/img/bzzbz_o29_s114_e4_f1_t1478759682811.jpg
                switch (curIVSelPic) {
                    case 11:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);

                        thumbnail = BitmapHelper.getThumbnail(getActivity(), mCurFromCamare,
                               60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);


                       long bitmapsize = BitmapHelper.getBitmapsize(thumbnail);
                        int byteCount = thumbnail.getByteCount();
                        LogHelper.i(TAG, "-----大小-thumbnail-" + byteCount+"----"+bitmapsize);

                        mTempStrUR1 = mTempStrURL;
                        holder.mImgFile11.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        LogHelper.i(TAG, "--------照相--1-返回-----mTempStrUR1-" + mTempStrUR1 + "-----" + mCurFromCamare.toString());
                        holder.mImgFile12.setVisibility(View.VISIBLE);

                        progressAdapter.notifyDataSetChanged();
                        break;
                    case 12:
                        LogHelper.i(TAG, "-------------" + curIVSelPic + "-----" + mTempStrURL);
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(getActivity(), mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        mTempStrUR2 = mTempStrURL;

                        LogHelper.i(TAG, "--------照相--2--返回-----mTempStrUR2-" + mTempStrUR2 + "-----" + mCurFromCamare.toString());
                        holder.mImgFile12.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        holder.mImgFile13.setVisibility(View.VISIBLE);

                        progressAdapter.notifyDataSetChanged();
                        break;
                    case 13:
                        LogHelper.i(TAG, "-------------" + curIVSelPic + "-----");
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(getActivity(), mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        mTempStrUR3 = mTempStrURL;

                        LogHelper.i(TAG, "--------照相--3--返回-----mTempStrUR3-" + mTempStrUR3 + "-----" + mCurFromCamare.toString());
                        holder.mImgFile13.setBackgroundDrawable(new BitmapDrawable(thumbnail));

                        progressAdapter.notifyDataSetChanged();
                        break;

                    case 14:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(getActivity(), mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);

                        mTempStrUR4 = mTempStrURL;
                        LogHelper.i(TAG, "--------照相--4-返回-----mTempStrUR4-" + mTempStrUR4 + "-----" + mCurFromCamare.toString());
                        holder.mImgResult11.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        holder.mImgResult12.setVisibility(View.VISIBLE);

                        progressAdapter.notifyDataSetChanged();
                        break;
                    case 15:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(getActivity(), mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        holder.mImgResult12.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mTempStrUR5 = mTempStrURL;
                        LogHelper.i(TAG, "--------照相--5-返回-----mTempStrUR5-" + mTempStrUR5 + "-----" + mCurFromCamare.toString());
                        holder.mImgResult13.setVisibility(View.VISIBLE);

                        progressAdapter.notifyDataSetChanged();
                        break;
                    case 16:
                        //mTempBitmap = BitmapFactory.decodeFile(imgPath);
                        thumbnail = BitmapHelper.getThumbnail(getActivity(), mCurFromCamare,
                                60 * (int) Constants.DENSITY, 60 * (int) Constants.DENSITY);
                        holder.mImgResult13.setBackgroundDrawable(new BitmapDrawable(thumbnail));
                        mTempStrUR6 = mTempStrURL;

                        LogHelper.i(TAG, "--------照相--5-返回-----mTempStrUR5-" + mTempStrUR5 + "-----" + mCurFromCamare.toString());
                        progressAdapter.notifyDataSetChanged();
                        break;

                }
                mShiFouJiangSCFlag = true;
            }
        }
    }



    /**
     * 创建mProgressDialog
     */
    private void createProgressDialogTitle(String title) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
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
        new AlertDialog.Builder(getActivity()).setTitle("Message").setMessage(mess)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

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
        @Override
        protected Boolean doInBackground(String... params) {
            //   boolean temp = uploadPicFile(mTempStrURL);
            String url = params[0];
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
                App.getInstance().showToast("上传成功");
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

        startActivityForResult(intent, 0);
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

}



