package com.tobdev.qywxinner.model.entity;

import java.util.List;

public class QywxInnerUser {


    private Integer id;
    private String corpId;
    private String userId;
    private String name;
    private String mobile;
    private String avatar;
    private String qrCode;
    private Integer status;
    private QywxInnerCompany company;
    private List<QywxInnerDepartment> departmentList;
    private Integer userType;


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public QywxInnerCompany getCompany() {
        return company;
    }

    public void setCompany(QywxInnerCompany company) {
        this.company = company;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public List<QywxInnerDepartment> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<QywxInnerDepartment> departmentList) {
        this.departmentList = departmentList;
    }
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getUserType() {
        return userType;
    }

    public void setUserType(Integer userType) {
        this.userType = userType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getQrCode() {
        return qrCode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    @Override
    public String toString() {
        return "QywxInnerUser{" +
                "id=" + id +
                ", corpId='" + corpId + '\'' +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", avatar='" + avatar + '\'' +
                ", qrCode='" + qrCode + '\'' +
                ", status=" + status +
                ", company=" + company +
                ", departmentList=" + departmentList +
                ", userType=" + userType +
                '}';
    }
}
