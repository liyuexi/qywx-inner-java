package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.model.entity.QywxInnerUser;
import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.JWTUtils;
import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * 小程序应用专用
 */
@RestController
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
    public JsonData login(HttpServletRequest request,@RequestParam("corp_id") String corpId, @RequestParam("code") String code){
        //通过code获取信息
        Map result = qywxInnerService.getCode2sessionUser(corpId,code);

        QywxInnerUser user = new QywxInnerUser();
        user.setCorpId(corpId);
        user.setUserId((String) result.get("userid"));
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

}
