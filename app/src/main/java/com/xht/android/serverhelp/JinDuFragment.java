package com.xht.android.serverhelp;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xht.android.serverhelp.model.ProsItem;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.AsyncImageLoader;
import com.xht.android.serverhelp.util.IntentUtils;
import com.xht.android.serverhelp.util.LogHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_APPEND;

/**
 * UJinDuFragment
 * <p>
 * 办证中列表项的详细
 */
public class JinDuFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String BRO_ACT= "com.xht.android.serverhelp.udate";
    private String mParam1;
    private String mParam2;


    private SharedPreferences sharedPreferences;
    ProgressAdapter.ViewHolder holder;
    private ProgressDialog mProgressDialog;
    private ChoosePicDialog mChoosePicDialog;
    private static final String TAG = "JinDuFragment";

    //提交更新进度后，出现的对任务进行的三个操作的Layout
    private LinearLayout mRwZzSel1, mRwZzSel2;

    //由于相册返回的照片路径不是想要的路径，所以临时又声明一个路径
    private String mTempStrT;

    private ArrayList<ProsItem> mProsItems = new ArrayList<>();

    boolean flag1 = false;//标记第一步完成
    boolean flag11 = false;//标记第一步完成
    private MainActivity mMainActivity;
    RwxqActivity mRwxqActivity;

    private ListView mProgressListView;
    private SharedPreferences mSharedPreferences;
    private ProgressAdapter progressAdapter;
    private int uid;
    private List<TaskPeople> mListTask;
    private String orderId;
    private int loadNum=0;
    private int curItem=1;


    public JinDuFragment() {
        // Required empty public constructor
    }

    private BroadcastReceiver mudateReceiver = new BroadcastReceiver() {


        @Override
        public void onReceive(Context context, Intent intent) {

        LogHelper.i(TAG,"----------刷新------");


             uid = intent.getIntExtra("mUid",0);
            orderId = intent.getStringExtra("ordId");
            ((RwxqActivity) getActivity()).mOrderId=Integer.parseInt(orderId);
            LogHelper.i(TAG,"------------"+uid+"---"+((RwxqActivity) getActivity()).mOrderId);

            getDataInit();

            int position = mSharedPreferences.getInt("position", 0);
            LogHelper.i(TAG,"-----------====----"+position);
            LogHelper.i(TAG,"----------刷新------"+position);

        }
    };


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

        mSharedPreferences=getActivity().getSharedPreferences("tou",MODE_APPEND);
        IntentFilter intentFilter2 = new IntentFilter(BRO_ACT);
        getActivity().registerReceiver(mudateReceiver, intentFilter2);

        uid = MainActivity.getInstance().getUid();
        mRwxqActivity = (RwxqActivity) getActivity();
        LogHelper.i(TAG, "---------MainActivity---uid------" + uid);



    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogHelper.i(TAG, "---------onAttach---uid------" + uid);


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mudateReceiver);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_progress, container, false);
        mProgressListView = (ListView) view.findViewById(R.id.mProgressListView);


        mProgressListView.setOnScrollListener(new AbsListView.OnScrollListener(){
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //滚动时一直回调，直到停止滚动时才停止回调。单击时回调一次。
                //firstVisibleItem：当前能看见的第一个列表项ID（从0开始）
                //visibleItemCount：当前能看见的列表项个数（小半个也算）
                //totalItemCount：列表项共数
                curItem = firstVisibleItem;

                SharedPreferences.Editor edit = mSharedPreferences.edit();
                edit.putInt("position",firstVisibleItem);
                edit.commit();
                LogHelper.i(TAG,"---------33--===="+firstVisibleItem);
            }
            @Override
            public void onScrollStateChanged(AbsListView view , int scrollState){
                //正在滚动时回调，回调2-3次，手指没抛则回调2次。scrollState = 2的这次不回调
                //回调顺序如下
                //第1次：scrollState = SCROLL_STATE_TOUCH_SCROLL(1) 正在滚动
                //第2次：scrollState = SCROLL_STATE_FLING(2) 手指做了抛的动作（手指离开屏幕前，用力滑了一下）
                //第3次：scrollState = SCROLL_STATE_IDLE(0) 停止滚动
            }
        });


        mProgressListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // 于对选中的项进行处理
                //将上一次滚动时的第一条信息，重新展示为第一条信息，即：实现点击后点击条目的位置不变；
                mProgressListView.setSelection(curItem);
            }
        });
        getDataInit();
        return view;
    }

    private void getDataInit() {
        mProsItems.clear();
        createProgressDialogTitle("正在加载数据...");
        if (((RwxqActivity) getActivity()).mOrderId==0){
            App.getInstance().showToast("服务器繁忙，请稍后再试...");
            dismissProgressDialog();
            return;
        }
        VolleyHelpApi.getInstance().getBZProcs(((RwxqActivity) getActivity()).mOrderId, new APIListener() {
            @Override
            public void onResult(Object result) {

                int len = result.toString().length();

                LogHelper.i(TAG, "-----Fragment----" + result.toString());


                // TODO
                // [{"employeeId":4,"file2":null,"file3":null,"progressStatus":4,"file0":null,"file1":null,"file6":null,"file7":null,
                // "file4":null,"file5":null,"endTime":null,
                // "employeeName":"蔡成安","startTime":"1478231803612","serviceId":106,"flowName":"企业核名","orderId":37,"flowId":1}]

                JSONObject tempJO;
                JSONArray tempJA = (JSONArray) result;
                ProsItem tempPI;
                int lenghtJA = tempJA.length();
                LogHelper.i(TAG,"-----lenghtJA-------"+lenghtJA);
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
                        tempPI.setImgFile5(tempJO.optString("file4"));
                        tempPI.setImgFile6(tempJO.optString("file5"));
                        tempPI.setImgFile7(tempJO.optString("file6"));
                        tempPI.setImgFile8(tempJO.optString("file7"));

                        tempPI.setImgResFile1(tempJO.optString("file8"));
                        tempPI.setImgResFile2(tempJO.optString("file9"));
                        tempPI.setImgResFile3(tempJO.optString("file10"));
                        tempPI.setImgResFile4(tempJO.optString("file11"));
                        tempPI.setImgResFile5(tempJO.optString("file12"));
                        tempPI.setImgResFile6(tempJO.optString("file13"));
                        tempPI.setImgResFile7(tempJO.optString("file14"));
                        tempPI.setImgResFile8(tempJO.optString("file15"));

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


    public class ProgressAdapter extends BaseAdapter  {

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
                holder.mUploadfile = (TextView) convertView.findViewById(R.id.mUploadfile);
                holder.mResultLoad = (TextView) convertView.findViewById(R.id.mResultLoad);
                holder.mOfficeStyle = (TextView) convertView.findViewById(R.id.mOfficeStyle);

                holder.mFile = (Button) convertView.findViewById(R.id.mImgFile);
                holder.mResult = (Button) convertView.findViewById(R.id.mImgResult);
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
            ProsItem item = mProsItem.get(position);
            final String status =item.getProgressStatus();
            final String flowId = item.getFlowId();
            LogHelper.i(TAG, "------------------" + flowId + "-==========");

            final String imgFile1 = item.getImgFile1();
            final String imgFile2 = item.getImgFile2();
            final String imgFile3 = item.getImgFile3();
            final String imgFile4 = item.getImgFile4();
            final String imgFile5 = item.getImgFile5();
            final String imgFile6 = item.getImgFile6();
            final String imgFile7 = item.getImgFile7();
            final String imgFile8 = item.getImgFile8();

            final String imgResFile1 = item.getImgResFile1();
            final String imgResFile2 = item.getImgResFile2();
            final String imgResFile3 = item.getImgResFile3();
            final String imgResFile4 = item.getImgResFile4();
            final String imgResFile5 = item.getImgResFile5();
            final String imgResFile6 = item.getImgResFile6();
            final String imgResFile7 = item.getImgResFile7();
            final String imgResFile8 = item.getImgResFile8();



            final String orderId = item.getOrderId();//订单id
            final String serviceId = item.getServiceId();//服务id
            holder.mSubmitgx1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MethodLoadSubmit(status);
                }
            });

            holder.mCancelrw1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cancelTaskMethod();
                }
            });
            holder.mContinuerw1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    continueTaskMethod();
                }
            });
            holder.mConverrw1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    forwardTaskMethod();
                }

            });
            holder.mFile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转上传过程图片
                    Bundle bundle=new Bundle();
                    bundle.putInt("uid",uid);
                    bundle.putString("status",status);
                    bundle.putString("style","process");
                    bundle.putString("flowId",flowId);
                    bundle.putString("orderId",orderId);
                    bundle.putString("serviceId",serviceId);
                    bundle.putString("imgFile1",imgFile1);
                    bundle.putString("imgFile2",imgFile2);
                    bundle.putString("imgFile3",imgFile3);
                    bundle.putString("imgFile4",imgFile4);
                    bundle.putString("imgFile5",imgFile5);
                    bundle.putString("imgFile6",imgFile6);
                    bundle.putString("imgFile7",imgFile7);
                    bundle.putString("imgFile8",imgFile8);
                    IntentUtils.startActivityNumberForResult(40,bundle,getActivity(),PicLoadActivity.class);

                }
            });
            holder.mResult.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //跳转上传结果图片
                    Bundle bundle=new Bundle();
                    bundle.putInt("uid",uid);
                    bundle.putString("status",status);
                    bundle.putString("style","result");
                    bundle.putString("flowId",flowId);
                    bundle.putString("orderId",orderId);
                    bundle.putString("serviceId",serviceId);

                    bundle.putString("imgFile1",imgResFile1);
                    bundle.putString("imgFile2",imgResFile2);
                    bundle.putString("imgFile3",imgResFile3);
                    bundle.putString("imgFile4",imgResFile4);
                    bundle.putString("imgFile5",imgResFile5);
                    bundle.putString("imgFile6",imgResFile6);
                    bundle.putString("imgFile7",imgResFile7);
                    bundle.putString("imgFile8",imgResFile8);

                    Intent intent=new Intent(getActivity(),PicLoadActivity.class);
                    intent.putExtra("bundle",bundle);
                    startActivityForResult(intent,45);
                }
            });

            LogHelper.i(TAG, "-------position-----" + position);

   /*  case "1"://审核中
        case "2":// 驳回
        case "3"://终止z  //TODO
        case "4"://审核中
        case "5"://审核通过*/

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
            LogHelper.i(TAG, "-------imgFile1-----" );
            //final String imgUrl = list[position];
            // 给 ImageView 设置一个 tag
            // holder.mImgFile11.setTag(imgFile1);
            // 预设一个图片
            setTrue();
            switch (status) {
                case "1":
                    holder.mUploadfile.setText("过程文件");
                    holder.mResultLoad.setText("结果文件");
                    holder.mSubmitgx1.setVisibility(View.VISIBLE);
                    holder.aftersub1.setVisibility(View.GONE);
                    holder.mSubmitgx1.setText("审核提交");
                    holder.mUploadfile.setTextColor(Color.BLACK);
                    holder.mResultLoad.setTextColor(Color.BLACK);
                    holder.mSubmitgx1.setTextColor(Color.WHITE);
                    holder.mSubmitgx1.setBackgroundResource(R.drawable.btn_background_circle);
                    holder.mFile.setText("上传图片");
                    holder.mResult.setText("上传图片");
                    break;
                case "4":
                    holder.mUploadfile.setText("过程文件");
                    holder.mResultLoad.setText("结果文件");
                    holder.mSubmitgx1.setVisibility(View.VISIBLE);
                    holder.aftersub1.setVisibility(View.GONE);
                    holder.mSubmitgx1.setText("审核中");
                    holder.mFile.setText("查看图片");
                    holder.mSubmitgx1.setBackgroundResource(R.drawable.btn_background_circle);
                    holder.mResult.setText("查看图片");

                    break;
                case "5":
                    holder.mUploadfile.setText("过程文件");
                    holder.mResultLoad.setText("结果文件");
                    holder.mFile.setText("查看图片");
                    holder.mResult.setText("查看图片");
                    if (position == (mProsItems.size() - 1)) {
                       /* holder.mUploadfile.setVisibility(View.VISIBLE);
                        holder.mResultLoad.setVisibility(View.VISIBLE);*/
                        String flowId1 = mProsItems.get(position).getFlowId();
                        if (flowId1.equals("19")) {
                            holder.mSubmitgx1.setVisibility(View.VISIBLE);
                            holder.aftersub1.setVisibility(View.GONE);
                            holder.mSubmitgx1.setText("已完成");
                            holder.mSubmitgx1.setTextColor(Color.WHITE);
                            holder.mSubmitgx1.setBackgroundResource(R.drawable.gray_stroke);
                            holder.mSubmitgx1.setClickable(false);

                        }else{
                            holder.mSubmitgx1.setVisibility(View.GONE);
                            holder.aftersub1.setVisibility(View.VISIBLE);
                        }

                    } else {
                        holder.mSubmitgx1.setVisibility(View.VISIBLE);
                        holder.aftersub1.setVisibility(View.GONE);
                     /*   holder.mUploadfile.setVisibility(View.INVISIBLE);
                        holder.mResultLoad.setVisibility(View.INVISIBLE);*/
                        holder.mSubmitgx1.setText("已完成");
                        holder.mSubmitgx1.setTextColor(Color.WHITE);
                        holder.mSubmitgx1.setBackgroundResource(R.drawable.gray_stroke);
                        holder.mSubmitgx1.setClickable(false);
                    }
                    break;
            }
            holder.mHeMing.setText(name);
            holder.mFollowName.setText(gengjinP);


            return convertView;
        }

        //提交审核
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
                                holder.mSubmitgx1.setBackgroundResource(R.drawable.btn_background_circle);
                                getDataInit();

                                dismissProgressDialog();
                            }

                            @Override
                            public void onError(Object e) {
                                dismissProgressDialog();
                                App.getInstance().showToast(e.toString());
                                holder.mSubmitgx1.setText("提交审核");
                            }
                        });
              //  progressAdapter.notifyDataSetChanged();
            }
        }

        class ViewHolder {
            TextView mHeMing;//核名
            TextView kssj1;//
            TextView mStartTime;//开始时间
            TextView jssj1;
            TextView mEndTime;//结束时间
            TextView mUploadfile;//上传图片文件
            TextView mResultLoad;//上传图片结果
            TextView mOfficeStyle;//类似于工商局的业务类型

            Button mFile;
            Button mResult;

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
    private void setTrue() {

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
       // createProgressDialogTitle("正在转交...");
        VolleyHelpApi.getInstance().getForwardTask(new APIListener() {
            @Override
            public void onResult(Object result) {
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

            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                App.getInstance().showToast("item" + "which" + which);
                LogHelper.i(TAG, "------确定--json---" + json.toString());
                ForwardTaskPeople(json);//{"fromEId":4,"orderId":95,"toEId":95}
                //dismissProgressDialog();
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

                        getDataInit();
                        //mRwxqActivity.finish();
                    }
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
     * 隐藏mProgressDialog
     */
    private void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
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



