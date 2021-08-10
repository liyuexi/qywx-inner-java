package com.tobdev.qywxinner.intercepter;

import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonIntercepter implements HandlerInterceptor {

        @Override
        public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
          //方式三 跨域
//        //表示接受任意域名的请求,也可以指定域名
//        response.setHeader("Access-Control-Allow-Origin", request.getHeader("origin"));
//
//        //该字段可选，是个布尔值，表示是否可以携带cookie
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//
//        response.setHeader("Access-Control-Allow-Methods", "GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS");
//
//        response.setHeader("`Access-Control-Allow-Headers", "*");

//        if(HttpMethod.OPTIONS.toString().equals(request.getMethod())){
//                return  true;
//        }



            //写死公司id
//            request.setAttribute("corp_id","wwe58c8eb857ded23d");
            return  true;

        }

        @Override
        public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView
        modelAndView) throws Exception {

        }

        @Override
        public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        }

}
