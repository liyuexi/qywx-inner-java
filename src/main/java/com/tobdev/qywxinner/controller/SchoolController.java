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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(produces = "application/json; charset=utf-8")
public class SchoolController {

    @Autowired
    private QywxInnerService qywxInnerService;


    @RequestMapping({"/school/oauthUrl"})
    JsonData oauthUrl(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam("oauth_callback") String oauthCallback) throws UnsupportedEncodingException {
        Map resData = new HashMap();

        String oauthRedirectUrl =  URLEncoder.encode(oauthCallback,"utf-8");
        String schoolOauthUrl = qywxInnerService.getSchoolOauthUrl(corpId,oauthRedirectUrl);
        resData.put("oauth_url",schoolOauthUrl);
        return  JsonData.buildSuccess(resData);

    }

    @RequestMapping("/school/oauthUser")
    @ResponseBody
    JsonData  oauthCallback(HttpServletRequest request,HttpServletResponse response,@RequestParam("corp_id") String corpId,@RequestParam("code") String code) throws IOException {

        //通过code获取信息
        Map result = qywxInnerService.getSchoolOauthUser(corpId,code);
//        Map result = qywxInnerService.getSchoolOauthUser(code);


//        System.out.println(result);
//        //查数据库获取人员
//        //人员已侦破产生token登录  //本案例仅从企业微信接口获取未从数据表中获取
//        QywxInnerUser user = new QywxInnerUser();
//        user.setCorpId((String) result.get("corpid"));
//        user.setUserId((String) result.get("userid"));
//        user.setUserType((Integer) result.get("user_Type"));
//        user.setName((String) result.get("name"));
//        user.setAvatar((String) result.get("avatar"));
//        String token=  JWTUtils.geneJsonWebToken(user);
//
//
//
//        //本案例写入cookie并跳转
//        CookieUtils.setCookie(response,"token",token,24*60*60);
//
//        String priIndexUrl = CommonUtils.RouteToUrl(request,"/school/index");
//        response.sendRedirect(priIndexUrl);

    }


    @RequestMapping("/school/messageSend")
    @ResponseBody
    public Map messageSend(HttpServletRequest request, HttpServletResponse response){
        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");

        String studentUserid = "90789833e153546ab8dbba1ab0d5f066";
        return  qywxInnerService.extContactMessageSend(corpId,studentUserid,"发送文本测试消息");
    }

    @RequestMapping("/school/department_list")
    @ResponseBody
    public Map departmentList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");


        Map result = qywxInnerService.getSchoolDepartmentList(corpId,"");

        //查数据库获取人员

        return  result;

    }

    @RequestMapping("/school/user_list")
    @ResponseBody
    public Map userList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //通过code获取信息

        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");

        //写死测试
        Map result = qywxInnerService.getSchoolUserList(corpId,"1","1");

        //查数据库获取人员

        return  result;

    }

}
