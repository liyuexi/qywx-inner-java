package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.qywxdecode.WXBizMsgCrypt;
import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class CallBackController {

    @GetMapping("/callback")
    @ResponseBody()
    public String callback(HttpServletRequest request ,@RequestParam("msg_signature") String sVerifyMsgSig,@RequestParam("timestamp") String sVerifyTimeStamp,@RequestParam("nonce") String sVerifyNonce,@RequestParam("echostr") String sVerifyEchoStr) throws Exception{

        String sToken = "Q03VjTIzuggUj9eRjk29r69Q7u";
        String sCorpID = "wwb2cf5b301c49f2ed";
        String sEncodingAESKey = "urbuWUQvXJbpTSMdCjMkjXuHJIxwQym0buUukv8gk7E";

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
}
