package com.xht.android.serverhelp.model;

import java.util.List;

/**
 * Created by Administrator on 2016/12/28.
 */

public class CompleteCompanyBean {


    /**
     * message : 公司详细信息加载成功
     * result : success
     * entity : {"company":[{"businessScope":"全国各地","taxeNo":null,"bankName":null,"compTypeId":1,"companyName":"测试公司四","addressDetail":"五桂山长命水","registCapital":"5000","bankCardNo":null,"compTypeName":"自然人独资","Status":"1","payState":null,"taxesType":"0","organizeCode":null,"finalNameStatus":null,"companyId":27,"telNo":null,"countyId":1}],"persons":[{"shareRatio":"75","personname":"小民","postname":"法人","tel":null,"idcardcode":"554352563","idcardaddress":"湛江","personid":5},{"shareRatio":"25","personname":"照明","postname":"监事","tel":null,"idcardcode":"4408852385355256","idcardaddress":"湘江","personid":23}]}
     * code : 0
     */

    private String message;
    private String result;
    private EntityBean entity;
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public EntityBean getEntity() {
        return entity;
    }

    public void setEntity(EntityBean entity) {
        this.entity = entity;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class EntityBean {
        private List<CompanyBean> company;
        private List<PersonsBean> persons;

        public List<CompanyBean> getCompany() {
            return company;
        }

        public void setCompany(List<CompanyBean> company) {
            this.company = company;
        }

        public List<PersonsBean> getPersons() {
            return persons;
        }

        public void setPersons(List<PersonsBean> persons) {
            this.persons = persons;
        }

        public static class CompanyBean {
            /**
             * businessScope : 全国各地
             * taxeNo : null
             * bankName : null
             * compTypeId : 1
             * companyName : 测试公司四
             * addressDetail : 五桂山长命水
             * registCapital : 5000
             * bankCardNo : null
             * compTypeName : 自然人独资
             * Status : 1
             * payState : null
             * taxesType : 0
             * organizeCode : null
             * finalNameStatus : null
             * companyId : 27
             * telNo : null
             * countyId : 1
             */

            private String businessScope;
            private Object taxeNo;
            private Object bankName;
            private int compTypeId;
            private String companyName;
            private String addressDetail;
            private String registCapital;
            private Object bankCardNo;
            private String compTypeName;
            private String Status;
            private Object payState;
            private String taxesType;
            private Object organizeCode;
            private Object finalNameStatus;
            private int companyId;
            private Object telNo;
            private int countyId;

            public String getBusinessScope() {
                return businessScope;
            }

            public void setBusinessScope(String businessScope) {
                this.businessScope = businessScope;
            }

            public Object getTaxeNo() {
                return taxeNo;
            }

            public void setTaxeNo(Object taxeNo) {
                this.taxeNo = taxeNo;
            }

            public Object getBankName() {
                return bankName;
            }

            public void setBankName(Object bankName) {
                this.bankName = bankName;
            }

            public int getCompTypeId() {
                return compTypeId;
            }

            public void setCompTypeId(int compTypeId) {
                this.compTypeId = compTypeId;
            }

            public String getCompanyName() {
                return companyName;
            }

            public void setCompanyName(String companyName) {
                this.companyName = companyName;
            }

            public String getAddressDetail() {
                return addressDetail;
            }

            public void setAddressDetail(String addressDetail) {
                this.addressDetail = addressDetail;
            }

            public String getRegistCapital() {
                return registCapital;
            }

            public void setRegistCapital(String registCapital) {
                this.registCapital = registCapital;
            }

            public Object getBankCardNo() {
                return bankCardNo;
            }

            public void setBankCardNo(Object bankCardNo) {
                this.bankCardNo = bankCardNo;
            }

            public String getCompTypeName() {
                return compTypeName;
            }

            public void setCompTypeName(String compTypeName) {
                this.compTypeName = compTypeName;
            }

            public String getStatus() {
                return Status;
            }

            public void setStatus(String Status) {
                this.Status = Status;
            }

            public Object getPayState() {
                return payState;
            }

            public void setPayState(Object payState) {
                this.payState = payState;
            }

            public String getTaxesType() {
                return taxesType;
            }

            public void setTaxesType(String taxesType) {
                this.taxesType = taxesType;
            }

            public Object getOrganizeCode() {
                return organizeCode;
            }

            public void setOrganizeCode(Object organizeCode) {
                this.organizeCode = organizeCode;
            }

            public Object getFinalNameStatus() {
                return finalNameStatus;
            }

            public void setFinalNameStatus(Object finalNameStatus) {
                this.finalNameStatus = finalNameStatus;
            }

            public int getCompanyId() {
                return companyId;
            }

            public void setCompanyId(int companyId) {
                this.companyId = companyId;
            }

            public Object getTelNo() {
                return telNo;
            }

            public void setTelNo(Object telNo) {
                this.telNo = telNo;
            }

            public int getCountyId() {
                return countyId;
            }

            public void setCountyId(int countyId) {
                this.countyId = countyId;
            }
        }

        public static class PersonsBean {
            /**
             * shareRatio : 75
             * personname : 小民
             * postname : 法人
             * tel : null
             * idcardcode : 554352563
             * idcardaddress : 湛江
             * personid : 5
             */

            private String shareRatio;
            private String personname;
            private String postname;
            private Object tel;
            private String idcardcode;
            private String idcardaddress;
            private int personid;

            public String getShareRatio() {
                return shareRatio;
            }

            public void setShareRatio(String shareRatio) {
                this.shareRatio = shareRatio;
            }

            public String getPersonname() {
                return personname;
            }

            public void setPersonname(String personname) {
                this.personname = personname;
            }

            public String getPostname() {
                return postname;
            }

            public void setPostname(String postname) {
                this.postname = postname;
            }

            public Object getTel() {
                return tel;
            }

            public void setTel(Object tel) {
                this.tel = tel;
            }

            public String getIdcardcode() {
                return idcardcode;
            }

            public void setIdcardcode(String idcardcode) {
                this.idcardcode = idcardcode;
            }

            public String getIdcardaddress() {
                return idcardaddress;
            }

            public void setIdcardaddress(String idcardaddress) {
                this.idcardaddress = idcardaddress;
            }

            public int getPersonid() {
                return personid;
            }

            public void setPersonid(int personid) {
                this.personid = personid;
            }
        }
    }
}
