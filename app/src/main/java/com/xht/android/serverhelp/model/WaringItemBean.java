package com.xht.android.serverhelp.model;

/**
 * Created by Administrator on 2016-10-17.
 */
public class WaringItemBean {

    private String taskName;//任务名
    private String comName;//公司名
    private String mName;//客户名
    private String mTime;//时间
    private String mAddress;//地区
    private int mDaty;//天数

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getComName() {
        return comName;
    }

    public void setComName(String comName) {
        this.comName = comName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public int getmDaty() {
        return mDaty;
    }

    public void setmDaty(int mDaty) {
        this.mDaty = mDaty;
    }
}
