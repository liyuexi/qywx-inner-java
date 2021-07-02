package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.service.QywxInnerService;

import com.tobdev.qywxinner.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 通讯录相关，H5及小程序应用公用
 */
@Controller
public class ExtContactController {

    @Autowired
    private QywxInnerService qywxInnerService;




    @RequestMapping("/extcontact/index")
    public String index(HttpServletRequest request,ModelMap model) throws Exception{

        String follerListUrl = CommonUtils.RouteToUrl(request,"/extcontact/follerList");
        model.put("follerlist_url",follerListUrl);

        String listUrl = CommonUtils.RouteToUrl(request,"/extcontact/list");
        model.put("list_url",listUrl);

        String grouplistUlr = CommonUtils.RouteToUrl(request,"/extcontact/grouplist");
        model.put("grouplist_url",grouplistUlr);

        return  "extcontact/index";

    }


    @RequestMapping("/extcontact/follerList")
    @ResponseBody()
    public Map getFollerList(HttpServletRequest request) throws Exception{

        String corpId = (String) request.getAttribute("corp_id");
        String userId = (String) request.getAttribute("user_id");
        return  qywxInnerService.getExtContactFollowUserList(corpId);

    }

    @RequestMapping("/extcontact/list")
    @ResponseBody()
    public Map getList(HttpServletRequest request) throws Exception{

        String corpId = (String) request.getAttribute("corp_id");
        String userId = (String) request.getAttribute("user_id");
        return  qywxInnerService.getExtContactList(corpId,userId);

    }

    @RequestMapping("/extcontact/grouplist")
    @ResponseBody()
    public Map getGroupList(HttpServletRequest request) throws Exception{

        String corpId = (String) request.getAttribute("corp_id");
        String userId = (String) request.getAttribute("user_id");
        return  qywxInnerService.getExtContactGroupchatList(corpId,userId);

    }


}
