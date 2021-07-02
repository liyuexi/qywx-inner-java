package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.service.QywxInnerService;


import com.tobdev.qywxinner.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
public class OaController {

    @Autowired
    private QywxInnerService qywxInnerService;


    @RequestMapping("/oa/index")
    String index(HttpServletRequest request, ModelMap model) throws Exception {


        String userId = (String) request.getAttribute("user_id");
        model.addAttribute("userId",userId);

        String  timestamp=""+System.currentTimeMillis();
        //随机字符串
        String nonce = "56565";
        //客户传参当前url  当前测试写死
        String url = CommonUtils.RouteToUrl(request,"/oa/index");
        String corpId = (String ) request.getAttribute("corp_id");

        Map signConig = qywxInnerService.getJsSign(corpId,nonce,timestamp,url);
        model.addAttribute("signConfig",signConig);

        Map signAgentConig = qywxInnerService.getJsSignAgent(corpId,nonce,timestamp,url);
        System.out.println(signAgentConig);
        model.addAttribute("signAgentConfig",signAgentConig);

        //审批流程
        Map approval = qywxInnerService.getApprovalFlow();
        System.out.println(approval.toString());
        model.addAttribute("templateId" ,  approval.get("templateId"));
        model.addAttribute("thirdNo"  ,  approval.get("thirdNo"));


        return "oa/index";

    }


    @RequestMapping("/oa/flowStatus")
    @ResponseBody
    public Map getAprrovalFlowStatus(HttpServletRequest request, @RequestParam("third_no") String thirdNo){
        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");

        return  qywxInnerService.getApprovalFlowStatus(corpId,thirdNo);
    }


}
