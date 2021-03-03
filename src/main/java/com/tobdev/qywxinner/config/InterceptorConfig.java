package com.tobdev.qywxinner.config;

import com.tobdev.qywxinner.intercepter.H5LoginIntercepter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 拦截器配置
 *
 */

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Bean
    H5LoginIntercepter h5LoginIntercepter(){
        return new H5LoginIntercepter();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(h5LoginIntercepter()).addPathPatterns("/*/pri/*","/contact/*","/extcontact/*","/message/*","/media/*","/oa/*","/school/*")
        .excludePathPatterns("/h5","/h5/index","/front/oauth","/school/oauth_callback");
        WebMvcConfigurer.super.addInterceptors(registry);
    }

}
