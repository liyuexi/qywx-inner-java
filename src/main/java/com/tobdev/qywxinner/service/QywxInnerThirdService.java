package com.tobdev.qywxinner.service;

import com.alibaba.fastjson.JSONObject;
import com.tobdev.qywxinner.config.QywxCacheConfig;
import com.tobdev.qywxinner.config.QywxInnerConfig;
import com.tobdev.qywxinner.model.entity.QywxInnerCompany;
import com.tobdev.qywxinner.model.entity.QywxInnerUser;
import com.tobdev.qywxinner.qywxdecode.AesException;
import com.tobdev.qywxinner.qywxdecode.WXBizMsgCrypt;
import com.tobdev.qywxinner.utils.RedisUtils;
import com.tobdev.qywxinner.utils.RestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.List;
import java.util.Map;

@Service
public class QywxInnerThirdService {

    @Autowired
    private QywxInnerCompanyService qywxInnerCompanyService;
    @Autowired
    private QywxInnerUserService qywxInnerUserService;
    @Autowired
    private QywxInnerConfig qywxInnerConfig;
    @Autowired
    private QywxCacheConfig qywxCacheConfig;
    @Autowired
    private RedisUtils strRedis;

    private final static Logger logger = LoggerFactory.getLogger("test");

    //********************************** 回调处理   *************************//
    /**
     * 回调url验证 get请求
     * @param sVerifyMsgSig
     * @param sVerifyTimeStamp
     * @param sVerifyNonce
     * @param sVerifyEchoStr
     * @return
     */
    public String verify(String sVerifyMsgSig,String sVerifyTimeStamp,
                         String sVerifyNonce,String sVerifyEchoStr){

        String sToken = qywxInnerConfig.getToken();
        String sCorpID =  qywxInnerConfig.getCorpId();
        String sEncodingAESKey = qywxInnerConfig.getEncodingAESKey();

        WXBizMsgCrypt wxcpt = null;
        try {
            wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        }catch (AesException E){
            return "error";
        }

		/*
		------------使用示例一：验证回调URL---------------
		*企业开启回调模式时，企业微信会向验证url发送一个get请求
		假设点击验证时，企业收到类似请求：
		* GET /cgi-bin/wxpush?msg_signature=5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3&timestamp=1409659589&nonce=263014780&echostr=P9nAzCzyDtyTWESHep1vC5X9xho%2FqYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp%2B4RPcs8TgAE7OaBO%2BFZXvnaqQ%3D%3D
		* HTTP/1.1 Host: qy.weixin.qq.com

		接收到该请求时，企业应		1.解析出Get请求的参数，包括消息体签名(msg_signature)，时间戳(timestamp)，随机数字串(nonce)以及企业微信推送过来的随机加密字符串(echostr),
		这一步注意作URL解码。
		2.验证消息体签名的正确性
		3. 解密出echostr原文，将原文当作Get请求的response，返回给企业微信
		第2，3步可以用企业微信提供的库函数VerifyURL来实现。

		*/
        String sEchoStr; //需要返回的明文
        try {
            sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp,
                    sVerifyNonce, sVerifyEchoStr);
            //System.out.println("verifyurl echostr: " + sEchoStr);
            // 验证URL成功，将sEchoStr返回
            // HttpUtils.SetResponse(sEchoStr);
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
            return "error";
        }
        return  sEchoStr;

    }

    /**
     * 回调接收 post请求处理
     * @param sVerifyMsgSig
     * @param sVerifyTimeStamp
     * @param sVerifyNonce
     * @param sData
     * @return
     */
    public  String callback(String sVerifyMsgSig,String sVerifyTimeStamp,String sVerifyNonce,String sData){


        //处理回调

        String sToken = qywxInnerConfig.getToken();
        String sSuiteid =qywxInnerConfig.getSuiteId();
        String sEncodingAESKey = qywxInnerConfig.getEncodingAESKey();


        String result = "error";
        WXBizMsgCrypt wxcpt = null;
        try {
            wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sSuiteid);
        }catch (AesException E){
            return result;
        }
        try{
            String sMsg = wxcpt.DecryptMsg(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sData);
            System.out.println("after encrypt sEncrytMsg: " + sMsg);
            // 加密成功
            // TODO: 解析出明文xml标签的内容进行处理
            // For example:
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            StringReader sr = new StringReader(sMsg);
            InputSource is = new InputSource(sr);
            Document document = db.parse(is);

            Element root = document.getDocumentElement();
            NodeList infoTypeNode = root.getElementsByTagName("InfoType");
            String infoType = infoTypeNode.item(0).getTextContent();
            System.out.print(infoType);
            switch (infoType){
                case "suite_ticket" :
                    setSuitTicket(root);

                    break;
                case "create_auth":
                    //获取auth_code
                    NodeList authcodeNode = root.getElementsByTagName("AuthCode");
                    String authcode = authcodeNode.item(0).getTextContent();
                     logger.info("auth code:"+authcode);
                    getPermentCode(authcode);
                    ;
                    break;
                case "change_auth":
                    ;
                    break;
                case "cancel_auth":
                    //获取corp_id
                    NodeList authCorpNode = root.getElementsByTagName("AuthCorpId");
                    String corpId = authCorpNode.item(0).getTextContent();
                    deleteCompany(corpId);
                    ;
                    break;
                default:
                    // logger.info(infoType);
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
            // 加密失败
            return result;
        }
        result = "success";
        return  result;

    }

    public Boolean getPermentCode(String authCode){
        //通过auth code获取公司信息及永久授权码

        JSONObject postJson = new JSONObject();
        postJson.put("auth_code",authCode);
        logger.error(postJson.toString());
        String url = String.format(qywxInnerConfig.getPermanentCodeUrl(),getSuiteToken());
        Map response = RestUtils.post(url,postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
            return  false;
        }
        logger.error(response.toString());

        //保存授权公司信息
        //https://open.work.weixin.qq.com/api/doc/90001/90143/90604
        /**
         "auth_corp_info":
         {
         "corpid": "xxxx",
         "corp_name": "name",
         "corp_type": "verified",
         "corp_square_logo_url": "yyyyy",
         "corp_user_max": 50,
         "corp_agent_max": 30,
         "corp_full_name":"full_name",
         "verified_end_time":1431775834,
         "subject_type": 1,
         "corp_wxqrcode": "zzzzz",
         "corp_scale": "1-50人",
         "corp_industry": "IT服务",
         "corp_sub_industry": "计算机软件/硬件/信息服务",
         "location":"广东省广州市"
         },
         "auth_info":
         {
         "agent" :
         [
         {
         "agentid":1,
         "name":"NAME",
         "round_logo_url":"xxxxxx",
         "square_logo_url":"yyyyyy",
         "appid":1,
         "privilege":
         {
         "level":1,
         "allow_party":[1,2,3],
         "allow_user":["zhansan","lisi"],
         "allow_tag":[1,2,3],
         "extra_party":[4,5,6],
         "extra_user":["wangwu"],
         "extra_tag":[4,5,6]
         }
         },
         {
         "agentid":2,
         "name":"NAME2",
         "round_logo_url":"xxxxxx",
         "square_logo_url":"yyyyyy",
         "appid":5
         }
         ]
         }
         * **/
        //获取永久授权码
        String permanenCode= (String) response.get("permanent_code");
        //获取corpId
        Map authCorpInfo =(Map) response.get("auth_corp_info");
        String corpId = (String) authCorpInfo.get("corpid");
        //获取agent
        Map authInfo = (Map) response.get("auth_info");
        List agentList = (List) authInfo.get("agent");
        Map agent = (Map) agentList.get(0);
        String agentId = String.valueOf(agent.get("agentid"));

        QywxInnerCompany company = new QywxInnerCompany();
        company.setAgentSecret(permanenCode);
        company.setCorpId(corpId) ;
        company.setCorpName((String) authCorpInfo.get("corp_name"));
        String fullName = authCorpInfo.get("corp_full_name") ==  null  ? "" :  (String)authCorpInfo.get("corp_full_name");
        company.setCorpFullName(fullName);
        company.setSubjectType((Integer) authCorpInfo.get("subject_type"));
        //设置授权应用id  用于Jssdk agentconfig等使用
        company.setAgentId(agentId);
        company.setStatus(1);
        logger.info(company.toString());
        qywxInnerCompanyService.saveCompany(company);

        //保存授权用户信息
        /**
         "auth_user_info":
         {
         "userid":"aa",
         "name":"xxx",
         "avatar":"http://xxx"
         },
         **/

        QywxInnerUser user = new QywxInnerUser();
        Map authUserInfo = (Map) response.get("auth_user_info");
        user.setUserId((String)authUserInfo.get("userid"));
        user.setName((String)authUserInfo.get("name"));
        user.setAvatar((String)authUserInfo.get("avatar"));
        user.setCorpId(corpId);
        user.setStatus(1);
        logger.info(user.toString());
        qywxInnerUserService.saveUser(user);

        //异步同步部门，人员 待处理
        return true;

    }

    private boolean deleteCompany(String corpId){
        return  qywxInnerCompanyService.deleteCompanyByCorpId(corpId) ;
    }



    //suite_ticket缓存
    private String setSuitTicket(Element root){
        NodeList nodelist = root.getElementsByTagName("SuiteTicket");
        String result = nodelist.item(0).getTextContent();
        logger.info("set"+qywxCacheConfig.getSuitTicket());
        strRedis.set(qywxCacheConfig.getSuitTicket(),result,600);
        return result;
    }

    //suite_ticket获取
    public String getSuitTicket(){
        String result;
        result = (String) strRedis.get(qywxCacheConfig.getSuitTicket());
        logger.info("get:"+qywxCacheConfig.getSuitTicket()+":"+result);
        if(result==""){
            logger.error("suit_ticket为空");
        }
        return result;
    }

    public String  getSuiteToken(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        //方式一直接json字符串
//        String postStr = "{\n" +
//                "    \"suite_id\":\"xxxx\" ,\n" +
//                "    \"suite_secret\": \"xxxx\", \n" +
//                "    \"suite_ticket\": \"xxxx\" \n" +
//                "}";
//        HttpEntity request = new HttpEntity(postStr,headers);
        //方式二 对象转字符串j
        String  suiteTicket = getSuitTicket();
        JSONObject postJson = new JSONObject();
        postJson.put("suite_id",qywxInnerConfig.getSuiteId());
        postJson.put("suite_secret",qywxInnerConfig.getSuiteSecret());
        postJson.put("suite_ticket",suiteTicket);
        Map response = RestUtils.post(qywxInnerConfig.getSuiteTokenUrl(),postJson);
        //获取错误日志
        if(response.containsKey("errcode") && (Integer) response.get("errcode") != 0){
            logger.error(response.toString());
        }
        String result = (String) response.get("suite_access_token");
        return result;
    }


}
