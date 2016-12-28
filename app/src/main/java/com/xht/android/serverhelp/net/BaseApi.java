package com.xht.android.serverhelp.net;

public class BaseApi {

    //检查更新
	public static final String CHECK_VERSION_URL = "http://www.xiaohoutai.com.cn:8888/XHT/business/apkcustomerserviceController/updateApkVersion";

    //办证中列表数据
	public static final String BZ_ITEMS_URL = "http://www.xiaohoutai.com.cn:8888/XHT/homeOrderDetailController/LoadCertificateDetail";

    //办证中进度
	public static final String BZ_ProsInit_URL = "http://www.xiaohoutai.com.cn:8888/XHT/homeOrderDetailController/LoadCertificateDetailJingDu";

    //上传图片
	//public static final String BZ_PIC_UPLOAD_Url ="http://www.xiaohoutai.com.cn:8888/XHT/servicefileController/receiveServiceFile";
	public static final String BZ_PIC_UPLOAD_Url ="http://www.xiaohoutai.com.cn:8888/XHT/servicefileController/receiveServiceFile";
    //上传头像图片
	public static final String BZ_TOU_UPLOAD_Url ="http://www.xiaohoutai.com.cn:8888/XHT/employeeController/uploadHeadPortrait";
	//public static final String BZ_PIC_UPLOAD_Url ="http://1173492292.tunnel.2bdata.com/javaEELog/XUtilsFileReceive";
    //更新进度提交数据
    public static final String POST_ProsREF_URL ="http://www.xiaohoutai.com.cn:8888/XHT/companyServiceController/updateProgress";
    //办证中获取成员数据
    public static final String BZ_ChengYuan_URL ="http://www.xiaohoutai.com.cn:8888/XHT/homeOrderDetailController/ListOfHeadDetail";

    //修改密码
    public static final String CHANGMIMA_URL ="http://www.xiaohoutai.com.cn:8888/XHT/employeeController/modifyPassword";


    //办证中取消任务
    public static final String BZ_CANCEL_URL ="http://www.xiaohoutai.com.cn:8888/XHT/companyServiceController/taskCancel";
    //办证中继续任务
    public static final String BZ_CONTINUS_URL ="http://www.xiaohoutai.com.cn:8888/XHT/companyServiceController/taskContinue";
    //办证中转交任务
    public static final String BZ_FORWARD_URL ="http://www.xiaohoutai.com.cn:8888/XHT/employeeController/loadPermitterInfo";
    //转交任务保存
    public static final String BZ_FORWARDSAVE_URL ="http://www.xiaohoutai.com.cn:8888/XHT/companyServiceController/taskTransfers";

    //首页数据
    public static final String URL_Main = "http://www.xiaohoutai.com.cn:8888/XHT/homeOrderDetailController/TogetherOrderNum";
    //任务池
    public static final String URL_TaskBar = "http://www.xiaohoutai.com.cn:8888/XHT/homeOrderDetailController/loadMissonDetail";
    //搜索功能
    public static final String URL_ADDBar = "http://www.xiaohoutai.com.cn:8888/XHT/companyServiceController/searchOfTaskPool";
    //接受任务
    public static final String Task_ITEM_POST_URL = "http://www.xiaohoutai.com.cn:8888/XHT/homeOrderDetailController/saveLoadMissonDetail";
    //访问个人绩效
    public static final String PERSONAL_GET_URL ="http://www.xiaohoutai.com.cn:8888/XHT/employeeController/loadAchievementsByUser";
    //获取当前步骤的客户信息
    public static final String STEPDATA_GET_URL ="http://www.xiaohoutai.com.cn:8888/XHT/employeeController/getDetailOfServiceStepFlow";
    //访问预警数据
    public static final String WARNING_POST_URL = "http://www.xiaohoutai.com.cn:8888/XHT/homeOrderDetailController/LoadWarmingDetail";
    //通讯录客户
    public static final String CONTACTSPOST_URL = "http://www.xiaohoutai.com.cn:8888/XHT/homeOrderDetailController/AddressBookCustomerDetail";
    //通讯录内部员工
    public static final String CONTACTS_GONGSI_URL = "http://www.xiaohoutai.com.cn:8888/XHT/homeOrderDetailController/AddressBookEmployeeDetail";

    public static final String COMPANY_GONGSI_URL = "http://www.xiaohoutai.com.cn:8888/XHT/ordercontactController/loadCompanyinfoByUserId";

    //登录提交验证
    public static final String LOGIN_URL="http://www.xiaohoutai.com.cn:8888/XHT/employeeController/employeeLogin";

    //访问公司人员信息
    public static final String COMPLETE_NAME_URL="";


}
