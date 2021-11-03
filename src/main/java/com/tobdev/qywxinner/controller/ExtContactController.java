package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.service.QywxInnerService;

import com.tobdev.qywxinner.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Map;

/**
 * 通讯录相关，H5及小程序应用公用
 */
@RestController
public class ExtContactController {

    @Autowired
    private QywxInnerService qywxInnerService;

    @RequestMapping("/extcontact/followUserList")
    @ResponseBody()
    public Map getFollerList(HttpServletRequest request, @RequestParam("corp_id") String corpId) throws Exception{

        return  qywxInnerService.getExtContactFollowUserList(corpId);

    }

    @RequestMapping("/extcontact/list")
    @ResponseBody()
    public Map getList(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam(value = "user_id") String userId) throws Exception{

        return  qywxInnerService.getExtContactList(corpId,userId);

    }

    @RequestMapping("/extcontact/detail")
    @ResponseBody()
    public Map getDetail(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam(value = "ext_userid") String extUserId) throws Exception{

        return  qywxInnerService.getExtContactDetail(corpId,extUserId);

    }

    @RequestMapping("/extcontact/groupChatlist")
    @ResponseBody()
    public Map getGroupList(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam(value = "user_id") String userId) throws Exception{

        return  qywxInnerService.getExtContactGroupchatList(corpId,userId);
    }

    @RequestMapping("/extcontact/groupChatDetail")
    @ResponseBody()
    public Map getGroupDetail(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam(value = "chat_id") String chatId) throws Exception{

        return  qywxInnerService.getExtContactGroupchatDetail(corpId,chatId);
    }

    @RequestMapping("/extcontact/sendWelcomeMs")
    @ResponseBody()
    public Map sendWelcomeMs(HttpServletRequest request,@RequestParam("corp_id") String corpId,@RequestParam(value = "welcome_code") String welcomeCode,@RequestParam(value = "text") String text) throws Exception{

        return  qywxInnerService.sendWelcomeMsg(corpId,welcomeCode,text);
    }

    @RequestMapping("/extcontact/addMsgTemplatea")
    @ResponseBody()
    public Map addMsgTemplatea(HttpServletRequest request, @RequestBody Map map  ) throws Exception{

        String userId =  (String)map.get("user_id");
        String extUserId =  (String)map.get("ext_userid");
        String corpId =  (String)map.get("corp_id");
        String text =  (String)map.get("text");

        ArrayList extUserList = new ArrayList<>();
        extUserList.add(extUserId);

        return  qywxInnerService.addMsgTemplate(corpId,extUserList,userId,text);
    }


}
