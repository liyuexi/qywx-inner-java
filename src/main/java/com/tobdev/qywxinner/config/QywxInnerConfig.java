package com.tobdev.qywxinner.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "qywx-third")
public class QywxInnerConfig {

    private String corpId;

    private String suiteId;
    private String suiteSecret;
    private String token;
    private String encodingAESKey;
    private Integer authType;
    private String templateId;
    private String approvalFlowId;

    private String baseUrl = "https://qyapi.weixin.qq.com/cgi-bin/";

    //获取access_token
    //https://open.work.weixin.qq.com/api/doc/90000/90135/91039
    private String accessTokenUrl = baseUrl+"gettoken?corpid=%s&corpsecret=%s";

    //身份验证 扫码授权登录
    //https://open.work.weixin.qq.com/api/doc/90000/90135/91019
    private String ssoAuthUrl = "https://open.work.weixin.qq.com/wwopen/sso/qrConnect?appid=%s&agentid=%s&redirect_uri=%s&state=%s";
    //https://open.work.weixin.qq.com/api/doc/90000/90135/91437
    //与H5登录接口一样
    private String ssoUserInfoUrl = baseUrl+"user/getuserinfo?access_token=%s&code=%s";

    //公司相关
    //https://open.work.weixin.qq.com/api/doc/90000/90135/90208
    private String departmentUrl = baseUrl+"department/list?access_token=%s";
    //https://open.work.weixin.qq.com/api/doc/90000/90135/90200
    private String userSimplelistUrl = baseUrl+"user/simplelist?access_token=%s&department_id=%s&fetch_child=%s";
    //https://open.work.weixin.qq.com/api/doc/90000/90135/90196
    private String userDetailUrl = baseUrl+"user/get?access_token=%s&userid=%s";

    //客户联系
    //获取配置了客户联系功能的成员列表 https://open.work.weixin.qq.com/api/doc/90000/90135/92571
    private String extContactFollowUserListUrl = baseUrl+"externalcontact/get_follow_user_list?access_token=%s";
    //获取客户列表 https://open.work.weixin.qq.com/api/doc/90000/90135/92113
    private String extContactListUrl = baseUrl+"externalcontact/list?access_token=%s&userid=%s";
    //获取客户群列表 https://open.work.weixin.qq.com/api/doc/90000/90135/92120
    private String extContactGroupchatUrl = baseUrl+"externalcontact/groupchat/list?access_token=%s";

    //消息推送
    //https://open.work.weixin.qq.com/api/doc/90000/90135/90236
    private String messageSendUrl= baseUrl+"message/send?access_token=%s";

    //素材管理
    //https://open.work.weixin.qq.com/api/doc/90000/90135/91054
    //type	是	媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件（file）
    private String mediaUploadUrl = baseUrl+"media/upload?access_token=%s&type=%s";
    private String mediaUploadimgUrl = baseUrl+"media/uploadimg?access_token=%s";
    private String mediaGetUrl = baseUrl+"media/get?access_token=%s&media_id=%s";
    private String mediaGetJssdkUrl = baseUrl+"media/get/jssdk?access_token=%s&media_id=%s";

    //审批
    //审批应用 https://work.weixin.qq.com/api/doc/90001/90143/91956
    private String oaCopyTemplateUrl ="oa/approval/copytemplate?access_token=%s";
    private String oaGetTemplateUrl ="/oa/gettemplatedetail?access_token=%s";
    private String oaApplyEventUrl = "oa/applyevent?access_token=%s";
    private String oaGetApprovalUrl ="oa/getapprovaldetail?access_token=%s";

    //审批流程引擎 https://work.weixin.qq.com/api/doc/90001/90143/93798
    private String openApprovalDataUrl = baseUrl+"corp/getopenapprovaldata?access_token=ACCESS_TOKEN";


    // H5应用
    //scope	是	应用授权作用域。企业自建应用固定填写：snsapi_base
    // https://open.work.weixin.qq.com/api/doc/90000/90135/91020
    private String oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_base&state=%s#wechat_redirect";
    //https://open.work.weixin.qq.com/api/doc/90000/90135/91023
    private String oauthUserUrl = baseUrl+"user/getuserinfo?access_token=%s&code=%s";

    //https://work.weixin.qq.com/api/doc/90000/90136/90506
    private String jsapiTicketUrl = baseUrl+"get_jsapi_ticket?access_token=%s";
    private String jsapiTicketAgentUrl = baseUrl+"ticket/get?access_token=%s&type=agent_config";

    //家校沟通
    //https://work.weixin.qq.com/api/doc/90000/90135/91638
    private String extContactMessageSendUrl = baseUrl+"externalcontact/message/send?access_token=%s";

