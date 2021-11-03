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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
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
        //Map result = qywxInnerService.getSchoolOauthUser(code);

        //System.out.println(result);
        //查数据库获取人员
        //人员已侦破产生token登录  //本案例仅从企业微信接口获取未从数据表中获取
        QywxInnerUser user = new QywxInnerUser();
        user.setCorpId((String) result.get("corpid"));
        user.setUserId((String) result.get("userid"));
        user.setUserType((Integer) result.get("user_Type"));
        user.setName((String) result.get("name"));
        user.setAvatar((String) result.get("avatar"));

        String token=  JWTUtils.geneJsonWebToken(user);
        result.put("token",token);

        return  JsonData.buildSuccess(result);

    }

    @RequestMapping("/school/subscribeQr")
    @ResponseBody
    JsonData  subscribeQr(HttpServletRequest request,HttpServletResponse response,@RequestParam("corp_id") String corpId) throws IOException {
        Map result = qywxInnerService.getSubscribeQr(corpId);
        return  JsonData.buildSuccess(result);
    }

    @PostMapping("/school/sendText")
    @ResponseBody()
    public JsonData sendtext(HttpServletRequest request, @RequestBody Map map ) throws Exception{

        String corpId = (String) map.get("corp_id");
        String toParentUserId =  (String)map.get("to_parent_userid");
        String toStudentUserId =  (String)map.get("to_student_userid");
        String text =  (String)map.get("text");
        Map resData = qywxInnerService.sendExtContactMessageText(corpId,toParentUserId,toStudentUserId,text);
        return  JsonData.buildSuccess(resData);

    }

    @PostMapping("/school/sendImage")
    @ResponseBody()
    public JsonData sendImage(HttpServletRequest request, @RequestBody Map map ) throws Exception{

        String corpId = (String) map.get("corp_id");
        String toParentUserId =  (String)map.get("to_parent_userid");
        String toStudentUserId =  (String)map.get("to_student_userid");
        String MediaId =  (String)map.get("media_id");
        Map resData = qywxInnerService.sendExtContactMessageImage(corpId,toParentUserId,toStudentUserId,MediaId);
        return  JsonData.buildSuccess(resData);

    }


    @RequestMapping("/school/departmentList")
    @ResponseBody
    public Map departmentList(HttpServletRequest request, HttpServletResponse response,@RequestParam("corp_id") String corpId,@RequestParam(value = "dept_id",defaultValue = "") String deptId) throws IOException {

        Map result = qywxInnerService.getSchoolDepartmentList(corpId,deptId);
        return  result;

    }


    @RequestMapping("/school/deptUserList")
    @ResponseBody
    public JsonData detpUserList(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam(value = "department_id") String departmentId){
        Map resData =    qywxInnerService.getSchoolUserList(corpId,departmentId,"0");
        return  JsonData.buildSuccess(resData);
    }


    @RequestMapping("/school/useDetail")
    public JsonData useDetail(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam(value = "user_id") String UserId){
        Map resData =    qywxInnerService.getSchoolUserDetail(corpId,UserId);
        return  JsonData.buildSuccess(resData);
    }


}
