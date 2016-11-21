/*
package com.xht.android.serverhelp.model;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.xht.android.serverhelp.App;
import com.xht.android.serverhelp.R;
import com.xht.android.serverhelp.RwxqActivity;
import com.xht.android.serverhelp.net.APIListener;
import com.xht.android.serverhelp.net.VolleyHelpApi;
import com.xht.android.serverhelp.util.LogHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

*/
/**
 * Created by Administrator on 2016-11-3.
 *//*


public class ProgressAdapter extends BaseAdapter implements View.OnClickListener {

    private List<ProsItem> mProsItem=new ArrayList<>();
    public RwxqActivity mContext;
    private  ProgressDialog mProgressDialog;
    ViewHolder holder;


    public ProgressAdapter(RwxqActivity mContext, List<ProsItem> Item){
        this.mProsItem=Item;
        this.mContext=mContext;
    }
    @Override
    public int getCount() {
        if (mProsItem.size()>0){
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
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView==null){
            holder=new ViewHolder();
            convertView=View.inflate(mContext, R.layout.item_progress,null);

            holder. mHeMing = (TextView) convertView.findViewById(R.id.mHeMing);
            holder.kssj1 = (TextView) convertView.findViewById(R.id.kssj1);
            holder. mStartTime = (TextView) convertView.findViewById(R.id.mStartTime);
            holder.jssj1 = (TextView) convertView.findViewById(R.id.jssj1);
            holder.mEndTime = (TextView) convertView.findViewById(R.id.mEndTime);
            holder. mUploadfile = (TextView) convertView.findViewById(R.id.mUploadfile);
            holder. mResultLoad = (TextView) convertView.findViewById(R.id.mResultLoad);
            holder.mOfficeStyle = (TextView) convertView.findViewById(R.id.mOfficeStyle);
            holder. mImgFile11 = (NetworkImageView) convertView.findViewById(R.id.mImgFile_11);
            holder. mImgFile12 = (NetworkImageView) convertView.findViewById(R.id.mImgFile_12);
            holder. mImgFile13 = (NetworkImageView) convertView.findViewById(R.id.mImgFile_13);
            holder. mImgResult11 = (NetworkImageView) convertView.findViewById(R.id.mImgResult11);
            holder. mImgResult12 = (NetworkImageView) convertView.findViewById(R.id.mImgResult12);
            holder. mImgResult13 = (NetworkImageView) convertView.findViewById(R.id.mImgResult13);
            holder.mFollowPeople = (TextView) convertView.findViewById(R.id.mFollowPeople);
            holder. mFollowName = (TextView)convertView. findViewById(R.id.mFollowName);
            holder. mSubmitgx1 = (TextView)convertView. findViewById(R.id.mSubmit_gx1);
            holder. mCancelrw1 = (TextView) convertView.findViewById(R.id.mCancel_rw1);
            holder.mContinuerw1 = (TextView)convertView. findViewById(R.id.mContinue_rw1);
            holder.mConverrw1 = (TextView) convertView.findViewById(R.id.mConver_rw1);
            holder. aftersub1 = (LinearLayout) convertView.findViewById(R.id.after_sub1);
            holder.shbz1 = (RelativeLayout) convertView.findViewById(R.id.shbz1);

            convertView.setTag(holder);
            // {"startTime":"1478063503084","comContactName":null,"serviceId":95,"ORDERID":20,"progressStatus":1,
            // "companyName":"地皇","endTime":null,"flowName":"企业核名","flowId":1}]

        }else{

            holder= (ViewHolder) convertView.getTag();
        }

        ProsItem item =mProsItem.get(position);
        String flowId = item.getFlowId();//流程id

        String serviceId = item.getServiceId();//服务id
        String name = item.getName();//步骤流程名称 如企业核名
        String startTime = item.getStartTime();//当前步骤开始时间
        String endTime = item.getEndTime();//当前步骤结束的时间
        String mStatus=item.getStatus();

        if (!TextUtils.isEmpty(startTime)){
            long mStartTime=Long.valueOf(startTime);
            holder.mStartTime.setText(getDateTime(mStartTime));
        }
        if (TextUtils.isEmpty(endTime)){
            long mEndTime=Long.valueOf(endTime);
            holder.mEndTime.setText(getDateTime(mEndTime));
        }
        String gengjinP = item.getGengjinP();//跟进任务的人名
        String orderId = item.getOrderId();//订单id
        String status = item.getStatus();//审核状态


      */
