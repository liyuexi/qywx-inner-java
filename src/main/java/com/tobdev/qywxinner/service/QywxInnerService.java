package com.tobdev.qywxinner.service;

import com.tobdev.qywxinner.config.QywxCacheConfig;
import com.tobdev.qywxinner.model.entity.QywxInnerCompany;
import com.tobdev.qywxinner.utils.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class QywxInnerService {

    private final static Logger logger = LoggerFactory.getLogger("test");

    @Autowired
    private QywxInnerService qywxInnerConfig;
    @Autowired
    private QywxCacheConfig qywxCacheConfig;

    @Autowired
    private RedisUtils strRedis;


    public String getCorpAccessToken(String corpId){
        
        String result = "";
        QywxInnerCompany company;
        QywxInnerCompany getRs = this.getCompanyByCorpId(corpId);
        if(getRs!=null){
            company = getRs;
        }else{
            logger.info("无授权公司信息");
            return  "";
        }

        logger.info(company.toString());
        String  suiteToken = qywxInnerService.getSuiteToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        JSONObject postJson = new JSONObject();
        postJson.put("auth_corpid",company.getCorpId());
        postJson.put("permanent_code",company.getPermanentCode());

        String  corpTokenUrl =  String.format(qywxThirdConfig.getCorpTokenUrl(),suiteToken);
        Map response = RestUtils.post(corpTokenUrl,postJson );
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }else{
            result = (String) response.get("access_token");
        }

        return result;

    }



    public Map getDepartmentList(String corpId){

        String corpToken = getCorpAccessToken(corpId);
//        if(id != "0"){
//            paramsMap.put("id",id);
//        }
        String url = String.format(qywxInnerConfig.getDepartmentUrl(),corpToken);
        Map response = RestUtils.get(url);
        //获取错误日志
        logger.error(response.toString());
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return response;
    }


    public Map getUserSimplelist(String corpId,String id,String fetch_child){

        String corpToken = getCorpAccessToken(corpId);
//        Map paramsMap = new HashMap();
//        paramsMap.put("access_token",corpToken);
//        paramsMap.put("department_id",id);
//        paramsMap.put("fetch_child",fetch_child);
        String url =String.format(qywxInnerConfig.getUserSimplelistUrl(),corpToken,id,fetch_child);
        Map response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return response;
    }

    public HashMap getJsSign(String corpId,String nonce, String timestamp,  String signUrl) throws  Exception{


        //获取jsticket
        String corpToken = getCorpAccessToken(corpId);

//        Map paramsMap = new HashMap();
//        paramsMap.put("access_token",corpToken);
        String url = String.format(qywxInnerConfig.getJsapiTicketUrl(),corpToken);
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

    //**********************************  客户联系相关   *************************//
    //获取配置了客户联系功能的成员列表
    public Map getExtContactFollowUserList(String corpId){
        String corpToken = this.getCorpAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactFollowUserListUrl(),corpToken) ;
        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    //获取指定成员添加的客户列表
    public Map getExtContactList(String corpId,String userId){

        String corpToken = this.getCorpAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactListUrl(),corpToken,userId) ;

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
        String corpToken = this.getCorpAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactGroupchatUrl(),corpToken) ;

        //分页，预期请求的数据量，取值范围 1 ~ 1000  测试写死1000
        JSONObject postJson = new JSONObject();
        //owner_filter  userid_list	否	用户ID列表。最多100个
        postJson.put("limit",1000);
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
        String corpToken = this.getCorpAccessToken(corpId);

        String url = String.format(qywxInnerConfig.getMessageSendUrl(),corpToken) ;
        //获取企业的agentid
        QywxInnerCompany company =  this.getCompanyByCorpId(corpId);
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
    public  byte[] downloadMedia(String corpId,String mediaId){
        String corpToken = this.getCorpAccessToken(corpId);
        String mediaDownloadUrl = String.format(qywxInnerConfig.getMediaGetUrl(),corpToken,mediaId);
        return RestUtils.dowload(mediaDownloadUrl);

    }

    //**********************************  oa审批相关   *************************//

    //审批流程引擎
    public Map getApprovalFlow(){
        Map approval = new HashMap();
        approval.put("templateId",qywxInnerConfig.getApprovalFlowId());
        approval.put("thirdNo",new SnowFlakeUtils(0, 0).createOrderNo());
        return approval;
    }

    public Map getApprovalFlowStatus(String corpId,String thirdNo){
        String corpToken = this.getCorpAccessToken(corpId);
        String approvalUrl = String.format(qywxInnerConfig.getOpenApprovalDataUrl(),corpToken);
        JSONObject postJson = new JSONObject();
        postJson.put("thirdNo",thirdNo);
        return RestUtils.post(approvalUrl,postJson);
    }

    //**********************************  PC相关   *************************//
    //PC网页 sso  用于非企业微信环境下扫码登录，如运行在浏览的器应用后台或者脱离企业微信环境下H5应用
    //https://open.work.weixin.qq.com/wwopen/sso/3rd_qrConnect?appid=ww100000a5f2191&redirect_uri=http%3A%2F%2Fwww.oa.com&state=web_login@gyoss9&usertype=admin
    public  String getSsoUrl(String redirectUrl,String userType){
        String state = "test";
        String ssoUrl = String.format(qywxInnerConfig.getSsoAuthUrl(),qywxInnerConfig.getCorpId(),redirectUrl,state,userType);
        return ssoUrl;
    }

    public Map getLoginInfo(String authCode){

        String url = String.format(qywxInnerConfig.getLoginInfoUrl(),getProviderToken()) ;

        JSONObject postJson = new JSONObject();
        postJson.put("auth_code",authCode);
        JSONObject response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }


    //********************************** H5应用 Oauth   *************************//
    public String getOauthUrl(String url){
//        应用授权作用域。
//        snsapi_base：静默授权，可获取成员的基础信息（UserId与DeviceId）；
//        snsapi_userinfo：静默授权，可获取成员的详细信息，但不包含手机、邮箱等敏感信息；
//        snsapi_privateinfo：手动授权，可获取成员的详细信息，包含手机、邮箱等敏感信息（已不再支持获取手机号/邮箱）。
        String scope = "snsapi_userinfo";
        String state = "sdfds343";
        String result = String.format(qywxInnerConfig.getOauthUrl(),qywxInnerConfig.getSuiteId(),url,scope,state);
        return  result;
    }

    public Map getOauthUser(String code) {

        String suiteToken = getSuiteToken();

        //获取访问用户身份

//        方法一
//        使用此url的suiteToken带了-_请求通过 java.lang.IllegalArgumentException: Not enough variable values available to expand 'suite_access_token'
//        https://blog.csdn.net/xs_challenge/article/details/109451263

//        Map<String,String> paramsMap = new HashMap();
//        String suiteToken = "9EEq7bDkw3zQlHU6CDfBqbjFCYHYt1O6OmTfKWOEfOvDGLSq-stlraI6rkeGgTFiMhQ8wu24ivyGdTvuuOR1hETOXgSXUoNINqALHF1I4Z3BxfAuagNh_XTGFcRpXnAq";
//        paramsMap.put("suite_access_token",suiteToken);
//        paramsMap.put("code",code);
//        Map response = qywxInnerHttpClient.getForObject(qywxInnerConfig.getOauthUserUrl(),Map.class,paramsMap);

//        方法二
//        String getOauthUrl = String.format(qywxInnerConfig.getOauthUserUrl(),suiteToken,code);
//        HttpHeaders httpHeaders = new HttpHeaders();
//        //httpHeaders.set("Cookie", cookies);
//        httpHeaders.set("Content-Type", "application/json; charset=utf-8");
//        URI uri = null;
//        try {
//            uri = new URI(getOauthUrl);
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//            return null;
//        }
//        logger.info(getOauthUrl);
//        Map  response = qywxInnerHttpClient.exchange(URI.create(getOauthUrl), HttpMethod.GET,new HttpEntity<>(httpHeaders),Map.class).getBody();
//

        //方法三
        String getOauthUrl = String.format(qywxInnerConfig.getOauthUserUrl(),suiteToken,code);
        Map  response = RestUtils.get(getOauthUrl);
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
            return  response;
        }

        String userTicket = (String) response.get("user_ticket");
        //获取访问用户敏感信息
        JSONObject postJson = new JSONObject();
        postJson.put("user_ticket",userTicket);
        String url = String.format(qywxInnerConfig.getOauthUserDetailUrl(),suiteToken);
        Map detaiResponse = RestUtils.post(url,postJson);
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



    public Map  getJsSignAgent(String corpId,String nonce, String timestamp,  String signUrl) throws  Exception{
        //https://work.weixin.qq.com/api/doc/90001/90144/90548
        //https://work.weixin.qq.com/api/doc/90001/90144/90539#%E8%8E%B7%E5%8F%96%E5%BA%94%E7%94%A8%E7%9A%84jsapi_ticket

        //获取jsticket
        String corpToken = this.getCorpAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getJsapiTicketAgentUrl(),corpToken);
        Map response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        String jsapiTicket = (String) response.get("ticket");
        //通过 获取jsticket  获取签名
        String sign = QywxSHA.getSHA1(jsapiTicket,nonce,timestamp,signUrl);

        //获取appid
        QywxInnerCompany company =  this.getCompanyByCorpId(corpId);
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
    public  String getSchoolOauthUrl(String url){

//        应用授权作用域。
//        snsapi_base：静默授权，可获取成员的基础信息（UserId与DeviceId）；
//        snsapi_userinfo：静默授权，可获取成员的详细信息，但不包含手机、邮箱等敏感信息；
//        snsapi_privateinfo：手动授权，可获取成员的详细信息，包含手机、邮箱等敏感信息（已不再支持获取手机号/邮箱）。
        String scope = "snsapi_userinfo";
        String state = "sdfds343";
        String result = String.format(qywxInnerConfig.getSchoolOauthUrl(),qywxInnerConfig.getSuiteId(),url,scope,state);
        return  result;
    }

    public Map getSchoolOauthUser(String code) {

        String suiteToken = getSuiteToken();
        //获取访问用户身份
        String getOauthUrl = String.format(qywxInnerConfig.getSchoolOauthUserUrl(),suiteToken,code);
        Map  response = RestUtils.get(getOauthUrl);
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
            return  response;
        }

        Map detaiResponse = new HashMap();
        //家长
        if(response.containsKey("external_userid")){


            //https://work.weixin.qq.com/api/doc/90001/90143/91711

            String coprId = (String) response.get("CorpId");
            String corpToken = this.getCorpAccessToken(coprId);

            ArrayList parentsArray = (ArrayList) response.get("parents");
            if(parentsArray.size()<0){
                JsonData.buildError("无家长信息");
            }
            HashMap parentsMap = (HashMap) parentsArray.get(0);
            String userId = (String) parentsMap.get("parent_userid");
            //获取访问用户敏感信息
            //https://work.weixin.qq.com/api/doc/90001/90143/92038
            String url = String.format(qywxInnerConfig.getSchoolUserGetUrl(),corpToken,userId);
            detaiResponse = RestUtils.get(url);
            //获取错误日志
            if(detaiResponse.containsKey("errcode") && (Integer) detaiResponse.get("errcode") != 0){
                logger.error(detaiResponse.toString());
            }

        }else{
            //公司成员
            String userTicket = (String) response.get("user_ticket");
            //获取访问用户敏感信息
            JSONObject postJson = new JSONObject();
            postJson.put("user_ticket",userTicket);
            String url = String.format(qywxInnerConfig.getOauthUserDetailUrl(),suiteToken);
            detaiResponse = RestUtils.post(url,postJson);
            //获取错误日志
            if(detaiResponse.containsKey("errcode") && (Integer) detaiResponse.get("errcode") != 0){
                logger.error(detaiResponse.toString());
            }
            detaiResponse.put("user_type",0);
        }



        /**
         * 家长
         {
         "errcode": 0,
         "errmsg": "ok",
         "user_type": 1,
         "student":{
         "student_userid": "zhangsan",
         "name": "张三",
         "department": [1, 2],
         "parents":[
         {
         "parent_userid": "zhangsan_parent1",
         "relation": "爸爸",
         "mobile":"18000000000",
         "is_subscribe": 1,
         "external_userid":"xxxxx"
         },
         {
         "parent_userid": "zhangsan_parent2",
         "relation": "妈妈",
         "mobile": "18000000001",
         "is_subscribe": 0
         }
         ]
         },
         "parent":{
         "parent_userid": "zhangsan_parent2",
         "mobile": "18000000003",
         "is_subscribe": 1,
         "external_userid":"xxxxx",
         "children":[
         {
         "student_userid": "zhangsan",
         "relation": "妈妈"
         },
         {
         "student_userid": "lisi",
         "relation": "伯母"
         }
         ]
         }
         }
         */

        return detaiResponse;

    }

    public Map getSchoolDepartmentList(String corpId,String deptId){
        String corpToken = this.getCorpAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getSchoolDepartmentListUrl(),corpToken,deptId) ;

        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;
    }

    public Map getSchoolUserList(String corpId,String deptId,String fetchChild){
        String corpToken = this.getCorpAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getSchoolUserListUrl(),corpToken,deptId,fetchChild) ;

        JSONObject response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;

    }

    public Map extContactMessageSend(String corpId,String studentUserid,String text){

        String corpToken = this.getCorpAccessToken(corpId);
        String url = String.format(qywxInnerConfig.getExtContactMessageSendUrl(),corpToken) ;

        //获取企业的agentid
        QywxInnerCompany company =  this.getCompanyByCorpId(corpId);
        Integer agentId = company.getAgentId();

        JSONObject postJson = new JSONObject();
        postJson.put("msgtype","text");
        postJson.put("agentid",agentId);
        postJson.put("to_student_userid",studentUserid);

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

    //******************************  小程序应用   *********************//
    public Map getCode2sessionUser(String code){
        String suiteToken = getSuiteToken();
        //获取访问用户身份
//        Map paramsMap = new HashMap();
//        paramsMap.put("suite_access_token",suiteToken);
//        paramsMap.put("code",code);
        String url = String.format(qywxInnerConfig.getCode2sessionUrl(),suiteToken,code);
        Map response = RestUtils.get(url);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        return  response;
    }
    
}
