package com.tobdev.qywxinner.controller;

import com.alibaba.fastjson.JSONObject;
import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

@RestController
public class AgentController {

    @Autowired
    private QywxInnerService qywxInnerService;


    @RequestMapping({"/agent/get"})
    JsonData getAgent(HttpServletRequest request, @RequestParam("corp_id") String corpId){
        Map resData =   qywxInnerService.getAgent(corpId);
        return    JsonData.buildSuccess(resData);

    }

    @PostMapping({"/agent/set"})
    JsonData setAgent(HttpServletRequest request , @RequestBody Map map){

        String corpId = (String) map.get("corp_id");

        String name =  (String)map.get("name");
        String desc =  (String)map.get("description");
        JSONObject postJson = new JSONObject();
        postJson.put("name",name);
        postJson.put("description",desc);
        Map resData =   qywxInnerService.setAgent(corpId,postJson);
        return    JsonData.buildSuccess(resData);
    }


}
