package com.tobdev.qywxinner.controller;

import com.alibaba.excel.EasyExcel;
import com.tobdev.qywxinner.model.excel.QywxContact;
import com.tobdev.qywxinner.service.QywxInnerService;

import com.tobdev.qywxinner.service.impl.QywxThirdUserServiceImpl;
import com.tobdev.qywxinner.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通讯录相关，H5及小程序应用公用
 */
@Controller
public class ContactController {

    @Autowired
    private QywxInnerService qywxInnerService;


    @RequestMapping("/contact/index")
    String index(HttpServletRequest request,ModelMap model){

        String corpId = (String) request.getAttribute("corp_id");
        model.put("corp_access_token",qywxInnerService.getAccessToken(corpId));

        String jssdkUrl = CommonUtils.RouteToUrl(request,"/contact/department");
        model.put("dept_url",jssdkUrl);

        String userUrl = CommonUtils.RouteToUrl(request,"/contact/user");
        model.put("user_url",userUrl);

        String opendataUrl = CommonUtils.RouteToUrl(request,"/contact/opendata");
        model.put("opendata_url",opendataUrl);

        String transUrl = CommonUtils.RouteToUrl(request,"/contact/trans");
        model.put("trans_url",transUrl);


        return  "contact/index";

    }

    @RequestMapping("/contact/department")
    @ResponseBody
    //@RequestParam(value = "id",defaultValue = "0") String id
    Map department(HttpServletRequest request){
        String corpId = (String) request.getAttribute("corp_id");
        return  qywxInnerService.getDepartmentList(corpId);
    }

    @RequestMapping("/contact/user")
    @ResponseBody
    Map user(HttpServletRequest request,@RequestParam(value = "department_id") String departmentId){
        String corpId = (String) request.getAttribute("corp_id");
        return  qywxInnerService.getUserSimplelist(corpId,departmentId,"0");
    }





}
