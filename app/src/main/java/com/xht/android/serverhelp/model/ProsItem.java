package com.xht.android.serverhelp.model;

/**
 * Created by czw on 2016/10/24.
 * 办证进度标签每步的数据项
 */

public class ProsItem {
    private String flowName;//流程步骤类型
    private String startTime;
    private String endTime;
    private String gengjinP;   //跟进人
    private String progressStatus;   //审核状态
    private String serviceId;   //服务步骤ID

    private String flowId;//流程编号
    private String orderId;//订单编号
    private String employeeId;//客服编号

    private  String mImgTag1;
    private  String mImgTag2;
    private  String mImgTag3;
    private  String mImgTag4;
    private  String mImgTag5;
    private  String mImgTag6;

    public String getmImgTag1() {
        return mImgTag1;
    }

    public void setmImgTag1(String mImgTag1) {
        this.mImgTag1 = mImgTag1;
    }

    public String getmImgTag2() {
        return mImgTag2;
    }

    public void setmImgTag2(String mImgTag2) {
        this.mImgTag2 = mImgTag2;
    }

    public String getmImgTag3() {
        return mImgTag3;
    }

    public void setmImgTag3(String mImgTag3) {
        this.mImgTag3 = mImgTag3;
    }

    public String getmImgTag4() {
        return mImgTag4;
    }

    public void setmImgTag4(String mImgTag4) {
        this.mImgTag4 = mImgTag4;
    }

    public String getmImgTag5() {
        return mImgTag5;
    }

    public void setmImgTag5(String mImgTag5) {
        this.mImgTag5 = mImgTag5;
    }

    public String getmImgTag6() {
        return mImgTag6;
    }

    public void setmImgTag6(String mImgTag6) {
        this.mImgTag6 = mImgTag6;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getProgressStatus() {
        return progressStatus;
    }

    public void setProgressStatus(String progressStatus) {
        this.progressStatus = progressStatus;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    // [{"employeeId":4,"file2":null,"file3":null,"progressStatus":4,"file0":null,"file1":null,"file6":null,"file7":null,
    // "file4":null,"file5":null,"endTime":null,
    // "employeeName":"蔡成安","startTime":"1478231803612","serviceId":106,"flowName":"企业核名","orderId":37,"flowId":1}]

    private String imgFile1;
    private String imgFile2;
    private String imgFile3;
    private String imgFile4;
    private String imgFile5;
    private String imgFile6;
    private String imgFile7;
    private String imgFile8;

    private String imgResFile1;
    private String imgResFile2;
    private String imgResFile3;
    private String imgResFile4;
    private String imgResFile5;
    private String imgResFile6;
    private String imgResFile7;
    private String imgResFile8;

    public String getImgFile1() {
        return imgFile1;
    }

    public void setImgFile1(String imgFile1) {
        this.imgFile1 = imgFile1;
    }

    public String getImgFile2() {
        return imgFile2;
    }

    public void setImgFile2(String imgFile2) {
        this.imgFile2 = imgFile2;
    }

    public String getImgFile3() {
        return imgFile3;
    }

    public void setImgFile3(String imgFile3) {
        this.imgFile3 = imgFile3;
    }

    public String getImgFile4() {
        return imgFile4;
    }

    public void setImgFile4(String imgFile4) {
        this.imgFile4 = imgFile4;
    }

    public String getImgResFile1() {
        return imgResFile1;
    }

    public void setImgResFile1(String imgResFile1) {
        this.imgResFile1 = imgResFile1;
    }

    public String getImgResFile2() {
        return imgResFile2;
    }

    public void setImgResFile2(String imgResFile2) {
        this.imgResFile2 = imgResFile2;
    }

    public String getImgResFile3() {
        return imgResFile3;
    }

    public void setImgResFile3(String imgResFile3) {
        this.imgResFile3 = imgResFile3;
    }

    public String getImgResFile4() {
        return imgResFile4;
    }

    public void setImgResFile4(String imgResFile4) {
        this.imgResFile4 = imgResFile4;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getImgFile5() {
        return imgFile5;
    }

    public void setImgFile5(String imgFile5) {
        this.imgFile5 = imgFile5;
    }

    public String getImgFile6() {
        return imgFile6;
    }

    public void setImgFile6(String imgFile6) {
        this.imgFile6 = imgFile6;
    }

    public String getImgFile7() {
        return imgFile7;
    }

    public void setImgFile7(String imgFile7) {
        this.imgFile7 = imgFile7;
    }

    public String getImgFile8() {
        return imgFile8;
    }

    public void setImgFile8(String imgFile8) {
        this.imgFile8 = imgFile8;
    }

    public String getImgResFile5() {
        return imgResFile5;
    }

    public void setImgResFile5(String imgResFile5) {
        this.imgResFile5 = imgResFile5;
    }

    public String getImgResFile6() {
        return imgResFile6;
    }

    public void setImgResFile6(String imgResFile6) {
        this.imgResFile6 = imgResFile6;
    }

    public String getImgResFile7() {
        return imgResFile7;
    }

    public void setImgResFile7(String imgResFile7) {
        this.imgResFile7 = imgResFile7;
    }

    public String getImgResFile8() {
        return imgResFile8;
    }

    public void setImgResFile8(String imgResFile8) {
        this.imgResFile8 = imgResFile8;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getGengjinP() {
        return gengjinP;
    }

    public void setGengjinP(String gengjinP) {
        this.gengjinP = gengjinP;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
