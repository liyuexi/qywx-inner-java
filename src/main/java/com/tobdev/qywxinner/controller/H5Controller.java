package com.tobdev.qywxinner.controller;

import com.tobdev.qywxinner.service.QywxInnerService;
import com.tobdev.qywxinner.utils.CommonUtils;
import com.tobdev.qywxinner.utils.CookieUtils;
import com.tobdev.qywxinner.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * H5应用专用
 */
@Controller
public class H5Controller {

    @Autowired
    private QywxInnerService qywxInnerService;


    @RequestMapping("/h5")
    void qywx(HttpServletResponse response) throws IOException {
        //先判断是否登录
        response.sendRedirect("https://tobdev.ant-xy.com");
    }

    @RequestMapping({"/h5/index"})
    String index(HttpServletRequest request, ModelMap model){
        //先判断是否登录
        //没有登录去登录
        String oauthRedirectUrl = CommonUtils.RouteToUrl(request,"/h5/oauth_callback");
        String oauthUrl = QywxInnerService.getOauthUrl(oauthRedirectUrl);
        model.put("oauth_url",oauthUrl);

        //没有登录去登录
        String schoolOauthRedirectUrl = CommonUtils.RouteToUrl(request,"/school/oauth_callback");
        String schoolOauthUrl = QywxInnerService.getSchoolOauthUrl(schoolOauthRedirectUrl);
        model.put("school_oauth_url",schoolOauthUrl);

        return  "h5/index";
    }

    @RequestMapping("/h5/oauth")
    public void oauth(HttpServletRequest request,HttpServletResponse response) throws IOException {

        String oauthRedirectUrl = CommonUtils.RouteToUrl(request,"/h5/oauth_callback");
        String oauthUrl = QywxInnerService.getOauthUrl(oauthRedirectUrl);
       // return  new ModelAndView(new RedirectView(oauthUrl));
        response.sendRedirect(oauthUrl);

    }



    @RequestMapping("/h5/oauth_callback")
    public void oauthCallback(HttpServletRequest request,HttpServletResponse response,@RequestParam("code") String code) throws IOException {
        //通过code获取信息
        Map result = QywxInnerService.getOauthUser(code);
        //查数据库获取人员

        //人员已侦破产生token登录  //本案例仅从企业微信接口获取未从数据表中获取
        QywxThirdUser user = new QywxThirdUser();
        user.setCorpId((String) result.get("corpid"));
        user.setUserId((String) result.get("userid"));
        user.setUserType(0);
        user.setName((String) result.get("name"));
        user.setAvatar((String) result.get("avatar"));
        String token=  JWTUtils.geneJsonWebToken(user);

//        result.put("token",token);

        //本案例写入cookie并跳转
        CookieUtils.setCookie(response,"token",token,24*60*60);

        String priIndexUrl = CommonUtils.RouteToUrl(request,"/h5/pri/index");
        response.sendRedirect(priIndexUrl);

    }


    @RequestMapping("/h5/pri/index")
    String home(HttpServletRequest request,ModelMap  model) throws Exception {

        //当前登录身份
        String userId = (String) request.getAttribute("user_id");
        String corpId = (String) request.getAttribute("corp_id");

        System.out.println(userId);
        System.out.println(corpId);

        model.put("user_id",userId);
        model.put("corp_id",corpId);


        model.put("access_token",qywxInnerService.getAccessToken(corpId));


        String jssdkUrl = CommonUtils.RouteToUrl(request,"/h5/pri/jssdk");
        model.put("jssdk_url",jssdkUrl);

        String contactUrl = CommonUtils.RouteToUrl(request,"/contact/index");
        model.put("contact_url",contactUrl);

        String extcontactUrl = CommonUtils.RouteToUrl(request,"/extcontact/index");
        model.put("extcontact_url",extcontactUrl);

        String messageUrl = CommonUtils.RouteToUrl(request,"/message/index");
        model.put("message_url",messageUrl);

        String mediaUrl = CommonUtils.RouteToUrl(request,"/media/index");
        model.put("media_url",mediaUrl);

        String oaUrl = CommonUtils.RouteToUrl(request,"/oa/index");
        model.put("oa_url",oaUrl);

        return "h5/pri/index";

    }

    @RequestMapping("/h5/pri/jssdk")
    String test(HttpServletRequest request,ModelMap  model) throws Exception {


        String userId = (String) request.getAttribute("user_id");
        model.addAttribute("userId",userId);

        String  timestamp=""+System.currentTimeMillis();
        //随机字符串
        String nonce = "56565";
        String url = CommonUtils.RouteToUrl(request,"/h5/pri/jssdk");
        String corpId = (String ) request.getAttribute("corp_id");

        Map signConig = qywxInnerService.getJsSign(corpId,nonce,timestamp,url);
        model.addAttribute("signConfig",signConig);

        Map signAgentConig = QywxInnerService.getJsSignAgent(corpId,nonce,timestamp,url);
        System.out.println(signAgentConig);
        model.addAttribute("signAgentConfig",signAgentConig);


//        model.addAttribute("xx","sdffdf");
//        model.addObject("sign",result);
//        model.setViewName("front/test");
        return "h5/pri/jssdk";

    }


    @RequestMapping("/h5/pri/jsSign")
    @ResponseBody()
    public Map getJsSign(HttpServletRequest request,@RequestParam("url") String url) throws Exception{
        //获取当前时间戳
        String  timestamp=""+System.currentTimeMillis();
        //随机字符串
        String nonce = "sdfsdf";
        String corp_id = (String) request.getAttribute("corp_id");

        Map result = qywxInnerService.getJsSign(corp_id,nonce,timestamp,url);

        return  result;
    }


}
