package com.xht.android.serverhelp.model;

/**
 * Created by czw on 2016/10/13.
 * <br>办证列表项明细
 */

public class BZItem {

    private String name;    //步骤名称
    private String time;    //时间
    private String kehu;    //客户
    private String comp;    //客户公司
    private String compArea;    //公司区域
    private int ordId;      //订单ID
    private String flowId;      //ID

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrdId() {
        return ordId;
    }

    public void setOrdId(int ordId) {
        this.ordId = ordId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKehu() {
        return kehu;
    }

    public void setKehu(String kehu) {
        this.kehu = kehu;
    }

    public String getComp() {
        return comp;
    }

    public void setComp(String comp) {
        this.comp = comp;
    }

    public String getCompArea() {
        return compArea;
    }

    public void setCompArea(String compArea) {
        this.compArea = compArea;
    }

}
