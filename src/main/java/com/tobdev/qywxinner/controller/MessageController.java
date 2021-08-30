package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.qywxdecode.WXBizMsgCrypt;
import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.CommonUtils;
import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {

    @Autowired
    private QywxInnerService qywxInnerService;


    @PostMapping("/message/sendtext")
    @ResponseBody()
    public JsonData sendtext(HttpServletRequest request, @RequestBody Map map ) throws Exception{

        String corpId = (String) map.get("corp_id");
        String toUserId =  (String)map.get("to_user_id");
        String text =  (String)map.get("text");
        Map resData = qywxInnerService.sendMessageText(corpId,toUserId,text);
        return  JsonData.buildSuccess(resData);

    }



    @GetMapping("/message/callback")
    @ResponseBody()
    public String callback(HttpServletRequest request ,@RequestParam("msg_signature") String sVerifyMsgSig,@RequestParam("timestamp") String sVerifyTimeStamp,@RequestParam("nonce") String sVerifyNonce,@RequestParam("echostr") String sVerifyEchoStr) throws Exception{

        String sToken = "3AUH";
        String sCorpID = "wwe58c8eb857ded23d";
        String sEncodingAESKey = "o0w3fGDUjYTKjK6I8ek9ZPvpCiEzAiYxV7JA8D3KvNl";

        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
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
            return sEchoStr;
            // 验证URL成功，将sEchoStr返回
            // HttpUtils.SetResponse(sEchoStr);
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }
        return "error";
    }


    @PostMapping("/message/callback")
    @ResponseBody()
    public String callbackData(HttpServletRequest request ,@RequestBody() String sRespData,@RequestParam("msg_signature") String sVerifyMsgSig,@RequestParam("timestamp") String sReqTimeStamp,@RequestParam("nonce") String sReqNonce) throws Exception {

        String sToken = "3AUH";
        String sCorpID = "wwe58c8eb857ded23d";
        String sEncodingAESKey = "o0w3fGDUjYTKjK6I8ek9ZPvpCiEzAiYxV7JA8D3KvNl";

        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(sToken, sEncodingAESKey, sCorpID);
        String sMsg = wxcpt.DecryptMsg(sVerifyMsgSig, sReqTimeStamp, sReqNonce, sRespData);
        System.out.println("after decrypt msg: " + sMsg);
        // TODO: 解析出明文xml标签的内容进行处理
        // For example:
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        StringReader sr = new StringReader(sMsg);
        InputSource is = new InputSource(sr);
        Document document = db.parse(is);

        Element root = document.getDocumentElement();
        NodeList nodelist1 = root.getElementsByTagName("Content");
        String Content = nodelist1.item(0).getTextContent();
        System.out.println("Content：" + Content);

        NodeList nodelist2 = root.getElementsByTagName("FromUserName");
        String fromUserName = nodelist2.item(0).getTextContent();



        String data = "<xml><ToUserName><![CDATA["+fromUserName +"]]></ToUserName><FromUserName><![CDATA[wwe58c8eb857ded23d]]></FromUserName><CreateTime>"+sReqTimeStamp+"</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[this is a test]]></Content></xml>";
        try{
            String sEncryptMsg = wxcpt.EncryptMsg(data, sReqTimeStamp, sReqNonce);
           return  sEncryptMsg;
            // 加密成功
            // TODO:
            // HttpUtils.SetResponse(sEncryptMsg);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            // 加密失败
        }


        return  "xxxx";

    }






    }
