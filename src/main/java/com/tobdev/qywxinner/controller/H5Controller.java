package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.model.entity.QywxInnerUser;
import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.CommonUtils;
import com.tobdev.qywxinner.utils.CookieUtils;
import com.tobdev.qywxinner.utils.JWTUtils;
import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * H5应用专用
 */
@RestController
public class H5Controller {

    @Autowired
    private QywxInnerService qywxInnerService;



    @RequestMapping({"/h5/oauthUrl"})
    JsonData oauthUrl(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam("oauth_callback") String oauthCallback) throws UnsupportedEncodingException {
        Map resData = new HashMap();

        //普通应用
        String oauthRedirectUrl =  URLEncoder.encode(oauthCallback,"utf-8");
        String oauthUrl = qywxInnerService.getOauthUrl(corpId,oauthRedirectUrl);
        resData.put("oauth_url",oauthUrl);
        return  JsonData.buildSuccess(resData);
    }


    @RequestMapping("/h5/oauthUser")
    JsonData  oauthCallback(HttpServletRequest request,HttpServletResponse response,@RequestParam("corp_id") String corpId,@RequestParam("code") String code) throws IOException {

        //通过code获取信息
        Map result = qywxInnerService.getOauthUser(corpId,code);
        //查数据库获取人员


        //人员已侦破产生token登录  //本案例仅从企业微信接口获取未从数据表中获取
        QywxInnerUser user = new QywxInnerUser();
        user.setCorpId(corpId);
        user.setUserId((String) result.get("userId"));
//        user.setUserId((String) result.get("userid"));
//        user.setUserType(0);
//        user.setName((String) result.get("name"));
//        user.setAvatar((String) result.get("avatar"));
//        user.setMobile((String) result.get("mobile"));
//        user.setQrCode((String) result.get("qr_code"));
        String token=  JWTUtils.geneJsonWebToken(user);

        result.put("token",token);

        return  JsonData.buildSuccess(result);
    }


    @RequestMapping("/h5/jsAgentSign")
    JsonData getJsAgentSign(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam("url") String url) throws Exception {

        String  timestamp=""+System.currentTimeMillis();
        //随机字符串
        String nonce = "56565";

        Map resData = qywxInnerService.getJsSignAgent(corpId,nonce,timestamp,url);


        return  JsonData.buildSuccess(resData);

    }

    @RequestMapping("/h5/jsSign")
    public JsonData getJsSign(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam("url") String url) throws Exception{
        //获取当前时间戳
        String  timestamp=""+System.currentTimeMillis();
        //随机字符串
        String nonce = "sdfsdf";

        Map resData = qywxInnerService.getJsSign(corpId,nonce,timestamp,url);

        return  JsonData.buildSuccess(resData);
    }


}
