package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.service.QywxInnerThirdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class CallbackController {

    @Autowired
    private QywxInnerService qywxInnerService;

    @GetMapping("/callback/company")
    @ResponseBody()
    public String agentGetCallback(HttpServletRequest request ,@RequestParam("xx") String  xx,@RequestParam("corp_id") String  corpId,@RequestParam("msg_signature") String sVerifyMsgSig,@RequestParam("timestamp") String sVerifyTimeStamp,@RequestParam("nonce") String sVerifyNonce,@RequestParam("echostr") String sVerifyEchoStr) throws Exception{
        System.out.print("回调开始");
        System.out.print(xx);
        System.out.print(corpId);
        System.out.print(sVerifyMsgSig);
        System.out.print(sVerifyTimeStamp);
        System.out.print(sVerifyNonce);
        System.out.print("get回调");
        return  qywxInnerService.verify(corpId, sVerifyMsgSig, sVerifyTimeStamp,
                sVerifyNonce, sVerifyEchoStr);

    }

    @ResponseBody
    @PostMapping("/callback/company")
    String agentPostCallback(
                        @RequestParam("corp_id") String  corpId,
                        @RequestParam(value = "msg_signature") String sVerifyMsgSig,
                        @RequestParam(value = "timestamp") String sVerifyTimeStamp,
                        @RequestParam(value = "nonce") String sVerifyNonce,
                        @RequestBody String body
    ){

        System.out.print("回调开始");
        System.out.print("**************************************自建应用回调开始**************************************");
        System.out.print(corpId);
        System.out.print(sVerifyMsgSig);
        System.out.print(sVerifyTimeStamp);
        System.out.print(sVerifyNonce);
        System.out.print(body);
        System.out.print("post回调");
        String res =   qywxInnerService.callback( sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, body);
        System.out.print("**************************************自建应用回调结束**************************************");
        return  res;
    }


}
