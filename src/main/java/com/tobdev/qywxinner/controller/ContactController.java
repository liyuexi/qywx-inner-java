package com.tobdev.qywxinner.controller;


import com.tobdev.qywxinner.service.QywxInnerService;


import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 通讯录相关，H5及小程序应用公用
 */
@RestController
public class ContactController {

    @Autowired
    private QywxInnerService qywxInnerService;


    @RequestMapping("/contact/useDetail")
    public JsonData useDetail(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam(value = "user_id") String UserId){
        Map resData =    qywxInnerService.getUserDetail(corpId,UserId);
        return  JsonData.buildSuccess(resData);
    }

    @RequestMapping("/contact/departmentList")
    //@RequestParam(value = "id",defaultValue = "0") String id
    public JsonData department(HttpServletRequest request,@RequestParam("corp_id") String corpId){
        Map resData =   qywxInnerService.getDepartmentList(corpId);
        return  JsonData.buildSuccess(resData);
    }

    @RequestMapping("/contact/deptUserList")
    @ResponseBody
    public JsonData detpUserList(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam(value = "department_id") String departmentId){
        Map resData =    qywxInnerService.getUserSimplelist(corpId,departmentId,"0");
        return  JsonData.buildSuccess(resData);
    }

    @RequestMapping("/contact/deptUserDetailList")
    @ResponseBody
    public JsonData detpUseDetailrList(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam(value = "department_id") String departmentId){
        Map resData =    qywxInnerService.getUserDetailList(corpId,departmentId,"0");
        return  JsonData.buildSuccess(resData);
    }

}
