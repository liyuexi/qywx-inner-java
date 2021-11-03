package com.tobdev.qywxinner.service;

import com.alibaba.fastjson.JSONObject;
import com.tobdev.qywxinner.config.QywxCacheConfig;
import com.tobdev.qywxinner.config.QywxInnerConfig;
import com.tobdev.qywxinner.model.entity.QywxInnerCompany;
import com.tobdev.qywxinner.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class QywxInnerService {

    private final static Logger logger = LoggerFactory.getLogger("test");

    @Autowired
    private QywxInnerConfig qywxInnerConfig;
    @Autowired
    private QywxCacheConfig qywxCacheConfig;
    @Autowired
    private QywxInnerCompanyService qywxInnerCompanyService;
    
    @Autowired
    private RedisUtils strRedis;


    public String getAccessToken(String corpId){
        
        String result = "";
        QywxInnerCompany company = qywxInnerCompanyService.getCompanyByCorpId(corpId);

        String agentSecret = company.getAgentSecret();

        String  accessTokenUrl =  String.format(qywxInnerConfig.getAccessTokenUrl(),corpId,agentSecret);
        Map response = RestUtils.get(accessTokenUrl );
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }else{
            result = (String) response.get("access_token");
        }

        return result;

    }

    //**********************************  应用管理相关   *************************//
    //获取应用
    public Map getAgent(String corpId){

        String accessToken = getAccessToken(corpId);
        //获取企业的agentid
        QywxInnerCompany company =  qywxInnerCompanyService.getCompanyByCorpId(corpId);
        Integer agentId = company.getAgentId();
        String url = String.format(qywxInnerConfig.getAgentGetUrl(),accessToken,agentId);
        Map response = RestUtils.get(url);
        //获取错误日志
        logger.error(response.toString());
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return response;

    }


    //设置应用
    public Map setAgent(String corpId, JSONObject postJson){

        //https://open.work.weixin.qq.com/api/doc/90001/90143/93414
        String accessToken = this.getAccessToken(corpId);

        String url = String.format(qywxInnerConfig.getAgentSetUrl(),accessToken) ;
        //获取企业的agentid
        QywxInnerCompany company =  qywxInnerCompanyService.getCompanyByCorpId(corpId);
        Integer agentId = company.getAgentId();

        postJson.put("agentid",agentId);
        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    //**********************************  通讯录相关   *************************//

    public Map getDepartmentList(String corpId){

        String accessToken = getAccessToken(corpId);
//        if(id != "0"){
//            paramsMap.put("id",id);
//        }
        String url = String.format(qywxInnerConfig.getDepartmentUrl(),accessToken);
        Map response = RestUtils.get(url);
        //获取错误日志
        logger.error(response.toString());
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return response;
    }


    public Map getUserSimplelist(String corpId,String id,String fetch_child){

        String accessToken = getAccessToken(corpId);
        String url =String.format(qywxInnerConfig.getUserSimplelistUrl(),accessToken,id,fetch_child);
        Map response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return response;
    }

    public Map getUserDetailList(String corpId,String id,String fetch_child){

        String accessToken = getAccessToken(corpId);
        String url =String.format(qywxInnerConfig.getUserDetailListUrl(),accessToken,id,fetch_child);
        Map response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return response;
    }


    public Map getUserDetail(String corpId,String userId){

        String accessToken = getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getUserDetailUrl(),accessToken,userId);
        Map response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return response;

    }





    //**********************************  客户联系相关   *************************//
    //获取配置了客户联系功能的成员列表
    public Map getExtContactFollowUserList(String corpId){
        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactFollowUserListUrl(),accessToken) ;
        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    //获取指定成员添加的客户列表
    public Map getExtContactList(String corpId,String userId){

        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactListUrl(),accessToken,userId) ;

        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    //获取客户详情
    public Map getExtContactDetail(String corpId,String extUserId){

        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactDetailUrl(),accessToken,extUserId,"") ;

        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    //获取配置过客户群管理的客户群列表。
    public Map getExtContactGroupchatList(String corpId,String userId){

        //https://open.work.weixin.qq.com/api/doc/90001/90143/93414
        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactGroupchatListUrl(),accessToken) ;

        //分页，预期请求的数据量，取值范围 1 ~ 1000  测试写死1000
        JSONObject postJson = new JSONObject();
        ArrayList ownerList = new ArrayList();
        ownerList.add(userId);
        JSONObject ownerJson = new JSONObject();
        ownerJson.put( "userid_list",ownerList);
        postJson.put("owner_filter",ownerJson);
        //owner_filter  userid_list	否	用户ID列表。最多100个
        postJson.put("limit",1000);
        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    //获取群聊详情
    public Map   getExtContactGroupchatDetail(String corpId,String chatId){

        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactGroupchatDetailUrl(),accessToken) ;

        JSONObject postJson = new JSONObject();
        postJson.put("chat_id",chatId);
        postJson.put("need_name",1);
        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    //发送客户欢迎语
    public Map   sendWelcomeMsg(String corpId,String welcomeCode,String text){

        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactGroupchatDetailUrl(),accessToken) ;
        /*
        {
            "welcome_code":"CALLBACK_CODE",
            "text":{
                "content":"文本消息内容"
            },
           "attachments": [
                  {
                    "msgtype":"link",
                    "link":{
                    "title":"消息标题",
                    "picurl":"https://example.pic.com/path",
                    "desc":"消息描述",
                    "url":"https://example.link.com/path"
                    }
                },
             ]
        }
        */
        JSONObject postJson = new JSONObject();
        postJson.put("welcome_code",welcomeCode);

        JSONObject textContentJson = new JSONObject();
        textContentJson.put("content",text);
        postJson.put("text",textContentJson);

        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    //创建企业群发
    public Map  addMsgTemplate(String corpId,ArrayList extUserList,String sender,String text){

        /**
         * {
         *     "chat_type": "single",
         *     "external_userid": [
         *         "woAJ2GCAAAXtWyujaWJHDDGi0mACAAAA",
         *         "wmqfasd1e1927831123109rBAAAA"
         *     ],
         *     "sender": "zhangsan",
         *     "text": {
         *         "content": "文本消息内容"
         *     },
         *     "attachments": [
     *           {
         *         "msgtype": "link",
         *         "link": {
         *             "title": "消息标题",
         *             "picurl": "https://example.pic.com/path",
         *             "desc": "消息描述",
         *             "url": "https://example.link.com/path"
         *          }
     *           },
         *     】
         * }
         */
        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtcontactAddMsgTemplateUrl(),accessToken) ;

        JSONObject postJson = new JSONObject();
        postJson.put("chat_type","single");

        postJson.put("external_userid",extUserList);

        postJson.put("sender",sender);

        JSONObject textContentJson = new JSONObject();
        textContentJson.put("content",text);
        postJson.put("text",textContentJson);


        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }



    //**********************************  消息相关   *************************//
    //消息推送
    //发送消息
    public Map sendMessageText(String corpId,String userId,String text){

        //https://open.work.weixin.qq.com/api/doc/90001/90143/93414
        String accessToken = this.getAccessToken(corpId);

        String url = String.format(qywxInnerConfig.getMessageSendUrl(),accessToken) ;
        //获取企业的agentid
        QywxInnerCompany company =  qywxInnerCompanyService.getCompanyByCorpId(corpId);
        Integer agentId = company.getAgentId();

        JSONObject postJson = new JSONObject();
        postJson.put("msgtype","text");
        postJson.put("agentid",agentId);

        JSONObject testJson =  new JSONObject();
        testJson.put("content",text);
        postJson.put("text",testJson);

        postJson.put("touser",userId);
        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    //发送消息
    public Map sendImage(String corpId,String userId,String mediaId){

        //https://open.work.weixin.qq.com/api/doc/90001/90143/93414
        String accessToken = this.getAccessToken(corpId);

        String url = String.format(qywxInnerConfig.getMessageSendUrl(),accessToken) ;
        //获取企业的agentid
        QywxInnerCompany company =  qywxInnerCompanyService.getCompanyByCorpId(corpId);
        Integer agentId = company.getAgentId();

        JSONObject postJson = new JSONObject();
        postJson.put("msgtype","image");
        postJson.put("agentid",agentId);

        JSONObject testJson =  new JSONObject();
        testJson.put("media_id",mediaId);
        postJson.put("image",testJson);

        postJson.put("touser",userId);
        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    public  String replyMessage(){

//        XStream xstream = new XStream();
//              '<xml>
//               <Encrypt><![CDATA[msg_encrypt]]></Encrypt>
//               <MsgSignature><![CDATA[msg_signature]]></MsgSignature>
//               <TimeStamp>timestamp</TimeStamp>
//               <Nonce><![CDATA[nonce]]></Nonce>
//               </xml>'
        return  "";
    }


    //**********************************  素材管理相关   *************************//

    public JSONObject uploadMedia(String corpId, MultiValueMap<String, Object> params ,String type){
        String accessToken = this.getAccessToken(corpId);
        //https://open.work.weixin.qq.com/api/doc/90000/90135/90253
        //type	是	媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件（file）
        //使用multipart/form-data POST上传文件， 文件标识名为”media”
        String url = String.format(qywxInnerConfig.getMediaUploadUrl(),accessToken,type);
        return  RestUtils.upload(url,params);

    }

    public JSONObject uploadImg(String corpId, MultiValueMap<String, Object> params ){
        String accessToken = this.getAccessToken(corpId);
        //https://open.work.weixin.qq.com/api/doc/90000/90135/90253
        //type	是	媒体文件类型，分别有图片（image）、语音（voice）、视频（video），普通文件（file）
        //使用multipart/form-data POST上传文件， 文件标识名为”media”
        String url = String.format(qywxInnerConfig.getMediaUploadimgUrl(),accessToken);
        return  RestUtils.upload(url,params);

    }


    public  byte[] downloadMedia(String corpId,String mediaId){
        String accessToken = this.getAccessToken(corpId);
        String mediaDownloadUrl = String.format(qywxInnerConfig.getMediaGetUrl(),accessToken,mediaId);
        return RestUtils.dowload(mediaDownloadUrl);

    }


    //**********************************  oa审批相关   *************************//

    //审批流程引擎
    public Map getAprrovalFlowInit(String corpId){
        QywxInnerCompany company = qywxInnerCompanyService.getCompanyByCorpId(corpId);
        Map approval = new HashMap();
        approval.put("templateId",company.getApprovalTemplateId());
        approval.put("thirdNo",new SnowFlakeUtils(0, 0).createOrderNo());
        return approval;
    }

    public Map getApprovalFlowStatus(String corpId,String thirdNo){
        String accessToken = this.getAccessToken(corpId);
        String approvalUrl = String.format(qywxInnerConfig.getOpenApprovalDataUrl(),accessToken);
        JSONObject postJson = new JSONObject();
        postJson.put("thirdNo",thirdNo);
        return RestUtils.post(approvalUrl,postJson);
    }

    //**********************************  效率工具相关   *************************//
    public  Map addCalendar(String corpId,String summary,String color,String organizeUserId,String shareUserId){
        String accessToken = this.getAccessToken(corpId);

        String url = String.format(qywxInnerConfig.getCalendarAddUrl(),accessToken) ;

        JSONObject postJson = new JSONObject();

        JSONObject calendarJson = new JSONObject();
        calendarJson.put("organizer",organizeUserId);
        calendarJson.put("summary",summary);
        calendarJson.put("color",color);
        ArrayList sharesArr =  new ArrayList();
        JSONObject shareJson = new JSONObject();
        shareJson.put("userid",shareUserId);
        sharesArr.add(shareJson);
        calendarJson.put("shares",sharesArr);

        postJson.put("calendar",calendarJson);
        JSONObject response = RestUtils.post(url,postJson);
        return  response;
    }

    public  Map getCalendar(String corpId,String calendarId){
        String accessToken = this.getAccessToken(corpId);

        String url = String.format(qywxInnerConfig.getCalendarDetailUrl(),accessToken) ;

        JSONObject postJson = new JSONObject();
        ArrayList calendarArr =  new ArrayList();
        calendarArr.add(calendarId);
        postJson.put("cal_id_list",calendarArr);
        JSONObject response = RestUtils.post(url,postJson);
        return  response;

    }

    public  Map addSchedule(String corpId,String organizeUserId,String attendeesUserId,String summary,String description){
        String accessToken = this.getAccessToken(corpId);

        String url = String.format(qywxInnerConfig.getScheduleAddUrl(),accessToken) ;

        JSONObject postJson = new JSONObject();

        JSONObject scheduleJson = new JSONObject();
        scheduleJson.put("organizer",organizeUserId);
        scheduleJson.put("summary",summary);
        scheduleJson.put("description",description);
        ArrayList attendeesArr =  new ArrayList();
        JSONObject attendeesJson = new JSONObject();
        attendeesJson.put("userid",attendeesUserId);
        attendeesArr.add(attendeesJson);
        scheduleJson.put("attendees",attendeesArr);

        postJson.put("schedule",scheduleJson);

        JSONObject response = RestUtils.post(url,postJson);
        return  response;

    }

    public  Map getScheduleList(String corpId,String calendarId,String offset,String limit){
        String accessToken = this.getAccessToken(corpId);

        String url = String.format(qywxInnerConfig.getScheduleListUrl(),accessToken) ;

        JSONObject postJson = new JSONObject();
        postJson.put("cal_id",calendarId);
        postJson.put("offset",offset);
        postJson.put("limit",limit);
        JSONObject response = RestUtils.post(url,postJson);
        return  response;

    }

    public  Map getSchedule(String corpId,String scheduleId){
        String accessToken = this.getAccessToken(corpId);

        String url = String.format(qywxInnerConfig.getScheduleDetailUrl(),accessToken) ;
        JSONObject postJson = new JSONObject();
        ArrayList scheduleArr =  new ArrayList();
        scheduleArr.add(scheduleId);
        postJson.put("schedule_id_list",scheduleArr);
        JSONObject response = RestUtils.post(url,postJson);
        return  response;
    }

    
    public  Map addMeeting(String corpId,String createUserId,String title,String meetingStart,String meetingDuration,String type){
        String accessToken = this.getAccessToken(corpId);

        String url = String.format(qywxInnerConfig.getMeetingCreateUrl(),accessToken) ;

        JSONObject postJson = new JSONObject();
        postJson.put("creator_userid",createUserId);
        postJson.put("title",title);
        postJson.put("meeting_start",meetingStart);
        postJson.put("meeting_duration",meetingDuration);
        postJson.put("type",type);

        JSONObject response = RestUtils.post(url,postJson);
        return  response;
    }

    public  Map getUserMeeting(String corpId,String userId){
        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getUserMeetingListUrl(),accessToken) ;
        JSONObject postJson = new JSONObject();
        postJson.put("userid",userId);
        JSONObject response = RestUtils.post(url,postJson);
        return  response;
    }

    public Map  cancelMeeting(String corpId,String meetingId){

        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getMeetingCancelUrl(),accessToken) ;
        JSONObject postJson = new JSONObject();
        postJson.put("meeting_id",meetingId);
        JSONObject response = RestUtils.post(url,postJson);
        return  response;

    }

    public Map getMeetingDetail(String corpId,String meetingId){

        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getMeetingDetailUrl(),accessToken) ;

        JSONObject postJson = new JSONObject();
        postJson.put("meetingid",meetingId);
        JSONObject response = RestUtils.post(url,postJson);
        return  response;
    }

    //**********************************  PC相关   *************************//
    //PC网页 sso  用于非企业微信环境下扫码登录，如运行在浏览的器应用后台或者脱离企业微信环境下H5应用
    //https://open.work.weixin.qq.com/wwopen/sso/3rd_qrConnect?appid=ww100000a5f2191&redirect_uri=http%3A%2F%2Fwww.oa.com&state=web_login@gyoss9&usertype=admin
    public  String getSsoUrl(String corpId,String redirectUrl){
        QywxInnerCompany company = qywxInnerCompanyService.getCompanyByCorpId(corpId);
        String state = "test";
        String ssoUrl = String.format(qywxInnerConfig.getSsoAuthUrl(),company.getCorpId(),company.getAgentId(),redirectUrl,state);
        return ssoUrl;
    }

    public Map getLoginInfo(String corpId,String authCode){

        String url = String.format(qywxInnerConfig.getSsoUserInfoUrl(),getAccessToken(corpId),authCode) ;
        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }

        //获取通讯录用户详情get
        String accessToken = getAccessToken(corpId);
        String userId = (String) response.get("UserId");
        String detailUrl = String.format(qywxInnerConfig.getUserDetailUrl(),accessToken,userId);
        Map detaiResponse = RestUtils.get(detailUrl);
        //获取错误日志
        if(detaiResponse.containsKey("errcode") && (Integer) detaiResponse.get("errcode") != 0){
            logger.error(detaiResponse.toString());
        }
        /**
         * {
         * 	"errcode": 0,
         * 	"errmsg": "ok",
         * 	"corpid": "wwcc3b4b831051d56e",
         * 	"userid": "LiYueXi",
         * 	"name": "LiYueXi",
         * 	"department": [1],
         * 	"gender": "1",
         * 	"avatar": "https://rescdn.qqmail.com/node/wwmng/wwmng/style/images/independent/DefaultAvatar$73ba92b5.png",
         * 	"open_userid": "woMAh2BwAApVMP0ZDYUk42tUw3CIeHFA"
         * }
         */

        return detaiResponse;

      //  return  response;

    }


    //********************************** H5应用 Oauth   *************************//
    public String getOauthUrl(String corpId,String url){
//        应用授权作用域。
//        snsapi_base：静默授权，可获取成员的基础信息（UserId与DeviceId）；
//        snsapi_userinfo：静默授权，可获取成员的详细信息，但不包含手机、邮箱等敏感信息；
//        snsapi_privateinfo：手动授权，可获取成员的详细信息，包含手机、邮箱等敏感信息（已不再支持获取手机号/邮箱）。
        QywxInnerCompany company = qywxInnerCompanyService.getCompanyByCorpId(corpId);
        String state = "sdfds343";
        String result = String.format(qywxInnerConfig.getOauthUrl(),company.getCorpId(),url,state);
        return  result;
    }

    public Map getOauthUser(String  corpId,String code) {

        String accessToken = getAccessToken(corpId);


        String getOauthUrl = String.format(qywxInnerConfig.getOauthUserUrl(),accessToken,code);
        Map  response = RestUtils.get(getOauthUrl);
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
            return  response;
        }
