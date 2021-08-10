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

        //家长应用
//        String schoolOauthRedirectUrl = CommonUtils.RouteToUrl(request,"/h5/oauth_callback");
//        String schoolOauthUrl = qywxInnerService.getSchoolOauthUrl(corpId,schoolOauthRedirectUrl);
//        resData.put("school_oauth_url",schoolOauthUrl);

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


    @RequestMapping("/h5/pri/index")
    String home(HttpServletRequest request,ModelMap  model) throws Exception {

        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");

        System.out.println(userId);
        System.out.println(corpId);

        model.put("user_id",userId);
        model.put("user_name",corpId);
//        model.put("mobile",corpId);
//        model.put("qr_code",corpId);


        model.put("access_token",qywxInnerService.getAccessToken(corpId));


        String jssdkUrl = CommonUtils.RouteToUrl(request,"/h5/pri/jssdk");
        model.put("jssdk_url",jssdkUrl);

        String contactUrl = CommonUtils.RouteToUrl(request,"/contact/index");
        model.put("contact_url",contactUrl);

        String extcontactUrl = CommonUtils.RouteToUrl(request,"/extcontact/index");
        model.put("extcontact_url",extcontactUrl);

        String messageUrl = CommonUtils.RouteToUrl(request,"/message/index");
        model.put("message_url",messageUrl);

        String mediaUrl = CommonUtils.RouteToUrl(request,"/media/index");
        model.put("media_url",mediaUrl);

        String oaUrl = CommonUtils.RouteToUrl(request,"/oa/index");
        model.put("oa_url",oaUrl);

        return "h5/pri/index";

    }

    @RequestMapping("/h5/pri/jssdk")
    String test(HttpServletRequest request,ModelMap  model) throws Exception {


        String userId = (String) request.getAttribute("user_id");
        model.addAttribute("userId",userId);

        String  timestamp=""+System.currentTimeMillis();
        //随机字符串
        String nonce = "56565";
        String url = CommonUtils.RouteToUrl(request,"/h5/pri/jssdk");
        String corpId = (String ) request.getAttribute("corp_id");

        Map signConig = qywxInnerService.getJsSign(corpId,nonce,timestamp,url);
        model.addAttribute("signConfig",signConig);

        Map signAgentConig = qywxInnerService.getJsSignAgent(corpId,nonce,timestamp,url);
        System.out.println(signAgentConig);
        model.addAttribute("signAgentConfig",signAgentConig);


//        model.addAttribute("xx","sdffdf");
//        model.addObject("sign",result);
//        model.setViewName("front/test");
        return "h5/pri/jssdk";

    }

    @RequestMapping("/h5/pri/jsSign")
    @ResponseBody()
    public Map getJsSign(HttpServletRequest request,@RequestParam("url") String url) throws Exception{
        //获取当前时间戳
        String  timestamp=""+System.currentTimeMillis();
        //随机字符串
        String nonce = "sdfsdf";
        String corp_id = (String) request.getAttribute("corp_id");

        Map result = qywxInnerService.getJsSign(corp_id,nonce,timestamp,url);

        return  result;
    }


}
