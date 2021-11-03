package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.service.QywxInnerService;


import com.tobdev.qywxinner.utils.CommonUtils;
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
import java.io.OutputStream;
import java.util.Map;

@RestController
public class OaController {

    @Autowired
    private QywxInnerService qywxInnerService;


    @RequestMapping({"/oa/aprrovalFlowInit"})
    JsonData getAprrovalTemplate(HttpServletRequest request, HttpServletResponse response, @RequestParam("corp_id") String corpId ) throws IOException {
        //审批流程
        Map resData = qywxInnerService.getAprrovalFlowInit(corpId);
        return  JsonData.buildSuccess(resData);
    }



    @RequestMapping("/oa/approvalFlowStatus")
    @ResponseBody
    public Map getAprrovalFlowStatus(HttpServletRequest request,@RequestParam("corp_id") String corpId, @RequestParam("third_no") String thirdNo){
        return  qywxInnerService.getApprovalFlowStatus(corpId,thirdNo);
    }



}
