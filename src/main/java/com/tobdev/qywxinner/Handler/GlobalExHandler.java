package com.tobdev.qywxinner.Handler;

import com.tobdev.qywxinner.utils.JsonData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExHandler {
    @ExceptionHandler(value = Exception.class)
    JsonData handleException(Exception e, HttpServletRequest request){
       return JsonData.buildError("全局异常");
    }
}
