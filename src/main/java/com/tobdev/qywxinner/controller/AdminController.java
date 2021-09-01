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
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
public class AdminController {


    @Autowired
    private QywxInnerService qywxInnerService;

    @RequestMapping("/admin/oauthUrl")
    public JsonData oauth(HttpServletRequest request, @RequestParam("corp_id") String corpId,@RequestParam("oauth_callback") String oauthCallback) throws IOException {
        Map resData = new HashMap();
        String oauthRedirectUrl =  URLEncoder.encode(oauthCallback,"utf-8");
        String oauthUrl = qywxInnerService.getSsoUrl(corpId,oauthRedirectUrl);
        resData.put("oauth_url",oauthUrl);
        return  JsonData.buildSuccess(resData);

    }

    @RequestMapping("/admin/oauthUser")
    public JsonData oauthCallback(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam("code") String code) throws IOException {

        //通过code获取信息
        Map result = qywxInnerService.getLoginInfo(corpId,code);
        //查数据库获取人员

        //人员已侦破产生token登录  //本案例仅从企业微信接口获取未从数据表中获取
        QywxInnerUser user = new QywxInnerUser();
        user.setCorpId((String) result.get("corpid"));
        user.setUserId((String) result.get("userid"));
        user.setUserType(0);
        user.setName((String) result.get("name"));
        user.setAvatar((String) result.get("avatar"));
        String token=  JWTUtils.geneJsonWebToken(user);

//        result.put("token",token);

        result.put("token",token);
        return  JsonData.buildSuccess(result);

    }


}