//        return  response;


        //获取通讯录用户详情get
        String userId = (String) response.get("UserId");
        String url = String.format(qywxInnerConfig.getUserDetailUrl(),accessToken,userId);
        Map detaiResponse = RestUtils.get(url);
        //获取错误日志
        if(detaiResponse.containsKey("errcode") && (Integer) detaiResponse.get("errcode") != 0){
            logger.error(detaiResponse.toString());
        }
        /**
         * {
         * 	"errcode": 0,
         * 	"errmsg": "ok",
         * 	"corpid": "wwcc3b4b831051d56e",
         * 	"userid": "LiYueXi",
         * 	"name": "LiYueXi",
         * 	"department": [1],
         * 	"gender": "1",
         * 	"avatar": "https://rescdn.qqmail.com/node/wwmng/wwmng/style/images/independent/DefaultAvatar$73ba92b5.png",
         * 	"open_userid": "woMAh2BwAApVMP0ZDYUk42tUw3CIeHFA"
         * }
         */

        return detaiResponse;

    }


    public HashMap getJsSign(String corpId,String nonce, String timestamp,  String signUrl) throws  Exception{


        //获取jsticket
        String accessToken = getAccessToken(corpId);

//        Map paramsMap = new HashMap();
//        paramsMap.put("access_token",accessToken);
        String url = String.format(qywxInnerConfig.getJsapiTicketUrl(),accessToken);
        Map response = RestUtils.get(url);
        //获取错误日志
        logger.error(response.toString());
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        String jsapiTicket = (String) response.get("ticket");
        System.out.println(jsapiTicket);
        String sign = QywxSHA.getSHA1(jsapiTicket,nonce,timestamp,signUrl);
        HashMap result = new HashMap();
        result.put("appId", corpId);
        result.put("timestamp", timestamp);
        result.put("nonceStr", nonce);
        result.put("signature", sign);
        return  result;

    }

    public Map  getJsSignAgent(String corpId,String nonce, String timestamp,  String signUrl) throws  Exception{
        //https://work.weixin.qq.com/api/doc/90001/90144/90548
        //https://work.weixin.qq.com/api/doc/90001/90144/90539#%E8%8E%B7%E5%8F%96%E5%BA%94%E7%94%A8%E7%9A%84jsapi_ticket

        //获取jsticket
        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getJsapiTicketAgentUrl(),accessToken);
        Map response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        String jsapiTicket = (String) response.get("ticket");
        //通过 获取jsticket  获取签名
        String sign = QywxSHA.getSHA1(jsapiTicket,nonce,timestamp,signUrl);

        //获取appid
        QywxInnerCompany company =  qywxInnerCompanyService.getCompanyByCorpId(corpId);
        Integer agentId = company.getAgentId();

        HashMap result = new HashMap();
        result.put("corpId", corpId);
        result.put("agentId", agentId);
        result.put("timestamp", timestamp);
        result.put("nonceStr", nonce);
        result.put("signature", sign);
        return  result;

    }

    //******************************  家校沟通   *********************//
    public  String getSchoolOauthUrl(String corpId,String url){


        QywxInnerCompany company = qywxInnerCompanyService.getCompanyByCorpId(corpId);
        String state = "sdfds343";
        String result = String.format(qywxInnerConfig.getOauthUrl(),company.getCorpId(),url,state);
        return  result;

    }

    public Map getSchoolOauthUser(String corpId,String code) {

        String accessToken = this.getAccessToken(corpId);
        //获取访问用户身份
        String getOauthUrl = String.format(qywxInnerConfig.getSchoolOauthUserUrl(),accessToken,code);
        Map  response = RestUtils.get(getOauthUrl);
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
            return  response;
        }

        Map detaiResponse = new HashMap();
        //家长
        if(response.containsKey("parent_userid")){
            //https://work.weixin.qq.com/api/doc/90000/90135/92337
            String userId = (String) response.get("parent_userid");
            String url = String.format(qywxInnerConfig.getSchoolUserGetUrl(),accessToken,userId);
            detaiResponse = RestUtils.get(url);
            //获取错误日志
            if(detaiResponse.containsKey("errcode") && (Integer) detaiResponse.get("errcode") != 0){
                logger.error(detaiResponse.toString());
            }
        }else if(response.containsKey("UserId")){
            //获取通讯录用户详情get
            String userId = (String) response.get("UserId");
            String url = String.format(qywxInnerConfig.getUserDetailUrl(),accessToken,userId);
            detaiResponse = RestUtils.get(url);
            //获取错误日志
            if(detaiResponse.containsKey("errcode") && (Integer) detaiResponse.get("errcode") != 0){
                logger.error(detaiResponse.toString());
            }
            detaiResponse.put("user_type",0);

        }else{
            //
        }

        return detaiResponse;

    }

    public  Map getSubscribeQr(String corpId){

        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactSubscribeQrUrl(),accessToken) ;

        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    public Map getSchoolDepartmentList(String corpId,String deptId){
        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getSchoolDepartmentListUrl(),accessToken,deptId) ;

        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;
    }



    public Map getSchoolUserList(String corpId,String deptId,String fetchChild){
        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getSchoolUserListUrl(),accessToken,deptId,fetchChild) ;

        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    public Map getSchoolUserDetail(String corpId,String deptId){
        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getSchoolUserGetUrl(),accessToken,deptId) ;

        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;
    }


    public Map sendExtContactMessageText(String corpId,String toParentUserId,String toStudentUserId,String text){

        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactMessageSendUrl(),accessToken) ;

        //获取企业的agentid
        QywxInnerCompany company =  qywxInnerCompanyService.getCompanyByCorpId(corpId);
        Integer agentId = company.getAgentId();

        JSONObject postJson = new JSONObject();
        postJson.put("msgtype","text");
        postJson.put("agentid",agentId);

        if(StringUtils.isNoneBlank(toParentUserId)){
            ArrayList toParentArr = new ArrayList<>();
            toParentArr.add(toParentUserId);
            postJson.put("to_parent_userid",toParentArr);
        }
        if(StringUtils.isNoneBlank(toStudentUserId)){
            ArrayList toStudentArr = new ArrayList<>();
            toStudentArr.add(toStudentUserId);
            postJson.put("to_student_userid",toStudentArr);
        }

        JSONObject testJson =  new JSONObject();
        testJson.put("content",text);
        postJson.put("text",testJson);

        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;
    }

    public Map sendExtContactMessageImage(String corpId,String toParentUserId,String toStudentUserId,String mediaId){

        String accessToken = this.getAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactMessageSendUrl(),accessToken) ;

        //获取企业的agentid
        QywxInnerCompany company =  qywxInnerCompanyService.getCompanyByCorpId(corpId);
        Integer agentId = company.getAgentId();

        JSONObject postJson = new JSONObject();
        postJson.put("msgtype","image");
        postJson.put("agentid",agentId);
        if(StringUtils.isNoneBlank(toParentUserId)){
            ArrayList toParentArr = new ArrayList<>();
            toParentArr.add(toParentUserId);
            postJson.put("to_parent_userid",toParentArr);
        }
        if(StringUtils.isNoneBlank(toStudentUserId)){
            ArrayList toStudentArr = new ArrayList<>();
            toStudentArr.add(toStudentUserId);
            postJson.put("to_student_userid",toStudentArr);
        }

        JSONObject testJson =  new JSONObject();
        testJson.put("media_id",mediaId);
        postJson.put("image",testJson);
        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;
    }


    //******************************  小程序应用   *********************//
    public Map getCode2sessionUser(String corpId,String code){
        String acessToken = getAccessToken(corpId);
        //获取访问用户身份
//        Map paramsMap = new HashMap();
//        paramsMap.put("suite_access_token",accessToken);
//        paramsMap.put("code",code);
        String url = String.format(qywxInnerConfig.getCode2sessionUrl(),acessToken,code);
        Map response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;
    }

}
