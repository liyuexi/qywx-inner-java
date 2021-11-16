package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

@RestController
public class EfficiencyController {

    @Autowired
    private QywxInnerService qywxInnerService;


    @PostMapping({"/eff/addCalendar"})
    JsonData addCalendar(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map ) throws IOException {

        String corpId = (String) map.get("corp_id");
        String summary = (String) map.get("summary");
        String color = (String) map.get("color");
        String organizeUserId = (String) map.get("organizer");
        ArrayList shareUserArr = (ArrayList) map.get("shares");

        Map resData = qywxInnerService.addCalendar(corpId,summary,color,organizeUserId,shareUserArr);
        return  JsonData.buildSuccess(resData);

    }

    @RequestMapping("/eff/getCalendar")
    public Map getCalendar(HttpServletRequest request,@RequestParam("corp_id") String corpId, @RequestParam("calendar_id") String calendarId){
        return  qywxInnerService.getCalendar(corpId,calendarId);
    }

    @PostMapping({"/eff/addSchedule"})
    JsonData addSchedule(HttpServletRequest request, HttpServletResponse response, @RequestBody Map map ) throws IOException {
        String corpId = (String) map.get("corp_id");
        String organizeUserId = (String) map.get("organize_user_id");
        ArrayList attendUserArr = (ArrayList) map.get("attends");
        String summary = (String) map.get("summary");
        String calendarId =(String) map.get("calendar_id");
        Integer startTime =(Integer) map.get("start_time");
        Integer endTime =(Integer) map.get("end_time");

        Map resData = qywxInnerService.addSchedule(corpId,organizeUserId,attendUserArr,summary,startTime,endTime,calendarId);
        return  JsonData.buildSuccess(resData);
    }



    @RequestMapping("/eff/getScheduleList")
    public Map getScheduleList(HttpServletRequest request,@RequestParam("corp_id") String corpId, @RequestParam("calendar_id") String calendarId){
        return  qywxInnerService.getScheduleList(corpId,calendarId,"0","10");
    }

    @GetMapping({"/eff/getSchedule"})
    public JsonData getSchedule(HttpServletRequest request, HttpServletResponse response, @RequestParam("corp_id") String corpId , @RequestParam("schedule_id") String scheduleId) throws IOException {
        Map resData = qywxInnerService.getSchedule(corpId,scheduleId);
        return  JsonData.buildSuccess(resData);
    }

    @RequestMapping("/eff/addMeeting")
    public JsonData addMeeting(HttpServletRequest request, @RequestBody Map map){
        String corpId = (String) map.get("corp_id");
        String organizeUserId = (String) map.get("organize_user_id");
        ArrayList attendUserArr = (ArrayList) map.get("attends");
        String title = (String) map.get("title");
        String type =(String) map.get("type");
        String startTime =(String) map.get("start_time");
        String duration =(String) map.get("duration");
        Map resData = qywxInnerService.addMeeting(corpId,organizeUserId,attendUserArr,title,startTime,duration,type);
        return  JsonData.buildSuccess(resData);
    }


    @GetMapping({"/eff/getMeetingDetail"})
    JsonData getMeetingDetail(HttpServletRequest request, HttpServletResponse response, @RequestParam("corp_id") String corpId ,@RequestParam("metting_id") String mettingId ) throws IOException {
        Map resData = qywxInnerService.getMeetingDetail(corpId,mettingId);
        return  JsonData.buildSuccess(resData);
    }

    @RequestMapping("/eff/getUserMeeting")
    public Map getUserMeeting(HttpServletRequest request,@RequestParam("corp_id") String corpId, @RequestParam("user_id") String userId){
        return  qywxInnerService.getUserMeeting(corpId,userId);
    }

}
