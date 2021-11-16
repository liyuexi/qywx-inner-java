package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 小程序应用专用
 */
@Controller
public class XcxController {

    @Autowired
    private QywxInnerService qywxInnerService;


    @RequestMapping("/test")
    @ResponseBody()
    JsonData index(){
        HashMap resData = new HashMap();
        resData.put("data","sdfdsf");
        return   JsonData.buildSuccess(resData);
    }


    @RequestMapping("/xcx/login")
    @ResponseBody()
    public Map oauthCallback(HttpServletRequest request,@RequestParam("corp_id") String corpId, @RequestParam("code") String code){
        //通过code获取信息
        Map result = qywxInnerService.getCode2sessionUser(corpId,code);
        return  result;

    }

}