    //此oauth与H5oauth一致  https://work.weixin.qq.com/api/doc/90000/90135/91857
    private String schoolOauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
    //https://work.weixin.qq.com/api/doc/90000/90135/91707
    private String schoolOauthUserUrl = "user/getuserinfo?access_token=%s&code=%s";

    private String schoolUrl = baseUrl+"school/";
    //https://work.weixin.qq.com/api/doc/90000/90135/92337
    private String schoolUserGetUrl = schoolUrl+"user/get?access_token=%s&userid=%s";
    //https://work.weixin.qq.com/api/doc/90000/90135/92343
    private String schoolDepartmentListUrl = schoolUrl+"department/list?access_token=%s&id=%s";
    //https://work.weixin.qq.com/api/doc/90000/90135/92446
    private String schoolUserListUrl = schoolUrl+"user/list?access_token=%s&department_id=%s&fetch_child=%s";



    //小程序应用
    //小程序登录流程 https://work.weixin.qq.com/api/doc/90000/90136/92426
    //code2Session https://work.weixin.qq.com/api/doc/90000/90136/91507
    private String code2sessionUrl = baseUrl+"miniprogram/jscode2session?access_token=%s&js_code=%s&grant_type=authorization_code";


    public String getJsapiTicketUrl() {
        return jsapiTicketUrl;
    }

    public String getOauthUrl() {
        return oauthUrl;
    }

    public String getUserSimplelistUrl() {
        return userSimplelistUrl;
    }

    public String getAccessTokenUrl() {
        return accessTokenUrl;
    }

    public String getDepartmentUrl() {
        return departmentUrl;
    }


    public String getCorpId() {
        return corpId;
    }

    public String getSuiteId() {
        return suiteId;
    }

    public String getSuiteSecret() {
        return suiteSecret;
    }

    public String getToken() {
        return token;
    }

    public String getEncodingAESKey() {
        return encodingAESKey;
    }

    public Integer getAuthType() {
        return authType;
    }

    public String getUserDetailUrl() {
        return userDetailUrl;
    }

    public String getOauthUserUrl() {
        return oauthUserUrl;
    }


    public String getCode2sessionUrl() {
        return code2sessionUrl;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public void setSuiteId(String suiteId) {
        this.suiteId = suiteId;
    }

    public void setSuiteSecret(String suiteSecret) {
        this.suiteSecret = suiteSecret;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setEncodingAESKey(String encodingAESKey) {
        this.encodingAESKey = encodingAESKey;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getApprovalFlowId() {
        return approvalFlowId;
    }

    public void setApprovalFlowId(String approvalFlowId) {
        this.approvalFlowId = approvalFlowId;
    }


    public String getSsoAuthUrl() {
        return ssoAuthUrl;
    }


    public String getJsapiTicketAgentUrl() {
        return jsapiTicketAgentUrl;
    }


    public String getExtContactFollowUserListUrl() {
        return extContactFollowUserListUrl;
    }

    public String getExtContactListUrl() {
        return extContactListUrl;
    }

    public String getExtContactGroupchatUrl() {
        return extContactGroupchatUrl;
    }

    public String getMessageSendUrl() {
        return messageSendUrl;
    }

    public String getMediaUploadUrl() {
        return mediaUploadUrl;
    }

    public String getMediaUploadimgUrl() {
        return mediaUploadimgUrl;
    }

    public String getMediaGetUrl() {
        return mediaGetUrl;
    }

    public String getMediaGetJssdkUrl() {
        return mediaGetJssdkUrl;
    }

    public String getOaCopyTemplateUrl() {
        return oaCopyTemplateUrl;
    }

    public String getOaGetTemplateUrl() {
        return oaGetTemplateUrl;
    }

    public String getOaApplyEventUrl() {
        return oaApplyEventUrl;
    }

    public String getOaGetApprovalUrl() {
        return oaGetApprovalUrl;
    }

    public String getOpenApprovalDataUrl() {
        return openApprovalDataUrl;
    }

    public String getSchoolOauthUrl() {
        return schoolOauthUrl;
    }

    public String getSchoolOauthUserUrl() {
        return schoolOauthUserUrl;
    }

    public String getSchoolUserGetUrl() {
        return schoolUserGetUrl;
    }

    public String getSchoolDepartmentListUrl() {
        return schoolDepartmentListUrl;
    }

    public String getSchoolUserListUrl() {
        return schoolUserListUrl;
    }

    public String getExtContactMessageSendUrl() {
        return extContactMessageSendUrl;
    }

    public String getSsoUserInfoUrl() {
        return ssoUserInfoUrl;
    }
}
