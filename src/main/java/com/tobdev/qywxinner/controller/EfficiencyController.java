package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
public class EfficiencyController {

    @Autowired
    private QywxInnerService qywxInnerService;


//    @PostMapping({"/oa/addCalendar"})
//    JsonData addCalendar(HttpServletRequest request, HttpServletResponse response ) throws IOException {
//        String corpId,String summary,String color,String organizeUserId,String shareUserId
//
//        Map resData = qywxInnerService.addCalendar(corpId);
//        return  JsonData.buildSuccess(resData);
//    }

    @RequestMapping("/oa/getCalendar")
    @ResponseBody
    public Map getCalendar(HttpServletRequest request,@RequestParam("corp_id") String corpId, @RequestParam("third_no") String thirdNo){
        return  qywxInnerService.getApprovalFlowStatus(corpId,thirdNo);
    }

    @PostMapping({"/oa/addSchedule"})
    JsonData addSchedule(HttpServletRequest request, HttpServletResponse response, @RequestParam("corp_id") String corpId ) throws IOException {
        Map resData = qywxInnerService.getAprrovalFlowInit(corpId);
        return  JsonData.buildSuccess(resData);
    }

    @RequestMapping("/oa/getScheduleList")
    @ResponseBody
    public Map getScheduleList(HttpServletRequest request,@RequestParam("corp_id") String corpId, @RequestParam("third_no") String thirdNo){
        return  qywxInnerService.getApprovalFlowStatus(corpId,thirdNo);
    }

    @PostMapping({"/oa/getSchedule"})
    JsonData getSchedule(HttpServletRequest request, HttpServletResponse response, @RequestParam("corp_id") String corpId ) throws IOException {
        Map resData = qywxInnerService.getAprrovalFlowInit(corpId);
        return  JsonData.buildSuccess(resData);
    }

    @RequestMapping("/oa/addMeeting")
    @ResponseBody
    public Map addMeeting(HttpServletRequest request,@RequestParam("corp_id") String corpId, @RequestParam("third_no") String thirdNo){
        return  qywxInnerService.getApprovalFlowStatus(corpId,thirdNo);
    }


    @PostMapping({"/oa/getMeetingDetail"})
    JsonData getMeetingDetail(HttpServletRequest request, HttpServletResponse response, @RequestParam("corp_id") String corpId ) throws IOException {
        Map resData = qywxInnerService.getAprrovalFlowInit(corpId);
        return  JsonData.buildSuccess(resData);
    }

    @RequestMapping("/oa/getUserMeeting")
    @ResponseBody
    public Map getUserMeeting(HttpServletRequest request,@RequestParam("corp_id") String corpId, @RequestParam("third_no") String thirdNo){
        return  qywxInnerService.getApprovalFlowStatus(corpId,thirdNo);
    }

}
