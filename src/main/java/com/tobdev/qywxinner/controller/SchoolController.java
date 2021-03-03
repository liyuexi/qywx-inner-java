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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping(produces = "application/json; charset=utf-8")
public class SchoolController {

    @Autowired
    private QywxInnerService qywxInnerService;


    @RequestMapping({"/school/index"})
    String index(HttpServletRequest request, ModelMap model){

        //没有登录去登录
        String schoolOauthRedirectUrl = CommonUtils.RouteToUrl(request,"/school/oauth_callback");
        String schoolOauthUrl = QywxInnerService.getSchoolOauthUrl(schoolOauthRedirectUrl);
        model.put("school_oauth_url",schoolOauthUrl);

        String schoolDepartmentUrl = CommonUtils.RouteToUrl(request,"/school/department_list");
        model.put("school_department_url",schoolDepartmentUrl);

        String schoolUserUrl = CommonUtils.RouteToUrl(request,"/school/user_list");
        model.put("school_user_url",schoolUserUrl);

        String schoolMessageUrl = CommonUtils.RouteToUrl(request,"/school/messageSend");
        model.put("school_message_url",schoolMessageUrl);


        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");

        model.put("access_token",qywxInnerService.getAccessToken(corpId));


        model.put("user_id",userId);
        model.put("corp_id",corpId);


        return  "school/index";
    }

    @RequestMapping("/school/oauth_callback")
    @ResponseBody
    public void oauthCallback(HttpServletRequest request, HttpServletResponse response, @RequestParam("code") String code) throws IOException {
        //通过code获取信息
        Map result = QywxInnerService.getSchoolOauthUser(code);
        System.out.println(result);
        //查数据库获取人员
        //人员已侦破产生token登录  //本案例仅从企业微信接口获取未从数据表中获取
        QywxInnerUser user = new QywxInnerUser();
        user.setCorpId((String) result.get("corpid"));
        user.setUserId((String) result.get("userid"));
        user.setUserType((Integer) result.get("user_Type"));
        user.setName((String) result.get("name"));
        user.setAvatar((String) result.get("avatar"));
        String token=  JWTUtils.geneJsonWebToken(user);

//        result.put("token",token);

        //本案例写入cookie并跳转
        CookieUtils.setCookie(response,"token",token,24*60*60);

        String priIndexUrl = CommonUtils.RouteToUrl(request,"/school/index");
        response.sendRedirect(priIndexUrl);

    }


    @RequestMapping("/school/messageSend")
    @ResponseBody
    public Map messageSend(HttpServletRequest request, HttpServletResponse response){
        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");

        String studentUserid = "90789833e153546ab8dbba1ab0d5f066";
        return  QywxInnerService.extContactMessageSend(corpId,studentUserid,"发送文本测试消息");
    }

    @RequestMapping("/school/department_list")
    @ResponseBody
    public Map departmentList(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");


        Map result = QywxInnerService.getSchoolDepartmentList(corpId,"");

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
        Map result = QywxInnerService.getSchoolUserList(corpId,"1","1");

        //查数据库获取人员

        return  result;

    }

}
