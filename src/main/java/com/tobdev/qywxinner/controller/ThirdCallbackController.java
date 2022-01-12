package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.qywxdecode.AesException;
import com.tobdev.qywxinner.qywxdecode.WXBizMsgCrypt;
import com.tobdev.qywxinner.service.QywxInnerThirdService;
import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.Map;

@RestController
public class ThirdCallbackController {

    @Autowired
    private QywxInnerThirdService qywxInnerThirdService;

    @GetMapping("/callback")
    @ResponseBody()
    public String thirdGetCallback(HttpServletRequest request ,@RequestParam("msg_signature") String sVerifyMsgSig,@RequestParam("timestamp") String sVerifyTimeStamp,@RequestParam("nonce") String sVerifyNonce,@RequestParam("echostr") String sVerifyEchoStr) throws Exception{

        return  qywxInnerThirdService.verify( sVerifyMsgSig, sVerifyTimeStamp,
                 sVerifyNonce, sVerifyEchoStr);

    }

    @ResponseBody
    @PostMapping("/callback")
    String thirdPostCallback(@RequestParam(value = "msg_signature") String sVerifyMsgSig,
                        @RequestParam(value = "timestamp") String sVerifyTimeStamp,
                        @RequestParam(value = "nonce") String sVerifyNonce,
                        @RequestBody String body
    ){

        System.out.print("回调开始");
        System.out.print("**************************************找开发模板回调开始**************************************");
        System.out.print(sVerifyMsgSig);
        System.out.print(sVerifyTimeStamp);
        System.out.print(sVerifyNonce);
        System.out.print(body);
        System.out.print("post回调");
        String res =  qywxInnerThirdService.callback( sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, body);
        System.out.print("**************************************找开发模板回调结束**************************************");
        return res;
    }



}
