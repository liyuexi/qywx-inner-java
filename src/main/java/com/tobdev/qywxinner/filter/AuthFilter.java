package com.tobdev.qywxinner.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tobdev.qywxinner.utils.JsonData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpRequest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebFilter(urlPatterns = "/api/v1/pri/*",filterName = "authFilter")
public class AuthFilter  implements Filter {

    private static final ObjectMapper objectMapper= new ObjectMapper();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest)servletRequest;
        HttpServletResponse resp = (HttpServletResponse)servletResponse;
        String token = req.getHeader("token");
        if(StringUtils.isEmpty(token)){
            token = req.getParameter("token");
        }
        if(!StringUtils.isEmpty(token)){
            filterChain.doFilter(servletRequest,servletResponse);
        }

        String json =  objectMapper.writeValueAsString(JsonData.buildError("登录失败"));
        renderJson(resp,json);
    }

    @Override
    public void destroy() {

    }

    private void renderJson(HttpServletResponse response,String json){
        response.setCharacterEncoding("UTF-8");
        response.setContentType("apllication/json");
        try(PrintWriter writer = response.getWriter()) {
            writer.print(json);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
