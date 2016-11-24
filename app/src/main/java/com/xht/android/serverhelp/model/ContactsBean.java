package com.xht.android.serverhelp.model;

/**
 * Created by Administrator on 2016/11/24.
 */

public class ContactsBean {
    //{"userid":14,"companyName":"春天","companyId":60}
    String userId;
    String companyName;
    String companyId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
}