/*  case "1"://审核中
        case "2":// 驳回
        case "3"://终止
        case "4"://审核中
        case "5"://审核通过*//*


        holder. mHeMing.setText(name);
        holder.mFollowName.setText(gengjinP);

        holder. mSubmitgx1.setOnClickListener(this);
        holder. mCancelrw1.setOnClickListener(this);
        holder.mContinuerw1.setOnClickListener(this);
        holder.mConverrw1.setOnClickListener(this);

        holder. mImgFile11.setOnClickListener(this);
        holder. mImgFile12.setOnClickListener(this);
        holder. mImgFile13.setOnClickListener(this);

        holder. mImgResult11.setOnClickListener(this);
        holder. mImgResult12.setOnClickListener(this);
        holder. mImgResult13.setOnClickListener(this);

        holder. mUploadfile.setOnClickListener(this);//上传文件图片
        holder. mResultLoad.setOnClickListener(this);//上传文件图片

        return convertView;
    }


    */
/**
     * 转换时间
     * @param time
     * @return
     *//*

    public String getDateTime(long time){
        Date date = new Date(time);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date);
    }
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.mUploadfile://文件图片上传
                LogHelper.i(TAG,"------文件图片上传");
                break;
            case R.id.mResultLoad://结果图片上传
                LogHelper.i(TAG,"------结果图片上传");
                break;
            case R.id.mImgFile_11:
                LogHelper.i(TAG,"------文件图片1");
                break;
            case R.id.mImgFile_12:
                LogHelper.i(TAG,"------文件图片2");
                break;
            case R.id.mImgFile_13:
                LogHelper.i(TAG,"------文件图片3");
                break;
            case R.id.mImgResult11:
                LogHelper.i(TAG,"------结果图片1");
                break;
            case  R.id.mImgResult12:
                LogHelper.i(TAG,"------结果图片2");
                break;
            case  R.id.mImgResult13:
                LogHelper.i(TAG,"------结果图片3");
                break;
            case  R.id.mSubmit_gx1://提交审核


               */
/* VolleyHelpApi.getInstance().postRefleshPro(((RwxqActivity) getActivity()).mOrderId,
                        mProsItem.get(0).getServiceId(), new APIListener() {
                            @Override
                            public void onResult(Object result) {
                                holder.mSubmitgx1.setText("审核中");
                                dismissProgressDialog();

                            }

                            @Override
                            public void onError(Object e) {
                                dismissProgressDialog();
                                App.getInstance().showToast(e.toString());
                            }
                        });*//*

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(200000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
                LogHelper.i(TAG,"------提交审核");

                break;
            case  R.id.mContinue_rw1://继续任务
                LogHelper.i(TAG,"------继续任务");

                break;
            case  R.id.mConver_rw1://转交任务
                LogHelper.i(TAG,"------转交任务");

                break;
            case  R.id.goToAlbum://相册选择照片
                LogHelper.i(TAG,"------相册选择照片");
                break;
            case  R.id.goToCamera://相机选择拍照
                LogHelper.i(TAG,"------相机选择拍照");
                break;


        }

    }

    static class ViewHolder{

         TextView mHeMing;//核名
         TextView kssj1;//
         TextView mStartTime;//开始时间
         TextView jssj1;
         TextView mEndTime;//结束时间
         TextView mUploadfile;//上传图片文件
         TextView mResultLoad;//上传图片结果
         TextView mOfficeStyle;//类似于工商局的业务类型
         NetworkImageView mImgFile11;//文件图片一
         NetworkImageView mImgFile12;//文件图片二
         NetworkImageView mImgFile13;//文件图片三
         NetworkImageView mImgResult11;//结果图片一
         NetworkImageView mImgResult12;//结果图片二
         NetworkImageView mImgResult13;//结果图片三
         TextView mFollowPeople;//跟进人
         TextView mFollowName;//跟进人姓名
         TextView mSubmitgx1;//提交审核
         TextView mCancelrw1;//取消任务
         TextView mContinuerw1;//继续任务
         TextView mConverrw1;//转交任务
         LinearLayout aftersub1;//提交审核之后显示LinearLayout
        RelativeLayout shbz1;
    }

  */
/*  *//*
*/
/**
     * 创建mProgressDialog
     *//*
*/
/*
    private void createProgressDialogTitle(String title)
    {
        if(mProgressDialog == null)
        {
            mProgressDialog = new ProgressDialog(mContext);
        }
        mProgressDialog.setTitle(title);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }

    *//*
*/
/**
     * 隐藏mProgressDialog
     *//*
*/
/*
    private void dismissProgressDialog()
    {
        if(mProgressDialog != null)
        {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }*//*



}
*/
