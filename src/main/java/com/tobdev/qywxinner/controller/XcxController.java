package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.service.QywxInnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


/**
 * 小程序应用专用
 */
@Controller
public class XcxController {

    @Autowired
    private QywxInnerService qywxInnerService;


//    @RequestMapping("/xcx/index")
//    @ResponseBody()
//    public Map index(){
//
//        return  0;
//    }


    @RequestMapping("/xcx/login")
    @ResponseBody()
    public Map oauthCallback(HttpServletRequest request, @RequestParam("code") String code){
        //通过code获取信息
        String corpId = (String) request.getAttribute("corp_id");
        Map result = qywxInnerService.getCode2sessionUser(corpId,code);
        return  result;

    }

}
