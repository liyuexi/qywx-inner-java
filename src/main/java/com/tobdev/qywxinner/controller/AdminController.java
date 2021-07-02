package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.model.entity.QywxInnerUser;
import com.tobdev.qywxinner.service.QywxInnerService;

import com.tobdev.qywxinner.utils.CommonUtils;
import com.tobdev.qywxinner.utils.CookieUtils;
import com.tobdev.qywxinner.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class AdminController {


    @Autowired
    private QywxInnerService qywxInnerService;

    @RequestMapping("/admin/index")
    String admin(HttpServletRequest request, ModelMap model){
        String corpId = (String) request.getAttribute("corp_id");

        String oauthRedirectUrl = CommonUtils.RouteToUrl(request,"/admin/oauth");
        String oauthUrl = qywxInnerService.getSsoUrl(corpId,oauthRedirectUrl);
        model.put("oauth_url",oauthUrl);
        return  "admin/index";

    }

    @RequestMapping("/admin/oauth")
    public void oauth(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String corpId = (String) request.getAttribute("corp_id");
        String oauthRedirectUrl = CommonUtils.RouteToUrl(request,"/admin/oauth_callback");
        String oauthUrl = qywxInnerService.getOauthUrl(corpId,oauthRedirectUrl);
        // return  new ModelAndView(new RedirectView(oauthUrl));
        response.sendRedirect(oauthUrl);

    }



    @RequestMapping("/admin/oauth_callback")
    public void oauthCallback(HttpServletRequest request,HttpServletResponse response,@RequestParam("code") String code) throws IOException {
        String corpId = (String) request.getAttribute("corp_id");

        //通过code获取信息
        Map result = qywxInnerService.getOauthUser(corpId,code);
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

        //本案例写入cookie并跳转
        CookieUtils.setCookie(response,"token",token,24*60*60);

        String priIndexUrl = CommonUtils.RouteToUrl(request,"/h5/pri/index");
        response.sendRedirect(priIndexUrl);

    }


    @RequestMapping("/admin/pri/index")
    String index(HttpServletRequest request, ModelMap model){
        //获取身份
        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");

        System.out.println(userId);
        System.out.println(corpId);

        model.put("user_id",userId);
        model.put("corp_id",corpId);


        model.put("access_token",qywxInnerService.getAccessToken(corpId));

        String contactUrl = CommonUtils.RouteToUrl(request,"/contact/index");
        model.put("contact_url",contactUrl);

        String extcontactUrl = CommonUtils.RouteToUrl(request,"/extcontact/index");
        model.put("extcontact_url",extcontactUrl);

        String messageUrl = CommonUtils.RouteToUrl(request,"/message/index");
        model.put("message_url",messageUrl);

        String mediaUrl = CommonUtils.RouteToUrl(request,"/media/index");
        model.put("media_url",mediaUrl);

        return  "admin/pri/index";

    }



}
