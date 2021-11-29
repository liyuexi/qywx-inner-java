package com.tobdev.qywxinner.model.entity;

public class QywxInnerCompany {


    private Integer id;
    private String corpId;
    private String agentId;
    private String agentSecret;

    private String corpName;
    private String corpFullName;
    private Integer subjectType;
    private String verifiedEndTime;
    private String approvalTemplateId;

    public String getApprovalTemplateId() {
        return approvalTemplateId;
    }

    public void setApprovalTemplateId(String approvalTemplateId) {
        this.approvalTemplateId = approvalTemplateId;
    }

    private Integer status;


    public String getCorpName() {
        return corpName;
    }

    public void setCorpName(String corpName) {
        this.corpName = corpName;
    }

    public String getCorpFullName() {
        return corpFullName;
    }

    public void setCorpFullName(String corpFullName) {
        this.corpFullName = corpFullName;
    }

    public Integer getSubjectType() {
        return subjectType;
    }

    public void setSubjectType(Integer subjectType) {
        this.subjectType = subjectType;
    }

    public String getVerifiedEndTime() {
        return verifiedEndTime;
    }

    public void setVerifiedEndTime(String verifiedEndTime) {
        this.verifiedEndTime = verifiedEndTime;
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


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }

    public void setAgentSecret(String agentSecret) {
        this.agentSecret = agentSecret;
    }

    public String getAgentSecret() {
        return agentSecret;
    }
}
